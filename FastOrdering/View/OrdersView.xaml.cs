using FastOrdering.Misc;
using FastOrdering.Model;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Input;
using Windows.UI.Xaml.Navigation;

// The Blank Page item template is documented at http://go.microsoft.com/fwlink/?LinkID=390556

namespace FastOrdering.View
{
	/// <summary>
	/// An empty page that can be used on its own or navigated to within a Frame.
	/// </summary>
	public sealed partial class OrdersView : Page
	{
		#region Attributes
		private Grid gridTapped = null;
		#endregion

		#region Methods
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

		protected override void OnNavigatedTo(NavigationEventArgs e)
		{
		}

		public bool ShowOrder(Order ord)
		{
			Frame.Navigate(typeof(NewOrderView), ord);
			return true;
		}
		#endregion

		#region AppBar Buttons Methods
		private void Grid_Tapped(object sender, TappedRoutedEventArgs e)
		{
			string id = (sender as Grid).Tag.ToString();
			Order ord = null;
			foreach (Order o in Order.orders)
			{
				if (id == o.ID)
					ord = o;
			}
			gridTapped = sender as Grid;
			LoginView.sock.GetOrder(id, ShowOrder);
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
			Socket.Disconnect();
			Frame.Navigate(typeof(LoginView));
		}
		#endregion
	}
}
