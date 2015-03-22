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
			entries.Add(new Dish(0, 5, "Salade", "entry"));
			entries.Add(new Dish(2, 3, "Radis", "entry"));

			dishes.Add(new Dish(3, 9, "Bavette", "dish"));
			dishes.Add(new Dish(4, 8, "Moules frites", "dish"));
			dishes.Add(new Dish(7, 10, "Boeuf bourguignon", "dish"));

			desserts.Add(new Dish(8, 4, "Glace", "dessert"));
			desserts.Add(new Dish(9, 5, "Gâteau", "dessert"));
		}

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
		private string name;
		public string Name
		{
			get { return name; }
		}
		private int idMenu;
		public int IDMenu
		{
			get { return idMenu; }
		}
		private string hasCompo;
		public string HasCompo
		{
			get { return hasCompo; }
		}
		private string hasNoCompo;
		public string HasNoCompo
		{
			get { return hasNoCompo; }
		}
		private string entryDish;
		public string EntryDish
		{
			get { return entryDish; }
		}
		private string dishDessert;
		public string DishDessert
		{
			get { return dishDessert; }
		}
		public string Entry { get; set; }
		public string Dessert { get; set; }
	}
}
