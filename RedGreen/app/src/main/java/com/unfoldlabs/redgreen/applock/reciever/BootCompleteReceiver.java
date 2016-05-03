package com.unfoldlabs.redgreen.applock.reciever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.unfoldlabs.redgreen.service.RedGreenService;
import com.unfoldlabs.redgreen.utilty.Bean;

public class BootCompleteReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Intent service = new Intent(context, RedGreenService.class);
		context.startService(service);
		Bean.setAlarm(context);
	}

}
