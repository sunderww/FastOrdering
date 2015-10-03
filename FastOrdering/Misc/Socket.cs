using FastOrdering.Model;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
using Quobject.SocketIoClientDotNet.Client;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Windows.Data.Xml.Dom;
using Windows.UI.Core;
using Windows.UI.Notifications;
using Windows.Web.Http;
using SocketIO = Quobject.SocketIoClientDotNet.Client.Socket;

namespace FastOrdering.Misc
{
	public class Socket
	{
		#region Attributes
		private static SocketIO socket;
		private string url = "http://163.5.84.184:4242/";
		#endregion

		#region Methods
		public Socket()
		{
			GetData();
			socket = Quobject.SocketIoClientDotNet.Client.IO.Socket(url);
			socket.On("notifications", (object data) =>
			{
				System.Diagnostics.Debug.WriteLine(data.ToString());
				GetNotification(data.ToString());
			});
			return;
		}

		private async void GetData()
		{
			await GetMenus();
			await GetCategories();
			//GetOptions();
			await GetDishes();
			await GetCompos();
			await GetLastOrders();
			await GetAlacarte();
			return;
		}

		static public void Disconnect()
		{
			socket.Disconnect();
		}
		#endregion

		#region SendToServer
		static public void SendOrder(Order order)
		{
			order.PrepareOrder();
			string str = JsonConvert.SerializeObject(order);
			System.Diagnostics.Debug.WriteLine(str);
			socket.Emit("send_order", new AckImpl((data) =>
			{
				System.Diagnostics.Debug.WriteLine(data.ToString());
				Order.orders.Add(JsonConvert.DeserializeObject<Order>(data.ToString()));
			}), str);
		}

		static public void Authentication(string key, Func<bool, bool> connectUser)
		{
			JObject jsonObject = new JObject();
			jsonObject["user_key"] = key;
			socket.Emit("authentication", new AckImpl(async (data) =>
			{
				System.Diagnostics.Debug.WriteLine(data.ToString());
				dynamic ouput = JsonConvert.DeserializeObject(data.ToString());
				bool answer = (bool)ouput["answer"];
				await Windows.ApplicationModel.Core.CoreApplication.MainView.CoreWindow.Dispatcher.RunAsync(CoreDispatcherPriority.Normal, () =>
				{
					connectUser(answer);
				});
			}), jsonObject);
		}
		#endregion

		#region GetFromServer
		public async Task<string> GetaString(string path)
		{
			string str = "";
			try
			{
				HttpClient httpClient = new HttpClient();
				httpClient.DefaultRequestHeaders.Accept.TryParseAdd("application/json");
				str = await httpClient.GetStringAsync(new Uri(url + path));
			}
			catch (Exception ex)
			{
				System.Diagnostics.Debug.WriteLine(ex.Message);
			}
			return str;
		}

		private void GetNotification(string str)
		{
			Notification notif = JsonConvert.DeserializeObject<Notification>(str);
			Notification.notifications.Add(notif);
			var toastNotifier = ToastNotificationManager.CreateToastNotifier();
			var toastXml = ToastNotificationManager.GetTemplateContent(ToastTemplateType.ToastText01);
			var toastText = toastXml.GetElementsByTagName("text");
			(toastText[0] as XmlElement).InnerText = notif.Message;
			var toast = new ToastNotification(toastXml);
			toastNotifier.Show(toast);
		}

		private async Task GetLastOrders()
		{
			string str = await GetaString("order");
			if (str == null || str == "")
				return;

			dynamic output = JsonConvert.DeserializeObject(str);
			foreach (Object order in output)
			{
				string orderStr = order.ToString();
				Order newOrder = JsonConvert.DeserializeObject<Order>(orderStr);
				if (Order.orders.Count == 0 || newOrder.Time.CompareTo(Order.orders.First().Time) < 0)
					Order.orders.Add(newOrder);
				else
					Order.orders.Insert(0, newOrder);
			}
		}

		public void GetOrder(string idOrder, Func<Order, bool> showOrder)
		{
			JObject jsonObject = new JObject();
			jsonObject["order"] = idOrder;
			Order ord = null;
			socket.Emit("get_order", new AckImpl(async (data) =>
			{
				System.Diagnostics.Debug.WriteLine(data.ToString());
				ord = ParseGetOrder(data.ToString());
				await Windows.ApplicationModel.Core.CoreApplication.MainView.CoreWindow.Dispatcher.RunAsync(CoreDispatcherPriority.Normal, () =>
				{
					showOrder(ord);
				});
			}), jsonObject);
		}

