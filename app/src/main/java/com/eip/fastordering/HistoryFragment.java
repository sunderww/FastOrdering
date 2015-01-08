package com.eip.fastordering;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class HistoryFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    private ArrayList<OrderStruct> items = new ArrayList<OrderStruct>();
    private AdapterHistory adapter;
    private final int sizeList = 20;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static HistoryFragment newInstance(int sectionNumber) {
        HistoryFragment fragment = new HistoryFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public HistoryFragment() {
        JSONObject cmd = new JSONObject();
        try {
            cmd.put("numOrder", "12");
            cmd.put("numTable", "2");
            cmd.put("numPA", "4");
            cmd.put("hour", "12:24");
            cmd.put("date", "01/01/15");
        } catch (JSONException e) {

        }

        addOrderToList(cmd);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_history, container, false);

        /***
         * Create a custom adapter for the listview of orders
         */
        adapter = new AdapterHistory(container.getContext(), items);
        ListView lv = (ListView)rootView.findViewById(R.id.history_list);
        lv.setAdapter(adapter);
        lv.setEmptyView(rootView.findViewById(R.id.history_list_empty));

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                displayPopupOrder((OrderStruct) adapterView.getAdapter().getItem(i));
            }
        });

        return rootView;
    }

    private void displayPopupOrder(OrderStruct item) {
        DialogOrder alertBuilder = new DialogOrder(getActivity(), item);
        alertBuilder.customView().show();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((Main) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

    /***
     * Add an item to the custom list view
     * @param cmd, Command formatted in JSON
     */
    public void addOrderToList(JSONObject cmd) {
        if (items.size() >= sizeList)
            items.remove(sizeList -1);
        items.add(0, new OrderStruct(cmd));
        if (adapter != null)
            adapter.notifyDataSetChanged();
    }

}
