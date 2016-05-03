package com.unfoldlabs.redgreen.activity;

import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.facebook.appevents.AppEventsLogger;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.MapBuilder;
import com.unfoldlabs.redgreen.R;
import com.unfoldlabs.redgreen.adapter.DrawerItemCustomAdapter;
import com.unfoldlabs.redgreen.config.Notifications;
import com.unfoldlabs.redgreen.db.DatabaseHandler;
import com.unfoldlabs.redgreen.db.SavingDBAdapter;
import com.unfoldlabs.redgreen.db.SavingDBAdapter.RATE_TABLE;
import com.unfoldlabs.redgreen.global.AppData;
import com.unfoldlabs.redgreen.global.ApplicationData;
import com.unfoldlabs.redgreen.interfaces.AppRateOnClickButtonListener;
import com.unfoldlabs.redgreen.interfaces.Constants;
import com.unfoldlabs.redgreen.log.Applog;
import com.unfoldlabs.redgreen.model.AlarmModel;
import com.unfoldlabs.redgreen.model.AppRate;
import com.unfoldlabs.redgreen.model.AppRatePreferenceHelper;
import com.unfoldlabs.redgreen.model.AppsInfo;
import com.unfoldlabs.redgreen.model.CacheList;
import com.unfoldlabs.redgreen.model.CleanAppDB;
import com.unfoldlabs.redgreen.model.NavigationDrawerItems;

import com.unfoldlabs.redgreen.receiver.CleanAppReciever;
import com.unfoldlabs.redgreen.utilty.Bean;
import com.unfoldlabs.redgreen.utilty.Utility;

import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

@SuppressWarnings("deprecation")
public class BaseActivity extends FragmentActivity {

    private SharedPreferences.Editor prefEditor;
    private SharedPreferences sharedpreferences;
    private String value;
    protected FrameLayout frameLayout;
    public DrawerLayout mDrawerLayout;
    public ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    public List<NavigationDrawerItems> items;
    private DrawerItemCustomAdapter adapter;
    private List<CleanAppDB> installedAppsList = new ArrayList<CleanAppDB>();
    private ArrayList<Integer> positions = new ArrayList<Integer>();
    private HashMap<Integer, String> hashMap = new HashMap<Integer, String>();
    private List<AppsInfo> listApps = new ArrayList<AppsInfo>();
    private DatabaseHandler database;
    public EasyTracker easyTracker = null;
    private TelephonyManager telephonyManager;
    public static boolean isAskbeforeSelection;
    private final static boolean IS_ASK_BEFORE_DELETE = true;
    public static int dialogSettings;
    private final static int DIALOG_SETTINGS = -1;
    public boolean bluetooth_state;
    // Clean App Dialogue
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    private int cleanAppSettings = -1;
    // Clean memory dialogue
    private int repeatSetting2 = -1;
    private boolean windowState = false;
    private SharedPreferences sp_cleanApp;
    private SharedPreferences.Editor ed_cleanApp;
    public int actionbarheight;
    private SharedPreferences sp_cleanMem;
    private SharedPreferences.Editor ed_cleanMem;
    private SharedPreferences sharedPreferencesAppLock;
    private String dynamicMaxJunkCacheMemorySize;
    private String dynamicJunkCacheMemorySize;
    private String dynamicMinJunkCacheMemorySize;
    private SharedPreferences sp;
    private String tempVal, tempValMin;
    public static final String FINISH_ALL_ACTIVITIES_ACTIVITY_ACTION = "com.unfoldlabs.redgreen.FINISH_ALL_ACTIVITIES_ACTIVITY_ACTION";
    public static final IntentFilter INTENT_FILTER = createIntentFilter();
    private BaseActivityReceiver baseActivityReceiver = new BaseActivityReceiver();

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        Drawable d = getResources().getDrawable(R.drawable.actionbar_baground);
        getActionBar().setBackgroundDrawable(d);
        getActionBar().setIcon(R.drawable.action_bar_icon);
        getActionBar().setDisplayUseLogoEnabled(true);
        getActionBar().setDisplayShowHomeEnabled(true);
        getActionBar().setDisplayShowTitleEnabled(true);
        easyTracker = EasyTracker.getInstance(this);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        actionbarheight = getActionBar().getHeight();
        sp_cleanApp = getSharedPreferences(getResources().getString(R.string.cleanapp_pref), Context.MODE_PRIVATE);
        ed_cleanApp = sp_cleanApp.edit();

