using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Linq;
using System.Runtime.CompilerServices;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Input;
using Windows.UI.Xaml.Data;

namespace FastOrdering.Misc {
	public class RelayCommand : ICommand {
		private readonly Action actionAExecuter;

		public RelayCommand(Action action) {
			actionAExecuter = action;
		}

		public bool CanExecute(object parameter) {
			return true;
		}

		public event EventHandler CanExecuteChanged;

		public void Execute(object parameter) {
			actionAExecuter();
		}
	}

	public class ViewModelBase : INotifyPropertyChanged {
		public event PropertyChangedEventHandler PropertyChanged;

		public void NotifyPropertyChanged(string nomPropriete) {
			if (PropertyChanged != null)
				PropertyChanged(this, new PropertyChangedEventArgs(nomPropriete));
		}

		private bool NotifyPropertyChanged<T>(ref T variable, T valeur, [CallerMemberName] string nomPropriete = null) {
			if (object.Equals(variable, valeur))
				return false;

			variable = valeur;
			NotifyPropertyChanged(nomPropriete);
			return true;
		}
	}

	public sealed class StringFormatConverter : IValueConverter {
		public object Convert(object value, Type targetType, object parameter, string language) {
			if (value == null)
				return null;

			if (value.GetType() == typeof(DateTime)) {
				DateTime time = (DateTime)value;
				return "Le " + time.Day + "/" + time.Month + "/" + time.Year + " à " + time.Hour + ":" + time.Minute;
			}

			if (parameter == null)
				return value;

			return string.Format((string)parameter, value);
		}

		public object ConvertBack(object value, Type targetType, object parameter,
			string language) {
			throw new NotImplementedException();
		}
	}
}
