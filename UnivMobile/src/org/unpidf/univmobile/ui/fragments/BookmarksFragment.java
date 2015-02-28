package org.unpidf.univmobile.ui.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.unpidf.univmobile.R;
import org.unpidf.univmobile.data.entities.Bookmark;
import org.unpidf.univmobile.data.entities.Library;
import org.unpidf.univmobile.data.entities.Link;
import org.unpidf.univmobile.data.entities.Poi;
import org.unpidf.univmobile.data.models.MyProfileDataModel;
import org.unpidf.univmobile.ui.activities.HomeActivity;
import org.unpidf.univmobile.ui.views.BookmarksListView;

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
		public void populateLibraries(List<Library> libraries) {


		}

		@Override
		public void populateBookmarks(List<Bookmark> bookmarks) {

			getView().findViewById(R.id.progressBar1).setVisibility(View.GONE);
			BookmarksListView bookmarksList = (BookmarksListView) getView().findViewById(R.id.bookmarks_list);
			if (bookmarks == null) {
				bookmarksList.setVisibility(View.GONE);
			} else {
				bookmarksList.init(bookmarks, Integer.MAX_VALUE, null, mOnBookmarkCLickListener);
			}
		}

		@Override
		public void hideLoadingIndicator() {

		}
	};

	private BookmarksListView.OnBookmarkClickListener mOnBookmarkCLickListener = new BookmarksListView.OnBookmarkClickListener() {
		@Override
		public void onBookmarkClicked(int root, int poiID) {
			HomeActivity a = (HomeActivity) getActivity();
			a.showPoi( poiID, root);
		}
	};
}
