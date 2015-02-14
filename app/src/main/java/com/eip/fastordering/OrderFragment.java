package com.eip.fastordering;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.astuetz.PagerSlidingTabStrip;

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
    private View _mRootView;
    private static FragmentActivity _mActivity;
    static private JSONObject _mOrder;

    /**
     * Methods
     */

    public static OrderFragment newInstance(int sectionNumber, JSONObject order) {
        OrderFragment fragment = new OrderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        _mOrder = order;
        return fragment;
    }

    public OrderFragment() {

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

        return _mRootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        _mActivity = (FragmentActivity)activity;

        ((Main) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

    static public void fetchElements(JSONObject elements) {
        JSONArray arr;
        _mElements.clear();
        try {
            arr = elements.getJSONArray("elements");
            for (int i = 0; i < arr.length(); ++i)
                _mElements.add(new ElementStruct(arr.getJSONObject(i)));
        } catch (JSONException e) {
            Log.d("ORDERFRAGMENT", "EXCEPTION JSON:" + e.toString());
        }
    }

    static public void fetchCard(JSONObject card, JSONObject cats) {
        _mCard = new CardStruct(card, cats);
    }

    static public void fetchMenus(JSONObject menus, JSONArray compos, JSONObject cats) {
        JSONArray arr;
        _mMenus.clear();
        try {
            arr = menus.getJSONArray("elements");
            for (int i = 0; i < arr.length(); ++i) {
                _mMenus.add(new MenuStruct(arr.getJSONObject(i), compos, cats));
            }
        } catch (JSONException e) {
            Log.d("ORDERFRAGMENT", "EXCEPTION JSON:" + e.toString());
        }
    }

    public static CardStruct get_mCard() {
        return _mCard;
    }

    public static ArrayList<MenuStruct> get_mMenus() {
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
    };

    public static String getNameCatById(String id) {
        if (id.equals(_mCard.get_mId()))
            return _mActivity.getString(R.string.card);
        for (MenuStruct menu : _mMenus) {
            if (menu.get_mId().equals(id))
                return menu.get_mName();
        }
        return "";
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