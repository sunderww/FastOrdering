package com.eip.fastordering;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;


public class HistoryFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    private ArrayList<Item> items = new ArrayList<Item>();
    private AdapterHistory adapter;


    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static HistoryFragment newInstance(int sectionNumber) {
        HistoryFragment fragment = new HistoryFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public HistoryFragment() {
        addOrderToList("Commande #11: table #4, 2pa", "Le 12/12/12 à 12h56");
        addOrderToList("Commande #10: table #4, 2pa", "Le 12/12/12 à 12h55");
        addOrderToList("Commande #9: table #4, 2pa", "Le 12/12/12 à 12h54");
        addOrderToList("Commande #8: table #4, 2pa", "Le 12/12/12 à 12h53");
        addOrderToList("Commande #7: table #4, 2pa", "Le 12/12/12 à 12h52");
        addOrderToList("Commande #6: table #4, 2pa", "Le 12/12/12 à 12h51");
        addOrderToList("Commande #5: table #4, 2pa", "Le 12/12/12 à 12h50");
        addOrderToList("Commande #4: table #4, 2pa", "Le 12/12/12 à 12h49");
        addOrderToList("Commande #3: table #4, 2pa", "Le 12/12/12 à 12h48");
        addOrderToList("Commande #2: table #4, 2pa", "Le 12/12/12 à 12h47");
        addOrderToList("Commande #1: table #4, 2pa", "Le 12/12/12 à 12h46");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_history, container, false);

        /***
         * Create a custom adapter for the listview of orders
         */
        adapter = new AdapterHistory(container.getContext(), items);
        ListView lv = (ListView)rootView.findViewById(R.id.history_list);
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
    public void addOrderToList(String line_one, String line_two) {
        items.add(new Item(line_one, line_two));
        if (adapter != null)
            adapter.notifyDataSetChanged();
    }

}
