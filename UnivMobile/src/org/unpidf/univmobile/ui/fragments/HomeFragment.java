package org.unpidf.univmobile.ui.fragments;


import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.unpidf.univmobile.R;
import org.unpidf.univmobile.UnivMobileApp;
import org.unpidf.univmobile.data.entities.Category;
import org.unpidf.univmobile.data.entities.ErrorEntity;
import org.unpidf.univmobile.data.entities.News;
import org.unpidf.univmobile.data.entities.Poi;
import org.unpidf.univmobile.data.models.UniversitiesDataModel;
import org.unpidf.univmobile.data.models.UniversityDataModel;
import org.unpidf.univmobile.data.operations.ReadCategoriesOperation;
import org.unpidf.univmobile.ui.activities.HomeActivity;
import org.unpidf.univmobile.ui.uiutils.FontHelper;
import org.unpidf.univmobile.ui.views.NewsItemView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends AbsFragment {


	private UniversityDataModel mUniversityDataModel;

	private SimpleDateFormat mDateFormat;
	private DisplayImageOptions mOptions;

	private MapView mMapView;
	private GoogleMap mMap;
	private boolean mAnimatedToMyPosition = false;


	private List<String> mBitmapsDownloadingUrls = new ArrayList<String>();
	private Map<String, BitmapDescriptor> mLoadedBitmaps = new HashMap<String, BitmapDescriptor>();
	private Map<String, List<Poi>> mPoisToBoLoaded = new HashMap<String, List<Poi>>();

	private final Object lock = new Object();

	public static HomeFragment newInstance() {
		HomeFragment fragment = new HomeFragment();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	}

	public HomeFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
		}
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		if (mUniversityDataModel != null) {
			mUniversityDataModel.clear();
			mUniversityDataModel = null;
		}
	}

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
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		if (mMapView != null) {
			mMapView.onLowMemory();
		}
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_home, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		mOptions = new DisplayImageOptions.Builder().cacheInMemory(false).cacheOnDisk(true).imageScaleType(ImageScaleType.EXACTLY_STRETCHED).build();
		mDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
		//init fonts
		FontHelper helper = ((UnivMobileApp) getActivity().getApplicationContext()).getFontHelper();
		helper.loadFont((android.widget.TextView) view.findViewById(R.id.main_article_time), FontHelper.FONT.EXO_ITALIC);
		helper.loadFont((android.widget.TextView) view.findViewById(R.id.main_article_title), FontHelper.FONT.EXO_BOLD);
		helper.loadFont((android.widget.TextView) view.findViewById(R.id.main_article_content), FontHelper.FONT.EXO_REGULAR);
		helper.loadFont((android.widget.TextView) view.findViewById(R.id.latest_news_title), FontHelper.FONT.EXO_BOLD);
		helper.loadFont((android.widget.TextView) view.findViewById(R.id.view_all_news), FontHelper.FONT.EXO_BOLD);

		view.findViewById(R.id.view_all_news).setOnClickListener(mViewAllNewsClickListener);


		mUniversityDataModel = new UniversityDataModel(getActivity(), mUniversityModelListener);
		mUniversityDataModel.getNews();
		mUniversityDataModel.getCategories();

		initMap();

	}

	private void populatePois(List<Poi> pois) {
		if (mMap != null && pois != null) {
			for (Poi p : pois) {
				if (p.getLat() != null && p.getLat().length() > 0 && p.getLng() != null && p.getLng().length() > 0) {

					String url = ReadCategoriesOperation.CATEGORIES_IMAGE_URL;
					Category cat = mUniversityDataModel.getCategoryById(p.getCategoryId());
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
	}

	private void initMap() {

		// Gets the MapView from the XML layout and creates it
		mMapView = (MapView) getView().findViewById(R.id.mapview);
		mMapView.onCreate(null);

		mMapView.onResume();

		// Gets to GoogleMap from the MapView and does initialization stuff
		if (mMapView != null) {
			mMap = mMapView.getMap();
			mMap.getUiSettings().setMyLocationButtonEnabled(false);
			mMap.setMyLocationEnabled(true);
			mMap.getUiSettings().setAllGesturesEnabled(false);
			mMap.setOnMarkerClickListener(null);
			mMap.setOnMyLocationChangeListener(mOnMyLocationChangeListener);
		}

		MapsInitializer.initialize(this.getActivity());


		if (!UniversitiesDataModel.getSavedUniversity(getActivity()).getRegionName().equals(UniversitiesDataModel.FRANCE_REGION)) {
			getView().findViewById(R.id.cat_2).setVisibility(View.GONE);
			getView().findViewById(R.id.cat_3).setVisibility(View.GONE);
		}
		getView().findViewById(R.id.cat_1).setOnClickListener(mOnCategoryClickListener);
		getView().findViewById(R.id.cat_2).setOnClickListener(mOnCategoryClickListener);
		getView().findViewById(R.id.cat_3).setOnClickListener(mOnCategoryClickListener);
	}

	private void populateNews(List<News> news) {
		if (news == null || news.size() == 0) {
			getView().findViewById(R.id.news_container).setVisibility(View.GONE);
		} else {
			ImageView mainArticleImage = (ImageView) getView().findViewById(R.id.main_article_image);
			mainArticleImage.setImageResource(R.drawable.ic_launcher);
			if (news.get(0).getImageUlr() != null) {
				ImageLoader.getInstance().displayImage(news.get(0).getImageUlr(), mainArticleImage, mOptions);
			}

			try {
				TextView dateView = (TextView) getView().findViewById(R.id.main_article_time);
				Date dateValue = mDateFormat.parse(news.get(0).getPublishedDate());

				SimpleDateFormat formatNew = new SimpleDateFormat("dd/MM/yyyy");
				String dateString = formatNew.format(dateValue);

				dateView.setText(dateString);

			} catch (Exception e) {
				e.printStackTrace();
			}

			TextView title = (TextView) getView().findViewById(R.id.main_article_title);
			title.setText(news.get(0).getTitle());

			TextView description = (TextView) getView().findViewById(R.id.main_article_content);
			title.setText(news.get(0).getTitle());

			NewsItemView news1 = (NewsItemView) getView().findViewById(R.id.news1);
			if (news.size() > 1) {
				news1.populate(news.get(1), mDateFormat, mOptions);
			} else {
				news1.setVisibility(View.GONE);
			}

			NewsItemView news2 = (NewsItemView) getView().findViewById(R.id.news2);
			if (news.size() > 2) {
				news2.populate(news.get(2), mDateFormat, mOptions);
			} else {
				news2.setVisibility(View.GONE);
			}

			NewsItemView news3 = (NewsItemView) getView().findViewById(R.id.news3);
			if (news.size() > 3) {
				news3.populate(news.get(3), mDateFormat, mOptions);
			} else {
				news3.setVisibility(View.GONE);
			}

			NewsItemView news4 = (NewsItemView) getView().findViewById(R.id.news4);
			if (news.size() > 4) {
				news4.populate(news.get(4), mDateFormat, mOptions);
			} else {
				news4.setVisibility(View.GONE);
			}

		}
	}

	private GoogleMap.OnMyLocationChangeListener mOnMyLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
		@Override
		public void onMyLocationChange(Location location) {
			if (!mAnimatedToMyPosition) {
				CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 16);
				mMap.moveCamera(cameraUpdate);
				mAnimatedToMyPosition = true;
				mMap.setOnMyLocationChangeListener(null);
			}
		}
	};

	private View.OnClickListener mOnCategoryClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			int pos = 0;
			switch (v.getId()) {
				case R.id.cat_1:
					pos = 0;
					break;
				case R.id.cat_2:
					pos = 1;
					break;
				case R.id.cat_3:
					pos = 2;
					break;
			}
			HomeActivity a = (HomeActivity) getActivity();
			a.showFragment(GeoCampusFragment.newInstance(pos, -1, -1, -1), GeoCampusFragment.class.getName(), false);
		}
	};
	private View.OnClickListener mViewAllNewsClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			HomeActivity a = (HomeActivity) getActivity();
			a.showFragment(new UniversityNewsFragment(), UniversityNewsFragment.class.getName(), true);
		}
	};

	private UniversityDataModel.UniversityModelListener mUniversityModelListener = new UniversityDataModel.UniversityModelListener() {


		@Override
		public void updateNews(List<News> news) {
			getView().findViewById(R.id.news_container).setVisibility(View.VISIBLE);
			populateNews(news);
		}

		@Override
		public void updatePois(List<Poi> pois) {
			getView().findViewById(R.id.progressBar1).setVisibility(View.GONE);
			populatePois(pois);

		}

		@Override
		public void showErrorMessage(ErrorEntity error) {
			getView().findViewById(R.id.progressBar1).setVisibility(View.GONE);
			getView().findViewById(R.id.news_container).setVisibility(View.VISIBLE);

		}
	};
}
