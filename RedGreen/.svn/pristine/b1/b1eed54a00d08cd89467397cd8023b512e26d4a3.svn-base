package com.unfoldlabs.redgreen.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.unfoldlabs.redgreen.R;
import com.unfoldlabs.redgreen.interfaces.TotalListener;
import com.unfoldlabs.redgreen.model.ListItem;
import com.unfoldlabs.redgreen.utilty.Utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

	private Context _context;
	private List<String> _listDataHeader;
	private HashMap<String, List<ListItem>> runningAppsListDataChild1;
	private TotalListener mListener;
	private static int sumFinal;
	private ArrayList<ArrayList<Boolean>> selectedChildCheckBoxStates = new ArrayList<>();
	private ArrayList<Boolean> selectedParentCheckBoxesState = new ArrayList<Boolean>();
	private SparseBooleanArray cleanMemoryArray = new SparseBooleanArray();
	private SparseBooleanArray memoryArray = new SparseBooleanArray();
	private SparseBooleanArray historyArray = new SparseBooleanArray();
	boolean historyState;
	private int tempTotalSize;

	/**
	 * On opening this activity the check boxes in checked state.
	 *
	 * @param sumFinal
	 */
	public ExpandableListAdapter(Context context, List<String> listDataHCleanBrowserListModel,
								 HashMap<String, List<ListItem>> runningAppsListDataChild1, int sumFinal) {
		this._context = context;
		this._listDataHeader = listDataHCleanBrowserListModel;
		this.runningAppsListDataChild1 = runningAppsListDataChild1;
		ExpandableListAdapter.sumFinal = sumFinal;
		tempTotalSize = sumFinal;
		initCheckStates(true);

	}

	/**
	 * All the check boxes checked/un checked state done.
	 */
	private void initCheckStates(boolean defaultState) {
		for (int i = 0; i < runningAppsListDataChild1.size(); i++) {
			selectedParentCheckBoxesState.add(i, defaultState);
			ArrayList<Boolean> childStates = new ArrayList<>();
			if(null != runningAppsListDataChild1.get(_listDataHeader.get(i)))
				if (runningAppsListDataChild1.get(_listDataHeader.get(i)).size() != 0){
					for (int j = 0; j < runningAppsListDataChild1.get(_listDataHeader.get(i)).size(); j++) {
						childStates.add(defaultState);
					}
				}
			selectedChildCheckBoxStates.add(i, childStates);
		}

	}

	/**
	 * to get the number of children count
	 */
	@Override
	public int getChildrenCount(int groupPosition) {
		if (runningAppsListDataChild1.get(_listDataHeader.get(groupPosition)) == null) {
			return 0;
		}
		else
			return runningAppsListDataChild1.get(_listDataHeader.get(groupPosition)).size();
	}

	/**
	 * to get the number of group items count
	 */
	@Override
	public int getGroupCount() {
		return _listDataHeader.size();
	}

	/**
	 * to get the child and group position
	 */
	@Override
	public Object getChild(int groupPosition, int childPosititon) {
		if (runningAppsListDataChild1.get(_listDataHeader.get(groupPosition)).get(childPosititon) == null) {
			return 0;
		}
		else
			return runningAppsListDataChild1.get(_listDataHeader.get(groupPosition)).get(childPosititon);
	}

	/**
	 * to get the child position in group
	 */
	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	/**
	 * to get the group position for child
	 */
	@Override
	public Object getGroup(int groupPosition) {
		return runningAppsListDataChild1.get(groupPosition);
	}

	/**
	 * to get group position
	 */
	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	/**
	 * To show the expandable child lists in clean memory module. i.e., For
	 * background running apps, memory RAM boost and clear browser history the
	 * items will be shown in child list
	 */
	@SuppressLint("NewApi")
	@Override
	public View getChildView(final int groupPosition, final int childPosition,
							 boolean isLastChild, View convertView, final ViewGroup parent) {

		final ListItem listItem = (ListItem) getChild(groupPosition, childPosition);

		ViewHolder holder;

		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) _context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(
					R.layout.clean_memory_child_layout, parent, false);
			holder = new ViewHolder();
			holder.txtTitle = (TextView) convertView
					.findViewById(R.id.child_tv_appname);
			holder.imgIcon = (ImageView) convertView
					.findViewById(R.id.child_icon);
			holder.txtUrl = (TextView) convertView
					.findViewById(R.id.child_tv_url);
			holder.childCheckBox = (CheckBox) convertView
					.findViewById(R.id.child_checkbox);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (groupPosition == 0) {
			holder.txtUrl.setVisibility(View.GONE);
			holder.txtTitle.setText(listItem.getApptitle());
			holder.imgIcon.setImageDrawable(listItem.getAppIcon());
			holder.txtUrl.setText("");
			holder.childCheckBox.setVisibility(View.VISIBLE);
		}
		if (groupPosition == 1) {
			holder.txtUrl.setVisibility(View.VISIBLE);
			holder.txtTitle.setText(listItem.getInfo());
			holder.imgIcon.setImageDrawable(listItem.getIcon());
			holder.txtUrl.setText("" + listItem.getId() + "MB");
			holder.childCheckBox.setVisibility(View.VISIBLE);
		}
		if (groupPosition == 2) {
			holder.txtUrl.setVisibility(View.VISIBLE);
			holder.txtTitle.setText(listItem.getHistoryTitle());
			holder.txtUrl.setText(listItem.getHistoryUrl());
			holder.imgIcon.setImageBitmap(listItem.getHistoryBitmap());
			holder.childCheckBox.setVisibility(View.GONE);

		}

		try {

			if (selectedChildCheckBoxStates.size() <= groupPosition) {
				ArrayList<Boolean> childState = new ArrayList<>();
				for (int i = 0; i < runningAppsListDataChild1.get(
						_listDataHeader.get(groupPosition)).size(); i++) {
					if (childState.size() > childPosition)
						childState.add(childPosition, false);
					else
						childState.add(false);
				}
				if (selectedChildCheckBoxStates.size() > groupPosition) {
					selectedChildCheckBoxStates.add(groupPosition, childState);
				} else
					selectedChildCheckBoxStates.add(childState);
			} else {
				if (selectedChildCheckBoxStates.size() > 0)
					holder.childCheckBox.setChecked(selectedChildCheckBoxStates
							.get(groupPosition).get(childPosition));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		/**
		 * To effect the group check box selection/un selection for making
		 * changes in children check boxes.
		 */
		holder.childCheckBox.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				boolean state = selectedChildCheckBoxStates.get(groupPosition)
						.get(childPosition);

				selectedChildCheckBoxStates.get(groupPosition).remove(
						childPosition);
				selectedChildCheckBoxStates.get(groupPosition).add(
						childPosition, state ? false : true);
				notifyDataSetChanged();

				boolean isCheck = ((CompoundButton) v).isChecked();
				showTotal(groupPosition, listItem, isCheck);
				if (null != mListener)
					mListener.GroupItemSelection(cleanMemoryArray, memoryArray, historyArray);
			}

		});

		return convertView;
	}

	/**
	 * To show the expandable group items in Clean memory module. i.e.,
	 * Background running apps, RAM Boost and Browser history.
	 */
	@Override
	public View getGroupView(final int groupPosition, final boolean isExpanded, View convertView, ViewGroup parent) {

		final ViewHolder holderGroup;

		if (convertView == null) {
			holderGroup = new ViewHolder();
			LayoutInflater infalInflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.clean_memory_group_layout, parent, false);

			holderGroup.groupNameCheckBox = (CheckBox) convertView.findViewById(R.id.group_chk_box);
			holderGroup.lblListHeader = (TextView) convertView.findViewById(R.id.expListHeader);
			holderGroup.childCheckedSizeTextview = (TextView) convertView.findViewById(R.id.childchecked_size);
			convertView.setTag(holderGroup);
		} else {
			holderGroup = (ViewHolder) convertView.getTag();
		}
		if (groupPosition == 0) {
			holderGroup.childCheckedSizeTextview.setVisibility(View.VISIBLE);
			holderGroup.childCheckedSizeTextview.setText(
					"" + cleanMemoryArray.size() + "/" + runningAppsListDataChild1.get(_listDataHeader.get(0)).size());
		}

		if (groupPosition == 1) {
			holderGroup.childCheckedSizeTextview.setVisibility(View.VISIBLE);
			holderGroup.childCheckedSizeTextview.setText("" + sumFinal + " " + "MB");
			mListener.getUpdateSelectedRam(sumFinal);
		}
		if (groupPosition == 2) {
			holderGroup.childCheckedSizeTextview.setVisibility(View.INVISIBLE);
		}
		holderGroup.lblListHeader.setTypeface(null, Typeface.BOLD);
		try{
			holderGroup.lblListHeader.setText("" + _listDataHeader.get(groupPosition));
		}catch(Exception e){
		}
		holderGroup.lblListHeader.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				/**
				 * when click on group item Opens a dialog if respected child
				 * list is empty for the group items.
				 */
				if (getChildrenCount(groupPosition) == 0) {

					if (groupPosition == 0) {
						Utility.showAlertDialog(_context,
								_context.getResources().getString(R.string.background_apps_list_empty));
					}
					if (groupPosition == 1) {
						Utility.showAlertDialog(_context,
								_context.getResources().getString(R.string.ramboost_list_empty));
					}
					if (groupPosition == 2) {
						Utility.showAlertDialog(_context,
								_context.getResources().getString(R.string.browser_history_empty));
					}
				} else {
					/**
					 * Expands the child list if the child list is not empty
					 * when click on group item.
					 */
					mListener.expandGroupEvent(groupPosition, isExpanded);

				}

			}
		});
		if (selectedParentCheckBoxesState.size() <= groupPosition) {
			selectedParentCheckBoxesState.add(groupPosition, false);
		} else {
			if (getChildrenCount(groupPosition) <= 0) {
				/**
				 * if the child list is empty header check box is in un
				 * selection mode and it cannot be selected.
				 */
				holderGroup.groupNameCheckBox.setChecked(false);
			} else {
				holderGroup.groupNameCheckBox.setChecked(selectedParentCheckBoxesState.get(groupPosition));
			}
		}

		if (selectedChildCheckBoxStates.size() > 0)
			for (int i = 0; i < selectedChildCheckBoxStates.get(groupPosition).size(); i++) {
				boolean selectionItems = selectedChildCheckBoxStates.get(groupPosition).get(i);
				if (selectionItems) {
					if (groupPosition == 0)
						cleanMemoryArray.put(i, selectionItems);

					else if (groupPosition == 1)
						memoryArray.put(i, selectionItems);
					else if(groupPosition == 2)
						historyArray.put(i, selectionItems);

				} else {
					if (groupPosition == 0)
						cleanMemoryArray.delete(i);

					else if (groupPosition == 1)
						memoryArray.delete(i);
					else if(groupPosition == 2)
						historyArray.delete(i);
				}
				if (groupPosition == 2) {
					historyState = holderGroup.groupNameCheckBox.isChecked();
					if (null != mListener)
						mListener.setHistoryState(historyState);
				}

				if (null != mListener)
					mListener.GroupItemSelection(cleanMemoryArray, memoryArray, historyArray);
			}
		/**
		 * functionality for checkbox state according to header check box
		 */
		holderGroup.groupNameCheckBox.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				boolean state = selectedParentCheckBoxesState.get(groupPosition);
				selectedParentCheckBoxesState.remove(groupPosition);
				selectedParentCheckBoxesState.add(groupPosition, state ? false : true);
				for (int i = 0; i < runningAppsListDataChild1.get(_listDataHeader.get(groupPosition)).size(); i++) {
					selectedChildCheckBoxStates.get(groupPosition).remove(i);
					selectedChildCheckBoxStates.get(groupPosition).add(i, state ? false : true);
				}
				notifyDataSetChanged();

				boolean isCheck = ((CompoundButton) v).isChecked();
				if (groupPosition == 1) {
					showGroupTotal(groupPosition, isCheck);
				}
			}

		});

		return convertView;
	}


	public class ViewHolder {
		TextView txtTitle;
		TextView txtUrl;
		ImageView imgIcon;
		CheckBox groupNameCheckBox;
		TextView lblListHeader;
		CheckBox childCheckBox;
		TextView childCheckedSizeTextview;
	}

	private void showTotal(int groupPosition, ListItem listItem, boolean isCheck) {
		if (isCheck == true && groupPosition == 1) {
			sumFinal += listItem.getId();
		} else {
			sumFinal -= listItem.getId();
		}
	}

	private void showGroupTotal(int groupPosition, boolean isCheck) {
		if (isCheck == false && groupPosition  == 1) {
			sumFinal = 0;
		} else {
			sumFinal = tempTotalSize;
		}
	}

	public void resetCheckedValues(boolean b) {
		initCheckStates(false);
		notifyDataSetChanged();
	}

	public void resetGrpCheckboxCleanMemory(boolean b) {
		selectedParentCheckBoxesState.add(0, b);
		notifyDataSetChanged();
	}

	public void resetGrpCheckboxRamMemory(boolean b) {
		selectedParentCheckBoxesState.add(1, b);
		notifyDataSetChanged();
	}

	public void resetGrpCheckboxBrowserHistory(boolean b) {
		selectedParentCheckBoxesState.add(2, b);
		notifyDataSetChanged();
	}

	public void setListener(TotalListener mListener) {
		this.mListener = mListener;
	}
}