package org.unpidf.univmobile.ui.fragments;


import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.unpidf.univmobile.R;
import org.unpidf.univmobile.UnivMobileApp;
import org.unpidf.univmobile.data.entities.ErrorEntity;
import org.unpidf.univmobile.data.entities.News;
import org.unpidf.univmobile.data.entities.NewsFeed;
import org.unpidf.univmobile.data.models.FeedsDataModel;
import org.unpidf.univmobile.data.models.NewsAndFeedsDataModel;
import org.unpidf.univmobile.data.models.UniversitiesDataModel;
import org.unpidf.univmobile.ui.adapters.NewsAdapter;
import org.unpidf.univmobile.ui.uiutils.FontHelper;
import org.unpidf.univmobile.ui.views.NewsItemView;

import java.util.ArrayList;
import java.util.List;


public class UniversityNewsFragment extends AbsFragment {

    private NewsAndFeedsDataModel mNewsDataModel;
    public NewsAdapter mNewsAdapter;
    private ListView mListView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private FontHelper mFontHelper;
    private FeedsDataModel mFeedsDataModel;

    private LinearLayout mFeedContainer;
    private HorizontalScrollView mFeedScrollContainer;


    public UniversityNewsFragment() {
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mNewsDataModel != null) {
            mNewsDataModel.clear();
            mNewsDataModel = null;
        }

        if (mFeedsDataModel != null) {
            mFeedsDataModel.clear();
            mFeedsDataModel = null;
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_university_news, container, false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        mListView = ((ListView) view.findViewById(R.id.list_view));

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(mOnRefreshListener);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.geo_orange_light);
        TextView univName = (TextView) view.findViewById(R.id.univ_name_textView);
        String name = UniversitiesDataModel.getSavedUniversity(getActivity()).getTitle();
        univName.setText(name);

        mFontHelper = ((UnivMobileApp) getActivity().getApplicationContext()).getFontHelper();
        mFontHelper.loadFont(univName, FontHelper.FONT.EXO_BOLD);

        mFeedContainer = (LinearLayout) view.findViewById(R.id.feeds_container_linearLayout);
        mFeedScrollContainer = (HorizontalScrollView) view.findViewById(R.id.container_horizontalScrollView);


        if (mFeedsDataModel == null) {
            mFeedsDataModel = new FeedsDataModel(getActivity());
            mFeedsDataModel.setListener(mFeedsDataModelListener);
        }

