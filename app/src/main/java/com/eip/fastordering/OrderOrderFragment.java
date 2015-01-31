package com.eip.fastordering;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    static View _mRootView;

    public static OrderOrderFragment newInstance(int position, JSONObject order) {
        OrderOrderFragment f = new OrderOrderFragment();

        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();
        _mListDataNb = new HashMap<String, List<String>>();

        if (order != null) {
            setDataOrderToLists(order);
        }

//        for (CategoryStruct cat : OrderFragment.get_mCard().get_mCategories()) {
//            listDataHeader.add(cat.get_mCategoryName());
//            listDataChild.put(listDataHeader.get(listDataHeader.size() - 1), new ArrayList<String>());
//            _mListDataNb.put(listDataHeader.get(listDataHeader.size() - 1), new ArrayList<String>());
//        }

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

    private static void setDataOrderToLists(JSONObject order) {
        try {
            JSONArray orders = order.getJSONArray("order");
            for (int i = 0; i < orders.length(); ++i) {
                JSONObject menu = orders.getJSONObject(i);
                listDataHeader.add(menu.getString("menuId"));
                JSONArray content = menu.getJSONArray("content");
                listDataChild.put(listDataHeader.get(listDataHeader.size() - 1), new ArrayList<String>());
                _mListDataNb.put(listDataHeader.get(listDataHeader.size() - 1), new ArrayList<String>());
                for (int j = 0; j < content.length(); ++j) {
                    JSONObject dish = content.getJSONObject(j);
                    listDataChild.get(listDataHeader.get(listDataHeader.size() - 1)).add(listDataChild.get(listDataHeader.get(listDataHeader.size() - 1)).size(), dish.getString("id"));
                    _mListDataNb.get(listDataHeader.get(listDataHeader.size() - 1)).add(_mListDataNb.get(listDataHeader.get(listDataHeader.size() - 1)).size(), dish.getString("qty"));
                }
            }
        } catch (JSONException e) {

        }
    }

    private static void checkListEmpty() {
        if (_mRootView != null) {
            ImageButton button = (ImageButton) _mRootView.findViewById(R.id.order_order_rectangle);
            TextView text = (TextView) _mRootView.findViewById(R.id.order_order_button_text);
            if (listAdapter.get_listDataHeader() != null) {
                if (listAdapter.get_listDataHeader().isEmpty()) {
                    button.setVisibility(View.GONE);
                    text.setVisibility(View.GONE);
                } else {
                    button.setVisibility(View.VISIBLE);
                    text.setVisibility(View.VISIBLE);
                }
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

        listAdapter = new ExpandableListAdapter(getActivity(), listDataHeader, listDataChild, true, _mListDataNb, getActivity(), 3);
        checkListEmpty();

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

                listAdapter.get_listDataHeader().clear();
                listAdapter.get_listDataChild().clear();
                _mListDataNb.clear();

//                for (CategoryStruct cat : OrderFragment.get_mCard().get_mCategories()) {
//                    listDataHeader.add(cat.get_mCategoryName());
//                    listDataChild.put(listDataHeader.get(listDataHeader.size() - 1), new ArrayList<String>());
//                    _mListDataNb.put(listDataHeader.get(listDataHeader.size() - 1), new ArrayList<String>());
//                }
                listAdapter.notifyDataSetChanged();
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
        checkListEmpty();
    }

    static void addCardElementToOrder(String idCard, String idDish, String number) {
        for (int i = 0; i < listAdapter.get_listDataHeader().size(); ++i) {
            if (listAdapter.get_listDataHeader().get(i).equals(idCard)) {
                //Verifie si plat deja present, si oui ajoute la qte
                for (int j = 0; j < listAdapter.get_listDataChild().get(listAdapter.get_listDataHeader().get(i)).size(); ++j) {
                    if (listAdapter.get_listDataChild().get(listAdapter.get_listDataHeader().get(i)).get(j).equals(idDish)) {
                        int one = Integer.parseInt(number);
                        int two = Integer.parseInt(_mListDataNb.get(listAdapter.get_listDataHeader().get(i)).get(j));
                        _mListDataNb.get(listAdapter.get_listDataHeader().get(i)).set(j, ((Integer)(one + two)).toString());
                        listAdapter.notifyDataSetChanged();
                        checkListEmpty();
                        return;
                    }
                }

                //Sinon ajoute dans les listes
                listAdapter.get_listDataChild().get(listAdapter.get_listDataHeader().get(i)).add(listAdapter.get_listDataChild().get(listAdapter.get_listDataHeader().get(i)).size(), idDish);
                _mListDataNb.get(listAdapter.get_listDataHeader().get(i)).add(_mListDataNb.get(listAdapter.get_listDataHeader().get(i)).size(), number);
                listAdapter.notifyDataSetChanged();
                checkListEmpty();
                return;
            }
        }
        listAdapter.get_listDataHeader().add(OrderFragment.get_mCard().get_mId());
        listAdapter.get_listDataChild().put(OrderFragment.get_mCard().get_mId(), new ArrayList<String>());
        _mListDataNb.put(OrderFragment.get_mCard().get_mId(), new ArrayList<String>());
        listAdapter.get_listDataChild().get(OrderFragment.get_mCard().get_mId()).add(0, idDish);
        _mListDataNb.get(OrderFragment.get_mCard().get_mId()).add(0, number);
        listAdapter.notifyDataSetChanged();
        checkListEmpty();
    }

    public static HashMap<String, List<String>> get_mListDataNb() {
        return _mListDataNb;
    }

    public static void set_idmListDataNb(int groupPosition, int childPosition, String value) {
        _mListDataNb.get(listDataHeader.get(groupPosition)).set(childPosition, value);
    }
}
