package com.unfoldlabs.redgreen.runningapps;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Class to get process details information which are running in background
 *
 */
public class ProcessDetailInfo {
	private static HashMap<String, ResolveInfo> AppsTable;
	public static final String IGNORE_PREFS_NAME = "IgnoredPackage";
	public static final String SELECT_PREFS_NAME = "CleanoidUnselectedPackage";
	private static SharedPreferences.Editor mIgnoredAppEditor;
	public static SharedPreferences mIgnoredAppSettings;
	private static SharedPreferences.Editor mSelectedAppEditor;
	public static SharedPreferences mSelectedAppSettings;
	public int Importance;
	private ApplicationInfo mAppInfo = null;
	private String mLabel;
	private String mPackageName;
	private PackageInfo mPkgInfo = null;
	private PackageManager mPkgManager;
	private ResolveInfo mResolveInfo;
	private boolean isApplication = true;
	private ArrayList<String> titles;
	private ArrayList<String> urls;
	private ArrayList<Bitmap> bitmaps;
	private boolean isSelect = false;

	public boolean isSelected() {
		return isSelect;
	}

	public void setSelected(boolean isSelect) {
		this.isSelect = isSelect;
	}

	/**
	 * Constructor for ProcessDetailInfo
	 *
	 * @param paramContext
	 * @param paramString
	 * @throws Exception
	 */

	public ProcessDetailInfo(Context paramContext, String paramString)
			throws Exception {
		loadApps(paramContext);
		mResolveInfo = AppsTable.get(paramString);
		if ((mResolveInfo != null) && (mResolveInfo.activityInfo != null)
				&& (mResolveInfo.activityInfo.applicationInfo != null))
			mAppInfo = mResolveInfo.activityInfo.applicationInfo;
		else
			isApplication = false;
		mPackageName = paramString;
		if(paramContext != null){
			if (mPkgManager == null)
				mPkgManager = paramContext.getApplicationContext()
						.getPackageManager();
			if (mSelectedAppSettings == null)
				mSelectedAppSettings = paramContext.getSharedPreferences(
						"CleanoidUnselectedPackage", 0);
			if (mSelectedAppEditor == null)
				mSelectedAppEditor = mSelectedAppSettings.edit();
			if (mIgnoredAppSettings == null)
				mIgnoredAppSettings = paramContext.getSharedPreferences(
						"IgnoredPackage", 0);
			if (mIgnoredAppEditor == null)
				mIgnoredAppEditor = mIgnoredAppSettings.edit();
		}
	}

	@SuppressWarnings("null")
	public static boolean IsPersistentApp(PackageInfo paramPackageInfo) {
		boolean bool = false;
		if (paramPackageInfo == null)
			bool = false;
		else {
			if ((paramPackageInfo.applicationInfo != null)
					&& ((ApplicationInfo.FLAG_PERSISTENT & paramPackageInfo.applicationInfo.flags) == ApplicationInfo.FLAG_PERSISTENT)) {
				bool = true;
			} else {
				if (paramPackageInfo.activities == null)
					bool = false;
			}
			return bool;
		}
		ActivityInfo[] arrayOfActivityInfo = paramPackageInfo.activities;
		int i = arrayOfActivityInfo.length;
		for (int j = 0;; j++) {
			if (j >= i) {
				bool = false;
				break;
			}
			ActivityInfo localActivityInfo = arrayOfActivityInfo[j];
			if ((localActivityInfo != null)
					&& (localActivityInfo.applicationInfo != null)
					&& ((ApplicationInfo.FLAG_PERSISTENT & localActivityInfo.applicationInfo.flags) == ApplicationInfo.FLAG_PERSISTENT)) {
				bool = true;
				break;
			}
		}
		return bool;
	}

	private PackageInfo getPackageInfo() {
		if (mPkgInfo != null) {
			try {
				mPkgInfo = mPkgManager.getPackageInfo(mAppInfo.packageName,
						ApplicationInfo.FLAG_SYSTEM);
				return mPkgInfo;
			} catch (PackageManager.NameNotFoundException localNameNotFoundException) {
			}
		}
		return mPkgInfo;
	}