        mFeedsDataModel.downloadFeeds();

    }

    private void addViewsToContainer() {

        for (int i = 0; i < mFeedsDataModel.getFeeds().size(); i++) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            CardView cardView = (CardView) inflater.inflate(R.layout.view_feed_item, null, false);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(dpToPx(10), dpToPx(5), dpToPx(10), dpToPx(5));
            cardView.setLayoutParams(params);
            TextView feedTV = (TextView) cardView.findViewById(R.id.feed_name_textView);
            feedTV.setText(mFeedsDataModel.getFeeds().get(i).getName());
            mFontHelper.loadFont(feedTV, FontHelper.FONT.EXO2_REGULAR);
            feedTV.setTag(mFeedsDataModel.getFeeds().get(i).getId());

            setSelectedFeeds(cardView, feedTV, mFeedsDataModel.getFeeds().get(i).getId());

            mFeedContainer.addView(cardView);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView clickedTV = (TextView) v.findViewById(R.id.feed_name_textView);
                    int id = (Integer) clickedTV.getTag();
                    if (mFeedsDataModel.isFeedChecked(id)) {
                        mFeedsDataModel.unselectFeed(id);
                        ressetFeeds();
                    } else {
                        mFeedsDataModel.selectFeed(id);
                        ressetFeeds();
                    }
                    if (mNewsAdapter != null) {
                        mNewsAdapter.clear();
                        mNewsAdapter.doNotShowFirstLoading();
                    }
                    mOnLoadNextListener.reload();
                }

            });
        }
    }

    private void ressetFeeds() {
        for (int i = 0; i < mFeedsDataModel.getFeeds().size(); i++) {
            CardView cardView = (CardView) mFeedContainer.getChildAt(i);
            TextView feedTV = (TextView) cardView.findViewById(R.id.feed_name_textView);
            setSelectedFeeds(cardView, feedTV, mFeedsDataModel.getFeeds().get(i).getId());
        }
    }

    private void setSelectedFeeds(CardView cardView, TextView feedTV, int id) {
        if (mFeedsDataModel.isFeedChecked(id)) {
            cardView.setCardBackgroundColor(getResources().getColor(R.color.geo_orange_light));
            feedTV.setTextColor(Color.WHITE);
        } else {
            cardView.setCardBackgroundColor(getResources().getColor(R.color.geo_orange_dark));
            feedTV.setTextColor(Color.GRAY);
        }
    }


    private Integer dpToPx(int dp) {
        Resources r = getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
        return Math.round(px);
    }


    private SwipeRefreshLayout.OnRefreshListener mOnRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            if (mNewsAdapter != null) {
                mNewsAdapter.clear();
                mNewsAdapter.doNotShowFirstLoading();
            }
            mOnLoadNextListener.reload();
        }
    };


    private NewsItemView.OnExpandListener mExpandListener = new NewsItemView.OnExpandListener() {
        @Override
        public void expanding(View view, int duration, int position) {
            final int scrollDistance = (int) (mListView.getScrollY() - view.getTop());

            mListView.smoothScrollToPositionFromTop(position, 0, duration);
        }
    };


    private NewsAndFeedsDataModel.NewsModelListener mNewsDataModelListener = new NewsAndFeedsDataModel.NewsModelListener() {
        @Override
        public void showLoadingIndicator() {
        }

        @Override
        public void updateNewsWithOnePage(List<News> news) {

        }

        @Override
        public void showErrorMessage(ErrorEntity error) {
            mSwipeRefreshLayout.setRefreshing(false);
            hideList();
        }

        @Override
        public void showNews(List<News> news) {
            mSwipeRefreshLayout.setRefreshing(false);
            if (news == null || news.size() <= 0) {
                hideList();
            }
            if (mNewsAdapter != null) {
                mNewsAdapter.addAll(news);
            }
        }


        @Override
        public void onError(ErrorEntity mError) {
            mSwipeRefreshLayout.setRefreshing(false);
            handleError(mError);
        }
    };

    private void hideList() {
        if (mNewsAdapter != null) {
            if (mNewsAdapter.getCount() > 1) {
                return;
            }
        }
        getView().findViewById(R.id.no_data).setVisibility(View.VISIBLE);
        getView().findViewById(R.id.list_view).setVisibility(View.GONE);
    }

    private NewsAdapter.OnLoadNextListener mOnLoadNextListener = new NewsAdapter.OnLoadNextListener() {
        @Override
        public void reload() {
            if (mNewsDataModel == null) {
                mNewsDataModel = new NewsAndFeedsDataModel(getActivity(), mFeedsDataModel.getSelectedFeeds());
                mNewsDataModel.setListener(mNewsDataModelListener);
            }
            mNewsDataModel.loadData();
        }

        @Override
        public void loadNextData() {
            if (mNewsDataModel == null) {
                mNewsDataModel = new NewsAndFeedsDataModel(getActivity(), mFeedsDataModel.getSelectedFeeds());
                mNewsDataModel.setListener(mNewsDataModelListener);
            }
            mNewsDataModel.loadNextPage();
        }

        @Override
        public boolean hasNextPage() {
            if (mNewsDataModel != null) {
                return mNewsDataModel.hasNewPage();
            } else {
                return true;
            }
        }
    };

    private FeedsDataModel.FeedsModelListener mFeedsDataModelListener = new FeedsDataModel.FeedsModelListener() {
        @Override
        public void showLoadingIndicator() {

        }

        @Override
        public void showErrorMessage(ErrorEntity error) {

        }

        @Override
        public void onFeedsReceived(List<NewsFeed> feeds) {

            if (feeds.size() < 2) {
                mFeedScrollContainer.setVisibility(View.GONE);
            } else {
                addViewsToContainer();
            }

            if (mNewsAdapter == null) {
                mNewsAdapter = new NewsAdapter(getActivity(), new ArrayList<News>(), mOnLoadNextListener, mExpandListener);
                mListView.setAdapter(mNewsAdapter);
            } else {
                mNewsAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onError(ErrorEntity mError) {
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        mFeedsDataModel.saveFeedsInPrefs(getActivity(), mFeedsDataModel.getSelectedFeeds());
    }
}
