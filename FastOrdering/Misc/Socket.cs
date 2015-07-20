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

namespace FastOrdering.Misc
{
	public class Socket
	{

		static public void SendOrder(Order order)
		{
			order.PrepareOrder();
			string str = JsonConvert.SerializeObject(order);
			System.Diagnostics.Debug.WriteLine(str);
		}

		private void GetNotification()
		{
			string str = "{\"numTable\": \"1\", \"msg\": \"egre erger fwef ewf\", \"date\": \"12/12/12\", \"hour\": \"12:12\"}";

			Notification.notifications.Add(JsonConvert.DeserializeObject<Notification>(str));
		}

		private void ReceiveOrder()
		{
			string str = "{\"numOrder\": \"2\", \"numTable\": \"5\", \"numPA\": \"1\", \"date\": \"01/01/2001\", \"hour\": \"12:12\"}";

			Order.orders.Add(JsonConvert.DeserializeObject<Order>(str));
		}

		private void GetLastOrders()
		{
			string str = "{\"orders\": [{\"numOrder\": \"1\", \"numTable\": \"7\", \"numPA\": \"3\", \"date\": \"01/01/2001\", \"hour\": \"12:12\"}]}";

			dynamic output = JsonConvert.DeserializeObject(str);
			foreach (Object order in output["orders"])
			{
				string orderStr = order.ToString();
				Order.orders.Add(JsonConvert.DeserializeObject<Order>(orderStr));
			}
		}

