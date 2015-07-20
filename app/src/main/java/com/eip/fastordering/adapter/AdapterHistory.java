package com.eip.fastordering.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.eip.fastordering.R;
import com.eip.fastordering.struct.OrderStruct;

import java.util.List;


public class AdapterHistory extends ArrayAdapter<OrderStruct> {

	private final Context           _mContext;
	private final List<OrderStruct> _mOrderStructsArrayList;

	/**
	 * Constructor
	 * @param context
	 * @param OrderStructsArrayList
	 */
	public AdapterHistory(Context context, List<OrderStruct> OrderStructsArrayList) {

		super(context, R.layout.row_history, OrderStructsArrayList);

		this._mContext = context;
		this._mOrderStructsArrayList = OrderStructsArrayList;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) _mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View rowView = inflater.inflate(R.layout.row_history, parent, false);

		TextView labelView = (TextView) rowView.findViewById(R.id.history_label_one);
		TextView valueView = (TextView) rowView.findViewById(R.id.history_label_two);
		labelView.setText(getContext().getString(R.string.dialog_order_table) + _mOrderStructsArrayList.get(position).get_mNumTable() +
				", " + getContext().getString(R.string.dialog_order_pa) + _mOrderStructsArrayList.get(position).get_mNumPA());

		valueView.setText(getContext().getString(R.string.on_the) + _mOrderStructsArrayList.get(position).get_mDate() +
				getContext().getString(R.string.at) + _mOrderStructsArrayList.get(position).get_mHour());

		return rowView;
	}
}
