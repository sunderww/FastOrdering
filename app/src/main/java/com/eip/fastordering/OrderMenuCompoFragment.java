package com.eip.fastordering;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Mewen on 22-Jan-15.
 */
public class OrderMenuCompoFragment extends Fragment{

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    static List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    private static HashMap<String, List<String>> _mListDataNb;
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

        listAdapter = new ExpandableListAdapter(getActivity(), listDataHeader, listDataChild, _mListDataNb, true);

        // setting list adapter
        expListView.setAdapter(listAdapter);
        expListView.setGroupIndicator(null);

        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                EditText text = (EditText)v.findViewById(R.id.nbDish);
                //text.setFocusable(true);
                //text.setFocusableInTouchMode(true);
                //ExpandableListAdapter._childPosition = childPosition;
                //ExpandableListAdapter._groupPosition = groupPosition;
                Log.d("CHILD", "FIIIIIRED");
                return false;
            }
        });

        ImageButton addButton = (ImageButton)rootView.findViewById(R.id.order_compo_rectangle);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TO DO
                //Get every dish + number and add to the order and close fragment
                int groupCount = listAdapter.getGroupCount();
                Log.d("COUNT I MAX", "" + groupCount);
                for (int i = 0; i < groupCount; ++i) {
                    int childCount = listAdapter.getChildrenCount(i);
                    Log.d("CHILD J MAX", "" + childCount);
                    Log.d("CHILD I IP", "" + i);
                    for (int j = 0; j < childCount; ++j) {
                        Log.d("CHILD J IP", "" + j);
                        View view = listAdapter.getChildView(i, j, false, null, null);
                        if (view != null) {
                            TextView txt = (TextView)view.findViewById(R.id.lblListItemRadio);
                            EditText nb = (EditText) view.findViewById(R.id.nbDish);
                            if (nb.getText().length() == 0)
                                Log.d("NOT FOUND", "NB");
                            Log.d("NB", "" + txt.getText() + " " + nb.getText());
                        }
                    }
                }
            }
        });

        return rootView;
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();
        _mListDataNb = new HashMap<String, List<String>>();

        for (MenuStruct item : OrderFragment.get_mMenus()) {
            if (item.get_mName().equals(_mMenu)) {
                for (CompositionStruct compo : item.get_mCat()) {
                    if (compo.get_mNameCompo().equals(_mCompo)) {
                        for (CategoryStruct cat : compo.get_mCat()) {
                            listDataHeader.add(cat.get_mCategoryName());
                            List<String> dishes = new ArrayList<String>();
                            List<String> nb = new ArrayList<String>();
                            for (String dish : cat.get_mIds()) {
                                dishes.add(dish);
                                nb.add("0");
                            }
                            listDataChild.put(listDataHeader.get(listDataHeader.size() - 1), dishes);
                            _mListDataNb.put(listDataHeader.get(listDataHeader.size() - 1), nb);
                        }
                    }
                }
            }
        }
    }

    public static HashMap<String, List<String>> get_mListDataNb() {
        return _mListDataNb;
    }

    public static void set_idmListDataNb(int groupPosition, int childPosition, String value) {
        _mListDataNb.get(listDataHeader.get(groupPosition)).set(childPosition, value);
    }
}
