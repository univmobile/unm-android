package org.unpidf.univmobile.view;

import org.unpidf.univmobile.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;

public class AproposActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_apropos);
		initActionBar();
	}

	private void initActionBar() {
		getActionBar().setDisplayHomeAsUpEnabled(true);
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
