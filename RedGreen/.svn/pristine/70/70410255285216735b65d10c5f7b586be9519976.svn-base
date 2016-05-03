package com.unfoldlabs.redgreen.global;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;

import com.unfoldlabs.redgreen.R;
import com.unfoldlabs.redgreen.activity.SaveBatteryActivity;
import com.unfoldlabs.redgreen.fragment.CleanMemoryFragment;
import com.unfoldlabs.redgreen.interfaces.Constants;
import com.unfoldlabs.redgreen.model.ListItem;
import com.unfoldlabs.redgreen.model.SaveBatteryModel;
import com.unfoldlabs.redgreen.runningapps.CommonLibrary;
import com.unfoldlabs.redgreen.runningapps.ProcessDetailInfo;
import com.unfoldlabs.redgreen.runningapps.ProcessManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class GlobalAsyncTask extends AsyncTask<Void, Void, Void>{

	private Context ctx;
	private List<ListItem> ramUsageList = new ArrayList<ListItem>();
	private List<ListItem> listItem = new ArrayList<ListItem>();
	private List<SaveBatteryModel> batteryBagroundList = new ArrayList<SaveBatteryModel>();
	private List<SaveBatteryModel> cpuCoolerList = new ArrayList<SaveBatteryModel>();
	private double sum;
	private ArrayList<ProcessDetailInfo> mDetailList = new ArrayList<>();
	private ActivityManager mActivityManager = null;

	public GlobalAsyncTask(Context ctx) {
		this.ctx = ctx;
	}

	@Override
	protected Void doInBackground(Void... params) {
		getMemory(ctx);
		getBagroundApps(ctx);

		return null;
	}
	private void getBagroundApps(Context ctx) {


		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1){


			List<ProcessManager.Process> processList = ProcessManager.getRunningApps();

			for (ProcessManager.Process processes: processList) {

				if((!processes.name
						.startsWith("com.android.inputmethod"))
						&& (!processes.name
						.equalsIgnoreCase("system"))
						&& (!processes.name
						.equalsIgnoreCase("com.android.phone"))
						&& (!processes.name
						.equalsIgnoreCase("android.process.acore"))
						&& (!processes.name
						.equalsIgnoreCase("com.htc.android.mail"))
						&& (!processes.name
						.equalsIgnoreCase("com.motorola.android.vvm"))
						&& (!processes.name
						.equalsIgnoreCase("com.android.alarmclock")))
					try{
						ProcessDetailInfo details = new ProcessDetailInfo(ctx,processes.getPackageName());
						mDetailList.add(details);
					}catch(Exception e){

					}
			}

		}else{
			mActivityManager = ((ActivityManager)ctx.getSystemService(
					Context.ACTIVITY_SERVICE));
			mDetailList = CommonLibrary.GetRunningProcess(ctx, mActivityManager, Integer.MIN_VALUE, Constants.SECURITY_LEVEL, false);
		}

		for (ProcessDetailInfo process : mDetailList) {
			ListItem item = new ListItem();
			SaveBatteryModel batteryItem = new SaveBatteryModel();
			if (!process.getLabel().equals(ctx.getResources().getString(R.string.app_name)) /*&& !process.getLabel().equals("Clean Master")
					&& !process.getLabel().equals("Chrome")*/) {
				item.setApptitle(process.getLabel());
				if (null != process.getIcon()) {
					item.setAppIcon(process.getIcon());
					listItem.add(item);
					batteryItem.setApptitle(process.getLabel());
					batteryItem.setAppIcon(process.getIcon());
					batteryBagroundList.add(batteryItem);
				}
			}
		}
	}

	@Override
	protected void onPostExecute(Void result) {

		super.onPostExecute(result);
		AppData.getInstance().setList(ramUsageList);
		AppData.getInstance().setMemorysize(sum);
		AppData.getInstance().setBagroundlist(listItem);
		//Save battery List Model
		AppData.getInstance().setSaveBatteryCpulist(cpuCoolerList);
		AppData.getInstance().setSaveBatteryBagroundlist(batteryBagroundList);
		if(null != CleanMemoryFragment.listener){
			CleanMemoryFragment.listener.getJunkItem();
		}
		if(null != SaveBatteryActivity.mLlistener){
			SaveBatteryActivity.mLlistener.getBackRoundApps();
		}

	}
	private void getMemory(Context ctx) {

		ActivityManager activityManager = (ActivityManager) ctx
				.getSystemService(Context.ACTIVITY_SERVICE);

		Map<Integer, String> pidMap = new TreeMap<Integer, String>();

		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1){
			List<RunningServiceInfo> runningAppProcesses = activityManager
					.getRunningServices(Integer.MAX_VALUE);
			for (RunningServiceInfo runningAppProcessInfo : runningAppProcesses) {
				pidMap.put(runningAppProcessInfo.pid,
						runningAppProcessInfo.process);
			}
		}else{
			List<RunningAppProcessInfo> runningAppProcesses = activityManager
					.getRunningAppProcesses();

			for (RunningAppProcessInfo runningAppProcessInfo : runningAppProcesses) {
				pidMap.put(runningAppProcessInfo.pid,
						runningAppProcessInfo.processName);
			}
		}

		Collection<Integer> keys = pidMap.keySet();
		for (int key : keys) {
			int pids[] = new int[1];
			pids[0] = key;
			android.os.Debug.MemoryInfo[] memoryInfoArray = activityManager
					.getProcessMemoryInfo(pids);

			if (null != ctx)
				for (android.os.Debug.MemoryInfo pidMemoryInfo : memoryInfoArray) {
					PackageManager packageManager = ctx
							.getPackageManager();
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
										R.string.app_name))&&!packageName.contains("com.android")) {
									ListItem item = new ListItem();
									item.setId(pidMemoryInfo.getTotalPss() / 1024);
									sum +=  (pidMemoryInfo.getTotalPss() / 1024);
									item.setIcon(appIcon);
									item.setInfo(title);
									item.setPackageName(packageName);
									ramUsageList.add(item);

									SaveBatteryModel batteryItem = new SaveBatteryModel();
									batteryItem.setId(pidMemoryInfo.getTotalPss() / 1024);
									batteryItem.setIcon(appIcon);
									batteryItem.setInfo(title);
									batteryItem.setPackageName(packageName);
									cpuCoolerList.add(batteryItem);
								}
						}

					}
				}
		}

	}
}
