package com.unfoldlabs.redgreen.background;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.os.AsyncTask;
import android.provider.Browser;
import android.support.v4.app.NotificationCompat;
import android.support.v4.text.BidiFormatter;
import android.text.format.Formatter;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.unfoldlabs.redgreen.R;
import com.unfoldlabs.redgreen.activity.HomeActivity;
import com.unfoldlabs.redgreen.global.AppData;
import com.unfoldlabs.redgreen.global.JunkAsyncTask;
import com.unfoldlabs.redgreen.interfaces.Constants;
import com.unfoldlabs.redgreen.interfaces.JunkListner;
import com.unfoldlabs.redgreen.model.ListItem;
import com.unfoldlabs.redgreen.utilty.CleanCache;
import com.unfoldlabs.redgreen.utilty.Utility;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class RunBackGround implements JunkListner {

	private Context ctx;
	private ActivityManager mActivityManager = null;
	private ContentResolver contentResolver;
	private ClipboardManager myClipboard;
	private int savedData;
	private double junkData;
	private List<ListItem> ramUsageList = new ArrayList<ListItem>();
	private NotificationManager mManager;
	public static JunkListner listener;
	private NotificationManager notificationManager;

	public RunBackGround(Context ctx, NotificationManager notificationManager) {
		this.ctx = ctx;
		this.notificationManager = notificationManager;
		listener = this;
		getJunk();
		clearHistory();
		cleanClipboardData();
		getMemory(ctx);
		getStartupApps();
		cleanCache();
	}

	private void getJunk() {
		if(!AppData.getInstance().isTaskRunning())
			new JunkAsyncTask(ctx).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
	}

	private void cleanCache() {
		new CleanCache(ctx);
	}

	private void clearHistory() {
		contentResolver = ctx.getContentResolver();
		try {
			if (Browser.canClearHistory(contentResolver)) {
				Browser.clearHistory(contentResolver);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void cleanClipboardData() {
		myClipboard = (ClipboardManager) ctx
				.getSystemService(Context.CLIPBOARD_SERVICE);
		ClipData data = ClipData.newPlainText("", "");
		myClipboard.setPrimaryClip(data);
	}

	private void getStartupApps() {
		mActivityManager = ((ActivityManager) ctx
				.getSystemService(Context.ACTIVITY_SERVICE));
		int ramBefore = Utility.getRAM(ctx);
		for (ListItem iterable_element : ramUsageList) {
			mActivityManager.killBackgroundProcesses(iterable_element.getPackageName());
		}
		savedData = ramBefore;
	}

	private void getMemory(Context ctx) {

		ActivityManager activityManager = (ActivityManager) ctx
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> runningAppProcesses = activityManager
				.getRunningAppProcesses();
		Map<Integer, String> pidMap = new TreeMap<Integer, String>();
		for (RunningAppProcessInfo runningAppProcessInfo : runningAppProcesses) {
			pidMap.put(runningAppProcessInfo.pid,
					runningAppProcessInfo.processName);
		}
		Collection<Integer> keys = pidMap.keySet();
		for (int key : keys) {
			int pids[] = new int[1];
			pids[0] = key;
			android.os.Debug.MemoryInfo[] memoryInfoArray = activityManager
					.getProcessMemoryInfo(pids);

			if (null != ctx)
				for (android.os.Debug.MemoryInfo pidMemoryInfo : memoryInfoArray) {
					PackageManager packageManager = ctx.getPackageManager();
					ApplicationInfo applicationInfo = null;
					try {
						applicationInfo = packageManager.getApplicationInfo(
								pidMap.get(pids[0]), 0);
					} catch (final NameNotFoundException e) {
					}
					final String title = (String) ((applicationInfo != null) ? packageManager
							.getApplicationLabel(applicationInfo) : "???");
					final String packageName = pidMap.get(pids[0]);
					if (null != applicationInfo) {
						Drawable appIcon = applicationInfo
								.loadIcon(packageManager);

						if ((!title.equals("???")) && null != appIcon) {
							if (null != ctx)
								if (!title.equals(ctx.getResources().getString(
										R.string.app_name))) {
									ListItem item = new ListItem();
									item.setId(pidMemoryInfo.getTotalPss() / 1024);
									item.setIcon(appIcon);
									item.setInfo(title);
									item.setPackageName(packageName);
									ramUsageList.add(item);
								}
						}
					}
				}
		}
	}
	@Override
	public void getJunkItem() {

		junkData = AppData.getInstance().getJunkData();
		int m3 = (int)junkData;
		if(savedData==0){
			getStartupApps();
		}
		try{
			Utility.cleanCacheDB(m3,savedData/1024);
		}catch(Exception e){
		}

		if(AppData.getInstance().isFromNoifiction()){
			generateNotification(ctx, m3,savedData/1024);
		}
	}

	@SuppressWarnings("deprecation")
	private void generateNotification(Context context, float junkSize, float m1) {
		String ramClean = "";
		String junk = "";
		String junkMessage = "";
		String ramMessage = "";

		long medMemory = AppData.getInstance().getJunkData();
		BidiFormatter bidiFormatter = BidiFormatter.getInstance();
		String sizeStr = bidiFormatter.unicodeWrap(Formatter.formatShortFileSize(context, medMemory));
		if(sizeStr.equals("0.00B")){
			junk = "0.00 MB";
			junkMessage = "Speed Booster saved " + junk + " of Storage";
		//	junkMessage = "Junk is already cleaned by RedGreen.";
		}else {
			junk = context.getString(R.string.apps_list_header_memory, sizeStr);
			String substring = sizeStr.substring(Math.max(sizeStr.length() - 2, 0));
			junk = removeLastChar(sizeStr) + " " + substring;
			junkMessage = "Speed Booster saved " + junk + " of Storage";
		}

		sumFinalListener = (int) AppData.getInstance().getMemorysize();
		if (sumFinalListener <= 0) {
			ramClean = "0.00 MB";
			sumFinalListener = (int) AppData.getInstance().getMemorysize();
			if(sumFinalListener <= 0){
				ramMessage = " and " + ramClean + " of Memory in Automatic Mode.";
			//	ramMessage = " and RAM is already cleaned by RedGreen.";
			}else{
				ramClean = sumFinalListener + " MB";
				ramMessage = " and " + ramClean + " of Memory in Automatic Mode.";
			}
		} else {
			ramClean = sumFinalListener + " MB";
			ramMessage = " and " + ramClean + " of Memory in Automatic Mode.";
		}

		String txtMassage = junkMessage + ramMessage;

		notificationManager.cancel(Constants.mNotificationId);
		int mNotificationId = 002;
		Intent intent1 = new Intent(context, HomeActivity.class);
		PendingIntent resultPendingIntent =	PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
		Notification notification = mBuilder.setSmallIcon(R.drawable.app_icon).setTicker("Speed Booster Running").setWhen(0)
				.setAutoCancel(true)
				.setContentTitle("RedGreen")
				.setStyle(new NotificationCompat.BigTextStyle().bigText(txtMassage))
				.setContentIntent(resultPendingIntent)
				.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
				/*.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.app_icon))*/
				.setContentText(txtMassage).build();

		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(mNotificationId, notification);

		speedBoosterAutoMemDialog(txtMassage, context);
		AppData.getInstance().setFromNoifiction(false);
	}
	/**
	 * Booster dialog box creation
	 */
	private int sumFinalListener;
	private void speedBoosterAutoMemDialog(String txtMassage, Context context) {
		final Dialog dialog = new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		dialog.setContentView(R.layout.speedbooster_baground_mem_dialogue);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(false);
		TextView appname = (TextView) dialog.findViewById(R.id.junk);
		appname.setText(txtMassage);
		dialog.show();
		Button ok = (Button) dialog.findViewById(R.id.ok);
		ok.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
	}

	@Override
	public void getRam() {

	}
	public String removeLastChar(String s) {
		if (s == null || s.length() == 0) {
			return s;
		}
		return s.substring(0, s.length()-2);

	}
}
