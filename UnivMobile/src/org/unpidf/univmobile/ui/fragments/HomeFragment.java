package org.unpidf.univmobile.ui.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.unpidf.univmobile.R;
import org.unpidf.univmobile.UnivMobileApp;
import org.unpidf.univmobile.ui.uiutils.FontHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {


	public static HomeFragment newInstance() {
		HomeFragment fragment = new HomeFragment();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	}

	public HomeFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
		}
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_home, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		//init fonts
		FontHelper helper = ((UnivMobileApp) getActivity().getApplicationContext()).getFontHelper();
		helper.loadFont((android.widget.TextView) view.findViewById(R.id.main_article_time), FontHelper.FONT.EXO_ITALIC);
		helper.loadFont((android.widget.TextView) view.findViewById(R.id.main_article_title), FontHelper.FONT.EXO_BOLD);
		helper.loadFont((android.widget.TextView) view.findViewById(R.id.main_article_content), FontHelper.FONT.EXO_REGULAR);
		helper.loadFont((android.widget.TextView) view.findViewById(R.id.latest_news_title), FontHelper.FONT.EXO_BOLD);
	}
}
