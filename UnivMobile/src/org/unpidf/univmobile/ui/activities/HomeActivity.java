package org.unpidf.univmobile.ui.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebViewFragment;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;

import org.unpidf.univmobile.R;
import org.unpidf.univmobile.UnivMobileApp;
import org.unpidf.univmobile.data.entities.NavigationMenu;
import org.unpidf.univmobile.ui.adapters.NavigationDrawerAdapter;
import org.unpidf.univmobile.ui.fragments.GeoCampusFragment;
import org.unpidf.univmobile.ui.fragments.HomeFragment;
import org.unpidf.univmobile.ui.fragments.LibraryFragment;
import org.unpidf.univmobile.ui.fragments.MediaFragment;
import org.unpidf.univmobile.ui.fragments.MyProfileFragment;
import org.unpidf.univmobile.ui.fragments.MyWebViewFragment;
import org.unpidf.univmobile.ui.fragments.NotificationsFragment;
import org.unpidf.univmobile.ui.fragments.UniversityNewsFragment;
import org.unpidf.univmobile.ui.uiutils.FontHelper;
import org.unpidf.univmobile.ui.widgets.AnimatedExpandableListView;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends Activity {

	private ActionBar actionBar;
	private DrawerLayout mDrawerLayout;
	private AnimatedExpandableListView mDrawerList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		moveDrawerToTop();
		initActionBar();
		initDrawer();
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
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
		showFirstFragment();
	}

	public int getStatusBarHeight() {
		int result = 0;
		int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
		if (resourceId > 0) {
			result = getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}


	private void initActionBar() {
		actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(false);
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setHomeButtonEnabled(false);
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setCustomView(R.layout.view_action_bar);
		actionBar.setDisplayShowTitleEnabled(false);

		View actionBarView = actionBar.getCustomView();
		actionBarView.findViewById(R.id.ic_menu).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mDrawerLayout.openDrawer(Gravity.LEFT);
			}
		});

		actionBarView.findViewById(R.id.ic_notifications).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showFragment(NotificationsFragment.newInstance(), NotificationsFragment.class.getName(), true);
			}
		});

		//init fonts
		FontHelper helper = ((UnivMobileApp) getApplicationContext()).getFontHelper();
		helper.loadFont((android.widget.TextView) actionBarView.findViewById(R.id.notifications_count), FontHelper.FONT.EXO_BOLD);
		helper.loadFont((android.widget.TextView) actionBarView.findViewById(R.id.university_name_1), FontHelper.FONT.EXO2_REGULAR);
		helper.loadFont((android.widget.TextView) actionBarView.findViewById(R.id.university_name_2), FontHelper.FONT.EXO2_REGULAR);
		helper.loadFont((android.widget.TextView) actionBarView.findViewById(R.id.university_name_3), FontHelper.FONT.EXO2_REGULAR);
		helper.loadFont((android.widget.TextView) actionBarView.findViewById(R.id.greetings), FontHelper.FONT.EXO2_REGULAR);
		helper.loadFont((android.widget.TextView) actionBarView.findViewById(R.id.user_name), FontHelper.FONT.EXO2_LIGHT);

	}

	private void initDrawer() {
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

		mDrawerLayout.findViewById(R.id.university_image).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showFragment(new HomeFragment(), HomeFragment.class.getName(), false);
			}
		});

		mDrawerList = (AnimatedExpandableListView) findViewById(R.id.listView);

		List<NavigationMenu> menuGroups = new ArrayList<NavigationMenu>();


		//first menu
		List<NavigationMenu> menuChild = new ArrayList<NavigationMenu>();
		menuChild.add(new NavigationMenu(11, getString(R.string.menu_group_profile)));
		menuChild.add(new NavigationMenu(12, getString(R.string.menu_group_library)));
		menuChild.add(new NavigationMenu(13, getString(R.string.menu_group_ent)));
		menuChild.add(new NavigationMenu(14, getString(R.string.menu_group_workspace)));
		menuChild.add(new NavigationMenu(15, getString(R.string.menu_group_students)));
		menuGroups.add(new NavigationMenu(1, getString(R.string.menu_services), R.drawable.ic_menu_first, menuChild));

		//second menu
		menuGroups.add(new NavigationMenu(2, getString(R.string.menu_university), R.drawable.ic_menu_second, null));

		//third menu
		menuChild = new ArrayList<NavigationMenu>();
		menuChild.add(new NavigationMenu(31, getString(R.string.menu_group_geo)));
		menuChild.add(new NavigationMenu(32, getString(R.string.menu_group_paris)));
		menuChild.add(new NavigationMenu(33, getString(R.string.menu_group_bonplans)));
		menuGroups.add(new NavigationMenu(3, getString(R.string.menu_interests), R.drawable.ic_menu_third, menuChild));

		//forth menu
		menuChild = new ArrayList<NavigationMenu>();
		menuChild.add(new NavigationMenu(41, "menu 1"));
		menuChild.add(new NavigationMenu(42, "menu 2"));
		menuChild.add(new NavigationMenu(43, "menu 3"));
		menuChild.add(new NavigationMenu(44, "menu 4"));
		menuChild.add(new NavigationMenu(45, "menu 5"));
		menuGroups.add(new NavigationMenu(4, getString(R.string.menu_my), R.drawable.ic_menu_forth, menuChild));

		NavigationDrawerAdapter a = new NavigationDrawerAdapter(this, menuGroups);
		mDrawerList.setAdapter(a);
		mDrawerList.setOnGroupClickListener(mOnGroupClickListener);

		mDrawerList.setOnChildClickListener(mtOnChildClickListener);

	}


	private void showFirstFragment() {
		showFragment(HomeFragment.newInstance(), HomeFragment.class.getName(), false);
	}

	private ExpandableListView.OnGroupClickListener mOnGroupClickListener = new ExpandableListView.OnGroupClickListener() {
		@Override
		public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
			if (groupPosition == 1) {
				showFragment(new UniversityNewsFragment(), UniversityNewsFragment.class.getName(), false);
			} else {
				// We call collapseGroupWithAnimation(int) and
				// expandGroupWithAnimation(int) to animate group
				// expansion/collapse.
				if (mDrawerList.isGroupExpanded(groupPosition)) {
					mDrawerList.collapseGroupWithAnimation(groupPosition);
				} else {
					mDrawerList.expandGroupWithAnimation(groupPosition);
				}
			}
			return true;
		}
	};

	private ExpandableListView.OnChildClickListener mtOnChildClickListener = new ExpandableListView.OnChildClickListener() {
		@Override
		public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

			if (groupPosition == 0 && childPosition == 0) {
				showFragment(MyProfileFragment.newInstance(), MyProfileFragment.class.getName(), false);
			} else if (groupPosition == 0 && childPosition == 1) {
				showFragment(LibraryFragment.newInstance(), LibraryFragment.class.getName(), false);
			} else if (groupPosition == 0 && childPosition == 2) {
				showFragment(MyWebViewFragment.newInstance("http://www.15min.lt"), MyWebViewFragment.class.getName(), false);
			} else if (groupPosition == 0 && childPosition == 3) {
				showFragment(MediaFragment.newInstance(), MediaFragment.class.getName(), false);
			} else if (groupPosition == 0 && childPosition == 4) {
				showFragment(MyWebViewFragment.newInstance("http://www.15min.lt"), MyWebViewFragment.class.getName(), false);
			} else if (groupPosition == 2) {
				showFragment(GeoCampusFragment.newInstance(), GeoCampusFragment.class.getName(), false);

			}


			//showFragment(HomeFragment.newInstance(), HomeFragment.class.getName(), false);
			return false;
		}
	};

	@Override
	public void onBackPressed() {
		if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
			mDrawerLayout.closeDrawer(Gravity.LEFT);
		} else {
			if (getFragmentManager().getBackStackEntryCount() > 0) {
				getFragmentManager().popBackStack();
			} else {
				super.onBackPressed();
			}
		}
	}

	public void showFragment(Fragment fragment, String tag, boolean add) {
		FragmentManager manager = getFragmentManager();
		Fragment currentFragment = (Fragment) manager.findFragmentByTag("MY_FRAGMENT");
		if (currentFragment == null || !currentFragment.getTag().equals(tag)) {
			FragmentTransaction transaction = manager.beginTransaction();
			if (add) {
				transaction.add(R.id.main_content, fragment, tag);
				transaction.addToBackStack(tag);
			} else {
				manager.popBackStack();
				transaction.replace(R.id.main_content, fragment, tag);
			}

			transaction.commit();
		}

		if (mDrawerLayout != null && mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
			mDrawerLayout.closeDrawer(Gravity.LEFT);
		}
	}
}