package org.unpidf.univmobile.ui.fragments;


import android.app.Fragment;
import android.os.Bundle;
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
import org.unpidf.univmobile.ui.views.MediaListView;

import java.util.List;

public class BookmarksFragment extends AbsFragment {
	private MyProfileDataModel mModel;

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
	public void onDestroy() {
		super.onDestroy();
		if (mModel != null) {
			mModel.clear();
			mModel = null;
		}
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_bookmarks, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		mModel = new MyProfileDataModel(getActivity(), mMyProfileDataModelInterface);
		mModel.getBookmarks();


	}

	private MyProfileDataModel.MyProfileDataModelInterface mMyProfileDataModelInterface = new MyProfileDataModel.MyProfileDataModelInterface() {
		@Override
		public void populateLinks(List<Link> links) {

		}

		@Override
		public void populateLibraries(List<Poi> pois) {


		}

		@Override
		public void populateBookmarks(List<Bookmark> bookmarks) {
			BookmarksListView bookmarksList = (BookmarksListView) getView().findViewById(R.id.bookmarks_list);
			if (bookmarks == null) {
				bookmarksList.setVisibility(View.GONE);
			} else {
				bookmarksList.init(bookmarks, Integer.MAX_VALUE, null, mOnBookmarkCLickListener);
			}
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
