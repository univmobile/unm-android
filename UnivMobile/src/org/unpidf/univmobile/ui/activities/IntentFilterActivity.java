package org.unpidf.univmobile.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;

import org.unpidf.univmobile.R;

/**
 * Created by rviewniverse on 2015-02-22.
 */
public class IntentFilterActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Uri data = getIntent().getData();
		String id = data.getQueryParameter("ID");

		if(id != null && id.length() > 0) {
			Intent intent = new Intent(this, HomeActivity.class);
			intent.putExtra(HomeActivity.EXTRA_POI_ID, Integer.parseInt(id));
			startActivity(intent);
		}

		finish();
	}
}
