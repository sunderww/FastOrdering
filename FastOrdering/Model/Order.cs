using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace FastOrdering.Model
{
	public class Order : INotifyPropertyChanged
	{
		public Order(int table, int pa, DateTime time, int nb)
		{
			this.table = table;
			this.pa = pa;
			this.time = time;
			this.nb = nb;
		}

		public int numOrder { get { return this.nb; } }
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
		public string Message { get { return "Commande #" + this.nb + ", Table #" + this.table + ", PA : " + this.pa; } }

		private int nb;
		private int table;
		private int pa;
		private DateTime time;
		private User user;
		private Status status;

		#region INotifyPropertyChanged
		public void NotifyPropertyChanged(string nomPropriete)
		{
			if (PropertyChanged != null)
				PropertyChanged(this, new PropertyChangedEventArgs(nomPropriete));
		}

		public event PropertyChangedEventHandler PropertyChanged;
		#endregion

		private enum Status
		{
			WAITING,
			IN_PROGRESS,
			ENTRY_OK,
			DISH_OK,
			DESSERT_OK,
			REFUSE,
			QUESTION
		}
	}
}
