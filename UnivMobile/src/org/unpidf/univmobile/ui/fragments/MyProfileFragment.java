package org.unpidf.univmobile.ui.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.unpidf.univmobile.R;
import org.unpidf.univmobile.UnivMobileApp;
import org.unpidf.univmobile.data.entities.Bookmark;
import org.unpidf.univmobile.data.entities.Link;
import org.unpidf.univmobile.data.entities.Poi;
import org.unpidf.univmobile.data.entities.University;
import org.unpidf.univmobile.data.models.MyProfileDataModel;
import org.unpidf.univmobile.data.models.UniversitiesDataModel;
import org.unpidf.univmobile.ui.activities.HomeActivity;
import org.unpidf.univmobile.ui.uiutils.FontHelper;
import org.unpidf.univmobile.ui.views.BookmarksListView;
import org.unpidf.univmobile.ui.views.LibraryListView;
import org.unpidf.univmobile.ui.views.MediaItemView;
import org.unpidf.univmobile.ui.views.MediaListView;
import org.w3c.dom.Text;

import java.util.List;


public class MyProfileFragment extends AbsFragment {

	private MyProfileDataModel mModel;


	public static MyProfileFragment newInstance() {
		MyProfileFragment fragment = new MyProfileFragment();
		Bundle args = new Bundle();

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

		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mModel != null) {
			mModel.clear();
			mModel = null;
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_my_profile, container, false);
	}


	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		//init profile and university
		University u = UniversitiesDataModel.getSavedUniversity(getActivity());
		TextView unName = (TextView) view.findViewById(R.id.university_name);
		unName.setText(u.getTitle());

		UnivMobileApp ap = (UnivMobileApp) getActivity().getApplication();
		TextView name = (TextView) view.findViewById(R.id.name);
		if (ap.getmLogin() != null) {

			name.setText(ap.getmLogin().getName());
		} else {
			name.setVisibility(View.GONE);
		}

		mModel = new MyProfileDataModel(getActivity(), mMyProfileDataModelInterface);
		mModel.getLinks();
		mModel.getLibraries();
		mModel.getBookmarks();

		view.findViewById(R.id.edit).setOnClickListener(mChangeUnivListener);
		//init fonts
		FontHelper helper = ((UnivMobileApp) getActivity().getApplicationContext()).getFontHelper();
		helper.loadFont((android.widget.TextView) view.findViewById(R.id.name), FontHelper.FONT.EXO_REGULAR);
		helper.loadFont((android.widget.TextView) view.findViewById(R.id.university_name), FontHelper.FONT.EXO_REGULAR);
		helper.loadFont((android.widget.TextView) view.findViewById(R.id.edit), FontHelper.FONT.EXO_REGULAR);
	}

	private View.OnClickListener mChangeUnivListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			HomeActivity a = (HomeActivity) getActivity();
			a.showFragment(ChangeUniversityFragment.newInstance(), ChangeUniversityFragment.class.getName(), true);
		}
	};


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

	private MyProfileDataModel.MyProfileDataModelInterface mMyProfileDataModelInterface = new MyProfileDataModel.MyProfileDataModelInterface() {
		@Override
		public void populateLinks(List<Link> links) {
			MediaListView mediaList = (MediaListView) getView().findViewById(R.id.media_list);
			mediaList.init(links, 5, mOnAllMediaClickListener);
		}

		@Override
		public void populateLibraries(List<Poi> pois) {

			if (pois != null && pois.size() > 0) {
				LibraryListView libraryList = (LibraryListView) getView().findViewById(R.id.library_list);
				libraryList.init(pois, 5, mOnAllLibraryClickListener, mOnLibraryClickListener);
			}

		}

		@Override
		public void populateBookmarks(List<Bookmark> bookmarks) {
			BookmarksListView bookmarksList = (BookmarksListView) getView().findViewById(R.id.bookmarks_list);
			if (bookmarks == null) {
				bookmarksList.setVisibility(View.GONE);
			} else {
				bookmarksList.init(bookmarks, 5, mOnAllBookmarksClickListener, mOnBookmarkCLickListener);
			}
		}
	};

	private LibraryListView.OnLibraryClickListener mOnLibraryClickListener = new LibraryListView.OnLibraryClickListener() {
		@Override
		public void onLibraryClicked(Poi poi) {
			HomeActivity a = (HomeActivity) getActivity();
			a.showPoi(poi, false);
		}
	};

	private BookmarksListView.OnBookmarkClickListener mOnBookmarkCLickListener = new BookmarksListView.OnBookmarkClickListener() {
		@Override
		public void onBookmarkClicked(Poi poi) {
			HomeActivity a = (HomeActivity) getActivity();
			a.showPoi(poi, false);
		}
	};


}
