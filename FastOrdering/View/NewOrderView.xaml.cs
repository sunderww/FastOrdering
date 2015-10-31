using FastOrdering.Misc;
using FastOrdering.Model;
using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Linq;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Controls.Primitives;
using Windows.UI.Xaml.Input;
using Windows.UI.Xaml.Navigation;

namespace FastOrdering.View
{

	public sealed partial class NewOrderView : Page
	{
		#region Attributes
		public ObservableCollection<Menu> Menus
		{
			get { return Menu.menus; }
		}

		public ObservableCollection<Category> ALaCarte
		{
			get { return Menu.alacarte.Categories; }
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

		private static string MenuIDSelected;
		#endregion

		#region Methods
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
			ord = e.Parameter as Order;
		}

		private void OnLeave()
		{
			foreach (Menu menu in Menu.menus)
				menu.Dishes.Clear();
			foreach (Option op in Option.options)
			{
				foreach (Option subOp in op.SubOptions)
					subOp.Number = "0";
				op.Number = "0";
			}
		}

		private void Control_Loaded(object sender, RoutedEventArgs e)
		{
			if (sender is ListView)
				menuList = sender as ListView;
			else if (sender is Grid)
				menuCompo = sender as Grid;
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

		private void OptionsButtonLoaded(object sender, RoutedEventArgs e)
		{
			AppBarButton abb = sender as AppBarButton;
			Dish dish = abb.DataContext as Dish;
			abb.Visibility = dish.HasOptions;
		}

		private void OptionsButtonTapped(object sender, TappedRoutedEventArgs e)
		{
			AppBarButton abb = sender as AppBarButton;
			Dish dish = abb.DataContext as Dish;
			OptionsList.DataContext = dish;
			OptionsList.ItemsSource = dish.options;
			FlyoutBase.ShowAttachedFlyout(sender as FrameworkElement);
		}

		private void AddOption(object sender, TappedRoutedEventArgs e)
		{
			optionsPopup.Hide();
		}

		private void AddComment(object sender, TappedRoutedEventArgs e)
		{
			ord.GlobalComment = Comment.Text;
			Comment.Text = "";
			commentPopup.Hide();
		}

		private void Dish_Tapped(object sender, TappedRoutedEventArgs e)
		{
			StackPanel sp = sender as StackPanel;
			Dish dish = sp.Tag as Dish;
			if (dish.options.Count == 0)
				return;
			OptionsList.DataContext = dish;
			OptionsList.ItemsSource = dish.options;
			FlyoutBase.ShowAttachedFlyout(sender as FrameworkElement);
		}
		#endregion

		#region ToggleSwitch Methods
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

		private void ToggleSwitch_Compo(object sender, RoutedEventArgs e)
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
					newDish.Key = new Dish(dish);
					newDish.Value = 0;
					dishes.Add(newDish);
				}
			}
			KeyValuePair<ObservableCollection<MyDictionary<Dish>>, string> cat = new KeyValuePair<ObservableCollection<MyDictionary<Dish>>, string>(dishes, g.Tag.ToString());
			foreach (KeyValuePair<ObservableCollection<MyDictionary<Dish>>, string> c in categoriesDishes)
			{
				if (c.Value == cat.Value)
				{
					categoriesDishes.Remove(c);
					break;
				}
			}
			categoriesDishes.Add(cat);
			lv.ItemsSource = dishes;

