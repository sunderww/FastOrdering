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
public class AdapterNotif extends ArrayAdapter<Item> {
    private final Context context;
    private final ArrayList<Item> itemsArrayList;

    public AdapterNotif(Context context, ArrayList<Item> itemsArrayList) {

        super(context, R.layout.row_notifications, itemsArrayList);

        this.context = context;
        this.itemsArrayList = itemsArrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.row_notifications, parent, false);

        TextView labelView = (TextView) rowView.findViewById(R.id.notif_label_one);
        TextView valueView = (TextView) rowView.findViewById(R.id.notif_label_two);

        labelView.setText(itemsArrayList.get(position).getTitle());
        valueView.setText(itemsArrayList.get(position).getDescription());

        return rowView;
    }
}
