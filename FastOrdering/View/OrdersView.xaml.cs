using FastOrdering.Misc;
using FastOrdering.Model;
using Newtonsoft.Json;
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
	public sealed partial class OrdersView : Page
	{
		public ObservableCollection<Order> orders;

		public OrdersView()
		{
			this.InitializeComponent();
			DrawerLayout.InitializeDrawerLayout();
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

		private void Grid_Tapped(object sender, TappedRoutedEventArgs e)
		{
			int id = (int)(sender as Grid).Tag;
			Order ord = orders.ElementAt(id - 1);

			//Socket sock = new Socket("/get_order");
			//Socket sock = new Socket();
			//Notification notif = JsonConvert.DeserializeObject<Notification>(sock.Emit("/receive_order"));
			//sock.Disconnect();

			ordNum.Text = "Commande #" + ord.numOrder;
			tablePA.Text = "Table #" + ord.numTable + ", PA :" + ord.numPA;
			time.Text = "Le " + ord.Time.Day + "/" + ord.Time.Month + "/" + ord.Time.Year + " à " + ord.Time.Hour + ":" + ord.Time.Minute;
			content.Text = "Contenu :";
			menuId.Text = "Menu id : 1212";
			//popupOrder.IsOpen = true;
			FlyoutBase.ShowAttachedFlyout(sender as FrameworkElement);

		}
	}
}
