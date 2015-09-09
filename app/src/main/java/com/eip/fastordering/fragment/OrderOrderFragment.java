package com.eip.fastordering.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.eip.fastordering.R;
import com.eip.fastordering.activity.LoginActivity;
import com.eip.fastordering.adapter.ExpandableListAdapter;
import com.eip.fastordering.struct.OrderStruct;
import com.github.nkzawa.socketio.client.Ack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Mewen on 18-Jan-15.
 */
public class OrderOrderFragment extends Fragment {

	private static ExpandableListAdapter         _mListAdapter;
	private static List<String>                  _mListDataHeader;
	private static HashMap<String, List<String>> _mListDataChild;
	private static View                          _mRootView;
	private static HashMap<String, List<String>> _mListDataNb;
	private static OrderStruct                   _mDetails;
	private        ExpandableListView            _mExpListView;

	/**
	 * Constructor
	 */
	public OrderOrderFragment() {

	}

	public static OrderOrderFragment newInstance(JSONObject order) {
		OrderOrderFragment f = new OrderOrderFragment();

		_mListDataHeader = new ArrayList<String>();
		_mListDataChild = new HashMap<String, List<String>>();
		_mListDataNb = new HashMap<String, List<String>>();

		if (order != null) {
			setDataOrderToLists(order);
		}

		Bundle b = new Bundle();
		f.setArguments(b);
		return f;
	}

	public static void setExistingOrder(OrderStruct order) {
		_mDetails = order;
	}

	private static void setDataOrderToLists(JSONObject order) {
		try {
			JSONArray orders = order.getJSONArray("order");
			for (int i = 0; i < orders.length(); ++i) {
				JSONObject menu = orders.getJSONObject(i);
				_mListDataHeader.add(menu.getString("menuId"));
				JSONArray content = menu.getJSONArray("content");
				_mListDataChild.put(_mListDataHeader.get(_mListDataHeader.size() - 1), new ArrayList<String>());
				_mListDataNb.put(_mListDataHeader.get(_mListDataHeader.size() - 1), new ArrayList<String>());
				for (int j = 0; j < content.length(); ++j) {
					JSONObject dish = content.getJSONObject(j);
					_mListDataChild.get(_mListDataHeader.get(_mListDataHeader.size() - 1)).add(_mListDataChild.get(_mListDataHeader.get(_mListDataHeader.size() - 1)).size(), dish.getString("id"));
					_mListDataNb.get(_mListDataHeader.get(_mListDataHeader.size() - 1)).add(_mListDataNb.get(_mListDataHeader.get(_mListDataHeader.size() - 1)).size(), dish.getString("qty"));
				}
			}
		} catch (JSONException e) {

		}
	}

	private static void checkListEmpty() {
		if (_mRootView != null) {
			ImageButton button = (ImageButton) _mRootView.findViewById(R.id.order_order_rectangle);
			TextView text = (TextView) _mRootView.findViewById(R.id.order_order_button_text);
			if (_mListAdapter.get_listDataHeader() != null) {
				if (_mListAdapter.get_listDataHeader().isEmpty()) {
					button.setVisibility(View.GONE);
					text.setVisibility(View.GONE);
				} else {
					button.setVisibility(View.VISIBLE);
					text.setVisibility(View.VISIBLE);
				}
			}
		}
	}

