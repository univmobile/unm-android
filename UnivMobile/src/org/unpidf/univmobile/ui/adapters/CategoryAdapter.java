package org.unpidf.univmobile.ui.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.squareup.picasso.Picasso;

import org.unpidf.univmobile.data.entities.Category;
import org.unpidf.univmobile.data.operations.ReadCategoriesOperation;
import org.unpidf.univmobile.ui.uiutils.ColorsHelper;
import org.unpidf.univmobile.ui.views.CategoryItemView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rokas on 2015-03-08.
 */
public class CategoryAdapter extends ArrayAdapter<Category> {

	private int mSelectedTab;

	public CategoryAdapter(Context context) {
		super(context, 0, new ArrayList<Category>());
	}

	public void setSelectedTab(int tab) {
		mSelectedTab = tab;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = new CategoryItemView(getContext());
		}
		CategoryItemView view = (CategoryItemView) convertView;
		view.setBackgroundResource(ColorsHelper.getCategoryItemBackgroundResource(mSelectedTab, getItem(position).isSelected()));
		view.setCategoryText(getItem(position).getName());
		String url = getItem(position).getActiveIconUrl();
		view.getImageView().setImageDrawable(null);
		Picasso.with(getContext()).cancelRequest(view.getImageView());
		if (url != null && url.length() > 0) {
			Picasso.with(getContext()).load(ReadCategoriesOperation.CATEGORIES_IMAGE_URL + url).into(view.getImageView());
		}


		return view;
	}
}
