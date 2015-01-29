package com.eip.fastordering;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * Created by Mewen on 18-Jan-15.
 */
public class OrderOrderFragment extends Fragment {

    static ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    static List<String> listDataHeader;
    static HashMap<String, List<String>> listDataChild;
    private static HashMap<String, List<String>> _mListDataNb;
    View _mRootView;

    public static OrderOrderFragment newInstance(int position) {
        OrderOrderFragment f = new OrderOrderFragment();

        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();
        _mListDataNb = new HashMap<String, List<String>>();

        for (CategoryStruct cat : OrderFragment.get_mCard().get_mCategories()) {
            listDataHeader.add(cat.get_mCategoryName());
            listDataChild.put(listDataHeader.get(listDataHeader.size() - 1), new ArrayList<String>());
            _mListDataNb.put(listDataHeader.get(listDataHeader.size() - 1), new ArrayList<String>());
        }

        Bundle b = new Bundle();
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public OrderOrderFragment() {

    }

    private void checkListEmpty() {
        if (_mRootView != null) {
            ImageButton button = (ImageButton) _mRootView.findViewById(R.id.order_order_rectangle);
            TextView text = (TextView) _mRootView.findViewById(R.id.order_order_button_text);
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
        _mRootView = inflater.inflate(R.layout.fragment_order_order, container, false);

//        listDataHeader = new ArrayList<String>();
//        listDataChild = new HashMap<String, List<String>>();
//        _mListDataNb = new HashMap<String, List<String>>();
//
//        for (CategoryStruct cat : OrderFragment.get_mCard().get_mCategories()) {
//            listDataHeader.add(cat.get_mCategoryName());
//            listDataChild.put(listDataHeader.get(listDataHeader.size() - 1), new ArrayList<String>());
//            _mListDataNb.put(listDataHeader.get(listDataHeader.size() - 1), new ArrayList<String>());
//        }

        // get the listview
        expListView = (ExpandableListView) _mRootView.findViewById(R.id.lvExp);
        expListView.setEmptyView(_mRootView.findViewById(R.id.order_order_none_text));

        // preparing list data
        prepareListData();
        checkListEmpty();

        listAdapter = new ExpandableListAdapter(getActivity(), listDataHeader, listDataChild, true, _mListDataNb, getActivity(), 3);

        // setting list adapter
        expListView.setAdapter(listAdapter);
        expListView.setGroupIndicator(null);

        ImageButton addButton = (ImageButton)_mRootView.findViewById(R.id.order_order_rectangle);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int groupCount = listAdapter.getGroupCount();
                for (int i = 0; i < groupCount; ++i) {
                    int childCount = listAdapter.getChildrenCount(i);
                    for (int j = 0; j < childCount; ++j) {
                        View view = listAdapter.getChildView(i, j, false, null, null);
                        if (view != null) {
                            TextView txt = (TextView)view.findViewById(R.id.lblListItemRadio);
                            EditText nb = (EditText) view.findViewById(R.id.nbDish);
                            Log.d("NB", "" + listAdapter.getGroup(i).toString() + " " + txt.getTag().toString() + " " + nb.getText());
                        }
                    }
                }
            }
        });

        return _mRootView;
    }

    private void prepareListData() {
    }

    static void addMenuToOrder(String menuId, HashMap<String, String> dishes) {
        Log.d("ADD MENU", "" + menuId);
        listAdapter.get_listDataHeader().add(menuId);
        Log.d("DETAILS", "" + listAdapter.get_listDataHeader());
        for (int i = 0; i < listAdapter.get_listDataHeader().size(); ++i) {
            if (listAdapter.get_listDataHeader().get(i).equals(menuId)) {
                listAdapter.get_listDataChild().put(menuId, new ArrayList<String>());
                _mListDataNb.put(menuId, new ArrayList<String>());
                for (Map.Entry<String, String> dish : dishes.entrySet()) {
                    listAdapter.get_listDataChild().get(listAdapter.get_listDataHeader().get(i)).add(listAdapter.get_listDataChild().get(listAdapter.get_listDataHeader().get(i)).size(), dish.getKey());
                    _mListDataNb.get(listDataHeader.get(i)).add(_mListDataNb.get(listDataHeader.get(i)).size(), dish.getValue());
                }
                listAdapter.notifyDataSetChanged();
            }
        }
        listAdapter.notifyDataSetChanged();
    }

    static void addCardElementToOrder(String category, String idDish, String number) {
        for (int i = 0; i < listDataHeader.size(); ++i) {
            if (listDataHeader.get(i).equals(category)) {
                //Verifie si plat deja present, si oui ajoute la qte
                for (int j = 0; j < listDataChild.get(listDataHeader.get(i)).size(); ++j) {
                    if (listDataChild.get(listDataHeader.get(i)).get(j).equals(idDish)) {
                        int one = Integer.parseInt(number);
                        int two = Integer.parseInt(_mListDataNb.get(listDataHeader.get(i)).get(j));
                        _mListDataNb.get(listDataHeader.get(i)).set(j, ((Integer)(one + two)).toString());
                        listAdapter.notifyDataSetChanged();
                        return;
                    }
                }

                //Sinon ajoute dans les listes
                listAdapter.get_listDataChild().get(listAdapter.get_listDataHeader().get(i)).add(listAdapter.get_listDataChild().get(listAdapter.get_listDataHeader().get(i)).size(), idDish);
                _mListDataNb.get(listDataHeader.get(i)).add(_mListDataNb.get(listDataHeader.get(i)).size(), number);
                listAdapter.notifyDataSetChanged();
            }
        }
    }

    public static HashMap<String, List<String>> get_mListDataNb() {
        return _mListDataNb;
    }

    public static void set_idmListDataNb(int groupPosition, int childPosition, String value) {
        _mListDataNb.get(listDataHeader.get(groupPosition)).set(childPosition, value);
    }
}
