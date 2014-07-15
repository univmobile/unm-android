package org.unpidf.univmobile;

import org.unpidf.univmobile.manager.DataManager;
import org.unpidf.univmobile.view.SelectUniversityActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class AccueilActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_accueil);
		initEvents();
	}

	private void initData() {
		if(DataManager.getCurrentUniversity() == null){
			((TextView)findViewById(R.id.universitySelected)).setText("Aucune université sélectionée.");
		}else{
			((TextView)findViewById(R.id.universitySelected)).setText(DataManager.getCurrentUniversity().getName());
		}
	}

	private void initEvents() {
		findViewById(R.id.selectUniversity).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getApplicationContext(), SelectUniversityActivity.class));
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		initData();
	}

}
