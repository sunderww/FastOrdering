package com.eip.fastordering;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Mewen on 28-Dec-14.
 */
public class AdapterNotif extends ArrayAdapter<NotifStruct> {
    private final Context context;
    private final ArrayList<NotifStruct> NotifStructsArrayList;
    private SparseBooleanArray mSelectedNotifStructsIds;

    public AdapterNotif(Context context, ArrayList<NotifStruct> NotifStructsArrayList) {

        super(context, R.layout.row_notifications, NotifStructsArrayList);

        this.context = context;
        this.NotifStructsArrayList = NotifStructsArrayList;
        mSelectedNotifStructsIds = new SparseBooleanArray();

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.row_notifications, parent, false);

        TextView labelView = (TextView) rowView.findViewById(R.id.notif_label_one);
        TextView valueView = (TextView) rowView.findViewById(R.id.notif_label_two);

        labelView.setText(NotifStructsArrayList.get(position).getTitle());
        valueView.setText(NotifStructsArrayList.get(position).getDescription());

        return rowView;
    }

    public void selectView(int position, boolean value) {
        if (value)
            mSelectedNotifStructsIds.put(position, value);
        else
            mSelectedNotifStructsIds.delete(position);
        notifyDataSetChanged();
    }

    public void toggleSelection(int position) {
        selectView(position, !mSelectedNotifStructsIds.get(position));
    }

    public SparseBooleanArray getSelectedIds() {
        return mSelectedNotifStructsIds;
    }

    public void removeSelection() {
        mSelectedNotifStructsIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }
}
