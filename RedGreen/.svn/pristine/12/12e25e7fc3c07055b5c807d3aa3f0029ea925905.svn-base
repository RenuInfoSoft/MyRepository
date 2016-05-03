package com.unfoldlabs.redgreen.background;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.unfoldlabs.redgreen.R;
import com.unfoldlabs.redgreen.activity.AppManagerActivity;
import com.unfoldlabs.redgreen.db.DatabaseHandler;
import com.unfoldlabs.redgreen.global.AppData;
import com.unfoldlabs.redgreen.global.ApplicationData;
import com.unfoldlabs.redgreen.log.Applog;
import com.unfoldlabs.redgreen.model.CleanAppDB;
import com.unfoldlabs.redgreen.utilty.Utility;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CleanAppBackground extends Activity {

    private Context context;
    private long daysDiff;
    public int dialogSettings = 7;
    private HashMap<Integer, String> hashMap = new HashMap<Integer, String>();
    private List<CleanAppDB> appList = new ArrayList<CleanAppDB>();
    private DatabaseHandler database;
    private ArrayList<Integer> positions = new ArrayList<Integer>();
    private List<CleanAppDB> list;
    private int setCount;
    public boolean isAskbeforeSelection;
    private SharedPreferences sharedPref;
    private final static boolean IS_ASK_BEFORE_DELETE = true;

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.background);
        context = this;
        database = new DatabaseHandler(this);
        appList.clear();
        appList = database.getAllApps(getPackageManager());
        Utility.getAllPackageList(getApplicationContext(), database);
        if (null != appList && appList.isEmpty()) {
            if (null != AppData.getInstance().getAppManagerInstalledAppsList())
                appList = AppData.getInstance().getAppManagerInstalledAppsList();
        }
        list = database.getDontDeleteApps(this.getPackageManager());
        sharedPref = getSharedPreferences("MY_SHARED_PREF", Context.MODE_PRIVATE);
        isAskbeforeSelection = sharedPref.getBoolean(getString(R.string.ask_bfore_delete), IS_ASK_BEFORE_DELETE);
        if (appList.size() == 0) {
            finish();
        } else {
            autoCleanForApps(appList);
        }
    }

    private void autoCleanForApps(List<CleanAppDB> appList) {
        ArrayList<CleanAppDB> tempList = new ArrayList<CleanAppDB>();
        for (int i = 0; i < appList.size(); i++) {
            String appLastUsedDate = appList.get(i).getDateTime();
            String curretDate = Utility.getDefaultDate();

            if (null != appLastUsedDate)
                if (!appLastUsedDate.isEmpty()) {
                    daysDiff = getdaysDiff(appLastUsedDate, curretDate);

                    if (dialogSettings > 0)
                        if (dialogSettings != 0 && dialogSettings == daysDiff
                                || daysDiff >= dialogSettings) {

                            boolean donotStatus = getDonotDeleteStatus(appList
                                    .get(i).getPackageName());

                            if (appList.get(i).getPackageName() != null
                                    && !donotStatus) {
                                tempList.add(appList.get(i));
                            } else {
                                if (setCount == appList.size()) {
									/*
									 * if Donot delete List and appList size are
									 * equal then will finish the activity
									 */
                                    finish();
                                }
                            }
                        }
                }
        }
        if (isAskbeforeSelection)
            askDeleteDialog(appList.size(), daysDiff, tempList);
        else
            Uninstall(tempList);
        ApplicationData.getInstance().setDaysNotUsed(""+daysDiff);
    }

    private void Uninstall(ArrayList<CleanAppDB> tempList) {
        for (int i = 0; i < tempList.size(); i++) {
            Intent uninstallIntent = new Intent(Intent.ACTION_DELETE);
            uninstallIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            uninstallIntent.setData(Uri.parse("package:" + tempList.get(i).getPackageName()));
            uninstallIntent.putExtra(Intent.EXTRA_RETURN_RESULT, true);
            startActivityForResult(uninstallIntent, i);
            hashMap.put(i, tempList.get(i).getPackageName());
        }
    }

    private void askDeleteDialog(int x, long daysDiff,
                                 final List<CleanAppDB> appList) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.cleanapp_custom_dialog);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        TextView appname = (TextView) dialog.findViewById(R.id.appname);
        SharedPreferences sharedPref = getSharedPreferences("MY_SHARED_PREF", Context.MODE_PRIVATE);
        if (daysDiff <= 7) {
            daysDiff = sharedPref.getInt(getString(R.string.dialog_setting), 7);
        }
        appname.setText(x + " apps has not been used for more than " + daysDiff
                + " days. Uninstall these apps" + "?");
        dialog.show();
        Button yes = (Button) dialog.findViewById(R.id.yes);
        Button no = (Button) dialog.findViewById(R.id.no);
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppData.getInstance().setAskDeleteCancel(true);
                DatabaseHandler databaseHandler = new DatabaseHandler(context);
                for (int i = 0; i < appList.size(); i++) {
                    databaseHandler.dontDeleteApps(1, appList.get(i).getPackageName());
                }
                dialog.dismiss();
                finishAffinity();
            }
        });
        yes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),
                        AppManagerActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Bundle bundleObject = new Bundle();
                AppData.getInstance().setAppManagerState1(false);
                AppData.getInstance().setAppManagerUnInstall(true);
                bundleObject.putSerializable("list", (Serializable) appList);
                intent.putExtras(bundleObject);
                startActivity(intent);
                finish();
            }
        });
    }

    private boolean getDonotDeleteStatus(String pname) {
        for (CleanAppDB CDB : list) {
            if (CDB.getPackageName().equalsIgnoreCase(pname)) {
                setCount = setCount + 1;
                return true;
            }
        }
        return false;
    }

    private long getdaysDiff(String applastUsedDate, String currentDate) {
        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long diff = 0;
        try {
            Date date1 = myFormat.parse(applastUsedDate);
            Date date2 = myFormat.parse(currentDate);
            diff = date2.getTime() - date1.getTime();
            Applog.logString("Days: " + "DAYS DIFF --   "
                    + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_CANCELED) {
            AppData.getInstance().setAskDeleteCancel(true);
        } else if (resultCode == Activity.RESULT_OK) {
            positions.add(requestCode);
            deletePackages();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}
