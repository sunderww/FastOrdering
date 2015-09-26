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

namespace FastOrdering.View
{

	public sealed partial class NewOrderView : Page
	{

		private ObservableCollection<Menu> menus = new ObservableCollection<Menu>();
		public ObservableCollection<Menu> Menus
		{
			get { return Menu.menus; }
		}

		public ObservableCollection<Dish> ALaCarte
		{
			get { return Menu.ALaCarte; }
		}

		private Collection<KeyValuePair<ObservableCollection<MyDictionary<Dish>>, string>> categoriesDishes = new Collection<KeyValuePair<ObservableCollection<MyDictionary<Dish>>, string>>();

		private Order ord;
		public Order Ord
		{
			get { return ord; }
			set { }
		}

		private ListView menuList = null;
		private Grid menuCompo = null;

		private Composition selectedCompo = null;
		public Composition SelectedCompo
		{
			get { return selectedCompo; }
		}

		private string modifOrderID = "";

		public NewOrderView()
		{
			this.InitializeComponent();
			this.DataContext = this;
		}

		protected override void OnNavigatedTo(NavigationEventArgs e)
		{
			ord = null;
			ord = new Order("1", 0, 0, DateTime.Now.ToString(), DateTime.Now);
			if (e.Parameter == null)
				return;
			modifOrderID = e.Parameter.ToString();
			foreach (Order o in Order.orders)
			{
				if (o.ID == modifOrderID)
					ord = o;
			}
		}

		private void SendOrder(object sender, RoutedEventArgs e)
		{
			Socket.SendOrder(ord);
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
					break;
				}
			}
		}

		private void TextBoxMenu_LostFocus(object sender, RoutedEventArgs e)
		{
			TextBox tb = sender as TextBox;
			Menu menu = null;
			bool isAlreadyIn = false;

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
						if (menu.Dishes.Count == 0)
							ord.Menus.Remove(menu);
						if (!isAlreadyIn && menu.Dishes.Count > 0)
							ord.Menus.Add(menu);
						break;
					}
				}
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
