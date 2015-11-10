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
import com.eip.fastordering.struct.OptionsStruct;
import com.eip.fastordering.struct.OrderStruct;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class DialogOrder {

	private static JSONObject               _mOrderDetailed;
	private        FragmentActivity         _mActivity;
	private        OrderStruct              _mItem;
	private        List<ContentOrderStruct> _mContent;
	private        Fragment                 _mFrag;

	/**
	 * Constructor
	 * @param activity
	 * @param frag
	 * @param fullOrder
	 */
	public DialogOrder(Activity activity, Fragment frag, JSONObject fullOrder) {
		_mContent = new ArrayList<>();
		_mOrderDetailed = fullOrder;
		_mActivity = (FragmentActivity) activity;
		_mFrag = frag;
		_mItem = new OrderStruct(fullOrder);

		getDetailedOrder(fullOrder);
		//TODO Not forget global_comment
	}

	public AlertDialog customView() {
		AlertDialog.Builder builder = new AlertDialog.Builder(_mActivity);

		LayoutInflater inflater = _mActivity.getLayoutInflater();
		View           view     = inflater.inflate(R.layout.dialog_order, null);
		builder.setView(view);
		builder.setPositiveButton(R.string.dialog_order_modify, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Fragment frag = new OrderFragment().newInstance(1, _mOrderDetailed, _mItem);
				FragmentManager fm = _mActivity.getSupportFragmentManager();
				FragmentTransaction ft = fm.beginTransaction();
				ft.replace(_mFrag.getId(), frag).addToBackStack(null).commit();
			}
		});
		builder.setNegativeButton(R.string.dialog_order_close, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});

		((TextView) view.findViewById(R.id.dialog_order_table)).setText(((TextView) view.findViewById(R.id.dialog_order_table)).getText() + _mItem.get_mNumTable());
		((TextView) view.findViewById(R.id.dialog_order_pa)).setText(((TextView) view.findViewById(R.id.dialog_order_pa)).getText() + _mItem.get_mNumPA());
		((TextView) view.findViewById(R.id.dialog_order_hour)).setText(((TextView) view.findViewById(R.id.dialog_order_hour)).getText() + _mItem.get_mDate() + _mActivity.getApplicationContext().getString(R.string.at) + _mItem.get_mHour());

		String lineSep = System.getProperty("line.separator");

		String contenu = "";
		for (int i = 0; i < _mContent.size(); ++i) {
			ContentOrderStruct content = _mContent.get(i);
			contenu += lineSep + lineSep + _mActivity.getApplicationContext().getString(R.string.dialog_box_menu) + OrderFragment.getNameCatById(content.get_mId(), _mActivity.getApplicationContext());
			for (int j = 0; j < content.get_mItems().size(); ++j) {
				ItemStruct item = content.get_mItems().get(j);
				contenu += lineSep + '\t' + '\t' + _mActivity.getApplicationContext().getString(R.string.dialog_box_dish) + OrderFragment.getNameElementById(item.get_mId()) + " x" + item.get_mQty();
				if (item.get_mOptions().size() > 0) {
					contenu += " (options:";
					for (Map.Entry<String, String> entry : item.get_mOptions().entrySet()) {
						contenu += " " + OptionsStruct.getInstance().getNameOptionById(entry.getKey()) + " x" + entry.getValue();
					}
					contenu += ")";
				}
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
