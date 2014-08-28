package org.unpidf.univmobile;

import org.unpidf.univmobile.manager.DataManager;
import org.unpidf.univmobile.manager.LocManager;
import org.unpidf.univmobile.manager.LocManager.LocListener;
import org.unpidf.univmobile.view.AproposActivity;
import org.unpidf.univmobile.view.GeocampusActivity;
import org.unpidf.univmobile.view.SelectUniversityActivity;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

/**
 * App Entry Point
 * @author Michel
 *
 */
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
		//LocManager.getInstance(this).requestUpdate();
	}

	private void initData() {
		if(DataManager.getInstance(this).getCurrentUniversity() == null){
			((TextView)findViewById(R.id.universitySelected)).setText("Aucune université sélectionnée.");
		}else{
			((TextView)findViewById(R.id.universitySelected)).setText(DataManager.getInstance(this).getCurrentUniversity().getTitle());
		}
	}

	private void initEvents() {
		findViewById(R.id.selectUniversity).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getApplicationContext(), SelectUniversityActivity.class));
				overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
			}
		});
		findViewById(R.id.selectGeocampus).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getApplicationContext(), GeocampusActivity.class));
			}
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add("À propos").setIcon(android.R.drawable.ic_menu_info_details).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);;
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getTitle().equals("À propos")){
			startActivity(new Intent(this, AproposActivity.class));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onResume() {
		super.onResume();
		initData();
	}

}
