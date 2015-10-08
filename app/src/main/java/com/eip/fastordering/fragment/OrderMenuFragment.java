package com.eip.fastordering.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.eip.fastordering.R;
import com.eip.fastordering.adapter.ExpandableListAdapter;
import com.eip.fastordering.struct.CompositionStruct;
import com.eip.fastordering.struct.MenuStruct;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Mewen on 18-Jan-15.
 */
public class OrderMenuFragment extends Fragment {

	/***
	 * Attributes
	 */

	private ExpandableListAdapter         _mListAdapter;
	private ExpandableListView            _mExpListView;
	private List<String>                  _mListDataHeader;
	private HashMap<String, List<String>> _mListDataChild;

	public OrderMenuFragment() {
		_mListDataHeader = new ArrayList<>();
		_mListDataChild = new HashMap<>();
	}

	/***
	 * Methods
	 */

	public static OrderMenuFragment newInstance() {
		OrderMenuFragment f = new OrderMenuFragment();
		Bundle            b = new Bundle();
		f.setArguments(b);
		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_order_menu, container, false);
		_mListDataHeader = new ArrayList<String>();
		_mListDataChild = new HashMap<String, List<String>>();

		// get the listview
		_mExpListView = (ExpandableListView) rootView.findViewById(R.id.lvExp);

		// preparing list data
		prepareListData();

		_mListAdapter = new ExpandableListAdapter(getActivity(), _mListDataHeader, _mListDataChild, false, null, null, 0, null);

		// setting list adapter
		_mExpListView.setAdapter(_mListAdapter);
		_mExpListView.setGroupIndicator(null);
		_mExpListView.setEmptyView(rootView.findViewById(R.id.order_menu_none));

		_mExpListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
			@Override
			public boolean onGroupClick(ExpandableListView _mExpandableListView, View view, int i, long l) {
				if (_mExpListView.getExpandableListAdapter().getChildrenCount(i) > 1)
					;
				else {
					Fragment frag = new OrderMenuCompoFragment().newInstance(_mListDataHeader.get(i), _mListDataChild.get(_mListDataHeader.get(i)).get(0));
					Log.d("IDS", "" + _mListDataHeader.get(i) + " " + _mListDataChild.get(_mListDataHeader.get(i)).get(0));
					FragmentManager fm = getActivity().getSupportFragmentManager();
					FragmentTransaction ft = fm.beginTransaction();
					ft.replace(getParentFragment().getId(), frag).addToBackStack(null).commit();
					Toast.makeText(getActivity(), "Menu unique", Toast.LENGTH_SHORT).show();
					return true;
				}
				return false;
			}
		});


		_mExpListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
			@Override
			public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
				Fragment frag = new OrderMenuCompoFragment().newInstance(_mListDataHeader.get(groupPosition), _mListDataChild.get(_mListDataHeader.get(groupPosition)).get(childPosition));

				FragmentManager     fm = getActivity().getSupportFragmentManager();
				FragmentTransaction ft = fm.beginTransaction();
				ft.replace(getParentFragment().getId(), frag).addToBackStack(null).commit();

				return false;
			}
		});

		return rootView;
	}

	private void prepareListData() {
		for (MenuStruct item : OrderFragment.get_mMenus()) {
			_mListDataHeader.add(item.get_mId());

			if (item.get_mCat().size() > 0) {
				List<String> subMenus = new ArrayList<String>();

				for (CompositionStruct compo : item.get_mCat()) {
					subMenus.add(compo.get_mNameCompo());
				}
				_mListDataChild.put(_mListDataHeader.get(_mListDataHeader.size() - 1), subMenus);
			}
		}
	}
}
