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
		public Order(int table, int pa, DateTime time, int id)
		{
			this.table = table;
			this.pa = pa;
			this.time = time;
			this.id = id;
		}

		public int numOrder { get { return this.id; } }
		public int numTable
		{
			get { return this.table; }
			set
			{
				if (this.table != value)
				{
					this.table = value;
					this.NotifyPropertyChanged("numTable");
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
		public DateTime Time { get { return this.time; } }
		public string Message { get { return "Commande #" + this.id + ", Table #" + this.table + ", PA : " + this.pa; } }

		private int id;
		public int ID
		{
			get { return id; }
		}
		private int table;
		private int pa;
		private DateTime time;
		private ObservableCollection<MyDictionary<Menu>> menus = new ObservableCollection<MyDictionary<Menu>>();
		public ObservableCollection<MyDictionary<Menu>> Menus
		{
			get { return menus; }
		}
		private ObservableCollection<MyDictionary<Dish>> dishes = new ObservableCollection<MyDictionary<Dish>>();
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
