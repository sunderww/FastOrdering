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

        labelView.setText(OrderStructsArrayList.get(position).getTitle());
        valueView.setText(OrderStructsArrayList.get(position).getDescription());

        return rowView;
    }
}
