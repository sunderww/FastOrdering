using FastOrdering.View;
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

// The User Control item template is documented at http://go.microsoft.com/fwlink/?LinkId=234236

namespace FastOrdering.Misc {
	public sealed partial class MyMenu : UserControl {
		public MyMenu() {
			this.InitializeComponent();
		}

		private void HyperlinkButton_Click(object sender, RoutedEventArgs e) {
			if (((HyperlinkButton)sender).Name == "Home")
				((Frame)Window.Current.Content).Navigate(typeof(ReceptionView));
			else if (((HyperlinkButton)sender).Name == "About")
				((Frame)Window.Current.Content).Navigate(typeof(AboutView));
			else if (((HyperlinkButton)sender).Name == "Ordering")
				((Frame)Window.Current.Content).Navigate(typeof(NewOrderView));
			//else if (((HyperlinkButton)sender).Name == "Options")
				//((Frame)Window.Current.Content).Navigate(typeof(OptionsView));
			else if (((HyperlinkButton)sender).Name == "Orders")
				((Frame)Window.Current.Content).Navigate(typeof(OrdersView));
			else if (((HyperlinkButton)sender).Name == "Notifications")
				((Frame)Window.Current.Content).Navigate(typeof(NotificationsView));
			//else if (((HyperlinkButton)sender).Name == "Logout")
			//	((Frame)Window.Current.Content).Navigate(typeof(OptionsView));
		}

	}
}
