package org.unpidf.univmobile.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import org.unpidf.univmobile.R;
import org.unpidf.univmobile.data.entities.News;
import org.unpidf.univmobile.ui.views.NewsItemView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by rviewniverse on 2015-02-02.
 */
public class NewsAdapter extends ArrayAdapter<News> {

    private SimpleDateFormat mDateFormat;
    private OnLoadNextListener mLoadNextListener;
    private NewsItemView.OnExpandListener mExpandListener;
    private View pendingView = null;
    private AtomicBoolean keepOnAppending = new AtomicBoolean(true);
    private boolean mLoading = false;
    private int pendingResource = R.layout.view_list_item_loading;
    protected int mLastItemID = -1;
    private boolean mFirstLoadingVisible = true;

    /**
     * Constructor wrapping a supplied ListAdapter and providing a id for a pending view.
     *
     * @param context
     */
    public NewsAdapter(Context context, List<News> list, OnLoadNextListener listener, NewsItemView.OnExpandListener expandListener) {
        super(context, 0, list);
        this.mLoadNextListener = listener;
        this.mExpandListener = expandListener;
        mDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
    }

    public void disablePaging() {
        keepOnAppending.set(false);
    }

    public void enablePaging() {
        keepOnAppending.set(true);
    }

    @Override
    public void clear() {
        keepOnAppending.set(true);
        super.clear();
    }

    public void doNotShowFirstLoading() {
        mFirstLoadingVisible = false;
        mLoading = true;
    }

    @Override
    public void notifyDataSetChanged() {
        if (mLoadNextListener != null && !mLoadNextListener.hasNextPage()) {
            keepOnAppending.set(false);
        }
        mLoading = false;
        super.notifyDataSetChanged();

    }

    /**
     * How many items are in the data set represented by this Adapter.
     */
    @Override
    public int getCount() {
        if (keepOnAppending.get()) {
            return (super.getCount() + 1); // one more for
            // "pending"
        }
        return (super.getCount());
    }

    /**
     * Masks ViewType so the AdapterView replaces the "Pending" row when new data is loaded.
     */
    public int getItemViewType(int position) {
        if (position == super.getCount() || position == mLastItemID) {
            return (IGNORE_ITEM_VIEW_TYPE);
        }

        return (super.getItemViewType(position));
    }

    /**
     * Masks ViewType so the AdapterView replaces the "Pending" row when new data is loaded.
     *
     * @see #getItemViewType(int)
     */
    public int getViewTypeCount() {
        return (super.getViewTypeCount() + 1);
    }

    @Override
    public News getItem(int position) {
        if (position >= super.getCount()) {
            return (null);
        }
        return (super.getItem(position));
    }

    @Override
    public boolean areAllItemsEnabled() {
        return (false);
    }

    @Override
    public boolean isEnabled(int position) {
        if (position >= super.getCount()) {
            return (false);
        }

        return (super.isEnabled(position));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if ((position == super.getCount() || position == mLastItemID) && keepOnAppending.get()) {
            if (pendingView == null) {
                pendingView = getPendingView(parent);
            }
            if (mLoadNextListener != null && !mLoading && mFirstLoadingVisible) {
                mLoading = true;
                mLoadNextListener.loadNextData();
            }

            if (!mFirstLoadingVisible && getCount() < 2) {
                pendingView.setVisibility(View.INVISIBLE);
                mFirstLoadingVisible = true;
            } else {
                pendingView.setVisibility(View.VISIBLE);
            }
            return (pendingView);
        } else if (convertView == null) {
            convertView = new NewsItemView(getContext());
            ((NewsItemView) convertView).setOnExpandListener(mExpandListener);
        }
        ((NewsItemView) convertView).populate(getItem(position), mDateFormat, position);
        return convertView;

    }

    /**
     * Inflates pending view using the pendingResource ID passed into the constructor
     *
     * @param parent
     * @return inflated pending view, or null if the context passed into the pending view constructor was null.
     */
    protected View getPendingView(ViewGroup parent) {
        if (getContext() != null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            return inflater.inflate(pendingResource, parent, false);
        }

        throw new RuntimeException("You must either override getPendingView() or supply a pending View resource via the constructor");
    }


    public interface OnLoadNextListener {

        public void reload();

        public void loadNextData();

        public boolean hasNextPage();
    }
}


