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
using FastOrdering.Misc;

// The Blank Page item template is documented at http://go.microsoft.com/fwlink/?LinkID=390556

namespace FastOrdering.View
{
	/// <summary>
	/// An empty page that can be used on its own or navigated to within a Frame.
	/// </summary>
	public sealed partial class NewOrderView : Page
	{
		//private ObservableCollection<Dish> entries = new ObservableCollection<Dish>();
		//public ObservableCollection<Dish> Entries
		//{
		//	get { return entries; }
		//}
		//private ObservableCollection<Dish> dishes = new ObservableCollection<Dish>();
		//public ObservableCollection<Dish> Dishes
		//{
		//	get { return dishes; }
		//}
		//private ObservableCollection<MyDictionary<Dish>> dishes2 = new ObservableCollection<MyDictionary<Dish>>();
		//public ObservableCollection<MyDictionary<Dish>> Dishes2
		//{
		//	get { return dishes2; }
		//}
		//private ObservableCollection<Dish> desserts = new ObservableCollection<Dish>();
		//public ObservableCollection<Dish> Desserts
		//{
		//	get { return desserts; }
		//}
		private ObservableCollection<Menu> menus = new ObservableCollection<Menu>();
		public ObservableCollection<Menu> Menus
		{
			get { return Menu.menus; }
		}

		public ObservableCollection<Dish> ALaCarte
		{
			get { return Menu.ALaCarte; }
			//get { return Menu.alacarte.Compositions; }
		}

		private Collection<KeyValuePair<ObservableCollection<MyDictionary<Dish>>, string>> categoriesDishes = new Collection<KeyValuePair<ObservableCollection<MyDictionary<Dish>>, string>>();

		private Order ord = new Order("1", 0, 0, "02/07/1990", DateTime.Now);
		public Order Ord
		{
			get { return ord; }
			set { }
		}

		private ListView menuList = null;
		private Grid menuCompo = null;

		//private Menu selectedMenu = null;
		//public Menu SelectedMenu
		//{
		//	get { return selectedMenu; }
		//}

		private Composition selectedCompo = null;
		public Composition SelectedCompo
		{
			get { return selectedCompo; }
		}

		private string modifOrderID = "";
		//public ObservableCollection<Order> orders;

		public NewOrderView()
		{
			this.InitializeComponent();
			this.DataContext = this;
			//DrawerLayout.InitializeDrawerLayout();

			//entries.Add(new Dish("0", 5, "Salade"));
			//entries.Add(new Dish("1", 15, "Foie gras"));
			//entries.Add(new Dish("2", 3, "Radis"));

			//dishes.Add(new Dish("3", 9, "Bavette"));
			//dishes.Add(new Dish("4", 8, "Moules frites"));
			//dishes.Add(new Dish("5", 13, "Magret de canard"));
			//dishes.Add(new Dish("6", 12, "Couscous"));
			//dishes.Add(new Dish("7", 10, "Boeuf bourguignon"));

			//desserts.Add(new Dish("8", 4, "Glace"));
			//desserts.Add(new Dish("9", 5, "Gâteau"));
			//desserts.Add(new Dish("10", 3, "Salade de fruits"));
		}

		protected override void OnNavigatedTo(NavigationEventArgs e)
		{
			//orders = new ObservableCollection<Order>();
			//orders.Add(new Order(1, 5, DateTime.Now, orders.Count + 1));
			//orders.Add(new Order(2, 3, DateTime.Today, orders.Count + 1));
			if (e.Parameter == null)
				return;
			modifOrderID = e.Parameter.ToString();
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
			Socket.SendOrder(ord);
			//var or = new Order(1, 2, DateTime.Now, 1);
			//string json = JsonConvert.SerializeObject(Ord);
			//System.Diagnostics.Debug.WriteLine(json);
		}

		//private void ToggleSwitch_Entry(object sender, RoutedEventArgs e)
		//{
		//	var toggleSwitch = sender as ToggleSwitch;
		//	var g = toggleSwitch.Parent as StackPanel;
		//	ListView entries = null;

		//	foreach (var elem in g.Children)
		//	{
		//		entries = elem as ListView;
		//		if (entries != null && entries.Name == "EntryList")
		//			break;
		//	}

		//	if (toggleSwitch != null && entries != null)
		//	{
		//		if (toggleSwitch.IsOn == true)
		//			entries.Visibility = Visibility.Visible;
		//		else
		//			entries.Visibility = Visibility.Collapsed;
		//	}
		//}

		//private void ToggleSwitch_Dish(object sender, RoutedEventArgs e)
		//{
		//	var toggleSwitch = sender as ToggleSwitch;
		//	var g = toggleSwitch.Parent as StackPanel;
		//	ListView dishes = null;

		//	foreach (var elem in g.Children)
		//	{
		//		dishes = elem as ListView;
		//		if (dishes != null && dishes.Name == "DishList")
		//			break;
		//	}

		//	if (toggleSwitch != null && dishes != null)
		//	{
		//		if (toggleSwitch.IsOn == true)
		//			dishes.Visibility = Visibility.Visible;
		//		else
		//			dishes.Visibility = Visibility.Collapsed;
		//	}
		//}

