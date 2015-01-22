package org.unpidf.univmobile.ui.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;

import org.unpidf.univmobile.R;
import org.unpidf.univmobile.data.entities.NavigationMenu;
import org.unpidf.univmobile.ui.adapters.NavigationDrawerAdapter;
import org.unpidf.univmobile.ui.widgets.AnimatedExpandableListView;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends Activity {

	private ActionBar actionBar;
	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;
	private AnimatedExpandableListView mDrawaerList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		moveDrawerToTop();
		initActionBar();
		initDrawer();
		//Quick cheat: Add Fragment 1 to default view
//		onItemClick(null, null, 0, 0);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}

	private void moveDrawerToTop() {
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		DrawerLayout drawer = (DrawerLayout) inflater.inflate(R.layout.fragment_navigation_drawer, null); // "null" is important.

		// HACK: "steal" the first child of decor view
		ViewGroup decor = (ViewGroup) getWindow().getDecorView();
		View child = decor.getChildAt(0);
		decor.removeView(child);
		LinearLayout container = (LinearLayout) drawer.findViewById(R.id.drawer_content); // This is the container we defined just now.
		container.addView(child, 0);
		drawer.findViewById(R.id.drawer).setPadding(0, getStatusBarHeight(), 0, 0);

		// Make the drawer replace the first child
		decor.addView(drawer);
	}

	public int getStatusBarHeight() {
		int result = 0;
		int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
		if (resourceId > 0) {
			result = getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		mDrawerToggle.syncState();
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}

		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void initActionBar() {
		actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);
	}

	private void initDrawer() {
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawaerList = (AnimatedExpandableListView) findViewById(R.id.listView);
		mDrawerLayout.setDrawerListener(createDrawerToggle());

		List<NavigationMenu> menuGroups = new ArrayList<NavigationMenu>();

		//first menu
		List<NavigationMenu> menuChild = new ArrayList<NavigationMenu>();
		menuChild.add(new NavigationMenu(11, "menu 1"));
		menuChild.add(new NavigationMenu(12, "menu 2"));
		menuChild.add(new NavigationMenu(13, "menu 3"));
		menuChild.add(new NavigationMenu(14, "menu 4"));
		menuChild.add(new NavigationMenu(15, "menu 5"));
		menuGroups.add(new NavigationMenu(1, "Services", R.drawable.ic_menu_first, menuChild));

		//second menu
		menuChild = new ArrayList<NavigationMenu>();
		menuChild.add(new NavigationMenu(21, "menu 1"));
		menuChild.add(new NavigationMenu(22, "menu 2"));
		menuChild.add(new NavigationMenu(23, "menu 3"));
		menuChild.add(new NavigationMenu(24, "menu 4"));
		menuChild.add(new NavigationMenu(25, "menu 5"));
		menuGroups.add(new NavigationMenu(2, "University", R.drawable.ic_menu_second, menuChild));

		//third menu
		menuChild = new ArrayList<NavigationMenu>();
		menuChild.add(new NavigationMenu(31, "menu 1"));
		menuChild.add(new NavigationMenu(32, "menu 2"));
		menuChild.add(new NavigationMenu(33, "menu 3"));
		menuChild.add(new NavigationMenu(34, "menu 4"));
		menuChild.add(new NavigationMenu(35, "menu 5"));
		menuGroups.add(new NavigationMenu(3, "Group 3", R.drawable.ic_menu_third, menuChild));

		//forth menu
		menuChild = new ArrayList<NavigationMenu>();
		menuChild.add(new NavigationMenu(41, "menu 1"));
		menuChild.add(new NavigationMenu(42, "menu 2"));
		menuChild.add(new NavigationMenu(43, "menu 3"));
		menuChild.add(new NavigationMenu(44, "menu 4"));
		menuChild.add(new NavigationMenu(45, "menu 5"));
		menuGroups.add(new NavigationMenu(4, "Group 4", R.drawable.ic_menu_forth, menuChild));

		NavigationDrawerAdapter a = new NavigationDrawerAdapter(this, menuGroups);
		mDrawaerList.setAdapter(a);
		mDrawaerList.setOnGroupClickListener(mOnGroupClickListener);

	}

	private DrawerListener createDrawerToggle() {
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

			@Override
			public void onDrawerClosed(View view) {
				super.onDrawerClosed(view);
			}

			@Override
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
			}

			@Override
			public void onDrawerStateChanged(int state) {
			}
		};
		return mDrawerToggle;
	}


	private ExpandableListView.OnGroupClickListener mOnGroupClickListener = new ExpandableListView.OnGroupClickListener() {
		@Override
		public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
			// We call collapseGroupWithAnimation(int) and
			// expandGroupWithAnimation(int) to animate group
			// expansion/collapse.
			if (mDrawaerList.isGroupExpanded(groupPosition)) {
				mDrawaerList.collapseGroupWithAnimation(groupPosition);
			} else {
				mDrawaerList.expandGroupWithAnimation(groupPosition);
			}
			return true;
		}
	};


}