        sp_cleanMem = getSharedPreferences(getResources().getString(R.string.cleanmemory_pref), Context.MODE_PRIVATE);
        ed_cleanMem = sp_cleanMem.edit();
        new AlarmModel();
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (null != mBluetoothAdapter && mBluetoothAdapter.isEnabled()) {
            bluetooth_state = true;
        } else if (null != mBluetoothAdapter && !mBluetoothAdapter.isEnabled()) {
            bluetooth_state = false;
        }
        setContentView(R.layout.base_navigationdrawer);
        frameLayout = (FrameLayout) findViewById(R.id.content_frame);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        items = new ArrayList<NavigationDrawerItems>();
        String[] mNavigationDrawerItemTitles = getResources().getStringArray(
                R.array.drawer_items);
        for (int i = 0; i < mNavigationDrawerItemTitles.length; i++) {
            NavigationDrawerItems obj = new NavigationDrawerItems();
            obj.setItemName(mNavigationDrawerItemTitles[i]);
            items.add(obj);
        }
        adapter = new DrawerItemCustomAdapter(this,
                R.layout.test_drawer_list_tem, items);
        mDrawerList.setAdapter(adapter);
        for (int i = 0; i < items.size(); i++) {
            adapter.setmSelectedItem(items.size());
        }
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, R.string.drawer_open,
                R.string.drawer_close) {
            /**
             * Called when a drawer has settled in a completely closed state.
             */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                /** AWS setExitedMenuScreen  **/
                ApplicationData.getInstance().setExitedMenuScreen("" + R.string.drawer_close);
            }
            /**
             * Called when a drawer has settled in a completely open state.
             */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        makeActionOverflowMenuShown();
        database = new DatabaseHandler(this);
        if (this instanceof HomeActivity) {
            showRateCount();
        }
        installedAppsList = database.getAllApps(getPackageManager());
        sharedPref = getSharedPreferences("MY_SHARED_PREF", Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        editor.putBoolean("notify", true);
        editor.commit();

        dialogSettings = sharedPref.getInt(getString(R.string.dialog_setting),DIALOG_SETTINGS);
        isAskbeforeSelection = sharedPref.getBoolean(getString(R.string.ask_bfore_delete), IS_ASK_BEFORE_DELETE);

        if ((this instanceof HomeActivity)) {
            boolean cleanAppSwitch = sharedPref.getBoolean(
                    getResources().getString(R.string.cleanappswitch), true);
            boolean cleanMemorySwitch = sharedPref.getBoolean(getResources().getString(R.string.cleanmemoryswitch), true);
            if (cleanAppSwitch) {
                CleanAppReciever.setList(installedAppsList);
                CleanAppReciever.setAlarms(BaseActivity.this);
            }
            if (cleanMemorySwitch) {
                Bean.setAlarm(BaseActivity.this);
            }
        }
        getBoostOnMemory();
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        /**
         * Sync the toggle state after onRestoreInstanceState has occurred.
         */
        if (mDrawerToggle != null) {
            mDrawerToggle.syncState();
        }
    }

    public void changelayout() {
    }

    private void makeActionOverflowMenuShown() {
        /**
         * devices with hardware menu button (e.g. Samsung Note) don't show
         * action overflow menu
         */
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class
                    .getDeclaredField("sHasPermanentMenuKey");
            if (menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception e) {
            Applog.logString(e.getLocalizedMessage());
        }
    }

    /**
     * On option item click functionality
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        mDrawerLayout.closeDrawers();
        if (mDrawerToggle != null) {
            if (mDrawerToggle.onOptionsItemSelected(item)) {
                return true;
            }
        }
        switch (item.getItemId()) {
            case R.id.menu_auto_clean:
                if (!(this instanceof AppManagerActivity))
                    Utility.showAlertDialog(BaseActivity.this, getResources()
                            .getString(R.string.not_implemented));
                break;
            case android.R.id.home:
                if (!(this instanceof HomeActivity))
                    finish();
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Creating options for menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    protected void onResume() {
        super.onResume();
        overridePendingTransition(0, 0);
        if (!(this instanceof AppManagerActivity)) {
            sharedpreferences = getSharedPreferences(
                    getResources().getString(R.string.preference_name),
                    Context.MODE_PRIVATE);
            value = sharedpreferences
                    .getString(
                            getResources().getString(
                                    R.string.sharedpreference_key), "");
            if (value.equalsIgnoreCase(getResources().getString(
                    R.string.list_view))) {
                items.get(1).setItemName(getResources().getString(R.string.grid_view));
                adapter.notifyDataSetChanged();
                changelayout();

            } else {
                items.get(1).setItemName(getResources().getString(R.string.list_view));
                adapter.notifyDataSetChanged();
                changelayout();
            }
        }
        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            SavingDBAdapter.getInstance().getRate().close();
        }catch (NullPointerException e){
            e.printStackTrace();
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
    protected void onPause() {
        super.onPause();
        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }
    /**
     * functionality to navigation drawer item selection
     */
    private class DrawerItemClickListener implements
            ListView.OnItemClickListener {


        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            TextView textViewName = (TextView) view.findViewById(R.id.textViewName);
            selectItem(position);
            mDrawerLayout.closeDrawers();
            easyTracker.send(MapBuilder.createEvent("Home Screen ", "Navigation menu closed",
                    "track event", null).build());
            /** AWS setMenuItemSelected, setNavigationMenuEnteredModule**/
            ApplicationData.getInstance().setMenuItemSelected("" + items.get(position).getItemName());
            ApplicationData.getInstance().setNavigationMenuEnteredModule("" + items.get(position).getItemName());

            switch (position) {
                case 0:
                    adapter.notifyDataSetChanged();
                    break;
                case 1:
                    String selected = String.valueOf((textViewName).getText());
                    sharedpreferences = getSharedPreferences(getResources()
                            .getString(R.string.preference_name),Context.MODE_PRIVATE);
                    prefEditor = sharedpreferences.edit();
                    prefEditor.putString(getResources().getString(
                                            R.string.sharedpreference_key),
                                    selected);
                    prefEditor.commit();
                    items.get(position).setItemName(selected);

                    /**
                     * if the name is LIST VIEW then text will change to GRID VIEW
                     * and layout also changes to Grid View
                     */
                    if (selected.equalsIgnoreCase(getResources().getString(
                            R.string.list_view))) {
                        items.get(position).setItemName(
                                getResources().getString(R.string.grid_view));

                        easyTracker.send(MapBuilder.createEvent(
                                getResources().getString(R.string.home_type_grid),
                                getResources().getString(
                                        R.string.home_type_grid_navigate),
                                "track event", null).build());

                        adapter.notifyDataSetChanged();
                        changelayout();
                    }
                    /**
                     * if the name is GRID VIEW then text will change to LIST VIEW
                     * and layout also changes to List View
                     */
                    else {
                        items.get(position).setItemName(
                                getResources().getString(R.string.list_view));
                        easyTracker.send(MapBuilder.createEvent(
                                getResources().getString(R.string.home_type_list),
                                getResources().getString(
                                        R.string.home_type_list_navigate),
                                "track event", null).build());

                        adapter.notifyDataSetChanged();
                        changelayout();
                    }
                   // startActivity(new Intent(BaseActivity.this, HomeActivity.class));
                    //adapter.notifyDataSetChanged();
                    break;

                /**
                 * functionality for click on navigation drawer item Auto Clean App
                 * selection option
                 */
                case 2:
                    easyTracker.send(MapBuilder.createEvent(
                            getResources().getString(R.string.auto_clean_dialog),
                            getResources().getString(
                                    R.string.auto_clean_dialog_navigate),
                            "track event", null).build());
                    cleanAppSettingsDialog(BaseActivity.this, windowState);
                    adapter.notifyDataSetChanged();
                    break;

                /**
                 * functionality for click on navigation drawer item Rate Us
                 * selection option
                 */
                case 3:
                    cleanMemorySettingsDialog(BaseActivity.this, windowState);
                    easyTracker.send(MapBuilder.createEvent(
                            getResources().getString(R.string.auto_speed_dialog),
                            getResources().getString(
                                    R.string.auto_speed_dialog_navigate),
                            "track event", null).build());
                    adapter.notifyDataSetChanged();
                    break;

                /**
                 * functionality for click on navigation drawer item Notifications
                 * ON/OFF selection option
                 */
                case 4:
                    cleanMemoryByMemoryDialog(BaseActivity.this, windowState);
                    easyTracker.send(MapBuilder.createEvent(
                            getResources().getString(R.string.auto_speed_dialog),
                            getResources().getString(
                                    R.string.auto_speed_dialog_navigate),
                            "track event", null).build());
                    adapter.notifyDataSetChanged();

                    break;
                case 5:
                    notificationSettingDialog(BaseActivity.this);
                    adapter.notifyDataSetChanged();
                    easyTracker.send(MapBuilder.createEvent(
                            getResources().getString(
                                    R.string.notifications_activity),
                            getResources().getString(
                                    R.string.notifications_activity_navigate),
                            "track event", null).build());

                    break;
                /**
                 * functionality for click on navigation drawer item Recommend Us
                 * selection option
                 */
                case 6:
                    AppData.getInstance().setApplockExit(true);
                    String packageName = getPackageName();
                    Intent rateIntent = new Intent(Intent.ACTION_VIEW,
                            AppRatePreferenceHelper.getGooglePlay(packageName));
                    if (AppRatePreferenceHelper.isPackageExists(getApplicationContext(),
                            AppRatePreferenceHelper.GOOGLE_PLAY_PACKAGE_NAME)) {
                        rateIntent
                                .setPackage(AppRatePreferenceHelper.GOOGLE_PLAY_PACKAGE_NAME);
                    }
                    startActivity(rateIntent);
                    easyTracker.send(MapBuilder.createEvent(
                            getResources().getString(
                                    R.string.rate_us_google_play_dialog),
                            getResources().getString(
                                    R.string.rate_us_google_play_dialog_navigate),
                            "track event", null).build());
                    AppRatePreferenceHelper.setAgreeShowDialog(
                            getApplicationContext(), false);

                    break;

                /**
                 * functionality for click on navigation drawer item Savings So Far
                 * selection option
                 */

                case 7:
                    AppData.getInstance().setApplockExit(true);
                    Intent intentContact = new Intent(Intent.ACTION_PICK,
                            Contacts.CONTENT_URI);
                    /**
                     * calling OnActivityResult with intent And Some contact for
                     * Identifie
                     */
                    startActivityForResult(intentContact,
                            Constants.RECOMENDED_RESULT_CODE);
                    easyTracker.send(MapBuilder.createEvent(
                            getResources()
                                    .getString(R.string.recommend_us_activity),
                            getResources().getString(
                                    R.string.recommend_us_activity_navigate),
                            "track event", null).build());

                    break;

                /**
                 * functionality for click on navigation drawer item About Us
                 * selection option
                 */

                case 8:
                    savingDialog();
                    easyTracker
                            .send(MapBuilder
                                    .createEvent(
                                            getResources().getString(
                                                    R.string.saving_so_far_dialog),
                                            getResources()
                                                    .getString(
                                                            R.string.saving_so_far_dialog_navigate),
                                            "track event", null).build());
                    adapter.notifyDataSetChanged();
                    break;
                case 9:
                    sharedPreferencesAppLock = getSharedPreferences(Constants.MyPREFERENCES, Context.MODE_PRIVATE);
                    String password = String.valueOf(sharedPreferencesAppLock.getInt("passwordFinal", 0));
                    SharedPreferences forgotEmailSharedPref = getSharedPreferences("APPLOCK_FORGET_PASSWORD", MODE_PRIVATE);
                    String forgotEmailDone = forgotEmailSharedPref.getString(getResources().getString(R.string.forgot_email_done), "");

                    if (password.equalsIgnoreCase("")) {
                        Toast.makeText(getApplicationContext(), "Please provide your AppLock Password", Toast.LENGTH_SHORT).show();
                    } else if (forgotEmailDone.equalsIgnoreCase("")) {
                        Toast.makeText(getApplicationContext(), "Please provide your AppLock Email ID", Toast.LENGTH_SHORT).show();
                    } else if (password.equalsIgnoreCase("") && forgotEmailDone.equalsIgnoreCase("")) {
                        Toast.makeText(getApplicationContext(), "Please provide your AppLock Password & Email ID", Toast.LENGTH_SHORT).show();
                    } else {
                        Utility.postMethod(forgotEmailDone, password, BaseActivity.this);
                    }

                    easyTracker.send(MapBuilder.createEvent(getString(R.string.app_lock),
                            getString(R.string.app_lock_forgot_password_sent_email),
                            "track event", null).build());
                    adapter.notifyDataSetChanged();
                    break;
                case 10:
                    Intent intent = new Intent(getApplicationContext(),
                            AboutUs.class);
                    startActivity(intent);
                    easyTracker.send(MapBuilder.createEvent(
                            getResources().getString(R.string.about_us_page),
                            getResources().getString(
                                    R.string.about_us_page_navigate),
                            "track event", null).build());
                    break;
            }
        }

    }



    /**
     * Dialog to display Rate us on Google Play
     */
    public void rateDialogue() {
        easyTracker.send(MapBuilder.createEvent(getString(R.string.home_screen),
                getString(R.string.home_screen_rate_us_dialogue_diaplayed),
                "track event", null).build());
        AppRate.with(BaseActivity.this).setInstallDays(0) // default 10, 0 means
                // install day.
                .setLaunchTimes(1) // default 10 times.
                .setRemindInterval(2) // default 1 day.
                .setShowNeutralButton(true) // default true.
                .setDebug(false) // default false.
                .setShowTitle(false) // default true
                .setOnClickButtonListener(new AppRateOnClickButtonListener() { // callback
                    // listener.
                    @Override
                    public void onClickButton(int which) {
                    }
                }).monitor();
        // Show a dialog if meets conditions.
        AppRate.showRateDialogIfMeetsConditions(BaseActivity.this);
    }

    /**
     * Dialog to display saving so far data
     */


    public void savingDialog() {
        easyTracker.send(MapBuilder.createEvent(getString(R.string.home_screen),
                getString(R.string.home_screen_savingsofar_dialogue_diaplayed),
                "track event", null).build());
        int totalCache = 0;
        int totalRAM = 0;
        String date = "";
        List<CacheList> cacheList = SavingDBAdapter.getInstance().getAllCache();

        for (CacheList cL : cacheList) {
            totalCache +=  cL.getCacheClean();
            totalRAM += cL.getRamClean();
            date = cL.getUpdatedDate();
        }

        final Dialog dialog = new Dialog(BaseActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.savingsofar);
        DecimalFormat formatter = new DecimalFormat("#0.00");
        dialog.setCanceledOnTouchOutside(false);
        Button ok = (Button) dialog.findViewById(R.id.idd);
        TextView cleared = (TextView) dialog.findViewById(R.id.clearedtxt);
        TextView updated = (TextView) dialog.findViewById(R.id.updatedtxt);
        TextView ramClean = (TextView) dialog.findViewById(R.id.ramtxt);
        if (SavingDBAdapter.getInstance().isOpen())
            if (SavingDBAdapter.getInstance().getCache() != null
                    && SavingDBAdapter.getInstance().getCache().getCount() > 0) {
                float m3 = totalCache / 1024;
                float m33 = m3 / 1000;

                if (m3 <= 0) {
                    cleared.setText("0.00 MB");
                } else if (m3 < 1000) {
                    cleared.setText("" + formatter.format(m3) + " KB");
                } else if (m3 > 1000){
                    cleared.setText("" + formatter.format(m33) + " MB");
                }

                if (totalRAM <= 0) {
                    ramClean.setText("0.00 MB");
                } else 	if (totalRAM < 1000) {
                    ramClean.setText("" + formatter.format(totalRAM) + " MB");
                } else 	if (totalRAM > 1000) {
                    float mRAM = totalRAM/1024;
                    ramClean.setText("" + formatter.format(totalRAM) + " MB");
                }

                SimpleDateFormat inputFormatter1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a", Locale.getDefault());
                Date date1 = new Date();
                try {
                    date1 = inputFormatter1.parse(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                SimpleDateFormat outputFormatter1 = new SimpleDateFormat("MMMM dd, yyyy hh:mm:ss a", Locale.getDefault());
                String output1 = outputFormatter1.format(date1);
                updated.setText(""+ output1);
            }
        SavingDBAdapter.getInstance().getRate().close();
        dialog.show();
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }


    /**
     * method used to pop up Rate us on Google Play dialogue for every 5
     * attempts
     */
    protected void showRateCount() {
        if (SavingDBAdapter.getInstance().isOpen()) {
            int count = SavingDBAdapter.getInstance().getRateCount().getColumnIndex(RATE_TABLE.COUNT);
            if (SavingDBAdapter.getInstance().getRateCount().getInt(count) % 5 == 0) {
                rateDialogue();
            }
            SavingDBAdapter.getInstance().getRate().close();
        }
    }
    /**
     * notifications on/off selection
     */
    protected void setNotifications() {
        if (null != telephonyManager.getDeviceId())
            if (sp.getInt(getString(R.string.notification_on), AppData.getInstance().getNotificationSettings()) == 0) {
                //getAllDeviceData();
                new Notifications(getApplicationContext(), 1);
            } else {
                new Notifications(getApplicationContext(), 0);
            }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_CANCELED) {
            AppData.getInstance().setAskDeleteCancel(true);
        } else if (resultCode == Activity.RESULT_OK) {
            positions.add(requestCode);
            deletePackages();
        }
        if (requestCode == Constants.RECOMENDED_RESULT_CODE) {
            if (resultCode == Activity.RESULT_OK) {

                Uri contactData = data.getData();
                Cursor c = managedQuery(contactData, null, null, null, null);
                if (c.moveToFirst()) {

                    String id = c
                            .getString(c
                                    .getColumnIndexOrThrow(ContactsContract.Contacts._ID));

                    String hasPhone = c
                            .getString(c
                                    .getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                    if (hasPhone.equalsIgnoreCase("1")) {
                        Cursor phones = getContentResolver()
                                .query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                        null,
                                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                                                + " = " + id, null, null);
                        phones.moveToFirst();
                        String cNumber = phones.getString(phones
                                .getColumnIndex("data1"));

                        Intent sharingIntent = new Intent(
                                android.content.Intent.ACTION_SEND);
                        sharingIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                        sharingIntent.setType("text/plain");
                        sharingIntent.putExtra("address", cNumber);
                        sharingIntent.putExtra(
                                android.content.Intent.EXTRA_TEXT,
                                Constants.share);
                        startActivity(sharingIntent);
                    }
                }
            }
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
        }
    }

    /***
     * Clean App Settings method
     ***/
    public void cleanAppSettingsDialog(Context ctx, boolean windowState) {
        final Dialog cleanAppDlg = new Dialog(ctx);
        cleanAppDlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        cleanAppDlg.setContentView(R.layout.clean_app_settings_dlg);
        final TextView button_ok = (TextView) cleanAppDlg
                .findViewById(R.id.btn_ok);
        cleanAppDlg.setCanceledOnTouchOutside(false);
        cleanAppSettings = sharedPref.getInt(getString(R.string.saved_auto_clean), 2);

        if (windowState == true) {
            WindowManager.LayoutParams wmlp = cleanAppDlg.getWindow()
                    .getAttributes();

            wmlp.gravity = Gravity.TOP | Gravity.AXIS_SPECIFIED;

            int density = getResources().getDisplayMetrics().densityDpi;
            switch (density) {
                case DisplayMetrics.DENSITY_HIGH:
                    wmlp.x = 560;
                    wmlp.y = 60;

                    break;
                case DisplayMetrics.DENSITY_XHIGH:
                    wmlp.x = 560;
                    wmlp.y = 80;
                    break;

                case DisplayMetrics.DENSITY_XXHIGH:
                    wmlp.x = 560;
                    wmlp.y = 120;
                    break;
                case DisplayMetrics.DENSITY_XXXHIGH:
                    wmlp.x = 560;
                    wmlp.y = 200;
                    break;
                case DisplayMetrics.DENSITY_MEDIUM:
                    wmlp.x = 560;
                    wmlp.y = 50;
                    break;
                case DisplayMetrics.DENSITY_LOW:
                    wmlp.x = 560;
                    wmlp.y = 30;
                    break;
            }
        }

        final RadioGroup appSettingGroup = (RadioGroup) cleanAppDlg
                .findViewById(R.id.app_setting_grp);
        final RadioGroup askBeforeGroup = (RadioGroup) cleanAppDlg
                .findViewById(R.id.ask_bfore_grp);
        final Switch cleanAppSwitch = (Switch) cleanAppDlg
                .findViewById(R.id.clenmemory_switch);

        cleanAppSwitch.setChecked(sharedPref.getBoolean(
                getResources().getString(R.string.cleanappswitch),
                true));
        cleanAppDlg.setCanceledOnTouchOutside(false);
        button_ok.setEnabled(true);
        button_ok.setTextColor(Color.WHITE);
        if (cleanAppSwitch.isChecked()) {

            for (int i = 0; i < appSettingGroup.getChildCount(); i++) {
                appSettingGroup.getChildAt(i).setEnabled(true);
            }
            for (int i = 0; i < askBeforeGroup.getChildCount(); i++) {
                askBeforeGroup.getChildAt(i).setEnabled(true);
            }

        } else {
            for (int i = 0; i < appSettingGroup.getChildCount(); i++) {
                appSettingGroup.getChildAt(i).setEnabled(false);
            }
            for (int i = 0; i < askBeforeGroup.getChildCount(); i++) {
                askBeforeGroup.getChildAt(i).setEnabled(false);
            }
        }
        if (cleanAppSettings == 0) {
            appSettingGroup.check(R.id.week);
        } else if (cleanAppSettings == 1) {
            appSettingGroup.check(R.id.days);
        } else if (cleanAppSettings == 2) {
            appSettingGroup.check(R.id.month);
        }

        if (isAskbeforeSelection) {
            askBeforeGroup.check(R.id.ask_before_on);
        } else {
            askBeforeGroup.check(R.id.ask_before_off);
        }


        appSettingGroup
                .setOnCheckedChangeListener(new OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        switch (checkedId) {
                            case R.id.week:
                                cleanAppSettings = 0;
                                dialogSettings = 7;
                                easyTracker.send(MapBuilder.createEvent(getString(R.string.app_manager),
                                        getString(R.string.app_manager_auto_setting_repeat_week),
                                        "track event", null).build());
                                break;
                            case R.id.days:
                                dialogSettings = 15;
                                cleanAppSettings = 1;
                                easyTracker.send(MapBuilder.createEvent(getString(R.string.app_manager),
                                        getString(R.string.app_manager_auto_setting_repeat_biweek),
                                        "track event", null).build());
                                break;
                            case R.id.month:
                                dialogSettings = 30;
                                cleanAppSettings = 2;
                                easyTracker.send(MapBuilder.createEvent(getString(R.string.app_manager),
                                        getString(R.string.app_manager_auto_setting_repeat_month),
                                        "track event", null).build());
                                break;

                            default:
                                break;
                        }
                    }
                });

        askBeforeGroup
                .setOnCheckedChangeListener(new OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        switch (checkedId) {
                            case R.id.ask_before_on:
                                isAskbeforeSelection = true;
                                easyTracker.send(MapBuilder.createEvent(getString(R.string.app_manager),
                                        getString(R.string.app_manager_auto_setting_ask_before_on),
                                        "track event", null).build());
                                break;
                            case R.id.ask_before_off:
                                isAskbeforeSelection = false;
                                easyTracker.send(MapBuilder.createEvent(getString(R.string.app_manager),
                                        getString(R.string.app_manager_auto_setting_ask_before_off),"track event", null).build());
                                break;
                            default:
                                break;
                        }

                    }
                });

        cleanAppDlg.show();
        button_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                easyTracker.send(MapBuilder.createEvent(getString(R.string.app_manager),
                        getString(R.string.app_manager_auto_setting_dialogue_done_button_clicked),
                        "track event", null).build());

                if (cleanAppSwitch.isChecked()) {
                    editor.putInt(getString(R.string.saved_auto_clean),
                            cleanAppSettings);
                    editor.putBoolean(getString(R.string.ask_bfore_delete),
                            isAskbeforeSelection);
                    if (dialogSettings <= 0)
                        dialogSettings = 7;
                    editor.putInt(getString(R.string.dialog_setting),
                            dialogSettings);
                    ed_cleanApp.putBoolean(getResources().getString(R.string.cleanapp_state), true);
                    editor.putBoolean(getResources().getString(R.string.cleanappswitch),
                            cleanAppSwitch.isChecked());
                    editor.commit();
                    ed_cleanApp.commit();
                    CleanAppReciever.setList(installedAppsList);
                    CleanAppReciever.setAlarms(BaseActivity.this);
                } else {

                }
                cleanAppDlg.dismiss();
            }
        });
        cleanAppSwitch
                .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {
                        // true if the switch is in the On position
                        cleanAppDlg.setCanceledOnTouchOutside(false);
                        button_ok.setEnabled(true);
                        button_ok.setTextColor(Color.WHITE);
                        if (cleanAppSwitch.isChecked()) {

                            for (int i = 0; i < appSettingGroup.getChildCount(); i++) {
                                appSettingGroup.getChildAt(i).setEnabled(true);
                            }
                            for (int i = 0; i < askBeforeGroup.getChildCount(); i++) {
                                askBeforeGroup.getChildAt(i).setEnabled(true);
                            }

                        } else {
                            for (int i = 0; i < appSettingGroup.getChildCount(); i++) {
                                appSettingGroup.getChildAt(i).setEnabled(false);
                            }
                            for (int i = 0; i < askBeforeGroup.getChildCount(); i++) {
                                askBeforeGroup.getChildAt(i).setEnabled(false);
                            }
                        }
                        easyTracker.send(MapBuilder.createEvent(getString(R.string.app_manager),
                                getString(R.string.app_manager_auto_setting_dialogue_switch) + isChecked,
                                "track event", null).build());
                        editor.putBoolean(getString(R.string.ask_bfore_delete),
                                isAskbeforeSelection);
                        editor.putBoolean(getResources().getString(R.string.cleanappswitch),
                                cleanAppSwitch.isChecked());
                        editor.commit();
                    }
                });
    }

    /***
     * Speed Booster Settings method
     ***/
    public void cleanMemorySettingsDialog(Context ctx, boolean windowState) {
        final Dialog cleanMemoryDlg = new Dialog(ctx);
        cleanMemoryDlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        cleanMemoryDlg.setContentView(R.layout.cleanmemory_dialog);
        cleanMemoryDlg.setCanceledOnTouchOutside(false);
        repeatSetting2 = sharedPref.getInt(
                getString(R.string.dialog_txt_REPEAT), 0);
        easyTracker.send(MapBuilder.createEvent(getResources().getString(R.string.speed_booster),
                getResources().getString(R.string.analytics_speedbooster_memory_entered), "track event", null).build());
        if (windowState == true) {
            WindowManager.LayoutParams wmlp = cleanMemoryDlg.getWindow()
                    .getAttributes();
            wmlp.gravity = Gravity.TOP | Gravity.CENTER;
            int density = getResources().getDisplayMetrics().densityDpi;
            switch (density) {
                case DisplayMetrics.DENSITY_HIGH:
                    wmlp.x = 560;
                    wmlp.y = 60;

                    break;
                case DisplayMetrics.DENSITY_XHIGH:
                    wmlp.x = 560;
                    wmlp.y = 80;
                    break;

                case DisplayMetrics.DENSITY_XXHIGH:
                    wmlp.x = 560;
                    wmlp.y = 120;
                    break;
                case DisplayMetrics.DENSITY_XXXHIGH:
                    wmlp.x = 560;
                    wmlp.y = 200;
                    break;
                case DisplayMetrics.DENSITY_MEDIUM:
                    wmlp.x = 560;
                    wmlp.y = 50;
                    break;
                case DisplayMetrics.DENSITY_LOW:
                    wmlp.x = 560;
                    wmlp.y = 30;
                    break;
            }
        }
        final CheckBox sunday = (CheckBox) cleanMemoryDlg.findViewById(R.id.sunday);
        final CheckBox monday = (CheckBox) cleanMemoryDlg.findViewById(R.id.monday);
        final CheckBox tuesday = (CheckBox) cleanMemoryDlg.findViewById(R.id.tuesday);
        final CheckBox wednsday = (CheckBox) cleanMemoryDlg.findViewById(R.id.wednsday);
        final CheckBox thursday = (CheckBox) cleanMemoryDlg.findViewById(R.id.thursday);
        final CheckBox friday = (CheckBox) cleanMemoryDlg.findViewById(R.id.friday);
        final CheckBox saturday = (CheckBox) cleanMemoryDlg.findViewById(R.id.saturday);
        final RadioGroup repeatCleanMemoryGroup = (RadioGroup) cleanMemoryDlg
                .findViewById(R.id.repeat_cln_mem_grp);
        final TextView button_Done = (TextView) cleanMemoryDlg
                .findViewById(R.id.btn_done);
        final TimePicker timePicker = (TimePicker) cleanMemoryDlg
                .findViewById(R.id.timePicker1);
        final Switch cleanMemorySwitch = (Switch) cleanMemoryDlg
                .findViewById(R.id.clenmemory_switch);

        final int hour = sharedPref.getInt(getString(R.string.Hour), 22);
        final int minute = sharedPref.getInt(getString(R.string.Minute), 00);
        repeatSetting2 = sharedPref.getInt(
                getString(R.string.dialog_txt_REPEAT), 0);
        cleanMemorySwitch.setChecked(sharedPref.
                getBoolean(getResources().getString(R.string.cleanmemoryswitch), true));
        // set current time into time picker
        timePicker.setCurrentHour(hour);
        timePicker.setCurrentMinute(minute);

        cleanMemoryDlg.setCanceledOnTouchOutside(false);
        button_Done.setEnabled(true);
        button_Done.setTextColor(Color.WHITE);

        if (cleanMemorySwitch.isChecked()) {
            timePicker.setEnabled(true);
            sunday.setEnabled(true);
            monday.setEnabled(true);
            thursday.setEnabled(true);
            wednsday.setEnabled(true);
            thursday.setEnabled(true);
            friday.setEnabled(true);
            saturday.setEnabled(true);
            for (int i = 0; i < repeatCleanMemoryGroup.getChildCount(); i++) {
                repeatCleanMemoryGroup.getChildAt(i).setEnabled(true);
            }
        } else {
            timePicker.setEnabled(false);
            sunday.setEnabled(false);
            monday.setEnabled(false);
            tuesday.setEnabled(false);
            wednsday.setEnabled(false);
            thursday.setEnabled(false);
            friday.setEnabled(false);
            saturday.setEnabled(false);
            for (int i = 0; i < repeatCleanMemoryGroup.getChildCount(); i++) {
                repeatCleanMemoryGroup.getChildAt(i).setEnabled(false);
            }
        }

        sunday.setChecked(sharedPref.getBoolean("SUNDAY", false));
        monday.setChecked(sharedPref.getBoolean("MONDAY", false));
        tuesday.setChecked(sharedPref.getBoolean("TUESDAY", false));
        wednsday.setChecked(sharedPref.getBoolean("WEDNESDAY", false));
        thursday.setChecked(sharedPref.getBoolean("THURSDAY", false));
        friday.setChecked(sharedPref.getBoolean("FRIDAY", false));
        saturday.setChecked(sharedPref.getBoolean("SATURDAY", true));

        if (repeatSetting2 == 0) {
            repeatCleanMemoryGroup.check(R.id.repeat_cln_week);
        } else if (repeatSetting2 == 1) {
            repeatCleanMemoryGroup.check(R.id.repeat_cln_biweekly);
        } else if (repeatSetting2 == 2) {
            repeatCleanMemoryGroup.check(R.id.repeat_cln_month);
        }

        repeatCleanMemoryGroup
                .setOnCheckedChangeListener(new OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        switch (checkedId) {
                            case R.id.repeat_cln_week:
                                repeatSetting2 = 0;
                                easyTracker.send(MapBuilder.createEvent(getResources().getString(R.string.speed_booster),
                                        getResources().getString(R.string.analytics_speedbooster_memory_repeat_week), "track event", null).build());
                                break;
                            case R.id.repeat_cln_biweekly:
                                repeatSetting2 = 1;
                                easyTracker.send(MapBuilder.createEvent(getResources().getString(R.string.speed_booster),
                                        getResources().getString(R.string.analytics_speedbooster_memory_repeat_biweek), "track event", null).build());
                                break;
                            case R.id.repeat_cln_month:
                                repeatSetting2 = 2;
                                easyTracker.send(MapBuilder.createEvent(getResources().getString(R.string.speed_booster),
                                        getResources().getString(R.string.analytics_speedbooster_memory_repeat_month), "track event", null).build());
                                break;
                            default:
                                break;
                        }
                    }
                });
        cleanMemoryDlg.show();
        button_Done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                easyTracker.send(MapBuilder.createEvent(getResources().getString(R.string.automatically_boost_memory),
                        getResources().getString(R.string.automatically_boost_memory_dialog_button), "track event", null).build());

                if(cleanMemorySwitch.isChecked()) {
                    int hour = timePicker.getCurrentHour();
                    int minute = timePicker.getCurrentMinute();
                    editor.putInt(getString(R.string.dialog_txt_REPEAT),repeatSetting2);
                    editor.putInt(getString(R.string.Hour), hour);
                    editor.putInt(getString(R.string.Minute), minute);
                    editor.putInt(getString(R.string.ON), minute);
                    editor.putBoolean(getResources().getString(R.string.cleanmemoryswitch), cleanMemorySwitch.isChecked());
                    ed_cleanMem.putBoolean((getResources().getString(R.string.cleanmem_state)), true);
                    ed_cleanMem.commit();
                    editor.putBoolean("SUNDAY", sunday.isChecked());
                    editor.putBoolean("MONDAY", monday.isChecked());
                    editor.putBoolean("TUESDAY", tuesday.isChecked());
                    editor.putBoolean("WEDNESDAY", wednsday.isChecked());
                    editor.putBoolean("THURSDAY", thursday.isChecked());
                    editor.putBoolean("FRIDAY", friday.isChecked());
                    editor.putBoolean("SATURDAY", saturday.isChecked());
                    editor.commit();
                    Bean.setAlarm(BaseActivity.this);
                }else {

                }
                cleanMemoryDlg.dismiss();
                easyTracker
                        .send(MapBuilder
                                .createEvent(
                                        getResources()
                                                .getString(
                                                        R.string.auto_speed_booster_dialog_settings),
                                        getResources()
                                                .getString(
                                                        R.string.auto_speed_booster_dialog_settings_done),
                                        "track event", null).build());
            }
        });
        cleanMemorySwitch
                .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {
                        easyTracker
                                .send(MapBuilder
                                        .createEvent(
                                                getResources()
                                                        .getString(
                                                                R.string.auto_speed_booster_dialog_settings),
                                                getResources()
                                                        .getString(
                                                                R.string.auto_speed_booster_dialog_settings_switch)+" "+isChecked,
                                                "track event", null).build());
                        // true if the switch is in the On position
                        cleanMemoryDlg.setCanceledOnTouchOutside(false);
                        button_Done.setEnabled(true);
                        button_Done.setTextColor(Color.WHITE);
                        if (isChecked) {
                            timePicker.setEnabled(true);
                            sunday.setEnabled(true);
                            monday.setEnabled(true);
                            tuesday.setEnabled(true);
                            wednsday.setEnabled(true);
                            thursday.setEnabled(true);
                            friday.setEnabled(true);
                            saturday.setEnabled(true);
                            for (int i = 0; i < repeatCleanMemoryGroup
                                    .getChildCount(); i++) {
                                repeatCleanMemoryGroup.getChildAt(i)
                                        .setEnabled(true);
                            }
                        } else {
                            timePicker.setEnabled(false);
                            sunday.setEnabled(false);
                            monday.setEnabled(false);
                            tuesday.setEnabled(false);
                            wednsday.setEnabled(false);
                            thursday.setEnabled(false);
                            friday.setEnabled(false);
                            saturday.setEnabled(false);
                            for (int i = 0; i < repeatCleanMemoryGroup
                                    .getChildCount(); i++) {
                                repeatCleanMemoryGroup.getChildAt(i)
                                        .setEnabled(false);
                            }
                        }
                        editor.putBoolean(getResources().getString(R.string.cleanmemoryswitch),
                                cleanMemorySwitch.isChecked());
                        editor.commit();
                    }
                });
    }

    public void cleanMemoryByMemoryDialog(Context ctx, boolean windowState) {
        final Dialog cleanMemoryDlg = new Dialog(ctx);
        cleanMemoryDlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        cleanMemoryDlg.setContentView(R.layout.clean_mem_boost_based_memory);
        cleanMemoryDlg.setCanceledOnTouchOutside(false);
        sp = getSharedPreferences(getResources().getString(R.string.cleanmemory_pref), Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor2 = sp.edit();
        final Switch switchCleanMemory = (Switch) cleanMemoryDlg.findViewById(R.id.clenmemory_switch);
        final Button button_Done = (Button) cleanMemoryDlg.findViewById(R.id.btn_done);
        final EditText onTextByMemoryStartingText = (EditText) cleanMemoryDlg
                .findViewById(R.id.boost_memory_for_edittext);
        if (0 != AppData.getInstance().getDynamicJunkCacheMemorySize()
                && AppData.getInstance().getDynamicJunkCacheMemorySize() > 0)

            dynamicJunkCacheMemorySize = "" + AppData.getInstance().getDynamicJunkCacheMemorySize();
        dynamicMaxJunkCacheMemorySize = "" + AppData.getInstance().getDynamicMaxJunkCacheMemorySize();
        dynamicMinJunkCacheMemorySize = ""+AppData.getInstance().getDynamicMinJunkCacheMemorySize();

        if(0 == sp.getInt("boost", 0)) {
            onTextByMemoryStartingText.setText(dynamicJunkCacheMemorySize);
        }else{
            onTextByMemoryStartingText.setText(""+sp.getInt("boost", 0));
        }

        switchCleanMemory.setChecked(AppData.getInstance().isBoostMemory());
        button_Done.setEnabled(true);
        cleanMemoryDlg.setCanceledOnTouchOutside(false);
        button_Done.setTextColor(Color.WHITE);
        if (switchCleanMemory.isChecked()) {
            onTextByMemoryStartingText.setTextColor(getResources().getColor(R.color.app_lock_green));
            onTextByMemoryStartingText.setEnabled(true);
        } else {
            onTextByMemoryStartingText.setEnabled(false);
            onTextByMemoryStartingText.setTextColor(getResources().getColor(R.color.btn_disable));

        }
        switchCleanMemory.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                AppData.getInstance().setBoostMemory(isChecked);
                button_Done.setEnabled(true);
                cleanMemoryDlg.setCanceledOnTouchOutside(false);
                button_Done.setTextColor(Color.WHITE);
                if (switchCleanMemory.isChecked()) {
                    onTextByMemoryStartingText.setTextColor(getResources().getColor(R.color.app_lock_green));
                    onTextByMemoryStartingText.setEnabled(true);
                } else {
                    onTextByMemoryStartingText.setTextColor(getResources().getColor(R.color.btn_disable));
                    cleanMemoryDlg.setCanceledOnTouchOutside(true);
                }

                easyTracker.send(MapBuilder.createEvent(getResources().getString(R.string.speed_booster),
                        getResources().getString(R.string.analytics_speedbooster_by_memory_switch) + isChecked, "track event", null).build());
            }
        });

        if (windowState == true) {
            WindowManager.LayoutParams wmlp = cleanMemoryDlg.getWindow().getAttributes();
            wmlp.gravity = Gravity.TOP | Gravity.CENTER;
            int density = getResources().getDisplayMetrics().densityDpi;
            switch (density) {
                case DisplayMetrics.DENSITY_HIGH:
                    wmlp.x = 560;
                    wmlp.y = 60;

                    break;
                case DisplayMetrics.DENSITY_XHIGH:
                    wmlp.x = 560;
                    wmlp.y = 80;
                    break;

                case DisplayMetrics.DENSITY_XXHIGH:
                    wmlp.x = 560;
                    wmlp.y = 120;
                    break;
                case DisplayMetrics.DENSITY_XXXHIGH:
                    wmlp.x = 560;
                    wmlp.y = 200;
                    break;
                case DisplayMetrics.DENSITY_MEDIUM:
                    wmlp.x = 560;
                    wmlp.y = 50;
                    break;
                case DisplayMetrics.DENSITY_LOW:
                    wmlp.x = 560;
                    wmlp.y = 30;
                    break;
            }
        }

        cleanMemoryDlg.show();
        button_Done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (switchCleanMemory.isChecked()) {
                    try {
                        if (onTextByMemoryStartingText.getText().toString().isEmpty()
                                || null != onTextByMemoryStartingText.getText().toString()) {
                            String val = onTextByMemoryStartingText.getText().toString();
                            if (val.isEmpty()) {
                                onTextByMemoryStartingText.setError("Cache sholud not be empty");
                            } else {
                                if (Integer.parseInt(val) < Integer.parseInt(tempValMin)) {
                                    onTextByMemoryStartingText.setError("Cache limit should not be < "+Integer.parseInt(tempValMin));
                                    AppData.getInstance().setDynamicMaxJunkCacheMemorySize(Integer.parseInt(tempValMin));
                                } else if (Integer.parseInt(val) > Integer.parseInt(tempVal)) {
                                    onTextByMemoryStartingText.setError("Cache limit should be <= " + Integer.parseInt(tempVal));
                                    AppData.getInstance().setDynamicMaxJunkCacheMemorySize(Integer.parseInt(val));
                                } else {
                                    cleanMemoryDlg.dismiss();
                                }
                                editor2.putInt("boost", Integer.parseInt(val));
                                editor2.commit();
                            }
                        }
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                } else {
                    cleanMemoryDlg.dismiss();
                }

                easyTracker.send(MapBuilder
                        .createEvent(getResources().getString(R.string.speed_booster_memory_based_dialog_title),
                                getResources().getString(R.string.speed_booster_memory_based_dialog_close),
                                "track event", null)
                        .build());
            }
        });

    }

    private void getBoostOnMemory(){
        if (AppData.getInstance().getInternalStorage() <= 8) {
            dynamicMaxJunkCacheMemorySize = "1000";
            dynamicMinJunkCacheMemorySize = "50";
        } else if (AppData.getInstance().getInternalStorage() <= 16) {
            dynamicMaxJunkCacheMemorySize = "2000";
            dynamicMinJunkCacheMemorySize= "100";
        } else if (AppData.getInstance().getInternalStorage() <= 32) {
            dynamicMaxJunkCacheMemorySize = "3000";
            dynamicMinJunkCacheMemorySize = "200";
        } else if (AppData.getInstance().getInternalStorage() >= 32) {
            dynamicMaxJunkCacheMemorySize = "4000";
            dynamicMinJunkCacheMemorySize = "500";
        }
        tempVal = dynamicMaxJunkCacheMemorySize;
        tempValMin = dynamicMinJunkCacheMemorySize;
    }
    public int getVersion() {
        int v = 0;
        try {
            v = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
        } catch (NameNotFoundException e) {

        }
        return v;
    }

    private void selectItem(int position) {
        adapter.setmSelectedItem(position);
    }

    public void resetCleanApp() {
    }
    /**
     * notifications dialog view
     */

    private void notificationSettingDialog(Context ctx) {
        easyTracker.send(MapBuilder.createEvent(getString(R.string.home_screen),
                getString(R.string.home_screen_notification_dialogue_diaplayed),
                "track event", null).build());
        sp = getSharedPreferences(getResources().getString(R.string.cleanmemory_pref), Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor2 = sp.edit();

        final Dialog dialog = new Dialog(ctx);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.notification_dialogue);
        dialog.setCanceledOnTouchOutside(false);
        final RadioGroup appSettingGroup = (RadioGroup)dialog.findViewById(R.id.notification_group);
        if(sp.getInt(getString(R.string.notification_on), AppData.getInstance().getNotificationSettings()) == 0){
            appSettingGroup.check(R.id.notification_on);
        } else if(sp.getInt(getString(R.string.notification_on), AppData.getInstance().getNotificationSettings()) == 1){
            appSettingGroup.check(R.id.notification_off);
        }

        appSettingGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.notification_on:
                        AppData.getInstance().setNotificationSettings(0);
                        dialog.dismiss();
                        break;
                    case R.id.notification_off:
                        AppData.getInstance().setNotificationSettings(1);
                        dialog.dismiss();
                        break;
                    default:
                        break;
                }
                editor2.putInt(getString(R.string.notification_on), AppData.getInstance().getNotificationSettings());
                editor2.commit();
                setNotifications();
            }
        });

        dialog.show();
    }

    protected void unbindDrawables(View findViewById) {
        try {
            if (findViewById != null) {
                if (findViewById.getBackground() != null) {
                    findViewById.getBackground().setCallback(null);
                }
                if (findViewById instanceof ViewGroup) {
                    for (int i = 0; i < ((ViewGroup) findViewById)
                            .getChildCount(); i++) {
                        unbindDrawables(((ViewGroup) findViewById)
                                .getChildAt(i));
                        ((ViewGroup) findViewById).removeViewAt(i);
                    }
                    if (!(((ViewGroup) findViewById).getClass().getSuperclass()
                            .getName()
                            .equalsIgnoreCase("android.widget.Adapter")))
                        ((ViewGroup) findViewById).removeAllViews();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void closeAllActivities() {
        try {
            sendBroadcast(new Intent(FINISH_ALL_ACTIVITIES_ACTIVITY_ACTION));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static IntentFilter createIntentFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(FINISH_ALL_ACTIVITIES_ACTIVITY_ACTION);
        return filter;
    }

    protected void registerBaseActivityReceiver() {
        registerReceiver(baseActivityReceiver, INTENT_FILTER);
    }

    protected void unRegisterBaseActivityReceiver() {
        unregisterReceiver(baseActivityReceiver);
    }

    public class BaseActivityReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction()
                    .equals(FINISH_ALL_ACTIVITIES_ACTIVITY_ACTION)) {
                finish();
            }
        }
    }
}
