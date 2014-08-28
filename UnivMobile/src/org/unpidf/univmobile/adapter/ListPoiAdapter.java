package org.unpidf.univmobile.adapter;

import java.util.List;

import org.unpidf.univmobile.R;
import org.unpidf.univmobile.custom.AnimatedExpandableListView.AnimatedExpandableListAdapter;
import org.unpidf.univmobile.dao.Poi;
import org.unpidf.univmobile.dao.PoiGroup;
import org.unpidf.univmobile.wrapper.PoiGroupWrapper;
import org.unpidf.univmobile.wrapper.PoiWrapper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ListPoiAdapter extends AnimatedExpandableListAdapter {

	private LayoutInflater inflater;
	private List<PoiGroup> items;
	private PoiGroupWrapper wrapperParent;
	private PoiWrapper wrapper;

	public ListPoiAdapter(Context context, List<PoiGroup> list) {
		this.inflater = LayoutInflater.from(context);
		this.items = list;
	}

	public void setList(List<PoiGroup> items) {
		this.items = items;
		notifyDataSetChanged();
	}

	@Override
	public Poi getChild(int groupPosition, int childPosition) {
		return items.get(groupPosition).getListPois().get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getRealChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		View row = convertView;
		if (row == null) {
			row = inflater.inflate(R.layout.item_poi, parent, false);
			wrapper = new PoiWrapper(row);
			row.setTag(wrapper);
		} else {
			wrapper = (PoiWrapper) row.getTag();
		}
		wrapper.getTextView().setText(getChild(groupPosition, childPosition).getTitle());
		return row;
	}

	@Override
	public int getRealChildrenCount(int groupPosition) {
		return items.get(groupPosition).getListPois().size();
	}

	@Override
	public PoiGroup getGroup(int groupPosition) {
		return items.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return items.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		View row = convertView;
		if (row == null) {
			row = inflater.inflate(R.layout.item_poi_parent, parent, false);
			wrapperParent = new PoiGroupWrapper(row);
			row.setTag(wrapperParent);
		} else {
			wrapperParent = (PoiGroupWrapper) row.getTag();
		}
		wrapperParent.getTextView().setText(getGroup(groupPosition).getGroupLabel());
		return row;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isChildSelectable(int arg0, int arg1) {
		return true;
	}

}