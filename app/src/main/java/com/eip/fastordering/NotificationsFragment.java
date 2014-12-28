package com.eip.fastordering;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;


public class NotificationsFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    private ArrayList<Item> items = new ArrayList<Item>();
    private AdapterNotif adapter;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static NotificationsFragment newInstance(int sectionNumber) {
        NotificationsFragment fragment = new NotificationsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public NotificationsFragment() {
        addNotificationToList("Table #11: Entree pretes", "Le 12/12/12 à 14h00");
        addNotificationToList("Table #11: Entree pretes", "Le 12/12/12 à 13h59");
        addNotificationToList("Table #11: Entree pretes", "Le 12/12/12 à 13h58");
        addNotificationToList("Table #11: Entree pretes", "Le 12/12/12 à 13h57");
        addNotificationToList("Table #11: Entree pretes", "Le 12/12/12 à 13h56");
        addNotificationToList("Table #11: Entree pretes", "Le 12/12/12 à 13h00");
        addNotificationToList("Table #11: Entree pretes", "Le 12/12/12 à 12h59");
        addNotificationToList("Table #11: Entree pretes", "Le 12/12/12 à 12h58");
        addNotificationToList("Table #11: Entree pretes", "Le 12/12/12 à 12h57");
        addNotificationToList("Table #11: Entree pretes", "Le 12/12/12 à 12h56");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_notifications, container, false);

        /***
         * Create a custom adapter for the listview of orders
         */
        adapter = new AdapterNotif(container.getContext(), items);
        ListView lv = (ListView)rootView.findViewById(R.id.notification_list);
        lv.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((Main) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

    /***
     * Add an item to the custom list view
     * @param line_one, first line of the item
     * @param line_two, second line of the item
     */
    public void addNotificationToList(String line_one, String line_two) {
        items.add(new Item(line_one, line_two));
        if (adapter != null)
            adapter.notifyDataSetChanged();
    }

}
