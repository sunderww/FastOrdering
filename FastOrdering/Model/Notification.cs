using System;
using System.Collections.ObjectModel;
using System.Linq;

namespace FastOrdering.Model
{
	public class Notification
	{
		#region Attributes
		public static ObservableCollection<Notification> notifications = new ObservableCollection<Notification>();

		public int ID { get { return this.id; } }
		public string Message { get { return "Table #" + this.numTable + " : " + this.msg; } }
		public string Msg { get { return this.msg; } }
		public DateTime Time { get { return this.date; } }

		private int id;
		private int numTable;
		private string msg;
		private DateTime date;
		#endregion

		#region Methods
		public Notification(int numTable, string msg, DateTime hour, DateTime date)
		{
			this.numTable = numTable;
			this.msg = msg;
			this.date = new DateTime(date.Year, date.Month, date.Day, hour.Hour, hour.Minute, hour.Second);
			this.id = (notifications.Count > 0) ? notifications.Last().ID + 1 : 0;
		}
		#endregion
	}
}
