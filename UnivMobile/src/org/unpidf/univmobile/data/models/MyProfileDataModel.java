package org.unpidf.univmobile.data.models;

import android.content.Context;

import org.unpidf.univmobile.UnivMobileApp;
import org.unpidf.univmobile.data.entities.Bookmark;
import org.unpidf.univmobile.data.entities.Category;
import org.unpidf.univmobile.data.entities.ErrorEntity;
import org.unpidf.univmobile.data.entities.Library;
import org.unpidf.univmobile.data.entities.Link;
import org.unpidf.univmobile.data.entities.Login;
import org.unpidf.univmobile.data.entities.Poi;
import org.unpidf.univmobile.data.operations.OperationListener;
import org.unpidf.univmobile.data.operations.ReadBookmarksOperation;
import org.unpidf.univmobile.data.operations.ReadLibrariesOperation;
import org.unpidf.univmobile.data.operations.ReadLinksOperation;
import org.unpidf.univmobile.data.operations.ReadPoiOperation;
import org.unpidf.univmobile.data.operations.ReadPoisOperation;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by rviewniverse on 2015-02-22.
 */
public class MyProfileDataModel extends AbsDataModel {

	private Context mCotnext;
	private MyProfileDataModelInterface mListener;
	private ReadLinksOperation mReadLinksOperation;

	private ReadLibrariesOperation mReadLibrariesOperation;

	private ReadBookmarksOperation mReadBookmarksOperation;

	private boolean mLibrariesFinished = false;
	private boolean mLinksFinished = false;
	private boolean mBookmarksFinished = false;

	private final Object lock = new Object();

	public MyProfileDataModel(Context c, MyProfileDataModelInterface listener) {
		mCotnext = c;
		mListener = listener;
	}

	@Override
	public void clear() {
		clearOperation(mReadLinksOperation);
		mReadLinksOperation = null;

		clearOperation(mReadLibrariesOperation);
		mReadLibrariesOperation = null;


		clearOperation(mReadBookmarksOperation);
		mReadBookmarksOperation = null;
	}

	private void notifyFinishedIfNeeded() {
		if (mLibrariesFinished && mLinksFinished && mBookmarksFinished) {
			if (mListener != null) {
				mListener.hideLoadingIndicator();
			}
		}
	}

	public void getBookmarks() {
		clearOperation(mReadBookmarksOperation);
		mReadBookmarksOperation = null;

		Login login = ((UnivMobileApp) mCotnext.getApplicationContext()).getmLogin();
		if (login == null && mListener != null) {
			mListener.populateBookmarks(null);
			synchronized (lock) {
				mBookmarksFinished = true;
				notifyFinishedIfNeeded();
			}
		} else {
			mReadBookmarksOperation = new ReadBookmarksOperation(mCotnext, mReadBookmarksOperationListener, login.getId());
			mReadBookmarksOperation.startOperation();
		}
	}

	public void getLinks() {
		clearOperation(mReadLinksOperation);
		mReadLinksOperation = null;

		int univID = UniversitiesDataModel.getSavedUniversity(mCotnext).getId();
		mReadLinksOperation = new ReadLinksOperation(mCotnext, mReadLinksOperationListener, univID);
		mReadLinksOperation.startOperation();
	}

	public void getLibraries() {
		clearOperation(mReadLibrariesOperation);
		mReadLibrariesOperation = null;

		int id = UniversitiesDataModel.getSavedUniversity(mCotnext).getId();
		mReadLibrariesOperation = new ReadLibrariesOperation(mCotnext, mReadLibrariesOperationListener, id);
		mReadLibrariesOperation.startOperation();
	}


	private OperationListener<List<Bookmark>> mReadBookmarksOperationListener = new OperationListener<List<Bookmark>>() {
		@Override
		public void onOperationStarted() {

		}

		@Override
		public void onOperationFinished(ErrorEntity error, List<Bookmark> result) {
			if (mListener != null && result != null) {
				mListener.populateBookmarks(result);
			}
			synchronized (lock) {
				mBookmarksFinished = true;
				notifyFinishedIfNeeded();
			}
		}

		@Override
		public void onPageDownloaded(List<Bookmark> result) {

		}
	};

	private OperationListener<List<Link>> mReadLinksOperationListener = new OperationListener<List<Link>>() {
		@Override
		public void onOperationStarted() {

		}

		@Override
		public void onOperationFinished(ErrorEntity error, List<Link> result) {
			if (mListener != null && result != null) {
				mListener.populateLinks(result);
			}
			synchronized (lock) {
				mLinksFinished = true;
				notifyFinishedIfNeeded();
			}
		}

		@Override
		public void onPageDownloaded(List<Link> result) {

		}
	};

	private OperationListener<List<Library>> mReadLibrariesOperationListener = new OperationListener<List<Library>>() {
		@Override
		public void onOperationStarted() {

		}

		@Override
		public void onOperationFinished(ErrorEntity error, List<Library> result) {
			if (mListener != null && result != null) {

				mListener.populateLibraries(result);
				synchronized (lock) {
					mLibrariesFinished = true;
					notifyFinishedIfNeeded();
				}
			}
		}

		@Override
		public void onPageDownloaded(List<Library> result) {

		}
	};

	public interface MyProfileDataModelInterface {
		public void populateLinks(List<Link> links);

		public void populateLibraries(List<Library> libraries);

		public void populateBookmarks(List<Bookmark> bookmarks);

		public void hideLoadingIndicator();
	}

}
