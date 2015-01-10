package com.eip.fastordering;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    private ArrayList<OrderStruct> _mItemsOrder = new ArrayList<OrderStruct>();
    private AdapterHistory _mAdapterOrder;

    private ArrayList<NotifStruct> _mItemsNotif = new ArrayList<NotifStruct>();
    private AdapterNotif _mAdapterNotif;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static HomeFragment newInstance(int sectionNumber) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        ((ListView)rootView.findViewById(R.id.home_list_order)).setEmptyView(((TextView)rootView.findViewById(R.id.home_empty_order)));
        ((ListView)rootView.findViewById(R.id.home_list_notif)).setEmptyView(((TextView)rootView.findViewById(R.id.home_empty_notif)));

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
        if (HistoryFragment.items.size() >= 1)
            _mItemsOrder.add(0, HistoryFragment.items.get(0));
        if (HistoryFragment.items.size() >= 2)
            _mItemsOrder.add(1, HistoryFragment.items.get(1));
        _mAdapterOrder.notifyDataSetChanged();

        _mItemsNotif.clear();
        if (NotificationsFragment.items.size() >= 1)
            _mItemsNotif.add(0, NotificationsFragment.items.get(0));
        if (NotificationsFragment.items.size() >= 2)
            _mItemsNotif.add(1, NotificationsFragment.items.get(1));
        _mAdapterNotif.notifyDataSetChanged();

        return rootView;
    }

    private void displayPopupOrder(OrderStruct item) {
        DialogOrder alertBuilder = new DialogOrder(getActivity(), item);
        alertBuilder.customView().show();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((Main) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

}
