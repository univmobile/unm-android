package org.unpidf.univmobile.adapter;

import java.util.ArrayList;
import java.util.List;

import org.unpidf.univmobile.R;
import org.unpidf.univmobile.dao.Comment;
import org.unpidf.univmobile.wrapper.CommentWrapper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class CommentPoiAdapter extends BaseAdapter {

	private List<Comment> items;
	private LayoutInflater inflater;
	private CommentWrapper wrapper = null;

	public CommentPoiAdapter(Context context, List<Comment> items) {
		inflater = LayoutInflater.from(context);
		this.items = new ArrayList<Comment>(items);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		if (row == null) {
			row = inflater.inflate(R.layout.item_comment, parent, false);
			wrapper = new CommentWrapper(row);
			row.setTag(wrapper);
		} else {
			wrapper = (CommentWrapper) row.getTag();
		}
		wrapper.getPseudoView().setText(items.get(position).getDisplayName());
		wrapper.getCommentView().setText(items.get(position).getText().trim());
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

	public void setList(List<Comment> itemsToDo) {
		this.items = new ArrayList<Comment>(itemsToDo);
		notifyDataSetChanged();
	}
}