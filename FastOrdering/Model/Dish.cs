using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace FastOrdering.Model
{
	public class Dish
	{
		Dish(int id, int price, string name)
		{
			this.id = id;
			this.price = price;
			this.name = name;
		}

		private int id;
		private int price;
		private string name;
		private string image;
	}
}
