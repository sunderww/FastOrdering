package com.eip.fastordering.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eip.fastordering.R;
import com.eip.fastordering.activity.Main;


public class OptionsFragment extends Fragment {

	private static final String ARG_SECTION_NUMBER = "section_number";

	/**
	 * Constructor
	 */
	public OptionsFragment() {
	}

	public static OptionsFragment newInstance(int sectionNumber) {
		OptionsFragment fragment = new OptionsFragment();
		Bundle          args     = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_options, container, false);
		return rootView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		((Main) activity).onSectionAttached(
				getArguments().getInt(ARG_SECTION_NUMBER));
	}

}
