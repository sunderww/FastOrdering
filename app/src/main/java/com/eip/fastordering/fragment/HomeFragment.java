package com.eip.fastordering.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.eip.fastordering.R;
import com.eip.fastordering.activity.Main;
import com.eip.fastordering.adapter.AdapterHistory;
import com.eip.fastordering.adapter.AdapterNotif;
import com.eip.fastordering.dialog.DialogOrder;
import com.eip.fastordering.struct.NotifStruct;
import com.eip.fastordering.struct.OrderStruct;

import java.util.ArrayList;


public class HomeFragment extends Fragment {

    /***
     * Attributes
     */

    private static final String ARG_SECTION_NUMBER = "section_number";
    private ArrayList<OrderStruct> _mItemsOrder = new ArrayList<OrderStruct>();
    private AdapterHistory _mAdapterOrder;
    private ArrayList<NotifStruct> _mItemsNotif = new ArrayList<NotifStruct>();
    private AdapterNotif _mAdapterNotif;

    public HomeFragment() {
    }

    /***
     * Methods
     */

    public static HomeFragment newInstance(int sectionNumber) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        ((ListView)rootView.findViewById(R.id.home_list_order)).setEmptyView(rootView.findViewById(R.id.home_empty_order));
        ((ListView)rootView.findViewById(R.id.home_list_notif)).setEmptyView(rootView.findViewById(R.id.home_empty_notif));

        _mAdapterOrder = new AdapterHistory(container.getContext(), _mItemsOrder);
        ((ListView)rootView.findViewById(R.id.home_list_order)).setAdapter(_mAdapterOrder);

        ((ListView)rootView.findViewById(R.id.home_list_order)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                displayPopupOrder((OrderStruct) adapterView.getAdapter().getItem(i));
            }
        });


        _mAdapterNotif = new AdapterNotif(container.getContext(), _mItemsNotif);
        ((ListView)rootView.findViewById(R.id.home_list_notif)).setAdapter(_mAdapterNotif);

        _mItemsOrder.clear();
        if (HistoryFragment._mItems.size() >= 1)
            _mItemsOrder.add(0, HistoryFragment._mItems.get(0));
        if (HistoryFragment._mItems.size() >= 2)
            _mItemsOrder.add(1, HistoryFragment._mItems.get(1));
        _mAdapterOrder.notifyDataSetChanged();

        _mItemsNotif.clear();
        if (NotificationsFragment._mItems.size() >= 1)
            _mItemsNotif.add(0, NotificationsFragment._mItems.get(0));
        if (NotificationsFragment._mItems.size() >= 2)
            _mItemsNotif.add(1, NotificationsFragment._mItems.get(1));
        _mAdapterNotif.notifyDataSetChanged();

        rootView.findViewById(R.id.home_rectangle_green).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                NavigationDrawerFragment.mCurrentSelectedPosition = 1;
                NavigationDrawerFragment.mDrawerListView.setItemChecked(1, true);
                NavigationDrawerFragment.mCallbacks.onNavigationDrawerItemSelected(1);
                getActivity().getActionBar().setTitle(getString(R.string.title_section2));
            }
        });

        return rootView;
    }

    private void displayPopupOrder(OrderStruct item) {
        DialogOrder alertBuilder = new DialogOrder(getActivity(), item, this);
        alertBuilder.customView().show();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((Main) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

}
