package com.eip.fastordering;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Mewen on 22-Jan-15.
 */
public class OrderMenuCompoFragment extends Fragment{

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    String _mMenu;
    String _mCompo;

    public static OrderMenuCompoFragment newInstance(String menuId, String compo) {
        OrderMenuCompoFragment instance = new OrderMenuCompoFragment();
        Bundle b = new Bundle();
        b.putString("menuId", menuId);
        b.putString("compo", compo);
        instance.setArguments(b);
        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public OrderMenuCompoFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle args = getArguments();
        _mMenu = args.getString("menuId");
        _mCompo = args.getString("compo");

        View rootView = inflater.inflate(R.layout.fragment_order_menu_compo, container, false);

        // get the listview
        expListView = (ExpandableListView) rootView.findViewById(R.id.lvExp);

        // preparing list data
        prepareListData();

        listAdapter = new ExpandableListAdapter(getActivity(), listDataHeader, listDataChild, true);

        // setting list adapter
        expListView.setAdapter(listAdapter);
        expListView.setGroupIndicator(null);

        ImageButton addButton = (ImageButton)rootView.findViewById(R.id.order_compo_rectangle);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TO DO
                //Get every dish + number and add to the order and close fragment
            }
        });

        return rootView;
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        for (MenuStruct item : OrderFragment.get_mMenus()) {
            if (item.get_mName().equals(_mMenu)) {
                for (CompositionStruct compo : item.get_mCat()) {
                    if (compo.get_mNameCompo().equals(_mCompo)) {
                        for (CategoryStruct cat : compo.get_mCat()) {
                            listDataHeader.add(cat.get_mCategoryName());
                            List<String> dishes = new ArrayList<String>();
                            for (String dish : cat.get_mIds()) {
                                dishes.add(dish);
                            }
                            listDataChild.put(listDataHeader.get(listDataHeader.size() - 1), dishes);
                        }
                    }
                }
            }
        }
    }
}
