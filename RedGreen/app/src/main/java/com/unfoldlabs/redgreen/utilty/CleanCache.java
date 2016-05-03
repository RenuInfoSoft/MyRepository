package com.unfoldlabs.redgreen.utilty;

import android.content.Context;
import android.content.pm.IPackageDataObserver;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.RemoteException;
import android.os.StatFs;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.CountDownLatch;


public class CleanCache {
	private boolean mIsCleaning = false;
	private Context context;
	private Method  mFreeStorageAndNotifyMethod;
	private long mCacheSize = 0;

	public CleanCache(Context context){
		this.context = context;
		cleanCache();
	}

	public void cleanCache() {
		mIsCleaning = true;
		try {

			mFreeStorageAndNotifyMethod = context.getPackageManager().getClass()
					.getMethod("freeStorageAndNotify", long.class,
							IPackageDataObserver.class);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		try {
			new TaskClean().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		}catch (RuntimeException e){
			e.printStackTrace();
		}
	}


	private class TaskClean extends AsyncTask<Void, Void, Long> {


		@SuppressWarnings("deprecation")
		@Override
		protected Long doInBackground(Void... params) {
			final CountDownLatch countDownLatch = new CountDownLatch(1);
			StatFs statFs = new StatFs(Environment.getDataDirectory()
					.getAbsolutePath());
			long blockSize;
			long blockCount;
			if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2){
				blockSize = statFs.getBlockSizeLong();
				blockCount = statFs.getBlockCountLong();
			}else {
				blockSize = statFs.getBlockSize();
				blockCount = statFs.getBlockCount();
			}
			try {
				mFreeStorageAndNotifyMethod.invoke(
						context.getPackageManager(),
						blockCount	* blockSize,
						new IPackageDataObserver.Stub() {
							@Override
							public void onRemoveCompleted(String packageName,
														  boolean succeeded) throws RemoteException {
								countDownLatch.countDown();
							}
						});
				countDownLatch.await();
			} catch (InvocationTargetException | InterruptedException
					| IllegalAccessException e) {
				e.printStackTrace();
			}
			return mCacheSize;
		}



		@Override
		protected void onPostExecute(Long result) {
			mIsCleaning = false;
		}
	}
}
