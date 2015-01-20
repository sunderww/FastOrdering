package com.eip.fastordering;

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
    private FragmentActivity _mActivity;

    /**
     * Methods
     */

    public static OrderFragment newInstance(int sectionNumber) {
        OrderFragment fragment = new OrderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);

        //TO delete
        JSONObject elements = new JSONObject();
        JSONArray arr = new JSONArray();
        JSONObject elem = new JSONObject();
        try {
            elem.put("id", "26");
            elem.put("price", 0);
            elem.put("name", "toto");
            arr.put(elem);
            elem.put("price", "23");
            elem.put("id", 12);
            elem.put("name", "salade");
            arr.put(elem);
            elements.put("elements", arr);
        } catch (JSONException e) {

        }
        getElements(elements);

        JSONObject carte = new JSONObject();
        JSONObject inside = new JSONObject();
        JSONArray compo = new JSONArray();
        JSONObject cat = new JSONObject();
        JSONArray ids = new JSONArray();
        try {
            ids.put("1");
            ids.put("2");
            cat.put("name", "entrees");
            cat.put("ids", ids);
            compo.put(cat);
            inside.put("composition", compo);
            carte.put("alacarte", inside);
        } catch (JSONException e) {

        }
        getCard(carte);

        JSONObject menus = new JSONObject();
        JSONArray menuarr = new JSONArray();
        JSONObject menu = new JSONObject();
        JSONArray compos = new JSONArray();
        JSONObject compo1 = new JSONObject();
        JSONArray cats = new JSONArray();
        JSONObject cat1 = new JSONObject();
        JSONArray ids1 = new JSONArray();
        try {
            ids1.put("1");
            ids1.put("2");
            cat1.put("name", "plats");
            cat1.put("ids", ids1);
            cats.put(cat1);
            compo1.put("price", 12);
            compo1.put("cat", cats);
            compos.put(compo1);
            menu.put("compositions", compos);
            menu.put("name", "mousaillon");
            menu.put("id", "1212");
            menuarr.put(menu);
            menus.put("menus", menuarr);
        } catch (JSONException e) {

        }
        getMenus(menus);
        //END TO delete

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

    static private void getElements(JSONObject elements) {
        JSONArray arr;
        try {
            arr = elements.getJSONArray("elements");
            for (int i = 0; i < arr.length(); ++i)
                _mElements.add(new ElementStruct(arr.getJSONObject(i)));
        } catch (JSONException e) {

        }
        Log.d("ORDER", "1");
    }

    static private void getCard(JSONObject card) {
        _mCard = new CardStruct(card);
        Log.d("ORDER", "2");
    }

    static private void getMenus(JSONObject menus) {
        JSONArray arr;
        try {
            arr = menus.getJSONArray("menus");
            for (int i = 0; i < arr.length(); ++i) {
                _mMenus.add(new MenuStruct(arr.getJSONObject(i)));
            }
        } catch (JSONException e) {

        }
        Log.d("ORDER", "3");
    }

    public class MyPagerAdapter extends android.support.v4.app.FragmentPagerAdapter {

        private final String[] TITLES = { "Menus", "A la carte", "Commande" };

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
                frag = OrderOrderFragment.newInstance(position);
            return frag;
        }
    }
}
