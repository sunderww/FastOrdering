using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
using Windows.Networking;
using SocketIO = Quobject.SocketIoClientDotNet.Client.Socket;
using Quobject.EngineIoClientDotNet.ComponentEmitter;
using Windows.Networking.Sockets;
using Windows.Storage.Streams;
using Windows.UI.Xaml;
using System.Net;
using FastOrdering.Model;
using Newtonsoft.Json;
using System.IO;
using Windows.Data.Json;
using Quobject.SocketIoClientDotNet.Client;
using Newtonsoft.Json.Linq;
using Windows.Web.Http;

namespace FastOrdering.Misc
{
	public class Socket
	{
		static private SocketIO socket;

		static public void SendOrder(Order order)
		{
			order.PrepareOrder();
			string str = JsonConvert.SerializeObject(order);
			System.Diagnostics.Debug.WriteLine(str);
			socket.Emit("send_order", new AckImpl((data) =>
			{
				System.Diagnostics.Debug.WriteLine("works");
				System.Diagnostics.Debug.WriteLine(data.ToString());
				ReceiveOrder(data.ToString());
			}), str);
		}

		static public void Authentication(string key)
		{
			JObject jsonObject = new JObject();
			jsonObject["user_key"] = key;
			socket.Emit("authentication", new AckImpl((data) =>
			{
				System.Diagnostics.Debug.WriteLine(data.ToString());
			}), jsonObject);
		}

		private void GetNotification(string str)
		{
			//string str = "{\"numTable\": \"1\", \"msg\": \"egre erger fwef ewf\", \"date\": \"12/12/12\", \"hour\": \"12:12\"}";

			Notification.notifications.Add(JsonConvert.DeserializeObject<Notification>(str));
		}

		static private void ReceiveOrder(string str)
		{
			//string str = "{\"numOrder\": \"2\", \"numTable\": \"5\", \"numPA\": \"1\", \"date\": \"01/01/2001\", \"hour\": \"12:12\"}";
			Order.orders.Add(JsonConvert.DeserializeObject<Order>(str));
		}

