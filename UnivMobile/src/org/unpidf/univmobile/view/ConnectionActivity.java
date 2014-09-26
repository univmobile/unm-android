package org.unpidf.univmobile.view;

import org.unpidf.univmobile.R;
import org.unpidf.univmobile.manager.UserManager;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Basic Connection Activity
 *
 * @author Michel
 */
public class ConnectionActivity extends Activity {

	private BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if(intent.getAction().equals(UserManager.NOTI_CONNEXION_OK)){
				Toast.makeText(ConnectionActivity.this, "Connecté !", Toast.LENGTH_SHORT).show();
				finish();
			}else{
				Toast.makeText(ConnectionActivity.this, "Erreur...", Toast.LENGTH_SHORT).show();
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_connection);
		IntentFilter filter = new IntentFilter(UserManager.NOTI_CONNEXION_OK);
		filter.addAction(UserManager.NOTI_CONNEXION_ERR);
		LocalBroadcastManager.getInstance(this).registerReceiver(receiver, filter);
		initActionBar();
		initEvents();
	}

	private void initEvents() {
		findViewById(R.id.button1).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String login = ((EditText)findViewById(R.id.editText1)).getText().toString().trim();
				String password = ((EditText)findViewById(R.id.editText2)).getText().toString().trim();
				if(login.length() == 0 || password.length() == 0){
					Toast.makeText(ConnectionActivity.this, "Veuillez entrer un login et un mot de passe", Toast.LENGTH_SHORT).show();
				}else{
					UserManager.getInstance(ConnectionActivity.this).connect(login, password);
				}
			}
		});
	}

	private void initActionBar() {
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setTitle("Connexion");
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onDestroy() {
		try{
			LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
		}catch(IllegalArgumentException e){
			e.printStackTrace();
		}
		super.onDestroy();
	}

}
