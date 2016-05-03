package com.unfoldlabs.redgreen.global;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.os.AsyncTask;
import android.os.RemoteException;

import com.unfoldlabs.redgreen.background.RunBackGround;
import com.unfoldlabs.redgreen.fragment.CleanMemoryFragment;
import com.unfoldlabs.redgreen.model.AppsListItem;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class JunkAsyncTask extends AsyncTask<Void, Integer, List<AppsListItem>> {
	private Context ctx;
	private int mAppCount = 0;
	private Method mGetPackageSizeInfoMethod;
	private long mCacheSize;

	public JunkAsyncTask(Context ctx) {
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
		} catch (InvocationTargetException | InterruptedException| IllegalAccessException e) {
			e.printStackTrace();
		}

		return new ArrayList<>(apps);
	}

	@Override
	protected void onPostExecute(List<AppsListItem> result) {
		super.onPostExecute(result);
		AppData.getInstance().setJunkData(mCacheSize);
		AppData.getInstance().setJunk(result);

		if (null != CleanMemoryFragment.listener) {
			CleanMemoryFragment.listener.getJunkItem();
		}
		if (null != RunBackGround.listener) {
			if (AppData.getInstance().isSpeedBoosterService()) {

				RunBackGround.listener.getJunkItem();
			}
		}
		AppData.getInstance().setTaskRunning(false);
	}

}