package com.unfoldlabs.redgreen.gcm;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.unfoldlabs.redgreen.R;
import com.unfoldlabs.redgreen.activity.HomeActivity;

public class GCMNotificationIntentService extends IntentService {

	public static final int NOTIFICATION_ID = 1;
	private NotificationManager mNotificationManager;
	public NotificationCompat.Builder builder;
	public static final String TAG = "GCMNotificationIntentService";
	public static String FROM_REG_ID = "";
	
/**
 * Constructor for GCMNotificationIntentService
 */
	public GCMNotificationIntentService() {
		super("GCMNotificationIntentService");
	}

/**
 * Get the Message from gcm through intent service
 */

	@Override
	protected void onHandleIntent(Intent intent) {
		Bundle extras = intent.getExtras();
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

		String messageType = gcm.getMessageType(intent);

		if (!extras.isEmpty()) {
			if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR
					.equals(messageType)) {
				sendNotification("Send error: " + extras.toString());
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED
					.equals(messageType)) {
				sendNotification("Deleted messages on server: "
						+ extras.toString());
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE
					.equals(messageType)) {

				sendNotification(""+extras.get("SERVER_MESSAGE"));

			}
		}
		GcmBroadcastReceiver.completeWakefulIntent(intent);
	}
/**
 * Method to send the Notification
 * @param msg
 */
	private void sendNotification(String msg) {
		mNotificationManager = (NotificationManager) this
				.getSystemService(Context.NOTIFICATION_SERVICE);

		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				new Intent(this, HomeActivity.class), 0);
		if(null != msg){
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				this).setSmallIcon(R.drawable.redgreen_small)
				.setContentTitle("RedGreen").setContentText(msg);

		mBuilder.setContentIntent(contentIntent);
		mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
		}
	}

}
