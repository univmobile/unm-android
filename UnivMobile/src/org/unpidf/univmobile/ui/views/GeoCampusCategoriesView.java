package org.unpidf.univmobile.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import org.unpidf.univmobile.R;
import org.unpidf.univmobile.UnivMobileApp;
import org.unpidf.univmobile.data.entities.Category;
import org.unpidf.univmobile.data.operations.ReadCategoriesOperation;
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


	private DisplayImageOptions mOptions;

	private Map<Integer, List<Category>> mCategories = new HashMap<Integer, List<Category>>();
	private Map<Integer, Integer> mRootCategories = new HashMap<Integer, Integer>();

	private Map<Integer, List<LinearLayout>> mCategoriesLayout = new HashMap<Integer, List<LinearLayout>>();


	private Map<Integer, List<Category>> mOldCategoriesSelected = new HashMap<Integer, List<Category>>();
	private Map<Integer, List<Category>> mCategoriesSelected = new HashMap<Integer, List<Category>>();

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

		mOptions = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).imageScaleType(ImageScaleType.EXACTLY_STRETCHED).build();
		LayoutInflater.from(getContext()).inflate(R.layout.view_geo_campus_categories, this, true);

		//init fonts
		FontHelper helper = ((UnivMobileApp) getContext().getApplicationContext()).getFontHelper();
		helper.loadFont((android.widget.TextView) findViewById(R.id.title), FontHelper.FONT.EXO_REGULAR);

		findViewById(R.id.top_container).setOnClickListener(mExpandListener);

		findViewById(R.id.search).setOnClickListener(mOnSearchClickListener);
	}

	public void clear() {
		mCategoriesInterface = null;
		mOptions = null;
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


		mOldCategoriesSelected.put(0, new ArrayList<Category>());
		mOldCategoriesSelected.put(1, new ArrayList<Category>());
		mOldCategoriesSelected.put(2, new ArrayList<Category>());

		mCategoriesSelected.put(0, new ArrayList<Category>());
		mCategoriesSelected.put(1, new ArrayList<Category>());
		mCategoriesSelected.put(2, new ArrayList<Category>());

		mSelectedTab = selectedTab;
		initTabs();
		initColors();
	}

	public void setSelectedCategories(List<Category> categories, int tabId) {
		mCategoriesSelected.put(tabId, categories);
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
		List<Category> categories = mCategories.get(mSelectedTab);
		List<LinearLayout> layouts = mCategoriesLayout.get(mSelectedTab);

		LinearLayout container = (LinearLayout) findViewById(R.id.categories_container);
		container.removeAllViews();
		//if (mSelectedTab == 0) {
		if (layouts == null) {
			layouts = new ArrayList<LinearLayout>();
			if (categories != null && categories.size() > 0) {
				for (int i = 0; i < categories.size(); i += 3) {
					LinearLayout l = new LinearLayout(getContext());
					l.setOrientation(LinearLayout.HORIZONTAL);
					l.setWeightSum(3);

					initCategoryView(categories, i, l);
					initCategoryView(categories, i + 1, l);
					initCategoryView(categories, i + 2, l);

					LinearLayout.LayoutParams paramsContainer = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
					container.addView(l, paramsContainer);
					layouts.add(l);
				}
			}
			mCategoriesLayout.put(mSelectedTab, layouts);

		} else {
			for (LinearLayout l : layouts) {
				LinearLayout.LayoutParams paramsContainer = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
				container.addView(l, paramsContainer);
			}
		}
		//}
	}

	private void initCategoryView(List<Category> categories, int position, LinearLayout container) {
		if (categories.size() > position) {
			CategoryItemView item3 = new CategoryItemView(getContext());
			item3.setBackgroundResource(ColorsHelper.getCategoryItemBackgroundResource(mSelectedTab));
			item3.setOnClickListener(mOnCategoryClickListener);
			item3.setTag(categories.get(position));
			item3.setCategoryText(categories.get(position).getName());
			if (categories.get(position).getActiveIconUrl() != null && categories.get(position).getActiveIconUrl().length() > 0) {
				ImageLoader.getInstance().displayImage(ReadCategoriesOperation.CATEGORIES_IMAGE_URL + categories.get(position).getInactiveIconUrl(), item3.getImageView(), mOptions);
			}
			LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT, 1);
			container.addView(item3, params3);
		}
	}

	private boolean shouldSelected(Category cat, int tab) {
		if (cat == null) {
			return false;
		}
		for (Category c : mCategoriesSelected.get(tab)) {
			if (c.getId() == cat.getId()) {
				return true;
			}
		}
		return false;
	}

	private void initColors() {
		findViewById(R.id.search).setBackgroundColor(ColorsHelper.getColorByTab(getContext(), true, mSelectedTab));
		findViewById(R.id.title).setBackgroundColor(ColorsHelper.getColorByTab(getContext(), false, mSelectedTab));
		findViewById(R.id.categories_scroll).setBackgroundColor(ColorsHelper.getColorByTab(getContext(), false, mSelectedTab));
	}

	private boolean isCategoriesUpdated(List<Category> oldList, List<Category> newList) {
		if (!oldList.containsAll(newList)) {
			return true;
		}

		if (!newList.containsAll(oldList)) {
			return true;
		}
		return false;
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
			List<Category> selected = mCategoriesSelected.get(mSelectedTab);
			if (selected.size() == 0) {
				mCategoriesInterface.onCategoriesChanged(mRootCategories.get(mSelectedTab));
			} else {
				mCategoriesInterface.onCategoriesChanged(selected);
			}
			mCategoriesInterface.onTabChanged(mSelectedTab);

		}
	}

	private OnClickListener mOnCategoryClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			v.setSelected(!v.isSelected());
			List<Category> selectedCategories = mCategoriesSelected.get(mSelectedTab);

			if (selectedCategories.contains(v.getTag())) {
				selectedCategories.remove(v.getTag());
			} else {
				selectedCategories.add((Category) v.getTag());
			}
		}
	};

	private OnClickListener mExpandListener = new OnClickListener() {
		@Override
		public void onClick(View v) {

			if (mIsExpanded) {
				((TextView) findViewById(R.id.title)).setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_arrow_up, 0, 0, 0);
				collapse();
				mIsExpanded = false;

				notifyUpdatedCategories();


			} else {
				((TextView) findViewById(R.id.title)).setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_arrow_down, 0, 0, 0);
				expand();
				for (int i = 0; i < 3; i++) {
					mIsExpanded = true;
					mOldCategoriesSelected.get(i).clear();
					if (mCategoriesSelected.get(i) != null && mCategoriesSelected.get(i).size() > 0) {
						mOldCategoriesSelected.get(i).addAll(mCategoriesSelected.get(i));
					} else {
						mOldCategoriesSelected.get(i).addAll(mCategories.get(i));
					}
				}
			}
		}
	};

	public void collapseAndCancel() {

		((TextView) findViewById(R.id.title)).setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_arrow_up, 0, 0, 0);


		for (int i = 0; i < 3; i++) {
			mCategoriesSelected.get(i).clear();
			if (mCategories.get(i).size() != mOldCategoriesSelected.get(i).size()) {
				mCategoriesSelected.get(i).addAll(mOldCategoriesSelected.get(i));
			}
			List<LinearLayout> layouts = mCategoriesLayout.get(i);
			if (layouts != null) {
				for (LinearLayout l : layouts) {
					for (int j = 0; j < l.getChildCount(); j++) {
						View child = l.getChildAt(j);
						child.setSelected(shouldSelected((Category) child.getTag(), i));
					}

				}
			}
		}


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

		void onCategoriesChanged(List<Category> selectedCategories);

		void onCategoriesChanged(int root);

		void onSearchClicked();

	}
}
