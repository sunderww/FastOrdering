package com.eip.fastordering.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.eip.fastordering.R;
import com.eip.fastordering.dialog.DialogOptions;
import com.eip.fastordering.fragment.OrderCardFragment;
import com.eip.fastordering.fragment.OrderFragment;
import com.eip.fastordering.fragment.OrderMenuCompoFragment;
import com.eip.fastordering.fragment.OrderOrderFragment;
import com.eip.fastordering.struct.DataDishStruct;
import com.eip.fastordering.struct.MenuStruct;
import com.eip.fastordering.struct.OptionsStruct;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExpandableListAdapter extends BaseExpandableListAdapter implements View.OnTouchListener {

    private static int _childPosition;
    private static int _groupPosition;
    private TextWatcher _watcher;
    private FragmentActivity _mFACtivity;
    private Context _context;
    private List<String> _listDataHeader;
    private HashMap<String, List<String>> _listDataChild;
    private Map<String, List<DataDishStruct>> _listOptions = null;
    private boolean _mElement;
    private int _mType;

    /**
     * Constructor
     *
     * @param context
     * @param listDataHeader
     * @param listChildData
     * @param element
     * @param listDataNb
     * @param fActivity
     * @param type
     */
    public ExpandableListAdapter(Context context, List<String> listDataHeader, HashMap<String, List<String>> listChildData,
                                 boolean element, HashMap<String, List<String>> listDataNb, FragmentActivity fActivity, int type,
                                 Map<String, List<DataDishStruct>> listOptions) {
        this._mFACtivity = fActivity;
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
        this._mElement = element;
        this._mType = type;
        if (listOptions != null) {
            _listOptions = listOptions;
        }

        this._watcher = new TextWatcher() {
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

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        String value = "";
        value = this._listDataChild.get(this._listDataHeader.get(groupPosition)).get(childPosititon);
        return value;
    }

    private String getChildNb(int groupPosition, int childPosition) {
        String value = "0";
        if (_mType == 2)
            value = OrderCardFragment.get_mListDataNb().get(this._listDataHeader.get(groupPosition))
                    .get(childPosition);
        else if (_mType == 1)
            value = OrderMenuCompoFragment.get_mListDataNb().get(this._listDataHeader.get(groupPosition))
                    .get(childPosition);
        else if (_mType == 3)
            value = OrderOrderFragment.get_mListDataNb().get(this._listDataHeader.get(groupPosition))
                    .get(childPosition);
        return value;
    }

    private void setChildNb(int groupPosition, int childPosition, String value) {
        if (_mType == 2)
            OrderCardFragment.set_idmListDataNb(groupPosition, childPosition, value);
        else if (_mType == 1)
            OrderMenuCompoFragment.set_idmListDataNb(groupPosition, childPosition, value);
        else if (_mType == 3)
            OrderOrderFragment.set_idmListDataNb(groupPosition, childPosition, value);
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
            childText = OrderFragment.getNameElementById((String) getChild(groupPosition, childPosition));

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (!_mElement)
                convertView = infalInflater.inflate(R.layout.list_item, null);
            else {
                convertView = infalInflater.inflate(R.layout.list_radio, null);

                final ViewHolder holder = new ViewHolder();
                holder.edtCode = (EditText) convertView.findViewById(R.id.nbDish);
                holder.edtCode.setOnTouchListener(this);
                convertView.setTag(holder);

                convertView.setOnTouchListener(this);
                convertView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        System.out.println("LONG CLICK GPOS=" + _groupPosition + " CPOS=" + _childPosition);
                        String idDish = ((TextView) (getChildView(_groupPosition, _childPosition, false, null, null).findViewById(R.id.lblListItemRadio))).getTag().toString();
                        System.out.println(idDish);
                        List<String> listOptionsDish = OrderFragment.get_mElements().get(idDish).get_mIdsOptions();

                        DialogOptions dialogOptions = null;

                        if (listOptionsDish != null && !listOptionsDish.isEmpty()) {
                            System.out.println("Current dish has options which are :");
                            for (String optionCatID : listOptionsDish) {
                                System.out.println("ID=" + optionCatID);
                                System.out.println("Name=" + OptionsStruct.getInstance().getNameGroupOptionById(optionCatID));
                                System.out.println("With options :");
                                for (String values : OptionsStruct.getInstance().getOptionsById(optionCatID).get_optionValues().values()) {
                                    System.out.println(values);
                                }
                            }

                            //TODO Test
                            DataDishStruct options =_listOptions.get(_listDataHeader.get(_groupPosition)).get(_childPosition);
                            //TODO End test

                            dialogOptions = new DialogOptions(_mFACtivity, listOptionsDish, _groupPosition, _childPosition, _mType, options);
                        } else {
                            System.out.println("Dish has no options");
                            DataDishStruct options =_listOptions.get(_listDataHeader.get(_groupPosition)).get(_childPosition);
                            dialogOptions = new DialogOptions(_mFACtivity, null, _groupPosition, _childPosition, _mType, options);
                        }
                        dialogOptions.customView().show();
                        dialogOptions.getDialog().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
                        dialogOptions.getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
                        return false;
                    }
                });

                TextView txt = (TextView) convertView.findViewById(R.id.lblListItemRadio);
                txt.setTag(_listDataChild.get(_listDataHeader.get(groupPosition)).get(childPosition));

                EditText nb = (EditText) convertView.findViewById(R.id.nbDish);
                nb.setText(getChildNb(groupPosition, childPosition));
                nb.addTextChangedListener(_watcher);
            }
        } else {
            if (_mElement) {
                EditText nb = (EditText) convertView.findViewById(R.id.nbDish);
                nb.removeTextChangedListener(_watcher);
                nb.setText(getChildNb(groupPosition, childPosition));
                nb.addTextChangedListener(_watcher);
            }
        }

        TextView txtListChild;
        if (!_mElement)
            txtListChild = (TextView) convertView.findViewById(R.id.lblListItem);
        else {
            txtListChild = (TextView) convertView.findViewById(R.id.lblListItemRadio);
            EditText nb = (EditText) convertView.findViewById(R.id.nbDish);
            PosHolder pos = new PosHolder();
            pos.childPos = childPosition;
            pos.groupPos = groupPosition;
            nb.setTag(pos);
            ((ViewHolder) convertView.getTag()).groupPos = groupPosition;
            ((ViewHolder) convertView.getTag()).childPos = childPosition;
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

        for (MenuStruct menu : OrderFragment.get_mMenus()) {
            if (headerTitle.equals(menu.get_mId()))
                headerTitle = menu.get_mName();
        }
        if (headerTitle.equals(OrderFragment.get_mCard().get_mId()))
            headerTitle = _mFACtivity.getResources().getString(R.string.card);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null); // R.layout.list_group //android.R.layout.simple_expandable_list_item_1

            convertView.setOnTouchListener(new GroupTouchListener());
        }

        ImageView image = (ImageView) convertView.findViewById(R.id.expandableIcon);

        if (getChildrenCount(groupPosition) > 0) {
            int imageResourceId = isExpanded ? R.drawable.ic_action_collapse : R.drawable.ic_action_expand;
            image.setImageResource(imageResourceId);
            if (getChildrenCount(groupPosition) == 1 && !_mElement) {
                image.setImageResource(R.drawable.ic_action_invisible);
            }
        } else {
            image.setImageResource(R.drawable.ic_action_invisible);
        }

        TextView lblListHeader = (TextView) convertView.findViewById(R.id.lblListHeader);
        lblListHeader.setText(headerTitle);
        lblListHeader.setTag(getGroup(groupPosition));
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
//            System.out.println("===================");
            EditText editText = (EditText) view;
            _groupPosition = ((PosHolder) editText.getTag()).groupPos;
            _childPosition = ((PosHolder) editText.getTag()).childPos;
