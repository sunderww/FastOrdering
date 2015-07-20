using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace FastOrdering.Model
{
	public class Category
	{
		public static ObservableCollection<Category> categories = new ObservableCollection<Category>();

		public Category (string name, string colorstring, DateTime createdAt, DateTime updatedAt, string id)
		{
			this.name = name;
			this.id = id;
		}

		private string name;
		public string Name
		{
			get { return name; }
		}
		private string id;
		public string ID
		{
			get { return id; }
		}
	}
}
