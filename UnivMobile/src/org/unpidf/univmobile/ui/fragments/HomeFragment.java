package org.unpidf.univmobile.ui.fragments;


import android.app.Fragment;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.TypedValue;
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
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.unpidf.univmobile.R;
import org.unpidf.univmobile.UnivMobileApp;
import org.unpidf.univmobile.data.entities.ErrorEntity;
import org.unpidf.univmobile.data.entities.News;
import org.unpidf.univmobile.data.entities.NewsFeed;
import org.unpidf.univmobile.data.entities.Poi;
import org.unpidf.univmobile.data.models.FeedsDataModel;
import org.unpidf.univmobile.data.models.UniversitiesDataModel;
import org.unpidf.univmobile.data.models.UniversityDataModel;
import org.unpidf.univmobile.data.operations.ReadCategoriesOperation;
import org.unpidf.univmobile.ui.activities.HomeActivity;
import org.unpidf.univmobile.ui.adapters.NewsAdapter;
import org.unpidf.univmobile.ui.uiutils.FontHelper;
import org.unpidf.univmobile.ui.views.NewsItemView;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends AbsFragment {


    private UniversityDataModel mUniversityDataModel;

    private SimpleDateFormat mDateFormat;

    private MapView mMapView;
    private GoogleMap mMap;
    private boolean mAnimatedToMyPosition = false;


    private SwipeRefreshLayout mSwipeRefreshLayout;
    private boolean mShowLoading = true;


    private final Object lock = new Object();
    private FeedsDataModel mFeedsDataModel;

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
        if (mFeedsDataModel != null) {
            mFeedsDataModel.clear();
            mFeedsDataModel = null;
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
            mMapView = null;
        }
        if (mMap != null) {
            mMap.setOnMyLocationChangeListener(null);
            mMap.setMyLocationEnabled(false);
            mMap = null;
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
        if(((HomeActivity)getActivity()).showNews) {
            if(FeedsDataModel.retrieveSavedFeeds(getActivity()).size()==0){
                if (mFeedsDataModel == null) {
                    mFeedsDataModel = new FeedsDataModel(getActivity());
                    mFeedsDataModel.setListener(mFeedsDataModelListener);
                }

                mFeedsDataModel.downloadFeeds();
            }
            else {
                mUniversityDataModel.getNews();
            }

        } else {
            view.findViewById(R.id.news_container).setVisibility(View.GONE);
        }
        if(((HomeActivity)getActivity()).mGeoTabs != 0) {
            mUniversityDataModel.getCategories();
        }

        initMap();

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(mOnRefreshListener);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.geo_purple_light);
    }

    private void populatePois(List<Poi> pois) {
        if (mMap != null && pois != null) {
            for (final Poi p : pois) {
                if (p.getLat() != null && p.getLat().length() > 0 && p.getLng() != null && p.getLng().length() > 0 && p.isActive()) {

                    if (p.getCategoryMarkerIcon() == null || p.getCategoryMarkerIcon().length() == 0) {
                        p.setCategoryMarkerIcon("cat_marker_7__biblio_big_jaune_marker.png");
                    }
                    final String url = ReadCategoriesOperation.CATEGORIES_IMAGE_URL + p.getCategoryMarkerIcon();


                    Resources r = getResources();
                    int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 25, r.getDisplayMetrics());
                    Picasso.with(getActivity()).load(url).resize(px, px).into(new Target() {

                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                            addMarker(p, BitmapDescriptorFactory.fromBitmap(bitmap));


                        }

                        @Override
                        public void onBitmapFailed(Drawable errorDrawable) {

                            Drawable d = getResources().getDrawable(R.drawable.ic_category_temp);
                            BitmapDrawable bd = (BitmapDrawable) d.getCurrent();
                            Bitmap b = bd.getBitmap();

                            Resources r = getResources();
                            int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 25, r.getDisplayMetrics());
                            Bitmap bhalfsize = Bitmap.createScaledBitmap(b, px, px, false);
                            addMarker(p, BitmapDescriptorFactory.fromBitmap(bhalfsize));

                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {

                        }
                    });
                }
            }
        }
    }


    private void addMarker(Poi p, BitmapDescriptor des) {
        if (mMap != null) {
            MarkerOptions options = new MarkerOptions();
            if (des != null) {
                options.icon(des);
            }
            options.anchor(0.5f, 0.5f);
            options.position(new LatLng(Double.parseDouble(p.getLat()), Double.parseDouble(p.getLng())));
            options.title(p.getName());
            Marker m = mMap.addMarker(options);
        }
    }

    private void initMap() {
        int geoTabs = ((HomeActivity) getActivity()).mGeoTabs;
        if (geoTabs != 0) {
            // Gets the MapView from the XML layout and creates it
            mMapView = (MapView) getView().findViewById(R.id.mapview);

            // Gets to GoogleMap from the MapView and does initialization stuff
            if (mMapView != null) {
                mMapView.onCreate(null);

                mMap = mMapView.getMap();
                if (mMap != null) {
                    mMap.getUiSettings().setMyLocationButtonEnabled(false);
                    mMap.setMyLocationEnabled(true);
                    mMap.getUiSettings().setAllGesturesEnabled(false);
                    mMap.setOnMarkerClickListener(null);
                    mMap.setOnMyLocationChangeListener(mOnMyLocationChangeListener);
                }
            }

            MapsInitializer.initialize(this.getActivity());

            if (!UniversitiesDataModel.getSavedUniversity(getActivity()).getRegionName().equals(UniversitiesDataModel.FRANCE_REGION)) {
                getView().findViewById(R.id.cat_2).setVisibility(View.GONE);
                getView().findViewById(R.id.cat_3).setVisibility(View.GONE);
            }
        } else {
            getView().findViewById(R.id.mapview).setVisibility(View.GONE);
            getView().findViewById(R.id.tabs).setVisibility(View.GONE);
            getView().findViewById(R.id.map_title).setVisibility(View.GONE);

        }

        if ((geoTabs & 1) != 1) {
            getView().findViewById(R.id.cat_1).setVisibility(View.GONE);
            getView().findViewById(R.id.separator1).setVisibility(View.GONE);
        }
        if ((geoTabs & 2) != 2) {
            getView().findViewById(R.id.cat_2).setVisibility(View.GONE);
            getView().findViewById(R.id.separator2).setVisibility(View.GONE);
        }
        if ((geoTabs & 4) != 4) {
            getView().findViewById(R.id.cat_3).setVisibility(View.GONE);
            getView().findViewById(R.id.separator2).setVisibility(View.GONE);
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
            //mainArticleImage.setImageResource(R.drawable.ic_launcher);
            if (news.get(0).getImageUrl() != null) {
                Picasso.with(getActivity()).load(news.get(0).getImageUrl()).into(mainArticleImage);
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
            description.setText(news.get(0).getDescription());

            TextView button = (TextView) getView().findViewById(R.id.main_article_action_button);
            if (news.get(0).getLink() != null) {
                try {
                    URL url = new URL(news.get(0).getLink());

                    final String urlString = news.get(0).getLink();
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(urlString));
                            getActivity().startActivity(i);
                        }
                    });

                } catch (MalformedURLException e) {
                    button.setVisibility(View.GONE);
                    e.printStackTrace();
                }
            } else {
                button.setVisibility(View.GONE);
            }

            NewsItemView news1 = (NewsItemView) getView().findViewById(R.id.news1);
            if (news.size() > 1) {
                news1.populate(news.get(1), mDateFormat, 0);
                news1.setVisibility(View.VISIBLE);
            } else {
                news1.setVisibility(View.GONE);
            }

            NewsItemView news2 = (NewsItemView) getView().findViewById(R.id.news2);
            if (news.size() > 2) {
                news2.populate(news.get(2), mDateFormat, 0);
                news2.setVisibility(View.VISIBLE);
            } else {
                news2.setVisibility(View.GONE);
            }

            NewsItemView news3 = (NewsItemView) getView().findViewById(R.id.news3);
            if (news.size() > 3) {
                news3.populate(news.get(3), mDateFormat, 0);
                news3.setVisibility(View.VISIBLE);
            } else {
                news3.setVisibility(View.GONE);
            }

            NewsItemView news4 = (NewsItemView) getView().findViewById(R.id.news4);
            if (news.size() > 4) {
                news4.populate(news.get(4), mDateFormat, 0);
                news4.setVisibility(View.VISIBLE);
            } else {
                news4.setVisibility(View.GONE);
            }

        }
    }

    private GoogleMap.OnMyLocationChangeListener mOnMyLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
        @Override
        public void onMyLocationChange(Location location) {
            if (!mAnimatedToMyPosition && mMap != null) {
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 16);
                mMap.moveCamera(cameraUpdate);
                mAnimatedToMyPosition = true;
                mMap.setOnMyLocationChangeListener(null);
            }
        }
    };

    private SwipeRefreshLayout.OnRefreshListener mOnRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {

            mShowLoading = false;
            if (mUniversityDataModel != null) {
                mUniversityDataModel.getNews();
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
            a.showFragment(new UniversityNewsFragment(), UniversityNewsFragment.class.getName(), false);
        }
    };

    private UniversityDataModel.UniversityModelListener mUniversityModelListener = new UniversityDataModel.UniversityModelListener() {


        @Override
        public void updateNews(List<News> news) {
            mSwipeRefreshLayout.setRefreshing(false);
            getView().findViewById(R.id.news_container).setVisibility(View.VISIBLE);
            getView().findViewById(R.id.progressBar1).setVisibility(View.GONE);
            populateNews(news);
        }

        @Override
        public void updatePois(List<Poi> pois) {
            mSwipeRefreshLayout.setRefreshing(false);
            getView().findViewById(R.id.progressBar1).setVisibility(View.GONE);
            populatePois(pois);

        }

        @Override
        public void showErrorMessage(ErrorEntity error) {
            if(!isDetached() && isVisible()) {
                mSwipeRefreshLayout.setRefreshing(false);
                getView().findViewById(R.id.progressBar1).setVisibility(View.GONE);
                getView().findViewById(R.id.news_container).setVisibility(View.GONE);
            }

        }

        @Override
        public void onError(ErrorEntity mError) {
            mSwipeRefreshLayout.setRefreshing(false);
            getView().findViewById(R.id.progressBar1).setVisibility(View.GONE);
            getView().findViewById(R.id.news_container).setVisibility(View.GONE);
            handleError(mError);
        }
    };

    private FeedsDataModel.FeedsModelListener mFeedsDataModelListener = new FeedsDataModel.FeedsModelListener() {
        @Override
        public void showLoadingIndicator() {

        }

        @Override
        public void showErrorMessage(ErrorEntity error) {

        }

        @Override
        public void onFeedsReceived(List<NewsFeed> feeds) {

            mFeedsDataModel.saveFeedsInPrefs(getActivity(), mFeedsDataModel.getSelectedFeeds());
            mUniversityDataModel.getNews();
        }

        @Override
        public void onError(ErrorEntity mError) {
        }
    };
}
