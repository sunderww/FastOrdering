using Newtonsoft.Json;
using System;
using System.Collections.ObjectModel;
using System.Linq;

namespace FastOrdering.Model
{
	public class Dish
	{
		#region Attributes
		public static ObservableCollection<Dish> dishes = new ObservableCollection<Dish>();

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
		[JsonIgnore]
		public string[] Categories
		{
			get { return categories; }
		}
		public int qty;
		public string comment;
		public Collection<Option> options = new Collection<Option>();
		[JsonIgnore]
		public string status;
		private bool available;
		#endregion

		#region Methods
		[JsonConstructor]
		public Dish(string id, string name, double price, string[] options, string[] categories_ids, bool available, DateTime createdAt, DateTime updatedAt)
		{
			this.id = id;
			this.name = name;
			this.price = price;
			this.available = available;
			this.categories = categories_ids;
			this.comment = "";
			this.status = "";
			foreach (Option op in Option.options)
			{
				if (options.Contains(op.ID))
					this.options.Add(op);
			}
		}

		public Dish(string id, double price, string name)
		{
			this.id = id;
			this.price = price;
			this.name = name;
			this.comment = "";
			this.status = "";
			this.qty = 0;
		}
		#endregion
	}
}
