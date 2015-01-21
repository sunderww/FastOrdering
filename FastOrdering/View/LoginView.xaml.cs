using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Runtime.InteropServices.WindowsRuntime;
using Windows.Foundation;
using Windows.Foundation.Collections;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Controls.Primitives;
using Windows.UI.Xaml.Data;
using Windows.UI.Xaml.Input;
using Windows.UI.Xaml.Media;
using Windows.UI.Xaml.Navigation;
using Quobject.SocketIoClientDotNet.Client;
using Quobject.EngineIoClientDotNet.ComponentEmitter;

// The Blank Page item template is documented at http://go.microsoft.com/fwlink/?LinkID=390556

namespace FastOrdering.View {
	/// <summary>
	/// An empty page that can be used on its own or navigated to within a Frame.
	/// </summary>
	public sealed partial class LoginView : Page {
		public LoginView() {
			this.InitializeComponent();
			//socket = IO.Socket("http://alexis-semren.com:1337");
		}

		/// <summary>
		/// Invoked when this page is about to be displayed in a Frame.
		/// </summary>
		/// <param name="e">Event data that describes how this page was reached.
		/// This parameter is typically used to configure the page.</param>
		protected override void OnNavigatedTo(NavigationEventArgs e) {
		}

		Socket socket;

		private void AppBarButton_Click(object sender, RoutedEventArgs e) {
			//socket = IO.Socket("http://alexis-semren.com:1337");
			//Emitter em = socket.Emit("/dish/read");
			//Emitter emit = socket.Emit("/dish/read", (data) => {
			//	System.Diagnostics.Debug.WriteLine(data);
			//	socket.Disconnect();
			//});
			Frame.Navigate(typeof(ReceptionView));
		}
	}
}
