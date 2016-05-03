package com.unfoldlabs.redgreen.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.unfoldlabs.redgreen.R;
import com.unfoldlabs.redgreen.global.AppData;
import com.unfoldlabs.redgreen.global.ApplicationData;
import com.unfoldlabs.redgreen.utilty.Utility;


public class QuitAppSplashActivity extends Activity {
    String lockFrom = null;
    public static final String FINISH_ALL_ACTIVITIES_ACTIVITY_ACTION = "com.unfoldlabs.redgreen.FINISH_ALL_ACTIVITIES_ACTIVITY_ACTION";
    private int TIME_INTERVEL = 1000;
    private ImageView imageView;
    private final Handler mHandler = new Handler();
    private Animation animRotate;
    /**
     * To show the spalsh screen layout in device
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quit_splash_screen_activity);
        imageView = (ImageView) findViewById(R.id.animatedImage);
        animRotate = AnimationUtils.loadAnimation(this, R.anim.anim_rotate);
        imageView.startAnimation(animRotate);
        mHandler.postDelayed(sRunnable, TIME_INTERVEL);
        ApplicationData.getInstance().setDoneButtonPressedTime(""+ Utility.getDefaultDate());
    }

    private Runnable sRunnable = new Runnable() {
        public void run() {

            if (AppData.getInstance().browserHistory != null) {
                AppData.getInstance().browserHistory.clear();
                AppData.getInstance().browserHistory = null;
            }
            if (AppData.getInstance().list != null) {
                AppData.getInstance().list.clear();
                AppData.getInstance().list = null;

            }
            if (AppData.getInstance().bagroundlist != null) {
                AppData.getInstance().bagroundlist.clear();
                AppData.getInstance().bagroundlist = null;
            }
            if (AppData.getInstance().junk != null) {
                AppData.getInstance().junk.clear();
                AppData.getInstance().junk = null;
            }
            if (AppData.getInstance().getDoNotDeleteAppsList() != null) {
                AppData.getInstance().getDoNotDeleteAppsList().clear();
                AppData.getInstance().setDoNotDeleteAppsList(null);
            }
            if (AppData.getInstance().getAppManagerInstalledAppsList() != null) {
                AppData.getInstance().getAppManagerInstalledAppsList().clear();
                AppData.getInstance().setAppManagerInstalledAppsList(null);
            }
            if (AppData.getInstance().saveBatteryCPUlist != null) {
                AppData.getInstance().saveBatteryCPUlist.clear();
                AppData.getInstance().saveBatteryCPUlist = null;
            }
            if (AppData.getInstance().saveBatteryBagroundlist != null) {
                AppData.getInstance().saveBatteryBagroundlist.clear();
                AppData.getInstance().saveBatteryBagroundlist = null;
            }
            finish();
            closeAllActivities();
        }
    };

    protected void closeAllActivities() {
        try {
            sendBroadcast(new Intent(FINISH_ALL_ACTIVITIES_ACTIVITY_ACTION));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}