		private async Task GetLastOrders()
		{
			string str = await GetaString("order");
			if (str == null || str == "")
				return;
			//string str = "{\"orders\": [{\"numOrder\": \"1\", \"numTable\": \"7\", \"numPA\": \"3\", \"date\": \"01/01/2001\", \"hour\": \"12:12\"}]}";

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

		static public Order GetOrder(string idOrder)
		{
			return null;
			JObject jsonObject = new JObject();
			jsonObject["order"] = idOrder;
			socket.Emit("get_order", new AckImpl((data) =>
			{
				System.Diagnostics.Debug.WriteLine(data.ToString());
			}), jsonObject);

			return null;

			string str = "";
			dynamic output = JsonConvert.DeserializeObject(str);
			string numOrder = (string)output["numOrder"];
			int numTable = (int)output["numOrder"];
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
								foreach (KeyValuePair<string, int> op in dict.Key.options)
								{
									if (op.Key == (string)optionOut["id"])
									{
										int nb = dict.Value + (int)optionOut["qty"];
										dict.Key.options.Remove(op.Key);
										dict.Key.options.Add(op.Key, nb);
										break;
									}
								}
								//dict.Key.options. Add((string)optionOut["id"], (int)optionOut["qty"]);
							}
							dict.Key.status = (int)contentOut["status"];
							menu.Dishes.Add(dict);
							break;
						}
					}
					//foreach (MyDictionary<Dish> dishes in menu.Dishes)
					//{
					//	if (id == dishes.Key.ID)
					//	{
					//		dishes.Value = (int)contentOut["qty"];
					//		dishes.Key.comment = (string)contentOut["comment"];
					//		foreach (Object option in contentOut["options"])
					//		{
					//			str = option.ToString();
					//			dynamic optionOut = JsonConvert.DeserializeObject(str);
					//			dishes.Key.options.Add((string)optionOut["id"], (int)optionOut["qty"]);
					//		}
					//		//dishes.Key.options = (string)contentOut["options"];
					//		dishes.Key.status = (int)contentOut["status"];
					//		break;
					//	}
					//}
				}
			}
			Order.orders.Add(ord);
			return ord;
		}

		private async Task GetMenus()
		{
			string str = await GetaString("menus");
			if (str == null || str == "")
				return;
			//string str = "{\"elements\": [{\"name\": \"Délices\", \"createdAt\": \"2016-05-05T03:31:12.211Z\", \"updatedAt\": \"2016-05-05T03:31:12.211Z\", \"id\": \"572abe8049bb4c97702057db\"}, ";
			//str += "{\"name\": \"alacarte\", \"createdAt\": \"2016-05-08T17:31:17.702Z\", \"updatedAt\": \"2016-05-08T17:31:17.702Z\", \"id\": \"572f77e5e4e081cc7a7006d2\"}, ";
			//str += "{\"name\": \"Gourmet\", \"createdAt\": \"2016-05-08T17:40:48.815Z\", \"updatedAt\": \"2016-05-08T17:40:48.815Z\", \"id\": \"572f7a20937726dc7ab8f903\"}]}";

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
			//string str = "[{\"name\": \"Entrées_alacarte\", \"colorString\": \"16777215\", \"createdAt\": \"2016-05-05T03:29:21.278Z\", \"updatedAt\": \"2016-05-05T03:29:21.278Z\", \"id\": \"572abe1149bb4c97702057d8\"}, ";
			//str += "{\"name\": \"Plats_alacarte\", \"colorString\": \"16777215\", \"createdAt\": \"2016-05-05T03:29:30.359Z\", \"updatedAt\": \"2016-05-05T03:29:30.359Z\", \"id\": \"572abe1a49bb4c97702057d9\"}, ";
			//str += "{\"name\": \"Desserts_alacarte\", \"colorString\": \"16777215\", \"createdAt\": \"2016-05-05T03:29:36.854Z\", \"updatedAt\": \"2016-05-05T03:29:36.854Z\", \"id\": \"572abe2049bb4c97702057da\"}, ";
			//str += "{\"name\": \"Entrées_menu_delice\", \"colorString\": \"16777215\", \"createdAt\": \"2016-05-08T17:33:55.672Z\", \"updatedAt\": \"2016-05-08T17:33:55.672Z\", \"id\": \"572f7883937726dc7ab8f8ef\"}, ";
			//str += "{\"colorString\": \"16777215\", \"createdAt\": \"2016-05-05T03:27:45.694Z\", \"name\": \"Plats_menu_delice\", \"updatedAt\": \"2016-05-08T17:34:03.901Z\", \"id\": \"572abdb149bb4c97702057d5\"}, ";
			//str += "{\"colorString\": \"16777215\", \"createdAt\": \"2016-05-05T03:27:52.345Z\", \"name\": \"Desserts_menu_delice\", \"updatedAt\": \"2016-05-08T17:34:09.770Z\", \"id\": \"572abdb849bb4c97702057d6\"}, ";
			//str += "{\"name\": \"Entrées_menu_gourmet\", \"colorString\": \"16777215\", \"createdAt\": \"2016-05-08T17:37:54.032Z\",  \"updatedAt\": \"2016-05-08T17:37:54.032Z\", \"id\": \"572f7972937726dc7ab8f8f8\"}, ";
			//str += "{\"name\": \"Plats_menu_gourmet\", \"colorString\": \"16777215\", \"createdAt\": \"2016-05-08T17:38:03.304Z\", \"updatedAt\": \"2016-05-08T17:38:03.304Z\", \"id\": \"572f797b937726dc7ab8f8f9\"}, ";
			//str += "{\"name\": \"Desserts_menu_gourmet\", \"colorString\": \"16777215\", \"createdAt\": \"2016-05-08T17:38:13.213Z\", \"updatedAt\": \"2016-05-08T17:38:13.213Z\", \"id\": \"572f7985937726dc7ab8f8fa\"}]";

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
			//string str = "{\"elements\": [{\"id\": \"572f78d9937726dc7ab8f8f2\", \"name\": \"Crottin de chèvre frais sur pomme\", \"price\": 0, \"categories_ids\": [\"572f7883937726dc7ab8f8ef\"], \"available\": true, \"options\" : [\"234335t43\"], \"createdAt\": \"2016-05-08T17:35:21.100Z\", \"updatedAt\": \"2016-05-08T17:35:21.100Z\"}, ";
			//str += "{\"available\": true, \"categories_ids\": [\"572abdb149bb4c97702057d5\", \"572f797b937726dc7ab8f8f9\"], \"createdAt\": \"2016-05-08T17:35:47.075Z\", \"id\": \"572f78f3937726dc7ab8f8f4\", \"name\": \"Plat végétarien \", \"price\": 10, \"options\" : [\"234335t43\"], \"updatedAt\": \"2016-05-08T17:51:12.064Z\"}]}";

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
			//string str = "{\"elements\": {\"id\": \"572f77e5e4e081cc7a7006d2\", \"name\": \"alacarte\", \"compo\": [\"572f7929937726dc7ab8f8f6\", \"572f793f937726dc7ab8f8f7\", \"572f7a3f937726dc7ab8f904\", \"572f7d3fd3a349ab7b1d860e\", \"572f7d4bd3a349ab7b1d860f\", \"572f7d56d3a349ab7b1d8610\"]}}";

			dynamic output = JsonConvert.DeserializeObject(str);
			var tab = (output["elements"])["compo"];
			Menu.alacarte = new Menu((string)(output["elements"])["name"], DateTime.Now, DateTime.Now, (string)(output["elements"])["id"]);
			//foreach (Menu m in Menu.menus)
			//{
			//	if (m.menuId == (string)((output["elements"])["id"]))
			//	{
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
			//		break;
			//	}
			//}
		}

		private async Task GetCompos()
		{
			string str = await GetaString("compos");
			if (str == null || str == "")
				return;
			//string str = "{\"elements\": [{\"name\": \"Entrée et plat\", \"price\": 12, \"menu_id\": \"572abe8049bb4c97702057db\", \"categories_ids\": [\"572abdb149bb4c97702057d5\", \"572f7883937726dc7ab8f8ef\"], \"createdAt\": \"2016-05-08T17:36:41.009Z\", \"updatedAt\": \"2016-05-08T17:36:41.009Z\", \"id\": \"572f7929937726dc7ab8f8f6\"}, ";
			//str += "{\"name\": \"Plat et dessert\", \"price\": 10, \"menu_id\": \"572abe8049bb4c97702057db\", \"categories_ids\": [\"572abdb149bb4c97702057d5\", \"572abe2049bb4c97702057da\"], \"createdAt\": \"2016-05-08T17:37:03.596Z\", \"updatedAt\": \"2016-05-08T17:37:03.596Z\", \"id\": \"572f793f937726dc7ab8f8f7\"}, ";
			//str += "{\"name\": \"Unique\", \"price\": 20, \"menu_id\": \"572f7a20937726dc7ab8f903\", \"categories_ids\": [\"572f7985937726dc7ab8f8fa\", \"572f797b937726dc7ab8f8f9\", \"572f7972937726dc7ab8f8f8\"], \"createdAt\": \"2016-05-08T17:41:19.131Z\", \"updatedAt\": \"2016-05-08T17:41:19.131Z\", \"id\": \"572f7a3f937726dc7ab8f904\"}, ";
			//str += "{\"name\": \"Desserts\", \"price\": 0, \"menu_id\": \"572f77e5e4e081cc7a7006d2\", \"categories_ids\": [\"572abe2049bb4c97702057da\"], \"createdAt\": \"2016-05-08T17:54:07.621Z\", \"updatedAt\": \"2016-05-08T17:54:07.621Z\", \"id\": \"572f7d3fd3a349ab7b1d860e\"}, ";
			//str += "{\"name\": \"Plats\", \"price\": 0, \"menu_id\": \"572f77e5e4e081cc7a7006d2\", \"categories_ids\": [\"572abe1a49bb4c97702057d9\"],\"createdAt\": \"2016-05-08T17:54:19.635Z\", \"updatedAt\": \"2016-05-08T17:54:19.635Z\", \"id\": \"572f7d4bd3a349ab7b1d860f\"}, ";
			//str += "{\"name\": \"Entrées\", \"price\": 0, \"menu_id\": \"572f77e5e4e081cc7a7006d2\", \"categories_ids\": [\"572abe1149bb4c97702057d8\"], \"createdAt\": \"2016-05-08T17:54:30.982Z\", \"updatedAt\": \"2016-05-08T17:54:30.982Z\", \"id\": \"572f7d56d3a349ab7b1d8610\"}]}";

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
			//string str = "{\"elements\": [{\"id\": \"234335t43\", \"values\": [{\"name\": \"bleue\", \"id\": \"324r434\"}]}]}";

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

		public async Task<string> GetaString(string path)
		{
			string str = "";
			try
			{
				HttpClient httpClient = new HttpClient();
				httpClient.DefaultRequestHeaders.Accept.TryParseAdd("application/json");
				str = await httpClient.GetStringAsync(new Uri("http://163.5.84.184:4242/" + path));
			}
			catch (Exception ex)
			{
				System.Diagnostics.Debug.WriteLine(ex.Message);
			}
			return str;
		}

		public Socket()
		{
			Connect_Click();
			socket = Quobject.SocketIoClientDotNet.Client.IO.Socket("http://163.5.84.184:4242");
			socket.On("notifications", (object data) =>
			{
				System.Diagnostics.Debug.WriteLine(data.ToString());
				GetNotification(data.ToString());
			});
			socket.On("receive_order", (object data) =>
			{
				System.Diagnostics.Debug.WriteLine(data.ToString());
			});
			socket.On("get_order", (object data) =>
			{
				System.Diagnostics.Debug.WriteLine(data.ToString());
			});
			socket.On("authentication", (object data) =>
			{
				System.Diagnostics.Debug.WriteLine(data.ToString());
			});
			return;
		}

		private void Disconnect()
		{
			socket.Disconnect();
		}

		private async void Connect_Click()
		{
			await GetMenus();
			await GetCategories();
			//GetOptions();
			await GetDishes();
			await GetCompos();
			await GetLastOrders();
			await GetAlacarte();
			return;
			//GetOrder();
			//SendOrder();
			//ReceiveOrder();
			//GetNotification();
		}
	}
}
