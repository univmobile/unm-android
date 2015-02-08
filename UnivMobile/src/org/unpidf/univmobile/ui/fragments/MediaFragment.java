package org.unpidf.univmobile.ui.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.unpidf.univmobile.R;
import org.unpidf.univmobile.UnivMobileApp;
import org.unpidf.univmobile.ui.uiutils.FontHelper;
import org.unpidf.univmobile.ui.views.LibraryListView;
import org.unpidf.univmobile.ui.views.MediaListView;

/**
 * A simple {@link android.app.Fragment} subclass.
 */
public class MediaFragment extends Fragment {


	public static MediaFragment newInstance() {
		MediaFragment fragment = new MediaFragment();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	}

	public MediaFragment() {
		// Required empty public constructor
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_media, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		MediaListView mediaList = (MediaListView) view.findViewById(R.id.media_list);
		mediaList.init(10, null);

	}
}
