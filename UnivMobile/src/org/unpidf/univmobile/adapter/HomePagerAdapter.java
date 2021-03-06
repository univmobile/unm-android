package org.unpidf.univmobile.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class HomePagerAdapter extends FragmentPagerAdapter {

	private final List<Fragment> list;
	
	public HomePagerAdapter(FragmentManager fm, List<Fragment> listFrag) {
		super(fm);
		this.list = listFrag;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		final String title = list.get(position).getArguments().getString("title");
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
