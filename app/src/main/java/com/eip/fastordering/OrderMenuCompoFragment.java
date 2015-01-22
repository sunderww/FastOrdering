package com.eip.fastordering;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Mewen on 22-Jan-15.
 */
public class OrderMenuCompoFragment extends Fragment{

    public static OrderMenuCompoFragment newInstance() {
        OrderMenuCompoFragment instance = new OrderMenuCompoFragment();
        Bundle b = new Bundle();
        instance.setArguments(b);
        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public OrderMenuCompoFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_order_menu_compo, container, false);

        return rootView;
    }
}
