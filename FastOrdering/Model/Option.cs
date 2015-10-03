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
		private Dictionary<string, string> values;
		public Dictionary<string, string> Values
		{
			get { return values; }
		}
		#endregion

		#region Methods
		public Option(string id, Dictionary<string, string> values)
		{
			this.id = id;
			this.values = values;
		}
		#endregion
	}
}
