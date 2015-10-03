using System;
using System.Collections.ObjectModel;

namespace FastOrdering.Model
{
	public class Category
	{
		#region Attributes
		public static ObservableCollection<Category> categories = new ObservableCollection<Category>();

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
		#endregion

		#region Methods
		public Category(string name, string colorstring, DateTime createdAt, DateTime updatedAt, string id)
		{
			this.name = name;
			this.id = id;
		}
		#endregion
	}
}
