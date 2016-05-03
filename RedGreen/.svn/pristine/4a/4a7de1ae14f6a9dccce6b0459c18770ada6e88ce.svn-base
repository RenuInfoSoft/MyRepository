package com.unfoldlabs.redgreen.receiver;

import com.unfoldlabs.redgreen.service.RedGreenService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class CleanReciever extends BroadcastReceiver{
	private Context context;


	@Override
	public void onReceive(Context context, Intent intent) {
		this.context = context;
		sendIntenttoServiece();
	}

	private void sendIntenttoServiece() {
		//no matter how many times start service, it will only execure onStartCommand method only if service already running.
		Intent intent = new Intent(context, RedGreenService.class);
		intent.putExtra("sendclean", "sendclean");
		context.startService(intent);

	}
}