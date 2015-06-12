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

		public Order(int numOrder, int numTable, int numPA, DateTime date, DateTime hour)
		{
			this.numTable = numTable;
			this.pa = numPA;
			this.date = new DateTime(date.Year, date.Month, date.Day, hour.Hour, hour.Minute, hour.Second);
			this.id = numOrder;
		}

		public void PrepareOrder()
		{
			foreach (Menu menu in menus)
			{
				menu.FillContent();
			}
		}

		//[JsonConstructor]
		//public Order(int numOrder, int numTable, int numPA, DateTime date, DateTime hour, string globalComment)
		//{
		//	this.numTable = numTable;
		//	this.pa = numPA;
		//	this.date = new DateTime(date.Year, date.Month, date.Day, hour.Hour, hour.Minute, hour.Second);
		//	this.id = numOrder;
		//	this.globalComment = globalComment;
		//}

		[JsonIgnore]
		public int numOrder { get { return this.id; } }
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
		public string Message { get { return "Commande #" + this.id + ", Table #" + this.numTable + ", PA : " + this.pa; } }

		private int id;
		[JsonIgnore]
		public int ID
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
