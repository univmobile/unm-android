package org.unpidf.univmobile.fragments;

import java.util.ArrayList;
import java.util.List;

import org.unpidf.univmobile.R;
import org.unpidf.univmobile.adapter.PagerAdapter;
import org.unpidf.univmobile.dao.Poi;
import org.unpidf.univmobile.dao.PoiGroup;
import org.unpidf.univmobile.manager.DataManager;
import org.unpidf.univmobile.manager.LocManager;
import org.unpidf.univmobile.manager.LocManager.LocListener;
import org.unpidf.univmobile.utils.Utils;
import org.unpidf.univmobile.view.GeocampusActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Locate all universities on map. Fragment for: {@link GeocampusActivity}
 *
 * @author Michel
 */
public class MapsPoiFragment extends BaseMapsFragment {

	private List<Poi> listPois;
	private ViewPager pager;
	private List<Marker> listMarkers;
	private boolean alreadyMoved;

	public static MapsPoiFragment newInstance(String title) {
		final Bundle bundle = new Bundle();
		bundle.putString("title", title);
		final MapsPoiFragment fragment = new MapsPoiFragment();
		fragment.setArguments(bundle);
		return fragment;
	}

	private BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (getView() == null || getActivity() == null || map == null) {
				return;
			}
			if (intent.getAction().equals(DataManager.NOTIF_POIS_OK)) {
				if(DataManager.getInstance(getActivity()).getMapInfo() != null){
					MAP_ZOOM = DataManager.getInstance(getActivity()).getMapInfo().getZoom();
				}
				listPois = new ArrayList<Poi>();
				final Iterable<PoiGroup> listGroupPois = DataManager.getInstance(getActivity()).getListPois();
				for (final PoiGroup poiGroup : listGroupPois) {
					listPois.addAll(poiGroup.getListPois());
				}
				initPager();
				addMarkers();
			}
		}
	};

	private OnPageChangeListener onPageChange = new OnPageChangeListener() {
		@Override
		public void onPageSelected(int index) {
			map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(listPois.get(index)
					.getLatitude(), listPois.get(index).getLongitude()
					), MAP_ZOOM));
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	};
	
	private LocListener locListener = new LocListener() {
		@Override
		public void onLocationNotChanged(String address) {
			Location loc = LocManager.getLocation();
			if(map != null){
				map.setMyLocationEnabled(loc.getLatitude() != 0 && loc.getLongitude() != 0);
			}
		}
		
		@Override
		public void onLocationChanged(Location loc) {
			if(map != null){
				map.setMyLocationEnabled(loc.getLatitude() != 0 && loc.getLongitude() != 0);
			}
		}
		
		@Override
		public void onAddressChanged(String address, boolean success) {
			
		}
	};

	public void onCreate(Bundle savedInstanceState) {
		idConteneur = R.id.map_container;
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		IntentFilter filter = new IntentFilter(DataManager.NOTIF_POIS_OK);
		filter.addAction(DataManager.NOTIF_POIS_ERR);
		LocalBroadcastManager.getInstance(getActivity()).registerReceiver(receiver, filter);
		return inflater.inflate(R.layout.frag_maps, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		DataManager.getInstance(getActivity()).launchPoisGetting(LocManager.getLocation());
	}
	
	@Override
	public void onResume() {
		super.onResume();
		LocManager.getInstance(getActivity()).addLocationListener(locListener);
		LocManager.getInstance(getActivity()).requestUpdate();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		LocManager.getInstance(getActivity()).removeLocationListener(locListener);
	}

	/**
	 * Init ViewPager used to display UniversityOverview
	 */
	protected void initPager() {
		pager = (ViewPager) getView().findViewById(R.id.pagerUniversity);
		List<Fragment> listFrag = new ArrayList<Fragment>();
		for (final Poi poi : listPois) {
			listFrag.add(UniversityOverviewFragment.newInstance(poi));
		}
		PagerAdapter adapter = new PagerAdapter(getChildFragmentManager(), listFrag);
		pager.setAdapter(adapter);
		pager.setOnPageChangeListener(onPageChange);
	}

	/**
	 * Show marker: go to right item in ViewPager
	 *
	 * @param marker
	 */
	private void showInfo(Marker marker) {
		getView().findViewById(R.id.pagerUniversity).setVisibility(View.VISIBLE);
		int pos = findPosWithId(marker.getSnippet());
		if (pos >= 0) {
			pager.setCurrentItem(pos, true);
		} else {
			hideInfo();
		}
	}

	private int findPosWithId(String id) {
		for (int i = 0; i < listPois.size(); i++) {
			if (listPois.get(i).getId().equals(id)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Hide ViewPager
	 */
	private void hideInfo() {
		getView().findViewById(R.id.pagerUniversity).setVisibility(View.GONE);
	}

	@Override
	protected void setUpMap() {
		int fiveDIp = Utils.convertDpToPixel(5, getResources());
		int paddingBottom = Utils.convertDpToPixel(105, getResources());

		map.setPadding(fiveDIp, fiveDIp, fiveDIp, paddingBottom);
		map.setInfoWindowAdapter(new InfoWindowAdapter() {
			private final TextView tv = new TextView(getActivity());

			{
				tv.setTextColor(Color.BLACK);
			}

			private final LinearLayout linearVide = new LinearLayout(getActivity());

			@Override
			public View getInfoWindow(Marker marker) {
				return linearVide;
			}

			@Override
			public View getInfoContents(Marker marker) {
				return null;
			}
		});
		map.setOnMapClickListener(new OnMapClickListener() {
			@Override
			public void onMapClick(LatLng arg0) {
				hideInfo();
			}
		});

		map.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
			@Override
			public void onInfoWindowClick(Marker marker) {
			}
		});
		map.setOnMarkerClickListener(new OnMarkerClickListener() {
			@Override
			public boolean onMarkerClick(final Marker marker) {
				selectMarker(marker);
				return true;
			}
		});
	}

	/**
	 * Add markers related to Pois on map
	 */
	private synchronized void addMarkers() {
		if (map == null) {
			return;
		}
		map.clear();
		// listMarkerOptions = new ArrayList<MarkerOptions>();
		listMarkers = new ArrayList<Marker>();
		for (final Poi poi : listPois) {
			final MarkerOptions markerOptions = new MarkerOptions() //
			.position(new LatLng(poi.getLatitude(), poi.getLongitude())) //
			.title(poi.getTitle()).snippet(poi.getId());
			// listMarkerOptions.add(markerOptions);
			final Marker marker = map.addMarker(markerOptions);
			listMarkers.add(marker);
		}

		selectFirstMarker();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		if (getActivity() != null) {
			try {
				LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(receiver);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * move the map to a given marker.
	 */
	private void selectMarker(final Marker marker) {
		showInfo(marker);
		Location loc = LocManager.getLocation();
		if(loc != null && loc.getLatitude() != 0 && loc.getLongitude() != 0 && !alreadyMoved){
			alreadyMoved = true;
			map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng( //
					loc.getLatitude(), //
					loc.getLongitude()), MAP_ZOOM));
		}else{
			map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng( //
					marker.getPosition().latitude, //
					marker.getPosition().longitude), MAP_ZOOM));
		}
	}

	/**
	 * move the map to the first marker, if any.
	 */
	private void selectFirstMarker() {
		if (listMarkers != null && !listMarkers.isEmpty()) {
			// Select the first element
			selectMarker(listMarkers.iterator().next());
		}
	}

	public boolean isMapsCentered(Poi poi) {
		if(map != null){
			double latCam = Utils.roundParam(map.getCameraPosition().target.latitude, 5);
			double lngCam = Utils.roundParam(map.getCameraPosition().target.longitude, 5);
			double latPoi = Utils.roundParam(poi.getLatitude(), 5);
			double lngPoi = Utils.roundParam(poi.getLongitude(), 5);
			if(latCam == latPoi && lngCam == lngPoi){
				return true;
			}
		}
		return false;
	}
	
	public void centerMap(Poi poi) {
		if(map != null){
			map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng( //
					poi.getLatitude(), 
					poi.getLongitude()), MAP_ZOOM));
		}
	}

}
