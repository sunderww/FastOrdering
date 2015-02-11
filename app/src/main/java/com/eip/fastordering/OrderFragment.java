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
    private FragmentActivity _mActivity;
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

        //TO delete
//        JSONObject elements = new JSONObject();
//        JSONArray arr = new JSONArray();
//        JSONObject elem = new JSONObject();
//        JSONObject elem2 = new JSONObject();
//        JSONObject elem3 = new JSONObject();
//        try {
//            elem.put("id", "1");
//            elem.put("price", 0);
//            elem.put("name", "Mais");
//
//            elem2.put("id", "2");
//            elem2.put("price", 12);
//            elem2.put("name", "Surimi");
//
//            elem3.put("id", "3");
//            elem3.put("price", 12);
//            elem3.put("name", "Steack");
//            arr.put(elem);
//            arr.put(elem2);
//            arr.put(elem3);
//            elements.put("elements", arr);
//        } catch (JSONException e) {
//
//        }
//        Log.d("ORIGINAL ELEM", elements.toString());
//        fetchElements(elements);

//        JSONObject carte = new JSONObject();
//        JSONObject inside = new JSONObject();
//        JSONArray compo = new JSONArray();
//        JSONObject cat = new JSONObject();
//        JSONArray ids = new JSONArray();
//        JSONObject cat3 = new JSONObject();
//        JSONArray ids3 = new JSONArray();
//        try {
//            ids.put("1");
//            ids.put("2");
//            ids3.put("3");
//            cat.put("name", "Entrees");
//            cat.put("ids", ids);
//            compo.put(cat);
//            cat3.put("name", "Plats");
//            cat3.put("ids", ids3);
//            compo.put(cat3);
//            inside.put("id", "1451");
//            inside.put("composition", compo);
//            carte.put("alacarte", inside);
//        } catch (JSONException e) {
//
//        }
//        fetchCard(carte);

//        JSONObject menus = new JSONObject();
//        JSONArray menuarr = new JSONArray();
//        JSONObject menu = new JSONObject();
//        JSONObject menu2 = new JSONObject();
//        JSONArray compos2 = new JSONArray();
//        JSONArray compos = new JSONArray();
//        JSONObject compo1 = new JSONObject();
//        JSONObject compo2 = new JSONObject();
//        JSONArray cats = new JSONArray();
//        JSONObject cat1 = new JSONObject();
//        JSONObject cat2 = new JSONObject();
//        JSONArray ids1 = new JSONArray();
//        JSONArray ids2 = new JSONArray();
//        try {
//            ids1.put("1");
//            ids1.put("2");
//            ids2.put("3");
//            cat1.put("name", "Entrees");
//            cat1.put("ids", ids1);
//            cat2.put("name", "Plats");
//            cat2.put("ids", ids2);
//            cats.put(cat1);
//            cats.put(cat2);
//
//            compo1.put("name", "Entrees + Plats");
//            compo1.put("price", 12);
//            compo1.put("cat", cats);
//
//            compo2.put("name", "Plats + Desserts");
//            compo2.put("price", 12);
//            compo2.put("cat", cats);
//
//            compos.put(compo1);
//            compos.put(compo2);
//
//            menu.put("compositions", compos);
//            menu.put("name", "Mousaillon");
//            menu.put("id", "1212");
//
//            compos2.put(compo1);
//
//            menu2.put("compositions", compos2);
//            menu2.put("name", "Pirate");
//            menu2.put("id", "1213");
//
//            menuarr.put(menu);
//            menuarr.put(menu2);
//
//            menus.put("menus", menuarr);
//        } catch (JSONException e) {
//
//        }
//        Log.d("MENUS", menus.toString());
//        fetchMenus(menus);


        /* New menu */
//        JSONArray arr1 = new JSONArray();
//        JSONObject menu1 = new JSONObject();
//        JSONArray arrmenus = new JSONArray();
//        JSONObject menus = new JSONObject();
//        try {
//            arr1.put("compo1");
//            menu1.put("id", "menu1");
//            menu1.put("name", "Mousaillon");
//            menu1.put("compo", arr1);
//            arrmenus.put(menu1);
//            menus.put("elements", arrmenus);
//        } catch (JSONException e) {
//
//        }
//
//        JSONArray arrcat = new JSONArray();
//        JSONObject compo1 = new JSONObject();
//        JSONArray arrcompos = new JSONArray();
//        try {
//            arrcat.put("cat1");
//            compo1.put("cat", arrcat);
//            compo1.put("id", "compo1");
//            compo1.put("price", 12);
//            compo1.put("name", "Plats + desserts");
//            arrcompos.put(compo1);
//        } catch (JSONException e) {
//
//        }
//
//        JSONArray arrelem = new JSONArray();
//        JSONObject cate = new JSONObject();
//        JSONArray arrcats = new JSONArray();
//        JSONObject cats = new JSONObject();
//        try {
//            arrelem.put("1");
//            arrelem.put("2");
//            arrelem.put("3");
//            cate.put("ids", arrelem);
//            cate.put("id", "cat1");
//            cate.put("name", "plat");
//            arrcats.put(cate);
//            cats.put("elements", arrcats);
//        } catch (JSONException e) {
//
//        }
//
//        fetchMenus(menus, arrcompos, cats);
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

    static public void fetchElements(JSONObject elements) {
        JSONArray arr;
        _mElements.clear();
        Log.d("IN ELEMENTS", elements.toString());
        try {
            arr = elements.getJSONArray("elements");
            for (int i = 0; i < arr.length(); ++i)
                _mElements.add(new ElementStruct(arr.getJSONObject(i)));
        } catch (JSONException e) {

        }
        Log.d("ORDER", "1");
    }

//    static private void fetchCard(JSONObject card) {
//        _mCard = new CardStruct(card);
//        Log.d("ORDER", "2");
//    }

    static public void fetchCard(JSONObject card, JSONObject cats) {
        _mCard = new CardStruct(card, cats);
        Log.d("ORDER", "2");
    }

//    static private void fetchMenus(JSONObject menus) {
//        JSONArray arr;
//        _mMenus.clear();
//        try {
//            arr = menus.getJSONArray("menus");
//            for (int i = 0; i < arr.length(); ++i) {
//                _mMenus.add(new MenuStruct(arr.getJSONObject(i)));
//            }
//        } catch (JSONException e) {
//
//        }
//        Log.d("ORDER", "3");
//    }

    static public void fetchMenus(JSONObject menus, JSONArray compos, JSONObject cats) {
        JSONArray arr;
        _mMenus.clear();
        try {
            Log.d("PRINT JSON MENUS", "" + menus.toString());
            arr = menus.getJSONArray("elements");
            for (int i = 0; i < arr.length(); ++i) {
                _mMenus.add(new MenuStruct(arr.getJSONObject(i), compos, cats));
            }
        } catch (JSONException e) {
            Log.d("EXCEPT", "MENU JSON");
        }
        Log.d("ORDER", "3");
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
            return "A la carte";
        for (MenuStruct menu : _mMenus) {
            if (menu.get_mId().equals(id))
                return menu.get_mName();
        }
        return "";
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
                frag = OrderOrderFragment.newInstance(position, _mOrder);
            return frag;
        }
    }
}