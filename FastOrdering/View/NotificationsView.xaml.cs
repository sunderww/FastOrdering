using FastOrdering.Misc;
using FastOrdering.Model;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Controls.Primitives;
using Windows.UI.Xaml.Input;
using Windows.UI.Xaml.Navigation;

// The Blank Page item template is documented at http://go.microsoft.com/fwlink/?LinkID=390556

namespace FastOrdering.View
{
	/// <summary>
	/// An empty page that can be used on its own or navigated to within a Frame.
	/// </summary>
	public sealed partial class NotificationsView : Page
	{
		#region Methods
		public NotificationsView()
		{
			this.InitializeComponent();
			NotificationsListbox.ItemsSource = Notification.notifications;

			if (Notification.notifications.Count == 0)
			{
				BorderNotif.Visibility = Visibility.Collapsed;
				NoNotif.Visibility = Visibility.Visible;
			}
			else
			{
				BorderNotif.Visibility = Visibility.Visible;
				NoNotif.Visibility = Visibility.Collapsed;
			}
		}

		private void Notif_Tapped(object sender, TappedRoutedEventArgs e)
		{
			Grid g = sender as Grid;
			FlyoutBase.ShowAttachedFlyout(sender as FrameworkElement);

			foreach (Notification notif in Notification.notifications)
				if (notif.ID == int.Parse(g.Tag.ToString()))
					notifText.Text = notif.Message;
		}

		/// <summary>
		/// Invoked when this page is about to be displayed in a Frame.
		/// </summary>
		/// <param name="e">Event data that describes how this page was reached.
		/// This parameter is typically used to configure the page.</param>
		protected override void OnNavigatedTo(NavigationEventArgs e)
		{
		}
		#endregion

		#region GridManipulation
		private void Grid_ManipulationStarted(object sender, ManipulationStartedRoutedEventArgs e)
		{
		}

		private void Grid_ManipulationCompleted(object sender, ManipulationCompletedRoutedEventArgs e)
		{
			var velocities = e.Velocities;
			var pos = 0;
			if (velocities.Linear.X > 0.5)
			{
				foreach (Notification n in Notification.notifications)
				{
					if (n.ID == (int)(sender as Grid).Tag)
						break;
					++pos;
				}
				if (pos < Notification.notifications.Count)
					Notification.notifications.RemoveAt(pos);
			}

			if (Notification.notifications.Count == 0)
			{
				BorderNotif.Visibility = Visibility.Collapsed;
				NoNotif.Visibility = Visibility.Visible;
			}
		}
		#endregion

		#region AppBar Buttons Methods
		private void DeleteAll_Click(object sender, RoutedEventArgs e)
		{
			Notification.notifications.Clear();

			if (Notification.notifications.Count == 0)
			{
				BorderNotif.Visibility = Visibility.Collapsed;
				NoNotif.Visibility = Visibility.Visible;
			}
		}

		private void NewCommand_Click(object sender, RoutedEventArgs e)
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
