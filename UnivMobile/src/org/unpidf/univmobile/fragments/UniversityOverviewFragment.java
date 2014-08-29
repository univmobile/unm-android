package org.unpidf.univmobile.fragments;

import org.unpidf.univmobile.R;
import org.unpidf.univmobile.dao.Poi;
import org.unpidf.univmobile.view.UniversityActivity;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
/**
 * Display some information about a university. Fragment for viewpager in : {@link MapsPoiFragment}
 * @author Michel
 *
 */
public class UniversityOverviewFragment extends Fragment{

	private Poi poi;

	public static UniversityOverviewFragment newInstance(Poi poi) {
		Bundle bundle = new Bundle();
		bundle.putSerializable("poi", poi);
		UniversityOverviewFragment frag = new UniversityOverviewFragment();
		frag.setArguments(bundle);
		return frag;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		poi = (Poi) getArguments().getSerializable("poi");
		return inflater.inflate(R.layout.frag_item_university, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		((TextView)getView().findViewById(R.id.textId)).setText(poi.getTitle());
		if(poi.getAdress() != null){
			((TextView)getView().findViewById(R.id.adresseId)).setText(poi.getAdress());
		}
		getView().findViewById(R.id.rootUnivItem).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getActivity(), UniversityActivity.class).putExtra("poi", poi));
			}
		});
	}

}
