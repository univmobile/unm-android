package org.unpidf.univmobile.ui.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.unpidf.univmobile.R;
import org.unpidf.univmobile.UnivMobileApp;
import org.unpidf.univmobile.data.entities.ErrorEntity;
import org.unpidf.univmobile.data.entities.Link;
import org.unpidf.univmobile.data.models.UniversitiesDataModel;
import org.unpidf.univmobile.data.operations.OperationListener;
import org.unpidf.univmobile.data.operations.ReadLinksOperation;
import org.unpidf.univmobile.ui.uiutils.FontHelper;
import org.unpidf.univmobile.ui.views.LibraryListView;
import org.unpidf.univmobile.ui.views.MediaListView;

import java.util.List;

/**
 * A simple {@link android.app.Fragment} subclass.
 */
public class MediaFragment extends AbsFragment {
	private ReadLinksOperation mReadLinksOperation;

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
	public void onDestroy() {
		super.onDestroy();
		mReadLinksOperation.clear();
		mReadLinksOperation = null;

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_media, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		int univID = UniversitiesDataModel.getSavedUniversity(getActivity()).getId();
		mReadLinksOperation = new ReadLinksOperation(getActivity(), mReadLinksOperationListener, univID);
		mReadLinksOperation.startOperation();

	}

	private OperationListener<List<Link>> mReadLinksOperationListener = new OperationListener<List<Link>>() {
		@Override
		public void onOperationStarted() {

		}

		@Override
		public void onOperationFinished(ErrorEntity error, List<Link> result) {
			getView().findViewById(R.id.progressBar1).setVisibility(View.GONE);
			if (error != null) {
				handleError(error);
			}
			if (result != null) {
				MediaListView mediaList = (MediaListView) getView().findViewById(R.id.media_list);
				mediaList.init(result, Integer.MAX_VALUE, null);
			}
		}

		@Override
		public void onPageDownloaded(List<Link> result) {

		}
	};
}
