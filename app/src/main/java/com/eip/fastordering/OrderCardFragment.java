package com.eip.fastordering;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Mewen on 18-Jan-15.
 */
public class OrderCardFragment extends Fragment {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    static List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    private static HashMap<String, List<String>> _mListDataNb;
    View _mRootView;

    public static OrderCardFragment newInstance(int position) {
        OrderCardFragment f = new OrderCardFragment();
        Bundle b = new Bundle();
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public OrderCardFragment() {

    }

    private void checkListEmpty() {
        if (_mRootView != null) {
            ImageButton button = (ImageButton) _mRootView.findViewById(R.id.order_compo_rectangle);
            TextView text = (TextView) _mRootView.findViewById(R.id.order_compo_button_text);
            if (listDataHeader.isEmpty()) {
                button.setVisibility(View.GONE);
                text.setVisibility(View.GONE);
            } else {
                button.setVisibility(View.VISIBLE);
                text.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        _mRootView = inflater.inflate(R.layout.fragment_order_card, container, false);
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();
        _mListDataNb = new HashMap<String, List<String>>();

        // get the listview
        expListView = (ExpandableListView) _mRootView.findViewById(R.id.lvExp);

        // preparing list data
        //prepareListData();
        checkListEmpty();

        listAdapter = new ExpandableListAdapter(getActivity(), listDataHeader, listDataChild, true, _mListDataNb, getActivity(), true);

        // setting list adapter
        expListView.setAdapter(listAdapter);
        expListView.setGroupIndicator(null);
        expListView.setEmptyView(_mRootView.findViewById(R.id.order_card_none));
        ((RelativeLayout)_mRootView.findViewById(R.id.order_card_layout)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(v instanceof EditText)) {
                    InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                }
            }
        });

        ImageButton addButton = (ImageButton)_mRootView.findViewById(R.id.order_compo_rectangle);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TO DO
                //Get every dish + number and add to the order and close fragment
                int groupCount = listAdapter.getGroupCount();
                for (int i = 0; i < groupCount; ++i) {
                    int childCount = listAdapter.getChildrenCount(i);
                    for (int j = 0; j < childCount; ++j) {
                        View view = listAdapter.getChildView(i, j, false, null, null);
                        if (view != null) {
                            TextView txt = (TextView)view.findViewById(R.id.lblListItemRadio);
                            EditText nb = (EditText) view.findViewById(R.id.nbDish);
                            Log.d("NB", "" + txt.getText() + " " + nb.getText());
                        }
                    }
                }
            }
        });

        return _mRootView;
    }

    private void prepareListData() {
        for (CategoryStruct item : OrderFragment.get_mCard().get_mCategories()) {
            listDataHeader.add(item.get_mCategoryName());
            List<String> ids = new ArrayList<String>();
            List<String> nb = new ArrayList<String>();
            for (String id : item.get_mIds()) {
                ids.add(id);
                nb.add("0");
            }
            listDataChild.put(listDataHeader.get(listDataHeader.size() - 1), ids);
            _mListDataNb.put(listDataHeader.get(listDataHeader.size() - 1), nb);
        }
    }

    public static HashMap<String, List<String>> get_mListDataNb() {
        return _mListDataNb;
    }

    public static void set_idmListDataNb(int groupPosition, int childPosition, String value) {
        _mListDataNb.get(listDataHeader.get(groupPosition)).set(childPosition, value);
    }
}