		private async Task GetMenus()
		{
			string str = await GetaString("menus");
			if (str == null || str == "")
				return;

			dynamic ouput = JsonConvert.DeserializeObject(str);
			foreach (Object menu in ouput["elements"])
			{
				string menuStr = menu.ToString();
				Menu m = JsonConvert.DeserializeObject<Menu>(menuStr);
				if (m.Name != "alacarte")
					Menu.menus.Add(m);
			}
		}

		private async Task GetCategories()
		{
			string str = await GetaString("cats");
			if (str == null || str == "")
				return;

			dynamic ouput = JsonConvert.DeserializeObject(str);
			foreach (Object categories in ouput["elements"])
			{
				string categoriesStr = categories.ToString();
				Category.categories.Add(JsonConvert.DeserializeObject<Category>(categoriesStr));
			}
		}

		private async Task GetDishes()
		{
			string str = await GetaString("elements");
			if (str == null || str == "")
				return;

			dynamic output = JsonConvert.DeserializeObject(str);
			foreach (Object dish in output["elements"])
			{
				string dishStr = dish.ToString();
				Dish.dishes.Add(JsonConvert.DeserializeObject<Dish>(dishStr));
			}
		}

		private async Task GetAlacarte()
		{
			string str = await GetaString("alacarte");
			if (str == null || str == "")
				return;

			dynamic output = JsonConvert.DeserializeObject(str);
			var tab = (output["elements"])["compo"];
			Menu.alacarte = new Menu((string)(output["elements"])["name"], DateTime.Now, DateTime.Now, (string)(output["elements"])["id"]);
			foreach (Category cat in Category.categories)
			{
				foreach (string id in tab)
				{
					if (id == cat.ID)
					{
						Menu.alacarte.Categories.Add(cat);
						break;
					}
				}
				Menu.OrderALaCarte();
			}
		}

		private async Task GetCompos()
		{
			string str = await GetaString("compos");
			if (str == null || str == "")
				return;

			dynamic output = JsonConvert.DeserializeObject(str);
			foreach (Object compo in output["elements"])
			{
				str = compo.ToString();
				Composition.compositions.Add(JsonConvert.DeserializeObject<Composition>(str));
			}
		}

		private async Task GetOptions()
		{
			string str = await GetaString("option");
			if (str == null || str == "")
				return;

			dynamic output = JsonConvert.DeserializeObject(str);
			foreach (Object option in output["elements"])
			{
				str = option.ToString();
				dynamic optionOut = JsonConvert.DeserializeObject(str);
				string id = (string)optionOut["id"];
				Dictionary<string, string> values = new Dictionary<string, string>();
				foreach (Object value in optionOut["values"])
				{
					str = value.ToString();
					dynamic valueOut = JsonConvert.DeserializeObject(str);
					values.Add((string)valueOut["name"], (string)valueOut["id"]);
				}
				Option.options.Add(new Option(id, values));
			}
		}

		private Order ParseGetOrder(string str)
		{
			dynamic output = JsonConvert.DeserializeObject(str);
			string numOrder = (string)output["numOrder"];
			int numTable = (int)output["numTable"];
			int numPA = (int)output["numPA"];
			string date = (string)output["date"];
			DateTime hour = (DateTime)output["hour"];
			string globalComment = (string)output["globalComment"];
			Order ord = new Order(numOrder, numTable, numPA, date, hour);
			ord.GlobalComment = globalComment;
			foreach (Object order in output["order"])
			{
				string orderStr = order.ToString();
				dynamic orderOut = JsonConvert.DeserializeObject(orderStr);
				string menuId = (string)orderOut["menuId"];
				Menu menu = null;
				foreach (Menu m in Menu.menus)
				{
					if (m.IDMenu == menuId)
					{
						ord.Menus.Add(m);
						menu = ord.Menus.Last();
						break;
					}
				}
				if (menu == null && Menu.alacarte.IDMenu == menuId)
				{
					ord.Menus.Add(Menu.alacarte);
					menu = ord.Menus.Last();
				}
				foreach (Object content in orderOut["content"])
				{
					string contentStr = content.ToString();
					dynamic contentOut = JsonConvert.DeserializeObject(contentStr);
					string id = (string)contentOut["id"];
					foreach (Dish dish in Dish.dishes)
					{
						if (id == dish.ID)
						{
							MyDictionary<Dish> dict = new MyDictionary<Dish>();
							dict.Value = (int)contentOut["qty"];
							dict.Key = dish;
							dict.Key.comment = (string)contentOut["comment"];
							foreach (Object option in contentOut["options"])
							{
								str = option.ToString();
								dynamic optionOut = JsonConvert.DeserializeObject(str);
								foreach (Option op in dict.Key.options)
								{
									dict.Key.options.Add(op);
								}
							}
							dict.Key.status = (string)contentOut["status"];
							menu.Dishes.Add(dict);
							break;
						}
					}
				}
			}
			return ord;
		}
		#endregion
	}
}
