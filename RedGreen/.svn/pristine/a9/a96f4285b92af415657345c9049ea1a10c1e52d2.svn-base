package com.unfoldlabs.redgreen.model;

import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.unfoldlabs.redgreen.interfaces.AppRateOnClickButtonListener;

public class AppRate {

	private static AppRate singleton;

	private int installDate = 10;

	private int launchTimes = 10;

	private int remindInterval = 1;

	private int eventsTimes = -1;

	private boolean isShowTitle = true;

	private boolean isShowNeutralButton = true;

	private boolean isDebug = false;

	private Context context;

	private View view;

	private AppRateOnClickButtonListener listener;

	private AppRate(Context context) {
		this.context = context.getApplicationContext();
	}

	public static AppRate with(Context context) {
		if (singleton == null) {
			synchronized (AppRate.class) {
				if (singleton == null) {
					singleton = new AppRate(context);
				}
			}
		}
		return singleton;
	}

	public AppRate setLaunchTimes(int launchTimes) {
		this.launchTimes = launchTimes;
		return this;
	}

	public AppRate setInstallDays(int installDate) {
		this.installDate = installDate;
		return this;
	}

	public AppRate setRemindInterval(int remindInterval) {
		this.remindInterval = remindInterval;
		return this;
	}

	public AppRate setShowNeutralButton(boolean isShowNeutralButton) {
		this.isShowNeutralButton = isShowNeutralButton;
		return this;
	}

	public AppRate setEventsTimes(int eventsTimes) {
		this.eventsTimes = eventsTimes;
		return this;
	}

	public AppRate setShowTitle(boolean isShowTitle) {
		this.isShowTitle = isShowTitle;
		return this;
	}

	public AppRate clearAgreeShowDialog() {
		AppRatePreferenceHelper.setAgreeShowDialog(context, true);
		return this;
	}

	public AppRate setDebug(boolean isDebug) {
		this.isDebug = isDebug;
		return this;
	}

	public AppRate setView(View view) {
		this.view = view;
		return this;
	}

	public AppRate setOnClickButtonListener(
			AppRateOnClickButtonListener listener) {
		this.listener = listener;
		return this;
	}

	public void monitor() {
		if (AppRatePreferenceHelper.isFirstLaunch(context)) {
			AppRatePreferenceHelper.setInstallDate(context);
		}
		AppRatePreferenceHelper.setLaunchTimes(context,
				AppRatePreferenceHelper.getLaunchTimes(context) + 1);
	}

	public static boolean showRateDialogIfMeetsConditions(Activity activity) {
		boolean isMeetsConditions = singleton.isDebug
				|| singleton.shouldShowRateDialog();
		singleton.showRateDialog(activity);
		return isMeetsConditions;
	}

	public static boolean passSignificantEvent(Activity activity) {
		return passSignificantEvent(activity, true);
	}

	public static boolean passSignificantEventAndConditions(Activity activity) {
		return passSignificantEvent(activity, singleton.shouldShowRateDialog());
	}

	private static boolean passSignificantEvent(Activity activity,
			boolean shouldShow) {
		Context context = activity.getApplicationContext();
		int eventTimes = AppRatePreferenceHelper.getEventTimes(context);
		AppRatePreferenceHelper.setEventTimes(context, ++eventTimes);
		boolean isMeetsConditions = singleton.isDebug
				|| (singleton.isOverEventPass() && shouldShow);
		if (isMeetsConditions) {
			singleton.showRateDialog(activity);
		}
		return isMeetsConditions;
	}

	public void showRateDialog(Activity activity) {
		if (!activity.isFinishing()) {
			AppRatePreferenceHelper.create(activity, isShowNeutralButton,
					isShowTitle, listener, view).show();
		}
	}

	public boolean isOverEventPass() {
		return eventsTimes != -1
				&& AppRatePreferenceHelper.getEventTimes(context) > eventsTimes;
	}

	public boolean shouldShowRateDialog() {
		return AppRatePreferenceHelper.getIsAgreeShowDialog(context)
				&& isOverLaunchTimes() && isOverInstallDate()
				&& isOverRemindDate();
	}

	private boolean isOverLaunchTimes() {
		return AppRatePreferenceHelper.getLaunchTimes(context) >= launchTimes;
	}

	private boolean isOverInstallDate() {
		return isOverDate(AppRatePreferenceHelper.getInstallDate(context),
				installDate);
	}

	private boolean isOverRemindDate() {
		return isOverDate(AppRatePreferenceHelper.getRemindInterval(context),
				remindInterval);
	}

	private boolean isOverDate(long targetDate, int threshold) {
		return new Date().getTime() - targetDate >= threshold * 24 * 60 * 60
				* 1000;
	}

}
