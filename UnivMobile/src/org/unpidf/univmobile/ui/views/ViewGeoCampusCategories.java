package org.unpidf.univmobile.ui.views;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.unpidf.univmobile.R;
import org.unpidf.univmobile.UnivMobileApp;
import org.unpidf.univmobile.data.entities.Category;
import org.unpidf.univmobile.ui.uiutils.FontHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by Rokas on 2015-02-07.
 */
public class ViewGeoCampusCategories extends RelativeLayout {

	private static enum CATEGORY {
		FIRST, SECOND, THIRD
	}

	private CATEGORY mSelectedCategory = CATEGORY.FIRST;

	private boolean mIsExpanded = false;
	private List<Integer> mSelectedIcons = new ArrayList<Integer>();

	public ViewGeoCampusCategories(Context context) {
		super(context);
		init();
	}

	public ViewGeoCampusCategories(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public ViewGeoCampusCategories(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		LayoutInflater.from(getContext()).inflate(R.layout.view_geo_campus_categories, this, true);

		//init fonts
		FontHelper helper = ((UnivMobileApp) getContext().getApplicationContext()).getFontHelper();
		helper.loadFont((android.widget.TextView) findViewById(R.id.title), FontHelper.FONT.EXO_REGULAR);

		findViewById(R.id.top_container).setOnClickListener(mExpandListener);

		initCategories();
		initColors();
		initTabs();
	}

	private void initTabs() {
		View cat1 = findViewById(R.id.cat_1);
		View cat2 = findViewById(R.id.cat_2);
		View cat3 = findViewById(R.id.cat_3);

		cat1.setOnClickListener(mOnTabClickListener);
		cat2.setOnClickListener(mOnTabClickListener);
		cat3.setOnClickListener(mOnTabClickListener);
		initTabsColor();
	}

	private void initTabsColor() {
		View cat1 = findViewById(R.id.cat_1);
		View cat2 = findViewById(R.id.cat_2);
		View cat3 = findViewById(R.id.cat_3);

		switch (mSelectedCategory) {
			case FIRST:
				cat1.setBackgroundColor(getResources().getColor(R.color.geo_orange_light));
				cat2.setBackgroundResource(R.drawable.ic_geo_tab_unselected_bg);
				cat3.setBackgroundResource(R.drawable.ic_geo_tab_unselected_bg);
				break;
			case SECOND:
				cat1.setBackgroundResource(R.drawable.ic_geo_tab_unselected_bg);
				cat2.setBackgroundColor(getResources().getColor(R.color.geo_purple_light));
				cat3.setBackgroundResource(R.drawable.ic_geo_tab_unselected_bg);
				break;
			case THIRD:
				cat1.setBackgroundResource(R.drawable.ic_geo_tab_unselected_bg);
				cat2.setBackgroundResource(R.drawable.ic_geo_tab_unselected_bg);
				cat3.setBackgroundColor(getResources().getColor(R.color.geo_green_light));
				break;
		}
	}

	private void initCategories() {
		List<Category> categories = new ArrayList<Category>();
		for (int i = 0; i < 20; i++) {
			Category c = new Category();
			c.id = i;
			categories.add(c);
		}

		LinearLayout container = (LinearLayout) findViewById(R.id.categories_container);
		container.removeAllViews();
		mSelectedIcons.clear();
		for (int i = 0; i < categories.size(); i += 3) {
			LinearLayout l = new LinearLayout(getContext());
			l.setOrientation(LinearLayout.HORIZONTAL);
			l.setWeightSum(3);

			CategoryItemView item1 = new CategoryItemView(getContext());
			item1.setOnClickListener(mOnCategoryClickListener);
			item1.setTag(categories.get(i).id);
			item1.setCategoryText("Bibliothèque Universitaire");
			item1.setCategoryIcon(R.drawable.ic_geo_category_1);
			LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT, 1);
			l.addView(item1, params1);

			if (categories.size() > i + 1) {
				CategoryItemView item2 = new CategoryItemView(getContext());
				item2.setOnClickListener(mOnCategoryClickListener);
				item2.setTag(categories.get(i + 1).id);
				item2.setCategoryText("Bibliothèque Universitaire");
				item1.setCategoryIcon(R.drawable.ic_geo_category_1);
				LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT, 1);
				l.addView(item2, params2);
			}

			if (categories.size() > i + 2) {
				CategoryItemView item3 = new CategoryItemView(getContext());
				item3.setOnClickListener(mOnCategoryClickListener);
				item3.setTag(categories.get(i + 2).id);
				item3.setCategoryText("Bibliothèque Universitaire");
				item1.setCategoryIcon(R.drawable.ic_geo_category_1);
				LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT, 1);
				l.addView(item3, params3);
			}

			LinearLayout.LayoutParams paramsContainer = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			container.addView(l, paramsContainer);

		}
	}

	private void initColors() {
		findViewById(R.id.search).setBackgroundColor(getBackgroundColor(true));
		findViewById(R.id.title).setBackgroundColor(getBackgroundColor(false));
		findViewById(R.id.categories_scroll).setBackgroundColor(getBackgroundColor(false));
	}

	private int getBackgroundColor(boolean selected) {
		switch (mSelectedCategory) {
			case FIRST:
				if (selected) {
					return getResources().getColor(R.color.geo_orange_dark);
				} else {
					return getResources().getColor(R.color.geo_orange_light);
				}
			case SECOND:
				if (selected) {
					return getResources().getColor(R.color.geo_purple_dark);
				} else {
					return getResources().getColor(R.color.geo_purple_light);
				}
			default:
			case THIRD:
				if (selected) {
					return getResources().getColor(R.color.geo_green_dark);
				} else {
					return getResources().getColor(R.color.geo_green_light);
				}
		}

	}

	private OnClickListener mOnTabClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.cat_1:
					mSelectedCategory = CATEGORY.FIRST;
					break;
				case R.id.cat_2:
					mSelectedCategory = CATEGORY.SECOND;
					break;
				case R.id.cat_3:
					mSelectedCategory = CATEGORY.THIRD;
					break;
			}
			initColors();
			initTabsColor();
			initCategories();
		}
	};


	private OnClickListener mOnCategoryClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {

			if (mSelectedIcons.contains(v.getTag())) {
				((CategoryItemView) v).setBackgroundColor(getBackgroundColor(false));
				mSelectedIcons.remove(v.getTag());
			} else {
				mSelectedIcons.add((Integer) v.getTag());
				((CategoryItemView) v).setBackgroundColor(getBackgroundColor(true));
			}
		}
	};

	private OnClickListener mExpandListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			View content = findViewById(R.id.categories_scroll);
			if (mIsExpanded) {
				((TextView) findViewById(R.id.title)).setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_up, 0);
				collapse(content);
				mIsExpanded = false;

			} else {
				((TextView) findViewById(R.id.title)).setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_down, 0);
				expand(content);
				mIsExpanded = true;
			}
		}
	};

	private void expand(final View v) {


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

	private void collapse(final View v) {
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
}
