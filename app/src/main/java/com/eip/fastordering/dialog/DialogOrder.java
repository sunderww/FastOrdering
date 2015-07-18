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

    DialogOrder(Activity activity, Fragment frag, JSONObject fullOrder) {
        super(activity);

        _mOrderDetailed = fullOrder;
        _mActivity = (FragmentActivity) activity;
        _mFrag = frag;
        _mItem = new OrderStruct(fullOrder);

        getDetailedOrder(fullOrder);

        //TODO Not forget global_comment
    }

    //TODO Delete after demo
    public DialogOrder(Activity activity, OrderStruct item,Fragment frag) {
        super(activity);

        _mActivity = (FragmentActivity) activity;
        _mFrag = frag;
        _mItem = item;

        JSONObject order = new JSONObject();
        JSONArray arr = new JSONArray();
        JSONObject comm = new JSONObject();
        JSONArray arrComm = new JSONArray();
        JSONObject itemOrder = new JSONObject();

        try {
            itemOrder.put("id", "54d9779f62c30f693817538f");
            itemOrder.put("comment", "bla bla bla");
            itemOrder.put("options", "bleu");
            itemOrder.put("status", "2");
            itemOrder.put("qty", 2);
            arrComm.put(itemOrder);
            comm.put("content", arrComm);
            comm.put("menuId", "54d978540bc970af38e34535");
            arr.put(comm);
            order.put("order", arr);
        } catch (JSONException e) {

        }
        getDetailedOrder(order);
    }

    public AlertDialog customView() {
        AlertDialog.Builder builder = new AlertDialog.Builder(_mActivity);

        LayoutInflater inflater = _mActivity.getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_order, null);
        builder.setView(view);
        builder.setPositiveButton(R.string.dialog_order_modify, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //TODO Delete after demo
                JSONObject content = new JSONObject();
                JSONArray arrContent = new JSONArray();
                JSONObject menu = new JSONObject();
                JSONArray arrMenu = new JSONArray();
                JSONObject order = new JSONObject();

                try {
                    content.put("id", "54d9779f62c30f693817538f");
                    content.put("qty", "1");
                    content.put("comment", "blabla");
                    content.put("status", "0");
                    content.put("options", "");
                    arrContent.put(content);
                    menu.put("content", arrContent);
                    menu.put("menuId", "54d978540bc970af38e34535");
                    menu.put("globalComment", "blablabla");
                    arrMenu.put(menu);
                    order.put("order", arrMenu);
                } catch (JSONException e) {

                }
                //TODO End delete

                Fragment frag = new OrderFragment().newInstance(1, order, _mItem);
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

        ((TextView)view.findViewById(R.id.dialog_order_title)).setText(((TextView)view.findViewById(R.id.dialog_order_title)).getText() + _mItem.get_mNumOrder());
        ((TextView)view.findViewById(R.id.dialog_order_table)).setText(((TextView)view.findViewById(R.id.dialog_order_table)).getText() + _mItem.get_mNumTable());
        ((TextView)view.findViewById(R.id.dialog_order_pa)).setText(((TextView)view.findViewById(R.id.dialog_order_pa)).getText() + _mItem.get_mNumPA());
        ((TextView)view.findViewById(R.id.dialog_order_hour)).setText(((TextView)view.findViewById(R.id.dialog_order_hour)).getText() + _mItem.get_mDate() + getContext().getString(R.string.at) + _mItem.get_mHour());

        String lineSep = System.getProperty("line.separator");

        //TODO Modify to better
        String contenu = "";
        for (int i = 0; i < _mContent.size(); ++i) {
            ContentOrderStruct content = _mContent.get(i);
            contenu += lineSep + lineSep + getContext().getString(R.string.dialog_box_menu) + OrderFragment.getNameCatById(content.get_mId());
            for (int j = 0; j < content.get_mItems().size(); ++j) {
                ItemStruct item = content.get_mItems().get(j);
                contenu += lineSep + getContext().getString(R.string.dialog_box_dish) + OrderFragment.getNameElementById(item.get_mId());
                if (item.get_mOptions().length() > 0)
                    contenu += getContext().getString(R.string.dialog_box_option) + item.get_mOptions();
                if (item.get_mComment().length() > 0)
                    contenu += getContext().getString(R.string.dialog_box_comment) + item.get_mComment();
            }
        }

        ((TextView)view.findViewById(R.id.dialog_order_content)).setText(((TextView)view.findViewById(R.id.dialog_order_content)).getText() + " " + contenu);

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
