using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace FastOrdering.Model
{
	public class User
	{
		User(int id, string name)
		{
			this.idUser = id;
			this.name = name;
		}

		private int idUser;
		private string name;
	}
}
