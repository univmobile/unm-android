package org.unpidf.univmobile.data.models;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import org.unpidf.univmobile.data.entities.ErrorEntity;
import org.unpidf.univmobile.data.entities.NewsFeed;
import org.unpidf.univmobile.data.operations.OperationListener;
import org.unpidf.univmobile.data.operations.ReadFeedsOperation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Created by rviewniverse on 2015-02-12.
 */
public class FeedsDataModel extends AbsDataModel {

    private Context mContext;
    private FeedsModelListener mListener;
    private ReadFeedsOperation mReadNewsOperation;

    private List<NewsFeed> mFeeds;
    private List<Integer> mSelectedFeeds = new ArrayList<>();



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

    public void downloadFeeds() {
        int univID = UniversitiesDataModel.getSavedUniversity(mContext).getId();
        mReadNewsOperation = new ReadFeedsOperation(mContext, mReadNewsListener, univID);
        mReadNewsOperation.startOperation();
    }

    public List<Integer> getSelectedFeeds() {
        return mSelectedFeeds;
    }

    public List<NewsFeed> getFeeds() {
        return mFeeds;
    }


    public static List<Integer> retrieveSavedFeeds(Context c) {
        List<Integer> feed = new ArrayList<>();

        SharedPreferences prefs = c.getSharedPreferences("univmobile", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString("MyFeedArray", "");
        if (json != null && json.length() > 0) {
            feed.addAll(Arrays.asList(gson.fromJson(json, Integer[].class)));
        }
        return feed;

    }

    private void setAllFeedsToList() {
        for (int i = 0; i < mFeeds.size(); i++) {
            mSelectedFeeds.add(mFeeds.get(i).getId());
        }
    }

    public static void saveFeedsInPrefs(Context c, List<Integer> feeds) {
        if (feeds.size() != 0) {
            SharedPreferences prefs = c.getSharedPreferences("univmobile", Context.MODE_PRIVATE);
            SharedPreferences.Editor prefsEditor = prefs.edit();
            Gson gson = new Gson();
            String json = gson.toJson(feeds);
            prefsEditor.putString("MyFeedArray", json);
            prefsEditor.commit();
        }
    }


    public boolean isFeedChecked(int id) {
        if (mSelectedFeeds.size() == 0) {
            return true;
        }
        for (Integer i : mSelectedFeeds) {
            if (i == id) {
                return true;
            }
        }
        return false;
    }

    public void selectFeed(int id) {
        if (mSelectedFeeds.contains(id)) {
        } else mSelectedFeeds.add(id);
    }

    public void unselectFeed(int id) {
        for (Iterator<Integer> iter = mSelectedFeeds.listIterator(); iter.hasNext(); ) {
            int i = iter.next();
            if (i == id) {
                iter.remove();
            }
        }

        if (mSelectedFeeds.size() == 0) {
            setAllFeedsToList();
        }
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
                    mFeeds = result;
                    mSelectedFeeds = retrieveSavedFeeds(mContext);

                    if (mSelectedFeeds.size() == 0) {
                        setAllFeedsToList();
                    }

                    mListener.onFeedsReceived(result);
                } else {
                    mListener.showErrorMessage(error);
                }
            }
        }

        @Override
        public void onPageDownloaded(List<NewsFeed> result) {
        }
    };


    public interface FeedsModelListener extends ModelListener {

        void showErrorMessage(ErrorEntity error);

        void onFeedsReceived(List<NewsFeed> news);

        void showLoadingIndicator();
    }
}