	/**
	 * Method to LoadAll Apps From background
	 *
	 * @param paramContext
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static void loadApps(Context paramContext) {
		Iterator localIterator = null;
		if (AppsTable == null) {
			AppsTable = new HashMap();
			Intent localIntent = new Intent("android.intent.action.MAIN", null);
			localIntent.addCategory("android.intent.category.LAUNCHER");
			localIterator = paramContext.getPackageManager()
					.queryIntentActivities(localIntent, 0).iterator();
			while (localIterator.hasNext()) {
				ResolveInfo localResolveInfo = (ResolveInfo) localIterator
						.next();
				if ((localResolveInfo != null)
						&& (localResolveInfo.activityInfo != null)
						&& (localResolveInfo.activityInfo.packageName != null)) {
					AppsTable.put(localResolveInfo.activityInfo.processName,
							localResolveInfo);
				}
			}
		}
	}

	public static void setSelected(boolean paramBoolean, Context paramContext,
								   String paramString) {
		if (mSelectedAppSettings == null)
			mSelectedAppSettings = paramContext.getSharedPreferences(
					"CleanoidUnselectedPackage", 0);
		if (mSelectedAppEditor == null)
			mSelectedAppEditor = mSelectedAppSettings.edit();
		if ((paramBoolean) && (mSelectedAppSettings.contains(paramString)))
			mSelectedAppEditor.remove(paramString);
		else {
			if (!paramBoolean) {
				mSelectedAppEditor.putBoolean(paramString, true);
			}
		}
		mSelectedAppEditor.commit();
	}

	public String getBaseActivity() {
		if (mResolveInfo != null)
			return mResolveInfo.activityInfo.name;
		return getPackageInfo().activities[0].name;
	}

	public Drawable getIcon() {
		if (mAppInfo != null)
			return mAppInfo.loadIcon(mPkgManager);
		return null;
	}

	public boolean getIgnored() {
		return mIgnoredAppSettings.getBoolean(mPackageName, false);
	}

	public String getLabel() {
		try {
			if ((mPackageName != null)
					&& (mPackageName.equals(mAppInfo.processName)))
				mLabel = mAppInfo.loadLabel(mPkgManager).toString();
			else
				mLabel = mAppInfo.processName;
			return mLabel;
		} catch (Exception localException) {
			mLabel = mPackageName;
		}
		return mLabel;
	}

	/**
	 * @return package name
	 */
	public String getPackageName() {
		return mPackageName;
	}

	/**
	 * @return package name(process)
	 */
	public String getProcessName() {
		return mPackageName;
	}

	public boolean getSelected() {
		return !mSelectedAppSettings.getBoolean(mPackageName, false);
	}

	public boolean isApplication() {
		return isApplication;
	}

	public boolean isGoodProcess() {
		return mAppInfo != null;
	}

	public boolean isSystemApp() {
		return (mAppInfo != null)
				&& ((ApplicationInfo.FLAG_SYSTEM ^ mAppInfo.flags) == ApplicationInfo.FLAG_SYSTEM);
	}

	public void setIgnored(boolean paramBoolean) {
		if (paramBoolean)
			mIgnoredAppEditor.putBoolean(mPackageName, true);
		else
			mIgnoredAppEditor.remove(mPackageName);
		mIgnoredAppEditor.commit();
	}

	/**
	 * app label
	 *
	 * @param paramString
	 */
	public void setLabel(String paramString) {
		mLabel = paramString;
	}

	public List<String> getTitles() {
		return titles;
	}

	/**
	 * set title
	 *
	 * @param titles
	 */
	public void setTitles(ArrayList<String> titles) {
		this.titles = titles;
	}

	public List<String> getUrls() {
		return urls;
	}

	/**
	 * set Url
	 *
	 * @param urls
	 */
	public void setUrls(ArrayList<String> urls) {
		this.urls = urls;
	}

	public List<Bitmap> getBitmaps() {
		return bitmaps;
	}

	public void setBitmaps(ArrayList<Bitmap> bitmaps) {
		this.bitmaps = bitmaps;
	}

}