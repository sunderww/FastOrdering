using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace FastOrdering.Model
{
	public class Dish
	{
		public static ObservableCollection<Dish> dishes = new ObservableCollection<Dish>();

		[JsonConstructor]
		public Dish(string id, string name, int price, string[] categories_ids, bool available, DateTime createdAt, DateTime updatedAt)
		{
			this.id = id;
			this.name = name;
			this.price = price;
			this.available = available;
			this.categories = categories_ids;
		}

		public Dish(string id, double price, string name)
		{
			this.id = id;
			this.price = price;
			this.name = name;
			this.comment = "";
			this.options = "";
			this.status = 0;
			this.qty = 0;
		}

		public string id;
		[JsonIgnore]
		public string ID
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
		private string[] categories;
		public string[] Categories
		{
			get { return categories; }
		}
		public int qty;
		public string comment;
		public string options;
		[JsonIgnore]
		public int status;
		private bool available;
	}
}
