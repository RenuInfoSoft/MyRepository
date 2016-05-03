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
import android.widget.ImageView;
import android.widget.TextView;

import com.unfoldlabs.redgreen.R;
import com.unfoldlabs.redgreen.interfaces.SaveBatteryListener;
import com.unfoldlabs.redgreen.model.SaveBatteryModel;
import com.unfoldlabs.redgreen.utilty.Utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SaveBatteryAdapter extends BaseExpandableListAdapter {

	private Context _context;
	private List<String> listDataHeader;
	private HashMap<String, List<SaveBatteryModel>> runningAppsListDataChild1;
	protected int size;
	private List<ArrayList<Boolean>> selectedChildCheckBoxStates = new ArrayList<>();
	private List<Boolean> selectedParentCheckBoxesState = new ArrayList<Boolean>();
	private SaveBatteryListener mListener;
	private double cpuTemp;
	private boolean cpuState;
	public double actualCPU;
	public double duplicateCPU;
	public int cpu_apps_list_size, background_apps;
	private SparseBooleanArray systemAppsArray = new SparseBooleanArray();
	private SparseBooleanArray bagroundAppsArray = new SparseBooleanArray();
	private SparseBooleanArray memoryBoostArray = new SparseBooleanArray();

	/**
	 * On opening this activity the check boxes in checked state.
	 *
	 * @param
	 */
	public SaveBatteryAdapter(Context context, List<String> listDataModel,
							  HashMap<String, List<SaveBatteryModel>> listDataChild, double temp) {
		this._context = context;
		this.listDataHeader = listDataModel;
		this.runningAppsListDataChild1 = listDataChild;
		cpuTemp = temp;
		cpuState = true;
		initCheckStates(true);
	}

	/**
	 * All the check boxes checked/un checked state done.
	 */
	private void initCheckStates(boolean defaultState) {
		try {
			for (int i = 0; i < runningAppsListDataChild1.size(); i++) {

				selectedParentCheckBoxesState.add(i, defaultState);

				ArrayList<Boolean> childStates = new ArrayList<>();

				if (0 < runningAppsListDataChild1.get(listDataHeader.get(i)).size() &&
						runningAppsListDataChild1.get(listDataHeader.get(i)) != null)
					for (int j = 0; j < runningAppsListDataChild1.get(listDataHeader.get(i)).size(); j++) {
						childStates.add(defaultState);
					}
				selectedChildCheckBoxStates.add(i, childStates);
			}
		}catch (NullPointerException e){
			e.printStackTrace();
		}

	}

	/**
	 * to get the number of children count
	 */
	@Override
	public int getChildrenCount(int groupPosition) {
		try {
			if (runningAppsListDataChild1.get(listDataHeader.get(groupPosition)) == null) {
				return 0;
			}
			else
				return runningAppsListDataChild1.get(listDataHeader.get(groupPosition))
						.size();
		}catch (IndexOutOfBoundsException e){
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * to get the number of group items count
	 */
	@Override
	public int getGroupCount() {
		try{
			return listDataHeader.size();
		}catch (NullPointerException e){
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * to get the child and group position
	 */
	@Override
	public Object getChild(int groupPosition, int childPosititon) {
		try{
			if (runningAppsListDataChild1.get(listDataHeader.get(groupPosition)).get(childPosititon) == null) {

				return 0;
			}
			else
				return runningAppsListDataChild1.get(listDataHeader.get(groupPosition))
						.get(childPosititon);
		}catch (IndexOutOfBoundsException e){
			e.printStackTrace();
		}
		return 0;
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
	/**
	 * To show the expandable group items in Clean memory module. i.e.,
	 * Background running apps, RAM Boost and Browser history.
	 */
	@Override
	public View getGroupView(final int groupPosition, final boolean isExpanded,
							 View convertView, ViewGroup parent) {

		ViewHolder holder = new ViewHolder();
		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) _context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(
					R.layout.clean_memory_group_layout, parent, false);

			holder.groupNameCheckBox = (CheckBox) convertView
					.findViewById(R.id.group_chk_box);
			holder.lblListHeader = (TextView) convertView
					.findViewById(R.id.expListHeader);
			holder.childCheckedSizeTextview = (TextView) convertView
					.findViewById(R.id.childchecked_size);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (groupPosition == 0) {
			holder.childCheckedSizeTextview.setVisibility(View.GONE);

		}
		if (groupPosition == 1) {
			holder.childCheckedSizeTextview.setVisibility(View.GONE);
		}
		if (groupPosition == 2) {
			holder.childCheckedSizeTextview.setVisibility(View.VISIBLE);

			background_apps = runningAppsListDataChild1.get(
					listDataHeader.get(1)).size();

			cpu_apps_list_size = runningAppsListDataChild1.get(
					listDataHeader.get(2)).size();
			double cpuTempNew = Math.round(cpuTemp * 100);
			actualCPU = cpuTempNew / 100;
			duplicateCPU = actualCPU + 1;
			if (cpuState == true) {
				holder.childCheckedSizeTextview.setText("" + cpuTempNew / 100
						+ " \u00B0C");
			} else {
				holder.childCheckedSizeTextview.setText("" + cpuTempNew / 100
						+ " \u00B0F");
			}
		}
		holder.lblListHeader.setTypeface(null, Typeface.BOLD);
		holder.lblListHeader.setText(listDataHeader.get(groupPosition));
		holder.lblListHeader.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				/**
				 * Expands the child list if the child list is not empty when
				 * click on group item.
				 */
				if (getChildrenCount(groupPosition) == 0) {

					if (groupPosition == 0) {
						Utility.showAlertDialog(
								_context,
								_context.getResources().getString(
										R.string.savebattery_system_apps_empty));
					}
					if (groupPosition == 1) {
						Utility.showAlertDialog(
								_context,
								_context.getResources()
										.getString(
												R.string.savebattery_installed_apps_empty));
					}
					if (groupPosition == 2) {
						Utility.showAlertDialog(
								_context,
								_context.getResources().getString(
										R.string.savebattery_cpucooling_empty));
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
		if (selectedChildCheckBoxStates.size() > 0)
			try {
				for (int i = 0; i < selectedChildCheckBoxStates.get(groupPosition)
						.size(); i++) {
					boolean selectionItems = selectedChildCheckBoxStates.get(
							groupPosition).get(i);
					if (selectionItems) {
						if (groupPosition == 0)
							systemAppsArray.put(i, selectionItems);

						else if (groupPosition == 1)
							bagroundAppsArray.put(i, selectionItems);

						else if (groupPosition == 2)
							memoryBoostArray.put(i, selectionItems);

					} else {
						if (groupPosition == 0)
							systemAppsArray.delete(i);

						else if (groupPosition == 1)
							bagroundAppsArray.delete(i);

						else if (groupPosition == 2)
							memoryBoostArray.delete(i);
					}

					if (null != mListener)
						mListener.GroupItemSelection(systemAppsArray,
								bagroundAppsArray, memoryBoostArray);
				}
			}catch (Exception e){
				e.printStackTrace();
			}
		/**
		 * functionality for checkbox state according to header check box
		 */
		holder.groupNameCheckBox.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				boolean state = selectedParentCheckBoxesState
						.get(groupPosition);

				selectedParentCheckBoxesState.remove(groupPosition);
				selectedParentCheckBoxesState.add(groupPosition, state ? false
						: true);
				notifyDataSetChanged();
				for (int i = 0; i < runningAppsListDataChild1.get(
						listDataHeader.get(groupPosition)).size(); i++) {
					selectedChildCheckBoxStates.get(groupPosition).remove(i);
					selectedChildCheckBoxStates.get(groupPosition).add(i,
							state ? false : true);

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
				holder.groupNameCheckBox.setChecked(false);
				holder.groupNameCheckBox.setEnabled(false);
			} else {
				holder.groupNameCheckBox
						.setChecked(selectedParentCheckBoxesState
								.get(groupPosition));
				holder.groupNameCheckBox.setEnabled(true);
			}
		}
		return convertView;
	}

	@Override
	public View getChildView(final int groupPosition, final int childPosition,
							 boolean isLastChild, View convertView, final ViewGroup parent) {

		final SaveBatteryModel saveBatteryItem = (SaveBatteryModel) getChild(
				groupPosition, childPosition);

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
			holder.txtTitle.setText(saveBatteryItem.getInstalledAppName());
			holder.imgIcon.setImageResource(saveBatteryItem
					.getInstalledAppIcon());
			holder.childCheckBox.setVisibility(View.VISIBLE);
		}
		if (groupPosition == 1) {
			holder.txtUrl.setVisibility(View.GONE);
			holder.txtTitle.setText(saveBatteryItem.getApptitle());
			holder.imgIcon.setImageDrawable(saveBatteryItem.getAppIcon());
			holder.childCheckBox.setVisibility(View.VISIBLE);
		}
		if (groupPosition == 2) {
			holder.txtUrl.setVisibility(View.GONE);
			holder.txtTitle.setText(saveBatteryItem.getInfo());
			holder.imgIcon.setImageDrawable(saveBatteryItem.getIcon());
			holder.childCheckBox.setVisibility(View.VISIBLE);
		}

		try {

			if (selectedChildCheckBoxStates.size() <= groupPosition) {
				ArrayList<Boolean> childState = new ArrayList<>();
				for (int i = 0; i < runningAppsListDataChild1.get(
						listDataHeader.get(groupPosition)).size(); i++) {
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
				if (selectedChildCheckBoxStates.size() >= 0)
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
			}
		});
		return convertView;
	}

	public void CpuTempSet(double d, boolean cpuState2) {

		cpuTemp = d;
		cpuState = cpuState2;
		notifyDataSetChanged();
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

	public void setListener(SaveBatteryListener mListener) {
		this.mListener = mListener;
	}

	public void resetGrpCheckboxSystemApps(boolean b) {
		selectedParentCheckBoxesState.add(0, b);
		notifyDataSetChanged();
	}

	public void resetGrpCheckboxBagroundApps(boolean b) {
		selectedParentCheckBoxesState.add(1, b);
		notifyDataSetChanged();

	}

	public void resetGrpCheckboxMemoryBoost(boolean b) {
		selectedParentCheckBoxesState.add(2, b);
		notifyDataSetChanged();

	}

	public void resetGrpCheckbox(boolean b) {
		initCheckStates(b);
		notifyDataSetChanged();
	}

}