package com.eip.fastordering;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Mewen on 18-Jan-15.
 */
public class OrderMenuFragment extends Fragment {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    public static OrderMenuFragment newInstance(int position) {
        OrderMenuFragment f = new OrderMenuFragment();
        Bundle b = new Bundle();
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public OrderMenuFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_order_menu, container, false);

        // get the listview
        expListView = (ExpandableListView) rootView.findViewById(R.id.lvExp);

        // preparing list data
        prepareListData();

        listAdapter = new ExpandableListAdapter(getActivity(), listDataHeader, listDataChild, null, false, null);

        // setting list adapter
        expListView.setAdapter(listAdapter);
        expListView.setGroupIndicator(null);

        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                if (expListView.getExpandableListAdapter().getChildrenCount(i) > 1)
                    Toast.makeText(getActivity(), "Ce menu a plusieurs choix", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getActivity(), "Menu unique", Toast.LENGTH_SHORT).show();
                return false;
            }
        });


        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Fragment frag = new OrderMenuCompoFragment().newInstance(listDataHeader.get(groupPosition), listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition));

                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(getParentFragment().getId(), frag).addToBackStack(null).commit();

                return false;
            }
        });

        return rootView;
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        for(MenuStruct item : OrderFragment.get_mMenus()) {
            listDataHeader.add(item.get_mName());

            if (item.get_mCat().size() > 0) {
                List<String> subMenus = new ArrayList<String>();

                for (CompositionStruct compo : item.get_mCat()) {
                    subMenus.add(compo.get_mNameCompo());
                }
                listDataChild.put(listDataHeader.get(listDataHeader.size() - 1), subMenus);
            }
        }
    }
}
