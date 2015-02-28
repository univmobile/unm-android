package org.unpidf.univmobile.ui.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.unpidf.univmobile.R;
import org.unpidf.univmobile.UnivMobileApp;
import org.unpidf.univmobile.data.entities.Bookmark;
import org.unpidf.univmobile.data.entities.Category;
import org.unpidf.univmobile.data.entities.Library;
import org.unpidf.univmobile.data.entities.Link;
import org.unpidf.univmobile.data.entities.Poi;
import org.unpidf.univmobile.data.models.MyProfileDataModel;
import org.unpidf.univmobile.data.models.UniversitiesDataModel;
import org.unpidf.univmobile.ui.activities.HomeActivity;
import org.unpidf.univmobile.ui.uiutils.FontHelper;
import org.unpidf.univmobile.ui.views.LibraryListView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class LibraryFragment extends AbsFragment {


	private MyProfileDataModel mMyProfileDataModel;

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
	public void onDestroy() {
		super.onDestroy();
		mMyProfileDataModel.clear();
		mMyProfileDataModel = null;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_library, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		int univID = UniversitiesDataModel.getSavedUniversity(getActivity()).getId();
		List<Category> cat = new ArrayList<Category>();
		Category c = new Category();
		c.setId(4);
		cat.add(c);
		mMyProfileDataModel = new MyProfileDataModel(getActivity(), mMyProfileDataModelInterface);
		mMyProfileDataModel.getLibraries();

		//init fonts
		FontHelper helper = ((UnivMobileApp) getActivity().getApplicationContext()).getFontHelper();
		helper.loadFont((android.widget.TextView) view.findViewById(R.id.add_to_bookmarks), FontHelper.FONT.EXO_REGULAR);

		view.findViewById(R.id.add_to_bookmarks_container).setOnClickListener(mOnShowAllLibrariesClick);
	}


	private View.OnClickListener mOnShowAllLibrariesClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			HomeActivity a = (HomeActivity) getActivity();
			a.showPoiByCategory(7, 0);
		}
	};


	private MyProfileDataModel.MyProfileDataModelInterface mMyProfileDataModelInterface = new MyProfileDataModel.MyProfileDataModelInterface() {
		@Override
		public void populateLinks(List<Link> links) {

		}

		@Override
		public void populateLibraries(List<Library> libraries) {
			if (libraries != null && libraries.size() > 0) {
				LibraryListView libraryList = (LibraryListView) getView().findViewById(R.id.library_list);
				libraryList.setVisibility(View.VISIBLE);
				libraryList.init(libraries, 5, null, mOnLibraryClickListener);
			}
		}

		@Override
		public void populateBookmarks(List<Bookmark> bookmarks) {

		}

		@Override
		public void hideLoadingIndicator() {

		}
	};


	private LibraryListView.OnLibraryClickListener mOnLibraryClickListener = new LibraryListView.OnLibraryClickListener() {
		@Override
		public void onLibraryClicked(int poiID) {
			HomeActivity a = (HomeActivity) getActivity();
			a.showPoi(poiID,0);
		}
	};
}
