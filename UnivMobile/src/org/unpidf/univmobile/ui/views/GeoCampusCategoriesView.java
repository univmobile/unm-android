package org.unpidf.univmobile.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.unpidf.univmobile.R;
import org.unpidf.univmobile.UnivMobileApp;
import org.unpidf.univmobile.data.entities.Category;
import org.unpidf.univmobile.ui.adapters.CategoryAdapter;
import org.unpidf.univmobile.ui.uiutils.ColorsHelper;
import org.unpidf.univmobile.ui.uiutils.FontHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rviewniverse on 2015-02-07.
 */
public class GeoCampusCategoriesView extends RelativeLayout {


	private Map<Integer, List<Category>> mCategories = new HashMap<Integer, List<Category>>();
	private Map<Integer, Integer> mRootCategories = new HashMap<Integer, Integer>();

	//private Map<Integer, List<LinearLayout>> mCategoriesLayout = new HashMap<Integer, List<LinearLayout>>();

//
//	private Map<Integer, List<Category>> mOldCategoriesSelected = new HashMap<Integer, List<Category>>();
//	private Map<Integer, List<Category>> mCategoriesSelected = new HashMap<Integer, List<Category>>();

	private List<Category> mChangedCats = new ArrayList<Category>();
	private CategoriesInterface mCategoriesInterface;

	private int mSelectedTab = 0;

	private boolean mIsExpanded = false;

	public GeoCampusCategoriesView(Context context) {
		super(context);
		init();
	}

