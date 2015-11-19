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
import com.eip.fastordering.fragment.OrderCardFragment;
import com.eip.fastordering.fragment.OrderMenuCompoFragment;
import com.eip.fastordering.struct.DataDishStruct;
import com.eip.fastordering.struct.OptionsStruct;

import java.util.List;


public class DialogOptions {

    private FragmentActivity _mActivity;
    private List<String> _options;
    private SparseArray<Group> groups;
    private AlertDialog dialog;
    private MyExpandableListAdapter listAdapter;
    private final int gposdish;
    private final int cposdish;
    private final int type;
    private DataDishStruct structOptions;

    /**
     * Constructor
     *
     * @param activity
     */
    public DialogOptions(Activity activity, List<String> options, int gposdish, int cposdish, int type,
                         DataDishStruct structOptions) {
        _mActivity = (FragmentActivity) activity;
        _options = options;
        this.gposdish = gposdish;
        this.cposdish = cposdish;
        this.type = type;
        this.groups = new SparseArray<>();
        this.structOptions = structOptions;
        //TODO Populate w/ options if from orderorderfragment or editing on card or whatever
    }

    public AlertDialog customView() {
        AlertDialog.Builder builder = new AlertDialog.Builder(_mActivity);

        LayoutInflater inflater = _mActivity.getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_options, null);
        builder.setView(view);
        builder.setPositiveButton(R.string.dialog_options_positive, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listAdapter.notifyDataSetChanged();
                int groupCount = listAdapter.getGroupCount();
                for (int i = 0; i < groupCount; ++i) {
                    int childCount = listAdapter.getChildrenCount(i);
                    for (int j = 0; j < childCount; ++j) {
                        View view = listAdapter.getChildView(i, j, false, null, null);
                        if (view != null) {
                            TextView txt = (TextView) view.findViewById(R.id.lblListItemRadio);
                            EditText nb = (EditText) view.findViewById(R.id.nbDish);

                            System.out.println(txt.getText().toString());
                            System.out.println(groups.get(i).children.get(j) + " " + OptionsStruct.getInstance().getNameOptionById(groups.get(i).children.get(j)));
                            System.out.println(Integer.parseInt(nb.getText().toString()));

                            if (Integer.parseInt(nb.getText().toString()) > 0) {
                                if (type == 1)
                                    OrderMenuCompoFragment.set_idmListDataOther(gposdish, cposdish, groups.get(i).string, groups.get(i).children.get(j), nb.getText().toString());
                                else if (type == 2)
                                    OrderCardFragment.set_idmListDataOther(gposdish, cposdish, groups.get(i).string, groups.get(i).children.get(j), nb.getText().toString());
                            }
                        }
                    }
                }
                EditText comment = (EditText) view.findViewById(R.id.dialog_order_com);
                if (type == 1)
                    OrderMenuCompoFragment.setComment(gposdish, cposdish, comment.getText().toString());
                else if (type == 2)
                    OrderCardFragment.setComment(gposdish, cposdish, comment.getText().toString());
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
        return dialog;
    }

    public AlertDialog getDialog() {
        return dialog;
    }

    public void createData(List<String> _options) {
        int i = 0;
        for (String optionCatID : _options) {
            Group group = new Group(optionCatID);
            for (String values : OptionsStruct.getInstance().getOptionsById(optionCatID).get_optionValues().keySet()) {
                group.children.add(values);
                if (structOptions != null)
                    group.values.add(structOptions.getmOptions().get(optionCatID).get(values));
                else
                    group.values.add("0");
            }
            groups.append(i++, group);
        }

        //TODO set the nb of options with the given ones
    }

}
