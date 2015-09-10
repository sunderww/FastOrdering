package com.eip.fastordering.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.eip.fastordering.R;
import com.eip.fastordering.activity.LoginActivity;
import com.eip.fastordering.activity.Main;
import com.eip.fastordering.adapter.AdapterHistory;
import com.eip.fastordering.dialog.DialogOrder;
import com.eip.fastordering.struct.OrderStruct;
import com.github.nkzawa.socketio.client.Ack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class HistoryFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final int _mSizeList = 20;
    public static List<OrderStruct> _mItems = new ArrayList<>();
    private static Fragment _mFragment;
    private static AdapterHistory _mAdapter;

    /**
     * Constructor
     */
    public HistoryFragment() {

    }

    public static HistoryFragment newInstance(int sectionNumber) {
        _mFragment = new HistoryFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        _mFragment.setArguments(args);
        return (HistoryFragment) _mFragment;
    }

    /***
     * Add an item to the custom list view
     *
     * @param cmd, Command formatted in JSON
     */
    static public void addOrderToList(JSONObject cmd) {
        if (_mItems.size() >= _mSizeList)
            _mItems.remove(_mSizeList - 1);
        _mItems.add(0, new OrderStruct(cmd));
        if (_mAdapter != null)
            _mAdapter.notifyDataSetChanged();
    }

    static public void getLastOrders(JSONObject orders) {
        JSONArray arrayOrders = null;
        try {
            arrayOrders = orders.getJSONArray("orders");
        } catch (JSONException e) {

        }
        for (int i = 0; i < arrayOrders.length(); ++i) {
            try {
                addOrderToList(arrayOrders.getJSONObject(i));
            } catch (JSONException e) {

            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_history, container, false);

        /***
         * Create a custom adapter for the listview of orders
         */
        _mAdapter = new AdapterHistory(container.getContext(), _mItems);

        ListView lv = (ListView) rootView.findViewById(R.id.history_list);
        lv.setAdapter(_mAdapter);
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
        JSONObject msg = new JSONObject();
        try {
            msg.put("order", item.get_mNumOrder());
//			LoginActivity._mSocket.emit("get_order", new IOAcknowledgeGetOrder(this, getActivity()), msg);
            Log.d("GETORDER", "GETORDER=" + msg.toString());

            LoginActivity._mSocket.emit("get_order", msg, new Ack() {
                @Override
                public void call(final Object... argss) {
                    JSONObject rep = null;
                    try {
                        rep = new JSONObject(argss[0].toString());
                        Log.d("IOACKNOWLEDFE", "FULL ORDER=" + rep.toString());
                    } catch (JSONException e) {
                        Log.d("IOACKNOWLEDGE", "EXCEPTION JSON:" + e.toString());
                    }

                    HistoryFragment.this.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                new DialogOrder(HistoryFragment.this.getActivity(), HistoryFragment.this, new JSONObject(argss[0].toString())).customView().show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

//                    new DialogOrder(HistoryFragment.this.getActivity(), HistoryFragment.this, rep).customView().show();
                }
            });
        } catch (JSONException e) {
            Log.d("LOGINACTIVITY", "EXCEPTION JSON:" + e.toString());
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((Main) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

}
