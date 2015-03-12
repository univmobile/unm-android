package org.unpidf.univmobile.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import org.unpidf.univmobile.R;
import org.unpidf.univmobile.UnivMobileApp;
import org.unpidf.univmobile.data.entities.ErrorEntity;
import org.unpidf.univmobile.data.entities.Region;
import org.unpidf.univmobile.data.entities.University;
import org.unpidf.univmobile.data.models.UniversitiesDataModel;
import org.unpidf.univmobile.data.operations.PostStatisticsOperation;
import org.unpidf.univmobile.ui.adapters.UniversityListAdapter;
import org.unpidf.univmobile.ui.uiutils.FontHelper;

import java.util.ArrayList;
import java.util.List;


public class SplashScreenActivity extends Activity {


	private UniversitiesDataModel mModel;
	private Spinner mSpinner;
	private static Handler mHandler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_splash_screen);

		//init fonts
		FontHelper helper = ((UnivMobileApp) getApplicationContext()).getFontHelper();
		helper.loadFont((android.widget.TextView) findViewById(R.id.title), FontHelper.FONT.EXO_REGULAR);
		helper.loadFont((android.widget.TextView) findViewById(R.id.accept_button), FontHelper.FONT.EXO_REGULAR);


		if (UniversitiesDataModel.isUniversitySaved(this)) {
			startHomeActivityDelayed();

			PostStatisticsOperation op = new PostStatisticsOperation(this, UniversitiesDataModel.getSavedUniversity(this).getSelf());
			op.startOperation();
		} else {
			mModel = new UniversitiesDataModel(this);
			mSpinner = (Spinner) findViewById(R.id.spinner);
			mModel.getRegions(mUniversitiesDataModelListener);
		}
	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(mModel != null) {
			mModel.clear();
			mModel = null;
		}
	}

	private void startHomeActivityDelayed() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				startHomeActivity();
			}
		}, 500);
	}

	private void startHomeActivity() {

		Intent i = new Intent(SplashScreenActivity.this, HomeActivity.class);
		startActivity(i);
		finish();
	}


	private View.OnClickListener mSelectUniversityListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			mModel.saveUniversity(mModel.universities().get(mSpinner.getSelectedItemPosition()));
			startHomeActivity();
		}
	};

	private View.OnClickListener mSelectRegionListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			v.setEnabled(false);
			mModel.getUniversities(mUniversitiesDataModelListener, mModel.regions().get(mSpinner.getSelectedItemPosition()));
		}
	};


	private UniversitiesDataModel.UniversitiesDataModelListener mUniversitiesDataModelListener = new UniversitiesDataModel.UniversitiesDataModelListener() {
		@Override
		public void showLoadingIndicator() {
			findViewById(R.id.progressBar1).setVisibility(View.VISIBLE);
		}

		@Override
		public void showRegions(List<Region> regionsList) {

			expand();

			findViewById(R.id.progressBar1).setVisibility(View.GONE);
			List<String> list = new ArrayList<String>();

			for (Region university : regionsList) {
				list.add(university.getName());
			}
			UniversityListAdapter dataAdapter = new UniversityListAdapter(SplashScreenActivity.this, list);
			mSpinner.setAdapter(dataAdapter);

			findViewById(R.id.accept_button).setOnClickListener(mSelectRegionListener);
		}

		@Override
		public void showUniversities(List<University> universityList) {
			findViewById(R.id.progressBar1).setVisibility(View.GONE);
			List<String> list = new ArrayList<String>();

			for (University university : universityList) {
				list.add(university.getTitle());
			}
			UniversityListAdapter dataAdapter = new UniversityListAdapter(SplashScreenActivity.this, list);

			mSpinner.setAdapter(dataAdapter);

			((TextView) findViewById(R.id.title)).setText(R.string.select_university);
			findViewById(R.id.accept_button).setEnabled(true);
			findViewById(R.id.accept_button).setOnClickListener(mSelectUniversityListener);
		}

		@Override
		public void showErrorMessage(ErrorEntity error) {
			findViewById(R.id.progressBar1).setVisibility(View.GONE);
		}
	};


	@Override
	public void onBackPressed() {
		if (mModel.universities() != null && mModel.regions() != null && mModel.regions().size() > 0) {
			mModel.setUniversities(null);

			List<String> list = new ArrayList<String>();

			for (Region university : mModel.regions()) {
				list.add(university.getName());
			}
			ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.view_spinner_item, list);
			dataAdapter.setDropDownViewResource(R.layout.view_spinner_item);
			mSpinner.setAdapter(dataAdapter);

			findViewById(R.id.accept_button).setOnClickListener(mSelectRegionListener);

		} else {
			super.onBackPressed();
		}
	}


	private void expand() {

		final View footer = findViewById(R.id.footer);
		final int height = footer.getHeight();

		Animation a = new Animation() {
			@Override
			protected void applyTransformation(float interpolatedTime, Transformation t) {
				((RelativeLayout.LayoutParams) footer.getLayoutParams()).bottomMargin = (int) (-(height - (height * interpolatedTime)));
				footer.requestLayout();
			}

			@Override
			public boolean willChangeBounds() {
				return true;
			}
		};

		// 0.25dp/ms
		a.setDuration((int) (height * 4 / getResources().getDisplayMetrics().density));

		footer.startAnimation(a);
	}


}
