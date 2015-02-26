package org.unpidf.univmobile.ui.fragments;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.VisibleRegion;
import com.google.zxing.integration.android.IntentIntegrator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.unpidf.univmobile.R;
import org.unpidf.univmobile.data.entities.Category;
import org.unpidf.univmobile.data.entities.Comment;
import org.unpidf.univmobile.data.entities.ErrorEntity;
import org.unpidf.univmobile.data.entities.ImageMap;
import org.unpidf.univmobile.data.entities.Poi;
import org.unpidf.univmobile.data.entities.RestoMenu;
import org.unpidf.univmobile.data.models.GeoDataModel;
import org.unpidf.univmobile.data.models.UniversitiesDataModel;
import org.unpidf.univmobile.data.operations.ReadCategoriesOperation;
import org.unpidf.univmobile.ui.SimpleDialogFragment;
import org.unpidf.univmobile.ui.activities.HomeActivity;
import org.unpidf.univmobile.ui.views.AddNewPoiView;
import org.unpidf.univmobile.ui.views.GeoCampusCategoriesView;
import org.unpidf.univmobile.ui.views.PoiDetailsView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class GeoCampusFragment extends AbsFragment {

	private static final String ARG_TAB_ID = "tab_id";
	private static final String ARG_IMAGE_MAP_ID = "arg_image_map_id";
	private static final String ARG_POI_ID = "arg_poi_id";
	private static final String ARG_CATEGORY_ID = "arg_category_id";
	private MapView mMapView;
	private GoogleMap mMap;

	private final LatLngBounds BOUNDS = new LatLngBounds(new LatLng(-0.0042, -0.0042), new LatLng(0.0042, 0.0042));

	private RelativeLayout.LayoutParams mMapParams;
	private boolean mStopHandler = true;
	private OverscrollHandler mOverscrollHandler = new OverscrollHandler();

	private GroundOverlay mImageOverlay;

	private Map<Marker, Poi> mMarkers;

	private Location mLocation;

	private int mTabPosition;
	private int mImageMapId = -1;
	private int mPoiID = -1;
	private int mCategoryID = -1;

	private GeoDataModel mModel;

	private DisplayImageOptions mOptions;

	private List<String> mBitmapsDownloadingUrls = new ArrayList<String>();
	private Map<String, BitmapDescriptor> mLoadedBitmaps = new HashMap<String, BitmapDescriptor>();
	private Map<String, List<Poi>> mPoisToBoLoaded = new HashMap<String, List<Poi>>();

	private final Object lock = new Object();

	private boolean mAnimatedToMyPosition = false;

	public static GeoCampusFragment newInstance(int tabPosition, int imageMpaId, int poiId, int category) {

		GeoCampusFragment fragment = new GeoCampusFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_TAB_ID, tabPosition);
		args.putInt(ARG_IMAGE_MAP_ID, imageMpaId);
		args.putInt(ARG_POI_ID, poiId);
		args.putInt(ARG_CATEGORY_ID, category);
		fragment.setArguments(args);
		return fragment;
	}

	public GeoCampusFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			mTabPosition = getArguments().getInt(ARG_TAB_ID);
			mImageMapId = getArguments().getInt(ARG_IMAGE_MAP_ID);
			mPoiID = getArguments().getInt(ARG_POI_ID);
			mCategoryID = getArguments().getInt(ARG_CATEGORY_ID);
		}
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		if (mModel != null) {
			mModel.clear();
			mModel = null;
		}
		GeoCampusCategoriesView catView = (GeoCampusCategoriesView) getView().findViewById(R.id.categories);
		catView.clear();
		PoiDetailsView poiView = (PoiDetailsView) getView().findViewById(R.id.poi_details_container);
		poiView.clear();
		AddNewPoiView view = (AddNewPoiView) getView().findViewById(R.id.new_poi_view);
		view.clear();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_geo_campus, container, false);
		return v;
	}

	private void initMap() {

		// Gets the MapView from the XML layout and creates it
		mMapView = (MapView) getView().findViewById(R.id.mapview);
		mMapView.onCreate(null);

		mMapView.onResume();

		// Gets to GoogleMap from the MapView and does initialization stuff
		mMap = mMapView.getMap();
		mMap.getUiSettings().setMyLocationButtonEnabled(false);
		mMap.setMyLocationEnabled(true);

		mMap.setOnMarkerClickListener(mOnMarkerClickListener);
		mMap.setOnMyLocationChangeListener(mOnMyLocationChangeListener);
		MapsInitializer.initialize(this.getActivity());

	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		mOptions = new DisplayImageOptions.Builder().cacheInMemory(false).cacheOnDisk(true).imageScaleType(ImageScaleType.EXACTLY_STRETCHED).build();
		mModel = new GeoDataModel(getActivity(), mDataModelListener);



		GeoCampusCategoriesView catView = (GeoCampusCategoriesView) getView().findViewById(R.id.categories);
		catView.setCategoriesInterface(mCategoriesInterface);

		if (mImageMapId != -1) {
			//mModel.getImageMap(mImageMapId);
			mModel.setImageMapToBeShownId(mImageMapId);
		}
		if (mPoiID != -1) {
			//mModel.getPoi(mPoiID);
			mModel.setPoiToBeShownId(mPoiID);
		}

		mModel.getCategories();


	}


	public void refreshPois(List<Category> selectedCategories, int root) {

		if (mMarkers == null) {
			mMarkers = new HashMap<Marker, Poi>();
		} else {
			Iterator it = mMarkers.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry pairs = (Map.Entry) it.next();
				Marker m = (Marker) pairs.getKey();
				m.remove();
			}
			mMarkers.clear();
		}

		mModel.getPois(selectedCategories, root);
	}


	private GoogleMap.OnMarkerClickListener mOnMarkerClickListener = new GoogleMap.OnMarkerClickListener() {
		@Override
		public boolean onMarkerClick(Marker marker) {

			Poi poi = mMarkers.get(marker);
			showPoiDetails(poi);
			return true;
		}
	};

	public void showImageMap(int id) {
		if (id != -1) {
			mImageMapId = id;
			mModel.getImageMap(mImageMapId);
		}
	}

	public void showPoi(int id) {
		mModel.getPoi(id);
	}

	public void showPoiDetails(Poi poi) {
		PoiDetailsView view = (PoiDetailsView) getView().findViewById(R.id.poi_details_container);
		GeoCampusCategoriesView catView = (GeoCampusCategoriesView) getView().findViewById(R.id.categories);

		view.populate(mPoiDetailsInterface, poi, mModel.getCategoryById(poi.getCategoryId()), catView.getSelectedTab());
		mModel.getRestoMenu(poi);
		mModel.getComments(poi, false);

	}

	private void addNewPoi() {
		AddNewPoiView view = (AddNewPoiView) getView().findViewById(R.id.new_poi_view);
		view.show(mAddNewPoiViewInterface);
	}

	public void categorySelected(Category cat) {
		AddNewPoiView view = (AddNewPoiView) getView().findViewById(R.id.new_poi_view);
		view.categorySelected(cat);
	}

	private void addMarkersWithUri(String uri) {

		Log.d("test", "adding markers by uri: " + uri);
		List<Poi> pois = mPoisToBoLoaded.get(uri);
		if (pois != null) {
			for (Poi p : pois) {
				addMarker(p, mLoadedBitmaps.get(uri));
			}
		}
		mPoisToBoLoaded.remove(uri);
	}

	private void addMarker(Poi p, BitmapDescriptor des) {
		MarkerOptions options = new MarkerOptions();
		options.icon(des);
		options.anchor(0.5f, 0.5f);
		options.position(new LatLng(Double.parseDouble(p.getLat()), Double.parseDouble(p.getLng())));
		options.title(p.getName());

		Marker m = mMap.addMarker(options);
		mMarkers.put(m, p);
	}


	private AddNewPoiView.AddNewPoiViewInterface mAddNewPoiViewInterface = new AddNewPoiView.AddNewPoiViewInterface() {
		@Override
		public void selectCategory() {
			HomeActivity a = (HomeActivity) getActivity();
			a.showFragment(SelectCategoryFragment.newInstance(mModel.ROOT_CAT_3), PoisSearchFragment.class.getName(), true);
		}

		@Override
		public void notAllFieldFilled() {
			FragmentTransaction ft = getFragmentManager().beginTransaction();
			Fragment prev = getFragmentManager().findFragmentByTag("dialog");
			if (prev != null) {
				ft.remove(prev);
			}
			ft.addToBackStack(null);

			// Create and show the dialog.
			SimpleDialogFragment newFragment = SimpleDialogFragment.newInstance(getString(R.string.missing_info));
			newFragment.show(ft, "dialog");
		}

		@Override
		public void postPoi(Category cat, String name, String address, String phone, String mail, String description) {
			mModel.postPoi(cat, name, address, phone, mail, description);
		}
	};

	private GeoCampusCategoriesView.CategoriesInterface mCategoriesInterface = new GeoCampusCategoriesView.CategoriesInterface() {
		@Override
		public void onTabChanged(int tabID) {
			ImageView icon = (ImageView) getView().findViewById(R.id.new_poi);
			if (tabID == 0) {
				icon.setImageResource(R.drawable.ic_qr);
				icon.setOnClickListener(mScanQrListener);
				icon.setVisibility(View.VISIBLE);
			} else if (tabID == 1) {
				icon.setVisibility(View.GONE);
			} else if (tabID == 2) {
				icon.setVisibility(View.VISIBLE);
				icon.setImageResource(R.drawable.ic_add);
				icon.setOnClickListener(mAddPoiListener);
			}
		}

		@Override
		public void onCategoriesChanged(List<Category> selectedCategories) {
			refreshPois(selectedCategories, -1);
		}

		@Override
		public void onCategoriesChanged(int root) {
			refreshPois(null, root);
		}

		@Override
		public void onSearchClicked() {
			HomeActivity a = (HomeActivity) getActivity();
			a.showFragment(PoisSearchFragment.newInstance(), PoisSearchFragment.class.getName(), true);
		}

	};


	private View.OnClickListener mScanQrListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			IntentIntegrator i = new IntentIntegrator(getActivity());
			i.initiateScan();
		}
	};

	private View.OnClickListener mAddPoiListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			addNewPoi();
		}
	};


	private PoiDetailsView.PoiDetailsInterface mPoiDetailsInterface = new PoiDetailsView.PoiDetailsInterface() {
		@Override
		public void postComment(String comment, Poi poi) {
			mModel.postComment(comment, poi);
		}

		@Override
		public void postBookmark(Poi poi) {
			mModel.postBookmark(poi);
		}

	};

	private GeoDataModel.GeoModelListener mDataModelListener = new GeoDataModel.GeoModelListener() {
		@Override
		public void showErrorMessage(ErrorEntity error) {

		}

		@Override
		public void populateCategories() {
			int root = 1;
			switch (mTabPosition) {
				case 0:
					root = mModel.ROOT_CAT_1;
					break;
				case 1:
					root = mModel.ROOT_CAT_2;
					break;
				case 2:
					root = mModel.ROOT_CAT_3;
					break;
			}
			GeoCampusCategoriesView v = (GeoCampusCategoriesView) getView().findViewById(R.id.categories);
			if (!UniversitiesDataModel.getSavedUniversity(getActivity()).getRegionName().equals(UniversitiesDataModel.FRANCE_REGION)) {
				v.setTabsCount(1);
			}

			v.populate(mModel.getCategories1(), mModel.getCategories2(), mModel.getCategories3(), mModel.ROOT_CAT_1, mModel.ROOT_CAT_2, mModel.ROOT_CAT_3, mTabPosition);

			mCategoriesInterface.onTabChanged(mTabPosition);
			initMap();

			if (mCategoryID != -1) {
				Category c = new Category();
				c.setId(mCategoryID);

				List<Category> categories = new ArrayList<Category>();
				categories.add(c);
				refreshPois(categories, -1);
				//v.setSelectedCategories(categories, mTabPosition);
			} else {
				refreshPois(null, root);
			}

		}

		@Override
		public void populatePois(List<Poi> pois) {
			if (mMap != null && pois != null) {
				for (Poi p : pois) {
					if (p.getLat() != null && p.getLat().length() > 0 && p.getLng() != null && p.getLng().length() > 0) {

						String url = ReadCategoriesOperation.CATEGORIES_IMAGE_URL;
						Category cat = mModel.getCategoryById(p.getCategoryId());
						if (cat != null && cat.getActiveIconUrl() != null && cat.getActiveIconUrl().length() > 0) {
							url += cat.getActiveIconUrl();
						} else {
							url += "cat_active_5__marker-cross.png";
						}


						synchronized (lock) {
							if (!mBitmapsDownloadingUrls.contains(url)) {
								mBitmapsDownloadingUrls.add(url);
								List<Poi> poi = mPoisToBoLoaded.get(url);
								if (poi == null) {
									poi = new ArrayList<Poi>();
								}
								poi.add(p);
								ImageLoader.getInstance().loadImage(url, new ImageSize(60, 60, 0), mOptions, new ImageLoadingListener() {
									@Override
									public void onLoadingStarted(String imageUri, View view) {

									}

									@Override
									public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

									}

									@Override
									public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
										synchronized (lock) {
											mLoadedBitmaps.put(imageUri, BitmapDescriptorFactory.fromBitmap(loadedImage));
											addMarkersWithUri(imageUri);
										}
									}

									@Override
									public void onLoadingCancelled(String imageUri, View view) {

									}
								});
							} else {
								if (mLoadedBitmaps.get(url) != null) {
									addMarker(p, mLoadedBitmaps.get(url));
								} else {
									List<Poi> poi = mPoisToBoLoaded.get(url);
									if (poi == null) {
										poi = new ArrayList<Poi>();
									}
									poi.add(p);
								}
							}
						}
					}

				}
			}
		}

		@Override
		public void populateComments(List<Comment> comments) {

			PoiDetailsView view = (PoiDetailsView) getView().findViewById(R.id.poi_details_container);

			view.populateComments(comments);

		}

		@Override
		public void populateCurrentMenu(List<RestoMenu> menus) {
			PoiDetailsView view = (PoiDetailsView) getView().findViewById(R.id.poi_details_container);

			view.populateRestoMenu(menus);
		}

		@Override
		public void showImageMapPois(ImageMap map) {

			if (mMarkers != null) {
				Iterator it = mMarkers.entrySet().iterator();
				while (it.hasNext()) {
					Map.Entry pairs = (Map.Entry) it.next();
					Marker m = (Marker) pairs.getKey();
					m.remove();
				}
				mMarkers.clear();
			}

			mDataModelListener.populatePois(map.getPois());

			GroundOverlayOptions newarkMap = new GroundOverlayOptions().image(BitmapDescriptorFactory.fromBitmap(map.getImage())).anchor(0.5f, 0.5f).position(new LatLng(0, 0), 1000, 1000);
			mImageOverlay = mMap.addGroundOverlay(newarkMap);

			mMap.setMapType(GoogleMap.MAP_TYPE_NONE);
			mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(0, 0), 17));

			mStopHandler = false;
			mOverscrollHandler.sendEmptyMessageDelayed(0, 100);

			disableLayout();
		}

		@Override
		public void showPoi(Poi poi) {
			showPoiDetails(poi);
		}

		@Override
		public void commentPosted(Poi poi) {
			PoiDetailsView view = (PoiDetailsView) getView().findViewById(R.id.poi_details_container);
			view.loadFinished();
			mModel.getComments(poi, true);
		}

		@Override
		public void poiPosted() {

			GeoCampusCategoriesView categories = (GeoCampusCategoriesView) getView().findViewById(R.id.categories);
			categories.notifyUpdatedCategories();

			AddNewPoiView view = (AddNewPoiView) getView().findViewById(R.id.new_poi_view);
			view.hide();
		}

		@Override
		public void bookmarkPosted() {
			PoiDetailsView view = (PoiDetailsView) getView().findViewById(R.id.poi_details_container);
			view.loadFinished();
		}

		@Override
		public void showAuthorizationError() {
			// DialogFragment.show() will take care of adding the fragment
			// in a transaction.  We also want to remove any currently showing
			// dialog, so make our own transaction and take care of that here.
			FragmentTransaction ft = getFragmentManager().beginTransaction();
			Fragment prev = getFragmentManager().findFragmentByTag("dialog");
			if (prev != null) {
				ft.remove(prev);
			}
			ft.addToBackStack(null);

			// Create and show the dialog.
			SimpleDialogFragment newFragment = SimpleDialogFragment.newInstance(getString(R.string.need_to_login));
			newFragment.show(ft, "dialog");

			PoiDetailsView poiView = (PoiDetailsView) getView().findViewById(R.id.poi_details_container);
			poiView.loadFinished();

			AddNewPoiView newPoiView = (AddNewPoiView) getView().findViewById(R.id.new_poi_view);
			newPoiView.loadFinished();
		}


	};


	@Override
	public void onPause() {
		if (mMapView != null) {
			mMapView.onPause();
		}
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
		if (mMapView != null) {
			mMapView.onResume();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mMapView != null) {
			mMapView.onDestroy();
		}
		if (mMap != null) {
			mMap.setOnMyLocationChangeListener(null);
			mMap.setMyLocationEnabled(false);
		}
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		if (mMapView != null) {
			mMapView.onLowMemory();
		}
	}

	@Override
	public boolean onBackPressed() {
		PoiDetailsView view = (PoiDetailsView) getView().findViewById(R.id.poi_details_container);
		if (view.getVisibility() == View.VISIBLE) {
			view.hide();
			return true;
		}
		GeoCampusCategoriesView v = (GeoCampusCategoriesView) getView().findViewById(R.id.categories);
		if (v.isExpanded()) {
			v.collapseAndCancel();
			return true;
		}
		if (mImageOverlay != null) {
			enableLayout();
			return true;
		}
		return super.onBackPressed();
	}


	private void disableLayout() {
		getView().findViewById(R.id.new_poi).setVisibility(View.GONE);
		getView().findViewById(R.id.categories).setVisibility(View.GONE);
		mMapParams = (RelativeLayout.LayoutParams) getView().findViewById(R.id.mapview).getLayoutParams();
		getView().findViewById(R.id.mapview).setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

	}

	private void enableLayout() {

		mImageOverlay.remove();
		mImageOverlay = null;

		mStopHandler = true;
		mOverscrollHandler = null;

		if (mLocation != null) {
			mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLocation.getLatitude(), mLocation.getLongitude()), 16));
		} else {
			mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(48.846891, 2.344414), 14));
		}

		mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		getView().findViewById(R.id.categories).setVisibility(View.VISIBLE);
		getView().findViewById(R.id.new_poi).setVisibility(View.VISIBLE);

		if (mMapParams != null) {
			getView().findViewById(R.id.mapview).setLayoutParams(mMapParams);
			mMapParams = null;
		}
		//refreshPois(((GeoCampusCategoriesView) getView().findViewById(R.id.categories)).getSelectedCategories());
	}


	private GoogleMap.OnMyLocationChangeListener mOnMyLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
		@Override
		public void onMyLocationChange(Location location) {
			if (!mAnimatedToMyPosition) {
				mLocation = location;
				CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 16);
				mMap.animateCamera(cameraUpdate);
				mAnimatedToMyPosition = true;
				mMap.setOnMyLocationChangeListener(null);
			}
		}
	};

	private LatLng getLatLngCorrection(LatLngBounds cameraBounds) {
		double latitude = 0, longitude = 0;
		if (cameraBounds.southwest.latitude < BOUNDS.southwest.latitude) {
			latitude = BOUNDS.southwest.latitude - cameraBounds.southwest.latitude;
		}
		if (cameraBounds.southwest.longitude < BOUNDS.southwest.longitude) {
			longitude = BOUNDS.southwest.longitude - cameraBounds.southwest.longitude;
		}
		if (cameraBounds.northeast.latitude > BOUNDS.northeast.latitude) {
			latitude = BOUNDS.northeast.latitude - cameraBounds.northeast.latitude;
		}
		if (cameraBounds.northeast.longitude > BOUNDS.northeast.longitude) {
			longitude = BOUNDS.northeast.longitude - cameraBounds.northeast.longitude;
		}
		return new LatLng(latitude, longitude);
	}


	private class OverscrollHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			if (!mStopHandler) {
				CameraPosition position = mMap.getCameraPosition();
				VisibleRegion region = mMap.getProjection().getVisibleRegion();
				float zoom = 17;
				LatLng correction = getLatLngCorrection(region.latLngBounds);
				if (zoom != 0 || correction.latitude != 0 || correction.longitude != 0) {
					zoom = (zoom == 0) ? position.zoom : zoom;
					double lat = position.target.latitude + correction.latitude;
					double lon = position.target.longitude + correction.longitude;
					CameraPosition newPosition = new CameraPosition(new LatLng(lat, lon), zoom, position.tilt, position.bearing);
					CameraUpdate update = CameraUpdateFactory.newCameraPosition(newPosition);
					mMap.moveCamera(update);
				}
		/* Recursively call handler every 100ms */
				sendEmptyMessageDelayed(0, 100);
			}
		}
	}

}


