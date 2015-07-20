package com.eip.fastordering.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.eip.fastordering.R;
import com.eip.fastordering.adapter.ExpandableListAdapter;
import com.eip.fastordering.struct.CategoryStruct;
import com.eip.fastordering.struct.CompositionStruct;
import com.eip.fastordering.struct.MenuStruct;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Mewen on 22-Jan-15.
 */
public class OrderMenuCompoFragment extends Fragment {

	private static List<String>                  listDataHeader = new ArrayList<>();
	private static HashMap<String, List<String>> _mListDataNb   = new HashMap<>();
	private ExpandableListAdapter         listAdapter;
	private ExpandableListView            expListView;
	private HashMap<String, List<String>> listDataChild;
	private String                        _mMenu;
	private String                        _mCompo;
	private View                          _mRootView;

	/**
	 * Constructor
	 */
	public OrderMenuCompoFragment() {

	}

	public static OrderMenuCompoFragment newInstance(String menuId, String compo) {
		OrderMenuCompoFragment instance = new OrderMenuCompoFragment();
		Bundle                 b        = new Bundle();
		b.putString("menuId", menuId);
		b.putString("compo", compo);
		instance.setArguments(b);
		return instance;
	}

	public static HashMap<String, List<String>> get_mListDataNb() {
		return _mListDataNb;
	}

	public static void set_idmListDataNb(int groupPosition, int childPosition, String value) {
		_mListDataNb.get(listDataHeader.get(groupPosition)).set(childPosition, value);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	private void checkListEmpty() {
		if (_mRootView != null) {
			ImageButton button = (ImageButton) _mRootView.findViewById(R.id.order_compo_rectangle);
			TextView text = (TextView) _mRootView.findViewById(R.id.order_compo_button_text);
			if (listDataHeader.isEmpty()) {
				button.setVisibility(View.GONE);
				text.setVisibility(View.GONE);
			} else {
				button.setVisibility(View.VISIBLE);
				text.setVisibility(View.VISIBLE);
			}
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Bundle args = getArguments();
		_mMenu = args.getString("menuId");
		_mCompo = args.getString("compo");

		_mRootView = inflater.inflate(R.layout.fragment_order_menu_compo, container, false);
		listDataHeader = new ArrayList<String>();
		listDataChild = new HashMap<String, List<String>>();
		_mListDataNb = new HashMap<String, List<String>>();


		// get the listview
		expListView = (ExpandableListView) _mRootView.findViewById(R.id.lvExp);
		// preparing list data
		prepareListData();
		checkListEmpty();

		listAdapter = new ExpandableListAdapter(getActivity(), listDataHeader, listDataChild, true, _mListDataNb, getActivity(), 1);

		// setting list adapter
		expListView.setAdapter(listAdapter);
		expListView.setGroupIndicator(null);
		expListView.setEmptyView(_mRootView.findViewById(R.id.order_compo_none));
		_mRootView.findViewById(R.id.order_compo_layout).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!(v instanceof EditText)) {
					InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
					inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
				}
			}
		});

		ImageButton addButton = (ImageButton) _mRootView.findViewById(R.id.order_compo_rectangle);
		addButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				boolean                 valid      = false;
				int                     groupCount = listAdapter.getGroupCount();
				HashMap<String, String> dishes     = new HashMap<String, String>();
				for (int i = 0; i < groupCount; ++i) {
					int childCount = listAdapter.getChildrenCount(i);
					for (int j = 0; j < childCount; ++j) {
						View view = listAdapter.getChildView(i, j, false, null, null);
						if (view != null) {
							TextView txt = (TextView) view.findViewById(R.id.lblListItemRadio);
							EditText nb = (EditText) view.findViewById(R.id.nbDish);
							Log.d("NB", "" + txt.getText() + " " + nb.getText());
							if (Integer.parseInt(nb.getText().toString()) > 0) {
								dishes.put(txt.getTag().toString(), nb.getText().toString());
								valid = true;
							}
						}
					}
				}
				if (valid)
					OrderOrderFragment.addMenuToOrder(_mMenu, dishes);
				getFragmentManager().popBackStack();
			}
		});

		return _mRootView;
	}

	private void prepareListData() {
		for (MenuStruct item : OrderFragment.get_mMenus()) {
			if (item.get_mId().equals(_mMenu)) {
				for (CompositionStruct compo : item.get_mCat()) {
					if (compo.get_mNameCompo().equals(_mCompo)) {
						for (CategoryStruct cat : compo.get_mCat()) {
							listDataHeader.add(cat.get_mCategoryName());
							List<String> dishes = new ArrayList<String>();
							List<String> nb = new ArrayList<String>();
							for (String dish : cat.get_mIds()) {
								dishes.add(dish);
								nb.add("0");
							}
							listDataChild.put(listDataHeader.get(listDataHeader.size() - 1), dishes);
							_mListDataNb.put(listDataHeader.get(listDataHeader.size() - 1), nb);
						}
					}
				}
			}
		}
	}
}
