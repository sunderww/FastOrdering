package com.eip.fastordering;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.HashMap;
import java.util.List;

public class ExpandableListAdapter extends BaseExpandableListAdapter implements View.OnTouchListener {

    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<String>> _listDataChild;
    private boolean _mElement;
    static int _childPosition;
    static int _groupPosition;
    TextWatcher _watcher;
    FragmentActivity _mFACtivity;
    private boolean _mCard;

    public ExpandableListAdapter(Context context, List<String> listDataHeader,HashMap<String, List<String>> listChildData,
                                 boolean element, HashMap<String, List<String>> listDataNb, FragmentActivity fActivity, boolean card) {
        this._mFACtivity = fActivity;
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
        this._mElement = element;
        this._mCard = card;
        this._watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Log.d("TOTO2", "===============================");
                //Log.d("ON CHANGED", "GP:" + _groupPosition + " CP:" + _childPosition + " " + s);
                //Log.d("ON CHANGED", "GP:" + groupPosition + " CP:" + childPosition + " " + s);
                //Log.d("DB BEF", "" + OrderMenuCompoFragment.get_mListDataNb());
                setChildNb(_groupPosition, _childPosition, s.toString());
                //Log.d("DB AFT", "" + OrderMenuCompoFragment.get_mListDataNb());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    private String getChildNb(int groupPosition, int childPosition) {
        if (_mCard)
            return OrderCardFragment.get_mListDataNb().get(this._listDataHeader.get(groupPosition))
                    .get(childPosition);
        return OrderMenuCompoFragment.get_mListDataNb().get(this._listDataHeader.get(groupPosition))
                .get(childPosition);
    }

    private void setChildNb(int groupPosition, int childPosition, String value) {
        //Log.d("SET CHIDL NB", "GP:" + groupPosition + " CP:" + childPosition + " VA:" + value);
        if (_mCard)
            OrderCardFragment.set_idmListDataNb(groupPosition, childPosition, value);
        else
            OrderMenuCompoFragment.set_idmListDataNb(groupPosition, childPosition, value);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText;

        if (!_mElement)
            childText = (String) getChild(groupPosition, childPosition);
        else
            childText = OrderFragment.getNameElementById((String)getChild(groupPosition, childPosition));


        if (_mElement) {
            //Log.d("TOTO", "====================================");
            //    Log.d("DEBUUUUUG CHILD", "" + childText + " " + getChildNb(groupPosition, childPosition));
            //Log.d("TOTO", "" + childText);
            //Log.d("DATAS", "" + OrderMenuCompoFragment.get_mListDataNb());
        }
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (!_mElement)
                convertView = infalInflater.inflate(R.layout.list_item, null);
            else {
                //Log.d("DEBUG NB", "" + childText + " GP:" + groupPosition + " CP:" + childPosition);

                convertView = infalInflater.inflate(R.layout.list_radio, null);

                final ViewHolder holder = new ViewHolder();
                holder.edtCode = (EditText) convertView.findViewById(R.id.nbDish);
                holder.edtCode.setOnTouchListener(this);
                convertView.setOnTouchListener(this);
                convertView.setTag(holder);

                EditText nb = (EditText)convertView.findViewById( R.id.nbDish);

                //Log.d("SET TEXT", "SET TEXT " + groupPosition + " " + childPosition);
                nb.setText(getChildNb(groupPosition, childPosition));

                nb.addTextChangedListener(_watcher);
            }
        } else {
            if (_mElement) {
                EditText nb = (EditText)convertView.findViewById( R.id.nbDish);
                nb.removeTextChangedListener(_watcher);
                //Log.d("SETTING TEXT", "GP:" + groupPosition + " CP:" + childPosition);
                nb.setText(getChildNb(groupPosition, childPosition));
                nb.addTextChangedListener(_watcher);
            }
        }

        TextView txtListChild;
        if (!_mElement)
            txtListChild = (TextView) convertView.findViewById(R.id.lblListItem);
        else {
            txtListChild = (TextView) convertView.findViewById(R.id.lblListItemRadio);

            EditText nb = (EditText)convertView.findViewById( R.id.nbDish);
            PosHolder pos = new PosHolder();
            pos.childPos = childPosition;
            pos.groupPos = groupPosition;
            nb.setTag(pos);

        }
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

            convertView.setOnTouchListener(new GroupTouchListener());
        }

        ImageView image = (ImageView) convertView.findViewById(R.id.expandableIcon);

        if(getChildrenCount(groupPosition) > 0){
            int imageResourceId = isExpanded ? R.drawable.ic_action_collapse : R.drawable.ic_action_expand;
            image.setImageResource(imageResourceId);
            if (getChildrenCount(groupPosition) == 1 && !_mElement)
                image.setImageResource(R.drawable.ic_action_invisible);

        } else {
            image.setImageResource(R.drawable.ic_action_invisible);
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
        if (view instanceof EditText) {
            EditText editText = (EditText) view;
            _groupPosition = ((PosHolder)editText.getTag()).groupPos;
            _childPosition = ((PosHolder)editText.getTag()).childPos;
            editText.setSelectAllOnFocus(true);
            editText.setFocusable(true);
            editText.setFocusableInTouchMode(true);
        } else {
            ViewHolder holder = (ViewHolder) view.getTag();
            holder.edtCode.setFocusable(false);
            holder.edtCode.setFocusableInTouchMode(false);
            if (_mElement) {
                InputMethodManager inputMethodManager = (InputMethodManager) _mFACtivity.getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(_mFACtivity.getCurrentFocus().getWindowToken(), 0);
            }
        }
        return false;
    }

    private class ViewHolder {
        EditText edtCode;
    }

    private class PosHolder {
        int childPos;
        int groupPos;
    }

    private class GroupTouchListener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (_mFACtivity != null) {
                InputMethodManager inputMethodManager = (InputMethodManager) _mFACtivity.getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(_mFACtivity.getCurrentFocus().getWindowToken(), 0);
            }
            return false;
        }
    }
}
