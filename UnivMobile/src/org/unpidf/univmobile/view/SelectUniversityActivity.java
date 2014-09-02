package org.unpidf.univmobile.view;

import org.unpidf.univmobile.R;
import org.unpidf.univmobile.dao.Region;
import org.unpidf.univmobile.fragments.SelectRegionFragment;
import org.unpidf.univmobile.fragments.SelectUniversityFragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
/**
 * Activity for selecting a university
 * @author Michel
 *
 */
public class SelectUniversityActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_univ);
		initActionBar();
		getFragmentManager().beginTransaction().replace(R.id.fragConteneur, SelectRegionFragment.newInstance() , "SelectRegion").commitAllowingStateLoss();
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

	@Override
	public void onBackPressed() {
		if(getFragmentManager().getBackStackEntryCount() == 0){
			finish();
		}else{
			getFragmentManager().popBackStack();
		}
	}
	
	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
	}

	/**
	 * Switch fragment to go to {@link SelectUniversityFragment}
	 * @param region
	 */
	public void swichToUniversity(Region region) {
		getFragmentManager().beginTransaction()
		.setCustomAnimations(R.anim.slide_in_left, 0, 0, R.anim.slide_in_right) 
		.add(R.id.fragConteneur, SelectUniversityFragment.newInstance(region) , "SelectUniv")
		.addToBackStack("SelectUniv")
		.commitAllowingStateLoss();
	}
	
}
