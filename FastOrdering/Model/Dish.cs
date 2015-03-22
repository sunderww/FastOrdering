using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace FastOrdering.Model
{
	public class Dish
	{
		public Dish(int id, int price, string name, string category)
		{
			this.id = id;
			this.price = price;
			this.name = name;
			this.category = category;
		}

		private int id;
		public int ID
		{
			get { return id; }
		}

		private int price;
		private string name;
		public string Name
		{
			get { return name; }
		}
		private string category;
	}
}
