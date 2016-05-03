package com.unfoldlabs.redgreen.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.unfoldlabs.redgreen.R;
import com.unfoldlabs.redgreen.model.NavigationDrawerItems;

import java.util.List;

public class DrawerItemCustomAdapter extends BaseAdapter {

	private Context mContext;
	private List<NavigationDrawerItems> data = null;

	/**
	 * Constructor for DrawerItemCustomAdapter.
	 */


	private int mSelectedItem;

	public int getmSelectedItem() {
		return mSelectedItem;
	}
	public void setmSelectedItem(int mSelectedItem) {
		this.mSelectedItem = mSelectedItem;

	}


	public DrawerItemCustomAdapter(Context mContext, int layoutResourceId,
								   List<NavigationDrawerItems> items) {
		this.mContext = mContext;
		this.data = items;
	}

	/**
	 * To show the items list in navigational drawer
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View listItem = convertView;
		LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
		listItem = inflater.inflate(R.layout.test_drawer_list_tem, parent,
				false);
		LinearLayout test_drawer_list_layout = (LinearLayout) listItem.findViewById(R.id.test_drawer_layout);
		TextView textViewName = (TextView) listItem
				.findViewById(R.id.textViewName);
		if (position == 0) {
			textViewName.setTextColor(mContext.getResources().getColor(
					R.color.navigation_background_color));
			test_drawer_list_layout.setBackgroundColor(mContext.getResources().getColor(
					R.color.navigation_text_color));
			textViewName.setText("SETTINGS");
			textViewName.setClickable(false);
			test_drawer_list_layout.setGravity(Gravity.CENTER);
			test_drawer_list_layout.setPadding(10, 0, 0, 0);
		} else {
			if (position == getmSelectedItem()) {
				textViewName.setTextColor(mContext.getResources().getColor(R.color.navigation_background_color));
			}else{
				textViewName.setTextColor(mContext.getResources().getColor(R.color.navigation_text_color));
			}
			String item = data.get(position).getItemName();
			textViewName.setText(item);
		}
		return listItem;
	}

	/**
	 * to get data size
	 */
	@Override
	public int getCount() {
		return data.size();
	}

	/**
	 * to get position of data
	 */
	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	/**
	 * to get item position
	 */
	@Override
	public long getItemId(int position) {
		return position;
	}

}
