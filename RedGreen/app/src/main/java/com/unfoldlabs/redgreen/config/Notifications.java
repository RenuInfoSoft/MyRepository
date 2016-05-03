package com.unfoldlabs.redgreen.config;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.unfoldlabs.redgreen.service.ServiceImpl;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Notifications {

	public static final String REG_ID = "regId";
	private static final String APP_VERSION = "appVersion";
	private GoogleCloudMessaging gcm;
	private Context context;
	private String regId;
	private TelephonyManager telephonyManager;
	private Integer notificationStatus;

	public Notifications(Context ctx, int id) {
		this.context = ctx;
		this.notificationStatus = id;
		initialization();
	}

	private void initialization() {
		telephonyManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		new AWSServiceConfig(context.getAssets());
		setNotification(notificationStatus);
	}

	private void setNotification(Integer notificationStatus) {
		regId = getRegistrationId(context);
		if (TextUtils.isEmpty(regId)) {
			regId = registerGCM();
		}
		Map<String, Object> registerMap = new HashMap<String, Object>();
		registerMap.put("emailId", "email");
		registerMap.put("mobileNo", "123456789");
		registerMap.put("imeiNo", telephonyManager.getDeviceId());
		registerMap.put("address", "address");
		new MyAsyncTask(registerMap, notificationStatus).execute();

	}

	private String registerGCM() {
		gcm = GoogleCloudMessaging.getInstance(context);
		regId = getRegistrationId(context);
		if (TextUtils.isEmpty(regId)) {
			registerInBackground();
		} else {
		}
		return regId;
	}

	/**
	 * getting registration id of the devoice
	 * @param context
	 * @return
	 */
	private String getRegistrationId(Context context) {
		final SharedPreferences prefs = context.getSharedPreferences(
				Notifications.class.getSimpleName(),
				Context.MODE_PRIVATE);
		String registrationId = prefs.getString(REG_ID, "");
		if (registrationId != null && registrationId != "") {
			return "";
		}
		int registeredVersion = prefs.getInt(APP_VERSION, Integer.MIN_VALUE);
		int currentVersion = getAppVersion(context);
		if (registeredVersion != currentVersion) {
			return "";
		}
		return registrationId;
	}

	private void registerInBackground() {
		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
				String msg = "";
				try {
					if (gcm == null) {
						gcm = GoogleCloudMessaging.getInstance(context);
					}
					regId = gcm.register(AWSConstants.PROJECT_ID);
					msg = "Device registered, registration ID=" + regId;
					storeRegistrationId(context, regId);
				} catch (IOException ex) {
					msg = "Error :" + ex.getMessage();
				}
				return msg;
			}
			@Override
			protected void onPostExecute(String msg) {

			}
		}.execute(null, null, null);
	}

	public class MyAsyncTask extends AsyncTask<Void, Void, Void> {

		private Map<String, Object> registerMap;
		private Integer notificationStatus;

		public MyAsyncTask(Map<String, Object> registerMap,
				Integer notificationStatus) {
			this.registerMap = registerMap;
			this.notificationStatus = notificationStatus;
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {
				JSONObject jsonObj = new ServiceImpl()
						.registerUserId(registerMap);
				if (jsonObj != null) {
					Integer userId = (Integer) jsonObj.get("userId");

					if (userId != null && userId != 0) {
						storeRegId(regId, userId, notificationStatus);
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

	}

	/**
	 * storing user id, device reg id, notification status and imei number of
	 * device
	 * 
	 * @param registerId
	 * @param userId
	 * @param notificationStatus
	 */
	@SuppressWarnings("unused")
	private void storeRegId(String registerId, Integer userId,
			Integer notificationStatus) {
		Map<String, Object> registerMap = new HashMap<String, Object>();
		registerMap.put("userId", userId);
		registerMap.put("deviceRegId", registerId);
		registerMap.put("notificationStatus", notificationStatus);
		registerMap.put("imeiNo", telephonyManager.getDeviceId());
		JSONObject jsonObj = new ServiceImpl().registerDeviceId(registerMap,
				notificationStatus);

	}

	/**
	 * storing the registration id of device
	 * 
	 * @param context
	 * @param regId
	 */
	private void storeRegistrationId(Context context, String regId) {
		final SharedPreferences prefs = context.getSharedPreferences(
				Notifications.class.getSimpleName(),
				Context.MODE_PRIVATE);
		int appVersion = getAppVersion(context);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(REG_ID, regId);
		editor.putInt(APP_VERSION, appVersion);
		editor.commit();
	}

	/**
	 * To Get App Version
	 */
	private static int getAppVersion(Context context) {
		try {
			PackageInfo packageInfo = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {

			throw new RuntimeException(e);
		}
	}

}
