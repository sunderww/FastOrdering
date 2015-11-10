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
import com.eip.fastordering.struct.OptionsStruct;

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
        groups.get(groupPosition).values.set(childPosition, value);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return OptionsStruct.getInstance().getNameOptionById(groups.get(groupPosition).children.get(childPosition));
//        return groups.get(groupPosition).children.get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final String children = (String) getChild(groupPosition, childPosition);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_radio, null);

            ((TextView) convertView.findViewById(R.id.lblListItemRadio)).setText(children);
            EditText nb = (EditText) convertView.findViewById(R.id.nbDish);

            final PosHolder pos = new PosHolder();
            pos.edtCode = nb;
            pos.edtCode.setOnTouchListener(this);
            pos.childPos = childPosition;
            pos.groupPos = groupPosition;
            convertView.setTag(pos);
            convertView.setOnTouchListener(this);

//            System.out.println("GETTING TEXT FOR " + groupPosition + " " + childPosition + " and is " + groups.get(groupPosition).values.get(childPosition));
            nb.setText(groups.get(groupPosition).values.get(childPosition));
            nb.addTextChangedListener(watcher);
            nb.setTag(pos);
        } else {
            EditText nb = (EditText) convertView.findViewById(R.id.nbDish);
            nb.removeTextChangedListener(watcher);
            nb.setText(groups.get(groupPosition).values.get(childPosition));
            nb.addTextChangedListener(watcher);
        }

        EditText nb = (EditText) convertView.findViewById(R.id.nbDish);
        PosHolder pos = new PosHolder();
        pos.childPos = childPosition;
        pos.groupPos = groupPosition;
        nb.setTag(pos);

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
        ((CheckedTextView) convertView).setText(OptionsStruct.getInstance().getNameGroupOptionById(group.string));
        ((CheckedTextView) convertView).setChecked(isExpanded);

        convertView.setOnTouchListener(new GroupTouchListener());

        return convertView;
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        if (view instanceof EditText) {
            EditText editText = (EditText) view;

            _groupPosition = ((PosHolder) view.getTag()).groupPos;
            _childPosition = ((PosHolder) view.getTag()).childPos;

            editText.setSelectAllOnFocus(true);
            editText.setFocusable(true);
            editText.setFocusableInTouchMode(true);

//            System.out.println("Touching options GPOS=" + _groupPosition + " CPOS=" + _childPosition);
        } else {
            PosHolder holder = (PosHolder) view.getTag();
            holder.edtCode.setFocusable(false);
            holder.edtCode.setFocusableInTouchMode(false);
            _groupPosition = holder.groupPos;
            _childPosition = holder.childPos;
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(dialog.getCurrentFocus().getWindowToken(), 0);

        }
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
        EditText edtCode;
        int childPos;
        int groupPos;
    }
}