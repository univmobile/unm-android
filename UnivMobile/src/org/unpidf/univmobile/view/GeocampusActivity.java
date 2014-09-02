package org.unpidf.univmobile.view;

import java.util.ArrayList;
import java.util.List;

import org.unpidf.univmobile.R;
import org.unpidf.univmobile.adapter.HomePagerAdapter;
import org.unpidf.univmobile.custom.PagerSlidingTabStrip;
import org.unpidf.univmobile.fragments.ListPoiFragment;
import org.unpidf.univmobile.fragments.MapsPoiFragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.MenuItem;
/**
 *  List and locate universities
 * @author Michel
 *
 */
public class GeocampusActivity extends Activity{

	private ViewPager mViewPager;
	private PagerSlidingTabStrip mTabs;
	private List<Fragment> listFraments;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_geocampus);
		initActionBar();
		initTabsPager();
	}

	/**
	 * Init {@link PagerSlidingTabStrip} of this activity
	 */
	private void initTabsPager() {
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mTabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
		listFraments = new ArrayList<Fragment>();
		listFraments.add(ListPoiFragment.newInstance("Liste"));
		listFraments.add(MapsPoiFragment.newInstance("Plan"));

		HomePagerAdapter adapter = new HomePagerAdapter(getFragmentManager(), listFraments);
		mViewPager.setAdapter(adapter);
		mTabs.setViewPager(mViewPager);
	}
	
	private void initActionBar() {
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setTitle("Géocampus");
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == android.R.id.home){
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public ViewPager getViewPager() {
		return mViewPager;
	}

	public int getCurrentPage() {
		return mViewPager.getCurrentItem();
	}
}
