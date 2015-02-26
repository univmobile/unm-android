package org.unpidf.univmobile.data.models;

import android.content.Context;

import org.unpidf.univmobile.data.entities.Comment;
import org.unpidf.univmobile.data.entities.ErrorEntity;
import org.unpidf.univmobile.data.entities.Poi;
import org.unpidf.univmobile.data.entities.RestoMenu;
import org.unpidf.univmobile.data.operations.OperationListener;
import org.unpidf.univmobile.data.operations.SearchPoisOperation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rviewniverse on 2015-02-21.
 */
public class SearchPoisDataModel extends AbsDataModel {


	private Context mContext;
	private SearchPoisModelListener mListener;
	private List<Poi> mPois = new ArrayList<Poi>();
	//pois serach
	private SearchPoisOperation mSearchPoisOperation;

	public SearchPoisDataModel(Context c, SearchPoisModelListener listener) {
		mContext = c;
		mListener = listener;
	}

	@Override
	public void clear() {
		mContext = null;
		mListener = null;


		clearOperation(mSearchPoisOperation);
		mSearchPoisOperation = null;
	}


	public void searchPois(String key) {
		clearOperation(mSearchPoisOperation);
		mSearchPoisOperation = null;

		mSearchPoisOperation = new SearchPoisOperation(mContext, mSearchPoisOperationListener, UniversitiesDataModel.getSavedUniversity(mContext).getId(), key);
		mSearchPoisOperation.startOperation();
	}

	public List<Poi> getPois() {
		return mPois;
	}


	private OperationListener<List<Poi>> mSearchPoisOperationListener = new OperationListener<List<Poi>>() {
		@Override
		public void onOperationStarted() {

		}

		@Override
		public void onOperationFinished(ErrorEntity error, List<Poi> result) {
			clearOperation(mSearchPoisOperation);
			mSearchPoisOperation = null;
			if (mListener != null) {

				mPois = result;
				mListener.showSearchResults(result);

			}
		}

		@Override
		public void onPageDownloaded(List<Poi> result) {

		}
	};

	public interface SearchPoisModelListener {


		public void showSearchResults(List<Poi> pois);
	}
}
