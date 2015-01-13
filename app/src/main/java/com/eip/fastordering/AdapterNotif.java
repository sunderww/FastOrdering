package com.eip.fastordering;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class AdapterNotif extends ArrayAdapter<NotifStruct> {

    /***
     * Attributes
     */

    private final Context _mContext;
    private final ArrayList<NotifStruct> _mNotifStructsArrayList;
    private SparseBooleanArray _mSelectedNotifStructsIds;

    /***
     * Methods
     */

    public AdapterNotif(Context context, ArrayList<NotifStruct> NotifStructsArrayList) {

        super(context, R.layout.row_notifications, NotifStructsArrayList);

        this._mContext = context;
        this._mNotifStructsArrayList = NotifStructsArrayList;
        this._mSelectedNotifStructsIds = new SparseBooleanArray();

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) _mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.row_notifications, parent, false);

        TextView labelView = (TextView) rowView.findViewById(R.id.notif_label_one);
        TextView valueView = (TextView) rowView.findViewById(R.id.notif_label_two);

        labelView.setText(_mNotifStructsArrayList.get(position).getTitle());
        valueView.setText(_mNotifStructsArrayList.get(position).getDescription());

        return rowView;
    }

    public void selectView(int position, boolean value) {
        if (value)
            _mSelectedNotifStructsIds.put(position, value);
        else
            _mSelectedNotifStructsIds.delete(position);
        notifyDataSetChanged();
    }

    public void toggleSelection(int position) {
        selectView(position, !_mSelectedNotifStructsIds.get(position));
    }

    public SparseBooleanArray getSelectedIds() {
        return _mSelectedNotifStructsIds;
    }

    public void removeSelection() {
        _mSelectedNotifStructsIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }
}
