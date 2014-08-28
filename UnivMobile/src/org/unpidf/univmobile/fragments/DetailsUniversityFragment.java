package org.unpidf.univmobile.fragments;

import org.unpidf.univmobile.R;
import org.unpidf.univmobile.dao.Poi;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class DetailsUniversityFragment extends BaseMapsFragment{

	private static final int MAX_TRY = 5;
	private Poi poi;
	private int count;
	
	public static DetailsUniversityFragment newInstance(Poi poi, String title) {
		Bundle bundle = new Bundle();
		bundle.putString("title", title);
		bundle.putSerializable("poi", poi);
		DetailsUniversityFragment frag = new DetailsUniversityFragment();
		frag.setArguments(bundle);
		return frag;
	}

	public void onCreate(Bundle savedInstanceState) {
		idConteneur = R.id.map_container_bis;
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		poi = (Poi) getArguments().getSerializable("poi");
		int count = 0;
		return inflater.inflate(R.layout.frag_details_university, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		((TextView)getView().findViewById(R.id.textId)).setText(poi.getTitle());
		((TextView)getView().findViewById(R.id.adresseId)).setText(poi.getAdress());
		((TextView)getView().findViewById(R.id.phoneId)).setText(poi.getPhone());
		waitForMap();
	}

	private void waitForMap() {
		if(count < MAX_TRY){
			setUpMapIfNeeded();
			if(map == null){
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						waitForMap();
						count++;
					}
				}, 500);
			}
		}
	}

	@Override
	protected void setUpMap() {
		map.getUiSettings().setZoomControlsEnabled(false);
		map.addMarker(new MarkerOptions().position(new LatLng(poi.getLatitude(), poi.getLongitude())).title(poi.getTitle()).snippet(poi.getId()));
		map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(poi.getLatitude(), poi.getLongitude()), MAP_ZOOM));
	}

}
