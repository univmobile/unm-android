package org.unpidf.univmobile.view;

import org.unpidf.univmobile.R;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SelectUniversityActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_accueil);
		getFragmentManager().beginTransaction().replace(R.id.fragConteneur, new SelectRegionFragment()).commitAllowingStateLoss();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		if(getFragmentManager().getBackStackEntryCount() == 0){
			finish();
		}else{
			getFragmentManager().popBackStack();
		}
	}

	private class SelectRegionFragment extends Fragment{

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			return inflater.inflate(R.layout.frag_list, container, false);
		}

		@Override
		public void onViewCreated(View view, Bundle savedInstanceState) {
			super.onViewCreated(view, savedInstanceState);
		}
	}

	private class SelectUniversityFragment extends Fragment{

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			return inflater.inflate(R.layout.frag_list, container, false);
		}

		@Override
		public void onViewCreated(View view, Bundle savedInstanceState) {
			super.onViewCreated(view, savedInstanceState);
		}
	}

}
