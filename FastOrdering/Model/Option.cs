using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;

namespace FastOrdering.Model
{
	public class Option
	{
		#region Attributes
		public static ObservableCollection<Option> options = new ObservableCollection<Option>();

		public string id;
		[JsonIgnore]
		public string ID
		{
			get { return id; }
		}
		private string name;
		[JsonIgnore]
		public string Name
		{
			get { return name; }
		}
		private string number;
		[JsonIgnore]
		public string Number
		{
			get { return number; }
			set { number = value; }
		}
		public string qty
		{
			get { return number; }
		}
		private ObservableCollection<Option> subOptions = new ObservableCollection<Option>();
		[JsonIgnore]
		public ObservableCollection<Option> SubOptions
		{
			get { return subOptions; }
		}
		#endregion

		#region Methods
		[JsonConstructor]
		public Option(string name, string id)
		{
			this.id = id;
			this.name = name;
			this.Number = "0";
		}

		public Option(Option op)
		{
			this.name = op.name;
			this.id = op.id;
			this.number = op.number;
			foreach (Option o in op.subOptions)
				this.subOptions.Add(new Option(o));
		}
		#endregion
	}
}