//            System.out.println("1 GPOS=" + _groupPosition + " CPOS=" + _childPosition);

            editText.setSelectAllOnFocus(true);
            editText.setFocusable(true);
            editText.setFocusableInTouchMode(true);
        } else {
//            System.out.println("===================");

            //TODO Commented
            ViewHolder holder = (ViewHolder) view.getTag();
            holder.edtCode.setFocusable(false);
            holder.edtCode.setFocusableInTouchMode(false);
            _groupPosition = holder.groupPos;
            _childPosition = holder.childPos;
//            System.out.println("2 GPOS=" + _groupPosition + " CPOS=" + _childPosition);

//            System.out.println(((TextView)(getChildView(_groupPosition, _childPosition, false, null, null).findViewById(R.id.lblListItemRadio))).getTag().toString());

            if (_mElement) {
                InputMethodManager inputMethodManager = (InputMethodManager) _mFACtivity.getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(_mFACtivity.getCurrentFocus().getWindowToken(), 0);
            }
        }

        return false;
    }

    public HashMap<String, List<String>> get_listDataChild() {
        return _listDataChild;
    }

    public List<String> get_listDataHeader() {
        return _listDataHeader;
    }

    private class ViewHolder {
        EditText edtCode;
        int childPos;
        int groupPos;
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
