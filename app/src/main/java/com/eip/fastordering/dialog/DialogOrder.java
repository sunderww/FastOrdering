package com.eip.fastordering.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.eip.fastordering.R;
import com.eip.fastordering.fragment.OrderFragment;
import com.eip.fastordering.struct.ContentOrderStruct;
import com.eip.fastordering.struct.ItemStruct;
import com.eip.fastordering.struct.OrderStruct;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class DialogOrder extends AlertDialog {

    private static JSONObject _mOrderDetailed;
    /***
     * Attributes
     */

    private FragmentActivity _mActivity;
    private OrderStruct _mItem;
    private ArrayList<ContentOrderStruct> _mContent = new ArrayList<ContentOrderStruct>();
    private Fragment _mFrag;

    /***
     * Methods
     */

    public DialogOrder(Activity activity, Fragment frag, JSONObject fullOrder) {
        super(activity);

        _mOrderDetailed = fullOrder;
        _mActivity = (FragmentActivity) activity;
        _mFrag = frag;
        _mItem = new OrderStruct(fullOrder);

        getDetailedOrder(fullOrder);
        //TODO Not forget global_comment
    }

//    TODO Delete after demo
//    public DialogOrder(Activity activity, OrderStruct item, Fragment frag) {
//        super(activity);
//
//        _mActivity = (FragmentActivity) activity;
//        _mFrag = frag;
//        _mItem = item;
//
//        JSONObject order = new JSONObject();
//        JSONArray arr = new JSONArray();
//        JSONObject comm = new JSONObject();
//        JSONArray arrComm = new JSONArray();
//        JSONObject itemOrder = new JSONObject();
//
//        JSONObject comm2 = new JSONObject();
//        JSONArray arrComm2 = new JSONArray();
//        JSONObject itemOrder2 = new JSONObject();
//
//        try {
//            itemOrder.put("id", "572f7a56937726dc7ab8f905");
//            itemOrder.put("comment", "bla bla bla");
//            itemOrder.put("options", "bleu");
//            itemOrder.put("status", "2");
//            itemOrder.put("qty", 5);
//            arrComm.put(itemOrder);
//            comm.put("content", arrComm);
//            comm.put("menuId", "572f77e5e4e081cc7a7006d2");
//            arr.put(comm);
//
//            itemOrder2.put("id", "572f79b0937726dc7ab8f8fe");
//            itemOrder2.put("comment", "bla bla bla");
//            itemOrder2.put("options", "bleu");
//            itemOrder2.put("status", "2");
//            itemOrder2.put("qty", 5);
//            arrComm2.put(itemOrder2);
//            comm2.put("content", arrComm2);
//            comm2.put("menuId", "572f7a20937726dc7ab8f903");
//            arr.put(comm2);
//
//            order.put("order", arr);
//        } catch (JSONException e) {
//
//        }
//        getDetailedOrder(order);
//    }

    public AlertDialog customView() {
        AlertDialog.Builder builder = new AlertDialog.Builder(_mActivity);

        LayoutInflater inflater = _mActivity.getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_order, null);
        builder.setView(view);
        builder.setPositiveButton(R.string.dialog_order_modify, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //TODO Delete after demo
//                JSONObject order = new JSONObject();
//                JSONArray arr = new JSONArray();
//                JSONObject comm = new JSONObject();
//                JSONArray arrComm = new JSONArray();
//                JSONObject itemOrder = new JSONObject();
//
//                JSONObject comm2 = new JSONObject();
//                JSONArray arrComm2 = new JSONArray();
//                JSONObject itemOrder2 = new JSONObject();
//
//                try {
//                    itemOrder.put("id", "572f7a56937726dc7ab8f905");
//                    itemOrder.put("comment", "bla bla bla");
//                    itemOrder.put("options", "bleu");
//                    itemOrder.put("status", "2");
//                    itemOrder.put("qty", 5);
//                    arrComm.put(itemOrder);
//                    comm.put("content", arrComm);
//                    comm.put("menuId", "572f77e5e4e081cc7a7006d2");
//                    arr.put(comm);
//
//                    itemOrder2.put("id", "572f79b0937726dc7ab8f8fe");
//                    itemOrder2.put("comment", "bla bla bla");
//                    itemOrder2.put("options", "bleu");
//                    itemOrder2.put("status", "2");
//                    itemOrder2.put("qty", 5);
//                    arrComm2.put(itemOrder2);
//                    comm2.put("content", arrComm2);
//                    comm2.put("menuId", "572f7a20937726dc7ab8f903");
//                    arr.put(comm2);
//
//                    order.put("order", arr);
//                } catch (JSONException e) {
//
//                }
                //TODO End delete

                Fragment frag = new OrderFragment().newInstance(1, _mOrderDetailed, _mItem);
                FragmentManager fm = _mActivity.getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(_mFrag.getId(), frag).addToBackStack(null).commit();
            }
        });
        builder.setNegativeButton(R.string.dialog_order_close, new OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        ((TextView) view.findViewById(R.id.dialog_order_table)).setText(((TextView) view.findViewById(R.id.dialog_order_table)).getText() + _mItem.get_mNumTable());
        ((TextView) view.findViewById(R.id.dialog_order_pa)).setText(((TextView) view.findViewById(R.id.dialog_order_pa)).getText() + _mItem.get_mNumPA());
        ((TextView) view.findViewById(R.id.dialog_order_hour)).setText(((TextView) view.findViewById(R.id.dialog_order_hour)).getText() + _mItem.get_mDate() + getContext().getString(R.string.at) + _mItem.get_mHour());

        String lineSep = System.getProperty("line.separator");

        String contenu = "";
        for (int i = 0; i < _mContent.size(); ++i) {
            ContentOrderStruct content = _mContent.get(i);
            contenu += lineSep + lineSep + getContext().getString(R.string.dialog_box_menu) + OrderFragment.getNameCatById(content.get_mId());
            for (int j = 0; j < content.get_mItems().size(); ++j) {
                ItemStruct item = content.get_mItems().get(j);
                contenu += lineSep + '\t' + '\t' + getContext().getString(R.string.dialog_box_dish) + OrderFragment.getNameElementById(item.get_mId()) + " x" + item.get_mQty();
            }
        }

        ((TextView) view.findViewById(R.id.dialog_order_content)).setText(((TextView) view.findViewById(R.id.dialog_order_content)).getText() + " " + contenu);

        return builder.create();
    }

    private void getDetailedOrder(JSONObject order) {
        JSONArray arr;
        try {
            arr = order.getJSONArray("order");
            for (int i = 0; i < arr.length(); ++i) {
                _mContent.add(new ContentOrderStruct(arr.getJSONObject(i)));
            }
        } catch (JSONException e) {
            Log.d("DIALOGORDER", "EXCEPTION JSON");
        }
    }
}
