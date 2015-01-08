package org.unpidf.univmobile.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

import org.unpidf.univmobile.R;
import org.unpidf.univmobile.data.entities.Regions;
import org.unpidf.univmobile.data.models.RegionsDataModel;
import org.unpidf.univmobile.data.models.RegionsDataModel.*;


public class SplashScreenActivity extends Activity {

	private static final int SPLASH_SCREEN_MIN_SHOW_TIME = 3 * 1000;
	private Handler mHandler = new Handler();

	private long mStartTime;
	private Regions mRegions;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_screen);

		mStartTime = System.currentTimeMillis();
		RegionsDataModel model = new RegionsDataModel(this);
		model.addObserver(mRegionsObserver);
		model.getRegions();
	}

	private RegionsDataModelObserver mRegionsObserver = new RegionsDataModelObserver() {

		@Override
		public void onLoadStarted() {

		}

		@Override
		public void onLoadFailed(String message) {

		}

		@Override
		public void onRegionsReceived(Regions regions) {
			mRegions = regions;
			long delayTime = mStartTime + SPLASH_SCREEN_MIN_SHOW_TIME - System.currentTimeMillis();
			if (delayTime < 0) {
				delayTime = 0;
			}
			mHandler.postDelayed(mStartMainActivityRunnable, delayTime);
		}
	};

	private Runnable mStartMainActivityRunnable = new Runnable() {
		@Override
		public void run() {
			Intent i = new Intent(SplashScreenActivity.this, SelectUniversityActivity.class);
			i.putExtra(SelectUniversityActivity.EXTRA_REGIONS, mRegions);
			startActivity(i);
			finish();
		}
	};
}
