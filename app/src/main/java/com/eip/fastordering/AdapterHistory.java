package com.eip.fastordering;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Mewen on 28-Dec-14.
 */
public class AdapterHistory extends ArrayAdapter<OrderStruct> {
    private final Context context;
    private final ArrayList<OrderStruct> OrderStructsArrayList;

    public AdapterHistory(Context context, ArrayList<OrderStruct> OrderStructsArrayList) {

        super(context, R.layout.row_history, OrderStructsArrayList);

        this.context = context;
        this.OrderStructsArrayList = OrderStructsArrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.row_history, parent, false);

        TextView labelView = (TextView) rowView.findViewById(R.id.history_label_one);
        TextView valueView = (TextView) rowView.findViewById(R.id.history_label_two);
        labelView.setText(getContext().getString(R.string.dialog_order_title) + OrderStructsArrayList.get(position).get_mNumOrder() +
            ", " + getContext().getString(R.string.dialog_order_table) + OrderStructsArrayList.get(position).get_mNumTable() +
            ", " + getContext().getString(R.string.dialog_order_pa) + OrderStructsArrayList.get(position).get_mNumPA());

        valueView.setText(getContext().getString(R.string.on_the) + OrderStructsArrayList.get(position).get_mDate() +
                getContext().getString(R.string.at) + OrderStructsArrayList.get(position).get_mHour());

        return rowView;
    }
}
