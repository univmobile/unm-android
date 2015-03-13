package org.unpidf.univmobile.ui.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flurry.android.FlurryAgent;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.unpidf.univmobile.R;
import org.unpidf.univmobile.UnivMobileApp;
import org.unpidf.univmobile.data.entities.Category;
import org.unpidf.univmobile.data.entities.ErrorEntity;
import org.unpidf.univmobile.data.entities.Login;
import org.unpidf.univmobile.data.entities.NavigationMenu;
import org.unpidf.univmobile.data.entities.NotificationEntity;
import org.unpidf.univmobile.data.entities.Poi;
import org.unpidf.univmobile.data.entities.University;
import org.unpidf.univmobile.data.models.GeoDataModel;
import org.unpidf.univmobile.data.models.MenusDataModel;
import org.unpidf.univmobile.data.models.NotificationsDataModel;
import org.unpidf.univmobile.data.models.UniversitiesDataModel;
import org.unpidf.univmobile.ui.adapters.NavigationDrawerAdapter;
import org.unpidf.univmobile.ui.fragments.AbsFragment;
import org.unpidf.univmobile.ui.fragments.GeoCampusFragment;
import org.unpidf.univmobile.ui.fragments.HomeFragment;
import org.unpidf.univmobile.ui.fragments.LibraryFragment;
import org.unpidf.univmobile.ui.fragments.MediaFragment;
import org.unpidf.univmobile.ui.fragments.MyProfileFragment;
import org.unpidf.univmobile.ui.fragments.MyWebViewFragment;
import org.unpidf.univmobile.ui.fragments.NotificationsFragment;
import org.unpidf.univmobile.ui.fragments.ShibbolethLoginFragment;
import org.unpidf.univmobile.ui.fragments.StandartLoginFragment;
import org.unpidf.univmobile.ui.fragments.UniversityNewsFragment;
import org.unpidf.univmobile.ui.uiutils.FontHelper;
import org.unpidf.univmobile.ui.widgets.AnimatedExpandableListView;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AbsActivity {

	private static final int MY_PROFILE_MENU_ID = 15;
	private static final int MY_LIBRARY_MENU_ID = 16;
	private static final int MY_MEDIA_MENU_ID = 17;

	private static final int GEO_MENU_ID = 20;
	private static final int GEO_UNIV_MENU_ID = 21;
	private static final int BONPLAN_MENU_ID = 22;

	public static final String EXTRA_POI_ID = "extra_poi_id";
	private ActionBar actionBar;
	private DrawerLayout mDrawerLayout;
	private AnimatedExpandableListView mDrawerList;

	private NotificationsDataModel mNotificationsDataModel;
	private MenusDataModel mMenusDataModel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		moveDrawerToTop();
		initActionBar();
		initDrawer();
		initNotifications();
	}

	public void restart() {
		Intent i = new Intent(HomeActivity.this, HomeActivity.class);
		startActivity(i);
		finish();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mNotificationsDataModel != null) {
			mNotificationsDataModel.clear();
			mNotificationsDataModel = null;
		}


		if (mMenusDataModel != null) {
			mMenusDataModel.clear();
			mMenusDataModel = null;
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		FlurryAgent.onStartSession(this, getString(R.string.flurry_key));
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

	private void initNotifications() {
		mNotificationsDataModel = new NotificationsDataModel(this);
		mNotificationsDataModel.getNotifications(new NotificationsDataModel.NotificationsModelListener() {
			@Override
			public void showLoadingIndicator() {
			}

			@Override
			public void notificationsReceived(int newNotificationsCount, List<NotificationEntity> notifications) {
				TextView countView = (TextView) getActionBar().getCustomView().findViewById(R.id.notifications_count);
				if (countView != null) {
					if (newNotificationsCount == 0) {
						countView.setVisibility(View.GONE);
					} else {
						countView.setVisibility(View.VISIBLE);
						countView.setText(Integer.toString(newNotificationsCount));
					}
				}
			}

			@Override
			public void onError(ErrorEntity mError) {
				handleError(mError);
			}
		});
	}

	public void resetNewNotificationsCount() {
		TextView countView = (TextView) getActionBar().getCustomView().findViewById(R.id.notifications_count);
		countView.setText("");
		countView.setVisibility(View.GONE);
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

		actionBarView.findViewById(R.id.login_button).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				University univ = UniversitiesDataModel.getSavedUniversity(HomeActivity.this);
				if (univ.getMobileShibbolethUrl() == null) {
					showFragment(StandartLoginFragment.newInstance(), StandartLoginFragment.class.getName(), true);
				} else {
					showFragment(ShibbolethLoginFragment.newInstance(), ShibbolethLoginFragment.class.getName(), true);
				}
			}
		});

		actionBarView.findViewById(R.id.university_container).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showSplashScreen();
			}
		});


