package com.eip.fastordering;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Mewen on 18-Jan-15.
 */
public class OrderOrderFragment extends Fragment {

    public static OrderOrderFragment newInstance(int position) {
        OrderOrderFragment f = new OrderOrderFragment();
        Bundle b = new Bundle();
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public OrderOrderFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_order_order, container, false);
        return rootView;
    }
}
