package org.unpidf.univmobile.ui.views;


import android.content.Context;
import android.graphics.Point;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.VisibleRegion;

public class MyCustomMapView extends MapView {

    public static float MAX_ZOOM = 20;
    public static float MIN_ZOOM = 5;
    public static float MIN_ZOOM_FOR_FLING = 7;

    public static double MAX_LONGITUDE = 0.0048;
    public static double MIN_LONGITUDE = -0.0048;
    public static double MAX_LATITUDE = 0.0048;
    public static double MIN_LATITUDE = -0.0048;



    private Handler mHandler = new Handler();
    private Context mContext;
    private VisibleRegion mLastCorrectRegion = null;

    private boolean mControlScroll = false;

    public MyCustomMapView(Context c, AttributeSet a, int o) {
        super(c, a, o);
        init(c);
    }

    public MyCustomMapView(Context c, AttributeSet a) {
        super(c, a);
        init(c);
    }

    public MyCustomMapView(Context c) {
        super(c);
        init(c);
    }

    public MyCustomMapView(Context c, GoogleMapOptions o) {
        super(c, o);
        init(c);
    }

    private GestureDetector mGestureDetector = null;
    private GestureDetector.SimpleOnGestureListener mGestudeListener =
            new GestureDetector.SimpleOnGestureListener() {

                @Override
                public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                    GoogleMap map = getMap();
                    LatLng target = map.getCameraPosition().target;
                    Point screenPoint = map.getProjection().toScreenLocation(target);
                    Point newPoint = new Point(screenPoint.x + (int) distanceX, screenPoint.y + (int) distanceY);
                    LatLng mapNewTarget = map.getProjection().fromScreenLocation(newPoint);
                    CameraUpdate update = CameraUpdateFactory.newLatLngZoom(
                            mapNewTarget, map.getCameraPosition().zoom);
                    tryUpdateCamera(update);
                    return true;
                }


            };
    private ScaleGestureDetector mScaleGestureDetector = null;
    private ScaleGestureDetector.SimpleOnScaleGestureListener mScaleGestudeListener =
            new ScaleGestureDetector.SimpleOnScaleGestureListener() {

                @Override
                public boolean onScale(ScaleGestureDetector detector) {

                    GoogleMap map = getMap();
                    double zoom = map.getCameraPosition().zoom;

                    double k = 1d / detector.getScaleFactor();
                    int x = (int) detector.getFocusX();
                    int y = (int) detector.getFocusY();
                    LatLng mapFocus = map.getProjection().
                            fromScreenLocation(new Point(x, y));
                    LatLng target = map.getCameraPosition().target;

                    zoom = zoom + Math.log(detector.getScaleFactor()) / Math.log(2d);
                    if (zoom < MIN_ZOOM)
                        if (zoom == MIN_ZOOM) return false;
                        else zoom = MIN_ZOOM;
                    if (zoom > MAX_ZOOM)
                        if (zoom == MAX_ZOOM) return false;
                        else zoom = MAX_ZOOM;

                    double dx = norm(mapFocus.longitude) - norm(target.longitude);
                    double dy = mapFocus.latitude - target.latitude;
                    double dk = 1d - 1d / k;
                    LatLng newTarget = new LatLng(target.latitude - dy * dk,
                            norm(target.longitude) - dx * dk);

                    CameraUpdate update = CameraUpdateFactory.newLatLngZoom(newTarget, (float) zoom);
                    tryUpdateCamera(update);
                    return true;
                }
            };


    private void tryUpdateCamera(CameraUpdate update) {
        GoogleMap map = getMap();
        final VisibleRegion reg = map.getProjection().getVisibleRegion();

        map.moveCamera(update);
        checkCurrentRegion(reg);

    }

    private void checkCurrentRegion(VisibleRegion oldReg) {
        GoogleMap map = getMap();
        VisibleRegion regNew = map.getProjection().getVisibleRegion();
        if (checkBounds(regNew)) {
            mLastCorrectRegion = regNew;
        } else {
            if (mLastCorrectRegion != null)
                oldReg = mLastCorrectRegion;

            map.moveCamera(CameraUpdateFactory.newLatLngBounds(oldReg.latLngBounds, 0));

        }
    }

    /**
     * @param lonVal
     * @return
     */
    private double norm(double lonVal) {
        while (lonVal > 360d) lonVal -= 360d;
        while (lonVal < -360d) lonVal += 360d;
        if (lonVal < 0) lonVal = 360d + lonVal;
        return lonVal;
    }

    private double denorm(double lonVal) {
        if (lonVal > 180d) lonVal = -360d + lonVal;
        return lonVal;
    }

    private boolean checkBounds(VisibleRegion reg) {
        double left = Math.min(
                Math.min(reg.farLeft.longitude, reg.nearLeft.longitude),
                Math.min(reg.farRight.longitude, reg.nearRight.longitude));
        double right = Math.max(
                Math.max(reg.farLeft.longitude, reg.nearLeft.longitude),
                Math.max(reg.farRight.longitude, reg.nearRight.longitude));
        double top = Math.max(
                Math.max(reg.farLeft.latitude, reg.nearLeft.latitude),
                Math.max(reg.farRight.latitude, reg.nearRight.latitude));
        double bottom = Math.min(
                Math.min(reg.farLeft.latitude, reg.nearLeft.latitude),
                Math.min(reg.farRight.latitude, reg.nearRight.latitude));

        boolean limitBounds = left < MIN_LONGITUDE || right > MAX_LONGITUDE ||
                bottom < MIN_LATITUDE || top > MAX_LATITUDE;
        return !limitBounds;
    }

    private void init(Context c) {

        MapsInitializer.initialize(c);

        mContext = c;
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                GoogleMap map = getMap();
                if (map != null) {
                    getMap().getUiSettings().setZoomControlsEnabled(false);
                    map.getUiSettings().setAllGesturesEnabled(false);

                    mGestureDetector = new GestureDetector(mContext, mGestudeListener);
                    mScaleGestureDetector = new ScaleGestureDetector(mContext, mScaleGestudeListener);
                } else mHandler.post(this);
            }
        });
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if(mControlScroll) {
            if (mGestureDetector != null) mGestureDetector.onTouchEvent(event);
            if (mScaleGestureDetector != null) mScaleGestureDetector.onTouchEvent(event);
        }
        return super.onInterceptTouchEvent(event);
    }

    public void controlScroll(boolean control) {
        mControlScroll = control;
        if(control) {
            GoogleMap map = getMap();
            if(map != null) {
               // mLastCorrectRegion = map.getProjection().getVisibleRegion();
            }
        }
    }
}