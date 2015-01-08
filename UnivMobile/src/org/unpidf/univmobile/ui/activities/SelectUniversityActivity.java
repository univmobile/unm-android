package org.unpidf.univmobile.ui.activities;

import org.unpidf.univmobile.R;
import org.unpidf.univmobile.data.entities.Region;
import org.unpidf.univmobile.data.entities.Regions;
import org.unpidf.univmobile.data.entities.University;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity for selecting a university
 *
 * @author Michel
 */
public class SelectUniversityActivity extends FragmentActivity {

	public final static String EXTRA_REGIONS = "regions";

	private Spinner mSpinner;
	private Regions mRegions;
	private int mSelectedRegionPosition = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_university);
		if (savedInstanceState != null) {
			mRegions = savedInstanceState.getParcelable(EXTRA_REGIONS);
		} else {
			mRegions = getIntent().getExtras().getParcelable(EXTRA_REGIONS);
		}
		initSpinnerRegions();

		findViewById(R.id.accept_button).setOnClickListener(mAcceptButtonListener);
	}


	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putParcelable(EXTRA_REGIONS, mRegions);
	}

	private void initSpinnerRegions() {
		mSpinner = (Spinner) findViewById(R.id.spinner);
		List<String> list = new ArrayList<String>();


		for (Region region : mRegions.getRegions()) {
			list.add(region.getLabel());
		}
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpinner.setAdapter(dataAdapter);
	}

	private void initSpinnerUniversities(Region region) {
		List<String> list = new ArrayList<String>();

		for (University university : region.getUniversities()) {
			list.add(university.getTitle());
		}
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpinner.setAdapter(dataAdapter);
	}


	private View.OnClickListener mAcceptButtonListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			if(mSelectedRegionPosition == -1) { //region is not selected
				mSelectedRegionPosition = mSpinner.getSelectedItemPosition();
				initSpinnerUniversities(mRegions.getRegions().get(mSelectedRegionPosition));
			} else { //select university

			}

		}
	};
}
