package org.unpidf.univmobile.view;

import java.util.ArrayList;
import java.util.List;

import org.unpidf.univmobile.R;
import org.unpidf.univmobile.adapter.HomePagerAdapter;
import org.unpidf.univmobile.custom.PagerSlidingTabStrip;
import org.unpidf.univmobile.dao.Poi;
import org.unpidf.univmobile.fragments.UniversityCommentFragment;
import org.unpidf.univmobile.fragments.UniversityDetailsFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.MenuItem;
/**
 * Details about a specific university
 * @author Michel
 *
 */
public class UniversityActivity extends FragmentActivity {

	private ViewPager mViewPager;
	private PagerSlidingTabStrip mTabs;
	private List<Fragment> listFraments;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_university);
		initActionBar();
		initTabsPager();
	}

	private void initActionBar() {
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setTitle(((Poi) getIntent().getSerializableExtra("poi")).getTitle());
	}
	
	/**
	 * Init {@link PagerSlidingTabStrip} of this activity
	 */
	private void initTabsPager() {
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mTabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
		listFraments = new ArrayList<Fragment>();
		listFraments.add(UniversityDetailsFragment.newInstance((Poi) getIntent().getSerializableExtra("poi"), "Détails"));
		listFraments.add(UniversityCommentFragment.newInstance((Poi) getIntent().getSerializableExtra("poi"), "Commentaires"));

		HomePagerAdapter adapter = new HomePagerAdapter(getSupportFragmentManager(), listFraments);
		mViewPager.setAdapter(adapter);
		mTabs.setViewPager(mViewPager);
		mTabs.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int arg0) {
			}
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}
			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
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
	public void finish() {
		super.finish();
//		overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
	}

}
