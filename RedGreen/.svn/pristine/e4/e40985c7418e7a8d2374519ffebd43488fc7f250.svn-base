package com.unfoldlabs.redgreen.applock.reciever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.unfoldlabs.redgreen.service.RedGreenService;

public class AppLockAlarmManagerReciever extends BroadcastReceiver {

	private Context context;
	@Override
	public void onReceive(final Context context, Intent intent) {
		this.context = context;
		sendIntenttoServiece();
	}

	private void sendIntenttoServiece() {
		//no matter how many times start service, it will only execure onStartCommand method only if service already running.
		Intent intent = new Intent(context, RedGreenService.class);
		intent.putExtra("applock", "alarm");
		context.startService(intent);
	}


}
