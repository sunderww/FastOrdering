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
		public string firstNotifMsg;
		public string FirstNotifMsg
		{
			get { return firstNotifMsg; }
		}
		public DateTime firstNotifTime;
		public DateTime FirstNotifTime
		{
			get { return firstNotifTime; }
		}
		public string secondNotifMsg;
		public string SecondNotifMsg
		{
			get { return secondNotifMsg; }
		}
		public DateTime secondNotifTime;
		public DateTime SecondNotifTime
		{
			get { return secondNotifTime; }
		}
		public string firstOrderMsg;
		public string FirstOrderMsg
		{
			get { return firstOrderMsg; }
		}
		public DateTime firstOrderTime;
		public DateTime FirstOrderTime
		{
			get { return firstOrderTime; }
		}
		public string secondOrderMsg;
		public string SecondOrderMsg
		{
			get { return secondOrderMsg; }
		}
		public DateTime secondOrderTime;
		public DateTime SecondOrderTime
		{
			get { return secondOrderTime; }
		}

		public ReceptionView()
		{
			this.InitializeComponent();
			DrawerLayout.InitializeDrawerLayout();
			notifications = new ObservableCollection<Notification>();
			notifications.Add(new Notification(1, "Entrées prêtes", DateTime.Now, notifications.Count));
			notifications.Add(new Notification(2, "Plats prêts", DateTime.Today, notifications.Count));

			int idx = notifications.IndexOf(notifications.Last());
			firstNotifMsg = notifications.Last().Message;
			firstNotifTime = notifications.Last().Time;
			secondNotifMsg = notifications.ElementAt(idx - 1).Message;
			secondNotifTime = notifications.ElementAt(idx - 1).Time;

			orders = new ObservableCollection<Order>();
			orders.Add(new Order(1, 5, DateTime.Now, orders.Count + 1));
			orders.Add(new Order(2, 3, DateTime.Today, orders.Count + 1));
			idx = orders.IndexOf(orders.Last());
			firstOrderMsg = orders.Last().Message;
			firstOrderTime = orders.Last().Time;
			secondOrderMsg = orders.ElementAt(idx - 1).Message;
			secondOrderTime = orders.ElementAt(idx - 1).Time;

			this.DataContext = this;
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
