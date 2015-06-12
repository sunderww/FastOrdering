using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace FastOrdering.Model
{
	public class Dish
	{
		public Dish(int id, double price, string name, string category)
		{
			this.id = id;
			this.price = price;
			this.name = name;
			this.category = category;
			this.comment = "";
			this.options = "";
			this.status = 0;
			this.qty = 0;
		}

		public int id;
		[JsonIgnore]
		public int ID
		{
			get { return id; }
		}

		private double price;
		private string name;
		[JsonIgnore]
		public string Name
		{
			get { return name; }
		}
		private string category;
		public int qty;
		public string comment;
		public string options;
		[JsonIgnore]
		public int status;
	}
}
