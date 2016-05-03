package com.unfoldlabs.redgreen.service;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;

import com.unfoldlabs.redgreen.R;
import com.unfoldlabs.redgreen.applock.lock.ShowApplockViewOtherApps;
import com.unfoldlabs.redgreen.applock.util.Util;
import com.unfoldlabs.redgreen.db.DatabaseHandler;
import com.unfoldlabs.redgreen.global.AppData;
import com.unfoldlabs.redgreen.global.JunkCacheTask;
import com.unfoldlabs.redgreen.log.Applog;
import com.unfoldlabs.redgreen.model.CleanAppDB;
//import com.unfoldlabs.redgreen.receiver.AlarmManagerHelper;
import com.unfoldlabs.redgreen.receiver.CleanAppReciever;
import com.unfoldlabs.redgreen.utilty.Bean;
import com.unfoldlabs.redgreen.utilty.DeviceInfo;
import com.unfoldlabs.redgreen.utilty.Utility;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

@SuppressLint("InlinedApi")
public class RedGreenService extends Service {
    private String topPackage;
    public static final String MyPREFERENCES = "REDPrefs";
    public SharedPreferences sharedpreferences;
    private Context mContext;
    private DatabaseHandler dbHandler;
    private ScreenReceiver mScreenReceiver;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private int SkipId = 0;
    private int SkipIdCleaner = 0;
    private int cleanDbUpdate = 0;
    private DatabaseHandler database;
    private ArrayList<CleanAppDB> list;
    private Handler handler = new Handler();
    private Handler handler2 = new Handler();

    IBinder mBinder = new LocalBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class LocalBinder extends Binder {
        public RedGreenService getServerInstance() {
            return RedGreenService.this;
        }
    }
    public class ScreenReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
                // Trigger package again
                Applog.logString("ScreenReceiver ACTION_SCREEN_ON");
                handler.post(openLockScreen);

