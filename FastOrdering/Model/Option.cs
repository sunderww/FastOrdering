using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace FastOrdering.Model
{
	public class Option
	{
		public static ObservableCollection<Option> options = new ObservableCollection<Option>();

		public Option(string id, Dictionary<string, string> values)
		{
			this.id = id;
			this.values = values;
		}

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
	}
}
