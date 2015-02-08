package org.unpidf.univmobile.ui.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.unpidf.univmobile.R;
import org.unpidf.univmobile.UnivMobileApp;
import org.unpidf.univmobile.ui.uiutils.FontHelper;
import org.unpidf.univmobile.ui.views.BookmarksListView;
import org.unpidf.univmobile.ui.views.LibraryItemView;
import org.unpidf.univmobile.ui.views.LibraryListView;

/**
 * A simple {@link Fragment} subclass.
 */
public class LibraryFragment extends Fragment {


	public static LibraryFragment newInstance() {
		LibraryFragment fragment = new LibraryFragment();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	}

	public LibraryFragment() {
		// Required empty public constructor
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_library, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		LibraryListView libraryList = (LibraryListView) view.findViewById(R.id.library_list);
		libraryList.init(10, null);

		//init fonts
		FontHelper helper = ((UnivMobileApp) getActivity().getApplicationContext()).getFontHelper();
		helper.loadFont((android.widget.TextView) view.findViewById(R.id.add_to_bookmarks), FontHelper.FONT.EXO_REGULAR);
	}
}
