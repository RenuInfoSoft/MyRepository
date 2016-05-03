package com.unfoldlabs.redgreen.applock.util;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;

import java.util.Calendar;
import java.util.List;

public abstract class Util {
	static Context context;
	//added below methond
	public static boolean isRedGreenServiceRunning(Class<?> serviceClass,Context context) {
	    ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
	    for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
	        if (serviceClass.getName().equals(service.service.getClassName())) {
	            return true;
	        }
	    }
	    return false;
	}
	/**
	 * Sets the background {@link Drawable} of a view.<br>
	 * On API level 16+ {@link View#setBackgroundDrawable(Drawable)} is
	 * deprecated, so we use the new method {@link View#setBackground(Drawable)}
	 *
	 * @param v
	 *            The {@link View} on which to set the background
	 * @param bg
	 *            The background
	 */
	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	public static void setBackgroundDrawable(View v, Drawable bg) {
		if (v == null)
			return;
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
			v.setBackgroundDrawable(bg);
		} else {
			v.setBackground(bg);
		}
	}
	public static List<UsageStats> getUsageStatsList(Context context){
		UsageStatsManager usm = getUsageStatsManager(context);
		Calendar calendar = Calendar.getInstance();
		long endTime = calendar.getTimeInMillis();
		calendar.add(Calendar.YEAR, -1);
		long startTime = calendar.getTimeInMillis();

		List<UsageStats> usageStatsList = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY,startTime,endTime);
		return usageStatsList;
	}
	private static UsageStatsManager getUsageStatsManager(Context context){
		UsageStatsManager usm = (UsageStatsManager) context.getSystemService("usagestats");
		return usm;
	}
}