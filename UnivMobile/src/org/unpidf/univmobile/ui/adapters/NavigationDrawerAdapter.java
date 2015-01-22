package org.unpidf.univmobile.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.unpidf.univmobile.R;
import org.unpidf.univmobile.data.entities.NavigationMenu;
import org.unpidf.univmobile.ui.widgets.AnimatedExpandableListView;

import java.util.List;

/**
 * Created by Rokas on 2015-01-20.
 */
public class NavigationDrawerAdapter extends AnimatedExpandableListView.AnimatedExpandableListAdapter {

	private Context mContext;
	private List<NavigationMenu> mContent;

	public NavigationDrawerAdapter(Context context, List<NavigationMenu> content) {
		mContext = context;
		mContent = content;
	}

	@Override
	public int getGroupCount() {
		return mContent.size();
	}

	@Override
	public int getRealChildrenCount(int groupPosition) {
		return mContent.get(groupPosition).getChilds().size();
	}

	@Override
	public NavigationMenu getGroup(int groupPosition) {
		return mContent.get(groupPosition);
	}

	@Override
	public NavigationMenu getChild(int groupPosition, int childPosition) {
		return mContent.get(groupPosition).getChilds().get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return mContent.get(groupPosition).getId();
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return mContent.get(groupPosition).getChilds().get(childPosition).getId();
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.view_menu_group, null);
		}
		TextView item = (TextView) convertView.findViewById(R.id.menu_title);
		item.setText(getGroup(groupPosition).getName());

		ImageView image = (ImageView) convertView.findViewById(R.id.menu_icon);
		image.setBackgroundResource(getGroup(groupPosition).getImageId());


		View bottom_line = convertView.findViewById(R.id.bottom_line);
		bottom_line.setBackgroundResource(getGroupColor(groupPosition));

		if (isExpanded) {
			bottom_line.setVisibility(View.VISIBLE);
		} else {
			bottom_line.setVisibility(View.INVISIBLE);
		}
		return convertView;
	}

	private int getGroupColor(int groupPosition) {
		switch (groupPosition) {
			case 0:
				return R.color.group_1_color;
			case 1:
				return R.color.group_2_color;
			case 2:
				return R.color.group_3_color;
			case 3:
				return R.color.group_4_color;
		}
		return R.color.group_1_color;
	}

	@Override
	public View getRealChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.view_menu_child, null);
		}
		TextView item = (TextView) convertView.findViewById(R.id.menu_child_name);
		item.setText(getChild(groupPosition, childPosition).getName());

//
//		View separator = convertView.findViewById(R.id.separator);
//		if( getChildrenCount(groupPosition) > 0) {
//			if (childPosition == getChildrenCount(groupPosition) - 1) {
//				separator.setVisibility(View.INVISIBLE);
//			} else {
//				separator.setVisibility(View.VISIBLE);
//			}
//		}
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
}
