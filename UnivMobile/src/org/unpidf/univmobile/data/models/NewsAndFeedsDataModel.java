package org.unpidf.univmobile.data.models;

import android.content.Context;

import org.unpidf.univmobile.data.entities.ErrorEntity;
import org.unpidf.univmobile.data.entities.News;
import org.unpidf.univmobile.data.operations.OperationListener;
import org.unpidf.univmobile.data.operations.ReadNewsOperation;

import java.util.List;

/**
 * Created by rviewniverse on 2015-02-12.
 */
public class NewsAndFeedsDataModel extends AbsDataModel {

	private Context mContext;
	private NewsModelListener mListener;

	private ReadNewsOperation mReadNewsOperation;

	private List<News> mNews;
	private List<Integer> mFeedList;

	public NewsAndFeedsDataModel(Context c, List<Integer> feedList) {
		mContext = c;
		mFeedList = feedList;
	}

	@Override
	public void clear() {
		clearOperation(mReadNewsOperation);
		mReadNewsOperation = null;

		mContext = null;
		mListener = null;
	}

	public void setListener(NewsModelListener listener) {
		mListener = listener;
	}

	public void loadData() {
		int univID = UniversitiesDataModel.getSavedUniversity(mContext).getId();
		mReadNewsOperation = new ReadNewsOperation(mContext, mReadNewsListener, univID, 0, 0, mFeedList);
		mReadNewsOperation.startOperation();
	}

	public void loadNextPage() {
		if (mReadNewsOperation != null && mReadNewsOperation.hasNextPage()) {
			int nextPage = mReadNewsOperation.getNextPage();
			clearOperation(mReadNewsOperation);
			mReadNewsOperation = null;

			int univID = UniversitiesDataModel.getSavedUniversity(mContext).getId();
			mReadNewsOperation = new ReadNewsOperation(mContext, mReadNewsListener, univID, nextPage, 0, mFeedList);
			mReadNewsOperation.startOperation();
		} else {
			loadData();
		}
	}

	public boolean hasNewPage() {
		if (mReadNewsOperation != null) {
			return mReadNewsOperation.hasNextPage();
		} else {
			return true;
		}
	}

	private OperationListener<List<News>> mReadNewsListener = new OperationListener<List<News>>() {
		@Override
		public void onOperationStarted() {
			if (mListener != null) {
				mListener.showLoadingIndicator();
			}
		}

		@Override
		public void onOperationFinished(ErrorEntity error, List<News> result) {
			if (error != null && mListener != null) {
				mListener.onError(error);
			}
			if (mListener != null) {
				if (result != null) {
					mListener.showNews(result);
				} else {
					mListener.showErrorMessage(error);
				}
			}
		}

		@Override
		public void onPageDownloaded(List<News> result) {
		}
	};


	public interface NewsModelListener extends ModelListener{
		void showLoadingIndicator();

		void updateNewsWithOnePage(List<News> news);

		void showErrorMessage(ErrorEntity error);

		void showNews(List<News> news);
	}
}
