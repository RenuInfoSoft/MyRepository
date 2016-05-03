package com.unfoldlabs.redgreen.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
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
import com.unfoldlabs.redgreen.db.DatabaseHandler;
import com.unfoldlabs.redgreen.global.AppData;
import com.unfoldlabs.redgreen.interfaces.AppManagerListener;
import com.unfoldlabs.redgreen.model.CleanAppDB;
import com.unfoldlabs.redgreen.utilty.Utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Shareefa on 15-03-2016.
 */
public class AppManagerAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> listDataHeader;
    private HashMap<String, List<CleanAppDB>> runningAppsListDataChild1;
    protected int size;
    private List<ArrayList<Boolean>> selectedChildCheckBoxStates = new ArrayList<>();
    private List<Boolean> selectedParentCheckBoxesState = new ArrayList<Boolean>();
    private AppManagerListener mListener;
    private SparseBooleanArray appArray = new SparseBooleanArray();
    private SparseBooleanArray doNotAppsArray = new SparseBooleanArray();
    private PackageManager packageManager;
    private DatabaseHandler database;
    private int sumFinal0 = 0;
    private int sumFinal1 = 0;
    private int tempSum = 0;
    private double tempFinalSum = 0.0;
    private double tempFinalDoNotSum = 0.0;
    private int doNotSumFinal = 0;
    private ArrayList<CleanAppDB> checkedlist;
    /**
     * On opening this activity the check boxes in checked state.
     *
     *
     */
    public AppManagerAdapter(Context context, DatabaseHandler database,  int sumFinal,
                             int doNotSumFinal, ArrayList<CleanAppDB> checkedlist,
                             List<String> listDataModel, HashMap<String, List<CleanAppDB>> listDataChild) {
        this._context = context;
        this.listDataHeader = listDataModel;
        this.runningAppsListDataChild1 = listDataChild;
        packageManager = context.getPackageManager();
        initCheckStates(false);
        tempSum = sumFinal;
        this.doNotSumFinal = doNotSumFinal;
        this.database = database;
        this.checkedlist = checkedlist;
        totalSum();
        totalDoNotDeleteSum();
    }
    private void totalSum(){
        double kb = tempSum / 1000;
        double mb = kb / 1000;
        double total_size_mb = Math.round(mb * 100);
        double finalSum = total_size_mb / 100;
        tempFinalSum = finalSum;
    }
    private void totalDoNotDeleteSum(){
        double kb = this.doNotSumFinal / 1000;
        double mb = kb / 1000;
        double total_size_mb = Math.round(mb * 100);
        double finalSum = total_size_mb / 100;
        tempFinalDoNotSum = finalSum;
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
            } else
                return runningAppsListDataChild1.get(listDataHeader.get(groupPosition)).size();
        }catch (IndexOutOfBoundsException | NullPointerException e){
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * to get the number of group items count
     */
    @Override
    public int getGroupCount() {
        try {
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
        try {
            if (runningAppsListDataChild1.get(listDataHeader.get(groupPosition)).get(childPosititon) == null) {
                return 0;
            } else
                return runningAppsListDataChild1.get(listDataHeader.get(groupPosition)).get(childPosititon);
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
    public View getGroupView(final int groupPosition, final boolean isExpanded,View convertView, ViewGroup parent) {

        ViewHolder holder = new ViewHolder();
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater)_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.clean_memory_group_layout, parent, false);
            holder.groupNameCheckBox = (CheckBox)convertView.findViewById(R.id.group_chk_box);
            holder.lblListHeader = (TextView)convertView.findViewById(R.id.expListHeader);
            holder.groupSizeTextview = (TextView)convertView.findViewById(R.id.childchecked_size);
            holder.groupCheckedSizeTextView = (TextView)convertView.findViewById(R.id.groupchecked_size);
            holder.backSlashTextView = (TextView)convertView.findViewById(R.id.back_slash);
            convertView.setTag(holder);
            if (null != checkedlist && checkedlist.size() > 0) {
                if( AppData.getInstance().isAppManagerUnInstall()){
                    AppData.getInstance().setAppManagerState0(false);
                    notifyDataSetChanged();
                    showTotalGroup0(true);
                    showTotalGroup1(true);
                    if(AppData.getInstance().isAppManagerState0()){
                        holder.groupCheckedSizeTextView.setText("" + 0.0 + " MB");
                    }else {
                        double kb = sumFinal0 / 1000;
                        double mb = kb / 1000;
                        double total_size_mb = Math.round(mb * 100);
                        final double finalSum = total_size_mb / 100;
                        selectedParentCheckBoxesState.add(0, true);
                        ArrayList<Boolean> childStates = new ArrayList<>();
                        if (0 < runningAppsListDataChild1.get(listDataHeader.get(0)).size() &&
                                runningAppsListDataChild1.get(listDataHeader.get(0)) != null)
                            for (int j = 0; j < runningAppsListDataChild1.get(listDataHeader.get(0)).size(); j++) {
                                childStates.add(true);
                            }
                        selectedChildCheckBoxStates.add(0, childStates);

                        if(null != runningAppsListDataChild1.get(listDataHeader.get(1))
                                && runningAppsListDataChild1.get(listDataHeader.get(1)).size() > 0) {

                            selectedParentCheckBoxesState.add(1, false);
                            ArrayList<Boolean> childStates2 = new ArrayList<>();
                            if (0 < runningAppsListDataChild1.get(listDataHeader.get(1)).size() &&
                                    runningAppsListDataChild1.get(listDataHeader.get(1)) != null)
                                for (int j = 0; j < runningAppsListDataChild1.get(listDataHeader.get(1)).size(); j++) {
                                    childStates2.add(false);
                                }
                            selectedChildCheckBoxStates.add(1, childStates2);

                        }
                        holder.groupCheckedSizeTextView.setText("" + finalSum + " MB");
                        checkedlist.clear();
                        AppData.getInstance().setAppManagerUnInstall(false);
                    }

                }
            }
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (groupPosition == 0) {
            holder.groupSizeTextview.setVisibility(View.VISIBLE);
            holder.groupSizeTextview.setText("" + tempFinalSum + " MB");
            holder.groupCheckedSizeTextView.setVisibility(View.VISIBLE);
            holder.backSlashTextView.setVisibility(View.VISIBLE);
            if(AppData.getInstance().isAppManagerState0()){
                holder.groupCheckedSizeTextView.setText("" + 0.0 + " MB");
            }else{
                double kb = sumFinal0 / 1000;
                double mb = kb / 1000;
                double total_size_mb = Math.round(mb * 100);
                final double finalSum = total_size_mb / 100;
                holder.groupCheckedSizeTextView.setText("" + finalSum + " MB");
            }
        }
        if (groupPosition == 1) {
            holder.groupSizeTextview.setVisibility(View.VISIBLE);
            holder.groupSizeTextview.setText("" + tempFinalDoNotSum + " MB");
            holder.groupCheckedSizeTextView.setVisibility(View.VISIBLE);
            holder.backSlashTextView.setVisibility(View.VISIBLE);
            if(AppData.getInstance().isAppManagerState1()){
                holder.groupCheckedSizeTextView.setText("" + 0.0 + " MB");
            }else{
                double kb = sumFinal1 / 1000;
                double mb = kb / 1000;
                double total_size_mb = Math.round(mb * 100);
                final double finalSum = total_size_mb / 100;
                holder.groupCheckedSizeTextView.setText("" + finalSum + " MB");
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
                        Utility.showAlertDialog( _context,"Installed Apps List is Empty");
                    }
                    if (groupPosition == 1) {
                        Utility.showAlertDialog( _context,"Do Not Delete Apps List is Empty");
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
                for (int i = 0; i < selectedChildCheckBoxStates.get(groupPosition).size(); i++) {
                    boolean selectionItems = selectedChildCheckBoxStates.get(groupPosition).get(i);
                    if (selectionItems) {
                        if (groupPosition == 0)
                            appArray.put(i, selectionItems);
                        else if (groupPosition == 1)
                            doNotAppsArray.put(i, selectionItems);
                    } else {
                        if (groupPosition == 0)
                            appArray.delete(i);
                        if(groupPosition == 1)
                            doNotAppsArray.delete(i);
                    }
                    if (null != mListener)
                        mListener.GroupItemSelection(appArray, doNotAppsArray);
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
                boolean state = selectedParentCheckBoxesState.get(groupPosition);
                selectedParentCheckBoxesState.remove(groupPosition);
                selectedParentCheckBoxesState.add(groupPosition, state ? false : true);
                try {
                    for (int i = 0; i < runningAppsListDataChild1.get(listDataHeader.get(groupPosition)).size(); i++) {
                        selectedChildCheckBoxStates.get(groupPosition).remove(i);
                        selectedChildCheckBoxStates.get(groupPosition).add(i, state ? false : true);
                    }
                }catch(IndexOutOfBoundsException | NullPointerException e){
                    e.printStackTrace();
                }
                notifyDataSetChanged();
                boolean isCheck = ((CompoundButton) v).isChecked();
                if (groupPosition == 0){
                    AppData.getInstance().setAppManagerState0(false);
                    showTotalGroup0(isCheck);
                }
                else if (groupPosition == 1){
                    AppData.getInstance().setAppManagerState1(false);
                    showTotalGroup1(isCheck);
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
                holder.groupNameCheckBox.setChecked(selectedParentCheckBoxesState.get(groupPosition));
                holder.groupNameCheckBox.setEnabled(true);
            }
        }
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, final ViewGroup parent) {
        try {
            final CleanAppDB cleanAppDBItem = (CleanAppDB) getChild(groupPosition, childPosition);
            ViewHolder holder;
            if (convertView == null) {
                LayoutInflater layoutInflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = layoutInflater.inflate(R.layout.clean_memory_child_layout, parent, false);
                holder = new ViewHolder();
                holder.txtTitle = (TextView) convertView.findViewById(R.id.child_tv_appname);
                holder.imgIcon = (ImageView) convertView.findViewById(R.id.child_icon);
                holder.txtUrl = (TextView) convertView.findViewById(R.id.child_tv_url);
                holder.childCheckBox = (CheckBox) convertView.findViewById(R.id.child_checkbox);
                holder.childSizeTextview = (TextView) convertView.findViewById(R.id.childchecked_size);
                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.childSizeTextview.setVisibility(View.VISIBLE);
            String appName = "";
            Drawable icon = null;
            try {
                appName = (String) packageManager
                        .getApplicationLabel(packageManager.getApplicationInfo(cleanAppDBItem.getPackageName(),
                                PackageManager.GET_META_DATA));
                icon = packageManager.getApplicationIcon(cleanAppDBItem.getPackageName());

            } catch (PackageManager.NameNotFoundException e) {
                database.deleteApp(cleanAppDBItem.getPackageName());
                e.printStackTrace();
            }catch (Exception e) {
            }
            holder.txtTitle.setText(appName);
            holder.imgIcon.setImageDrawable(icon);
            if (null != cleanAppDBItem.getDateTime())
                if (cleanAppDBItem.getDateTime().isEmpty()) {
                    holder.txtUrl.setText("Last used: "+ AppData.getInstance().getAppInstalledTime());
                } else {
                    holder.txtUrl.setText("Last used: "+ cleanAppDBItem.getDateTime());
                }
            holder.childSizeTextview.setText("Loading..");
            if (null != cleanAppDBItem.getApp_size()){
                String s = cleanAppDBItem.getApp_size();
                double size = Double.parseDouble(s);
                double kb = size / 1000;
                double mb = kb / 1000;

                if (kb < 1000) {
                    double total_size_kb= Math.round(kb*100);
                    holder.childSizeTextview.setText(total_size_kb/100 + " KB");
                } else {
                    double total_size_mb= Math.round(mb*100);
                    holder.childSizeTextview.setText(total_size_mb/100 + " MB");
                }
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

                    boolean state = selectedChildCheckBoxStates.get(groupPosition).get(childPosition);
                    selectedChildCheckBoxStates.get(groupPosition).remove(childPosition);
                    selectedChildCheckBoxStates.get(groupPosition).add(childPosition, state ? false : true);
                    boolean isCheck2 = ((CompoundButton) v).isChecked();
                    notifyDataSetChanged();
                    if (groupPosition == 0){
                        AppData.getInstance().setAppManagerState0(false);
                        showTotal0(isCheck2, cleanAppDBItem);
                    }
                    else if (groupPosition == 1){
                        AppData.getInstance().setAppManagerState1(false);
                        showTotal1(isCheck2, cleanAppDBItem);
                    }
                    if (selectedChildCheckBoxStates.size() > 0)
                        try {
                            for (int i = 0; i < selectedChildCheckBoxStates.get(groupPosition).size(); i++) {
                                boolean selectionItems = selectedChildCheckBoxStates.get(groupPosition).get(i);
                                if (selectionItems) {
                                    if (groupPosition == 0)
                                        appArray.put(i, selectionItems);
                                    else if (groupPosition == 1)
                                        doNotAppsArray.put(i, selectionItems);
                                } else {
                                    if (groupPosition == 0)
                                        appArray.delete(i);
                                    if(groupPosition == 1)
                                        doNotAppsArray.delete(i);
                                }
                                if (null != mListener)
                                    mListener.GroupItemSelection(appArray, doNotAppsArray);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                }
            });
        }catch (ClassCastException e){
            e.printStackTrace();
        }
        return convertView;
    }
    @Override
    public void notifyDataSetChanged()
    {// Refresh List rows
        super.notifyDataSetChanged();
    }

    public void showTotal0(boolean isCheck, CleanAppDB cleanAppDB) {
        if (null != cleanAppDB.getApp_size()) {
            if (isCheck) {
                sumFinal0 += Integer.parseInt(cleanAppDB.getApp_size());
            } else {
                sumFinal0 -= Integer.parseInt(cleanAppDB.getApp_size());
            }
        }
    }
    public void showTotal1(boolean isCheck, CleanAppDB cleanAppDB) {
        if (null != cleanAppDB.getApp_size()) {
            if (isCheck) {
                sumFinal1 += Integer.parseInt(cleanAppDB.getApp_size());
            } else {
                sumFinal1 -= Integer.parseInt(cleanAppDB.getApp_size());
            }
        }
    }
    public void showTotalGroup0(boolean isCheck) {
        if(isCheck){
            sumFinal0 = tempSum;
        }else{
            sumFinal0 = 0;
        }
    }
    public void showTotalGroup1(boolean isCheck) {
        if(isCheck){
            sumFinal1 = doNotSumFinal;
        }else{
            sumFinal1 = 0;
        }
    }
    public class ViewHolder {
        TextView txtTitle;
        TextView txtUrl;
        ImageView imgIcon;
        CheckBox groupNameCheckBox;
        TextView lblListHeader;
        CheckBox childCheckBox;
        TextView childSizeTextview;
        TextView groupSizeTextview;
        TextView groupCheckedSizeTextView;
        TextView backSlashTextView;
    }
    public void setListener(AppManagerListener mListener) {
        this.mListener = mListener;
    }

    public void resetGrpCheckboxApps(boolean b) {
        selectedParentCheckBoxesState.add(0, b);
        notifyDataSetChanged();
    }
    public void resetGrpCheckboxDoNotApps(boolean b) {
        selectedParentCheckBoxesState.add(1, b);
        notifyDataSetChanged();
    }
    public void resetCheckedValues(boolean b) {
        initCheckStates(false);
        notifyDataSetChanged();
    }
}