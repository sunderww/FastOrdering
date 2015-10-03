﻿using FastOrdering.Misc;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Navigation;

// The Blank Page item template is documented at http://go.microsoft.com/fwlink/?LinkID=390556

namespace FastOrdering.View
{
	/// <summary>
	/// An empty page that can be used on its own or navigated to within a Frame.
	/// </summary>
	public sealed partial class LoginView : Page
	{
		#region Attributes
		public static Socket sock;
		#endregion

		#region Methods
		public LoginView()
		{
			this.InitializeComponent();
			ID.Text = "$2a$10$iJadROc3r8A/ERPvgTWHxuGXuEBlPYZjWSjCyPPv6yS.pM7E.naCa";
		}

		/// <summary>
		/// Invoked when this page is about to be displayed in a Frame.
		/// </summary>
		/// <param name="e">Event data that describes how this page was reached.
		/// This parameter is typically used to configure the page.</param>
		protected override void OnNavigatedTo(NavigationEventArgs e)
		{
		}

		public bool ConnectUser(bool answer)
		{
			if (answer)
				Frame.Navigate(typeof(ReceptionView));
			else
				Socket.Disconnect();
			return answer;
		}
		#endregion

		#region AppBar Buttons Methods
		private void AppBarButton_Click(object sender, RoutedEventArgs e)
		{
			sock = new Socket();
			Socket.Authentication(ID.Text, ConnectUser);
		}
		#endregion
	}
}
