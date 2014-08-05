package org.unpidf.univmobile.view;

import org.unpidf.univmobile.R;
import org.unpidf.univmobile.manager.MappingManager;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

public class AproposActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_apropos);
		initActionBar();
		((TextView)findViewById(R.id.urlApiId)).setText(MappingManager.getUrlApi(this));
	}

	private void initActionBar() {
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setTitle("A propos");
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == android.R.id.home){
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