//		actionBarView.findViewById(R.id.login_container).setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				((UnivMobileApp) getApplicationContext()).logout();
//				restart();
//				//showFragment(ShibbolethLoginFragment.newInstance(), ShibbolethLoginFragment.class.getName(), true);
//			}
//		});

		University u = UniversitiesDataModel.getSavedUniversity(this);
		TextView universityText = (TextView) actionBarView.findViewById(R.id.university_name);
		universityText.setText(u.getTitle());

		initUser();

		//init fonts
		FontHelper helper = ((UnivMobileApp) getApplicationContext()).getFontHelper();
		helper.loadFont((android.widget.TextView) actionBarView.findViewById(R.id.notifications_count), FontHelper.FONT.EXO_BOLD);
		helper.loadFont((android.widget.TextView) actionBarView.findViewById(R.id.university_name), FontHelper.FONT.EXO2_REGULAR);
		helper.loadFont((android.widget.TextView) actionBarView.findViewById(R.id.greetings), FontHelper.FONT.EXO2_REGULAR);
		helper.loadFont((android.widget.TextView) actionBarView.findViewById(R.id.user_name), FontHelper.FONT.EXO2_LIGHT);
		helper.loadFont((android.widget.TextView) actionBarView.findViewById(R.id.login_button), FontHelper.FONT.EXO_MEDIUM);

	}

	public void initUser() {
		View actionBarView = actionBar.getCustomView();
		View loginContainer = actionBarView.findViewById(R.id.login_container);
		View loginButton = actionBarView.findViewById(R.id.login_button);
		Login login = ((UnivMobileApp) getApplicationContext()).getLogin();
		if (login == null) {
			loginButton.setVisibility(View.VISIBLE);
			loginContainer.setVisibility(View.GONE);
		} else {
			loginButton.setVisibility(View.GONE);
			loginContainer.setVisibility(View.VISIBLE);

			TextView name = (TextView) actionBarView.findViewById(R.id.user_name);
			name.setText(login.getName());
		}
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

		initMenu();
	}

	private void initMenu() {

		mMenusDataModel = new MenusDataModel(this);
		mMenusDataModel.getMenus(new MenusDataModel.MenusModelListener() {
			@Override
			public void menusReceived(List<org.unpidf.univmobile.data.entities.NavigationMenu> msMenus, List<org.unpidf.univmobile.data.entities.NavigationMenu> muMenus, List<org.unpidf.univmobile.data.entities.NavigationMenu> ttMenus) {

				mDrawerLayout.findViewById(R.id.navigation_progress).setVisibility(View.GONE);
				List<NavigationMenu> menuGroups = new ArrayList<NavigationMenu>();
//				//first menu
//				List<NavigationMenu> menuChild = new ArrayList<NavigationMenu>();
//				menuChild.add(new NavigationMenu(11, getString(R.string.menu_group_profile)));
//				menuChild.add(new NavigationMenu(12, getString(R.string.menu_group_library)));
//				menuChild.add(new NavigationMenu(13, getString(R.string.menu_group_ent)));
//				menuChild.add(new NavigationMenu(14, getString(R.string.menu_group_workspace)));
//				menuChild.add(new NavigationMenu(15, getString(R.string.menu_group_students)));
				menuGroups.add(new NavigationMenu(1, getString(R.string.menu_services), R.drawable.ic_menu_first, msMenus));

				//second menu
				menuGroups.add(new NavigationMenu(2, getString(R.string.menu_university), R.drawable.ic_menu_second, null));

//				//third menu
//				menuChild = new ArrayList<NavigationMenu>();
//				menuChild.add(new NavigationMenu(31, getString(R.string.menu_group_geo)));
//				menuChild.add(new NavigationMenu(32, getString(R.string.menu_group_paris)));
//				menuChild.add(new NavigationMenu(33, getString(R.string.menu_group_bonplans)));
				List<org.unpidf.univmobile.data.entities.NavigationMenu> menus = new ArrayList<NavigationMenu>();
				if (!UniversitiesDataModel.getSavedUniversity(HomeActivity.this).getRegionName().equals(UniversitiesDataModel.FRANCE_REGION)) {
					for (org.unpidf.univmobile.data.entities.NavigationMenu m : ttMenus) {
						if (m.getId() != 21 && m.getId() != 22) {
							menus.add(m);
						}
					}
				} else {
					menus = ttMenus;
				}
				menuGroups.add(new NavigationMenu(3, getString(R.string.menu_interests), R.drawable.ic_menu_third, menus));

//				//forth menu
//				menuChild = new ArrayList<NavigationMenu>();
//				menuChild.add(new NavigationMenu(41, "menu 1"));
//				menuChild.add(new NavigationMenu(42, "menu 2"));
//				menuChild.add(new NavigationMenu(43, "menu 3"));
//				menuChild.add(new NavigationMenu(44, "menu 4"));
//				menuChild.add(new NavigationMenu(45, "menu 5"));
				menuGroups.add(new NavigationMenu(4, getString(R.string.menu_my), R.drawable.ic_menu_forth, muMenus));

				NavigationDrawerAdapter a = new NavigationDrawerAdapter(HomeActivity.this, menuGroups);
				mDrawerList.setAdapter(a);
				mDrawerList.setOnGroupClickListener(mOnGroupClickListener);

				mDrawerList.setOnChildClickListener(mtOnChildClickListener);
			}


			@Override
			public void onError(ErrorEntity mError) {
				mDrawerLayout.findViewById(R.id.navigation_progress).setVisibility(View.GONE);
				handleError(mError);
			}
		});


	}


	private void showFirstFragment() {
		int id = getIntent().getIntExtra(EXTRA_POI_ID, -1);
		if (id != -1) {
			GeoCampusFragment f = GeoCampusFragment.newInstance(0, id, -1, -1);
			showFragment(f, GeoCampusFragment.class.getName(), false);
		} else {
			showFragment(HomeFragment.newInstance(), HomeFragment.class.getName(), false);
		}
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

			if (id == MY_PROFILE_MENU_ID) {
				showFragment(MyProfileFragment.newInstance(), MyProfileFragment.class.getName(), false);
			} else if (id == MY_LIBRARY_MENU_ID) {
				showFragment(LibraryFragment.newInstance(), LibraryFragment.class.getName(), false);
			} else if (id == MY_MEDIA_MENU_ID) {
				showFragment(MediaFragment.newInstance(), MediaFragment.class.getName(), false);
			} else if (id == GEO_MENU_ID) {
				showFragment(GeoCampusFragment.newInstance(0, -1, -1, -1), GeoCampusFragment.class.getName(), false);
			} else if (id == GEO_UNIV_MENU_ID) {
				showFragment(GeoCampusFragment.newInstance(1, -1, -1, -1), GeoCampusFragment.class.getName(), false);
			} else if (id == BONPLAN_MENU_ID) {
				showFragment(GeoCampusFragment.newInstance(2, -1, -1, -1), GeoCampusFragment.class.getName(), false);
			} else {
				NavigationDrawerAdapter adapter = (NavigationDrawerAdapter) mDrawerList.getExpandableListAdapter();
				NavigationMenu menu = adapter.getChild(groupPosition, childPosition);

				showFragment(MyWebViewFragment.newInstance(menu.getUrl(), menu.getContent()), menu.getUrl() + menu.getContent(), false);

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
			Object f = getFragmentManager().findFragmentById(R.id.main_content);
			if (f != null && f instanceof AbsFragment) {
				if (((AbsFragment) f).onBackPressed()) {
					return;
				}
			}

			if (getFragmentManager().getBackStackEntryCount() > 0) {
				getFragmentManager().popBackStack();
			} else {
				super.onBackPressed();
			}
		}
	}

	public void logedIn(Login login) {
		((UnivMobileApp) getApplicationContext()).setLogin(login);
		restart();
//		;
//		removeTopFragment();
//		initUser();
//
//		FragmentManager manager = getFragmentManager();
//		MyProfileFragment f = (MyProfileFragment) manager.findFragmentByTag(MyProfileFragment.class.getName());
//		if (f != null) {
//			f.reload();
//		}

	}

	public void showPoiByCategory(int categoryID, int root) {
		FragmentManager manager = getFragmentManager();


		GeoCampusFragment f = (GeoCampusFragment) manager.findFragmentByTag(GeoCampusFragment.class.getName());
		if (f == null) {
			f = GeoCampusFragment.newInstance(0, -1, -1, categoryID);
			showFragment(f, GeoCampusFragment.class.getName(), false);
		} else {
			Category c = new Category();
			c.setId(categoryID);

			List<Category> categories = new ArrayList<Category>();
			categories.add(c);
			f.refreshPois(categories, root);
		}


	}

	public void categorySelected(Category cat) {
		onBackPressed();
		FragmentManager manager = getFragmentManager();
		GeoCampusFragment f = (GeoCampusFragment) manager.findFragmentByTag(GeoCampusFragment.class.getName());
		if (f != null) {
			f.categorySelected(cat);
		}
	}

	public void showPoi(int id, int categoryRoot) {

		int tabPosition = 0;
		if (categoryRoot == GeoDataModel.ROOT_CAT_2) {
			tabPosition = 1;
		} else if (categoryRoot == GeoDataModel.ROOT_CAT_3) {
			tabPosition = 2;
		}

		FragmentManager manager = getFragmentManager();
		GeoCampusFragment f = (GeoCampusFragment) manager.findFragmentByTag(GeoCampusFragment.class.getName());
		if (f == null) {
			f = GeoCampusFragment.newInstance(tabPosition, -1, id, -1);
			showFragment(f, GeoCampusFragment.class.getName(), false);
		} else {
			f.showPoi(tabPosition, id);
		}
	}

	public void showPoi(Poi poi, int categoryRoot, boolean goBack) {
		if (goBack) {
			onBackPressed();
		}
		int tabPosition = 0;
		if (categoryRoot == GeoDataModel.ROOT_CAT_2) {
			tabPosition = 1;
		} else if (categoryRoot == GeoDataModel.ROOT_CAT_3) {
			tabPosition = 2;
		}
		FragmentManager manager = getFragmentManager();

		GeoCampusFragment f = (GeoCampusFragment) manager.findFragmentByTag(GeoCampusFragment.class.getName());
		if (f == null) {
			f = GeoCampusFragment.newInstance(tabPosition, -1, poi.getId(), -1);
			showFragment(f, GeoCampusFragment.class.getName(), false);
		} else {
			f.showPoiDetails(tabPosition, poi, true);
		}
	}

	public void showFragment(Fragment fragment, String tag, boolean add) {
		FragmentManager manager = getFragmentManager();
		Fragment currentFragment = (Fragment) manager.findFragmentById(R.id.main_content);
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

	public void showSplashScreen() {
		Intent i = new Intent(this, SplashScreenActivity.class);
		i.putExtra("ignore_saved", true);
		startActivityForResult(i, 111);
	}

	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == 111) {
			if (resultCode == 222) {
				finish();
			}
		} else {
			IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
			if (scanResult != null && scanResult.getContents() != null) {
				Uri data = Uri.parse(scanResult.getContents());
				String id = data.getQueryParameter("ID");
				if (id != null) {
					id = id.replaceAll("\\s", "");
					id = id.replaceAll("\\n", "");
					FragmentManager manager = getFragmentManager();
					GeoCampusFragment f = (GeoCampusFragment) manager.findFragmentByTag(GeoCampusFragment.class.getName());
					f.showImageMap(Integer.parseInt(id));
				}
			}
		}

	}


}