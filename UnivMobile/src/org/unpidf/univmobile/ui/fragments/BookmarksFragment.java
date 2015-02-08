package org.unpidf.univmobile.ui.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.unpidf.univmobile.R;
import org.unpidf.univmobile.ui.views.BookmarksListView;
import org.unpidf.univmobile.ui.views.MediaListView;

public class BookmarksFragment extends Fragment {


	public static BookmarksFragment newInstance() {
		BookmarksFragment fragment = new BookmarksFragment();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	}

	public BookmarksFragment() {
		// Required empty public constructor
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_bookmarks, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		BookmarksListView bookmarksList = (BookmarksListView) view.findViewById(R.id.bookmarks_list);
		bookmarksList.init(10, null);

	}
}