                //Service is not running start service
                if (!Util.isRedGreenServiceRunning(RedGreenService.class, context)) {
                    Intent service = new Intent(context, RedGreenService.class);
                    context.startService(service);
                }
            }
            if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                Applog.logString("ScreenReceiver ACTION_SCREEN_OFF");
                handler.removeCallbacks(openLockScreen);
            }
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        mScreenReceiver = new ScreenReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(mScreenReceiver, filter);
        startService(new Intent(mContext, RedGreenService.class));
        handler.post(openLockScreen);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Flag comes from Applock alarm manager receiver
        if (intent != null) {
            String appLock = intent.getStringExtra("applock");
            if (appLock != null) {
                if (appLock.equals("alarm")) {
                    checkApplockStatus();
                }
            }
        }
        if (intent != null) {
            String send = intent.getStringExtra("send");
            if (send != null) {
                if (send.equals("send")) {
                    // speedBoosterAlarm();
                }
            }
        }
        if (intent != null) {
            String sendclean = intent.getStringExtra("sendclean");
            if (sendclean != null) {
                if (sendclean.equals("sendclean")) {
                    cleanAppAlarm();
                }
            }
        }
        if (intent != null) {
            String sendclean = intent.getStringExtra("Bootsend");
            if (sendclean != null) {
                if (sendclean.equals("Bootsend")) {
                    //AlarmManagerHelper.setAlarms(getApplicationContext());
                }
            }
        }
        if (intent != null) {
            String sendclean = intent.getStringExtra("Bootsendclean");
            if (sendclean != null) {
                if (sendclean.equals("Bootsendclean")) {
                    CleanAppReciever.setAlarms(getApplicationContext());
                }
            }
        }
        if (intent != null) {
            String sendAWS = intent.getStringExtra("sendAWS");
            if (sendAWS != null) {
                if (sendAWS.equals("sendAWS")) {
                    DeviceInfo deviceInfo = new DeviceInfo();
                    deviceInfo.getAllDeviceData(getApplicationContext() );
                }
            }
        }
        return START_STICKY;
    }


    private Runnable openLockScreen = new Runnable() {
        @Override
        public void run() {

            handler.postDelayed(openLockScreen, 800);
            checkApplockStatus();
            try {
                setNotifications();
            }catch (NullPointerException e){

            }
            if (AppData.getInstance().isBoostMemory())
                if (!AppData.getInstance().isTaskRunning()) {
                    new JunkCacheTask(getApplicationContext()).execute();
                }
        }
    };

    private void setNotifications() {
        Calendar cal = Calendar.getInstance();
        int Day = cal.get(Calendar.DAY_OF_WEEK );
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minutes = cal.get(Calendar.MINUTE);
        sharedPreferences = getSharedPreferences("MY_SHARED_PREF", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();


        if(Day ==2 && hour ==0 && minutes==0 ){

            if(sharedPreferences.getBoolean("notify", true)){
                editor.putBoolean("notify", false);
                editor.commit();
                editor.putBoolean("sundayFlag", true);
                editor.commit();
                Bean.setAlarm(getApplicationContext());
            }

        }else if(Day ==2 && hour >=0&&minutes>0 ){
            editor.putBoolean("notify", true);
            editor.commit();
        }else{
            editor.putBoolean("notify", true);
            editor.commit();
        }
    }

    private Runnable openLockScreen2 = new Runnable() {
        @Override
        public void run() {

            checkApplockStatusNew();
        }
    };

    private void checkApplockStatusNew() {
        String packageName = "";
        try {
            packageName = getProcess();
        } catch (Exception e) {
            e.printStackTrace();
        }
        sharedpreferences = mContext.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        Set<String> set = sharedpreferences.getStringSet("key", null);
        if (set == null) {
            set = new HashSet<String>();
            editor.putStringSet("key", set);
            editor.commit();
        } else {

        }
        topPackage = sharedpreferences.getString("TOP_PACKAGE", "");
        if (set != null && packageName != null) {
            if (set.contains(packageName)) {

                if (!topPackage.equals(packageName) && packageName.contains("com.google") && !packageName.contains("com.android.systemui")) {
                    if (AppData.getInstance().isApplockExit() ) {
                        AppData.getInstance().setApplockExit(false);
                    } else{
                        Intent intent = new Intent(this, ShowApplockViewOtherApps.class);
                        intent.putExtra("LOCK_FROM", "fromService");
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                    editor.remove("TOP_PACKAGE");
                    editor.commit();
                    editor.putString("TOP_PACKAGE", packageName);
                    editor.commit();
                }
            } else if (!packageName.equals("com.unfoldlabs.redgreen")) {
                editor.remove("TOP_PACKAGE");
                editor.commit();
                editor.putString("TOP_PACKAGE", "");
                editor.commit();
            }
        }
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(mScreenReceiver);
        super.onDestroy();
        handler.removeCallbacks(openLockScreen);
        handler2.removeCallbacks(openLockScreen2);

    }

    public String getProcess() throws Exception {
        if (Build.VERSION.SDK_INT >= 21) {
            return getProcessNew();
        } else {
            return getProcessOld();
        }
    }

    //API 21 and above
    private String getProcessNew() {
        String topPackageName = null;
        UsageStatsManager mUsageStatsManager = (UsageStatsManager) getSystemService("usagestats");
        long time = System.currentTimeMillis();
        // We get usage stats for the last 10 seconds
        List<UsageStats> stats = mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000 * 10, time);
        // Sort the stats by the last time used
        if (stats != null) {
            SortedMap<Long, UsageStats> mySortedMap = new TreeMap<Long, UsageStats>();
            for (UsageStats usageStats : stats) {
                mySortedMap.put(usageStats.getLastTimeUsed(), usageStats);
            }
            if (mySortedMap != null && !mySortedMap.isEmpty()) {
                topPackageName = mySortedMap.get(mySortedMap.lastKey()).getPackageName();
            }
        }
        return topPackageName;
    }

    //API below 21
    @SuppressWarnings("deprecation")
    private String getProcessOld() {
        String topPackageName = null;
        ActivityManager activity = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> runningTask = activity.getRunningTasks(1);
        if (runningTask != null) {
            RunningTaskInfo taskTop = runningTask.get(0);
            ComponentName componentTop = taskTop.topActivity;
            topPackageName = componentTop.getPackageName();
        }
        return topPackageName;
    }

    private void checkApplockStatus() {
        String packageName = "";

        try {
            packageName = getProcess();
        } catch (Exception e) {
            e.printStackTrace();
        }
        sharedpreferences = mContext.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        Set<String> set = sharedpreferences.getStringSet("key", null);
        if (set == null) {
            set = new HashSet<String>();
            editor.putStringSet("key", set);
            editor.commit();
        } else {
        }
        topPackage = sharedpreferences.getString("TOP_PACKAGE", "");
        if (set != null && packageName != null) {

            if (set.contains(packageName)) {

                if (!topPackage.equals(packageName) && !packageName.contains("com.google")
                        && !packageName.contains("com.android.systemui")) {
                    if (AppData.getInstance().isApplockExit()) {
                        AppData.getInstance().setApplockExit(false);
                    }else {
                        Intent intent = new Intent(this, ShowApplockViewOtherApps.class);
                        intent.putExtra("LOCK_FROM", "fromService");
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                    editor.remove("TOP_PACKAGE");
                    editor.commit();
                    editor.putString("TOP_PACKAGE", packageName);
                    editor.commit();

                } else if (!topPackage.equals(packageName) && packageName.contains("com.google")) {
                    handler2.postDelayed(openLockScreen2, 1500);
                }
            } else if (!packageName.equals("com.unfoldlabs.redgreen")) {
                editor.remove("TOP_PACKAGE");
                editor.commit();
                editor.putString("TOP_PACKAGE", "");
                editor.commit();
            }
        }
        // Clea App Database Update
        cleanAppDatabaseUpadte(packageName);
    }


    //it updates every alternative applock call means 4sec. app lock alarm will execute every 2 sec
    private void cleanAppDatabaseUpadte(String dPackageName) {
        if (dPackageName != null) {
            if (!dPackageName.equals("com.unfoldlabs.redgreen")) {
                if (cleanDbUpdate == 0) {
                    cleanDbUpdate = 1;
                    if (dbHandler == null)
                        dbHandler = new DatabaseHandler(getApplicationContext());
                    dbHandler.upDateColumn(dPackageName, Utility.getDefaultDate());
                } else {
                    cleanDbUpdate = 0;
                }
            }
        }
    }


    private void cleanAppAlarm() {
        sharedPreferences = this.getSharedPreferences("MY_SHARED_PREF", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        int repeatCleanApp = sharedPreferences.getInt(getApplicationContext().getString(R.string.saved_auto_clean), 2);
        boolean autoCleanSwitch = sharedPreferences.getBoolean(
                this.getResources().getString(R.string.cleanappswitch), true);
        if (autoCleanSwitch) {
            // Get ArrayList Bundle
            database = new DatabaseHandler(this);
            list = (ArrayList<CleanAppDB>) database.getAllApps(getPackageManager());
            if (null != list && list.size() > 0) {

                if (repeatCleanApp == 0) {
                    Utility.generateCleanAppNotification(this, list);
                    CleanAppReciever.setAlarms(getApplicationContext());
                } else if (repeatCleanApp == 1) {
                    boolean flag = sharedPreferences.getBoolean("flag",
                            false);
                    if (!flag) {
                        flag = !flag;
                        editor.putBoolean("flag", flag);
                        editor.commit();
                        CleanAppReciever
                                .setAlarms(getApplicationContext());
                    } else {
                        flag = !flag;
                        editor.putBoolean("flag", flag);
                        editor.commit();
                        Utility.generateCleanAppNotification(this, list);
                        CleanAppReciever
                                .setAlarms(getApplicationContext());
                    }
                } else if (repeatCleanApp == 2) {
                    if (SkipId <= 2) {
                        SkipId += 1;
                        editor.putInt("skipId", SkipId);
                        editor.commit();

                        CleanAppReciever
                                .setAlarms(getApplicationContext());
                    } else {
                        Utility.generateCleanAppNotification(this, list);
                        CleanAppReciever
                                .setAlarms(getApplicationContext());
                        SkipId = 0;
                        editor.putInt("skipId", SkipId);
                        editor.commit();
                    }
                }
            }
        }
    }
}