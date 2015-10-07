package com.eip.fastordering.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;
import com.eip.fastordering.R;
import com.eip.fastordering.activity.Main;
import com.eip.fastordering.customs.StockMenu;
import com.eip.fastordering.struct.CardStruct;
import com.eip.fastordering.struct.CategoryStruct;
import com.eip.fastordering.struct.CompositionStruct;
import com.eip.fastordering.struct.ElementStruct;
import com.eip.fastordering.struct.MenuStruct;
import com.eip.fastordering.struct.OptionsStruct;
import com.eip.fastordering.struct.OrderStruct;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class OrderFragment extends Fragment {

	private static final String                  ARG_SECTION_NUMBER = "section_number";
	private static       List<ElementStruct>     _mElements         = new ArrayList<>();
	private static       List<MenuStruct>        _mMenus            = new ArrayList<>();
	private static       List<CategoryStruct>    _mCats             = new ArrayList<>();
	private static       List<CompositionStruct> _mCompos           = new ArrayList<>();
	private static CardStruct       _mCard;
	private static OptionsStruct    _mOptions;
	private static FragmentActivity _mActivity;
	private static JSONObject       _mOrder;
	private static OrderStruct      _mDetails;
	private        View             _mRootView;

	/**
	 * Constructor
	 */
	public OrderFragment() {

	}

	/**
	 * Set new instance of OrderFragment
	 * @param sectionNumber
	 * @param order
	 * @param details
	 * @return
	 */
	public static OrderFragment newInstance(int sectionNumber, JSONObject order, OrderStruct details) {
		OrderFragment fragment = new OrderFragment();
		Bundle        args     = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		_mOrder = order;
		_mDetails = details;
		return fragment;
	}

	/**
	 * Create option lists
	 */
	public static void fetchOptions() {
		try {
			_mOptions = new OptionsStruct(new JSONObject(StockMenu.instance().read("/options")));
//			_mOptions = new OptionsStruct(new JSONObject(_mActivity.getSharedPreferences("DATACARD", 0).getString("/options", "toto")));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create element list
	 */
	public static void fetchElements() {
		get_mOptions();
		JSONArray arr;
		_mElements.clear();
		try {
			arr = new JSONObject(StockMenu.instance().read("/elements")).getJSONArray("elements");
//			arr = new JSONObject(_mActivity.getSharedPreferences("DATACARD", 0).getString("/elements", "toto")).getJSONArray("elements");
			for (int i = 0; i < arr.length(); ++i)
				_mElements.add(new ElementStruct(arr.getJSONObject(i)));
		} catch (JSONException e) {
			Log.d("ORDERFRAGMENT", "EXCEPTION JSON:" + e.toString());
		}
	}

	/**
	 * Create the cardStruct
	 */
	public static void fetchCard() {
		fetchMenus();
		try {
			_mCard = new CardStruct(new JSONObject(StockMenu.instance().read("/alacarte")), _mCats);
//			_mCard = new CardStruct(new JSONObject(_mActivity.getSharedPreferences("DATACARD", 0).getString("/alacarte", "toto")), _mCats);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the menu list
	 */
	public static void fetchMenus() {
		get_mElements();

		JSONArray arr;
		_mMenus.clear();
		_mCompos.clear();
		_mCats.clear();

		//Create all the cats
		try {
			arr = new JSONObject(StockMenu.instance().read("/cats")).getJSONArray("elements");
//			arr = new JSONObject(_mActivity.getSharedPreferences("DATACARD", 0).getString("/cats", "toto")).getJSONArray("elements");
			for (int i = 0; i < arr.length(); ++i) {
				_mCats.add(new CategoryStruct(arr.getJSONObject(i)));
			}
		} catch (JSONException e) {
			Log.d("ORDERFRAGMENT", "EXCEPTION JSON:" + e.toString());
		}

		//Add the dishes to the categories
		for (ElementStruct item : _mElements) {
			for (String idCat : item.get_mIdsCat()) {
				for (CategoryStruct cat : _mCats) {
					if (cat.get_mId().equals(idCat)) {
						cat.get_mIds().add(item.get_mId());
					}
				}
			}
		}

		//Create all the compos and add the categories to them
		try {
			arr = new JSONObject(StockMenu.instance().read("/compos")).getJSONArray("elements");
//			arr = new JSONObject(_mActivity.getSharedPreferences("DATACARD", 0).getString("/compos", "toto")).getJSONArray("elements");
			for (int i = 0; i < arr.length(); ++i) {
				_mCompos.add(new CompositionStruct(arr.getJSONObject(i), _mCats));
			}
		} catch (JSONException e) {
			Log.d("ORDERFRAGMENT", "EXCEPTION JSON:" + e.toString());
		}

		//Create all the menu
		try {
			arr = new JSONObject(StockMenu.instance().read("/menus")).getJSONArray("elements");
//			arr = new JSONObject(_mActivity.getSharedPreferences("DATACARD", 0).getString("/menus", "toto")).getJSONArray("elements");
			for (int i = 0; i < arr.length(); ++i) {
				_mMenus.add(new MenuStruct(arr.getJSONObject(i)));
			}
		} catch (JSONException e) {
			Log.d("ORDERFRAGMENT", "EXCEPTION JSON:" + e.toString());
		}

		//Add the compositions to the menus
		for (CompositionStruct compo : _mCompos) {
			for (MenuStruct menu : _mMenus) {
				if (compo.get_mMenuId().equals(menu.get_mId())) {
					menu.get_mCat().add(compo);
				}
			}
		}
	}

	public static List<ElementStruct> get_mElements() {
		if (_mElements.isEmpty()) {
			fetchElements();
		}
		return _mElements;
	}

	public static OptionsStruct get_mOptions() {
		if (_mOptions == null) {
			fetchOptions();
		}
		return _mOptions;
	}

	/**
	 * Get the card
	 * @return
	 */
	public static CardStruct get_mCard() {
		if (_mCard == null) {
			fetchCard();
		}
		return _mCard;
	}

	/**
	 * Get the menus
	 * @return
	 */
	public static List<MenuStruct> get_mMenus() {
		if (_mMenus.size() == 0) {
			fetchMenus();
		}
		return _mMenus;
	}

	/**
	 * Get the name of an element by its id
	 * @param id
	 * @return
	 */
	public static String getNameElementById(String id) {
		for (ElementStruct elem : _mElements) {
			if (elem.get_mId().equals(id))
				return elem.get_mName();
		}
		return "";
	}

	/**
	 * Get the name of category by its id
	 * @param id
	 * @return
	 */
	public static String getNameCatById(String id, Context context) {
		if (id.equals(get_mCard().get_mId()))
			return context.getString(R.string.card);
		for (MenuStruct menu : _mMenus) {
			if (menu.get_mId().equals(id))
				return menu.get_mName();
		}
		return "";
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		_mRootView = inflater.inflate(R.layout.fragment_order, container, false);

		// Initialize the ViewPager and set an adapter
		ViewPager pager = (ViewPager) _mRootView.findViewById(R.id.pager);
		pager.setAdapter(new MyPagerAdapter(getChildFragmentManager()));
		pager.setOffscreenPageLimit(2);

		// Bind the tabs to the ViewPager
		PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) _mRootView.findViewById(R.id.tabs);
		tabs.setViewPager(pager);

		if (_mDetails != null) {
			OrderOrderFragment.setExistingOrder(_mDetails);
		}

		return _mRootView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		_mActivity = (FragmentActivity) activity;

		((Main) activity).onSectionAttached(
				getArguments().getInt(ARG_SECTION_NUMBER));
	}

	/**
	 * Custom adapter for slider
	 */
	public class MyPagerAdapter extends android.support.v4.app.FragmentPagerAdapter {

		private final String[] TITLES = {getActivity().getString(R.string.menus), getActivity().getString(R.string.card), getActivity().getString(R.string.order)};

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return TITLES[position];
		}

		@Override
		public int getCount() {
			return TITLES.length;
		}

		@Override
		public Fragment getItem(int position) {

			Fragment frag = null;
			if (position == 0)
				frag = OrderMenuFragment.newInstance();
			else if (position == 1)
				frag = OrderCardFragment.newInstance();
			else if (position == 2)
				frag = OrderOrderFragment.newInstance(_mOrder);
			return frag;
		}
	}
}