	public GeoCampusCategoriesView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public GeoCampusCategoriesView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}


	private void init() {
		LayoutInflater.from(getContext()).inflate(R.layout.view_geo_campus_categories, this, true);

		//init fonts
		FontHelper helper = ((UnivMobileApp) getContext().getApplicationContext()).getFontHelper();
		helper.loadFont((android.widget.TextView) findViewById(R.id.title), FontHelper.FONT.EXO_REGULAR);

		findViewById(R.id.top_container).setOnClickListener(mExpandListener);

		findViewById(R.id.search).setOnClickListener(mOnSearchClickListener);
	}

	public void clear() {
		mCategoriesInterface = null;
	}

	public void setCategoriesInterface(CategoriesInterface listener) {
		mCategoriesInterface = listener;
	}

	public void setTabsCount(int tabsCount) {
		if (tabsCount == 1) {
			findViewById(R.id.cat_2).setVisibility(View.GONE);
			findViewById(R.id.cat_3).setVisibility(View.GONE);
			findViewById(R.id.separator1).setVisibility(View.GONE);
			findViewById(R.id.separator2).setVisibility(View.GONE);
		}
	}


	public void populate(List<Category> cat1, List<Category> cat2, List<Category> cat3, int root1, int root2, int root3, int selectedTab) {
		findViewById(R.id.loading).setVisibility(View.GONE);
		if (cat1 == null) {
			cat1 = new ArrayList<Category>();
		}
		if (cat2 == null) {
			cat2 = new ArrayList<Category>();
		}

		if (cat3 == null) {
			cat3 = new ArrayList<Category>();
		}
		mCategories.put(0, cat1);
		mCategories.put(1, cat2);
		mCategories.put(2, cat3);

		mRootCategories.put(0, root1);
		mRootCategories.put(1, root2);
		mRootCategories.put(2, root3);


		mSelectedTab = selectedTab;
		initTabs();
		initColors();
	}


	public int getSelectedTab() {
		return mSelectedTab;
	}

	public boolean isExpanded() {
		return mIsExpanded;
	}

	private void initTabs() {
		View cat1 = findViewById(R.id.cat_1);
		View cat2 = findViewById(R.id.cat_2);
		View cat3 = findViewById(R.id.cat_3);

		cat1.setOnClickListener(mOnTabClickListener);
		cat2.setOnClickListener(mOnTabClickListener);
		cat3.setOnClickListener(mOnTabClickListener);
		initTabsColor();
		initCategories();
	}

	private void initTabsColor() {
		View cat1 = findViewById(R.id.cat_1);
		View cat2 = findViewById(R.id.cat_2);
		View cat3 = findViewById(R.id.cat_3);

		switch (mSelectedTab) {
			case 0:
				cat1.setBackgroundColor(getResources().getColor(R.color.geo_orange_light));
				cat2.setBackgroundResource(R.drawable.ic_geo_tab_unselected_bg);
				cat3.setBackgroundResource(R.drawable.ic_geo_tab_unselected_bg);
				break;
			case 1:
				cat1.setBackgroundResource(R.drawable.ic_geo_tab_unselected_bg);
				cat2.setBackgroundColor(getResources().getColor(R.color.geo_purple_light));
				cat3.setBackgroundResource(R.drawable.ic_geo_tab_unselected_bg);
				break;
			case 2:
				cat1.setBackgroundResource(R.drawable.ic_geo_tab_unselected_bg);
				cat2.setBackgroundResource(R.drawable.ic_geo_tab_unselected_bg);
				cat3.setBackgroundColor(getResources().getColor(R.color.geo_green_light));
				break;
		}
	}

	private void initCategories() {
		GridView catGrid = (GridView) findViewById(R.id.categories_grid);
		CategoryAdapter adapter = (CategoryAdapter) catGrid.getAdapter();
		if (adapter == null) {
			adapter = new CategoryAdapter(getContext());
			catGrid.setAdapter(adapter);
			catGrid.setOnItemClickListener(mOnCategoryClickListener);
		}
		adapter.clear();

		adapter.setSelectedTab(mSelectedTab);
		adapter.addAll(mCategories.get(mSelectedTab));
		adapter.notifyDataSetChanged();

	}

	private void initColors() {
		findViewById(R.id.search).setBackgroundColor(ColorsHelper.getColorByTab(getContext(), true, mSelectedTab));
		findViewById(R.id.title).setBackgroundColor(ColorsHelper.getColorByTab(getContext(), false, mSelectedTab));
		findViewById(R.id.categories_scroll).setBackgroundColor(ColorsHelper.getColorByTab(getContext(), false, mSelectedTab));
	}


	private OnClickListener mOnSearchClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (mCategoriesInterface != null) {
				mCategoriesInterface.onSearchClicked();
			}
		}
	};

	private OnClickListener mOnTabClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			boolean updated = false;
			switch (v.getId()) {
				case R.id.cat_1:
					if (mSelectedTab != 0) {
						mSelectedTab = 0;
						updated = true;
					}
					break;
				case R.id.cat_2:
					if (mSelectedTab != 1) {
						mSelectedTab = 1;
						updated = true;
					}
					break;
				case R.id.cat_3:
					if (mSelectedTab != 2) {
						mSelectedTab = 2;
						updated = true;
					}
					break;
			}
			if (updated) {
				initColors();
				initTabsColor();
				initCategories();
				notifyUpdatedCategories();
			}
		}
	};


	public void notifyUpdatedCategories() {
		if (mCategoriesInterface != null) {
			List<Category> selected = new ArrayList<Category>();
			for (Category c : mCategories.get(mSelectedTab)) {
				if (c.isSelected()) {
					selected.add(c);
				}
			}
			if (selected.size() == 0) {
				mCategoriesInterface.onCategoriesChanged(mRootCategories.get(mSelectedTab));
			} else {
				mCategoriesInterface.onCategoriesChanged(selected, mRootCategories.get(mSelectedTab));
			}
			mCategoriesInterface.onTabChanged(mSelectedTab);

		}
	}

	private AdapterView.OnItemClickListener mOnCategoryClickListener = new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			GridView catGrid = (GridView) findViewById(R.id.categories_grid);
			CategoryAdapter adapter = (CategoryAdapter) catGrid.getAdapter();
			Category c = adapter.getItem(position);
			c.changeSelected();
			if (mChangedCats.contains(c)) {
				mChangedCats.remove(c);
			} else {
				mChangedCats.add(c);
			}
			adapter.notifyDataSetChanged();
		}
	};

	private OnClickListener mExpandListener = new OnClickListener() {
		@Override
		public void onClick(View v) {

			if (mIsExpanded) {
				((TextView) findViewById(R.id.title)).setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_arrow_up, 0, 0, 0);
				collapse();
				mIsExpanded = false;
				mChangedCats.clear();
				notifyUpdatedCategories();
			} else {
				((TextView) findViewById(R.id.title)).setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_arrow_down, 0, 0, 0);
				expand();
				for (int i = 0; i < 3; i++) {
					mIsExpanded = true;
				}
			}
		}
	};

	public void collapseAndCancel() {

		((TextView) findViewById(R.id.title)).setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_arrow_up, 0, 0, 0);

		for (Category c : mChangedCats) {
			c.changeSelected();
		}
		mChangedCats.clear();
		mIsExpanded = false;
		initCategories();
		collapse();
	}

	private void expand() {

		final View v = findViewById(R.id.categories_scroll);
		int topPartHeight = findViewById(R.id.top_container).getHeight();
		int topPartMargin = ((LayoutParams) findViewById(R.id.top_container).getLayoutParams()).topMargin;
		int tabsHeight = findViewById(R.id.tabs).getHeight();
		final int targetHeight = getHeight() - topPartHeight - tabsHeight - topPartMargin;
		//final int targetHeight = v.getMeasuredHeight() + getMeasuredHeight();
		v.getLayoutParams().height = 0;
		v.setVisibility(View.VISIBLE);
		Animation a = new Animation() {
			@Override
			protected void applyTransformation(float interpolatedTime, Transformation t) {
				v.getLayoutParams().height = interpolatedTime == 1 ? LayoutParams.WRAP_CONTENT : (int) (targetHeight * interpolatedTime);
				v.requestLayout();
				v.getParent().requestLayout();
			}

			@Override
			public boolean willChangeBounds() {
				return true;
			}
		};

		// 0.5dp/ms
		a.setDuration((int) (targetHeight * 2 / v.getContext().getResources().getDisplayMetrics().density));

		v.startAnimation(a);
	}

	private void collapse() {
		final View v = findViewById(R.id.categories_scroll);
		final int initialHeight = v.getMeasuredHeight();

		Animation a = new Animation() {
			@Override
			protected void applyTransformation(float interpolatedTime, Transformation t) {
				if (interpolatedTime == 1) {
					v.setVisibility(View.GONE);
				} else {
					v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
					v.requestLayout();
					v.getParent().requestLayout();
				}
			}

			@Override
			public boolean willChangeBounds() {
				return true;
			}
		};

		// 0.5dp/ms
		a.setDuration((int) (initialHeight * 2 / v.getContext().getResources().getDisplayMetrics().density));

		a.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				v.setVisibility(GONE);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}
		});
		v.startAnimation(a);
	}

	public interface CategoriesInterface {

		void onTabChanged(int tabID);

		void onCategoriesChanged(List<Category> selectedCategories, int root);

		void onCategoriesChanged(int root);

		void onSearchClicked();

	}
}
