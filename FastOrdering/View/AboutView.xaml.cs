using FastOrdering.Misc;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Navigation;

// The Blank Page item template is documented at http://go.microsoft.com/fwlink/?LinkID=390556

namespace FastOrdering.View
{
	/// <summary>
	/// An empty page that can be used on its own or navigated to within a Frame.
	/// </summary>
	public sealed partial class AboutView : Page
	{
		#region Methods
		public AboutView()
		{
			this.InitializeComponent();
			//DrawerLayout.InitializeDrawerLayout();
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

		#region AppBar Buttons Methods
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
