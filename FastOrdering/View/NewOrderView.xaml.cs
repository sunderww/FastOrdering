﻿using FastOrdering.Model;
using System;
using System.Collections.Generic;
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

namespace FastOrdering.View {
	/// <summary>
	/// An empty page that can be used on its own or navigated to within a Frame.
	/// </summary>
	public sealed partial class NewOrderView : Page {
		public NewOrderView() {
			this.InitializeComponent();
			DrawerLayout.InitializeDrawerLayout();
		}

		/// <summary>
		/// Invoked when this page is about to be displayed in a Frame.
		/// </summary>
		/// <param name="e">Event data that describes how this page was reached.
		/// This parameter is typically used to configure the page.</param>
		protected override void OnNavigatedTo(NavigationEventArgs e) {
		}

		private void DrawerIcon_Tapped(object sender, TappedRoutedEventArgs e) {
			if (DrawerLayout.IsDrawerOpen)
				DrawerLayout.CloseDrawer();
			else
				DrawerLayout.OpenDrawer();
		}

		private void AppBarButton_Click(object sender, RoutedEventArgs e) {
			Order or = new Order(1, 2, DateTime.Now);
			string json = JsonConvert.SerializeObject(or);
			System.Diagnostics.Debug.WriteLine(json);
		}
	}
}
