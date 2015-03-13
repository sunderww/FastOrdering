using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace FastOrdering.Model
{
	class Menu
	{
		Menu(int id, string name)
		{
			this.idMenu = id;
			this.name = name;
		}

		private List<Dish> entries;
		public List<Dish> Entries
		{
			get { return entries; }
		}
		private List<Dish> dishes;
		public List<Dish> Dishes
		{
			get { return dishes; }
		}
		private List<Dish> desserts;
		public List<Dish> Desserts
		{
			get { return desserts; }
		}
		private string name;
		private int idMenu;
	}
}
