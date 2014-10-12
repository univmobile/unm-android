package org.unpidf.univmobile.view;

import org.unpidf.univmobile.R;
import org.unpidf.univmobile.dao.University;
import org.unpidf.univmobile.manager.DataManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * Accueil Connection Activity
 *
 * @author Michel
 */
public class ConnectionAccueilActivity extends Activity {

	private static final int REQUEST_CONNECT = 123;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_accueil_connection);
		initActionBar();
		initEvents();
		University univ = DataManager.getInstance(this).getCurrentUniversity();
		if(univ == null || univ.getShibbolethIdentityProvider() == null){
			//Shibbo nor available
			finish();
			startActivity(new Intent(ConnectionAccueilActivity.this, ConnectionStandardActivity.class));
		}
	}

	private void initEvents() {
		findViewById(R.id.buttonShibboleth).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivityForResult(new Intent(ConnectionAccueilActivity.this, ConnectionShibbolethActivity.class), REQUEST_CONNECT);
			}
		});
		findViewById(R.id.buttonStandard).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivityForResult(new Intent(ConnectionAccueilActivity.this, ConnectionStandardActivity.class), REQUEST_CONNECT);
			}
		});
	}

	private void initActionBar() {
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setTitle("Connexion");
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == REQUEST_CONNECT && resultCode == RESULT_OK){
			//On vient de se connecter
			finish();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
