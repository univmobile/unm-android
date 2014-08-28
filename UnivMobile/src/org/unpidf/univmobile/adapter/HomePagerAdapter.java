package org.unpidf.univmobile.adapter;

import java.util.List;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

public class HomePagerAdapter extends FragmentPagerAdapter {

	private List<Fragment> list;
	
	public HomePagerAdapter(FragmentManager fm, List<Fragment> listFrag) {
		super(fm);
		list = listFrag;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		String title = list.get(position).getArguments().getString("title");
		return title;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Fragment getItem(int position) {
		return list.get(position);
	}

}