	static void addMenuToOrder(String menuId, HashMap<String, String> dishes) {
		List<String> listHeaderAdapter = _mListAdapter.get_listDataHeader();

		int i = 0;
		for (String menu : listHeaderAdapter) {
			if (menu.equals(menuId)) {
				int j = 0;
				for (Map.Entry<String, String> dish : dishes.entrySet()) {
					boolean found = false;
					for (String dishInOrder : _mListAdapter.get_listDataChild().get(menu)) {
						if (dishInOrder.equals(dish.getKey())) {
							int one = Integer.parseInt(_mListDataNb.get(menu).get(j));
							int two = Integer.parseInt(dish.getValue());
							_mListDataNb.get(menu).set(j, ((Integer) (one + two)).toString());
							found = true;
							break;
						}
					}
					if (!found) {
						_mListAdapter.get_listDataChild().get(menu).add(dish.getKey());
						_mListDataNb.get(menu).add(dish.getValue());
					}
					++j;
				}
				_mListAdapter.notifyDataSetChanged();
				checkListEmpty();
				return;
			}
			++i;
		}

		listHeaderAdapter.add(menuId);
		_mListAdapter.get_listDataChild().put(listHeaderAdapter.get(listHeaderAdapter.size() - 1), new ArrayList<String>());
		_mListDataNb.put(listHeaderAdapter.get(listHeaderAdapter.size() - 1), new ArrayList<String>());
		for (Map.Entry<String, String> dish : dishes.entrySet()) {
			_mListAdapter.get_listDataChild().get(listHeaderAdapter.get(listHeaderAdapter.size() - 1)).add(_mListAdapter.get_listDataChild().get(listHeaderAdapter.get(listHeaderAdapter.size() - 1)).size(), dish.getKey());
			_mListDataNb.get(listHeaderAdapter.get(listHeaderAdapter.size() - 1)).add(_mListDataNb.get(listHeaderAdapter.get(listHeaderAdapter.size() - 1)).size(), dish.getValue());
		}
		_mListAdapter.notifyDataSetChanged();
		checkListEmpty();
	}

	static void addCardElementToOrder(String idCard, String idDish, String number) {
		for (int i = 0; i < _mListAdapter.get_listDataHeader().size(); ++i) {
			if (_mListAdapter.get_listDataHeader().get(i).equals(idCard)) {
				//Verifie si plat deja present, si oui ajoute la qte
				for (int j = 0; j < _mListAdapter.get_listDataChild().get(_mListAdapter.get_listDataHeader().get(i)).size(); ++j) {
					if (_mListAdapter.get_listDataChild().get(_mListAdapter.get_listDataHeader().get(i)).get(j).equals(idDish)) {
						int one = Integer.parseInt(number);
						int two = Integer.parseInt(_mListDataNb.get(_mListAdapter.get_listDataHeader().get(i)).get(j));
						_mListDataNb.get(_mListAdapter.get_listDataHeader().get(i)).set(j, ((Integer) (one + two)).toString());
						_mListAdapter.notifyDataSetChanged();
						Toast.makeText(_mRootView.getContext(), R.string.order_added_success, Toast.LENGTH_SHORT).show();
						checkListEmpty();
						return;
					}
				}

				//Sinon ajoute dans les listes
				_mListAdapter.get_listDataChild().get(_mListAdapter.get_listDataHeader().get(i)).add(_mListAdapter.get_listDataChild().get(_mListAdapter.get_listDataHeader().get(i)).size(), idDish);
				_mListDataNb.get(_mListAdapter.get_listDataHeader().get(i)).add(_mListDataNb.get(_mListAdapter.get_listDataHeader().get(i)).size(), number);
				_mListAdapter.notifyDataSetChanged();
				Toast.makeText(_mRootView.getContext(), R.string.order_added_success, Toast.LENGTH_SHORT).show();
				checkListEmpty();
				return;
			}
		}
		_mListAdapter.get_listDataHeader().add(OrderFragment.get_mCard().get_mId());
		_mListAdapter.get_listDataChild().put(OrderFragment.get_mCard().get_mId(), new ArrayList<String>());
		_mListDataNb.put(OrderFragment.get_mCard().get_mId(), new ArrayList<String>());
		_mListAdapter.get_listDataChild().get(OrderFragment.get_mCard().get_mId()).add(0, idDish);
		_mListDataNb.get(OrderFragment.get_mCard().get_mId()).add(0, number);
		_mListAdapter.notifyDataSetChanged();
		Toast.makeText(_mRootView.getContext(), R.string.order_added_success, Toast.LENGTH_SHORT).show();
		checkListEmpty();
	}

	public static HashMap<String, List<String>> get_mListDataNb() {
		return _mListDataNb;
	}

