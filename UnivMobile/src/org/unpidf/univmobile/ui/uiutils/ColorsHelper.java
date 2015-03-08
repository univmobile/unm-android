package org.unpidf.univmobile.ui.uiutils;

import android.content.Context;

import org.unpidf.univmobile.R;

/**
 * Created by rviewniverse on 2015-02-11.
 */
public class ColorsHelper {

	public static int getColorByTab(Context c, boolean dark, int tab) {
		switch (tab) {
			case 0:
				if (dark) {
					return c.getResources().getColor(R.color.geo_orange_dark);
				} else {
					return c.getResources().getColor(R.color.geo_orange_light);
				}
			case 1:
				if (dark) {
					return c.getResources().getColor(R.color.geo_purple_dark);
				} else {
					return c.getResources().getColor(R.color.geo_purple_light);
				}
			default:
			case 2:
				if (dark) {
					return c.getResources().getColor(R.color.geo_green_dark);
				} else {
					return c.getResources().getColor(R.color.geo_green_light);
				}
		}

	}

	public static String getColorCodeByTab(Context c, int tab) {
		switch (tab) {
			case 0:
				return Integer.toHexString(c.getResources().getColor(R.color.geo_orange_dark));
			case 1:
				return Integer.toHexString(c.getResources().getColor(R.color.geo_purple_dark));

			default:
			case 2:
				return Integer.toHexString(c.getResources().getColor(R.color.geo_green_dark));

		}
	}

	public static int getCircleByTab(Context c, int tab) {
		switch (tab) {
			case 0:
				return R.drawable.circle_orange;
			case 1:
				return R.drawable.circle_purple;
			default:
			case 2:
				return R.drawable.circle_green;

		}
	}


	public static int getCategoryItemBackgroundResource(int tab, boolean selected) {
		switch (tab) {
			case 0:
				if (selected)
					return R.color.geo_orange_dark;
				return R.drawable.selector_color_orange;
			case 1:
				if (selected)
					return R.color.geo_purple_dark;
				return R.drawable.selector_color_purple;
			case 2:
				if (selected)
					return R.color.geo_green_dark;
				return R.drawable.selector_color_green;
		}
		return R.drawable.selector_color_green;
	}

	public static int getBookmarksBackgroundResource(int tab) {
		switch (tab) {
			case 0:
				return R.drawable.box_orange;

			case 1:
				return R.drawable.box_purple;
			case 2:
				return R.drawable.box_green;
		}
		return R.drawable.box_orange;
	}

	public static int getAddCommentBackgroundResource(int tab) {
		switch (tab) {
			case 0:
				return R.drawable.box_comments_orange;

			case 1:
				return R.drawable.box_comments_purple;
			case 2:
				return R.drawable.box_comments_green;
		}
		return R.drawable.box_comments_orange;
	}

}
