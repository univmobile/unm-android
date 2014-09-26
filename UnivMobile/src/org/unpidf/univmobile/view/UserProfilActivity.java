package org.unpidf.univmobile.view;

import org.unpidf.univmobile.R;
import org.unpidf.univmobile.dao.User;
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
import android.widget.TextView;
import android.widget.Toast;

/**
 * Display user profile
 *
 * @author Michel
 */
public class UserProfilActivity extends Activity {

	private BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if(intent.getAction().equals(UserManager.NOTI_DISCONNEXION_OK)){
				Toast.makeText(UserProfilActivity.this, "Déconnexion prise en compte.", Toast.LENGTH_SHORT).show();
				finish();
			}else{
				Toast.makeText(UserProfilActivity.this, "Erreur...", Toast.LENGTH_SHORT).show();
			}
		}
	};
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profil);
        initActionBar();
        IntentFilter filter = new IntentFilter(UserManager.NOTI_DISCONNEXION_OK);
		filter.addAction(UserManager.NOTI_DISCONNEXION_ERR);
		LocalBroadcastManager.getInstance(this).registerReceiver(receiver, filter);
        User user = UserManager.getInstance(this).getUser();
        if(user == null){
        	finish();
        	return;
        }
        ((TextView)findViewById(R.id.nomComplet)).setText(user.getDisplayName());
        ((TextView)findViewById(R.id.email)).setText(user.getMail());
        ((TextView)findViewById(R.id.identifiant)).setText(user.getUid());
        findViewById(R.id.deconnect).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				UserManager.getInstance(UserProfilActivity.this).disconnect();
			}
		});
    }

    private void initActionBar() {
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle("Profil");
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
