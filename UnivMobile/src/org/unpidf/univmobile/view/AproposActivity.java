package org.unpidf.univmobile.view;

import org.unpidf.univmobile.R;
import org.unpidf.univmobile.manager.DataManager;
import org.unpidf.univmobile.manager.MappingManager;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

/**
 * About Activity containing useful information
 *
 * @author Michel
 */
public class AproposActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apropos);
        initActionBar();
        ((TextView) findViewById(R.id.urlApiId)).setText(MappingManager.getUrlApiJson(this));
        ((TextView) findViewById(R.id.appVersion)).setText(MappingManager.getAppVersion(this));
    }

    private void initActionBar() {
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle("À propos"); // Majuscule accentuée
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
    protected void onResume() {
        super.onResume();
        initData();
        ((TextView) findViewById(R.id.jsonBaseURL)).setText(jsonBaseURL);
    }

    /**
     * This field is set after a first HTTP call to the backend JSON API URL (urlApi),
     * because the backend may return a different base URL at runtime than the one
     * in the Android app configuration.
     */
    private String jsonBaseURL = null;

    private void initData() {
        if (jsonBaseURL==null) {
            jsonBaseURL = DataManager.getInstance(this).getJsonBaseURL();
        }
    }
}
