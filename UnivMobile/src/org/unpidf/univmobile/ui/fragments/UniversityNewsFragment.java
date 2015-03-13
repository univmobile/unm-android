package org.unpidf.univmobile.ui.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import org.unpidf.univmobile.R;
import org.unpidf.univmobile.data.entities.ErrorEntity;
import org.unpidf.univmobile.data.entities.News;
import org.unpidf.univmobile.data.models.NewsDataModel;
import org.unpidf.univmobile.ui.adapters.NewsAdapter;
import org.unpidf.univmobile.ui.views.NewsItemView;
import org.unpidf.univmobile.ui.views.NewsListHeaderView;

import java.util.ArrayList;
import java.util.List;


public class UniversityNewsFragment extends AbsFragment {

	private NewsDataModel mNewsDataModel;
	private NewsAdapter mNewsAdapter;
	private ListView mListView;
	private SwipeRefreshLayout mSwipeRefreshLayout;

	public UniversityNewsFragment() {
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		if (mNewsDataModel != null) {
			mNewsDataModel.clear();
			mNewsDataModel = null;
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
		mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);

		if (mNewsAdapter == null) {
			mNewsAdapter = new NewsAdapter(getActivity(), new ArrayList<News>(), mOnLoadNextListener, mExpandListener);
			mListView.setAdapter(mNewsAdapter);
		} else {
			mNewsAdapter.notifyDataSetChanged();
		}
	}

	private SwipeRefreshLayout.OnRefreshListener mOnRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
		@Override
		public void onRefresh() {
			if(mNewsAdapter != null) {
				mNewsAdapter.clear();
				mNewsAdapter.doNotShowFirstLoading();
			}
			mOnLoadNextListener.loadNextData();
		}
	};


	private NewsItemView.OnExpandListener mExpandListener = new NewsItemView.OnExpandListener() {
		@Override
		public void expanding(View view, int duration, int position) {
			final int scrollDistance = (int) (mListView.getScrollY() - view.getTop());

			mListView.smoothScrollToPositionFromTop(position, 0, duration);
		}
	};


	private NewsDataModel.NewsModelListener mNewsDataModelListener = new NewsDataModel.NewsModelListener() {
		@Override
		public void showLoadingIndicator() {
		}

		@Override
		public void updateNewsWithOnePage(List<News> news) {

		}

		@Override
		public void showErrorMessage(ErrorEntity error) {
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
		public void loadNextData() {
			if (mNewsDataModel == null) {
				mNewsDataModel = new NewsDataModel(getActivity());
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
}
