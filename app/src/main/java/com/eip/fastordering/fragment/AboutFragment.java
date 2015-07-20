package com.eip.fastordering.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eip.fastordering.R;
import com.eip.fastordering.activity.Main;


public class AboutFragment extends Fragment {

	private static final String ARG_SECTION_NUMBER = "section_number";

	/**
	 * Constructor
	 */
	public AboutFragment() {
	}

	public static AboutFragment newInstance(int sectionNumber) {
		AboutFragment fragment = new AboutFragment();
		Bundle        args     = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_about, container, false);
		return rootView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		((Main) activity).onSectionAttached(
				getArguments().getInt(ARG_SECTION_NUMBER));
	}
}
