package com.eip.fastordering.customs;

/**
 * Created by MewSoul on 06/11/2015.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.eip.fastordering.R;

public class MyExpandableListAdapter extends BaseExpandableListAdapter implements View.OnTouchListener {

    private static int _childPosition;
    private static int _groupPosition;
    private final SparseArray<Group> groups;
    public LayoutInflater inflater;
    public Activity activity;
    private AlertDialog dialog;
    private TextWatcher watcher;

    public MyExpandableListAdapter(Activity act, SparseArray<Group> groups, AlertDialog dialog) {
        activity = act;
        this.groups = groups;
        inflater = act.getLayoutInflater();
        this.dialog = dialog;

        this.watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setChildNb(_groupPosition, _childPosition, s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
    }

    private void setChildNb(int groupPosition, int childPosition, String value) {
        groups.get(groupPosition).values.add(childPosition, value);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return groups.get(groupPosition).children.get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final String children = (String) getChild(groupPosition, childPosition);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_radio, null);
        }
        ((TextView) convertView.findViewById(R.id.lblListItemRadio)).setText(children);

        PosHolder pos = new PosHolder();
        pos.childPos = childPosition;
        pos.groupPos = groupPosition;
        convertView.setTag(pos);
        convertView.findViewById(R.id.nbDish).setTag(pos);

        EditText nb = (EditText) convertView.findViewById(R.id.nbDish);
        nb.setText(groups.get(groupPosition).values.get(childPosition));
        nb.addTextChangedListener(watcher);

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return groups.get(groupPosition).children.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groups.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return groups.size();
    }

    @Override
    public void onGroupCollapsed(int groupPosition) {
        super.onGroupCollapsed(groupPosition);
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        super.onGroupExpanded(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.listrow_group, null);
        }
        Group group = (Group) getGroup(groupPosition);
        ((CheckedTextView) convertView).setText(group.string);
        ((CheckedTextView) convertView).setChecked(isExpanded);

        convertView.setOnTouchListener(new GroupTouchListener());

        return convertView;
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        _groupPosition = ((PosHolder) view.getTag()).groupPos;
        _childPosition = ((PosHolder) view.getTag()).childPos;
        System.out.println("Touching options GPOS=" + _groupPosition + " CPOS=" + _childPosition);
        return false;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    private class GroupTouchListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            InputMethodManager inputMethodManager = (InputMethodManager) dialog.getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(dialog.getCurrentFocus().getWindowToken(), 0);
            return false;
        }
    }

    private class PosHolder {
        int childPos;
        int groupPos;
    }
}