package com.unfoldlabs.redgreen.utilty;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.PackageStats;
import android.content.res.Resources;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.RemoteException;
import android.os.StatFs;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.text.format.Formatter;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.unfoldlabs.redgreen.R;
import com.unfoldlabs.redgreen.activity.HomeActivity;
import com.unfoldlabs.redgreen.background.CleanAppBackground;
import com.unfoldlabs.redgreen.background.RunBackGround;
import com.unfoldlabs.redgreen.db.DatabaseHandler;
import com.unfoldlabs.redgreen.db.SavingDBAdapter;
import com.unfoldlabs.redgreen.global.AppData;
import com.unfoldlabs.redgreen.global.ApplicationData;
import com.unfoldlabs.redgreen.interfaces.Constants;
import com.unfoldlabs.redgreen.log.Applog;
import com.unfoldlabs.redgreen.model.AppsInfo;
import com.unfoldlabs.redgreen.model.CleanAppDB;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Utility {

	public static double totalSize = 0.0;
	public static double newSize;
	public static double allTotalSize;
	public static int sumFinal;
	public static Map<String,String> map = null;
	static int count = 0;
	static int appCount = 0;

	private static boolean isSelected;

	public static NotificationManager mManager;

	public static void showAlertDialog(Context context, String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(context.getResources().getString(R.string.app_name));
		builder.setMessage(message);
		builder.setCancelable(true);
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});
		AlertDialog alert = builder.create();
		alert.setCanceledOnTouchOutside(false);
		alert.show();

	}

	private static ProgressDialog progressDialog;

	public static void showProgressDialog(Context context, String string) {
		progressDialog = new ProgressDialog(context, AlertDialog.THEME_HOLO_LIGHT);
		progressDialog.setMessage(string);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.setCancelable(false);
		progressDialog.show();
	}

	public static void DismissDialog(){
		if(null != progressDialog)
			progressDialog.dismiss();
	}

	public static boolean isNetworkAvailable(Context c) {
		ConnectivityManager connectivityManager = (ConnectivityManager) c
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

	public static String getDefaultDate() {
		Calendar calender = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.getDefault());
		String formatDate = df.format(calender.getTime());
		return formatDate;
	}

	public static String getDbPath(Context c) {
		return c.getDatabasePath(Constants.DB_NAME).getAbsolutePath();
	}

	public static String getAppName(String packageName,
									PackageManager packageManager) {

		try {
			return (String) packageManager.getApplicationLabel(packageManager
					.getApplicationInfo(packageName,
							PackageManager.GET_META_DATA));
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return "";
	}

	public static double getInternalMemory() {

		int internalMemoryePercentage = 0;
		StatFs statFs = new StatFs(Environment.getExternalStorageDirectory()
				.getAbsolutePath());

		long blockSize;
		long totalSize;
		long freeSize;

		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2){
			blockSize = statFs.getBlockSizeLong();
			totalSize = statFs.getBlockCountLong() * blockSize / (1024 * 1024);
			freeSize = statFs.getFreeBlocksLong() * blockSize / (1024 * 1024);
		}else{
			blockSize = statFs.getBlockSize();
			totalSize = statFs.getBlockCount() * blockSize / (1024 * 1024);
			freeSize = statFs.getFreeBlocks() * blockSize / (1024 * 1024);
		}
		/** AWS setHomeTotalROM, setTotalROM**/
		ApplicationData.getInstance().setHomeTotalROM("" + totalSize);
		ApplicationData.getInstance().setTotalROM("" + totalSize);

		double availableinternalMemory = (totalSize - freeSize);
		double availableinternalMemoryPercentage = ((availableinternalMemory / totalSize) * 100.00d);

		/** AWS setHomeUsedROM, setUsedROM **/
		ApplicationData.getInstance().setHomeUsedROM("" + availableinternalMemoryPercentage);
		ApplicationData.getInstance().setUsedROM("" + availableinternalMemoryPercentage);

		internalMemoryePercentage = (int) Math.ceil(availableinternalMemoryPercentage);
		/** AWS  setPercentageROM **/
		ApplicationData.getInstance().setPercentageROM("" + internalMemoryePercentage);

		return internalMemoryePercentage;
	}

	/**
	 * Method for to get TotalMemory
	 *
	 * @return
	 */
	@SuppressWarnings("unused")
	public static double getTotalMemory() {

		double totalMemory = 0.0;
		String str1 = "/proc/meminfo";
		String str2;
		String[] arrayOfString;
		double initial_memory = 0;
		try {
			FileReader localFileReader = new FileReader(str1);
			BufferedReader localBufferedReader = new BufferedReader(
					localFileReader, 8192);
			str2 = localBufferedReader.readLine();// meminfo
			arrayOfString = str2.split("\\s+");
			for (String num : arrayOfString) {
			}

			initial_memory = Integer.valueOf(arrayOfString[1]).doubleValue() * 1024;
			totalMemory = (initial_memory) / (1024 * 1024);
			localBufferedReader.close();
		} catch (IOException e) {

		}
		return totalMemory;
	}

	public static int getRAM(Context mContext){
		double percentRAM;
		ActivityManager activityManager = (ActivityManager)mContext.getSystemService(Context.ACTIVITY_SERVICE);
		ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
		activityManager.getMemoryInfo(memoryInfo);

		long availableMemory = memoryInfo.availMem / 1048576L;
		long totalMemory = memoryInfo.totalMem/1048576L;
		/** AWS setTotalRAM, setUsedRAM**/
		ApplicationData.getInstance().setTotalRAM("" + totalMemory);
		ApplicationData.getInstance().setUsedRAM("" + (totalMemory - availableMemory));
		percentRAM = ((double) (totalMemory - availableMemory)/totalMemory) * 100;

		/** AWS setPercentageRAM**/
		ApplicationData.getInstance().setPercentageRAM(""+percentRAM);
		return (int)percentRAM;
	}


	public static String getDeviceName() {
		String manufacturer = Build.MANUFACTURER;
		String model = Build.MODEL;
		if (model.startsWith(manufacturer)) {
			return capitalize(model);
		} else {
			return capitalize(manufacturer) + " " + model;
		}
	}

	private static String capitalize(String s) {
		if (s == null || s.length() == 0) {
			return "";
		}
		char first = s.charAt(0);
		if (Character.isUpperCase(first)) {
			return s;
		} else {
			return Character.toUpperCase(first) + s.substring(1);
		}
	}

	public static String getMacAddress(Context ctx) {
		WifiManager wimanager = (WifiManager) ctx
				.getSystemService(Context.WIFI_SERVICE);
		String macAddress = wimanager.getConnectionInfo().getMacAddress();
		if (macAddress == null) {
			macAddress = "Device don't have mac address or wi-fi is disabled";
		}
		return macAddress;
	}

	public static String getBluetoothMacAddress() {
		BluetoothAdapter mBluetoothAdapter = BluetoothAdapter
				.getDefaultAdapter();

		/**
		 * if device does not support Bluetooth
		 */
		if (mBluetoothAdapter == null) {
			return null;
		}

		return mBluetoothAdapter.getAddress();
	}

	public static String getInternalStorage(Context ctx) {

		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize;
		long totalBlocks = 0;

		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2){
			blockSize = stat.getBlockSizeLong();
			totalSize = stat.getBlockCountLong();
		}else{
			blockSize = stat.getBlockSize();
			totalBlocks = stat.getBlockCount();
		}
		return Formatter.formatFileSize(ctx, totalBlocks * blockSize);
	}

	@SuppressWarnings("static-access")
	public static String getTotalRAM(Context ctx) {
		ActivityManager actManager = (ActivityManager) ctx
				.getSystemService(ctx.ACTIVITY_SERVICE);
		MemoryInfo memInfo = new ActivityManager.MemoryInfo();
		actManager.getMemoryInfo(memInfo);
		long totalMemory = memInfo.availMem;
		return Formatter.formatFileSize(ctx, totalMemory);
	}

	public static ArrayList<AppsInfo> getInstalledApps(Context ctx) {
		ArrayList<AppsInfo> listApps = new ArrayList<AppsInfo>();
		final PackageManager pm = ctx.getPackageManager();
		DatabaseHandler db = new DatabaseHandler(ctx);
		List<PackageInfo> PackList = ctx.getPackageManager()
				.getInstalledPackages(0);

		for (int i = 0; i < PackList.size(); i++) {

			final AppsInfo apps = new AppsInfo();

			PackageInfo PackInfo = PackList.get(i);

			PackageManager packageManager = ctx.getPackageManager();
			String label = " ";

			if ((PackInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {

				try {
					if (!PackInfo.packageName.contains("com.android.keyguard")) {
						label = (String) packageManager
								.getApplicationLabel(packageManager
										.getApplicationInfo(
												PackInfo.packageName,
												PackageManager.GET_META_DATA));
					}
				} catch (NameNotFoundException e1) {
					e1.printStackTrace();
				}

				apps.setLabel(label);
				if (null != PackInfo.packageName) {
					apps.setPackageName(PackInfo.packageName);
				}
			} else {
				apps.setPackageName("");
			}

			apps.setVersionCode("" + PackInfo.versionCode);
			if (null != PackInfo.versionName) {
				apps.setVersionName(PackInfo.versionName);
			} else {
				apps.setVersionName("");
			}
			try {
				PackageInfo packageInfo = pm.getPackageInfo(
						PackInfo.packageName,
						PackageManager.GET_PERMISSIONS);
				int uid = packageInfo.applicationInfo.uid;
				apps.setPid(uid);

				Date installTime = new Date(packageInfo.firstInstallTime);
				apps.setInstalledTime(installTime.toString());
				apps.setUpdatedTime(db
						.getLastusedTime(packageInfo.packageName));
			} catch (PackageManager.NameNotFoundException e) {
				e.printStackTrace();
			}

			listApps.add(apps);

		}

		return listApps;

	}

	public static List<CleanAppDB> getAllPackageList(Context ctx) {

		List<CleanAppDB> list = new ArrayList<CleanAppDB>();

		PackageManager pm = ctx.getPackageManager();
		List<ApplicationInfo> packages = pm
				.getInstalledApplications(PackageManager.GET_META_DATA);
		for (ApplicationInfo applicationInfo : packages) {
			try {
				PackageInfo packageInfo = pm.getPackageInfo(
						applicationInfo.packageName,
						PackageManager.GET_PERMISSIONS);
				if (!isSystemPackage(packageInfo)) {
					if (!packageInfo.packageName.equalsIgnoreCase("com.unfoldlabs.redgreen")) {
						CleanAppDB db = new CleanAppDB(true);
						if(null == AppData.getInstance().getAppInstalledTime()){
							db.setDateTime(getDefaultDate());
						}
						db.setDateTime(AppData.getInstance().getAppInstalledTime());
						db.setPackageName(packageInfo.packageName);
						list.add(db);
					}

				}
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}
		}
		AppData.getInstance().setAppManagerInstalledAppsList(list);
		return list;
	}

	public static void  getAllPackageList(Context ctx,final DatabaseHandler database) {

		map = new HashMap<String,String>();
		final PackageManager pm = ctx.getPackageManager();
		Method getPackageSizeInfo = null;
		final List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
		for (ApplicationInfo applicationInfo : packages) {
			try {
				final PackageInfo packageInfo = pm.getPackageInfo(
						applicationInfo.packageName, PackageManager.GET_PERMISSIONS);
				if (!isSystemPackage(packageInfo)) {
					if (!packageInfo.packageName.equalsIgnoreCase("com.unfoldlabs.redgreen")) {

						appCount = appCount+1;
						try {
							getPackageSizeInfo = pm.getClass().getMethod("getPackageSizeInfo",String.class, IPackageStatsObserver.class);

							getPackageSizeInfo.invoke(pm, packageInfo.packageName,
									new IPackageStatsObserver.Stub() {

										@Override
										public void onGetStatsCompleted(PackageStats pStats, boolean succeeded)
												throws RemoteException {
											long size = pStats.codeSize;
											addPackageToMap(pm,packages, packageInfo.packageName, String.valueOf(size), database);
										}


									});
						} catch (Exception e) {
							e.printStackTrace(System.err);
						}

					}
				}
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	private static void addPackageToMap(PackageManager pm ,List<ApplicationInfo> packages,String packageName, String valueOf,DatabaseHandler database) {
		int sum = 0;
		count = count+1;
		map.put(packageName, valueOf);

		if(appCount == count){
			for (ApplicationInfo applicationInfo : packages) {
				try {
					final PackageInfo packageInfo = pm.getPackageInfo(
							applicationInfo.packageName, PackageManager.GET_PERMISSIONS);
					if (!isSystemPackage(packageInfo)) {
						if (!packageInfo.packageName.equalsIgnoreCase("com.unfoldlabs.redgreen")) {

							final CleanAppDB db = new CleanAppDB(isSelected);
							db.setDateTime(AppData.getInstance().getAppInstalledTime());
							db.setPackageName(packageInfo.packageName);
							db.setApp_size("" + map.get(packageInfo.packageName));

							String summ = map.get(packageInfo.packageName);
							sum += Integer.parseInt(summ);

							database.insertCleanApp(db, false);
							ApplicationData.getInstance().setAppInstalledName("" +packageInfo.packageName);
						}
					}
				} catch (NameNotFoundException e) {
					e.printStackTrace();
				}
			}
			sumFinal = sum;
			ApplicationData.getInstance().setAppInstalledDate(AppData.getInstance().getAppInstalledTime());
			ApplicationData.getInstance().setAppInstalledEndTime("" + Utility.getDefaultDate());


			if(map != null){
				map.clear();
				map =null;
			}
		}
	}
	/**
	 * Return whether the given PackgeInfo represents a system package or not.
	 * User-installed packages (Market or otherwise) should not be denoted as
	 * system packages.
	 *
	 * @param pkgInfo
	 * @return
	 */
	public static boolean isSystemPackage(PackageInfo pkgInfo) {
		return ((pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) ? true
				: false;
	}

	/**
	 * Dialog for AutoClean Selection
	 */
	@SuppressWarnings("deprecation")
	public static void generateNotification(Context context){
		String textMessage = "Speed Booster Running in Automatic Mode.";
		Intent intent1 = new Intent(context, HomeActivity.class);
		intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
		PendingIntent resultPendingIntent =	PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_CANCEL_CURRENT);
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
		Notification notification = mBuilder.setSmallIcon(R.drawable.app_icon).setTicker("Speed Booster Running").setWhen(0)
				.setAutoCancel(true)
				.setContentTitle("RedGreen")
				.setStyle(new NotificationCompat.BigTextStyle().bigText(textMessage))
				.setContentIntent(resultPendingIntent)
				.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
				/*.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.app_icon))*/
				.setContentText(textMessage).build();
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(Constants.mNotificationId, notification);

		AppData.getInstance().setFromNoifiction(true);
		new RunBackGround(context, notificationManager);
	}

	public static void generateCleanAppNotification(Context context, ArrayList<CleanAppDB> list){

		mManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
		Intent intent1 = new Intent(context,HomeActivity.class);
		Notification notification = new Notification(R.drawable.app_icon,"App Manager Running", System.currentTimeMillis());
		intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP| Intent.FLAG_ACTIVITY_CLEAR_TOP);
		PendingIntent pendingNotificationIntent = PendingIntent.getActivity(context,0, intent1,PendingIntent.FLAG_UPDATE_CURRENT);
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		notification.setLatestEventInfo(context, "RedGreen", "App Manager in Automatic Mode", pendingNotificationIntent);
		mManager.notify(0, notification);

		// clean App baground service
		Intent i = new Intent(context,CleanAppBackground.class);
		Bundle bundleObject = new Bundle();
		bundleObject.putSerializable("list", list);
		i.putExtras(bundleObject);
		i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(i);
	}

	public static void showAlertDialogNetworkConnection(Context context, String message, String buttonMessage) {
		final Dialog dialog = new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		dialog.setContentView(R.layout.forget_email_sent_dailog);
		dialog.setCanceledOnTouchOutside(false);
		TextView sentEmail = (TextView) dialog.findViewById(R.id.sentEmail);
		TextView ok = (TextView) dialog.findViewById(R.id.ok);
		ok.setText(buttonMessage);
		sentEmail.setText(message);
		dialog.show();
		ok.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
	}
	public static void showAlertDialog(Context context) {
		try {
			SharedPreferences forgotEmailSharedPref = context.getSharedPreferences("APPLOCK_FORGET_PASSWORD", Context.MODE_PRIVATE);
			String forgot_email = forgotEmailSharedPref.getString(context.getResources()
					.getString(R.string.forgot_email_done), "");
			final Dialog dialog = new Dialog(context);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
			dialog.setContentView(R.layout.forget_email_sent_dailog);
			dialog.setCanceledOnTouchOutside(false);
			TextView sentEmail = (TextView) dialog.findViewById(R.id.sentEmail);
			TextView ok = (TextView) dialog.findViewById(R.id.ok);
			String userName = forgot_email.split("@")[0];
			String domailName = forgot_email.split("@")[1];
			String domainEnd = domailName.split(".c")[0];
			int n = domainEnd.length();
			char last = domainEnd.charAt(n-1);

			//if (forgot_email.length() > 10) {
			userName = userName.substring(0, 1) + "..." + "@";
			domailName = domailName.substring(0, 1) + "...";
			//} else {
			//	userName = forgot_email;
			//}
			sentEmail.setText("Your password has been sent to" + " " + userName + domailName + last + ".com");
			dialog.show();
			ok.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();

				}
			});
		}catch (ArrayIndexOutOfBoundsException e){
			e.printStackTrace();
		}
	}
	public static void postMethod(String userEmailID, String password, Context context) {
		String android_id = Settings.Secure.getString(context
				.getContentResolver(), Settings.Secure.ANDROID_ID);
		if (Utility.isNetworkAvailable(context)) {
			new AppLockInfoAsynTask(android_id, userEmailID, password).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
			Utility.showAlertDialog(context);
		} else {
			Utility.showAlertDialogNetworkConnection(context, "No Network Connection", "OK");
		}
	}

	public static class AppLockInfoAsynTask extends AsyncTask<String, Void, String> {
		private String password;
		private String userEmailID;
		private String deviceID;
		private int responseCode;
		Context context;

		public AppLockInfoAsynTask(String android_id, String userEmailID,
								   String password) {
			this.password = password;
			this.userEmailID = userEmailID;
			deviceID = android_id;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... params) {
			// process the Search parameter string
			// try to fetch the data
			try {
				URL requestUrl = new URL(Constants.POSTURL);
				HttpURLConnection connection = (HttpURLConnection) requestUrl
						.openConnection();
				connection.setDoOutput(true);
				connection.setDoInput(true);
				connection.setRequestProperty("Content-Type",
						"application/json");
				connection.setRequestProperty("Accept", "application/json");
				connection.setRequestMethod("POST");

				JSONObject data = new JSONObject();
				Applog.logString("App LockuserEmailID HTTP POST Response : "
						+ userEmailID);
				Applog.logString("App Lock userEmailID HTTP POST Response : "
						+ password);
				Applog.logString("App Lock userEmailID HTTP POST Response : "
						+ deviceID);

				data.put("email", userEmailID);
				data.put("password", password);
				data.put("deviceId", deviceID);
				// sending data & specifying the encoding utf-8
				OutputStream os = connection.getOutputStream();
				os.write(data.toString().getBytes("UTF-8"));
				os.close();

				// display what returns the POST request
				StringBuilder sb = new StringBuilder();
				responseCode = connection.getResponseCode();
				if (responseCode == HttpURLConnection.HTTP_OK) {
					BufferedReader br = new BufferedReader(
							new InputStreamReader(
									connection.getInputStream(), "utf-8"));
					String line = null;
					while ((line = br.readLine()) != null) {
						sb.append(line + "\n");
					}
					br.close();
					Applog.logString("App Lock HTTP POST Response 1 : " + sb.toString());
				} else {
					Applog.logString("App Lock HTTP POST Response Message 2 : "
							+ connection.getResponseMessage());
				}
				Applog.logString("App Lock HTTP Response Code: 3 " + responseCode);
			} catch (MalformedURLException e) {
				Applog.logString("App Lock Error processing URL 4"+ e);
			} catch (IOException e) {
				Applog.logString("App Lock Error connecting to Host 5"+ e);
			} catch (JSONException e) {
				Applog.logString("App Lock Error handling JSON Object 6"+ e);
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (responseCode == 200) {
			}
		}
	}
	/** copy file */
	public static void copyFile(InputStream input, OutputStream output)
			throws IOException {
		byte[] buffer = new byte[1024];
		int length;

		while ((length = input.read(buffer)) > 0)
			output.write(buffer, 0, length);
	}

	/** save preference value */
	public static void saveValue(String key, int value, Context context) {
		SharedPreferences prefManager = PreferenceManager
				.getDefaultSharedPreferences(context);
		SharedPreferences.Editor edit = prefManager.edit();
		edit.putInt(key, value);
		edit.commit();
	}


	/**
	 * cleaning cache details in db functionality
	 *
	 * @param cacheSizemain
	 * @param ramCleaned
	 */
	public static void cleanCacheDB(int cacheSizemain, int ramCleaned) {
		try{
			Calendar c = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a", Locale.getDefault());
			String strDate = sdf.format(c.getTime());
			if (SavingDBAdapter.getInstance().isOpen())
				if (SavingDBAdapter.getInstance().getCache() != null){
					SavingDBAdapter.getInstance().addCache(cacheSizemain, strDate,
							ramCleaned);
				}
			if(SavingDBAdapter.getInstance().getCache() != null)
				SavingDBAdapter.getInstance().getCache().close();
		}catch(NullPointerException e){
			e.printStackTrace();
		}
	}

	/**
	 * TO calculate the density pixel for Animations
	 * @param resources
	 * @param dp
	 * @return
	 */
	public static float dp2px(Resources resources, float dp) {
		final float scale = resources.getDisplayMetrics().density;
		return dp * scale + 0.5f;
	}

	/**
	 * TO calculate the scale pixel for Animations
	 * @param resources
	 * @param sp
	 * @return
	 */

	public static float sp2px(Resources resources, float sp) {
		final float scale = resources.getDisplayMetrics().scaledDensity;
		return sp * scale;
	}
}
