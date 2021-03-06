package org.unpidf.univmobile.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentStatePagerAdapter {

	private final List<Fragment> list;
	
	public PagerAdapter(FragmentManager fm, List<Fragment> listFrag) {
		super(fm);
		this.list = listFrag;
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
