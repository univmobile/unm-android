package org.unpidf.univmobile.ui.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.unpidf.univmobile.R;
import org.unpidf.univmobile.ui.adapters.NewsAdapter;
import org.unpidf.univmobile.ui.views.NewsListHeaderView;


public class UniversityNewsFragment extends Fragment {


	public UniversityNewsFragment() {
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_university_news, container, false);
	}


	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		ListView list = ((ListView)view.findViewById(R.id.list_view));
		list.setAdapter(new NewsAdapter(getActivity(), null));
		list.addHeaderView(new NewsListHeaderView(getActivity()));
	}
}
