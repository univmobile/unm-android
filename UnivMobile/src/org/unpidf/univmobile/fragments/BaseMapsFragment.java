/*
 * Copyright (C) 2013 Maciej Górski
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.unpidf.univmobile.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
/**
 * MasterFragment to create quickly a maps using GMS
 *
 */
public abstract class BaseMapsFragment extends Fragment {

	protected int idConteneur;
	protected LatLng pos;
	public static int MAP_ZOOM = 16;
	protected SupportMapFragment mapFragment;
	protected GoogleMap map;

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		createMapFragmentIfNeeded();
	}

	@Override
	public void onResume() {
		super.onResume();
		setUpMapIfNeeded();
	}

	private void createMapFragmentIfNeeded() {
		FragmentManager fm = getChildFragmentManager();
		mapFragment = (SupportMapFragment) fm.findFragmentById(idConteneur);
		if (mapFragment == null) {
			mapFragment = createMapFragment();
			FragmentTransaction tx = fm.beginTransaction();
			tx.add(idConteneur, mapFragment);
			tx.commit();
		}
	}

	protected SupportMapFragment createMapFragment() {
		GoogleMapOptions options = new GoogleMapOptions();
		if(pos != null){
			options.camera(CameraPosition.fromLatLngZoom(pos, MAP_ZOOM));
		}else{
			options.camera(CameraPosition.fromLatLngZoom(new LatLng(48.84650925911,2.3459243774), MAP_ZOOM - 7));
		}
		return SupportMapFragment.newInstance(options);
	}

	protected void setUpMapIfNeeded() {
		if (map == null) {
			map = mapFragment.getMap();
			if (map != null) {
				setUpMap();
			}
		}
	}

	protected abstract void setUpMap();
}