		//private void ToggleSwitch_Dessert(object sender, RoutedEventArgs e)
		//{
		//	var toggleSwitch = sender as ToggleSwitch;
		//	var g = toggleSwitch.Parent as StackPanel;
		//	ListView desserts = null;

		//	foreach (var elem in g.Children)
		//	{
		//		desserts = elem as ListView;
		//		if (desserts != null && desserts.Name == "DessertList")
		//			break;
		//	}

		//	if (toggleSwitch != null && dishes != null)
		//	{
		//		if (toggleSwitch.IsOn == true)
		//			desserts.Visibility = Visibility.Visible;
		//		else
		//			desserts.Visibility = Visibility.Collapsed;
		//	}
		//}

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

			if (toggleSwitch != null && menus != null)
			{
				if (toggleSwitch.IsOn == true)
					menus.Visibility = Visibility.Visible;
				else
					menus.Visibility = Visibility.Collapsed;
			}
		}

		private void BackToOrder(object sender, RoutedEventArgs e)
		{
			selectedCompo = null;
			menuList.Visibility = Visibility.Visible;
			menuCompo.Visibility = Visibility.Collapsed;
			BackToOrderButton.Visibility = Visibility.Collapsed;
			categoriesDishes.Clear();
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

		private static string MenuIDSelected;
		private void Menu_Tapped(object sender, TappedRoutedEventArgs e)
		{
			TextBlock tb = sender as TextBlock;

			foreach (var composition in Composition.compositions)
			{
				if (composition.ID == tb.Tag.ToString())
				{
					selectedCompo = composition;
					MenuIDSelected = composition.MenuID;
					break;
				}
			}
			menuList.Visibility = Visibility.Collapsed;
			menuCompo.Visibility = Visibility.Visible;
			//selectedMenu.Entry = (tb.Text == "Entrées + Plats" || tb.Name == "SingleMenu") ? "Visible" : "Collapsed";
			//selectedMenu.Dessert = (tb.Text == "Plats + Desserts" || tb.Name == "SingleMenu") ? "Visible" : "Collapsed";
			menuCompo.DataContext = selectedCompo;
			foreach (var child in menuCompo.Children)
			{
				ListView lv = child as ListView;
				if (lv != null && lv.Name == "AllCategories")
				{
					lv.ItemsSource = selectedCompo.Categories;
					break;
				}
			}
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
			Menu menu = Menu.alacarte;
			bool isAlreadyIn = false;

			foreach (Menu m in ord.Menus)
			{
				if (m.IDMenu == menu.IDMenu)
				{
					menu = m;
					isAlreadyIn = true;
					break;
				}
			}

			foreach (Dish dish in Dish.dishes)
			{
				if (tb.Tag.ToString() == dish.ID)
				{
					foreach (var d in menu.Dishes)
					{
						if (d.Key.ID == tb.Tag.ToString())
						{
							menu.Dishes.Remove(d);
							break;
						}
					}
					if (tb.Text.Replace(".", "") == "")
						tb.Text = "0";
					MyDictionary<Dish> newDish = new MyDictionary<Dish>();
					newDish.Key = new Dish(dish.ID, 0, dish.Name);
					newDish.Value = int.Parse(tb.Text);
					if (newDish.Value > 0)
						menu.Dishes.Add(newDish);
					if (menu.Dishes.Count == 0)
						ord.Menus.Remove(menu);
					else if (!isAlreadyIn)
						ord.Menus.Add(menu);
					//ord.Dishes.Add(newDish);
					break;
				}
			}
			//ObservableCollection<Dish> collection = null;

			//if (tb.Name == "Entries")
			//	collection = entries;
			//else if (tb.Name == "Dishes")
			//	collection = dishes;
			//else if (tb.Name == "Desserts")
			//	collection = desserts;

			//if (collection == null)
			//	return;

			//foreach (var dish in collection)
			//	if (dish.ID == tb.Tag.ToString())
			//	{
			//		foreach (var d in ord.Dishes)
			//		{
			//			if (d.Key.ID == dish.ID)
			//				ord.Dishes.Remove(d);
			//			break;
			//		}
			//		MyDictionary<Dish> newDish = new MyDictionary<Dish>();
			//		newDish.Key = dish;
			//		newDish.Value = int.Parse(tb.Text);
			//		if (newDish.Value > 0)
			//			ord.Dishes.Add(newDish);
			//		break;
			//	}
		}

		private void TextBoxMenu_LostFocus(object sender, RoutedEventArgs e)
		{
			TextBox tb = sender as TextBox;
			Menu menu = null;
			bool isAlreadyIn = false;
			//ObservableCollection<Dish> collection = null;
			//ObservableCollection<MyDictionary<Dish>> menuCollection = null;

			foreach (Menu m in ord.Menus)
			{
				if (m.IDMenu == MenuIDSelected)
				{
					menu = m;
					isAlreadyIn = true;
					break;
				}
			}
			if (!isAlreadyIn)
				foreach (Menu m in Menu.menus)
				{
					if (m.IDMenu == MenuIDSelected)
					{
						menu = m;
						break;
					}
				}
			if (menu == null)
				return;

			foreach (KeyValuePair<ObservableCollection<MyDictionary<Dish>>, string> col in categoriesDishes)
			{
				foreach (MyDictionary<Dish> dish in col.Key)
				{
					if (dish.Key.ID == tb.Tag.ToString())
					{
						//foreach (var d in ord.Dishes)
						foreach (var d in menu.Dishes)
						{
							if (d.Key.ID == dish.Key.ID)
							{
								menu.Dishes.Remove(d);
								break;
							}
						}
						if (tb.Text.Replace(".", "") == "")
							tb.Text = "0";
						dish.Value = int.Parse(tb.Text);
						if (dish.Value > 0)
							menu.Dishes.Add(dish);
						//ord.Dishes.Add(newDish);
						if (menu.Dishes.Count == 0)
							ord.Menus.Remove(menu);
						if (!isAlreadyIn && menu.Dishes.Count > 0)
							ord.Menus.Add(menu);
						break;
					}
				}
			}

			//if (selectedMenu == null)
			//	return;

			//if (tb.Name == "Entries")
			//	collection = entries;
			//else if (tb.Name == "Dishes")
			//	collection = dishes;
			//else if (tb.Name == "Desserts")
			//	collection = desserts;

			//if (collection == null)
			//	return;

			//foreach (var menu in ord.Menus)
			//	if (menu.IDMenu == selectedMenu.IDMenu && tb.Name == "Entries")
			//	{
			//		menuCollection = menu.Entries;
			//		break;
			//	}
			//	else if (menu.IDMenu == selectedMenu.IDMenu && tb.Name == "Dishes")
			//	{
			//		menuCollection = menu.Dishes;
			//		break;
			//	}
			//	else if (menu.IDMenu == selectedMenu.IDMenu && tb.Name == "Desserts")
			//	{
			//		menuCollection = menu.Desserts;
			//		break;
			//	}

			//if (menuCollection == null)
			//{
			//	ord.Menus.Add(new Menu(selectedMenu.IDMenu, selectedMenu.Name, selectedMenu.EntryDish, selectedMenu.DishDessert));
			//	if (tb.Name == "Entries")
			//		menuCollection = ord.Menus.Last().Entries;
			//	else if (tb.Name == "Dishes")
			//		menuCollection = ord.Menus.Last().Dishes;
			//	else if (tb.Name == "Desserts")
			//		menuCollection = ord.Menus.Last().Desserts;
			//}

			//foreach (var dish in collection)
			//	if (dish.ID == tb.Tag.ToString())
			//	{
			//		foreach (var d in menuCollection)
			//		{
			//			if (d.Key.ID == dish.ID)
			//				menuCollection.Remove(d);
			//			break;
			//		}
			//		MyDictionary<Dish> newDish = new MyDictionary<Dish>();
			//		newDish.Key = dish;
			//		newDish.Value = int.Parse(tb.Text);
			//		if (newDish.Value > 0)
			//			menuCollection.Add(newDish);
			//		break;
			//	}
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

		private void ToggleSwitch_Toggled(object sender, RoutedEventArgs e)
		{
			var toggleSwitch = sender as ToggleSwitch;
			var g = toggleSwitch.Parent as StackPanel;
			ListView lv = null;

			foreach (var elem in g.Children)
			{
				lv = elem as ListView;
				if (lv != null)
					break;
			}

			//bool isIn = false;
			//foreach (KeyValuePair<ObservableCollection<MyDictionary<Dish>>, string> pair in categoriesDishes)
			//{
			//	if (g.Tag.ToString() == pair.Value)
			//	{
			//		isIn = true;
			//		break;
			//	}
			//}

			//if (!isIn)
			//{
			ObservableCollection<MyDictionary<Dish>> dishes = new ObservableCollection<MyDictionary<Dish>>();
			foreach (Dish dish in Dish.dishes)
			{
				if (dish.Categories.Contains(g.Tag.ToString()))
				{
					MyDictionary<Dish> newDish = new MyDictionary<Dish>();
					newDish.Key = dish;
					newDish.Value = 0;
					dishes.Add(newDish);
				}
			}
			categoriesDishes.Add(new KeyValuePair<ObservableCollection<MyDictionary<Dish>>, string>(dishes, g.Tag.ToString()));
			lv.ItemsSource = dishes;
			//}

			if (toggleSwitch != null && lv != null)
			{
				if (toggleSwitch.IsOn == true)
					lv.Visibility = Visibility.Visible;
				else
					lv.Visibility = Visibility.Collapsed;
			}
		}

		private void ToggleSwitch_Toggled_1(object sender, RoutedEventArgs e)
		{
			var toggleSwitch = sender as ToggleSwitch;
			var g = toggleSwitch.Parent as StackPanel;
			ListView lv = null;

			foreach (var elem in g.Children)
			{
				lv = elem as ListView;
				if (lv != null)
					break;
			}

			if (toggleSwitch != null && lv != null)
			{
				if (toggleSwitch.IsOn == true)
					lv.Visibility = Visibility.Visible;
				else
					lv.Visibility = Visibility.Collapsed;
			}
		}

	}
}
