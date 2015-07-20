﻿using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace FastOrdering.Model
{
	public class Composition
	{
		public static ObservableCollection<Composition> compositions = new ObservableCollection<Composition>();

		public Composition(string name, int price, string menu_id, string[] categories_ids, DateTime createdAt, DateTime updatedAt, string id)
		{
			this.name = name;
			this.price = price;
			//this.categories = categories_ids;
			this.id = id;
			this.menuID = menu_id;
			foreach (Menu menu in Menu.menus)
			{
				if (menu.IDMenu == menu_id)
				{
					menu.HasCompo = "Visible";
					menu.Compositions.Add(this);
				}
			}
			foreach (Category cat in Category.categories)
			{
				if (categories_ids.Contains(cat.ID))
					categories.Add(cat);
			}
		}

		private string id;
		public string ID
		{
			get { return id; }
		}
		private string name;
		public string Name
		{
			get { return name; }
		}
		private string menuID;
		public string MenuID
		{
			get { return menuID; }
		}
		private int price;
		//private string[] categories;
		private ObservableCollection<Category> categories = new ObservableCollection<Category>();
		public ObservableCollection<Category> Categories
		{
			get { return categories; }
		}
	}
}
