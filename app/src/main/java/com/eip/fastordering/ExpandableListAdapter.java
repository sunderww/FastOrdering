package com.eip.fastordering;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import java.util.HashMap;
import java.util.List;

public class ExpandableListAdapter extends BaseExpandableListAdapter implements View.OnTouchListener {

    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<String>> _listDataChild;
    private boolean _mRadio;
    //static int _childPosition;
    //static int _groupPosition;

    public ExpandableListAdapter(Context context, List<String> listDataHeader,
                                 HashMap<String, List<String>> listChildData, HashMap<String, List<String>> listDataNb, boolean radio) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
        this._mRadio = radio;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    private String getChildNb(int groupPosition, int childPosition) {
        return OrderMenuCompoFragment.get_mListDataNb().get(this._listDataHeader.get(groupPosition))
                .get(childPosition);
    }

    private void setChildNb(int groupPosition, int childPosition, String value) {
        Log.d("SET CHIDL NB", "GP:" + groupPosition + " CP:" + childPosition + " VA:" + value);
        OrderMenuCompoFragment.set_idmListDataNb(groupPosition, childPosition, value);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = (String) getChild(groupPosition, childPosition);

        Log.d("DEBUG NB", "" + childText + " GP:" + groupPosition + " CP:" + childPosition);
        if (_mRadio)
            Log.d("DEBUUUUUG CHILD", "" + childText + " " + getChildNb(groupPosition, childPosition));

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (!_mRadio)
                convertView = infalInflater.inflate(R.layout.list_item, null);
            else {
                convertView = infalInflater.inflate(R.layout.list_radio, null);

                //final ViewHolder holder = new ViewHolder();
                //holder.edtCode = (EditText) convertView.findViewById(R.id.nbDish);
                //holder.edtCode.setOnTouchListener(this);
                //convertView.setOnTouchListener(this);
                //convertView.setTag(holder);

                EditText nb = (EditText)convertView.findViewById( R.id.nbDish);
                nb.setText(getChildNb(groupPosition, childPosition));
                nb.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        Log.d("ON CHANGED", "GP:" + groupPosition + " CP:" + childPosition + " " + s);
                        setChildNb(groupPosition, childPosition, s.toString());
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
            }
        } //else {
        //    if (_mRadio) {
        //        ViewHolder holder = (ViewHolder) convertView.getTag();
        //        holder.edtCode.setText(getChildNb(groupPosition, childPosition));
        //    }
        //}

        TextView txtListChild;
        if (!_mRadio)
            txtListChild = (TextView) convertView.findViewById(R.id.lblListItem);
        else
            txtListChild = (TextView) convertView.findViewById(R.id.lblListItemRadio);
        txtListChild.setText(childText);

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (this._listDataChild.get(this._listDataHeader.get(groupPosition)) != null)
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
        return 0;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null); // R.layout.list_group //android.R.layout.simple_expandable_list_item_1
        }

        ImageView image = (ImageView) convertView.findViewById(R.id.expandableIcon);

        if(getChildrenCount(groupPosition) != 0){
            int imageResourceId = isExpanded ? R.drawable.ic_action_collapse : R.drawable.ic_action_expand;
            image.setImageResource(imageResourceId);

            image.setVisibility(View.VISIBLE);
        } else {
            image.setVisibility(View.INVISIBLE);
        }

        //TextView lblListHeader = (TextView) convertView.findViewById(android.R.id.text1);
        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        //lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        Log.d("@@@", "DOUBLE FIRED");
        if (view instanceof EditText) {
            EditText editText = (EditText) view;
            editText.setFocusable(true);
            editText.setFocusableInTouchMode(true);
        } else {
            ViewHolder holder = (ViewHolder) view.getTag();
            holder.edtCode.setFocusable(false);
            holder.edtCode.setFocusableInTouchMode(false);
        }
        return false;
    }

    private class ViewHolder {
        EditText edtCode;
    }
}