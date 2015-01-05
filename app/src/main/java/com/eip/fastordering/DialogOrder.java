package com.eip.fastordering;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.preference.DialogPreference;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

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

        TextView firstLine = (TextView) view.findViewById(R.id.dialog_first_line);
        firstLine.setText(_mItem.getTitle());
        TextView secondLine = (TextView) view.findViewById(R.id.dialog_second_line);
        secondLine.setText(_mItem.getDescription());

        return builder.create();
    }
}
