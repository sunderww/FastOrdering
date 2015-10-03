using Newtonsoft.Json;
using System;
using System.Collections.ObjectModel;
using System.Linq;

namespace FastOrdering.Model
{
	public class Menu
	{
		#region Attributes
		public static ObservableCollection<Menu> menus = new ObservableCollection<Menu>();
		public static ObservableCollection<Dish> ALaCarte = new ObservableCollection<Dish>();
		public static Menu alacarte;

		public ObservableCollection<Dish> content = new ObservableCollection<Dish>();

		private ObservableCollection<MyDictionary<Dish>> dishes = new ObservableCollection<MyDictionary<Dish>>();
		[JsonIgnore]
		public ObservableCollection<MyDictionary<Dish>> Dishes
		{
			get { return dishes; }
		}

		private string name;
		[JsonIgnore]
		public string Name
		{
			get { return name; }
		}
		private string idMenu;
		[JsonIgnore]
		public string IDMenu
		{
			get { return idMenu; }
		}
		public string menuId
		{
			get { return idMenu; }
		}
		private string hasCompo;
		[JsonIgnore]
		public string HasCompo
		{
			set { hasCompo = value; }
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

		[JsonIgnore]
		private ObservableCollection<Composition> compositions = new ObservableCollection<Composition>();
		[JsonIgnore]
		public ObservableCollection<Composition> Compositions
		{
			get { return compositions; }
		}

		[JsonIgnore]
		private ObservableCollection<Category> categories = new ObservableCollection<Category>();
		[JsonIgnore]
		public ObservableCollection<Category> Categories
		{
			get { return categories; }
		}
		#endregion

		#region Methods
		[JsonConstructor]
		public Menu(string name, DateTime createdAt, DateTime updatedAt, string id)
		{
			this.name = name;
			this.idMenu = id;
			this.hasCompo = "Collapsed";
			this.hasNoCompo = "Collapsed";
		}

		public Menu(string id, string name, string entryDish, string dishDessert)
		{
			this.idMenu = id;
			this.name = name;
			this.entryDish = entryDish;
			this.dishDessert = dishDessert;
			this.hasCompo = (entryDish == "Visible" || dishDessert == "Visible") ? "Visible" : "Collapsed";
			this.hasNoCompo = (entryDish == "Visible" || dishDessert == "Visible") ? "Collapsed" : "Visible";
			this.Entry = "Collapsed";
			this.Dessert = "Collapsed";
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

		static public void OrderALaCarte()
		{
			foreach (Dish dish in Dish.dishes)
			{
				foreach (Composition compo in alacarte.Compositions)
				{
					foreach (Category cat in compo.Categories)
					{
						if (dish.Categories.Contains(cat.ID) && ALaCarte.Contains(dish) == false)
							ALaCarte.Add(dish);
					}
				}
			}
		}
		#endregion
	}
}
