package org.unpidf.univmobile.data.models;

import android.content.Context;

import org.unpidf.univmobile.UnivMobileApp;
import org.unpidf.univmobile.data.entities.Bookmark;
import org.unpidf.univmobile.data.entities.Category;
import org.unpidf.univmobile.data.entities.ErrorEntity;
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

/**
 * Created by rviewniverse on 2015-02-22.
 */
public class MyProfileDataModel extends AbsDataModel {

	private Context mCotnext;
	private MyProfileDataModelInterface mListener;
	private ReadLinksOperation mReadLinksOperation;

	//private ReadPoisOperation mReadPoisOperation;
	private ReadLibrariesOperation mReadLibrariesOperation;
	private ReadPoiOperation mReadLibraryPoiOperation;
	private List<String> mLibrariesPoisUrls;
	private List<Poi> mLibrariesPois = new ArrayList<Poi>();
	private int mCurrentLibrary = 0;

	private ReadBookmarksOperation mReadBookmarksOperation;
	private ReadPoiOperation mReadBookmarkPoiOperation;
	private List<Bookmark> mBookmarks;
	private int mCurrentBookmark = 0;

	public MyProfileDataModel(Context c, MyProfileDataModelInterface listener) {
		mCotnext = c;
		mListener = listener;
	}

	@Override
	public void clear() {
		clearOperation(mReadLinksOperation);
		mReadLinksOperation = null;

//		clearOperation(mReadPoisOperation);
//		mReadPoisOperation = null;

		clearOperation(mReadLibrariesOperation);
		mReadLibrariesOperation = null;

		clearOperation(mReadLibraryPoiOperation);
		mReadLibraryPoiOperation = null;


		clearOperation(mReadBookmarksOperation);
		mReadBookmarksOperation = null;

		clearOperation(mReadBookmarkPoiOperation);
		mReadBookmarkPoiOperation = null;
	}

	public void getBookmarks() {
		clearOperation(mReadBookmarksOperation);
		mReadBookmarksOperation = null;
		mCurrentBookmark = 0;

		Login login = ((UnivMobileApp) mCotnext.getApplicationContext()).getmLogin();
		if (login == null && mListener != null) {
			mListener.populateBookmarks(null);
		} else {
			mReadBookmarksOperation = new ReadBookmarksOperation(mCotnext, mReadBookmarksOperationListener, login.getId());
			//mReadBookmarksOperation = new ReadBookmarksOperation(mCotnext, mReadBookmarksOperationListener, "1");
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
//		clearOperation(mReadPoisOperation);
//		mReadPoisOperation = null;

		clearOperation(mReadLibrariesOperation);
		mReadLibrariesOperation = null;

		int id = UniversitiesDataModel.getSavedUniversity(mCotnext).getId();
		mReadLibrariesOperation = new ReadLibrariesOperation(mCotnext, mReadLibrariesOperationListener, id);
		mReadLibrariesOperation.startOperation();
//		int univID = UniversitiesDataModel.getSavedUniversity(mCotnext).getId();
//		List<Category> cat = new ArrayList<Category>();
//		Category c = new Category();
//		c.setId(4);
//		cat.add(c);
//		mReadPoisOperation = new ReadPoisOperation(mCotnext, mReadPoisOperationListener, cat, -1, univID, null);
//		mReadPoisOperation.startOperation();
	}

	private void getLibraryPoi(int pos) {
		clearOperation(mReadLibraryPoiOperation);
		mReadLibraryPoiOperation = null;

		mCurrentLibrary = pos;
		mReadLibraryPoiOperation = new ReadPoiOperation(mCotnext, mReadLibraryPoiOperationListener, -1, mLibrariesPoisUrls.get(pos));
		mReadLibraryPoiOperation.startOperation();
	}

	public void getBookmakrPoi(int position) {
		clearOperation(mReadBookmarkPoiOperation);
		mReadBookmarkPoiOperation = null;

		mCurrentBookmark = position;
		mReadBookmarkPoiOperation = new ReadPoiOperation(mCotnext, mReadBookmarkPoiOperationListener, -1, mBookmarks.get(position).getPoiUrl());
		mReadBookmarkPoiOperation.startOperation();
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

	private OperationListener<Poi> mReadBookmarkPoiOperationListener = new OperationListener<Poi>() {
		@Override
		public void onOperationStarted() {

		}

		@Override
		public void onOperationFinished(ErrorEntity error, Poi result) {
			if (mListener != null && result != null) {
				mBookmarks.get(mCurrentBookmark).setPoi(result);
				if (mBookmarks.size() > mCurrentBookmark + 1) {

					getBookmakrPoi(mCurrentBookmark + 1);
				} else {
					mListener.populateBookmarks(mBookmarks);
				}
			}
		}

		@Override
		public void onPageDownloaded(Poi result) {

		}
	};

	private OperationListener<List<Bookmark>> mReadBookmarksOperationListener = new OperationListener<List<Bookmark>>() {
		@Override
		public void onOperationStarted() {

		}

		@Override
		public void onOperationFinished(ErrorEntity error, List<Bookmark> result) {
			if (mListener != null && result != null) {
				mBookmarks = result;
				getBookmakrPoi(0);
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
		}

		@Override
		public void onPageDownloaded(List<Link> result) {

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

//	private OperationListener<List<Poi>> mReadPoisOperationListener = new OperationListener<List<Poi>>() {
//		@Override
//		public void onOperationStarted() {
//
//		}
//
//		@Override
//		public void onOperationFinished(ErrorEntity error, List<Poi> result) {
//			if (mListener != null && result != null) {
//				mListener.populateLibraries(result);
//			}
//		}
//
//		@Override
//		public void onPageDownloaded(List<Poi> result) {
//
//		}
//	};

	public interface MyProfileDataModelInterface {
		public void populateLinks(List<Link> links);

		public void populateLibraries(List<Poi> pois);

		public void populateBookmarks(List<Bookmark> bookmarks);
	}

}
