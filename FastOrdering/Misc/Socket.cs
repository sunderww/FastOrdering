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

		private void GetNotification()
		{
			string str = "{\"numTable\": \"1\", \"msg\": \"egre erger fwef ewf\", \"date\": \"12/12/12\", \"hour\": \"12:12\"}";

			Notification.notifications.Add(JsonConvert.DeserializeObject<Notification>(str));
		}

		private void ReceiveOrder()
		{
			string str = "{\"numOrder\": \"1\", \"numTable\": \"7\", \"numPA\": \"2\", \"date\": \"01/01/2001\", \"hour\": \"12:12\"}";

			Order.orders.Add(JsonConvert.DeserializeObject<Order>(str));
		}

		private void GetOrder()
		{
			// pas terminé
			string str = "{\"numOrder\": \"1\", \"numTable\": \"7\", \"numPA\": \"2\", \"date\": \"01/01/2001\", \"hour\": \"12:12\", \"globalComment\": \"blablabla\""+
				"\"order\": [{\"menuId\": \"2\", \"content\": [{\"id\": \"id_dish\", \"qty\": \"2\", \"comment\": \"blabla\", \"status\": \"0\", \"options\": \"\"}],}]}";

			Order.orders.Add(JsonConvert.DeserializeObject<Order>(str));
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

		//public Socket()
		//{
		//	clientSocket = new StreamSocket();
		//	socket = Quobject.SocketIoClientDotNet.Client.IO.Socket("http://163.5.84.184:4242");
		//	Connect_Click();
		//}

		private async void Connect_Click()
		{
			GetNotification();
			return;

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

		public Socket()
		{
			Connect_Click();
			//await sock.ConnectAsync("http://alexis-semren.com", "1337");
			System.Diagnostics.Debug.WriteLine("begin");
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
		}

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
