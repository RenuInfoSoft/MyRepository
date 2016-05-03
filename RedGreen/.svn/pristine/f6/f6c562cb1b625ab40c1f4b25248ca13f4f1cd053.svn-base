package com.unfoldlabs.redgreen.adapter;

import android.content.Context;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.unfoldlabs.redgreen.R;
import com.unfoldlabs.redgreen.model.AppsListItem;

import java.util.ArrayList;
import java.util.List;

public class AppsListAdapter extends BaseAdapter {

	private List<AppsListItem> mFilteredItems;
	private Context mContext;

	public enum SortBy {
		APP_NAME, CACHE_SIZE, APP_DESC, CACHE_DESC
	}

	private class ViewHolder {
		ImageView image;
		TextView name, size;
	}

	public AppsListAdapter(Context context) {
		mContext = context;
		mFilteredItems = new ArrayList<>();

	}

	@Override
	public int getCount() {
		return mFilteredItems.size();
	}

	@Override
	public AppsListItem getItem(int i) {
		return mFilteredItems.get(i);
	}

	@Override
	public long getItemId(int i) {
		return i;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	/**
	 * To show the cache list. In that list each item has app icon, app name and
	 * cache size.
	 */
	@Override
	public View getView(final int i, View convertView, ViewGroup viewParent) {
		final AppsListItem item;

		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.list_item, viewParent, false);
		}

		ViewHolder viewHolder = (ViewHolder) convertView.getTag();

		if (viewHolder == null) {
			viewHolder = new ViewHolder();
			viewHolder.image = (ImageView) convertView
					.findViewById(R.id.app_icon);
			viewHolder.name = (TextView) convertView
					.findViewById(R.id.app_name);
			viewHolder.size = (TextView) convertView
					.findViewById(R.id.app_size);
			convertView.setTag(viewHolder);
		}

		item = getItem(i);
		/**
		 * setting app image , label and cache size to each list item
		 */
		viewHolder.image.setImageDrawable(item.getApplicationIcon());
		viewHolder.name.setText(item.getApplicationName());
		viewHolder.size.setText(Formatter.formatShortFileSize(mContext,
				item.getCacheSize()));
		item.getPackageName();
		return convertView;
	}

	/**
	 * if the cache list is not empty the sortAndFilter method will be called to
	 * sort and filter list as user wants.
	 */
	public void setItems(List<AppsListItem> items) {
		mFilteredItems = new ArrayList<>(items);
	}
}