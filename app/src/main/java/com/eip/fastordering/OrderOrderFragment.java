package com.eip.fastordering;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Mewen on 18-Jan-15.
 */
public class OrderOrderFragment extends Fragment {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    View _mRootView;

    public static OrderOrderFragment newInstance(int position) {
        OrderOrderFragment f = new OrderOrderFragment();
        Bundle b = new Bundle();
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public OrderOrderFragment() {

    }

    private void checkListEmpty() {
        if (_mRootView != null) {
            ImageButton button = (ImageButton) _mRootView.findViewById(R.id.order_order_rectangle);
            TextView text = (TextView) _mRootView.findViewById(R.id.order_order_button_text);
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
        _mRootView = inflater.inflate(R.layout.fragment_order_order, container, false);
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // get the listview
        expListView = (ExpandableListView) _mRootView.findViewById(R.id.lvExp);
        expListView.setEmptyView(_mRootView.findViewById(R.id.order_order_none_text));

        // preparing list data
        prepareListData();
        checkListEmpty();

        listAdapter = new ExpandableListAdapter(getActivity(), listDataHeader, listDataChild, false, null, null, false);

        // setting list adapter
        expListView.setAdapter(listAdapter);
        expListView.setGroupIndicator(null);

        return _mRootView;
    }

    private void prepareListData() {
    }
}
