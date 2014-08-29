package org.unpidf.univmobile.fragments;

import org.unpidf.univmobile.R;
import org.unpidf.univmobile.dao.Poi;
import org.unpidf.univmobile.view.UniversityActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
/**
 * See all details of a specific university. Fragment for: {@link UniversityActivity}
 * @author Michel
 *
 */
public class UniversityDetailsFragment extends BaseMapsFragment{

	private static final int MAX_TRY = 10;
	private Poi poi;
	private int count;
	
	public static UniversityDetailsFragment newInstance(Poi poi, String title) {
		Bundle bundle = new Bundle();
		bundle.putString("title", title);
		bundle.putSerializable("poi", poi);
		UniversityDetailsFragment frag = new UniversityDetailsFragment();
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
		return inflater.inflate(R.layout.frag_details_university, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		((TextView)getView().findViewById(R.id.titleId)).setText(poi.getTitle());
		if(poi.getAdress() != null){
			((TextView)getView().findViewById(R.id.adressId)).setText(poi.getAdress());
		}else{
			getView().findViewById(R.id.adressId).setVisibility(View.GONE);
			getView().findViewById(R.id.adressLabel).setVisibility(View.GONE);
			getView().findViewById(R.id.adressSep).setVisibility(View.GONE);
		}
		if(poi.getPhone() != null){
			((TextView)getView().findViewById(R.id.phoneId)).setText(poi.getPhone());
		}else{
			getView().findViewById(R.id.phoneId).setVisibility(View.GONE);
			getView().findViewById(R.id.phoneLabel).setVisibility(View.GONE);
			getView().findViewById(R.id.phoneSep).setVisibility(View.GONE);
		}
		if(poi.getWebUrl() != null){
			((TextView)getView().findViewById(R.id.webUrlId)).setText(poi.getWebUrl());
		}else{
			getView().findViewById(R.id.webUrlId).setVisibility(View.GONE);
			getView().findViewById(R.id.webUrlLabel).setVisibility(View.GONE);
			getView().findViewById(R.id.webUrlSep).setVisibility(View.GONE);
		}
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
				}, 200);
			}
		}
	}

	@Override
	protected void setUpMap() {
		map.getUiSettings().setZoomControlsEnabled(false);
		map.addMarker(new MarkerOptions().position(new LatLng(poi.getLatitude(), poi.getLongitude())).title(poi.getTitle()).snippet(poi.getId()));
		map.animateCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.builder().target(new LatLng(poi.getLatitude(), poi.getLongitude())).zoom(MAP_ZOOM).build()));
	}

}
