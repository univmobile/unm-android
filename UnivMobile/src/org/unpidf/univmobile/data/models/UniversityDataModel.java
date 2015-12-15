package org.unpidf.univmobile.data.models;

import android.content.Context;

import org.unpidf.univmobile.data.entities.Category;
import org.unpidf.univmobile.data.entities.ErrorEntity;
import org.unpidf.univmobile.data.entities.News;
import org.unpidf.univmobile.data.entities.Poi;
import org.unpidf.univmobile.data.operations.OperationListener;
import org.unpidf.univmobile.data.operations.ReadCategoriesOperation;
import org.unpidf.univmobile.data.operations.ReadNewsAndFeedsOperation;
import org.unpidf.univmobile.data.operations.ReadPoisOperation;

import java.util.List;

/**
 * Created by rviewniverse on 2015-02-12.
 */
public class UniversityDataModel extends AbsDataModel {

    private Context mContext;
    private UniversityModelListener mListener;

    private ReadNewsAndFeedsOperation mReadNewsOperation;

    private ReadPoisOperation mReadPoisOperation;

    //all categories
    private ReadCategoriesOperation mReadCategoriesOperationAll;
    private List<Category> mCategoriesAll;

    public UniversityDataModel(Context c, UniversityModelListener listener) {
        mContext = c;
        mListener = listener;
    }

    @Override
    public void clear() {
        mContext = null;
        mListener = null;

        clearOperation(mReadNewsOperation);
        mReadNewsOperation = null;

        clearOperation(mReadPoisOperation);
        mReadPoisOperation = null;

        clearOperation(mReadCategoriesOperationAll);
        mReadCategoriesOperationAll = null;

    }

    public Category getCategoryById(int id) {
        for (Category c : mCategoriesAll) {
            if (c.getId() == id) {
                return c;
            }
        }
        return null;
    }

    public void getCategories() {
        clearOperation(mReadCategoriesOperationAll);
        mReadCategoriesOperationAll = null;


        mReadCategoriesOperationAll = new ReadCategoriesOperation(mContext, mCategoriesAllListener, -1);
        mReadCategoriesOperationAll.startOperation();
    }

    public void getNews() {
        clearOperation(mReadNewsOperation);
        mReadNewsOperation = null;

        int univID = UniversitiesDataModel.getSavedUniversity(mContext).getId();
        mReadNewsOperation = new ReadNewsAndFeedsOperation(mContext, mReadNewsListener, univID, 0, 5, FeedsDataModel.retrieveSavedFeeds(mContext));
        mReadNewsOperation.startOperation();
    }

    public void getPois() {
        clearOperation(mReadPoisOperation);
        mReadPoisOperation = null;

        int univID = UniversitiesDataModel.getSavedUniversity(mContext).getId();
        mReadPoisOperation = new ReadPoisOperation(mContext, mReadPoisListener, null, -1, univID, null);
        mReadPoisOperation.startOperation();
    }


    private OperationListener<List<Category>> mCategoriesAllListener = new OperationListener<List<Category>>() {
        @Override
        public void onOperationStarted() {
        }


        @Override
        public void onOperationFinished(ErrorEntity error, List<Category> result) {
            if (error != null && mListener != null) {
                mListener.onError(error);
            }

            mReadCategoriesOperationAll.clear();
            mReadCategoriesOperationAll = null;

            mCategoriesAll = result;

            getPois();
        }

        @Override
        public void onPageDownloaded(List<Category> result) {

        }
    };

    private OperationListener<List<Poi>> mReadPoisListener = new OperationListener<List<Poi>>() {
        @Override
        public void onOperationStarted() {

        }

        @Override
        public void onOperationFinished(ErrorEntity error, List<Poi> result) {
            if (error != null && mListener != null) {
                mListener.onError(error);
            }
            clearOperation(mReadPoisOperation);

            if (mListener != null) {
                if (result != null) {
                    mListener.updatePois(result);
                } else {
                    mListener.showErrorMessage(error);
                }
            }

        }

        @Override
        public void onPageDownloaded(List<Poi> result) {

        }
    };

    private OperationListener<List<News>> mReadNewsListener = new OperationListener<List<News>>() {
        @Override
        public void onOperationStarted() {

        }

        @Override
        public void onOperationFinished(ErrorEntity error, List<News> result) {
            if (error != null && mListener != null) {
                mListener.onError(error);
            }
            clearOperation(mReadNewsOperation);
            mReadNewsOperation = null;

            if (mListener != null) {
                if (result != null) {
                    mListener.updateNews(result);
                } else {
                    mListener.showErrorMessage(error);
                }
            }
        }

        @Override
        public void onPageDownloaded(List<News> result) {
        }
    };


    public interface UniversityModelListener extends ModelListener {


        void updateNews(List<News> news);

        void updatePois(List<Poi> pois);

        void showErrorMessage(ErrorEntity error);
    }
}
