using FastOrdering.Model;
using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
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

// The Blank Page item template is documented at http://go.microsoft.com/fwlink/?LinkID=390556

namespace FastOrdering.View
{
	/// <summary>
	/// An empty page that can be used on its own or navigated to within a Frame.
	/// </summary>
	public sealed partial class ReceptionView : Page
	{
		public ObservableCollection<Notification> notifications;
		public ObservableCollection<Order> orders;

		public ReceptionView()
		{
			this.InitializeComponent();
			DrawerLayout.InitializeDrawerLayout();
			notifications = new ObservableCollection<Notification>();
			notifications.Add(new Notification(1, "Entrées prêtes", DateTime.Now, notifications.Count));
			notifications.Add(new Notification(2, "Plats prêts", DateTime.Today, notifications.Count));
			NotificationsListbox.ItemsSource = notifications;
			orders = new ObservableCollection<Order>();
			orders.Add(new Order(1, 5, DateTime.Now, orders.Count + 1));
			orders.Add(new Order(2, 3, DateTime.Today, orders.Count + 1));
			OrdersListbox.ItemsSource = orders;
		}

		/// <summary>
		/// Invoked when this page is about to be displayed in a Frame.
		/// </summary>
		/// <param name="e">Event data that describes how this page was reached.
		/// This parameter is typically used to configure the page.</param>
		protected override void OnNavigatedTo(NavigationEventArgs e)
		{
		}

		private void DrawerIcon_Tapped(object sender, TappedRoutedEventArgs e)
		{
			if (DrawerLayout.IsDrawerOpen)
				DrawerLayout.CloseDrawer();
			else
				DrawerLayout.OpenDrawer();
		}

		private void AppBarButton_Click(object sender, RoutedEventArgs e)
		{
			Frame.Navigate(typeof(NewOrderView));
		}
	}
}