	public static void set_idmListDataNb(int groupPosition, int childPosition, String value) {
		_mListDataNb.get(_mListDataHeader.get(groupPosition)).set(childPosition, value);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		_mRootView = inflater.inflate(R.layout.fragment_order_order, container, false);
		_mRootView.setOnTouchListener(new GroupTouchListener());

		// get the listview
		_mExpListView = (ExpandableListView) _mRootView.findViewById(R.id.lvExp);
		_mExpListView.setEmptyView(_mRootView.findViewById(R.id.order_order_none_text));

		_mListAdapter = new ExpandableListAdapter(getActivity(), _mListDataHeader, _mListDataChild, true, _mListDataNb, getActivity(), 3);
		checkListEmpty();

		// setting list adapter
		_mExpListView.setAdapter(_mListAdapter);
		_mExpListView.setGroupIndicator(null);

		if (_mDetails != null) {
			((TextView) _mRootView.findViewById(R.id.order_order_table_edit)).setText(_mDetails.get_mNumTable());
			((TextView) _mRootView.findViewById(R.id.order_order_pa_edit)).setText(_mDetails.get_mNumPA());
		}

		ImageButton sendButton = (ImageButton) _mRootView.findViewById(R.id.order_order_rectangle);
		sendButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				if (_mListAdapter.getGroupCount() <= 0) {
					Toast.makeText(getActivity(), R.string.order_order_order_missing, Toast.LENGTH_SHORT).show();
					return;
				} else if (((EditText) _mRootView.findViewById(R.id.order_order_table_edit)).getText().toString().equals("")) {
					Toast.makeText(getActivity(), R.string.order_order_table_missing, Toast.LENGTH_SHORT).show();
					return;
				} else if (((EditText) _mRootView.findViewById(R.id.order_order_pa_edit)).getText().toString().equals("")) {
					Toast.makeText(getActivity(), R.string.order_order_pa_missing, Toast.LENGTH_SHORT).show();
					return;
				}

				JSONObject orderJSON = new JSONObject();
				JSONObject dish;
				JSONArray  content;
				JSONObject menu;
				JSONArray  arrMenus  = new JSONArray();

				try {
					int groupCount = _mListAdapter.getGroupCount();
					for (int i = 0; i < groupCount; ++i) {
						content = new JSONArray();
						int childCount = _mListAdapter.getChildrenCount(i);
						for (int j = 0; j < childCount; ++j) {
							View view = _mListAdapter.getChildView(i, j, false, null, null);
							if (view != null) {
								dish = new JSONObject();
								TextView txt = (TextView) view.findViewById(R.id.lblListItemRadio);
								EditText nb = (EditText) view.findViewById(R.id.nbDish);
								dish.put("id", txt.getTag().toString());
								dish.put("qty", Integer.parseInt(nb.getText().toString()));

								//TODO Add fields
								dish.put("comment", "blabla");
								dish.put("cuisson", "cramé");

								content.put(dish);
							}
						}
						menu = new JSONObject();
						menu.put("menuId", _mListAdapter.getGroup(i).toString());
						menu.put("content", content);
						arrMenus.put(menu);
					}
					//SET UP ORDER JSON
					orderJSON.put("numTable", ((EditText) _mRootView.findViewById(R.id.order_order_table_edit)).getText().toString());
					orderJSON.put("numPA", ((EditText) _mRootView.findViewById(R.id.order_order_pa_edit)).getText().toString());

					//TODO Add field
					orderJSON.put("globalComment", "toto");

					orderJSON.put("order", arrMenus);

					//TODO Check once done - Alexis
					if (_mDetails != null) {
						orderJSON.put("numOrder", _mDetails.get_mNumOrder());
					}

					Log.d("COMMANDE READY", orderJSON.toString());
					LoginActivity._mSocket.emit("send_order", orderJSON, new Ack() {
						@Override
						public void call(Object... objects) {
							Log.d("SENDORDERFRAG", "" + objects[0]);
						}
					});
				} catch (JSONException e) {

				}

				((EditText) _mRootView.findViewById(R.id.order_order_table_edit)).setText("");
				((EditText) _mRootView.findViewById(R.id.order_order_pa_edit)).setText("");
				_mListAdapter.get_listDataHeader().clear();
				_mListAdapter.get_listDataChild().clear();
				_mListDataNb.clear();

				_mListAdapter.notifyDataSetChanged();
				checkListEmpty();
			}
		});

		return _mRootView;
	}

	private class GroupTouchListener implements View.OnTouchListener {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
			inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
			return false;
		}
	}
}
