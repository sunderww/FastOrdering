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
		private ObservableCollection<Dish> entries = new ObservableCollection<Dish>();
		public ObservableCollection<Dish> Entries
		{
			get { return entries; }
		}
		private ObservableCollection<Dish> dishes = new ObservableCollection<Dish>();
		public ObservableCollection<Dish> Dishes
		{
			get { return dishes; }
		}
		private ObservableCollection<Dish> desserts = new ObservableCollection<Dish>();
		public ObservableCollection<Dish> Desserts
		{
			get { return desserts; }
		}
		private ObservableCollection<Menu> menus = new ObservableCollection<Menu>();
		public ObservableCollection<Menu> Menus
		{
			get { return menus; }
		}

		private Order ord = new Order(0, 0, DateTime.Now, DateTime.Now, 1);
		public Order Ord
		{
			get { return ord; }
			set { }
		}

		private ListView menuList = null;
		private Grid menuCompo = null;

		private Menu selectedMenu = null;
		public Menu SelectedMenu
		{
			get { return selectedMenu; }
		}

		private int modifOrderID = 0;
		//public ObservableCollection<Order> orders;

		public NewOrderView()
		{
			this.InitializeComponent();
			this.DataContext = this;
			//DrawerLayout.InitializeDrawerLayout();

			entries.Add(new Dish(0, 5, "Salade", "entry"));
			entries.Add(new Dish(1, 15, "Foie gras", "entry"));
			entries.Add(new Dish(2, 3, "Radis", "entry"));

			dishes.Add(new Dish(3, 9, "Bavette", "dish"));
			dishes.Add(new Dish(4, 8, "Moules frites", "dish"));
			dishes.Add(new Dish(5, 13, "Magret de canard", "dish"));
			dishes.Add(new Dish(6, 12, "Couscous", "dish"));
			dishes.Add(new Dish(7, 10, "Boeuf bourguignon", "dish"));

			desserts.Add(new Dish(8, 4, "Glace", "dessert"));
			desserts.Add(new Dish(9, 5, "Gâteau", "dessert"));
			desserts.Add(new Dish(10, 3, "Salade de fruits", "dessert"));

			menus.Add(new Menu(0, "Mousaillon", "Visible", "Visible"));
			menus.Add(new Menu(1, "Pirate", "Collapsed", "Collapsed"));
		}

		protected override void OnNavigatedTo(NavigationEventArgs e)
		{
			//orders = new ObservableCollection<Order>();
			//orders.Add(new Order(1, 5, DateTime.Now, orders.Count + 1));
			//orders.Add(new Order(2, 3, DateTime.Today, orders.Count + 1));
			if (e.Parameter == null)
				return;
			modifOrderID = int.Parse(e.Parameter.ToString());
			foreach (Order o in Order.orders)
			{
				if (o.ID == modifOrderID)
					ord = o;
			}
		}

		//private void DrawerIcon_Tapped(object sender, TappedRoutedEventArgs e)
		//{
		//	if (DrawerLayout.IsDrawerOpen)
		//		DrawerLayout.CloseDrawer();
		//	else
		//		DrawerLayout.OpenDrawer();
		//}

		private void SendOrder(object sender, RoutedEventArgs e)
		{
			//var or = new Order(1, 2, DateTime.Now, 1);
			string json = JsonConvert.SerializeObject(Ord);
			System.Diagnostics.Debug.WriteLine(json);
		}

		private void ToggleSwitch_Entry(object sender, RoutedEventArgs e)
		{
			var toggleSwitch = sender as ToggleSwitch;
			var g = toggleSwitch.Parent as StackPanel;
			ListView entries = null;

			foreach (var elem in g.Children)
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
			var toggleSwitch = sender as ToggleSwitch;
			var g = toggleSwitch.Parent as StackPanel;
			ListView dishes = null;

			foreach (var elem in g.Children)
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

		private void ToggleSwitch_Dessert(object sender, RoutedEventArgs e)
		{
			var toggleSwitch = sender as ToggleSwitch;
			var g = toggleSwitch.Parent as StackPanel;
			ListView desserts = null;

			foreach (var elem in g.Children)
			{
				desserts = elem as ListView;
				if (desserts != null && desserts.Name == "DessertList")
					break;
			}

			if (toggleSwitch != null && dishes != null)
			{
				if (toggleSwitch.IsOn == true)
					desserts.Visibility = Visibility.Visible;
				else
					desserts.Visibility = Visibility.Collapsed;
			}
		}

		private void ToggleSwitch_Menu(object sender, RoutedEventArgs e)
		{
			var toggleSwitch = sender as ToggleSwitch;
			var g = toggleSwitch.Parent as Grid;
			Grid menus = null;

			foreach (var elem in g.Children)
			{
				menus = elem as Grid;
				if (menus != null && menus.Name == "MenuCompo")
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

		private void BackToOrder(object sender, RoutedEventArgs e)
		{
			selectedMenu = null;
			menuList.Visibility = Visibility.Visible;
			menuCompo.Visibility = Visibility.Collapsed;
			BackToOrderButton.Visibility = Visibility.Collapsed;
		}

		private void Hub_SectionsInViewChanged(object sender, SectionsInViewChangedEventArgs e)
		{
			if (OrderHub.SectionsInView[0] == SendOrderHubSection)
			{
				SendOrderButton.Visibility = Visibility.Visible;
				BackToOrderButton.Visibility = Visibility.Collapsed;
			}
			else if (OrderHub.SectionsInView[0] == AlacarteHubSection)
			{
				SendOrderButton.Visibility = Visibility.Collapsed;
				BackToOrderButton.Visibility = Visibility.Collapsed;
			}
			else if (OrderHub.SectionsInView[0] == MenusHubSection)
			{
				SendOrderButton.Visibility = Visibility.Collapsed;
				BackToOrderButton.Visibility = menuCompo.Visibility;
			}
		}

		private void Menu_Tapped(object sender, TappedRoutedEventArgs e)
		{
			TextBlock tb = sender as TextBlock;
			foreach (var menu in Menus)
			{
				if (menu.IDMenu == int.Parse(tb.Tag.ToString()))
				{
					selectedMenu = menu;
					break;
				}
			}
			menuList.Visibility = Visibility.Collapsed;
			menuCompo.Visibility = Visibility.Visible;
			selectedMenu.Entry = (tb.Text == "Entrées + Plats" || tb.Name == "SingleMenu") ? "Visible" : "Collapsed";
			selectedMenu.Dessert = (tb.Text == "Plats + Desserts" || tb.Name == "SingleMenu") ? "Visible" : "Collapsed";
			menuCompo.DataContext = selectedMenu;
			BackToOrderButton.Visibility = Visibility.Visible;
		}

		private void Control_Loaded(object sender, RoutedEventArgs e)
		{
			if (sender is ListView)
				menuList = sender as ListView;
			else if (sender is Grid)
				menuCompo = sender as Grid;
		}

		private void TextBox_LostFocus(object sender, RoutedEventArgs e)
		{
			TextBox tb = sender as TextBox;
			ObservableCollection<Dish> collection = null;

			if (tb.Name == "Entries")
				collection = entries;
			else if (tb.Name == "Dishes")
				collection = dishes;
			else if (tb.Name == "Desserts")
				collection = desserts;

			if (collection == null)
				return;

			foreach (var dish in collection)
				if (dish.ID == int.Parse(tb.Tag.ToString()))
				{
					foreach (var d in ord.Dishes)
					{
						if (d.Key.ID == dish.ID)
							ord.Dishes.Remove(d);
						break;
					}
					MyDictionary<Dish> newDish = new MyDictionary<Dish>();
					newDish.Key = dish;
					newDish.Value = int.Parse(tb.Text);
					if (newDish.Value > 0)
						ord.Dishes.Add(newDish);
					break;
				}
		}

		private void TextBoxMenu_LostFocus(object sender, RoutedEventArgs e)
		{
			TextBox tb = sender as TextBox;
			ObservableCollection<Dish> collection = null;
			ObservableCollection<MyDictionary<Dish>> menuCollection = null;

			if (selectedMenu == null)
				return;

			if (tb.Name == "Entries")
				collection = entries;
			else if (tb.Name == "Dishes")
				collection = dishes;
			else if (tb.Name == "Desserts")
				collection = desserts;

			if (collection == null)
				return;

			foreach (var menu in ord.Menus)
				if (menu.IDMenu == selectedMenu.IDMenu && tb.Name == "Entries")
				{
					menuCollection = menu.Entries;
					break;
				}
				else if (menu.IDMenu == selectedMenu.IDMenu && tb.Name == "Dishes")
				{
					menuCollection = menu.Dishes;
					break;
				}
				else if (menu.IDMenu == selectedMenu.IDMenu && tb.Name == "Desserts")
				{
					menuCollection = menu.Desserts;
					break;
				}

			if (menuCollection == null)
			{
				ord.Menus.Add(new Menu(selectedMenu.IDMenu, selectedMenu.Name, selectedMenu.EntryDish, selectedMenu.DishDessert));
				if (tb.Name == "Entries")
					menuCollection = ord.Menus.Last().Entries;
				else if (tb.Name == "Dishes")
					menuCollection = ord.Menus.Last().Dishes;
				else if (tb.Name == "Desserts")
					menuCollection = ord.Menus.Last().Desserts;
			}

			foreach (var dish in collection)
				if (dish.ID == int.Parse(tb.Tag.ToString()))
				{
					foreach (var d in menuCollection)
					{
						if (d.Key.ID == dish.ID)
							menuCollection.Remove(d);
						break;
					}
					MyDictionary<Dish> newDish = new MyDictionary<Dish>();
					newDish.Key = dish;
					newDish.Value = int.Parse(tb.Text);
					if (newDish.Value > 0)
						menuCollection.Add(newDish);
					break;
				}
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
