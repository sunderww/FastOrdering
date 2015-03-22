﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using SocketIO = Quobject.SocketIoClientDotNet.Client.Socket;
using Quobject.EngineIoClientDotNet.ComponentEmitter;

namespace FastOrdering.Misc
{
	public class Socket
	{
		private SocketIO socket;
		private string uri = "http://alexis-semren.com:1337";
		public string ret = "";

		public Socket()
		{
			System.Diagnostics.Debug.WriteLine("begin");
			socket = Quobject.SocketIoClientDotNet.Client.IO.Socket(this.uri);
			socket.On(SocketIO.EVENT_CONNECT, () =>
			{
				socket.Emit("hi");
				System.Diagnostics.Debug.WriteLine("emit");
			});
			socket.On("hi", (data) =>
			{
				System.Diagnostics.Debug.WriteLine("cb");
				System.Diagnostics.Debug.WriteLine(data);
				socket.Disconnect();
			});
			System.Diagnostics.Debug.WriteLine("end");
			//socket.Connect();
		}

		public string Emit(string eventString)
		{
			//Emitter em = socket.Emit(eventString);
			Emitter emit = socket.Emit(eventString, (data) =>
			{
				this.ret = data.ToString();
				System.Diagnostics.Debug.WriteLine(data);
				System.Diagnostics.Debug.WriteLine("test");
			});
			return this.ret;
		}

		public void Disconnect()
		{
			socket.Disconnect();
		}
	}
}
