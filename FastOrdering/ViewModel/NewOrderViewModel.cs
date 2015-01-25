using FastOrdering.Misc;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Input;

namespace FastOrdering.ViewModel {
	public class NewOrderViewModel : ViewModelBase {
		public ICommand Connect { get; private set; }

		public NewOrderViewModel() {
			Connect = new RelayCommand(Order);
		}

		private void Order() {
			System.Diagnostics.Debug.WriteLine("toto");

			new Socket("send_order");
		}
	}
}
