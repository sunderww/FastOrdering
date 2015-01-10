package com.eip.fastordering;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class NotificationsFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    static public ArrayList<NotifStruct> items = new ArrayList<NotifStruct>();
    static public AdapterNotif adapter;
    static private View rootView = null;
    static private final int sizeList = 20;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static NotificationsFragment newInstance(int sectionNumber) {
        NotificationsFragment fragment = new NotificationsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);

        items.clear();
        addNotificationToList("Table #11: Entree pretes", "Le 12/12/12 à 14h00");
        addNotificationToList("Table #11: Entree pretes", "Le 12/12/12 à 14h01");
        addNotificationToList("Table #11: Entree pretes", "Le 12/12/12 à 14h02");
        addNotificationToList("Table #11: Entree pretes", "Le 12/12/12 à 14h03");
        addNotificationToList("Table #11: Entree pretes", "Le 12/12/12 à 14h04");
        addNotificationToList("Table #11: Entree pretes", "Le 12/12/12 à 14h05");
        addNotificationToList("Table #11: Entree pretes", "Le 12/12/12 à 14h06");
        addNotificationToList("Table #11: Entree pretes", "Le 12/12/12 à 14h07");
        addNotificationToList("Table #11: Entree pretes", "Le 12/12/12 à 14h08");
        addNotificationToList("Table #11: Entree pretes", "Le 12/12/12 à 14h09");

        return fragment;
    }

    public NotificationsFragment() {

    }

    static private void checkListEmpty() {
        if (rootView != null) {
            ImageButton button = (ImageButton) rootView.findViewById(R.id.notif_rectangle_red);
            TextView text = (TextView) rootView.findViewById(R.id.notification_clean_text);
            if (items.isEmpty()) {
                button.setVisibility(View.GONE);
                text.setVisibility(View.GONE);
            } else {
                button.setVisibility(View.VISIBLE);
                text.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_notifications, container, false);

        /***
         * Create a custom adapter for the listview of orders
         */
        adapter = new AdapterNotif(container.getContext(), items);
        final ListView lv = (ListView)rootView.findViewById(R.id.notification_list);

        ImageButton button = (ImageButton) rootView.findViewById(R.id.notif_rectangle_red);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                items.clear();
                adapter.notifyDataSetChanged();
                checkListEmpty();
            }
        });

        checkListEmpty();

        lv.setEmptyView(rootView.findViewById(R.id.notification_list_empty));
        lv.setAdapter(adapter);
        lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        lv.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

            @Override
            public void onItemCheckedStateChanged(ActionMode mode,
                                                  int position, long id, boolean checked) {
                // Capture total checked items
                final int checkedCount = lv.getCheckedItemCount();
                // Set the CAB title according to total checked items
                String selected = (checkedCount > 1) ? "choisis" : "choisi";
                mode.setTitle(checkedCount + " " + selected);
                // Calls toggleSelection method from ListViewAdapter Class
                adapter.toggleSelection(position);
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.delete:
                        // Calls getSelectedIds method from ListViewAdapter Class
                        SparseBooleanArray selected = adapter
                                .getSelectedIds();
                        // Captures all selected ids with a loop
                        for (int i = (selected.size() - 1); i >= 0; i--) {
                            if (selected.valueAt(i)) {
                                NotifStruct selecteditem = adapter
                                        .getItem(selected.keyAt(i));
                                // Remove selected items following the ids
                                adapter.remove(selecteditem);
                                items.remove(selecteditem);
                                checkListEmpty();
                            }
                        }
                        // Close CAB
                        mode.finish();
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                mode.getMenuInflater().inflate(R.menu.activity_main, menu);
                return true;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                // TODO Auto-generated method stub
                adapter.removeSelection();
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                // TODO Auto-generated method stub
                return false;
            }
        });

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
    static public void addNotificationToList(String line_one, String line_two) {
        if (items.size() >= sizeList)
            items.remove(sizeList -1);
        items.add(0, new NotifStruct(line_one, line_two));
        if (adapter != null)
            adapter.notifyDataSetChanged();
        checkListEmpty();
    }

}
