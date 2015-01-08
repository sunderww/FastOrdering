package com.eip.fastordering;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.preference.DialogPreference;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by Mewen on 04-Jan-15.
 */
public class DialogOrder extends AlertDialog {

    private Activity _mActivity;
    private OrderStruct _mItem;

    DialogOrder(Activity activity, OrderStruct item) {
        super(activity);

        _mActivity = activity;
        _mItem = item;
    }

    public AlertDialog customView() {
        AlertDialog.Builder builder = new AlertDialog.Builder(_mActivity);

        LayoutInflater inflater = _mActivity.getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_order, null);
        builder.setView(view);
        builder.setNegativeButton(R.string.dialog_order_close, new OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        ((TextView)view.findViewById(R.id.dialog_order_title)).setText(((TextView)view.findViewById(R.id.dialog_order_title)).getText() + _mItem.get_mNumOrder());
        ((TextView)view.findViewById(R.id.dialog_order_table)).setText(((TextView)view.findViewById(R.id.dialog_order_table)).getText() + _mItem.get_mNumTable());
        ((TextView)view.findViewById(R.id.dialog_order_pa)).setText(((TextView)view.findViewById(R.id.dialog_order_pa)).getText() + _mItem.get_mNumPA());
        ((TextView)view.findViewById(R.id.dialog_order_hour)).setText(((TextView)view.findViewById(R.id.dialog_order_hour)).getText() + _mItem.get_mDate() + " à " + _mItem.get_mHour());

        return builder.create();
    }
}
