package org.unpidf.univmobile.ui.fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.unpidf.univmobile.R;


public class StandartLoginFragment extends Fragment {
	private static final String ARG_DATA = "param1";

	private String mTestData;

	public static StandartLoginFragment newInstance(String tempData) {
		StandartLoginFragment fragment = new StandartLoginFragment();
		Bundle args = new Bundle();
		args.putString(ARG_DATA, tempData);
		fragment.setArguments(args);
		return fragment;
	}

	public StandartLoginFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			mTestData = getArguments().getString(ARG_DATA);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_standart_login, container, false);
	}
}
