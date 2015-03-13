using FastOrdering.Model;
using FastOrdering.Misc;
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
using Newtonsoft.Json;

// The Blank Page item template is documented at http://go.microsoft.com/fwlink/?LinkID=390556

namespace FastOrdering.View
{
	/// <summary>
	/// An empty page that can be used on its own or navigated to within a Frame.
	/// </summary>
	public sealed partial class NotificationsView : Page
	{
		public ObservableCollection<Notification> notifications;

		public NotificationsView()
		{
			this.InitializeComponent();
			DrawerLayout.InitializeDrawerLayout();
			Socket sock = new Socket();
			Notification notif = JsonConvert.DeserializeObject<Notification>(sock.Emit("/notification"));
			sock.Disconnect();

			notifications = new ObservableCollection<Notification>();
			notifications.Add(new Notification(1, "Entrées prêtes", DateTime.Now, notifications.Count));
			notifications.Add(new Notification(2, "Plats prêts", DateTime.Today, notifications.Count));
			notifications.Add(new Notification(3, "Desserts prêts", DateTime.Today, notifications.Count));
			NotificationsListbox.ItemsSource = notifications;

			if (notifications.Count == 0)
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
			notifications.Clear();

			if (notifications.Count == 0)
			{
				BorderNotif.Visibility = Visibility.Collapsed;
				NoNotif.Visibility = Visibility.Visible;
			}
		}

		private void Grid_ManipulationStarted(object sender, ManipulationStartedRoutedEventArgs e)
		{
			System.Diagnostics.Debug.WriteLine("toto");
		}

		private void Grid_ManipulationCompleted(object sender, ManipulationCompletedRoutedEventArgs e)
		{
			var velocities = e.Velocities;
			System.Diagnostics.Debug.WriteLine(velocities.Linear.X);
			var pos = 0;
			if (velocities.Linear.X > 0.5)
			{
				foreach (Notification n in notifications)
				{
					if (n.ID == (int)(sender as Grid).Tag)
						break;
					++pos;
				}
				if (pos < notifications.Count)
					notifications.RemoveAt(pos);
			}

			if (notifications.Count == 0)
			{
				BorderNotif.Visibility = Visibility.Collapsed;
				NoNotif.Visibility = Visibility.Visible;
			}
		}
	}
}
