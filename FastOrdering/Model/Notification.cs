using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace FastOrdering.Model
{
	public class Notification
	{
		public Notification(int table, string msg, DateTime time, int id)
		{
			this.table = table;
			this.msg = msg;
			this.time = time;
			this.id = id;
		}

		public int ID { get { return this.id; } }
		public int numTable { get { return this.table; } }
		public string Message { get { return "Table #" + this.table + " : " + this.msg; } }
		public string Msg { get { return this.msg; } }
		public DateTime Time { get { return this.time; } }
		public DateTime date { get { return Time.Date; } }
		public int hour { get { return Time.Hour; } }

		private int id;
		private int table;
		private string msg;
		private DateTime time;
	}
}
