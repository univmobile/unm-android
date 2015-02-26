package org.unpidf.univmobile.ui.fragments;


import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.unpidf.univmobile.R;
import org.unpidf.univmobile.UnivMobileApp;
import org.unpidf.univmobile.data.entities.Category;
import org.unpidf.univmobile.data.entities.ErrorEntity;
import org.unpidf.univmobile.data.entities.Poi;
import org.unpidf.univmobile.data.models.LibrariesDataModel;
import org.unpidf.univmobile.data.models.UniversitiesDataModel;
import org.unpidf.univmobile.data.operations.OperationListener;
import org.unpidf.univmobile.data.operations.ReadPoisOperation;
import org.unpidf.univmobile.ui.activities.HomeActivity;
import org.unpidf.univmobile.ui.uiutils.FontHelper;
import org.unpidf.univmobile.ui.views.BookmarksListView;
import org.unpidf.univmobile.ui.views.LibraryItemView;
import org.unpidf.univmobile.ui.views.LibraryListView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class LibraryFragment extends AbsFragment {


	private LibrariesDataModel mLibrariesDataModel;

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
		mLibrariesDataModel.clear();
		mLibrariesDataModel = null;
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
		mLibrariesDataModel = new LibrariesDataModel(getActivity(), mLibrariesDataModelInterface);
		mLibrariesDataModel.getLibraries();

		//init fonts
		FontHelper helper = ((UnivMobileApp) getActivity().getApplicationContext()).getFontHelper();
		helper.loadFont((android.widget.TextView) view.findViewById(R.id.add_to_bookmarks), FontHelper.FONT.EXO_REGULAR);

		view.findViewById(R.id.add_to_bookmarks_container).setOnClickListener(mOnShowAllLibrariesClick);
	}


	private View.OnClickListener mOnShowAllLibrariesClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			HomeActivity a = (HomeActivity) getActivity();
			a.showPoiByCategory(7);
		}
	};


	private LibrariesDataModel.LibrariesModelInterface mLibrariesDataModelInterface = new LibrariesDataModel.LibrariesModelInterface() {
		@Override
		public void populateLibraries(List<Poi> pois) {
			if (pois != null && pois.size() > 0) {
				LibraryListView libraryList = (LibraryListView) getView().findViewById(R.id.library_list);
				libraryList.init(pois, Integer.MAX_VALUE, null, mOnLibraryClickListener);
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
}
