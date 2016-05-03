package com.unfoldlabs.redgreen.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.SparseBooleanArray;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.MapBuilder;
import com.unfoldlabs.redgreen.R;
import com.unfoldlabs.redgreen.adapter.AppManagerAdapter;
import com.unfoldlabs.redgreen.db.DatabaseHandler;
import com.unfoldlabs.redgreen.global.AppData;
import com.unfoldlabs.redgreen.global.ApplicationData;
import com.unfoldlabs.redgreen.interfaces.AppManagerListener;
import com.unfoldlabs.redgreen.model.CleanAppDB;
import com.unfoldlabs.redgreen.model.ComparatorMethod;
import com.unfoldlabs.redgreen.utilty.Utility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Shareefa on 15-03-2016.
 */
public class AppManagerActivity extends BaseActivity implements AppManagerListener, View.OnClickListener {

    private List<CleanAppDB> appsList = new ArrayList<CleanAppDB>();
    private List<CleanAppDB> doNotDelAppsList = new ArrayList<CleanAppDB>();
    public List<CleanAppDB> checkedList;
    private HashMap<String, List<CleanAppDB>> listDataChild;
    private List<String> listDataHeader;
    private HashMap<Integer, String> hashMap = new HashMap<Integer, String>();
    private ArrayList<Integer> positions = new ArrayList<Integer>();
    private SparseBooleanArray appsArray = new SparseBooleanArray();
    private SparseBooleanArray doNotAppsArray = new SparseBooleanArray();
    private AppManagerAdapter listAdapter;
    private AppManagerListener appManagerListener;
    private boolean windowState = true;
    private int dialogueCount = 0;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    public EasyTracker easyTracker;
    private ExpandableListView expListView;
    private int dialogSorting = -1;
    private SharedPreferences cleanAppSharedPref;
    private SharedPreferences.Editor cleanAppEditor;
    private boolean isDirectionSelection = true;
    private DatabaseHandler database;
    private int sumFinal = 0;
    private int doNotSumFinal = 0;
    private Button unInstallButton;
    private TextView moveDoNotTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.appmanager_layout, frameLayout);
        AppData.getInstance().setAppManagerState0(true);
        AppData.getInstance().setAppManagerState1(true);
        appManagerListener = this;
        easyTracker = EasyTracker.getInstance(getApplicationContext());
        sp = getSharedPreferences(getResources().getString(R.string.cleanapp_pref), Context.MODE_PRIVATE);
        editor = sp.edit();
        cleanAppSharedPref = getSharedPreferences("CLEAN_APP_SHARED_PREF", Context.MODE_PRIVATE);
        cleanAppEditor = cleanAppSharedPref.edit();

        expListView = (ExpandableListView) findViewById(R.id.lvExp_savebattery);
        unInstallButton = (Button) findViewById(R.id.uninstall_button);
        moveDoNotTextView = (TextView)findViewById(R.id.do_not_delete_textview);
        moveDoNotTextView.setOnClickListener(this);
        moveDoNotTextView.setTextColor(getResources().getColor(R.color.btn_disable));
        unInstallButton.setOnClickListener(this);
        unInstallButton.setTextColor(getResources().getColor(R.color.btn_disable));
        dialogueCount = sp.getInt(getResources().getString(R.string.cleanapp_count), dialogueCount);
        dialogueCount += 1;
        editor.putInt(getResources().getString(R.string.cleanapp_count), dialogueCount);
        editor.commit();
        if (sp.getInt(getResources().getString(R.string.cleanapp_count), dialogueCount) == 5
                && sp.getBoolean(getResources().getString(R.string.cleanapp_state), false) == false) {
            cleanAppSettingsDialog(AppManagerActivity.this, windowState);
        }
        easyTracker.send(MapBuilder.createEvent(getString(R.string.app_manager),
                getString(R.string.app_manager_activity_diaplayed), "track event", null).build());

        Bundle bundleObject = getIntent().getExtras();
        if (null != bundleObject) {
            checkedList = (ArrayList<CleanAppDB>) bundleObject.getSerializable("list");
        }
        dialogSorting = cleanAppSharedPref.getInt(getResources().getString(R.string.cleanAppSortingType), dialogSorting);
        isDirectionSelection = cleanAppSharedPref.getBoolean(getResources().getString(R.string.cleanAppSorting), isDirectionSelection);
        new LoadingProgressData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private ProgressDialog dialog;

    class LoadingProgressData extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            try {
                dialog = new ProgressDialog(AppManagerActivity.this);
                dialog.setMessage("Loading please wait.");
                dialog.setCanceledOnTouchOutside(false);
                dialog.setCancelable(false);
                dialog.show();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                getAllRunningProcess();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            sortingAll();
            setDataMethod();
            try {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    /*
 * Preparing the list data
 */
    private void getAllRunningProcess() {
        sumFinal = 0;
        database = new DatabaseHandler(getApplicationContext());
        appsList.clear();
        appsList = database.getAllApps(getPackageManager());
        for (int i = 0; i < appsList.size(); i++) {
            sumFinal += Integer.parseInt(appsList.get(i).getApp_size());
        }
        Utility.getAllPackageList(getApplicationContext(), database);
        if (null != appsList && appsList.isEmpty()) {
            if (null != AppData.getInstance().getAppManagerInstalledAppsList())
                appsList = AppData.getInstance().getAppManagerInstalledAppsList();
        }
        doNotDelAppsList.clear();
        doNotDelAppsList = database.getDontDeleteApps(this.getPackageManager());
        for (int i = 0; i < doNotDelAppsList.size(); i++) {
            doNotSumFinal += Integer.parseInt(doNotDelAppsList.get(i).getApp_size());
        }
        if (null != doNotDelAppsList && doNotDelAppsList.isEmpty()) {
            if (null != AppData.getInstance().getDoNotDeleteAppsList())
                doNotDelAppsList = AppData.getInstance().getDoNotDeleteAppsList();
        }
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<CleanAppDB>>();
        // Adding child data
        listDataHeader.add("Installed Apps");
        listDataHeader.add("Do Not Delete Apps");
        listDataChild.put(listDataHeader.get(0), appsList);
        if (null != doNotDelAppsList && !doNotDelAppsList.isEmpty()) {
            listDataChild.put(listDataHeader.get(1), doNotDelAppsList);
        }
    }

    private void setDataMethod() {
        listAdapter = new AppManagerAdapter(AppManagerActivity.this,
                database, sumFinal, doNotSumFinal, (ArrayList<CleanAppDB>) checkedList, listDataHeader, listDataChild);
        listAdapter.setListener(AppManagerActivity.this);
        expListView.setAdapter(listAdapter);
        listAdapter.notifyDataSetChanged();
        listAdapter.notifyDataSetInvalidated();
        expListView.invalidate();
        sortingAll();
        if (database != null) {
            database.close();
        }
        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return false;
            }
        });
        // Listview Group expanded listener
        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int previousGroup = -1;

            @Override
            public void onGroupExpand(int groupPosition) {
                if (groupPosition != previousGroup)
                    expListView.collapseGroup(previousGroup);
                previousGroup = groupPosition;
            }
        });
        // Listview Group collasped listener
        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
            }
        });
        // Listview on child click listener
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                return false;
            }
        });
        ApplicationData.getInstance().setAppInstalledSize("" + listAdapter.getChildrenCount(0));
        ApplicationData.getInstance().setDndappInstalledSize("" + listAdapter.getChildrenCount(1));
    }

    private void newReload() {
        appsList.clear();
        appsList = database.getAllApps(getPackageManager());
        Set<CleanAppDB> appSet = new HashSet<>(appsList);
        appsList.clear();
        appsList.addAll(appSet);
        sumFinal = 0;
        doNotSumFinal = 0;
        for (int i = 0; i < appsList.size(); i++) {
            sumFinal += Integer.parseInt(appsList.get(i).getApp_size());
        }
        if (null != appsList && appsList.isEmpty()) {
            if (null != AppData.getInstance().getAppManagerInstalledAppsList())
                appsList = AppData.getInstance().getAppManagerInstalledAppsList();
        }
        doNotDelAppsList.clear();
        doNotDelAppsList = database.getDontDeleteApps(this.getPackageManager());
        Set<CleanAppDB> doNotDelSet = new HashSet<>(doNotDelAppsList);
        doNotDelAppsList.clear();
        doNotDelAppsList.addAll(doNotDelSet);
        for (int i = 0; i < doNotDelAppsList.size(); i++) {
            doNotSumFinal += Integer.parseInt(doNotDelAppsList.get(i).getApp_size());
        }
        if (null != doNotDelAppsList && doNotDelAppsList.isEmpty()) {
            if (null != AppData.getInstance().getDoNotDeleteAppsList())
                doNotDelAppsList = AppData.getInstance().getDoNotDeleteAppsList();
        }
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<CleanAppDB>>();
        // Adding child data
        listDataHeader.add("Installed Apps");
        listDataHeader.add("Do Not Delete Apps");
        listDataChild.put(listDataHeader.get(0), appsList);
        if (null != doNotDelAppsList && !doNotDelAppsList.isEmpty()) {
            listDataChild.put(listDataHeader.get(1), doNotDelAppsList);
        }
        setDataMethod();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.uninstall_button:
                easyTracker.send(MapBuilder.createEvent(getString(R.string.app_manager),
                        getString(R.string.app_manager_uninstall_button_clicked),
                        "track event", null).build());
                unInstallButton.setEnabled(false);
                unInstallButton.setTextColor(getResources().getColor(R.color.btn_disable));
                AppData.getInstance().setApplockExit(true);
                Utility.showProgressDialog(AppManagerActivity.this, "Loading Please Wait");
                if (appsArray.size() > 0 && appsList.size() > 0) {
                    for (int i = 0; i < appsArray.size(); i++) {
                        CleanAppDB packageName = (CleanAppDB) listAdapter.getChild(0, appsArray.keyAt(i));
                        Uninstall(packageName.getPackageName(), i);
                    }
                    appsArray.clear();
                }
                if (doNotAppsArray.size() > 0 && doNotDelAppsList.size() > 0) {
                    for (int i = 0; i < doNotAppsArray.size(); i++) {
                        CleanAppDB packageName = (CleanAppDB) listAdapter.getChild(1, doNotAppsArray.keyAt(i));
                        Uninstall(packageName.getPackageName(), i);
                    }
                    doNotAppsArray.clear();
                }
                break;
            case R.id.do_not_delete_textview:
                moveDoNotTextView.setEnabled(false);
                moveDoNotTextView.setTextColor(getResources().getColor(R.color.btn_disable));
                String doNotDialogueMessage = "";
                if(appsArray.size() == 1){
                    doNotDialogueMessage = "Moved "+appsArray.size()+" app to Do Not Delete Apps List";
                }else{
                    doNotDialogueMessage = "Moved "+appsArray.size()+" apps to Do Not Delete Apps List";
                }
                if (appsArray.size() > 0 && appsList.size() > 0) {
                    AppData.getInstance().setAskDeleteCancel(true);
                    DatabaseHandler databaseHandler = new DatabaseHandler(getApplicationContext());
                    if (appsArray.size() > 0 && appsList.size() > 0) {
                        for (int i = 0; i < appsArray.size(); i++) {
                            CleanAppDB packageName = (CleanAppDB) listAdapter.getChild(0, appsArray.keyAt(i));
                            databaseHandler.dontDeleteApps(1, packageName.getPackageName());
                        }
                        appsArray.clear();
                    }
                    newReload();
                    listAdapter.notifyDataSetChanged();
                    listAdapter.notifyDataSetInvalidated();
                    expListView.invalidate();
                    Utility.showAlertDialogNetworkConnection(AppManagerActivity.this, doNotDialogueMessage,"Done");
                    break;
                } else {
                    Toast.makeText(getApplicationContext(), "Please select apps", Toast.LENGTH_SHORT).show();
                }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_CANCELED) {
            if (null != checkedList) {
                AppData.getInstance().setApplockExit(true);
                checkedList.clear();
            }
            appsArray.clear();
            doNotAppsArray.clear();
            AppData.getInstance().setAppManagerState0(true);
            AppData.getInstance().setAppManagerState1(true);
            Utility.DismissDialog();
            listAdapter.resetCheckedValues(false);
            listAdapter.resetGrpCheckboxDoNotApps(false);
            listAdapter.resetGrpCheckboxApps(false);
            listAdapter.notifyDataSetChanged();
            listAdapter.notifyDataSetInvalidated();
            unInstallButton.setEnabled(false);
            unInstallButton.setTextColor(getResources().getColor(R.color.btn_disable));
        } else if (resultCode == Activity.RESULT_OK) {
            AppData.getInstance().setApplockExit(true);
            positions.add(requestCode);
            deletePackages();
        }
    }

    /**
     * Delete packages from database
     */
    private void deletePackages() {
        if (positions.size() > 0) {
            for (int i = 0; i < positions.size(); i++) {
                database.deleteApp(hashMap.get(positions.get(i)));
            }
            if (database.getAllApps(getPackageManager()).size() <= 0) {
            }
        }
        if (null != checkedList) {
            checkedList.clear();
        }
        newReload();
        appsArray.clear();
        doNotAppsArray.clear();
        AppData.getInstance().setAppManagerState0(true);
        AppData.getInstance().setAppManagerState1(true);
        listAdapter.resetGrpCheckboxDoNotApps(false);
        listAdapter.resetGrpCheckboxApps(false);
        Utility.DismissDialog();
        listAdapter.notifyDataSetChanged();
        listAdapter.notifyDataSetInvalidated();
        unInstallButton.setEnabled(false);
        unInstallButton.setTextColor(getResources().getColor(R.color.btn_disable));
    }

    /**
     * For Un installing applications
     */
    private void Uninstall(String packagename, int i) {
        Intent uninstallIntent = new Intent(Intent.ACTION_DELETE);
        uninstallIntent.setData(Uri.parse("package:" + packagename));
        uninstallIntent.putExtra(Intent.EXTRA_RETURN_RESULT, true);
        startActivityForResult(uninstallIntent, i);
        hashMap.put(i, packagename);
    }

    /**
     * Button disable or enable for list items.
     */
    private void buttonDisable() {
        if (appsArray.size() == 0 && doNotAppsArray.size() == 0) {
            unInstallButton.setEnabled(false);
            unInstallButton.setTextColor(getResources().getColor(R.color.btn_disable));
        }
        else{
            unInstallButton.setEnabled(true);
            unInstallButton.setTextColor(Color.WHITE);
        }
        if (appsArray.size() == 0){
            moveDoNotTextView.setEnabled(false);
            moveDoNotTextView.setTextColor(getResources().getColor(R.color.btn_disable));
        }else{
            moveDoNotTextView.setEnabled(true);
            moveDoNotTextView.setTextColor(Color.WHITE);
        }
        listAdapter.notifyDataSetChanged();
        listAdapter.notifyDataSetInvalidated();
    }

    @Override
    public void expandGroupEvent(int groupPosition, boolean isExpanded) {
        if (isExpanded)
            expListView.collapseGroup(groupPosition);
        else
            expListView.expandGroup(groupPosition);
    }

    @Override
    public void GroupItemSelection(SparseBooleanArray appsArray, SparseBooleanArray doNotAppsArray) {
        this.appsArray = appsArray;
        this.doNotAppsArray = doNotAppsArray;
        buttonDisable();
        if (appsArray.size() == appsList.size()) {
            easyTracker.send(MapBuilder.createEvent(getString(R.string.app_manager),
                    getString(R.string.app_manager_all_installed_apps_selected),
                    "track event", null).build());
            listAdapter.resetGrpCheckboxApps(true);
        } else {
            listAdapter.resetGrpCheckboxApps(false);
        }
        if (doNotAppsArray.size() == doNotDelAppsList.size()) {
            listAdapter.resetGrpCheckboxDoNotApps(true);
            easyTracker.send(MapBuilder.createEvent(getString(R.string.app_manager),
                    getString(R.string.app_manager_all_donot_apps_selected),
                    "track event", null).build());
        } else {
            listAdapter.resetGrpCheckboxDoNotApps(false);
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        EasyTracker.getInstance(this).activityStart(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EasyTracker.getInstance(this).activityStop(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (appsList != null) {
            appsList.clear();
        }
        if (doNotDelAppsList != null) {
            doNotDelAppsList.clear();
        }
        easyTracker.send(MapBuilder.createEvent(getString(R.string.app_manager),
                getString(R.string.app_manager_exit_time),
                "track event", null).build());
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    /**
     * show options menu items on this activity
     */
    @Override

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.findItem(R.id.menu_auto_clean).setVisible(false);
        menu.findItem(R.id.menu_fahrenheit).setVisible(false);
        menu.findItem(R.id.menu_celsius).setVisible(false);
        menu.findItem(R.id.action_sort_by_size_asc).setVisible(false);
        menu.findItem(R.id.action_sort_by_size_des).setVisible(false);
        menu.findItem(R.id.action_name_ascending).setVisible(false);
        menu.findItem(R.id.action_name_decending).setVisible(false);
        menu.findItem(R.id.action_sort_by_lastused_asc).setVisible(false);
        menu.findItem(R.id.action_sort_by_lastused_des).setVisible(false);
        menu.findItem(R.id.action_sort_by_size_asc).setVisible(false);
        menu.findItem(R.id.action_sort_by_name_asc).setVisible(false);
        menu.findItem(R.id.action_sort_by_name_des).setVisible(false);
        menu.findItem(R.id.action_sort_by_size_des).setVisible(false);
        menu.findItem(R.id.action_remove_after_invite).setVisible(false);
        menu.findItem(R.id.menu_ask_delete).setVisible(false);
        menu.findItem(R.id.menu_auto_speed_setting).setVisible(false);
        menu.findItem(R.id.menu_auto_speed_memory).setVisible(false);
        menu.findItem(R.id.menu_dont_delete_apps).setVisible(false);
        return true;
    }

    /**
     * on option item click functionality in clean app activity.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sort:
                easyTracker.send(MapBuilder.createEvent(getString(R.string.app_manager),
                        getString(R.string.app_manager_sorting_dialogue_displayed),
                        "track event", null).build());
                cleanAppSortingDialog();
                break;
            case R.id.menu_settings:
                easyTracker.send(MapBuilder.createEvent(getString(R.string.app_manager),
                        getString(R.string.app_manager_setting_dialogue_displayed),
                        "track event", null).build());
                cleanAppSettingsDialog(AppManagerActivity.this, windowState);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void sortingAll() {
        if (isDirectionSelection == true && dialogSorting == -1) {
            ComparatorMethod.packageAsscMethod(this, appsList);
            ComparatorMethod.packageAsscMethod(this, doNotDelAppsList);
        } else if (isDirectionSelection == false && dialogSorting == -1) {
            ComparatorMethod.packageDescMethod(this, appsList);
            ComparatorMethod.packageDescMethod(this, doNotDelAppsList);
        } else if (isDirectionSelection == true && dialogSorting == 1) {
            Collections.sort(appsList, ComparatorMethod.dateDesComparator);
            Collections.sort(doNotDelAppsList, ComparatorMethod.dateDesComparator);
        } else if (isDirectionSelection == false && dialogSorting == 1) {
            Collections.sort(appsList, ComparatorMethod.dateAsscComparator);
            Collections.sort(doNotDelAppsList, ComparatorMethod.dateAsscComparator);
        }
    }

    private void cleanAppSortingDialog() {
        final Dialog cleanAppSortingDialog = new Dialog(AppManagerActivity.this);
        cleanAppSortingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        cleanAppSortingDialog.setContentView(R.layout.cleanapp_sorting_dialog);
        TextView button_ok = (TextView) cleanAppSortingDialog.findViewById(R.id.button_done);
        RadioGroup appSettingGroup = (RadioGroup) cleanAppSortingDialog.findViewById(R.id.app_setting_grp);
        RadioButton last_used_button = (RadioButton) cleanAppSortingDialog.findViewById(R.id.last_used);
        last_used_button.setVisibility(View.VISIBLE);
        RadioGroup askBeforeAscendingGroup = (RadioGroup) cleanAppSortingDialog.findViewById(R.id.ask_bfore_grp);
        WindowManager.LayoutParams wmlp = cleanAppSortingDialog.getWindow().getAttributes();
        wmlp.gravity = Gravity.TOP | Gravity.CENTER;
        int density = getResources().getDisplayMetrics().densityDpi;
        switch (density) {
            case DisplayMetrics.DENSITY_MEDIUM:
                wmlp.x = 560;
                wmlp.y = 50;
                break;
            case DisplayMetrics.DENSITY_LOW:
                wmlp.x = 560;
                wmlp.y = 30;
                break;
            case DisplayMetrics.DENSITY_HIGH:
                wmlp.x = 160;
                wmlp.y = 60;
                break;
            case DisplayMetrics.DENSITY_XHIGH:
                wmlp.x = 220;
                wmlp.y = 80;
                break;
            case DisplayMetrics.DENSITY_XXHIGH:
                wmlp.x = 450;
                wmlp.y = 120;
                break;
            case DisplayMetrics.DENSITY_XXXHIGH:
                wmlp.x = 160;
                wmlp.y = 220;
                break;
        }
        cleanAppSortingDialog.show();
        dialogSorting = cleanAppSharedPref.getInt(getResources().getString(R.string.cleanAppSortingType), dialogSorting);
        if (dialogSorting == -1) {
            appSettingGroup.check(R.id.name);
        } else if (dialogSorting == 1) {
            appSettingGroup.check(R.id.last_used);
        }

        isDirectionSelection = cleanAppSharedPref.getBoolean(getResources().getString(R.string.cleanAppSorting), isDirectionSelection);
        if (isDirectionSelection) {
            askBeforeAscendingGroup.check(R.id.ascending);
        } else {
            askBeforeAscendingGroup.check(R.id.decending);
        }
        appSettingGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {
                    case R.id.name:
                        dialogSorting = -1;
                        easyTracker.send(MapBuilder.createEvent(getString(R.string.app_manager),
                                getString(R.string.app_manager_sorting_done_name),
                                "track event", null).build());
                        break;
                    case R.id.last_used:
                        dialogSorting = 1;
                        easyTracker.send(MapBuilder.createEvent(getString(R.string.app_manager),
                                getString(R.string.app_manager_sorting_done_lastused_time),
                                "track event", null).build());
                        break;
                    default:
                        break;
                }

            }
        });

        askBeforeAscendingGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.decending:
                        isDirectionSelection = false;
                        break;
                    case R.id.ascending:
                        isDirectionSelection = true;
                        break;
                    default:
                        break;
                }

            }
        });

        cleanAppSortingDialog.show();
        button_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                easyTracker.send(MapBuilder.createEvent(getString(R.string.app_manager),
                        getString(R.string.app_manager_sorting_done_button_clicked),
                        "track event", null).build());

                sortingAll();
                cleanAppEditor.putBoolean(getResources().getString(R.string.cleanAppSorting), isDirectionSelection);
                cleanAppEditor.commit();
                cleanAppEditor.putInt(getResources().getString(R.string.cleanAppSortingType), dialogSorting);
                cleanAppEditor.commit();
                listAdapter.notifyDataSetChanged();
                cleanAppSortingDialog.dismiss();
            }

        });
    }
}