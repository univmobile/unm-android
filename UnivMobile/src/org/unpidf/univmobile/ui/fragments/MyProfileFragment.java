package org.unpidf.univmobile.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.unpidf.univmobile.R;
import org.unpidf.univmobile.UnivMobileApp;
import org.unpidf.univmobile.data.entities.Bookmark;
import org.unpidf.univmobile.data.entities.ErrorEntity;
import org.unpidf.univmobile.data.entities.Library;
import org.unpidf.univmobile.data.entities.Link;
import org.unpidf.univmobile.data.entities.University;
import org.unpidf.univmobile.data.models.MyProfileDataModel;
import org.unpidf.univmobile.data.models.UniversitiesDataModel;
import org.unpidf.univmobile.ui.activities.HomeActivity;
import org.unpidf.univmobile.ui.uiutils.FontHelper;
import org.unpidf.univmobile.ui.views.BookmarksListView;
import org.unpidf.univmobile.ui.views.LibraryListView;
import org.unpidf.univmobile.ui.views.MediaListView;

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

		laodData();

		view.findViewById(R.id.edit).setOnClickListener(mChangeUnivListener);
		//init fonts
		FontHelper helper = ((UnivMobileApp) getActivity().getApplicationContext()).getFontHelper();
		helper.loadFont((android.widget.TextView) view.findViewById(R.id.name), FontHelper.FONT.EXO_REGULAR);
		helper.loadFont((android.widget.TextView) view.findViewById(R.id.university_name), FontHelper.FONT.EXO_REGULAR);
		helper.loadFont((android.widget.TextView) view.findViewById(R.id.edit), FontHelper.FONT.EXO_REGULAR);
	}

	private void laodData() {
		UnivMobileApp ap = (UnivMobileApp) getActivity().getApplication();
		TextView name = (TextView) getView().findViewById(R.id.name);
		if (ap.getLogin() != null) {
			name.setVisibility(View.VISIBLE);
			name.setText(ap.getLogin().getName());
		} else {
			name.setVisibility(View.GONE);
		}

		mModel = new MyProfileDataModel(getActivity(), mMyProfileDataModelInterface);
		mModel.getLinks();
		mModel.getLibraries();
		mModel.getBookmarks();
	}

	public void reload() {
		MediaListView mediaList = (MediaListView) getView().findViewById(R.id.media_list);
		mediaList.setVisibility(View.GONE);
		mediaList.clear();

		LibraryListView libraryList = (LibraryListView) getView().findViewById(R.id.library_list);
		libraryList.setVisibility(View.GONE);
		libraryList.clear();

		BookmarksListView bookmarksList = (BookmarksListView) getView().findViewById(R.id.bookmarks_list);
		bookmarksList.setVisibility(View.GONE);
		bookmarksList.clear();

		getView().findViewById(R.id.progressBar1).setVisibility(View.VISIBLE);

		mModel.clear();
		laodData();

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
			mediaList.setVisibility(View.VISIBLE);
			mediaList.init(links, 5, mOnAllMediaClickListener);
		}

		@Override
		public void populateLibraries(List<Library> libraries) {
			LibraryListView libraryList = (LibraryListView) getView().findViewById(R.id.library_list);
			libraryList.setVisibility(View.VISIBLE);
			libraryList.init(libraries, 5, mOnAllLibraryClickListener, mOnLibraryClickListener);

		}

		@Override
		public void populateBookmarks(List<Bookmark> bookmarks) {
			BookmarksListView bookmarksList = (BookmarksListView) getView().findViewById(R.id.bookmarks_list);
			bookmarksList.setVisibility(View.VISIBLE);
			bookmarksList.init(bookmarks, 5, mOnAllBookmarksClickListener, mOnBookmarkCLickListener);

		}

		@Override
		public void hideLoadingIndicator() {
			getView().findViewById(R.id.progressBar1).setVisibility(View.GONE);
		}

		@Override
		public void onError(ErrorEntity mError) {

			getView().findViewById(R.id.progressBar1).setVisibility(View.GONE);
			handleError(mError);
		}
	};

	private LibraryListView.OnLibraryClickListener mOnLibraryClickListener = new LibraryListView.OnLibraryClickListener() {
		@Override
		public void onLibraryClicked(int poiID) {
			HomeActivity a = (HomeActivity) getActivity();
			a.showPoi(poiID, 0);
		}
	};

	private BookmarksListView.OnBookmarkClickListener mOnBookmarkCLickListener = new BookmarksListView.OnBookmarkClickListener() {
		@Override
		public void onBookmarkClicked(int root, int poiID) {
			HomeActivity a = (HomeActivity) getActivity();
			a.showPoi(poiID, 0);
		}
	};


}
