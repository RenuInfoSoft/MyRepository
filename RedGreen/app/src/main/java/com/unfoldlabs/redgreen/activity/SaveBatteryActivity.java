package com.unfoldlabs.redgreen.activity;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.util.DisplayMetrics;
import android.util.SparseBooleanArray;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.MapBuilder;
import com.unfoldlabs.redgreen.R;
import com.unfoldlabs.redgreen.adapter.SaveBatteryAdapter;
import com.unfoldlabs.redgreen.global.AppData;
import com.unfoldlabs.redgreen.global.ApplicationData;
import com.unfoldlabs.redgreen.global.GlobalAsyncTask;
import com.unfoldlabs.redgreen.global.MethodsForRedGreen;
import com.unfoldlabs.redgreen.interfaces.Constants;
import com.unfoldlabs.redgreen.interfaces.SaveBatteryListener;
import com.unfoldlabs.redgreen.model.ComparatorMethod;
import com.unfoldlabs.redgreen.model.SaveBatteryModel;
import com.unfoldlabs.redgreen.runningapps.CommonLibrary;
import com.unfoldlabs.redgreen.runningapps.ProcessDetailInfo;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class SaveBatteryActivity extends BaseActivity implements
        SaveBatteryListener, OnClickListener {
    private List<String> listDataHeader;
    private HashMap<String, List<SaveBatteryModel>> listDataChild;
    private List<SaveBatteryModel> systemAppsList = new ArrayList<SaveBatteryModel>();
    private List<SaveBatteryModel> backgroundAppsList = new ArrayList<SaveBatteryModel>();
    private List<SaveBatteryModel> memoryBoostList = new ArrayList<SaveBatteryModel>();
    private SparseBooleanArray systemAppsArray = new SparseBooleanArray();
    private SparseBooleanArray bagroundAppsArray = new SparseBooleanArray();
    private SparseBooleanArray memoryBoostArray = new SparseBooleanArray();
    private String[] appNames;
    private ArrayList<ProcessDetailInfo> mDetailList;
    private ActivityManager mActivityManager;
    private ExpandableListView expListView;
    private SaveBatteryAdapter listAdapter;

    private int[] systemAppIcons = new int[]{R.drawable.bluetooth,
            R.drawable.brightness, R.drawable.screen_timeout};
    private double cpuTemp;
    private double cpuTempFar;
    private boolean cpuState = false;
    private boolean cpuCheck;
    private boolean shutDownProcessCheck;
    private Button saveBatteryButton;
    private int brightnessLevel = 0;
    private float curBrightnessValue = 0;
    private int dialogSettingsgallery = -1;
    private boolean isDirectionSelection = true;
    private boolean fCheck = true;
    private boolean lockCheck;
    private MethodsForRedGreen method;
    private String processSavingsString = "";
    private String timeoutSavings = "";
    private float total = 0.0f;
    private String totalFinal1 = "";
    private String totalFinal2 = "";
    private float totalFinal = 0.0f;
    private float tempProcess = 0.0f;
    private float tempScreenTimeout = 0.0f;
    private double tempInFar;
    private SharedPreferences batterySharedPref, batterySettingSharedPref, cpuCoolerTempSharedPref;
    private Editor batteryEditor, batterySettingEditor, cpuCoolerTempEditor;
    public EasyTracker easyTracker;
    private Bundle bundle;
    public static SaveBatteryListener mLlistener;
    private double cpu_reduced_temp_in_far, cpu_reduced_temp_in_celcius;
    private ProgressDialog progressDialog, progressDialog2;
    private Dialog battery_dialog;
    private int checkDisplayTimeout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.savebatterylayout, frameLayout);
        mLlistener = this;
        easyTracker = EasyTracker.getInstance(getApplicationContext());
        mActivityManager = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
        expListView = (ExpandableListView) findViewById(R.id.lvExp_savebattery);
        saveBatteryButton = (Button) findViewById(R.id.save_battery_button);
        saveBatteryButton.setOnClickListener(this);
        batterySharedPref = getSharedPreferences("BATTERY_SHARED_PREF",
                Context.MODE_PRIVATE);
        batteryEditor = batterySharedPref.edit();

        batterySettingSharedPref = getSharedPreferences(
                "BATTERY_SETTINGS_SHARED_PREF", Context.MODE_PRIVATE);
        batterySettingEditor = batterySettingSharedPref.edit();

        cpuCoolerTempSharedPref = getSharedPreferences(
                "CPU_COOLER_TEMP_SHARED_PREF", Context.MODE_PRIVATE);
        cpuCoolerTempEditor = cpuCoolerTempSharedPref.edit();

        new LoadingProgressData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        bundle = getIntent().getExtras();
        if (bundle != null) {
            cpuTemp = bundle.getDouble("cpuTemp");
            cpuTempFar = bundle.getDouble("cpuTempFar");
            tempInFar = bundle.getDouble("cpuTempFar");
            cpu_reduced_temp_in_far = bundle.getDouble("cpuReducedTempFar");
            cpu_reduced_temp_in_celcius = bundle.getDouble("cpuReducedTempCel");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.findItem(R.id.menu_settings).setVisible(true);
        menu.findItem(R.id.menu_auto_clean).setVisible(false);
        menu.findItem(R.id.menu_auto_speed_setting).setVisible(false);
        menu.findItem(R.id.menu_auto_speed_memory).setVisible(false);
        menu.findItem(R.id.menu_dont_delete_apps).setVisible(false);
        menu.findItem(R.id.action_sort_by_lastused_asc).setVisible(false);
        menu.findItem(R.id.action_sort_by_lastused_des).setVisible(false);
        menu.findItem(R.id.action_sort_by_size_asc).setVisible(false);
        menu.findItem(R.id.action_sort_by_name_asc).setVisible(false);
        menu.findItem(R.id.action_sort_by_name_des).setVisible(false);
        menu.findItem(R.id.action_sort_by_size_des).setVisible(false);
        menu.findItem(R.id.action_name_ascending).setVisible(false);
        menu.findItem(R.id.action_name_decending).setVisible(false);
        menu.findItem(R.id.action_remove_after_invite).setVisible(false);
        menu.findItem(R.id.menu_celsius).setVisible(false);
        menu.findItem(R.id.menu_fahrenheit).setVisible(false);
        return true;
    }

    /**
     * selected option functionality.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sort:
                saveBatterySortingDialog();
                break;
            case R.id.menu_settings:
                saveBatterySettingDialog();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /*
     * Preparing the list data
     */
    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<SaveBatteryModel>>();
        // Adding child data
        listDataHeader.add(getString( R.string.savebattery_system_apps));
        listDataHeader.add(getString(R.string.savebattery_installed_apps));
        listDataHeader.add(getString(R.string.savebattery_cpucooling));
        listDataChild.put(listDataHeader.get(0), systemAppsList);
        listDataChild.put(listDataHeader.get(1), backgroundAppsList);
        listDataChild.put(listDataHeader.get(2), memoryBoostList);

    }

    private void getAllRunningProcess() {
        mDetailList = CommonLibrary.GetRunningProcess(getApplicationContext(), mActivityManager, Integer.MIN_VALUE, Constants.SECURITY_LEVEL, false);
        appNames = getResources().getStringArray(
                R.array.list_of_savebatteryapps);
        if(systemAppsList.isEmpty()){
            for (int i = 0; i < appNames.length; i++) {
                SaveBatteryModel item = new SaveBatteryModel();
                item.setInstalledAppName(appNames[i]);
                item.setInstalledAppIcon(systemAppIcons[i]);
                systemAppsList.add(item);
            }
        }
        if (null != backgroundAppsList && backgroundAppsList.isEmpty()) {
            if (null != AppData.getInstance().getSaveBatteryBagroundlist())
                backgroundAppsList = AppData.getInstance().getSaveBatteryBagroundlist();
        }
        if (null != memoryBoostList && memoryBoostList.isEmpty()) {
            if (null != AppData.getInstance().getSaveBatteryCpulist()) {
                memoryBoostList = AppData.getInstance().getSaveBatteryCpulist();
            }
        }
        prepareListData();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void expandGroupEvent(int groupPosition, boolean isExpanded) {
        if (isExpanded)
            expListView.collapseGroup(groupPosition);
        else
            expListView.expandGroup(groupPosition);
    }

    @Override
    public void GroupItemSelection(SparseBooleanArray systemAppsArray1,
                                   SparseBooleanArray bagroundAppsArray1,
                                   SparseBooleanArray memoryBoostArray1) {
        systemAppsArray = systemAppsArray1;
        bagroundAppsArray = bagroundAppsArray1;
        memoryBoostArray = memoryBoostArray1;
        buttonDisable();
        if (systemAppsArray.size() == systemAppsList.size()) {
            easyTracker.send(MapBuilder.createEvent(getString(R.string.save_battery),
                    getString(R.string.save_battery_system_apps_selected),
                    "track event", null).build());
            listAdapter.resetGrpCheckboxSystemApps(true);
        } else {
            listAdapter.resetGrpCheckboxSystemApps(false);
        }
        if (bagroundAppsArray.size() == backgroundAppsList.size()) {
            easyTracker.send(MapBuilder.createEvent(getString(R.string.save_battery),
                    getString(R.string.save_battery_apps_selected),
                    "track event", null).build());
            listAdapter.resetGrpCheckboxBagroundApps(true);
        } else {
            listAdapter.resetGrpCheckboxBagroundApps(false);
        }
        if (memoryBoostArray.size() == memoryBoostList.size()) {
            easyTracker.send(MapBuilder.createEvent(getString(R.string.save_battery),
                    getString(R.string.save_battery_cpu_apps_selected),
                    "track event", null).build());
            listAdapter.resetGrpCheckboxMemoryBoost(true);
        } else {
            listAdapter.resetGrpCheckboxMemoryBoost(false);
        }
    }

    @Override
    public void onClick(View v) {
        saveBatteryButton.setEnabled(false);
        saveBatteryButton.setTextColor(getResources().getColor(R.color.btn_disable));
        easyTracker.send(MapBuilder.createEvent(getString(R.string.save_battery),
                getString(R.string.battery_save_click_function),
                "track event", null).build());
        cpuCoolerTempEditor.putBoolean(getString(R.string.cpuCoolerTemp), false);
        cpuCoolerTempEditor.commit();
        new coolerAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    /**
     * Turns off Blue tooth if it is ON.
     */
    private void turnOffBlueTooth() {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter
                .getDefaultAdapter();

        if (null != mBluetoothAdapter && mBluetoothAdapter.isEnabled()) {
            mBluetoothAdapter.disable();
        }
    }

    /**
     * Device Brightness
     */
    private void brightNessLevel() {
        try {
            curBrightnessValue = android.provider.Settings.System.getInt(
                    getContentResolver(),
                    android.provider.Settings.System.SCREEN_BRIGHTNESS);
        } catch (SettingNotFoundException e) {
            e.printStackTrace();
        }
        final int screen_brightness = (int) curBrightnessValue;

        BroadcastReceiver batteryLevelReceiver = new BroadcastReceiver() {

            public void onReceive(Context context, Intent intent) {
                context.unregisterReceiver(this);
                brightnessLevel = (int) (65 * (2.55));

                if (screen_brightness >= brightnessLevel) {
                    brightNessOff(brightnessLevel);

                } else {
                }
            }
        };
        IntentFilter batteryLevelFilter = new IntentFilter(
                Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(batteryLevelReceiver, batteryLevelFilter);
    }

    private void brightNessOff(int level) {
        /**
         * Set the brightness of this window SCREEN_BRIGHTNESS The screen back
         * light brightness between 0 and 255.
         *
         */

        WindowManager.LayoutParams layoutpars = getWindow().getAttributes();
        android.provider.Settings.System.putInt(getContentResolver(),
                android.provider.Settings.System.SCREEN_BRIGHTNESS, level);
        layoutpars.screenBrightness = level / (float) 255;
        getWindow().setAttributes(layoutpars);
    }

    /**
     * Screen timeout sets to 30sec
     */
    private void screenTimeOut() {
        try {
            checkDisplayTimeout = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT);
            if (checkDisplayTimeout > 30000) {
                // the DisplayTimeout has changed
                tempScreenTimeout = checkDisplayTimeout;
                Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, 30000);
            }
        } catch (SettingNotFoundException e) {
            // The SCREEN_OFF_TIMEOUT setting didn't change 'cause it doesn't exist
        }

    }

    class LoadingProgressData extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            try {
                progressDialog2 = new ProgressDialog(SaveBatteryActivity.this);
                progressDialog2.setMessage("Loading please wait.");
                progressDialog2.setCanceledOnTouchOutside(false);
                progressDialog2.setCancelable(false);
                progressDialog2.show();
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
            try {
                if (progressDialog2.isShowing()) {
                    progressDialog2.dismiss();
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            samplemethod();
        }
    }

    /**
     * Button disable or enable for list items.
     */
    private void buttonDisable() {
        if (systemAppsArray.size() == 0 && bagroundAppsArray.size() == 0
                && memoryBoostArray.size() == 0) {
            saveBatteryButton.setEnabled(false);
            saveBatteryButton.setTextColor(getResources().getColor(R.color.btn_disable));
        } else {
            saveBatteryButton.setEnabled(true);
            saveBatteryButton.setTextColor(Color.WHITE);
        }
    }

    private void samplemethod() {
        listAdapter = new SaveBatteryAdapter(SaveBatteryActivity.this,
                listDataHeader, listDataChild, cpuTemp);
        listAdapter.setListener(SaveBatteryActivity.this);
        expListView.setAdapter(listAdapter);
        expListView.expandGroup(2);
        expListView.setOnGroupClickListener(new OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                return false;
            }
        });

        // Listview Group expanded listener
        expListView.setOnGroupExpandListener(new OnGroupExpandListener() {
            int previousGroup = -1;

            @Override
            public void onGroupExpand(int groupPosition) {
                if (groupPosition != previousGroup)
                    expListView.collapseGroup(previousGroup);
                previousGroup = groupPosition;
            }
        });

        // Listview Group collasped listener
        expListView.setOnGroupCollapseListener(new OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
            }
        });

        // Listview on child click listener
        expListView.setOnChildClickListener(new OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                return false;
            }
        });

        fCheck = batterySettingSharedPref.getBoolean(getString(R.string.batterysetting), true);
        isDirectionSelection = batterySharedPref.getBoolean(getString(R.string.batterysorting), isDirectionSelection);
        if (fCheck) {
            cpuTempFar = tempInFar;
            cpuState = false;
            listAdapter.CpuTempSet(cpuTempFar, cpuState);
            /** AWS setCpuHeatInitially**/
            ApplicationData.getInstance().setCpuHeatInitially("" + cpuTempFar);
            ApplicationData.getInstance().setDisplayTempFrom("" + cpuTempFar);

        } else {
        }
        if (isDirectionSelection) {
            ascendinglist();
        } else {
            descendlist();
        }
    }

    private void listClearbuttonDisable() {
        if (systemAppsList.size() <= 0 || backgroundAppsList.size() <= 0
                || memoryBoostList.size() <= 0) {
            saveBatteryButton.setEnabled(false);
            saveBatteryButton.setTextColor(getResources().getColor(R.color.btn_disable));

        } else {
            saveBatteryButton.setEnabled(true);
            saveBatteryButton.setTextColor(Color.WHITE);
        }
    }

    /**
     * Battery Save Dialog Box View
     */
    @SuppressLint("SetJavaScriptEnabled")
    private void batterysave_dialogbox() {
        try {
            battery_dialog = new Dialog(this);
            battery_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            battery_dialog
                    .setContentView(R.layout.savebattery_percentages_dialogue);
            battery_dialog.setCanceledOnTouchOutside(false);
            battery_dialog.setCancelable(false);
            TextView button_ok = (TextView) battery_dialog.findViewById(R.id.ok);
            TextView button_timeout = (TextView) battery_dialog
                    .findViewById(R.id.timeout);
            TextView button_shutting = (TextView) battery_dialog
                    .findViewById(R.id.shutting);
            TextView button_approx = (TextView) battery_dialog
                    .findViewById(R.id.approx);
            TextView button_cputemp = (TextView) battery_dialog
                    .findViewById(R.id.cpu_temp_reduced);

            if (lockCheck == true) {
                if (timeoutSavings.isEmpty()) {
                    button_timeout.setText("0.00 %");
                }else if(batterySharedPref.getString("screenValue", "").equals(String.valueOf(tempScreenTimeout).substring(0, 4))){
                    button_timeout.setText("0.00 %");
                } else{
                    button_timeout.setText(String.valueOf(tempScreenTimeout).substring(0, 4) + " %");
                    totalFinal1 = String.valueOf(tempScreenTimeout).substring(0,4);
                    totalFinal = totalFinal + Float.valueOf(totalFinal1);
                    batteryEditor.putString("screenValue", String.valueOf(tempScreenTimeout).substring(0, 4));
                    batteryEditor.commit();
                }
            }
            if (shutDownProcessCheck == true) {

                if (processSavingsString.isEmpty()) {
                    button_shutting.setText("0.00 %");
                } else {
                    try {
                        if(batterySharedPref.getString("shutDownFstValue", "").equals(String.valueOf(tempProcess).substring(0, 4))){
                            button_shutting.setText("0.00 %");
                        }else {
                            totalFinal2 = String.valueOf(tempProcess).substring(0, 4);
                            button_shutting.setText(String.valueOf(tempProcess).substring(0, 4) + " %");
                            totalFinal = totalFinal + Float.valueOf(totalFinal2);
                            batteryEditor.putString("shutDownFstValue", String.valueOf(tempProcess).substring(0, 4));
                            batteryEditor.commit();
                        }
                    } catch (StringIndexOutOfBoundsException e) {
                        e.printStackTrace();
                    }
                }
            } else if (shutDownProcessCheck == false) {
                button_shutting.setText("0.00 %");
            }
            if (totalFinal == 0) {
                button_approx.setText("0.00 %");
            } else {
                try{
                    totalFinal = Float.valueOf(totalFinal1)+Float.valueOf(totalFinal2);

                }catch(NumberFormatException ex){
                    //either a or b is not a number
                    //  result = "Invalid input";
                }
                button_approx.setText(String.format("%.2f", totalFinal) + " %");
            }
            DecimalFormat formatter = new DecimalFormat("#0.00");
            if (cpuCheck == true) {

                if (fCheck == true) {
                    button_cputemp.setText(formatter.format(cpu_reduced_temp_in_far)
                            + " \u00B0F");
                } else {
                    button_cputemp.setText(formatter.format(cpu_reduced_temp_in_celcius)
                            + " \u00B0C");
                }
            } else {
                if (cpuState == true) {
                    button_cputemp.setText(0 + " \u00B0C");
                } else if (fCheck == true) {
                    button_cputemp.setText(0 + " \u00B0F");
                }
            }
            cpuSettings();
            battery_dialog.setCanceledOnTouchOutside(false);

            battery_dialog.setDismissMessage(null);
            battery_dialog.setTitle(null);


            battery_dialog.show();
            button_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    easyTracker.send(MapBuilder.createEvent(getString(R.string.save_battery),
                            getString(R.string.battery_save_dialog_navigate),
                            "track event", null).build());
                    if (battery_dialog.isShowing()) {
                        battery_dialog.dismiss();
                    }
                    finish();
                }
            });
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        ApplicationData.getInstance().setDisplayTempTo("" + cpuTempFar);
        ApplicationData.getInstance().setCpuTempLastly("" + cpuTempFar);
    }

    //*//** Converts to celsius **//*
    private void convertFahrenheitToCelsius() {
        cpuState = true;
        listAdapter.CpuTempSet(cpuTemp, cpuState);
    }


    //*//** Converts to fahrenheit **//*
    private void convertCelsiusToFahrenheit() {
        cpuTempFar = tempInFar;
        cpuState = false;
        listAdapter.CpuTempSet(cpuTempFar, cpuState);
    }

    private void ascendinglist() {
        Collections.sort(backgroundAppsList,
                ComparatorMethod.saveBatteryNewAsceComparator);
        Collections.sort(memoryBoostList,
                ComparatorMethod.saveBatteryNewAsceCpuComparator);
    }

    private void descendlist() {
        Collections.sort(backgroundAppsList,
                ComparatorMethod.saveBatteryNewDescComparator);

        Collections.sort(memoryBoostList,
                ComparatorMethod.saveBatteryNewDescCpuComparator);
    }

    private void saveBatterySettingDialog() {
        final Dialog saveBatterySettingDialog = new Dialog(
                SaveBatteryActivity.this);
        saveBatterySettingDialog.setCanceledOnTouchOutside(false);
        saveBatterySettingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        saveBatterySettingDialog
                .setContentView(R.layout.save_battery_settings_dialog);
        final CheckBox fahrenheit = (CheckBox) saveBatterySettingDialog
                .findViewById(R.id.fahrenheit);
        final CheckBox celcius = (CheckBox) saveBatterySettingDialog
                .findViewById(R.id.celcius);
        TextView settings_done = (TextView) saveBatterySettingDialog
                .findViewById(R.id.button_done);

        fahrenheit.setChecked(batterySettingSharedPref.getBoolean(
                getString(R.string.batterysetting),
                fCheck));
        cpuSettings();
        easyTracker.send(MapBuilder.createEvent(getString(R.string.save_battery),
                "Settings dialog is displayed",
                "track event", null).build());
        WindowManager.LayoutParams wmlp = saveBatterySettingDialog.getWindow()
                .getAttributes();

        wmlp.gravity = Gravity.TOP | Gravity.CENTER;

        int density = getResources().getDisplayMetrics().densityDpi;
        switch (density) {

            case DisplayMetrics.DENSITY_LOW:
                wmlp.x = 160;
                wmlp.y = 40;

                break;
            case DisplayMetrics.DENSITY_MEDIUM:
                wmlp.x = 160;
                wmlp.y = 50;

                break;
            case DisplayMetrics.DENSITY_HIGH:
                wmlp.x = 160;
                wmlp.y = 60;

                break;
            case DisplayMetrics.DENSITY_XHIGH:
                wmlp.x = 200;
                wmlp.y = 80;
                break;

            case DisplayMetrics.DENSITY_XXHIGH:
                wmlp.x = 160;
                wmlp.y = 120;
                break;
            case DisplayMetrics.DENSITY_XXXHIGH:
                wmlp.x = 160;
                wmlp.y = 200;
                break;
        }
        saveBatterySettingDialog.show();
        settings_done.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                easyTracker.send(MapBuilder.createEvent(getString(R.string.save_battery),
                        getString(
                                R.string.setting_dialog_done_button),
                        "track event", null).build());
                batterySettingEditor.putBoolean(
                        getString(R.string.batterysetting),
                        fCheck);
                batterySettingEditor.commit();
                saveBatterySettingDialog.dismiss();
                cpuSettings();

            }
        });

        fCheck = batterySettingSharedPref.getBoolean(
                getString(R.string.batterysetting), true);
        fahrenheit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                fahrenheit.setChecked(true);
                celcius.setChecked(false);
                fCheck = true;
            }
        });

        celcius.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                fahrenheit.setChecked(false);
                celcius.setChecked(true);
                fCheck = false;
            }

        });
        if (fCheck == true) {
            fahrenheit.setChecked(true);
            celcius.setChecked(false);
        } else if (fCheck == false) {
            celcius.setChecked(true);
            fahrenheit.setChecked(false);
        }

    }


    private void saveBatterySortingDialog() {
        final Dialog saveBatterySortingDlg = new Dialog(
                SaveBatteryActivity.this);
        saveBatterySortingDlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        saveBatterySortingDlg.setContentView(R.layout.cleanapp_sorting_dialog);

        TextView button_ok = (TextView) saveBatterySortingDlg
                .findViewById(R.id.button_done);

        RadioGroup appSettingGroup = (RadioGroup) saveBatterySortingDlg
                .findViewById(R.id.app_setting_grp);
        RadioGroup askBeforeAscendingGroup = (RadioGroup) saveBatterySortingDlg
                .findViewById(R.id.ask_bfore_grp);
        easyTracker.send(MapBuilder.createEvent(getString(R.string.save_battery),
                getString(R.string.save_battery_sorting_dialogue_entered),
                "track event", null).build());
        WindowManager.LayoutParams wmlp = saveBatterySortingDlg.getWindow()
                .getAttributes();

        wmlp.gravity = Gravity.TOP | Gravity.CENTER;

        int density = getResources().getDisplayMetrics().densityDpi;
        switch (density) {
            case DisplayMetrics.DENSITY_HIGH:
                wmlp.x = 160;
                wmlp.y = 60;

                break;
            case DisplayMetrics.DENSITY_XHIGH:
                wmlp.x = 200;
                wmlp.y = 80;
                break;

            case DisplayMetrics.DENSITY_XXHIGH:
                wmlp.x = 160;
                wmlp.y = 120;
                break;
            case DisplayMetrics.DENSITY_XXXHIGH:
                wmlp.x = 160;
                wmlp.y = 200;
                break;
        }

        if (dialogSettingsgallery == -1) {
            appSettingGroup.check(R.id.name);
        }
        isDirectionSelection = batterySharedPref.getBoolean(getString(R.string.batterysorting), isDirectionSelection);
        if (isDirectionSelection) {
            askBeforeAscendingGroup.check(R.id.ascending);
        } else {
            askBeforeAscendingGroup.check(R.id.decending);
        }

        appSettingGroup
                .setOnCheckedChangeListener(new OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {

                        switch (checkedId) {
                            case R.id.name:
                                dialogSettingsgallery = -1;
                                break;
                            default:
                                break;
                        }
                    }
                });

        askBeforeAscendingGroup
                .setOnCheckedChangeListener(new OnCheckedChangeListener() {

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

        saveBatterySortingDlg.show();
        button_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                easyTracker.send(MapBuilder.createEvent(
                        getString(R.string.save_battery),
                        getString(
                                R.string.sorting_dialog_done_button),
                        "track event", null).build());
                if (isDirectionSelection == true) {
                    ascendinglist();
                } else if (isDirectionSelection == false) {
                    descendlist();
                }
                batteryEditor.putBoolean(getString(R.string.batterysorting),
                        isDirectionSelection);
                batteryEditor.commit();
                listAdapter.notifyDataSetChanged();
                saveBatterySortingDlg.dismiss();
            }

        });
    }

    private class coolerAsyncTask extends AsyncTask<Void, Void, Void> {
        public coolerAsyncTask() {
        }

        @Override
        protected void onPreExecute() {
            try {
                progressDialog = new ProgressDialog(SaveBatteryActivity.this);
                progressDialog.setMessage("Processing...");
                progressDialog.setIndeterminateDrawable(getResources().getDrawable(R.drawable.green_progress));
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setCancelable(false);
                progressDialog.show();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onPostExecute(Void result) {
            try {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            buttonDisable();
            listClearbuttonDisable();
            memorySave();
            batterysave_dialogbox();
            listAdapter.notifyDataSetChanged();
            listAdapter.notifyDataSetInvalidated();
            expListView.invalidate();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                if (systemAppsArray != null) {
                    for (int X = systemAppsArray.size() - 1; X >= 0; X--) {
                        int key = systemAppsArray.keyAt(X);
                        boolean value = systemAppsArray.get(key);

                        if (key == 0 && value == true) {
                            turnOffBlueTooth();
                        }
                        if (key == 1 && value == true) {
                            brightNessLevel();
                        }
                        if (key == 2 && value == true) {
                            screenTimeOut();
                            lockCheck = true;
                        }
                    }

                }
                for (int X = bagroundAppsArray.size() - 1; X >= 0; X--) {
                    int key = bagroundAppsArray.keyAt(X);
                    boolean value = bagroundAppsArray.get(key);
                    SaveBatteryModel listItemName = (SaveBatteryModel) listAdapter.getChild(1, key);
                    String runningAppname = listItemName.getPackageName();
                    mActivityManager.killBackgroundProcesses(runningAppname);
                    if (value) {
                        shutDownProcessCheck = true;
                    }
                }

                for (int X = memoryBoostArray.size() - 1; X >= 0; X--) {
                    int key = memoryBoostArray.keyAt(X);
                    boolean value = memoryBoostArray.get(key);
                    SaveBatteryModel listItemName = (SaveBatteryModel) listAdapter.getChild(0, key);
                    String runningAppname = listItemName.getApptitle();
                    for (ProcessDetailInfo detailInfo : mDetailList) {
                        if (!detailInfo.getLabel().equalsIgnoreCase(runningAppname)) {
                            detailInfo.setSelected(true);
                        }
                    }
                    CommonLibrary.KillProcess(getApplicationContext(), mDetailList, mActivityManager, true);

                    if (value) {
                        cpuCheck = true;
                    }

                }
                memoryBoostArray.clear();
                bagroundAppsArray.clear();

                AppData.getInstance().setSaveBatteryBagroundlist(backgroundAppsList);
                AppData.getInstance().setSaveBatteryCpulist(memoryBoostList);
                new GlobalAsyncTask(getApplicationContext())
                        .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }

    }

    private void memorySave() {

        // gps save start
        method = new MethodsForRedGreen();
        // gps save end

        // battery save start
        int custSettings = bagroundAppsArray.size();

        /** AWS setNoOfProcessesShuttingown, **/
        ApplicationData.getInstance().setNoOfProcessesShuttingown("" + bagroundAppsArray.size());

        float tempProcess = (float) method.getProcessBatterySavings(
                custSettings, getApplicationContext());
        this.tempProcess = tempProcess;
        processSavingsString = String.format("%.2f", tempProcess) + "%";
        /** AWS setShuttingDownPercentage, **/
        ApplicationData.getInstance().setShuttingDownPercentage("" + processSavingsString);
        ApplicationData.getInstance().setShuttingDownMemorySaved("" + tempProcess);
        total = total + tempProcess;
        // battery save end

        // Bluetooth BatterySavings start
        float tempBluetooth = method.getBluetoothBatterySavings();
        total = total + tempBluetooth;
        // Bluetooth End

        // Screen BatterySaving Start
        int needBrightness = (int) (65 * 2.55);
        int curBrightnessValue = 0;
        try {
            curBrightnessValue = android.provider.Settings.System.getInt(
                    getContentResolver(),
                    android.provider.Settings.System.SCREEN_BRIGHTNESS);

            /** AWS setScreenBrightnessFrom, **/
            ApplicationData.getInstance().setScreenBrightnessFrom("" + curBrightnessValue);
        } catch (SettingNotFoundException e) {
            e.printStackTrace();
        }
        ApplicationData.getInstance().setScreenTimeoutTo("" + needBrightness);
        if (needBrightness <= curBrightnessValue) {
            float tempScreen = method.getScreenBrightNessSavings(
                    curBrightnessValue, needBrightness);
            /** AWS setScreenBrightnessFrom **/
            ApplicationData.getInstance().setScreenBrightnessPercentage("" + tempScreen);
            total = total + tempScreen;
        }
        // End Screen Battery Save

        // Screen TImeout Saving

        /** AWS setScreenTimeoutFrom, setScreenTimeoutTo, setScreenTimeoutPercentage**/
        ApplicationData.getInstance().setScreenTimeoutFrom("" + checkDisplayTimeout / 1000);

        float systemTimeout = checkDisplayTimeout / 1000;
        float tempScreenTimeout = method.getScreenTimeoutSavings(
                systemTimeout / 60, (int) 0.5);
        this.tempScreenTimeout = tempScreenTimeout;
        ApplicationData.getInstance().setScreenTimeoutTo("" + tempScreenTimeout);
        timeoutSavings = String.format("%.2f", tempScreenTimeout) + "%";
        ApplicationData.getInstance().setScreenTimeoutPercentage("" + timeoutSavings);
        total = total + tempScreenTimeout;
        // end Screen TImeout Saving
    }

    public void cpuSettings() {
        if (fCheck) {
            convertCelsiusToFahrenheit();
        } else {
            convertFahrenheitToCelsius();
        }
    }

    @Override
    public void getBackRoundApps() {
        try {
            getAllRunningProcess();
        } catch (Exception e) {
        }
        samplemethod();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        if (progressDialog2 != null && progressDialog2.isShowing()) {
            progressDialog2.dismiss();
        }
        if (battery_dialog != null && battery_dialog.isShowing()) {
            battery_dialog.dismiss();
        }
        if(systemAppsList.size() <=0) {
            if (systemAppsList != null) {
                systemAppsList.clear();
            }
        }
        if(backgroundAppsList.size() <=0) {
            if (backgroundAppsList != null) {
                backgroundAppsList.clear();
            }
        }
        if(memoryBoostList.size() <=0) {
            if (memoryBoostList != null) {
                memoryBoostList.clear();
            }
        }

        easyTracker.send(MapBuilder.createEvent(getString(R.string.save_battery),
                getString(R.string.save_battery_exit_time),
                "track event", null).build());
    }
}
