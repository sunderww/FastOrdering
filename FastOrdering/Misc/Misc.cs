using System;
using System.ComponentModel;
using System.Runtime.CompilerServices;
using Windows.UI.Xaml.Data;

namespace FastOrdering.Misc
{
	public class ViewModelBase : INotifyPropertyChanged
	{
		public event PropertyChangedEventHandler PropertyChanged;

		public void NotifyPropertyChanged(string nomPropriete)
		{
			if (PropertyChanged != null)
				PropertyChanged(this, new PropertyChangedEventArgs(nomPropriete));
		}

		private bool NotifyPropertyChanged<T>(ref T variable, T valeur, [CallerMemberName] string nomPropriete = null)
		{
			if (object.Equals(variable, valeur))
				return false;

			variable = valeur;
			NotifyPropertyChanged(nomPropriete);
			return true;
		}
	}

	public sealed class StringFormatConverter : IValueConverter
	{
		public object Convert(object value, Type targetType, object parameter, string language)
		{
			if (value == null)
				return null;

			if (value.GetType() == typeof(DateTime))
			{
				DateTime time = (DateTime)value;
				return "Le " + time.Day + "/" + time.Month + "/" + time.Year + " à " + time.Hour + ":" + time.Minute;
			}

			if (parameter == null)
				return value;

			return string.Format((string)parameter, value);
		}

		public object ConvertBack(object value, Type targetType, object parameter, string language)
		{
			throw new NotImplementedException();
		}
	}
}
