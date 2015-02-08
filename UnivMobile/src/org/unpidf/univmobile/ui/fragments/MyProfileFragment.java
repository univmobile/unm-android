package org.unpidf.univmobile.ui.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.unpidf.univmobile.R;
import org.unpidf.univmobile.UnivMobileApp;
import org.unpidf.univmobile.ui.activities.HomeActivity;
import org.unpidf.univmobile.ui.uiutils.FontHelper;
import org.unpidf.univmobile.ui.views.BookmarksListView;
import org.unpidf.univmobile.ui.views.LibraryListView;
import org.unpidf.univmobile.ui.views.MediaItemView;
import org.unpidf.univmobile.ui.views.MediaListView;


public class MyProfileFragment extends Fragment {

	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";

	private String mParam1;
	private String mParam2;



	public static MyProfileFragment newInstance() {
		MyProfileFragment fragment = new MyProfileFragment();
		Bundle args = new Bundle();
//		args.putString(ARG_PARAM1, param1);
//		args.putString(ARG_PARAM2, param2);
		fragment.setArguments(args);
		return fragment;
	}

	public MyProfileFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			mParam1 = getArguments().getString(ARG_PARAM1);
			mParam2 = getArguments().getString(ARG_PARAM2);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_my_profile, container, false);
	}


	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		MediaListView mediaList = (MediaListView) view.findViewById(R.id.media_list);
		mediaList.init(5, mOnAllMediaClickListener);

		LibraryListView libraryList = (LibraryListView) view.findViewById(R.id.library_list);
		libraryList.init(5, mOnAllLibraryClickListener);

		BookmarksListView bookmarksList = (BookmarksListView) view.findViewById(R.id.bookmarks_list);
		bookmarksList.init(5, mOnAllBookmarksClickListener);

		//init fonts
		FontHelper helper = ((UnivMobileApp) getActivity().getApplicationContext()).getFontHelper();
		helper.loadFont((android.widget.TextView) view.findViewById(R.id.name), FontHelper.FONT.EXO_REGULAR);
		helper.loadFont((android.widget.TextView) view.findViewById(R.id.university_name), FontHelper.FONT.EXO_REGULAR);
		helper.loadFont((android.widget.TextView) view.findViewById(R.id.edit), FontHelper.FONT.EXO_REGULAR);
	}

	private View.OnClickListener mOnAllMediaClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			HomeActivity a = (HomeActivity) getActivity();
			a.showFragment(MediaFragment.newInstance(), MediaFragment.class.getName(), true);
		}
	};

	private View.OnClickListener mOnAllLibraryClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			HomeActivity a = (HomeActivity) getActivity();
			a.showFragment(LibraryFragment.newInstance(), LibraryFragment.class.getName(), true);
		}
	};

	private View.OnClickListener mOnAllBookmarksClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			HomeActivity a = (HomeActivity) getActivity();
			a.showFragment(BookmarksFragment.newInstance(), BookmarksFragment.class.getName(), true);
		}
	};
}
