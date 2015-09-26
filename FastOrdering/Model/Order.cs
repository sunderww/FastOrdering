using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.ComponentModel;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace FastOrdering.Model
{
	public class Order : INotifyPropertyChanged
	{
		public static ObservableCollection<Order> orders = new ObservableCollection<Order>();

		public Order(string id, int table_id, int dinerNumber, string date, DateTime time)
		{
			this.numTable = table_id;
			this.pa = dinerNumber;
			DateTime d;
			if (DateTime.TryParseExact(date, "d/M/yyyy", null, System.Globalization.DateTimeStyles.None, out d))
				this.date = new DateTime(d.Year, d.Month, d.Day, time.Hour, time.Minute, time.Second);
			this.id = id;
			this.globalComment = "";
		}

		public void PrepareOrder()
		{
			foreach (Menu menu in menus)
			{
				menu.FillContent();
			}
		}

		[JsonIgnore]
		public string numOrder { get { return this.id; } }
		[JsonIgnore]
		public int Table
		{
			get { return this.numTable; }
			set
			{
				if (this.numTable != value)
				{
					this.numTable = value;
					this.NotifyPropertyChanged("Table");
				}
			}
		}
		public int numPA
		{
			get { return this.pa; }
			set
			{
				if (this.pa != value)
				{
					this.pa = value;
					this.NotifyPropertyChanged("numTable");
				}
			}
		}
		[JsonIgnore]
		public DateTime Time { get { return this.date; } }
		[JsonIgnore]
		public string Message { get { return "Table #" + this.numTable + ", PA : " + this.pa; } }

		private string id;
		[JsonIgnore]
		public string ID
		{
			get { return id; }
		}
		public int numTable;
		private int pa;
		private DateTime date;
		private string globalComment;
		public string GlobalComment
		{
			get { return globalComment; }
			set { globalComment = value; }
		}
		private ObservableCollection<Menu> menus = new ObservableCollection<Menu>();
		[JsonIgnore]
		public ObservableCollection<Menu> Menus
		{
			get { return menus; }
		}
		public ObservableCollection<Menu> order
		{
			get { return menus; }
		}
		private ObservableCollection<MyDictionary<Dish>> dishes = new ObservableCollection<MyDictionary<Dish>>();
		[JsonIgnore]
		public ObservableCollection<MyDictionary<Dish>> Dishes
		{
			get { return dishes; }
		}

		#region INotifyPropertyChanged
		public void NotifyPropertyChanged(string nomPropriete)
		{
			if (PropertyChanged != null)
				PropertyChanged(this, new PropertyChangedEventArgs(nomPropriete));
		}

		public event PropertyChangedEventHandler PropertyChanged;
		#endregion
	}

	public class MyDictionary<T>
	{
		public T Key { get; set; }
		public int Value { get; set; }
	}
}
