package com.unfoldlabs.redgreen.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.MapBuilder;
import com.unfoldlabs.redgreen.R;
import com.unfoldlabs.redgreen.db.DatabaseHandler;
import com.unfoldlabs.redgreen.db.SavingDBAdapter;
import com.unfoldlabs.redgreen.db.SavingDBAdapter.RATE_TABLE;
import com.unfoldlabs.redgreen.db.SavingDbConfig;
import com.unfoldlabs.redgreen.global.AppData;
import com.unfoldlabs.redgreen.global.ApplicationData;
import com.unfoldlabs.redgreen.global.BrowserAsyncTask;
import com.unfoldlabs.redgreen.global.GlobalAsyncTask;
import com.unfoldlabs.redgreen.global.JunkAsyncTask;
import com.unfoldlabs.redgreen.global.RamUsage;
import com.unfoldlabs.redgreen.utilty.Utility;

import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;

public class SplashScreenActivity extends Activity {

	private static final int TIME_INTERVEL = 3000;
	private ImageView imageView;
	private SavingDbConfig config;
	private DatabaseHandler database;
	public EasyTracker easyTracker = null;

	/**
	 * To show the spalsh screen layout in device
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		AppData.getInstance().setAskDeleteCancel(false);
		setContentView(R.layout.splashscreen_layout);
		imageView = (ImageView) findViewById(R.id.animatedImage);
		final Animation animRotate = AnimationUtils.loadAnimation(this,

				R.anim.anim_rotate);
		imageView.startAnimation(animRotate);
		easyTracker = EasyTracker.getInstance(this);

		easyTracker.send(MapBuilder.createEvent(getString(R.string.splash_screen), getString(R.string.splash_screen_entered),
				"track event", null).build());
		database = new DatabaseHandler(getApplicationContext());
		new InsertTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		AppData.getInstance().setRamTaskFlag(false);
		AppData.getInstance().setSpeedBoosterService(false);
		AppData.getInstance().setJunkCleaned(false);
		StatFs statFs = new StatFs(Environment.getExternalStorageDirectory()
				.getAbsolutePath());
		long blockSize;
		long totalSize;

		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2){
			blockSize = statFs.getBlockSizeLong();
			totalSize = statFs.getBlockCountLong() * blockSize;
		}else{
			blockSize = statFs.getBlockSize();
			totalSize = statFs.getBlockCount() * blockSize;
		}


		AppData.getInstance().setInternalStorage(totalSize/(1024 * 1024 *1024));

		if (AppData.getInstance().getInternalStorage() <= 8) {
			AppData.getInstance().setDynamicJunkCacheMemorySize(200);
			AppData.getInstance().setDynamicMaxJunkCacheMemorySize(1000);
			AppData.getInstance().setDynamicMinJunkCacheMemorySize(50);
		} else if (AppData.getInstance().getInternalStorage() <= 16) {
			AppData.getInstance().setDynamicJunkCacheMemorySize(500);
			AppData.getInstance().setDynamicMaxJunkCacheMemorySize(2000);
			AppData.getInstance().setDynamicMinJunkCacheMemorySize(100);
		} else if (AppData.getInstance().getInternalStorage() <= 32) {
			AppData.getInstance().setDynamicJunkCacheMemorySize(800);
			AppData.getInstance().setDynamicMaxJunkCacheMemorySize(3000);
			AppData.getInstance().setDynamicMinJunkCacheMemorySize(200);
		} else if (AppData.getInstance().getInternalStorage() >= 32) {
			AppData.getInstance().setDynamicJunkCacheMemorySize(1000);
			AppData.getInstance().setDynamicMaxJunkCacheMemorySize(4000);
			AppData.getInstance().setDynamicMinJunkCacheMemorySize(500);
		}

		ApplicationData.getInstance().setDefaultMemorySize(""+AppData.getInstance().getDynamicJunkCacheMemorySize()+" MB");

		savingSoFarDatabase();
		insertRateCount();

		new GlobalAsyncTask(getApplicationContext()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		new BrowserAsyncTask(getApplicationContext()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		new JunkAsyncTask(getApplicationContext()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		new RamUsage(getApplicationContext()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date resultdate = new Date(getAppFirstInstallTime(getApplicationContext()));

		AppData.getInstance().setAppInstalledTime(sdf.format(resultdate));

		Thread background = new Thread() {
			public void run() {
				try {
					sleep(TIME_INTERVEL);
					Intent intent = new Intent(getApplicationContext(),
							HomeActivity.class);
					easyTracker.send(MapBuilder.createEvent(
							getResources().getString(R.string.splash_screen),
							getResources().getString(
									R.string.splash_screen_navigate),
							"track event", null).build());
					startActivity(intent);
					finish();
				} catch (Exception e) {

				}
			}
		};
		background.start();
	}



	/**
	 * to store the first app installation time
	 *
	 * @param context
	 *
	 * @return
	 */
	private long getAppFirstInstallTime(Context context) {
		PackageInfo packageInfo;
		try {
			packageInfo = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
			return packageInfo.firstInstallTime;
		} catch (NameNotFoundException e) {
			/**
			 * should never happen
			 */
			return 0;
		}
	}

	/**
	 * checking the database is open or not
	 *
	 *
	 */
	private void savingSoFarDatabase() {
		if (!SavingDBAdapter.getInstance().isOpen()) {

			try {
				config = new SavingDbConfig(SavingDBAdapter.DataBase_NAME, 3,
						this);
				SavingDBAdapter.getInstance().open(config);

			} catch (IOException e) {

			} // try to open the database
		}

	}

	/**
	 * insert and update the d	ata into rate us database
	 */

	private void insertRateCount() {

		if (SavingDBAdapter.getInstance().isOpen())
			if (SavingDBAdapter.getInstance().getRateCount() != null
					&& SavingDBAdapter.getInstance().getRateCount().getCount() > 0) {

				int count = SavingDBAdapter.getInstance().getRateCount()
						.getColumnIndex(RATE_TABLE.COUNT);
				SavingDBAdapter.getInstance().updateRateCount(
						1,
						Sum(SavingDBAdapter.getInstance().getRateCount()
								.getInt(count), 1));

			} else {
				SavingDBAdapter.getInstance().addRateCount(1);
			}

		SavingDBAdapter.getInstance().getRate().close();

	}


	/**
	 * update the number of attempts addition
	 */
	private int Sum(int j, int i) {
		int b = j + i;
		return b;
	}


	private class InsertTask extends AsyncTask<Void, Void, Void>{
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			ApplicationData.getInstance().setAppInstalledStartTime(""+Utility.getDefaultDate());
		}

		@Override
		protected Void doInBackground(Void... params) {
			Utility.getAllPackageList(getApplicationContext(), database);
			easyTracker.send(MapBuilder.createEvent(getString(R.string.splash_screen), getString(R.string.splash_screen_inserting_apps),
					"track event", null).build());
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
		}
	}
	@Override
	public void onStart() {
		super.onStart();
		EasyTracker.getInstance(this).activityStart(this);
	}

	@Override
	public void onStop() {
		super.onStop();
		EasyTracker.getInstance(this).activityStop(this);
	}
}