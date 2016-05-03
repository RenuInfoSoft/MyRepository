package com.unfoldlabs.redgreen.model;

import java.util.Date;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.unfoldlabs.redgreen.R;
import com.unfoldlabs.redgreen.interfaces.AppRateOnClickButtonListener;

public final class AppRatePreferenceHelper {

	private static final String PREF_FILE_NAME = "android_rate_pref_file";

	private static final String PREF_KEY_INSTALL_DATE = "android_rate_install_date";

	private static final String PREF_KEY_LAUNCH_TIMES = "android_rate_launch_times";

	private static final String PREF_KEY_IS_AGREE_SHOW_DIALOG = "android_rate_is_agree_show_dialog";

	private static final String PREF_KEY_REMIND_INTERVAL = "android_rate_remind_interval";

	private static final String PREF_KEY_EVENT_TIMES = "android_rate_event_times";

	private AppRatePreferenceHelper() {
	}

	static SharedPreferences getPreferences(Context context) {
		return context.getSharedPreferences(PREF_FILE_NAME,
				Context.MODE_PRIVATE);
	}

	static Editor getPreferencesEditor(Context context) {
		return getPreferences(context).edit();
	}

	/**
	 * Clear data in shared preferences.<br/>
	 * 
	 * @param context
	 *            context
	 */
	static void clearSharedPreferences(Context context) {
		final SharedPreferences.Editor editor = getPreferencesEditor(context);
		editor.remove(PREF_KEY_INSTALL_DATE);
		editor.remove(PREF_KEY_LAUNCH_TIMES);
		commitOrApply(editor);
	}

	/**
	 * Set agree flag about show dialog.<br/>
	 * If it is false, rate dialog will never shown unless data is cleared.
	 * 
	 * @param context
	 *            context
	 * @param isAgree
	 *            agree with showing rate dialog
	 */
	public static void setAgreeShowDialog(Context context, boolean isAgree) {
		final SharedPreferences.Editor editor = getPreferencesEditor(context);
		editor.putBoolean(PREF_KEY_IS_AGREE_SHOW_DIALOG, isAgree);
		commitOrApply(editor);
	}

	static boolean getIsAgreeShowDialog(Context context) {
		return getPreferences(context).getBoolean(
				PREF_KEY_IS_AGREE_SHOW_DIALOG, true);
	}

	/**
	 * set remind interval in shared preferences
	 * @param context
	 */
	static void setRemindInterval(Context context) {
		SharedPreferences.Editor editor = getPreferencesEditor(context);
		editor.remove(PREF_KEY_REMIND_INTERVAL);
		editor.putLong(PREF_KEY_REMIND_INTERVAL, new Date().getTime());
		commitOrApply(editor);
	}

	static long getRemindInterval(Context context) {
		return getPreferences(context).getLong(PREF_KEY_REMIND_INTERVAL, 0);
	}

	/**
	 * Set install date in shared preferences
	 * @param context
	 */
	static void setInstallDate(Context context) {
		SharedPreferences.Editor editor = getPreferencesEditor(context);
		editor.putLong(PREF_KEY_INSTALL_DATE, new Date().getTime());
		commitOrApply(editor);
	}

	static long getInstallDate(Context context) {
		return getPreferences(context).getLong(PREF_KEY_INSTALL_DATE, 0);
	}

	/**
	 * set launch times in shared preferences
	 */
	static void setLaunchTimes(Context context, int launchTimes) {
		SharedPreferences.Editor editor = getPreferencesEditor(context);
		editor.putInt(PREF_KEY_LAUNCH_TIMES, launchTimes);
		commitOrApply(editor);
	}

	static int getLaunchTimes(Context context) {
		return getPreferences(context).getInt(PREF_KEY_LAUNCH_TIMES, 0);
	}

	static boolean isFirstLaunch(Context context) {
		return getPreferences(context).getLong(PREF_KEY_INSTALL_DATE, 0) == 0L;
	}

	static int getEventTimes(Context context) {
		return getPreferences(context).getInt(PREF_KEY_EVENT_TIMES, 0);
	}

	/**
	 * set event times in shared preferences
	 */
	static void setEventTimes(Context context, int eventTimes) {
		SharedPreferences.Editor editor = getPreferencesEditor(context);
		editor.putInt(PREF_KEY_EVENT_TIMES, eventTimes);
		commitOrApply(editor);
	}

	private static void commitOrApply(SharedPreferences.Editor editor) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			editor.apply();
		} else {
			editor.commit();
		}
	}

	public static final String GOOGLE_PLAY_PACKAGE_NAME = "com.android.vending";


	static Dialog create(final Context context,
						 final boolean isShowNeutralButton, final boolean isShowTitle,
						 final AppRateOnClickButtonListener listener, final View view) {
		final Dialog dialog = new Dialog(context);
		dialog.setCanceledOnTouchOutside(false);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.rate_us_custom_dialog);
		TextView noThanks = (TextView) dialog.findViewById(R.id.no_thanks);
		TextView later = (TextView) dialog.findViewById(R.id.later);
		TextView rateNow = (TextView) dialog.findViewById(R.id.rate_now);
		/**
		 * Rate us on Google Play dialog show
		 */
		dialog.show();
		rateNow.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String packageName = context.getPackageName();
				Intent intent = new Intent(Intent.ACTION_VIEW, getGooglePlay(packageName));
				if (isPackageExists(context,
						GOOGLE_PLAY_PACKAGE_NAME)) {
					intent.setPackage(GOOGLE_PLAY_PACKAGE_NAME);
				}
				context.startActivity(intent);
				AppRatePreferenceHelper.setAgreeShowDialog(context, false);
				if (listener != null)
					listener.onClickButton(-1);
			}
		});
		later.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				AppRatePreferenceHelper.setRemindInterval(context);
				if (listener != null)
					listener.onClickButton(-3);
			}
		});
		noThanks.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				AppRatePreferenceHelper.setAgreeShowDialog(context, false);
				if (listener != null)
					listener.onClickButton(-2);
			}
		});
		return dialog;
	}

	public static final String GOOGLE_PLAY = "https://play.google.com/store/apps/details?id=com.unfoldlabs.redgreen&hl=en";
	public static Uri getGooglePlay(String packageName) {
		return packageName == null ? null : Uri
				.parse(GOOGLE_PLAY + packageName);
	}

	public static boolean isPackageExists(Context context, String targetPackage) {
		PackageManager pm = context.getPackageManager();
		List<ApplicationInfo> packages = pm.getInstalledApplications(0);
		for (ApplicationInfo packageInfo : packages) {
			if (packageInfo.packageName.equals(targetPackage))
				return true;
		}
		return false;
	}
}