			if (toggleSwitch != null && lv != null)
			{
				if (toggleSwitch.IsOn == true)
					lv.Visibility = Visibility.Visible;
				else
					lv.Visibility = Visibility.Collapsed;
			}
		}
		#endregion

		#region TextBox Methods
		//private void TextBox_LostFocus(object sender, RoutedEventArgs e)
		//{
		//	TextBox tb = sender as TextBox;
		//	Menu menu = Menu.alacarte;
		//	bool isAlreadyIn = false;

		//	foreach (Menu m in ord.Menus)
		//	{
		//		if (m.IDMenu == menu.IDMenu)
		//		{
		//			menu = m;
		//			isAlreadyIn = true;
		//			break;
		//		}
		//	}

		//	foreach (Dish dish in Dish.dishes)
		//	{
		//		if (tb.Tag.ToString() == dish.ID)
		//		{
		//			foreach (var d in menu.Dishes)
		//			{
		//				if (d.Key.ID == tb.Tag.ToString())
		//				{
		//					menu.Dishes.Remove(d);
		//					break;
		//				}
		//			}
		//			if (tb.Text.Replace(".", "") == "")
		//				tb.Text = "0";
		//			MyDictionary<Dish> newDish = new MyDictionary<Dish>();
		//			newDish.Key = new Dish(dish.ID, 0, dish.Name);
		//			newDish.Value = int.Parse(tb.Text);
		//			if (newDish.Value > 0)
		//				menu.Dishes.Add(newDish);
		//			if (menu.Dishes.Count == 0)
		//				ord.Menus.Remove(menu);
		//			else if (!isAlreadyIn)
		//				ord.Menus.Add(menu);
		//			break;
		//		}
		//	}
		//}

		private void TextBox_GotFocus(object sender, RoutedEventArgs e)
		{
			TextBox tb = sender as TextBox;
			tb.SelectAll();
		}

		private void TextBoxMenu_LostFocus(object sender, RoutedEventArgs e)
		{
			TextBox tb = sender as TextBox;
			Menu menu = null;
			bool isAlreadyIn = false;

			foreach (Menu m in ord.Menus)
				if (m.IDMenu == MenuIDSelected)
				{
					menu = m;
					isAlreadyIn = true;
					break;
				}
			if (!isAlreadyIn)
				foreach (Menu m in Menu.menus)
					if (m.IDMenu == MenuIDSelected)
					{
						menu = m;
						break;
					}
			if (menu == null)
				return;

			foreach (KeyValuePair<ObservableCollection<MyDictionary<Dish>>, string> col in categoriesDishes)
			{
				foreach (MyDictionary<Dish> dish in col.Key)
					if (dish.Key.ID == tb.Tag.ToString())
					{
						foreach (var d in menu.Dishes)
							if (d.Key.ID == dish.Key.ID)
							{
								dish.Value = d.Value;
								menu.Dishes.Remove(d);
								break;
							}
						if (tb.Text.Replace(".", "") == "")
							tb.Text = "0";
						dish.Value += int.Parse(tb.Text);
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

		private void TextBox_Alacarte(object sender, RoutedEventArgs e)
		{
			TextBox tb = sender as TextBox;
			Menu menu = null;
			bool isAlreadyIn = false;

			foreach (Menu m in ord.Menus)
				if (m.IDMenu == Menu.alacarte.IDMenu)
				{
					menu = m;
					isAlreadyIn = true;
					break;
				}
			if (!isAlreadyIn)
				menu = Menu.alacarte;
			if (menu == null)
				return;

			foreach (KeyValuePair<ObservableCollection<MyDictionary<Dish>>, string> col in categoriesDishes)
				foreach (MyDictionary<Dish> dish in col.Key)
					if (dish.Key.ID == tb.Tag.ToString())
					{
						foreach (var d in menu.Dishes)
							if (d.Key.ID == dish.Key.ID)
							{
								menu.Dishes.Remove(d);
								break;
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
		#endregion

		#region AppBar Buttons Methods
		private void SendOrder(object sender, RoutedEventArgs e)
		{
			Socket.SendOrder(ord);
			Frame.Navigate(typeof(ReceptionView));
			OnLeave();
		}

		private void BackToOrder(object sender, RoutedEventArgs e)
		{
			selectedCompo = null;
			menuList.Visibility = Visibility.Visible;
			menuCompo.Visibility = Visibility.Collapsed;
			BackToOrderButton.Visibility = Visibility.Collapsed;
			categoriesDishes.Clear();
		}

		private void ShowComment(object sender, RoutedEventArgs e)
		{
			FlyoutBase.ShowAttachedFlyout(sender as FrameworkElement);
		}

		private void Home_Click(object sender, RoutedEventArgs e)
		{
			Frame.Navigate(typeof(ReceptionView));
			OnLeave();
		}

		private void Notification_Click(object sender, RoutedEventArgs e)
		{
			Frame.Navigate(typeof(NotificationsView));
			OnLeave();
		}

		private void History_Click(object sender, RoutedEventArgs e)
		{
			Frame.Navigate(typeof(OrdersView));
			OnLeave();
		}

		private void About_Click(object sender, RoutedEventArgs e)
		{
			Frame.Navigate(typeof(AboutView));
			OnLeave();
		}

		private void LogOut_Click(object sender, RoutedEventArgs e)
		{
			Socket.Disconnect();
			Frame.Navigate(typeof(LoginView));
			OnLeave();
		}
		#endregion
	}
}
