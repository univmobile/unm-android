package org.unpidf.univmobile.data.models;

import android.content.Context;

import org.unpidf.univmobile.data.entities.ErrorEntity;
import org.unpidf.univmobile.data.entities.News;
import org.unpidf.univmobile.data.operations.AbsOperation;
import org.unpidf.univmobile.data.operations.OperationListener;
import org.unpidf.univmobile.data.operations.ReadNewsOperation;

import java.util.List;

/**
 * Created by rviewniverse on 2015-02-12.
 */
public class NewsDataModel extends AbsDataModel {

	private Context mContext;
	private NewsModelListener mListener;

	private ReadNewsOperation mReadNewsOperation;
	private boolean mShouldBePaged = false;

	public NewsDataModel(Context c) {
		mContext = c;
	}

	@Override
	public void clear() {
		clearOperation(mReadNewsOperation);
		mReadNewsOperation = null;

		mContext = null;
		mListener = null;
	}


	public void getNews(NewsModelListener listener, boolean shouldBePaged) {
		clearOperation(mReadNewsOperation);
		mReadNewsOperation = null;

		mListener = listener;
		mShouldBePaged = shouldBePaged;

		int univID = UniversitiesDataModel.getSavedUniversity(mContext).getId();
		mReadNewsOperation = new ReadNewsOperation(mContext, mReadNewsListener, univID);
		mReadNewsOperation.startOperation();
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
			clearOperation(mReadNewsOperation);
			mReadNewsOperation = null;

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
			if (mListener != null && mShouldBePaged) {

				mListener.updateNewsWithOnePage(result);

			}
		}
	};


	public interface NewsModelListener {
		void showLoadingIndicator();

		void updateNewsWithOnePage(List<News> news);

		void showErrorMessage(ErrorEntity error);

		void showNews(List<News> news);
	}
}
