package org.unpidf.univmobile.fragments;

import org.unpidf.univmobile.R;
import org.unpidf.univmobile.dao.Poi;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CommentaireUniversityFragment extends Fragment{

	private Poi poi;
	
	public static CommentaireUniversityFragment newInstance(Poi poi, String title) {
		Bundle bundle = new Bundle();
		bundle.putString("title", title);
		bundle.putSerializable("poi", poi);
		CommentaireUniversityFragment frag = new CommentaireUniversityFragment();
		frag.setArguments(bundle);
		return frag;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		poi = (Poi) getArguments().getSerializable("poi");
		return inflater.inflate(R.layout.frag_commentaires_university, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		//TODO
	}

}
