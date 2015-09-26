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
		//public ObservableCollection<Order> orders = new ObservableCollection<Order>();

		public OrdersView()
		{
			this.InitializeComponent();
			OrdersListbox.ItemsSource = Order.orders;

			if (Order.orders.Count == 0)
			{
				history.Visibility = Visibility.Collapsed;
				NoOrder.Visibility = Visibility.Visible;
			}
			else
			{
				history.Visibility = Visibility.Visible;
				NoOrder.Visibility = Visibility.Collapsed;
			}
		}

		/// <summary>
		/// Invoked when this page is about to be displayed in a Frame.
		/// </summary>
		/// <param name="e">Event data that describes how this page was reached.
		/// This parameter is typically used to configure the page.</param>
		protected override void OnNavigatedTo(NavigationEventArgs e)
		{
		}

		private void Grid_Tapped(object sender, TappedRoutedEventArgs e)
		{
			string id = (sender as Grid).Tag.ToString();
			Order ord = null;
			foreach (Order o in Order.orders)
			{
				if (id == o.ID)
					ord = o;
			}

			ord = Socket.GetOrder(id);
			if (ord == null)
				return;

			ordNum.Text = "Commande #" + ord.numOrder;
			tablePA.Text = "Table #" + ord.Table + ", PA :" + ord.numPA;
			time.Text = "Le " + ord.Time.Day + "/" + ord.Time.Month + "/" + ord.Time.Year + " à " + ord.Time.Hour + ":" + ord.Time.Minute;
			content.Text = "Contenu :";
			menuId.Text = "Menu id : " + ord.ID;
			modifyButton.Tag = ord.ID;
			FlyoutBase.ShowAttachedFlyout(sender as FrameworkElement);
		}

		private void ModifyButton_Tapped(object sender, TappedRoutedEventArgs e)
		{
			Frame.Navigate(typeof(NewOrderView), modifyButton.Tag);
		}

		private void NewOrder_Click(object sender, RoutedEventArgs e)
		{
			Frame.Navigate(typeof(NewOrderView));
		}

		private void Home_Click(object sender, RoutedEventArgs e)
		{
			Frame.Navigate(typeof(ReceptionView));
		}

		private void Notification_Click(object sender, RoutedEventArgs e)
		{
			Frame.Navigate(typeof(NotificationsView));
		}

		private void History_Click(object sender, RoutedEventArgs e)
		{
			Frame.Navigate(typeof(OrdersView));
		}

		private void About_Click(object sender, RoutedEventArgs e)
		{
			Frame.Navigate(typeof(AboutView));
		}

		private void LogOut_Click(object sender, RoutedEventArgs e)
		{
		}
	}
}
