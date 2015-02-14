using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using SocketIO = Quobject.SocketIoClientDotNet.Client.Socket;
using Quobject.EngineIoClientDotNet.ComponentEmitter;

namespace FastOrdering.Misc {
	public class Socket {
		private SocketIO socket;
		private string uri = "http://alexis-semren.com:1337";
		public string ret = "";

		public Socket(string eventString) {
			socket = Quobject.SocketIoClientDotNet.Client.IO.Socket(uri);
			//Emitter em = socket.Emit(eventString);
			Emitter emit = socket.Emit(eventString, (data) => {
				ret = data.ToString();
				System.Diagnostics.Debug.WriteLine(data);
				socket.Disconnect();
			});
		}
	}
}
