package org.unpidf.univmobile.fragments;

import org.unpidf.univmobile.R;
import org.unpidf.univmobile.dao.Poi;
import org.unpidf.univmobile.view.UniversityActivity;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
/**
 * See all comments of a specific university. Fragment for: {@link UniversityActivity}
 * @author Michel
 *
 */
public class UniversityCommentFragment extends Fragment{

	private Poi poi;
	
	public static UniversityCommentFragment newInstance(Poi poi, String title) {
		Bundle bundle = new Bundle();
		bundle.putString("title", title);
		bundle.putSerializable("poi", poi);
		UniversityCommentFragment frag = new UniversityCommentFragment();
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
