package org.unpidf.univmobile.data.models;

import android.content.Context;

import org.unpidf.univmobile.UnivMobileApp;
import org.unpidf.univmobile.data.entities.Bookmark;
import org.unpidf.univmobile.data.entities.ErrorEntity;
import org.unpidf.univmobile.data.entities.Link;
import org.unpidf.univmobile.data.entities.Login;
import org.unpidf.univmobile.data.entities.Poi;
import org.unpidf.univmobile.data.operations.OperationListener;
import org.unpidf.univmobile.data.operations.ReadBookmarksOperation;
import org.unpidf.univmobile.data.operations.ReadLibrariesOperation;
import org.unpidf.univmobile.data.operations.ReadLinksOperation;
import org.unpidf.univmobile.data.operations.ReadPoiOperation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rviewniverse on 2015-02-22.
 */
public class LibrariesDataModel extends AbsDataModel {

	private Context mCotnext;
	private LibrariesModelInterface mListener;

	//private ReadPoisOperation mReadPoisOperation;
	private ReadLibrariesOperation mReadLibrariesOperation;
	private ReadPoiOperation mReadLibraryPoiOperation;
	private List<String> mLibrariesPoisUrls;
	private List<Poi> mLibrariesPois = new ArrayList<Poi>();
	private int mCurrentLibrary = 0;


	public LibrariesDataModel(Context c, LibrariesModelInterface listener) {
		mCotnext = c;
		mListener = listener;
	}

	@Override
	public void clear() {

		clearOperation(mReadLibrariesOperation);
		mReadLibrariesOperation = null;

		clearOperation(mReadLibraryPoiOperation);
		mReadLibraryPoiOperation = null;
	}


	public void getLibraries() {

		clearOperation(mReadLibrariesOperation);
		mReadLibrariesOperation = null;

		int id = UniversitiesDataModel.getSavedUniversity(mCotnext).getId();
		mReadLibrariesOperation = new ReadLibrariesOperation(mCotnext, mReadLibrariesOperationListener, id);
		mReadLibrariesOperation.startOperation();
	}

	private void getLibraryPoi(int pos) {
		clearOperation(mReadLibraryPoiOperation);
		mReadLibraryPoiOperation = null;

		mCurrentLibrary = pos;
		mReadLibraryPoiOperation = new ReadPoiOperation(mCotnext, mReadLibraryPoiOperationListener, -1, mLibrariesPoisUrls.get(pos));
		mReadLibraryPoiOperation.startOperation();
	}

	private OperationListener<Poi> mReadLibraryPoiOperationListener = new OperationListener<Poi>() {
		@Override
		public void onOperationStarted() {

		}

		@Override
		public void onOperationFinished(ErrorEntity error, Poi result) {
			if (mListener != null && result != null) {
				mLibrariesPois.add(result);
				if (mLibrariesPoisUrls.size() > mCurrentLibrary + 1) {

					getLibraryPoi(mCurrentLibrary + 1);
				} else {
					mListener.populateLibraries(mLibrariesPois);
				}
			}
		}

		@Override
		public void onPageDownloaded(Poi result) {

		}
	};


	private OperationListener<List<String>> mReadLibrariesOperationListener = new OperationListener<List<String>>() {
		@Override
		public void onOperationStarted() {

		}

		@Override
		public void onOperationFinished(ErrorEntity error, List<String> result) {
			if (result != null) {
				mLibrariesPoisUrls = result;
				getLibraryPoi(0);
			}
		}

		@Override
		public void onPageDownloaded(List<String> result) {

		}
	};


	public interface LibrariesModelInterface {

		public void populateLibraries(List<Poi> pois);
	}

}
