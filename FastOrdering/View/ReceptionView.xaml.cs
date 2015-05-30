﻿using FastOrdering.Model;
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
			//DrawerLayout.InitializeDrawerLayout();

			int count = Notification.notifications.Count;
			if (count > 0)
			{
				int idx = Notification.notifications.IndexOf(Notification.notifications.Last());
				firstNotifMsg = Notification.notifications.Last().Message;
				firstNotifTime = Notification.notifications.Last().Time;
				if (count > 1)
				{
					secondNotifMsg = Notification.notifications.ElementAt(idx - 1).Message;
					secondNotifTime = Notification.notifications.ElementAt(idx - 1).Time;
				}
			}

			count = Order.orders.Count;
			if (count > 0)
			{
				int idx = Order.orders.IndexOf(Order.orders.Last());
				firstOrderMsg = Order.orders.Last().Message;
				firstOrderTime = Order.orders.Last().Time;
				if (count > 1)
				{
					secondOrderMsg = Order.orders.ElementAt(idx - 1).Message;
					secondOrderTime = Order.orders.ElementAt(idx - 1).Time;
				}
			}

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

		//private void DrawerIcon_Tapped(object sender, TappedRoutedEventArgs e)
		//{
		//	if (DrawerLayout.IsDrawerOpen)
		//		DrawerLayout.CloseDrawer();
		//	else
		//		DrawerLayout.OpenDrawer();
		//}

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
		}

	}
}
