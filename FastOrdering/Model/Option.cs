using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;

namespace FastOrdering.Model
{
	public class Option
	{
		#region Attributes
		public static ObservableCollection<Option> options = new ObservableCollection<Option>();

		private string id;
		public string ID
		{
			get { return id; }
		}
		private string name;
		public string Name
		{
			get { return name; }
		}
		private ObservableCollection<Option> subOptions = new ObservableCollection<Option>();
		public ObservableCollection<Option> SubOptions
		{
			get { return subOptions; }
		}
		#endregion

		#region Methods
		public Option(string name, string id)
		{
			this.id = id;
			this.name = name;
		}
		#endregion
	}
}
