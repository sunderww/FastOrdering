using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace FastOrdering.Model {
	public class Order {
		public Order(uint table, uint pa, DateTime time, int nb) {
			_table = table;
			_pa = pa;
			_time = time;
			_nb = nb;
		}

		public int numOrder { get { return _nb; } }
		public uint numTable { get { return _table; } }
		public uint numPA { get { return _pa; } }
		public DateTime Time { get { return _time; } }
		public string Message { get { return "Commande #" + _nb + ", Table #" + _table + ", PA : " + _pa; } }

		private int			_nb;
		private uint		_table;
		private uint		_pa;
		private DateTime	_time;
	}
}
