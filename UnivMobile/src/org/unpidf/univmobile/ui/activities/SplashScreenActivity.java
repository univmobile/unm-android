package org.unpidf.univmobile.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import org.unpidf.univmobile.R;
import org.unpidf.univmobile.data.entities.ErrorEntity;
import org.unpidf.univmobile.data.entities.Region;
import org.unpidf.univmobile.data.entities.University;
import org.unpidf.univmobile.data.models.UniversitiesDataModel;

import java.util.ArrayList;
import java.util.List;


public class SplashScreenActivity extends Activity implements UniversitiesDataModel.UniversitiesDataModelObserver {


	private UniversitiesDataModel mModel;
	private Spinner mSpinner;
	private static Handler mHandler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_splash_screen);
		mModel = new UniversitiesDataModel(this);

		if (mModel.isUniversitySaved()) {
			findViewById(R.id.splash_screen_content).setVisibility(View.GONE);
			//findViewById(R.id.splash_screen_footer).setVisibility(View.GONE);
			startHomeActivityDelayed();
		} else {
			mSpinner = (Spinner) findViewById(R.id.spinner);
			mModel.getRegions(this);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mModel.clear();
		mModel = null;
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
			mModel.getUniversities(SplashScreenActivity.this, mModel.regions().get(mSpinner.getSelectedItemPosition()));
		}
	};


	@Override
	public void showLoadingIndicator() {
		findViewById(R.id.progressBar1).setVisibility(View.VISIBLE);
	}

	@Override
	public void showRegions(List<Region> regionsList) {
		findViewById(R.id.progressBar1).setVisibility(View.GONE);
		List<String> list = new ArrayList<String>();

		for (Region university : regionsList) {
			list.add(university.getName());
		}
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.view_spinner_item, list);
		dataAdapter.setDropDownViewResource(R.layout.view_spinner_item);
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
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.view_spinner_item, list);
		dataAdapter.setDropDownViewResource(R.layout.view_spinner_item);
		mSpinner.setAdapter(dataAdapter);

		((TextView) findViewById(R.id.title)).setText(R.string.select_university);
		findViewById(R.id.accept_button).setEnabled(true);
		findViewById(R.id.accept_button).setOnClickListener(mSelectUniversityListener);
	}

	@Override
	public void showErrorMessage(ErrorEntity error) {
		findViewById(R.id.progressBar1).setVisibility(View.GONE);
	}

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

		}
	}
}
