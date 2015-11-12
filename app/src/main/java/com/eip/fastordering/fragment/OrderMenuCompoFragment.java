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
import com.eip.fastordering.struct.DataDishStruct;
import com.eip.fastordering.struct.MenuStruct;
import com.eip.fastordering.struct.OptionsStruct;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mewen on 22-Jan-15.
 */
public class OrderMenuCompoFragment extends Fragment {

	private static List<String>                  listDataHeader = new ArrayList<>();
	private static HashMap<String, List<String>> _mListDataNb   = new HashMap<>();
	private static HashMap<String, List<DataDishStruct>> _mListDataOthers = new HashMap<>();
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

	public static void set_idmListDataOther(int gpos, int cpos, String catopt, String opt, String value) {
		DataDishStruct options = _mListDataOthers.get(listDataHeader.get(gpos)).get(cpos);
		options.getmOptions().get(catopt).put(opt, value);
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
		_mListDataOthers = new HashMap<String, List<DataDishStruct>>();


		// get the listview
		expListView = (ExpandableListView) _mRootView.findViewById(R.id.lvExp);
		// preparing list data
		prepareListData();
		checkListEmpty();

		listAdapter = new ExpandableListAdapter(getActivity(), listDataHeader, listDataChild, true, _mListDataNb, getActivity(), 1, null);

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
				Map<String, DataDishStruct> optionMap     = new HashMap<>();
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
								optionMap.put(txt.getTag().toString(), _mListDataOthers.get(listDataHeader.get(i)).get(j));
								System.out.println("SETTING TO ID=" + txt.getTag().toString() + " VALUES=" + nb.getText().toString() + " OPTIONS FOLLOWING");
								valid = true;
							}
							DataDishStruct option = _mListDataOthers.get(listDataHeader.get(i)).get(j);
                            if (option != null) {
                                System.out.println("HAS OPTIONS SET AND WHICH ARE=");
                                for (String catoptions : option.getmOptions().keySet()) {
                                    for (Map.Entry<String, String> entry : option.getmOptions().get(catoptions).entrySet()) {
                                        System.out.println("KEY=" + entry.getKey() + " NAME=" + OptionsStruct.getInstance().getNameOptionById(entry.getKey()) + " VALUE=" + entry.getValue());
                                    }
                                }
                            }
						}
					}
				}
				if (valid)
					OrderOrderFragment.addMenuToOrder(_mMenu, dishes, optionMap);
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
							List<DataDishStruct> datas = new ArrayList<>();
							for (String dish : cat.get_mIds()) {
								dishes.add(dish);
								nb.add("0");
								OrderFragment.get_mElements();
								System.out.println("Preparing options for item : " + dish + " " + OrderFragment.getNameElementById(dish));
								datas.add(new DataDishStruct(OrderFragment.get_mElements().get(dish)));
							}
							listDataChild.put(listDataHeader.get(listDataHeader.size() - 1), dishes);
							_mListDataNb.put(listDataHeader.get(listDataHeader.size() - 1), nb);
							_mListDataOthers.put(listDataHeader.get(listDataHeader.size() - 1), datas);
						}
					}
				}
			}
		}
	}
}
