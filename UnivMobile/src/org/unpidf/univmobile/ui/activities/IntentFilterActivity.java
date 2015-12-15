package org.unpidf.univmobile.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

/**
 * Created by rviewniverse on 2015-02-22.
 */
public class IntentFilterActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Uri data = getIntent().getData();
        if (data != null) {
            String id = data.getQueryParameter("ID");
            String poiID = data.getQueryParameter("poiID");

            if (id != null && id.length() > 0) {
                Intent intent = new Intent(this, HomeActivity.class);
                intent.putExtra(HomeActivity.EXTRA_IMAGE_MAP_ID, Integer.parseInt(id));
                intent.putExtra(HomeActivity.EXTRA_POI_ID, Integer.parseInt(poiID));
                startActivity(intent);
            }
        }
        finish();
    }
}
