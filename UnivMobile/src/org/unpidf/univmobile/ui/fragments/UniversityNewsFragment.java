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
		mNewsDataModel.clear();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_university_news, container, false);
	}


	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mListView = ((ListView) view.findViewById(R.id.list_view));
		mNewsDataModel = new NewsDataModel(getActivity());
		mNewsDataModel.getNews(mNewsDataModelListener, true);
	}

	private NewsDataModel.NewsModelListener mNewsDataModelListener = new NewsDataModel.NewsModelListener() {
		@Override
		public void showLoadingIndicator() {
			getView().findViewById(R.id.progressBar1).setVisibility(View.VISIBLE);
		}

		@Override
		public void updateNewsWithOnePage(List<News> news) {
			getView().findViewById(R.id.progressBar1).setVisibility(View.GONE);
			if (mNewsAdapter == null) {
				mNewsAdapter = new NewsAdapter(getActivity(), news);
				mListView.setAdapter(mNewsAdapter);
			} else {
				mNewsAdapter.addAll(news);
				mNewsAdapter.notifyDataSetChanged();
			}
		}

		@Override
		public void showErrorMessage(ErrorEntity error) {
			getView().findViewById(R.id.progressBar1).setVisibility(View.GONE);
		}

		@Override
		public void showNews(List<News> news) {
			getView().findViewById(R.id.progressBar1).setVisibility(View.GONE);

			if (mNewsAdapter == null) {
				mNewsAdapter = new NewsAdapter(getActivity(), news);
				mListView.setAdapter(mNewsAdapter);
			} else {
				mNewsAdapter.clear();
				mNewsAdapter.addAll(news);
				mNewsAdapter.notifyDataSetChanged();
			}

		}
	};
}
