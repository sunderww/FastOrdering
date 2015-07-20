package com.eip.fastordering.fragment;

import android.app.Activity;
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
import com.eip.fastordering.struct.CardStruct;
import com.eip.fastordering.struct.CategoryStruct;
import com.eip.fastordering.struct.CompositionStruct;
import com.eip.fastordering.struct.ElementStruct;
import com.eip.fastordering.struct.MenuStruct;
import com.eip.fastordering.struct.OrderStruct;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class OrderFragment extends Fragment {

    /***
     * Attributes
     */

    private static final String ARG_SECTION_NUMBER = "section_number";

    private static ArrayList<ElementStruct> _mElements = new ArrayList<ElementStruct>();
    private static CardStruct _mCard;
    private static ArrayList<MenuStruct> _mMenus = new ArrayList<MenuStruct>();
    private static ArrayList<CategoryStruct> _mCats = new ArrayList<CategoryStruct>();
    private static ArrayList<CompositionStruct> _mCompos = new ArrayList<CompositionStruct>();
    private static FragmentActivity _mActivity;
    static private JSONObject _mOrder;
    static private OrderStruct _mDetails;
    private View _mRootView;

    public OrderFragment() {

    }

    /**
     * Methods
     */

    public static OrderFragment newInstance(int sectionNumber, JSONObject order, OrderStruct details) {
        OrderFragment fragment = new OrderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        _mOrder = order;
        _mDetails = details;
        return fragment;
    }

    static public void fetchElements() {
        Log.d("ORDERFRAGMENT", "BEFORE");
        try {
            Log.d("ORDERFRAGMENT", "READ PREF=" + _mActivity.getSharedPreferences("DATACARD", 0).getString("/elements", "toto"));
        } catch(Exception e) {
            Log.d("ORDERFRAGMENT", "EXCEPTION=" + e.toString());
        }
        Log.d("ORDERFRAGMENT", "AFTER");

        JSONArray arr;
        _mElements.clear();
        try {
            arr = new JSONObject(_mActivity.getSharedPreferences("DATACARD", 0).getString("/elements", "toto")).getJSONArray("elements");
            for (int i = 0; i < arr.length(); ++i)
                _mElements.add(new ElementStruct(arr.getJSONObject(i)));
        } catch (JSONException e) {
            Log.d("ORDERFRAGMENT", "EXCEPTION JSON:" + e.toString());
        }
    }

    static public void fetchCard() {
        fetchMenus();
        try {
            Log.d("FETCH ELEMENTS", _mActivity.getSharedPreferences("DATACARD", 0).getString("/alacarte", "toto"));
            _mCard = new CardStruct(new JSONObject(_mActivity.getSharedPreferences("DATACARD", 0).getString("/alacarte", "toto")), _mCats);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static public void fetchMenus() {
        fetchElements();

        JSONArray arr;
        _mMenus.clear();
        _mCompos.clear();
        _mCats.clear();

        //Create all the cats
        try {
            arr = new JSONObject(_mActivity.getSharedPreferences("DATACARD", 0).getString("/cats", "toto")).getJSONArray("elements");
            for (int i = 0; i < arr.length(); ++i) {
                _mCats.add(new CategoryStruct(arr.getJSONObject(i)));
            }
        } catch (JSONException e) {
            Log.d("ORDERFRAGMENT", "EXCEPTION JSON:" + e.toString());
        }

        //Add the dishes to the categories
        for (ElementStruct item : _mElements) {
            for (String idCat: item.get_mIdsCat()) {
                for (CategoryStruct cat: _mCats) {
                    if (cat.get_mId().equals(idCat)) {
                        cat.get_mIds().add(item.get_mId());
                    }
                }
            }
        }

        //Create all the compos and add the categories to them
        try {
            arr = new JSONObject(_mActivity.getSharedPreferences("DATACARD", 0).getString("/compos", "toto")).getJSONArray("elements");
            for (int i = 0; i < arr.length(); ++i) {
                _mCompos.add(new CompositionStruct(arr.getJSONObject(i), _mCats));
            }
        } catch (JSONException e) {
            Log.d("ORDERFRAGMENT", "EXCEPTION JSON:" + e.toString());
        }

        //Create all the menu
        try {
            arr = new JSONObject(_mActivity.getSharedPreferences("DATACARD", 0).getString("/menus", "toto")).getJSONArray("elements");
            for (int i = 0; i < arr.length(); ++i) {
                _mMenus.add(new MenuStruct(arr.getJSONObject(i)));
            }
        } catch (JSONException e) {
            Log.d("ORDERFRAGMENT", "EXCEPTION JSON:" + e.toString());
        }

        //Add the compositions to the menus
        for (CompositionStruct compo : _mCompos) {
            for (MenuStruct menu: _mMenus) {
                if (compo.get_mMenuId().equals(menu.get_mId())) {
                    menu.get_mCat().add(compo);
                }
            }
        }
    }

    /* OLD */
//    static public void fetchCard(JSONObject card, JSONObject cats) {
//        _mCard = new CardStruct(card, cats);
//    }

    public static CardStruct get_mCard() {
        if (_mCard == null) {
            fetchCard();
        }
        return _mCard;
    }

    /* OLD */
//    static public void fetchMenus(JSONObject menus, JSONArray compos, JSONObject cats) {
//        JSONArray arr;
//        _mMenus.clear();
//        try {
//            arr = menus.getJSONArray("elements");
//            for (int i = 0; i < arr.length(); ++i) {
//                _mMenus.add(new MenuStruct(arr.getJSONObject(i), compos, cats));
//            }
//        } catch (JSONException e) {
//            Log.d("ORDERFRAGMENT", "EXCEPTION JSON:" + e.toString());
//        }
//    }

    public static ArrayList<MenuStruct> get_mMenus() {
        if (_mMenus.size() == 0) {
            fetchMenus();
        }
        return _mMenus;
    }

    public static ArrayList<ElementStruct> get_mElements() {
        return _mElements;
    }

    public static String getNameElementById(String id) {
        for (ElementStruct elem : _mElements) {
            if (elem.get_mId().equals(id))
                return elem.get_mName();
        }
      return "";
    }

    public static String getNameCatById(String id) {
        if (id.equals(_mCard.get_mId()))
            return _mActivity.getString(R.string.card);
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

        Log.d("ORDERFRAGMENT", "ONCREATEVIEW");

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
        _mActivity = (FragmentActivity)activity;

        ((Main) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

    public class MyPagerAdapter extends android.support.v4.app.FragmentPagerAdapter {

        private final String[] TITLES = { getActivity().getString(R.string.menus), getActivity().getString(R.string.card), getActivity().getString(R.string.order)};

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
                frag = OrderMenuFragment.newInstance(position);
            else if (position == 1)
                frag = OrderCardFragment.newInstance(position);
            else if (position == 2)
                frag = OrderOrderFragment.newInstance(position, _mOrder);
            return frag;
        }
    }
}