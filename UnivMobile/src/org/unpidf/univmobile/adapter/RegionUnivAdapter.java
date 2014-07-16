package org.unpidf.univmobile.adapter;

import java.util.List;

import org.unpidf.univmobile.R;
import org.unpidf.univmobile.dao.Region;
import org.unpidf.univmobile.dao.University;
import org.unpidf.univmobile.wrapper.RegionUnivWrapper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class RegionUnivAdapter extends BaseAdapter {

	private List<? extends Object> items;
	private LayoutInflater inflater;
	private RegionUnivWrapper wrapper = null;

	public RegionUnivAdapter(Context context, List<? extends Object> items) {
		inflater = LayoutInflater.from(context);
		this.items = items;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		if (row == null) {
			row = inflater.inflate(R.layout.item_region_univ, parent, false);
			wrapper = new RegionUnivWrapper(row);
			row.setTag(wrapper);
		} else {
			wrapper = (RegionUnivWrapper) row.getTag();
		}
		
		Object object = items.get(position);
		if(object instanceof Region){
			wrapper.getTextView().setText(((Region)object).getLabel());
		}else{
			wrapper.getTextView().setText(((University)object).getTitle());
		}
		return (row);
	}

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public Object getItemAtPosition(int position) {
		return items.get(position);
	}

	@Override
	public Object getItem(int position) {
		return items.get(position);
	}
}