package org.unpidf.univmobile.data.models;

import android.content.Context;

import org.unpidf.univmobile.data.entities.ErrorEntity;
import org.unpidf.univmobile.data.entities.News;
import org.unpidf.univmobile.data.entities.NewsFeed;
import org.unpidf.univmobile.data.operations.AbsOperation;
import org.unpidf.univmobile.data.operations.OperationListener;
import org.unpidf.univmobile.data.operations.ReadFeedsOperation;
import org.unpidf.univmobile.data.operations.ReadNewsOperation;

import java.util.List;

/**
 * Created by rviewniverse on 2015-02-12.
 */
public class FeedsDataModel extends AbsDataModel {

	private Context mContext;
	private FeedsModelListener mListener;

	private ReadFeedsOperation mReadNewsOperation;

	public FeedsDataModel(Context c) {
		mContext = c;
	}

	@Override
	public void clear() {
		clearOperation(mReadNewsOperation);
		mReadNewsOperation = null;

		mContext = null;
		mListener = null;
	}

	public void setListener(FeedsModelListener listener) {
		mListener = listener;
	}

	public void getFeeds() {
		int univID = UniversitiesDataModel.getSavedUniversity(mContext).getId();
		mReadNewsOperation = new ReadFeedsOperation(mContext, mReadNewsListener, univID);
		mReadNewsOperation.startOperation();
	}



	private OperationListener<List<NewsFeed>> mReadNewsListener = new OperationListener<List<NewsFeed>>() {
		@Override
		public void onOperationStarted() {
			if (mListener != null) {
				mListener.showLoadingIndicator();
			}
		}

		@Override
		public void onOperationFinished(ErrorEntity error, List<NewsFeed> result) {
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
		public void onPageDownloaded(List<NewsFeed> result) {
		}
	};


	public interface FeedsModelListener extends ModelListener{

		void showErrorMessage(ErrorEntity error);

		void showNews(List<NewsFeed> news);

		void showLoadingIndicator();
	}
}
