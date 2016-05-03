package com.unfoldlabs.redgreen.global;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.os.AsyncTask;
import android.os.RemoteException;
import android.util.Log;

import com.unfoldlabs.redgreen.model.AppsListItem;
import com.unfoldlabs.redgreen.utilty.Utility;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class JunkCacheTask extends AsyncTask<Void, Integer, List<AppsListItem>> {
	private Context ctx;
	private long mCacheSize = 0;
	private int mAppCount = 0;
	private Method mGetPackageSizeInfoMethod;

	public JunkCacheTask(Context ctx) {
		this.ctx = ctx;
		try {

			mGetPackageSizeInfoMethod = ctx
					.getPackageManager()
					.getClass()
					.getMethod("getPackageSizeInfo", String.class,
							IPackageStatsObserver.class);

		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected List<AppsListItem> doInBackground(Void... params) {
		mCacheSize = 0;

		final List<ApplicationInfo> packages = ctx.getPackageManager()
				.getInstalledApplications(PackageManager.GET_META_DATA);
		final CountDownLatch countDownLatch = new CountDownLatch(
				packages.size());
		final List<AppsListItem> apps = new ArrayList<AppsListItem>();
		publishProgress(0, packages.size());

		try {
			for (ApplicationInfo pkg : packages) {
				mGetPackageSizeInfoMethod.invoke(ctx.getPackageManager(),
						pkg.packageName, new IPackageStatsObserver.Stub() {

							@Override
							public void onGetStatsCompleted(
									PackageStats pStats, boolean succeeded)
									throws RemoteException {
								synchronized (apps) {
									publishProgress(++mAppCount,
											packages.size());

									if (succeeded && pStats.cacheSize > 0) {
										try {
											apps.add(new AppsListItem(
													pStats.packageName,
													ctx.getPackageManager()
															.getApplicationLabel(
																	ctx.getPackageManager()
																			.getApplicationInfo(
																					pStats.packageName,
																					PackageManager.GET_META_DATA))
															.toString(),
													ctx.getPackageManager()
															.getApplicationIcon(
																	pStats.packageName),
													pStats.cacheSize));

											mCacheSize += pStats.cacheSize;
										} catch (PackageManager.NameNotFoundException e) {
											e.printStackTrace();
										}
									}
								}

								synchronized (countDownLatch) {
									countDownLatch.countDown();
								}
							}
						});
			}
			countDownLatch.await();
		} catch (InvocationTargetException | InterruptedException
				| IllegalAccessException e) {
			e.printStackTrace();
		}

		return new ArrayList<>(apps);
	}

	@Override
	protected void onPostExecute(List<AppsListItem> result) {
		super.onPostExecute(result);
		AppData.getInstance().setJunkData(mCacheSize);

		AppData.getInstance().setJunk(result);



		long junkData = AppData.getInstance().getJunkData()/(1024*1024);


		if(!AppData.getInstance().isJunkCleaned()){
			if (AppData.getInstance().getInternalStorage() <= 8 ) {
				if (junkData >= 50 || (junkData >=  AppData.getInstance().getDynamicMaxJunkCacheMemorySize() && 0 < AppData.getInstance().getDynamicMaxJunkCacheMemorySize()) ) {
					Utility.generateNotification(ctx);
					AppData.getInstance().setJunkCleaned(true);
				}
			} else if (AppData.getInstance().getInternalStorage() <= 16) {
				if (junkData >= 100 || (junkData >=  AppData.getInstance().getDynamicMaxJunkCacheMemorySize()&& 0 < AppData.getInstance().getDynamicMaxJunkCacheMemorySize())) {
					Utility.generateNotification(ctx);
					AppData.getInstance().setJunkCleaned(true);
				}
			} else if (AppData.getInstance().getInternalStorage() <= 32) {
				if (junkData >= 200 || (junkData >=  AppData.getInstance().getDynamicMaxJunkCacheMemorySize()&& 0 < AppData.getInstance().getDynamicMaxJunkCacheMemorySize())) {
					Utility.generateNotification(ctx);
					AppData.getInstance().setJunkCleaned(true);
				}
			} else if (AppData.getInstance().getInternalStorage() >= 32) {
				if (junkData >= 500 || (junkData >=  AppData.getInstance().getDynamicMaxJunkCacheMemorySize()&& 0 < AppData.getInstance().getDynamicMaxJunkCacheMemorySize())) {
					Utility.generateNotification(ctx);
					AppData.getInstance().setJunkCleaned(true);
				}
			}
			AppData.getInstance().setSpeedBoosterService(true);
		}
		AppData.getInstance().setTaskRunning(false);
	}

}
