package org.unpidf.univmobile;

import org.unpidf.univmobile.manager.DataManager;
import org.unpidf.univmobile.manager.LocManager;
import org.unpidf.univmobile.manager.LocManager.LocListener;
import org.unpidf.univmobile.view.SelectUniversityActivity;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class AccueilActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_accueil);
		initEvents();
		LocManager.getInstance(this).addLocationListener(new LocListener() {
			@Override
			public void onLocationNotChanged(String address) {
				Location loc = LocManager.getLocation();
				if(loc != null && loc.getLatitude() != 0 && loc.getLongitude() != 0){
					Toast.makeText(AccueilActivity.this, "Loc Not Changed : lat="+loc.getLatitude()+", long="+loc.getLongitude(), Toast.LENGTH_SHORT).show();
				}
			}
			
			@Override
			public void onLocationChanged(Location loc) {
				if(loc != null && loc.getLatitude() != 0 && loc.getLongitude() != 0){
					Toast.makeText(AccueilActivity.this, "Loc Changed : lat="+loc.getLatitude()+", long="+loc.getLongitude(), Toast.LENGTH_SHORT).show();
				}
			}
			
			@Override
			public void onAddressChanged(String address, boolean success) {
			}
		});
		LocManager.getInstance(this).requestUpdate();
	}

	private void initData() {
		if(DataManager.getInstance(this).getCurrentUniversity() == null){
			((TextView)findViewById(R.id.universitySelected)).setText("Aucune université sélectionée.");
		}else{
			((TextView)findViewById(R.id.universitySelected)).setText(DataManager.getInstance(this).getCurrentUniversity().getTitle());
		}
	}

	private void initEvents() {
		findViewById(R.id.selectUniversity).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getApplicationContext(), SelectUniversityActivity.class));
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		initData();
	}

}
