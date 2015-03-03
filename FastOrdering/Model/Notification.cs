using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace FastOrdering.Model {
	public class Notification {
		public Notification(uint table, string msg, DateTime time, int id) {
			_table = table;
			_msg = msg;
			_time = time;
			_id = id;
		}

		public int		ID { get { return _id; } }
		public uint		numTable { get { return _table; } }
		public string	Message { get { return "Table #" + _table + " : " + _msg; } }
		public string	msg { get { return _msg; } }
		public DateTime Time { get { return _time; } }
		public DateTime date { get { return Time.Date; } }
		public int		hour { get { return Time.Hour; } }

		private int			_id;
		private uint		_table;
		private string		_msg;
		private DateTime	_time;
	}
}
