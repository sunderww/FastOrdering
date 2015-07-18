package com.eip.fastordering.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.eip.fastordering.R;
import com.eip.fastordering.activity.Main;
import com.eip.fastordering.adapter.AdapterHistory;
import com.eip.fastordering.dialog.DialogOrder;
import com.eip.fastordering.struct.OrderStruct;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class HistoryFragment extends Fragment {

    /***
     * Attributes
     */

    private static final String ARG_SECTION_NUMBER = "section_number";
    static private final int _mSizeList = 20;
    static public ArrayList<OrderStruct> _mItems = new ArrayList<OrderStruct>();
    static private Fragment _mFragment;
    static private AdapterHistory _mAdapter;
    static private JSONObject _mFullOrder;

    public HistoryFragment() {

    }

    /***
     * Methods
     */

    public static HistoryFragment newInstance(int sectionNumber) {
        _mFragment = new HistoryFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        _mFragment.setArguments(args);

        //TODO Delete after demo
        JSONObject orders = new JSONObject();
        JSONArray arr = new JSONArray();
        _mItems.clear();
        JSONObject cmd = new JSONObject();
        try {
            cmd.put("numOrder", "12");
            cmd.put("numTable", "2");
            cmd.put("numPA", "4");
            cmd.put("hour", "12:24");
            cmd.put("date", "01/01/15");
            arr.put(cmd);
        } catch (JSONException e) {

        }

        try {
            cmd.put("hour", "12:25");
            arr.put(cmd);
            orders.put("orders", arr);
        } catch (JSONException e) {

        }
        getLastOrders(orders);
        //TODO End delete

        return (HistoryFragment)_mFragment;
    }

    /***
     * Add an item to the custom list view
     * @param cmd, Command formatted in JSON
     */
    static public void addOrderToList(JSONObject cmd) {
        if (_mItems.size() >= _mSizeList)
            _mItems.remove(_mSizeList -1);
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

        ListView lv = (ListView)rootView.findViewById(R.id.history_list);
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
        //TODO Uncomment after demo
//        JSONObject arg = new JSONObject();
//        try {
//            arg.put("orderId", item.get_mNumOrder());
//        } catch (JSONException e) {
//            Log.d("HISTORY FRAGMENT", "EXCEPTION JSON:" + e.toString());
//        }
//
//        LoginActivity._mSocket.emit("get_order", new IOAcknowledge() {
//            @Override
//            public void ack(Object... objects) {
//                try {
//                    _mFullOrder = new JSONObject(objects[0].toString());
//                    DialogOrder alertBuilder = new DialogOrder(getActivity(), _mFragment, _mFullOrder);
//                    alertBuilder.customView().show();
//                } catch (JSONException e) {
//
//                }
//            }
//        }, arg);

//        TODO For demo
        DialogOrder alertBuilder = new DialogOrder(getActivity(), item, this);
        alertBuilder.customView().show();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((Main) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

}
