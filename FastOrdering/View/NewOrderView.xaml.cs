using FastOrdering.Model;
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
using System.Collections.ObjectModel;

// The Blank Page item template is documented at http://go.microsoft.com/fwlink/?LinkID=390556

namespace FastOrdering.View
{
	/// <summary>
	/// An empty page that can be used on its own or navigated to within a Frame.
	/// </summary>
	public sealed partial class NewOrderView : Page
	{
		public ObservableCollection<string> entries = new ObservableCollection<string>();
		public ObservableCollection<string> Entries
		{
			get { return entries; }
		}
		public ObservableCollection<string> dishes = new ObservableCollection<string>();
		public ObservableCollection<string> Dishes
		{
			get { return dishes; }
		}

		public NewOrderView()
		{
			this.InitializeComponent();
			this.DataContext = this;
			DrawerLayout.InitializeDrawerLayout();

			entries.Add("Salade");
			entries.Add("Foie gras");
			entries.Add("Radis");

			dishes.Add("Bavette");
			dishes.Add("Moules frites");
			dishes.Add("Magret de canard");
			dishes.Add("Couscous");
			dishes.Add("Boeuf bourguignon");
		}

		private void DrawerIcon_Tapped(object sender, TappedRoutedEventArgs e)
		{
			if (DrawerLayout.IsDrawerOpen)
				DrawerLayout.CloseDrawer();
			else
				DrawerLayout.OpenDrawer();
		}

		private void SendOrder(object sender, RoutedEventArgs e)
		{
			Order or = new Order(1, 2, DateTime.Now, 1);
			string json = JsonConvert.SerializeObject(or);
			System.Diagnostics.Debug.WriteLine(json);
		}

		public Order ord = new Order(0, 0, DateTime.Now, 1);
		public Order Ord
		{
			get { return ord; }
			set { }
		}

		private void ToggleSwitch_Entry(object sender, RoutedEventArgs e)
		{
			ToggleSwitch toggleSwitch = sender as ToggleSwitch;
			Grid g = toggleSwitch.Parent as Grid;
			ListView entries = null;

			foreach (UIElement elem in g.Children)
			{
				entries = elem as ListView;
				if (entries != null && entries.Name == "EntryList")
					break;
			}

			if (toggleSwitch != null && entries != null)
			{
				if (toggleSwitch.IsOn == true)
					entries.Visibility = Visibility.Visible;
				else
					entries.Visibility = Visibility.Collapsed;
			}
		}

		private void ToggleSwitch_Dish(object sender, RoutedEventArgs e)
		{
			ToggleSwitch toggleSwitch = sender as ToggleSwitch;
			Grid g = toggleSwitch.Parent as Grid;
			ListView dishes = null;

			foreach (UIElement elem in g.Children)
			{
				dishes = elem as ListView;
				if (dishes != null && dishes.Name == "DishList")
					break;
			}

			if (toggleSwitch != null && dishes != null)
			{
				if (toggleSwitch.IsOn == true)
					dishes.Visibility = Visibility.Visible;
				else
					dishes.Visibility = Visibility.Collapsed;
			}
		}

		private void ToggleSwitch_Menu(object sender, RoutedEventArgs e)
		{
			ToggleSwitch toggleSwitch = sender as ToggleSwitch;
			Grid g = toggleSwitch.Parent as Grid;
			ListView menus = null;

			foreach (UIElement elem in g.Children)
			{
				menus = elem as ListView;
				if (menus != null && menus.Name == "MenuList")
					break;
			}

			if (toggleSwitch != null && dishes != null)
			{
				if (toggleSwitch.IsOn == true)
					menus.Visibility = Visibility.Visible;
				else
					menus.Visibility = Visibility.Collapsed;
			}
		}

		private void AddElements(object sender, RoutedEventArgs e)
		{

		}

		private void AddMenusElements(object sender, RoutedEventArgs e)
		{

		}

		private void Hub_SectionsInViewChanged(object sender, SectionsInViewChangedEventArgs e)
		{
			if (OrderHub.SectionsInView[0] == SendOrderHubSection)
			{
				SendOrderButton.Visibility = Visibility.Visible;
				AddMenusElementsButton.Visibility = Visibility.Collapsed;
				AddElementsButton.Visibility = Visibility.Collapsed;
			}
			else if (OrderHub.SectionsInView[0] == AlacarteHubSection)
			{
				SendOrderButton.Visibility = Visibility.Collapsed;
				AddMenusElementsButton.Visibility = Visibility.Visible;
				AddElementsButton.Visibility = Visibility.Collapsed;
			}
			else
			{
				SendOrderButton.Visibility = Visibility.Collapsed;
				AddMenusElementsButton.Visibility = Visibility.Collapsed;
				AddElementsButton.Visibility = Visibility.Visible;
			}
		}
	}
}
