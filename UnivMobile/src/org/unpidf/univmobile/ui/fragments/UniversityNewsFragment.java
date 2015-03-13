package org.unpidf.univmobile.ui.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.unpidf.univmobile.R;
import org.unpidf.univmobile.data.entities.ErrorEntity;
import org.unpidf.univmobile.data.entities.News;
import org.unpidf.univmobile.data.models.NewsDataModel;
import org.unpidf.univmobile.ui.adapters.NewsAdapter;
import org.unpidf.univmobile.ui.views.NewsListHeaderView;

import java.util.ArrayList;
import java.util.List;


public class UniversityNewsFragment extends AbsFragment {

	private NewsDataModel mNewsDataModel;
	private NewsAdapter mNewsAdapter;
	private ListView mListView;

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

		if (mNewsAdapter == null) {
			mNewsAdapter = new NewsAdapter(getActivity(), new ArrayList<News>(), mOnLoadNextListener);
			mListView.setAdapter(mNewsAdapter);
		} else {
			mNewsAdapter.notifyDataSetChanged();
		}
	}

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
			if(news == null || news.size() <= 0) {
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
		if(mNewsAdapter != null) {
			if(mNewsAdapter.getCount() > 1) {
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
