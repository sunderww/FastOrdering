package com.eip.fastordering.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.eip.fastordering.R;
import com.eip.fastordering.customs.Group;
import com.eip.fastordering.customs.MyExpandableListAdapter;
import com.eip.fastordering.struct.OptionsStruct;

import java.util.List;


public class DialogOptions {

    private FragmentActivity _mActivity;
    private List<String> _options;
    private SparseArray<Group> groups = new SparseArray<Group>();
    private AlertDialog dialog;
    private MyExpandableListAdapter listAdapter;

    /**
     * Constructor
     *
     * @param activity
     */
    public DialogOptions(Activity activity, List<String> options) {
        _mActivity = (FragmentActivity) activity;
        _options = options;
        //TODO Not forget comment
    }

    public AlertDialog customView() {
        AlertDialog.Builder builder = new AlertDialog.Builder(_mActivity);

        LayoutInflater inflater = _mActivity.getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_options, null);
        builder.setView(view);
        builder.setPositiveButton(R.string.dialog_options_positive, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listAdapter.notifyDataSetChanged();
                int groupCount = listAdapter.getGroupCount();
                for (int i = 0; i < groupCount; ++i) {
                    System.out.println("I=" + i);
                    int childCount = listAdapter.getChildrenCount(i);
                    for (int j = 0; j < childCount; ++j) {
                        System.out.println("J=" + j);
                        View view = listAdapter.getChildView(i, j, false, null, null);
                        if (view != null) {
                            TextView txt = (TextView) view.findViewById(R.id.lblListItemRadio);
                            System.out.println(txt.getText().toString());
                            EditText nb = (EditText) view.findViewById(R.id.nbDish);
                            System.out.println(Integer.parseInt(nb.getText().toString()));
//                            if (Integer.parseInt(nb.getText().toString()) > 0)
//                                OrderOrderFragment.addCardElementToOrder(OrderFragment.get_mCard().get_mId(), txt.getTag().toString(), nb.getText().toString());
                        }
                    }
                }
            }
        });
        builder.setNegativeButton(R.string.dialog_options_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        if (_options != null && !_options.isEmpty())
            createData(_options);

        final ExpandableListView listView = (ExpandableListView) view.findViewById(R.id.listView);

        dialog = builder.create();

        listAdapter = new MyExpandableListAdapter(_mActivity, groups, dialog);
        listView.setAdapter(listAdapter);

//        for (int i = 0; i < groups.size(); i++) {
//            listView.expandGroup(i);
//        }
//        listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
//            public boolean onGroupClick(ExpandableListView arg0, View itemView, int itemPosition, long itemId) {
//                listView.expandGroup(itemPosition);
//                return true;
//            }
//        });

        return dialog;
    }

    public AlertDialog getDialog() {
        return dialog;
    }

    public void createData(List<String> _options) {
        int i = 0;
        for (String optionCatID : _options) {
            Group group = new Group(OptionsStruct.getInstance().getNameGroupOptionById(optionCatID));
            for (String values : OptionsStruct.getInstance().getOptionsById(optionCatID).get_optionValues().values()) {
                group.children.add(values);
                group.values.add("0");
            }
            groups.append(i++, group);
        }
    }

}
