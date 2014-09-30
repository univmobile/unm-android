package org.unpidf.univmobile;

import org.unpidf.univmobile.dao.User;
import org.unpidf.univmobile.manager.DataManager;
import org.unpidf.univmobile.manager.LocManager;
import org.unpidf.univmobile.manager.UserManager;
import org.unpidf.univmobile.view.AproposActivity;
import org.unpidf.univmobile.view.ConnectionActivity;
import org.unpidf.univmobile.view.GeocampusActivity;
import org.unpidf.univmobile.view.SelectUniversityActivity;
import org.unpidf.univmobile.view.UserProfilActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * App Entry Point
 * This is the first activity launched from launcher
 *
 */
public class AccueilActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_accueil);
		initEvents();
		LocManager.getInstance(this).requestUpdate();
		UserManager.getInstance(this).checkConnection();
	}

	private void initUser() {
		User user = UserManager.getInstance(this).getUser();
		if(user != null){
			((Button)findViewById(R.id.userTextView)).setText(user.getDisplayName());
			((Button)findViewById(R.id.userTextView)).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					startActivity(new Intent(AccueilActivity.this, UserProfilActivity.class));
				}
			});
		}else{
			((Button)findViewById(R.id.userTextView)).setText("Se connecter");
			((Button)findViewById(R.id.userTextView)).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					startActivity(new Intent(AccueilActivity.this, ConnectionActivity.class));
				}
			});
		}
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
	protected void onResume() {
		super.onResume();
		initData();
		initUser();
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

}
