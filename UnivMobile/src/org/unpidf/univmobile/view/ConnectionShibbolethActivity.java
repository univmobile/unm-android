package org.unpidf.univmobile.view;

import org.unpidf.univmobile.R;
import org.unpidf.univmobile.dao.University;
import org.unpidf.univmobile.manager.DataManager;
import org.unpidf.univmobile.manager.MappingManager;
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
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

/**
 * Shibbo Connection Activity
 *
 * @author Michel
 */
public class ConnectionShibbolethActivity extends Activity {
	
	private WebView webView;
	private String key;
	private String loginToken;
	
	private BroadcastReceiver receiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			if(intent.getAction().equals(UserManager.NOTI_SHIBBO_PREPARE_OK)){
				loginToken = intent.getStringExtra("loginToken");
				key = intent.getStringExtra("key");
				University univ = DataManager.getInstance(context).getCurrentUniversity();
				String url = MappingManager.getUrlShibbo(context, MappingManager.getUrlShibboTarget(context, loginToken), univ.getShibbolethIdentityProvider());
				System.out.println("---URL WEBVIEW= "+url);
				webView.loadUrl(url);
			}else if(intent.getAction().equals(UserManager.NOTI_SHIBBO_PREPARE_ERR)){
				Toast.makeText(context, "Erreur...", Toast.LENGTH_SHORT).show();
			}else if(intent.getAction().equals(UserManager.NOTI_CONNEXION_OK)){
				Toast.makeText(context, "Connecté !", Toast.LENGTH_SHORT).show();
				startActivity(new Intent(ConnectionShibbolethActivity.this, UserProfilActivity.class));
				ConnectionShibbolethActivity.this.setResult(RESULT_OK);
				finish();
			}else if(intent.getAction().equals(UserManager.NOTI_CONNEXION_ERR)){
				Toast.makeText(context, "Echec de la connexion !", Toast.LENGTH_SHORT).show();
			}
		}
	};
	
	private WebViewClient client = new WebViewClient(){
		
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			System.out.println("---URL WEB = "+url);
			if(url.startsWith(MappingManager.getUrlShibboSuccess(ConnectionShibbolethActivity.this))){
				Toast.makeText(ConnectionShibbolethActivity.this, "Succès !!!", Toast.LENGTH_SHORT).show();
				UserManager.getInstance(ConnectionShibbolethActivity.this).retriveShibbo(loginToken, key);
				view.setVisibility(View.GONE);
			}
			view.loadUrl(url);
			return true;
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shibboleth_connection);
		IntentFilter filter = new IntentFilter(UserManager.NOTI_SHIBBO_PREPARE_OK);
		filter.addAction(UserManager.NOTI_SHIBBO_PREPARE_ERR);
		filter.addAction(UserManager.NOTI_CONNEXION_OK);
		filter.addAction(UserManager.NOTI_CONNEXION_ERR);
		LocalBroadcastManager.getInstance(this).registerReceiver(receiver, filter);
		webView = (WebView) findViewById(R.id.shibboWebView);
		initWebView();
		UserManager.getInstance(this).prepareShibbo();
		initActionBar();
	}

	private void initWebView() {
		CookieSyncManager.createInstance(this);
		CookieManager cookieManager = CookieManager.getInstance();
		cookieManager.removeAllCookie();

		WebSettings ws = webView.getSettings();
		ws.setSaveFormData(false);
		ws.setSavePassword(false);
		webView.setWebViewClient(client);
		webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
		webView.getSettings().setSaveFormData(false);
		webView.getSettings().setJavaScriptEnabled(true);
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
		super.onDestroy();
		try{
			LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
		}catch(IllegalArgumentException e){
			e.printStackTrace();
		}
	}

}
