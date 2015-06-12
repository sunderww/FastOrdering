using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace FastOrdering.Model
{
	public class Menu
	{
		public static ObservableCollection<Menu> menus = new ObservableCollection<Menu>();

		public Menu(int id, string name, string entryDish, string dishDessert)
		{
			this.idMenu = id;
			this.name = name;
			this.entryDish = entryDish;
			this.dishDessert = dishDessert;
			this.hasCompo = (entryDish == "Visible" || dishDessert == "Visible") ? "Visible" : "Collapsed";
			this.hasNoCompo = (entryDish == "Visible" || dishDessert == "Visible") ? "Collapsed" : "Visible";
			this.Entry = "Collapsed";
			this.Dessert = "Collapsed";

			MyDictionary<Dish> d = new MyDictionary<Dish>();
			d.Key = new Dish(0, 5, "Salade", "entry");
			d.Value = 0;
			entries.Add(d);
			MyDictionary<Dish> d2 = new MyDictionary<Dish>();
			d2.Key = new Dish(2, 3, "Radis", "entry");
			entries.Add(d2);

			MyDictionary<Dish> d3 = new MyDictionary<Dish>();
			d3.Key = new Dish(1, 9, "Bavette", "dish");
			dishes.Add(d3);
			MyDictionary<Dish> d1 = new MyDictionary<Dish>();
			d1.Key = new Dish(4, 8, "Moules frites", "dish");
			dishes.Add(d1);
			MyDictionary<Dish> d4 = new MyDictionary<Dish>();
			d4.Key = new Dish(7, 10, "Boeuf bourguignon", "dish");
			dishes.Add(d4);

			MyDictionary<Dish> d5 = new MyDictionary<Dish>();
			d5.Key = new Dish(8, 4, "Glace", "dessert");
			desserts.Add(d5);
			MyDictionary<Dish> d6 = new MyDictionary<Dish>();
			d6.Key = new Dish(9, 5, "Gâteau", "dessert");
			desserts.Add(d6);
		}

		public void FillContent()
		{
			foreach (MyDictionary<Dish> dish in dishes)
			{
				dish.Key.qty = dish.Value;
				if (dish.Value > 0)
					content.Add(dish.Key);
			}
		}

		public ObservableCollection<Dish> content = new ObservableCollection<Dish>();
		private ObservableCollection<MyDictionary<Dish>> entries = new ObservableCollection<MyDictionary<Dish>>();
		[JsonIgnore]
		public ObservableCollection<MyDictionary<Dish>> Entries
		{
			get { return entries; }
		}
		private ObservableCollection<MyDictionary<Dish>> dishes = new ObservableCollection<MyDictionary<Dish>>();
		[JsonIgnore]
		public ObservableCollection<MyDictionary<Dish>> Dishes
		{
			get { return dishes; }
		}
		private ObservableCollection<MyDictionary<Dish>> desserts = new ObservableCollection<MyDictionary<Dish>>();
		[JsonIgnore]
		public ObservableCollection<MyDictionary<Dish>> Desserts
		{
			get { return desserts; }
		}
		private string name;
		[JsonIgnore]
		public string Name
		{
			get { return name; }
		}
		private int idMenu;
		[JsonIgnore]
		public int IDMenu
		{
			get { return idMenu; }
		}
		public int menuId
		{
			get { return idMenu; }
		}
		private string hasCompo;
		[JsonIgnore]
		public string HasCompo
		{
			get { return hasCompo; }
		}
		private string hasNoCompo;
		[JsonIgnore]
		public string HasNoCompo
		{
			get { return hasNoCompo; }
		}
		private string entryDish;
		[JsonIgnore]
		public string EntryDish
		{
			get { return entryDish; }
		}
		private string dishDessert;
		[JsonIgnore]
		public string DishDessert
		{
			get { return dishDessert; }
		}
		[JsonIgnore]
		public string Entry { get; set; }
		[JsonIgnore]
		public string Dessert { get; set; }
	}
}
