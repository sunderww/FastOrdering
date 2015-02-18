package com.eip.fastordering;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class NotificationsFragment extends Fragment {

    /***
     * Attributes
     */

    private static final String ARG_SECTION_NUMBER = "section_number";
    static public ArrayList<NotifStruct> _mItems = new ArrayList<NotifStruct>();
    static public AdapterNotif _mAdapter;
    static private View _mRootView = null;
    static private final int _mSizeList = 20;
    private static  NotificationsFragment _mFragment;

    /***
     * Methods
     */

    public static NotificationsFragment newInstance(int sectionNumber) {
        _mFragment = new NotificationsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        _mFragment.setArguments(args);

        _mItems.clear();

        //TODO Delete after demo
        JSONObject notif = new JSONObject();
        try {
            notif.put("numTable", "11");
            notif.put("msg", "Entree pretes");
            notif.put("date", "12/12/12");
            notif.put("hour", "12:12");
        } catch (JSONException e) {

        }
        addNotificationToList(notif);
        //TODO End delete

        return _mFragment;
    }

    public NotificationsFragment() {

    }

    static private void checkListEmpty() {
        if (_mRootView != null) {
            ImageButton button = (ImageButton) _mRootView.findViewById(R.id.notif_rectangle_red);
            TextView text = (TextView) _mRootView.findViewById(R.id.notification_clean_text);
            if (_mItems.isEmpty()) {
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
        _mRootView = inflater.inflate(R.layout.fragment_notifications, container, false);

        /***
         * Create a custom adapter for the listview of orders
         */

        _mAdapter = new AdapterNotif(container.getContext(), _mItems);
        final ListView lv = (ListView)_mRootView.findViewById(R.id.notification_list);

        ImageButton button = (ImageButton) _mRootView.findViewById(R.id.notif_rectangle_red);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                _mItems.clear();
                _mAdapter.notifyDataSetChanged();
                checkListEmpty();
            }
        });

        checkListEmpty();

        lv.setEmptyView(_mRootView.findViewById(R.id.notification_list_empty));
        lv.setAdapter(_mAdapter);
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
                _mAdapter.toggleSelection(position);
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.delete:
                        // Calls getSelectedIds method from ListViewAdapter Class
                        SparseBooleanArray selected = _mAdapter
                                .getSelectedIds();
                        // Captures all selected ids with a loop
                        for (int i = (selected.size() - 1); i >= 0; i--) {
                            if (selected.valueAt(i)) {
                                NotifStruct selecteditem = _mAdapter
                                        .getItem(selected.keyAt(i));
                                // Remove selected items following the ids
                                _mAdapter.remove(selecteditem);
                                _mItems.remove(selecteditem);
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
                _mAdapter.removeSelection();
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                // TODO Auto-generated method stub
                return false;
            }
        });

        return _mRootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((Main) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

    static public void addNotificationToList(JSONObject notif) {
        if (_mItems.size() >= _mSizeList)
            _mItems.remove(_mSizeList -1);
        _mItems.add(0, new NotifStruct(notif));
        if (_mAdapter != null)
            _mAdapter.notifyDataSetChanged();
        checkListEmpty();
        try {
            createNotificationLauncher("Nouvelle notification", notif.getString("msg"));
        } catch (JSONException e) {
            Log.d("NOTIFICATIONSFRAGMENT", "EXCEPTION JSON:" + e.toString());
        }
    }

    static public void createNotificationLauncher(String title, String text) {
        Main._mBuilder.setContentTitle(title).setContentText(text);
        int mNotificationId = 001;
        Main._mNotifyMgr.notify(mNotificationId, Main._mBuilder.build());
    }

}
