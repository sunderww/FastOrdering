using FastOrdering.Misc;
using Quobject.EngineIoClientDotNet.ComponentEmitter;
using Quobject.SocketIoClientDotNet.Client;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Input;

namespace FastOrdering.ViewModel {
	public class LoginViewModel : ViewModelBase {
		public ICommand Connect { get; private set; }

		public LoginViewModel() {
			Connect = new RelayCommand(ConnectUser);
		}

		private void ConnectUser() {
			System.Diagnostics.Debug.WriteLine("toto");
		}
	}
}