		static public Order GetOrder()
		{
			Menu.menus.Add(new Menu("0", "Mousaillon", "Visible", "Visible"));
			Menu.menus.Add(new Menu("2", "Pirate", "Collapsed", "Collapsed"));

			string str = "{\"numOrder\": \"3\", \"numTable\": \"2\", \"numPA\": \"5\", \"date\": \"01/01/2001\", \"hour\": \"12:12\", \"globalComment\": \"blablabla\", " +
				"\"order\": [{\"menuId\": \"2\", \"content\": [{\"id\": \"1\", \"qty\": \"2\", \"comment\": \"blabla\", \"status\": \"0\", \"options\": \"\"}],}]}";

			dynamic output = JsonConvert.DeserializeObject(str);
			int numOrder = (int)output["numOrder"];
			int numTable = (int)output["numOrder"];
			int numPA = (int)output["numPA"];
			DateTime date = (DateTime)output["date"];
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
					foreach (MyDictionary<Dish> dishes in menu.Dishes)
					{
						if (id == dishes.Key.ID)
						{
							dishes.Value = (int)contentOut["qty"];
							dishes.Key.comment = (string)contentOut["comment"];
							dishes.Key.options = (string)contentOut["options"];
							dishes.Key.status = (int)contentOut["status"];
							break;
						}
					}
				}
			}
			Order.orders.Add(ord);
			return ord;
		}

		private void GetMenus()
		{
			string str = "{\"elements\": [{\"name\": \"Délices\", \"createdAt\": \"2016-05-05T03:31:12.211Z\", \"updatedAt\": \"2016-05-05T03:31:12.211Z\", \"id\": \"572abe8049bb4c97702057db\"}, ";
			str += "{\"name\": \"alacarte\", \"createdAt\": \"2016-05-08T17:31:17.702Z\", \"updatedAt\": \"2016-05-08T17:31:17.702Z\", \"id\": \"572f77e5e4e081cc7a7006d2\"}, ";
			str += "{\"name\": \"Gourmet\", \"createdAt\": \"2016-05-08T17:40:48.815Z\", \"updatedAt\": \"2016-05-08T17:40:48.815Z\", \"id\": \"572f7a20937726dc7ab8f903\"}]}";

			dynamic ouput = JsonConvert.DeserializeObject(str);
			foreach (Object menu in ouput["elements"])
			{
				string menuStr = menu.ToString();
				Menu m = JsonConvert.DeserializeObject<Menu>(menuStr);
				if (m.Name != "alacarte")
					Menu.menus.Add(m);
			}
		}

		private void GetCategories()
		{
			string str = "{\"elements\": [{\"name\": \"Entrées_alacarte\", \"colorString\": \"16777215\", \"createdAt\": \"2016-05-05T03:29:21.278Z\", \"updatedAt\": \"2016-05-05T03:29:21.278Z\", \"id\": \"572abe1149bb4c97702057d8\"}, ";
			str += "{\"name\": \"Plats_alacarte\", \"colorString\": \"16777215\", \"createdAt\": \"2016-05-05T03:29:30.359Z\", \"updatedAt\": \"2016-05-05T03:29:30.359Z\", \"id\": \"572abe1a49bb4c97702057d9\"}, ";
			str += "{\"name\": \"Desserts_alacarte\", \"colorString\": \"16777215\", \"createdAt\": \"2016-05-05T03:29:36.854Z\", \"updatedAt\": \"2016-05-05T03:29:36.854Z\", \"id\": \"572abe2049bb4c97702057da\"}, ";
			str += "{\"name\": \"Entrées_menu_delice\", \"colorString\": \"16777215\", \"createdAt\": \"2016-05-08T17:33:55.672Z\", \"updatedAt\": \"2016-05-08T17:33:55.672Z\", \"id\": \"572f7883937726dc7ab8f8ef\"}, ";
			str += "{\"colorString\": \"16777215\", \"createdAt\": \"2016-05-05T03:27:45.694Z\", \"name\": \"Plats_menu_delice\", \"updatedAt\": \"2016-05-08T17:34:03.901Z\", \"id\": \"572abdb149bb4c97702057d5\"}, ";
			str += "{\"colorString\": \"16777215\", \"createdAt\": \"2016-05-05T03:27:52.345Z\", \"name\": \"Desserts_menu_delice\", \"updatedAt\": \"2016-05-08T17:34:09.770Z\", \"id\": \"572abdb849bb4c97702057d6\"}, ";
			str += "{\"name\": \"Entrées_menu_gourmet\", \"colorString\": \"16777215\", \"createdAt\": \"2016-05-08T17:37:54.032Z\",  \"updatedAt\": \"2016-05-08T17:37:54.032Z\", \"id\": \"572f7972937726dc7ab8f8f8\"}, ";
			str += "{\"name\": \"Plats_menu_gourmet\", \"colorString\": \"16777215\", \"createdAt\": \"2016-05-08T17:38:03.304Z\", \"updatedAt\": \"2016-05-08T17:38:03.304Z\", \"id\": \"572f797b937726dc7ab8f8f9\"}, ";
			str += "{\"name\": \"Desserts_menu_gourmet\", \"colorString\": \"16777215\", \"createdAt\": \"2016-05-08T17:38:13.213Z\", \"updatedAt\": \"2016-05-08T17:38:13.213Z\", \"id\": \"572f7985937726dc7ab8f8fa\"}]}";

			dynamic ouput = JsonConvert.DeserializeObject(str);
			foreach (Object categories in ouput["elements"])
			{
				string categoriesStr = categories.ToString();
				Category.categories.Add(JsonConvert.DeserializeObject<Category>(categoriesStr));
			}
		}

		private void GetDishes()
		{
			string str = "{\"elements\": [{\"id\": \"572f78d9937726dc7ab8f8f2\", \"name\": \"Crottin de chèvre frais sur pomme\", \"price\": 0, \"categories_ids\": [\"572f7883937726dc7ab8f8ef\"], \"available\": true, \"createdAt\": \"2016-05-08T17:35:21.100Z\", \"updatedAt\": \"2016-05-08T17:35:21.100Z\"}, ";
			str += "{\"available\": true, \"categories_ids\": [\"572abdb149bb4c97702057d5\", \"572f797b937726dc7ab8f8f9\"], \"createdAt\": \"2016-05-08T17:35:47.075Z\", \"id\": \"572f78f3937726dc7ab8f8f4\", \"name\": \"Plat végétarien \", \"price\": 10, \"updatedAt\": \"2016-05-08T17:51:12.064Z\"}]}";

			dynamic output = JsonConvert.DeserializeObject(str);
			foreach (Object dish in output["elements"])
			{
				string dishStr = dish.ToString();
				Dish.dishes.Add(JsonConvert.DeserializeObject<Dish>(dishStr));
			}
		}

		private void GetAlacarte()
		{
			string str = "{\"elements\": {\"id\": \"572f77e5e4e081cc7a7006d2\", \"name\": \"alacarte\", \"compo\": [\"572f7929937726dc7ab8f8f6\", \"572f793f937726dc7ab8f8f7\", \"572f7a3f937726dc7ab8f904\", \"572f7d3fd3a349ab7b1d860e\", \"572f7d4bd3a349ab7b1d860f\", \"572f7d56d3a349ab7b1d8610\"]}}";

			dynamic output = JsonConvert.DeserializeObject(str);
			var tab = (output["elements"])["compo"];
			Menu.alacarte = new Menu((string)(output["elements"])["name"], DateTime.Now, DateTime.Now, (string)(output["elements"])["id"]);
			//foreach (Menu m in Menu.menus)
			//{
			//	if (m.menuId == (string)((output["elements"])["id"]))
			//	{
			foreach (Composition compo in Composition.compositions)
			{
				foreach (string id in tab)
				{
					if (id == compo.ID)
					{
						Menu.alacarte.Compositions.Add(compo);
						break;
					}
				}
			}
			//		break;
			//	}
			//}
		}

		private void GetCompos()
		{
			string str = "{\"elements\": [{\"name\": \"Entrée et plat\", \"price\": 12, \"menu_id\": \"572abe8049bb4c97702057db\", \"categories_ids\": [\"572abdb149bb4c97702057d5\", \"572f7883937726dc7ab8f8ef\"], \"createdAt\": \"2016-05-08T17:36:41.009Z\", \"updatedAt\": \"2016-05-08T17:36:41.009Z\", \"id\": \"572f7929937726dc7ab8f8f6\"}, ";
			str += "{\"name\": \"Plat et dessert\", \"price\": 10, \"menu_id\": \"572abe8049bb4c97702057db\", \"categories_ids\": [\"572abdb149bb4c97702057d5\", \"572abe2049bb4c97702057da\"], \"createdAt\": \"2016-05-08T17:37:03.596Z\", \"updatedAt\": \"2016-05-08T17:37:03.596Z\", \"id\": \"572f793f937726dc7ab8f8f7\"}, ";
			str += "{\"name\": \"Unique\", \"price\": 20, \"menu_id\": \"572f7a20937726dc7ab8f903\", \"categories_ids\": [\"572f7985937726dc7ab8f8fa\", \"572f797b937726dc7ab8f8f9\", \"572f7972937726dc7ab8f8f8\"], \"createdAt\": \"2016-05-08T17:41:19.131Z\", \"updatedAt\": \"2016-05-08T17:41:19.131Z\", \"id\": \"572f7a3f937726dc7ab8f904\"}, ";
			str += "{\"name\": \"Desserts\", \"price\": 0, \"menu_id\": \"572f77e5e4e081cc7a7006d2\", \"categories_ids\": [\"572abe2049bb4c97702057da\"], \"createdAt\": \"2016-05-08T17:54:07.621Z\", \"updatedAt\": \"2016-05-08T17:54:07.621Z\", \"id\": \"572f7d3fd3a349ab7b1d860e\"}, ";
			str += "{\"name\": \"Plats\", \"price\": 0, \"menu_id\": \"572f77e5e4e081cc7a7006d2\", \"categories_ids\": [\"572abe1a49bb4c97702057d9\"],\"createdAt\": \"2016-05-08T17:54:19.635Z\", \"updatedAt\": \"2016-05-08T17:54:19.635Z\", \"id\": \"572f7d4bd3a349ab7b1d860f\"}, ";
			str += "{\"name\": \"Entrées\", \"price\": 0, \"menu_id\": \"572f77e5e4e081cc7a7006d2\", \"categories_ids\": [\"572abe1149bb4c97702057d8\"], \"createdAt\": \"2016-05-08T17:54:30.982Z\", \"updatedAt\": \"2016-05-08T17:54:30.982Z\", \"id\": \"572f7d56d3a349ab7b1d8610\"}]}";

			dynamic output = JsonConvert.DeserializeObject(str);
			foreach (Object compo in output["elements"])
			{
				str = compo.ToString();
				Composition.compositions.Add(JsonConvert.DeserializeObject<Composition>(str));
			}
		}

		private SocketIO socket;

		private StreamSocket clientSocket;
		private HostName serverHost;
		private string serverHostnameString = "163.5.84.184";
		//private string serverHostnameString = "127.0.0.1";
		private string serverPort = "4242";
		//private string serverPort = "27017";
		private bool connected = false;
		private bool closing = false;

		public Socket()
		{
			clientSocket = new StreamSocket();
			socket = Quobject.SocketIoClientDotNet.Client.IO.Socket("http://163.5.84.184:4242");
			socket.On(SocketIO.EVENT_CONNECT_ERROR, (data) =>
				{
					System.Diagnostics.Debug.WriteLine(data);
				});
			socket.On(SocketIO.EVENT_CONNECT, () =>
			{
				socket.Emit("/dish/read");
				System.Diagnostics.Debug.WriteLine("emit");
			});
			socket.On("/dish/read", (data) =>
			{
				System.Diagnostics.Debug.WriteLine("emits");
				System.Diagnostics.Debug.WriteLine(data);
				socket.Disconnect();
			});
			Emitter emit = socket.Emit("/dish/read", (data) =>
			{
				System.Diagnostics.Debug.WriteLine(data);
				System.Diagnostics.Debug.WriteLine("test");
			});
			System.Diagnostics.Debug.WriteLine("end");
			Connect_Click();
		}

		private async void Connect_Click()
		{
			GetOrder();
			//SendOrder();
			GetLastOrders();
			ReceiveOrder();
			GetNotification();
			GetMenus();
			GetCategories();
			GetDishes();
			GetCompos();
			GetAlacarte();
			//return;

			if (connected)
			{
				return;
			}

			try
			{
				serverHost = new HostName(serverHostnameString);
				// Try to connect to the 
				await clientSocket.ConnectAsync(serverHost, serverPort).AsTask();
				//		await _clientSocket.ConnectAsync(remoteHost, "27017").AsTask(cancellationToken.Token);
				connected = true;
			}
			catch (Exception exception)
			{
				// If this is an unknown status, 
				// it means that the error is fatal and retry will likely fail.
				if (SocketError.GetStatus(exception.HResult) == SocketErrorStatus.Unknown)
				{
					throw;
				}

				// Could retry the connection, but for this simple example
				// just close the socket.

				closing = true;
				// the Close method is mapped to the C# Dispose
				clientSocket.Dispose();
				clientSocket = null;

			}
			Send_Click();
		}

		private async void Send_Click()
		{
			if (!connected)
			{
				return;
			}

			uint len = 0; // Gets the UTF-8 string length.

			System.Diagnostics.Debug.WriteLine("begin write");

			try
			{
				// add a newline to the text to send
				string sendData = "/dish/read" + Environment.NewLine;
				DataWriter writer = new DataWriter(clientSocket.OutputStream);
				len = writer.MeasureString(sendData); // Gets the UTF-8 string length.
				writer.WriteString(sendData);

				// Call StoreAsync method to store the data to a backing stream
				await writer.StoreAsync();
				await writer.FlushAsync();

				// detach the stream and close it
				writer.DetachStream();
				writer.Dispose();

			}
			catch (Exception exception)
			{
				// If this is an unknown status, 
				// it means that the error is fatal and retry will likely fail.
				if (SocketError.GetStatus(exception.HResult) == SocketErrorStatus.Unknown)
				{
					throw;
				}

				// Could retry the connection, but for this simple example
				// just close the socket.

				closing = true;
				clientSocket.Dispose();
				clientSocket = null;
				connected = false;

			}

			// Now try to receive data from server

			System.Diagnostics.Debug.WriteLine("begin read");

			try
			{

				DataReader reader = new DataReader(clientSocket.InputStream);
				// Set inputstream options so that we don't have to know the data size
				reader.InputStreamOptions = InputStreamOptions.Partial;
				System.Diagnostics.Debug.WriteLine("loadasync1");
				await reader.LoadAsync(2048);
				System.Diagnostics.Debug.WriteLine("loadasync2");
				string data = "";
				while (reader.UnconsumedBufferLength > 0)
				{
					uint toread = reader.ReadUInt32();
					data = reader.ReadString(toread);
				}
				System.Diagnostics.Debug.WriteLine(data);
			}
			catch (Exception exception)
			{
				// If this is an unknown status, 
				// it means that the error is fatal and retry will likely fail.
				if (SocketError.GetStatus(exception.HResult) == SocketErrorStatus.Unknown)
				{
					throw;
				}

				// Could retry, but for this simple example
				// just close the socket.

				closing = true;
				clientSocket.Dispose();
				clientSocket = null;
				connected = false;

			}
			System.Diagnostics.Debug.WriteLine("end");
		}

		private StreamSocket sock = new StreamSocket();
		//private SocketIO socket;
		//private string uri = "http://alexis-semren.com:1337";
		//private string uri = "http://163.5.84.184:4242/";
		//public string ret = "";

		//private StreamSocket _clientSocket = new StreamSocket();
		//private bool _connected = false;
		//private DataReader _dataReader;
		//public readonly string Channel;

		//public Socket()
		//{
		//	Connect();
		//}

		//public async void Connect()
		//{
		//	//StreamSocket tcpSocket = new StreamSocket();
		//	//HostName remoteHost = new HostName("127.0.0.1");
		//	HostName remoteHost = new HostName("163.5.84.184");
		//	//HostName remoteHost = new HostName("alexis-semren.com");
		//	CancellationTokenSource cancellationToken = new CancellationTokenSource();

		//	cancellationToken.CancelAfter(5000);
		//	try
		//	{
		//		await _clientSocket.ConnectAsync(remoteHost, "4242").AsTask(cancellationToken.Token);
		//		//await tcpSocket.ConnectAsync(remoteHost, "1337").AsTask(cancellationToken.Token);
		//		//await _clientSocket.ConnectAsync(remoteHost, "27017").AsTask(cancellationToken.Token);
		//		_connected = true;
		//	}
		//	catch (Exception ex)
		//	{
		//		var socketError = SocketError.GetStatus(ex.HResult);
		//	}
		//	SendRawMessage("/dish/read");
		//	ReadData();
		//	_clientSocket.Dispose();
		//	//if (_connected) return false;
		//	//var hostname = new HostName("www.alexis-semren.com");
		//	//var hostname = new HostName("127.0.0.1");
		//	//await _clientSocket.ConnectAsync(hostname, "1337");
		//	//try
		//	//{
		//	//	await _clientSocket.ConnectAsync(hostname, "27017");
		//	//	_connected = true;
		//	//	ReadData();
		//	//}
		//	//catch (Exception exception)
		//	//{
		//	//	if (SocketError.GetStatus(exception.HResult) == SocketErrorStatus.Unknown)
		//	//	{
		//	//		throw;
		//	//	}
		//	//	System.Diagnostics.Debug.WriteLine("Connect failed with error: " + exception.Message);
		//	//	_clientSocket.Dispose();
		//	//}
		//	//return true;
		//}

		//async public void ReadData()
		//{
		//	try
		//	{
		//		System.Diagnostics.Debug.WriteLine("begin");
		//		if (!_connected || _clientSocket == null) return;
		//		_dataReader = new DataReader(_clientSocket.InputStream);
		//		_dataReader.InputStreamOptions = InputStreamOptions.Partial;
		//		uint s = await _dataReader.LoadAsync(2048);
		//		string data = _dataReader.ReadString(s);
		//		System.Diagnostics.Debug.WriteLine(data);
		//		ReadData();
		//		System.Diagnostics.Debug.WriteLine("end");
		//	}
		//	catch (Exception exception)
		//	{
		//		if (SocketError.GetStatus(exception.HResult) == SocketErrorStatus.Unknown)
		//		{
		//			throw;
		//		}
		//		System.Diagnostics.Debug.WriteLine("Receive failed with error: " + exception.Message);
		//		_clientSocket.Dispose();
		//		_connected = false;

		//	}
		//}

		//async public void SendRawMessage(string message)
		//{
		//	try
		//	{
		//		var writer = new DataWriter(_clientSocket.OutputStream);
		//		writer.WriteString(message + "\r\n");
		//		uint len = writer.MeasureString(message + Environment.NewLine);
		//		await writer.StoreAsync();
		//		//await writer.FlushAsync();
		//		writer.DetachStream();
		//		writer.Dispose();
		//	}
		//	catch (Exception exception)
		//	{
		//		if (SocketError.GetStatus(exception.HResult) == SocketErrorStatus.Unknown)
		//		{
		//			throw;
		//		}
		//		System.Diagnostics.Debug.WriteLine("Send data failed with error: " + exception.Message);
		//		_clientSocket.Dispose();
		//		_connected = false;

		//	}
		//}

		//public Socket()
		//{
		//	Connect_Click();
		//	//await sock.ConnectAsync("http://alexis-semren.com", "1337");
		//	System.Diagnostics.Debug.WriteLine("begin");
		//	socket = Quobject.SocketIoClientDotNet.Client.IO.Socket("http://163.5.84.184:4242");

		//	socket.On(SocketIO.EVENT_CONNECT_ERROR, (data) =>
		//		{
		//			System.Diagnostics.Debug.WriteLine(data);
		//		});
		//	socket.On(SocketIO.EVENT_CONNECT, () =>
		//	{
		//		socket.Emit("/dish/read");
		//		System.Diagnostics.Debug.WriteLine("emit");
		//	});
		//	socket.On("/dish/read", (data) =>
		//	{
		//		System.Diagnostics.Debug.WriteLine("emits");
		//		System.Diagnostics.Debug.WriteLine(data);
		//		socket.Disconnect();
		//	});
		//	Emitter emit = socket.Emit("/dish/read", (data) =>
		//	{
		//		System.Diagnostics.Debug.WriteLine(data);
		//		System.Diagnostics.Debug.WriteLine("test");
		//	});
		//	System.Diagnostics.Debug.WriteLine("end");
		//}

		//public void Connect()
		//{
		//	System.Diagnostics.Debug.WriteLine("begin");

		//	socket.On(SocketIO.EVENT_CONNECT_ERROR, (data) =>
		//	{
		//		System.Diagnostics.Debug.WriteLine("error");
		//		System.Diagnostics.Debug.WriteLine(data);
		//	});
		//	socket.On(SocketIO.EVENT_CONNECT, () =>
		//	{
		//		socket.Emit("/elements");
		//		System.Diagnostics.Debug.WriteLine("emit");
		//	});
		//	socket.On("/elements", (data) =>
		//	{
		//		System.Diagnostics.Debug.WriteLine("emits");
		//		System.Diagnostics.Debug.WriteLine(data);
		//	});
		//	System.Diagnostics.Debug.WriteLine("end");
		//}

		//public void Disconnect()
		//{
		//	//socket.Disconnect();
		//}

		//void callback()
		//{

		//}

		//public string Emit(string eventString)
		//{
		//	//Emitter em = socket.Emit(eventString);
		//	Emitter emit = socket.Emit(eventString, (data) =>
		//	{
		//		this.ret = data.ToString();
		//		System.Diagnostics.Debug.WriteLine(data);
		//		System.Diagnostics.Debug.WriteLine("test");
		//	});
		//	return this.ret;
		//}

		//public void Disconnect()
		//{
		//	socket.Disconnect();
		//}
	}
}
