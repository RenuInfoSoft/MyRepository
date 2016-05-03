package com.unfoldlabs.redgreen.receivers;

import java.util.Date;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;


import com.unfoldlabs.redgreen.R;
import com.unfoldlabs.redgreen.utilty.Utility;

public class WednsdayReceiver extends BroadcastReceiver{
	private SharedPreferences sharedPreferences;
	private SharedPreferences.Editor editor;
	@Override
	public void onReceive(Context context, Intent intent) {

		sharedPreferences = context.getSharedPreferences("MY_SHARED_PREF", Context.MODE_PRIVATE);
		editor = sharedPreferences.edit();
		int repeat = sharedPreferences.getInt(
				context.getString(R.string.dialog_txt_REPEAT), 0);
		boolean autoCleanMemorySwitch = sharedPreferences.getBoolean(
				context.getResources().getString(R.string.cleanmemoryswitch), true);



		if (autoCleanMemorySwitch) {
			if (repeat == 0) {
				Utility.generateNotification(context);
			} else if (repeat == 1) {
				boolean flagCleaner = sharedPreferences.getBoolean("flagCleanerWednesday",
						false);
				if (!flagCleaner) {
					flagCleaner = !flagCleaner;
					editor.putBoolean("flagCleanerWednesday", flagCleaner);
					editor.commit();
				} else {
					flagCleaner = !flagCleaner;
					editor.putBoolean("flagCleanerWednesday", flagCleaner);
					editor.commit();
					Utility.generateNotification(context);
				}
			} else if (repeat == 2) {
				sharedPreferences.getInt("SkipIdCleanerWednesday", 0);
				if (sharedPreferences.getInt("SkipIdCleanerWednesday", 0) <= 2) {
					int SkipIdCleaner = sharedPreferences.getInt("SkipIdCleanerWednesday", 0)+1;
					editor.putInt("SkipIdCleanerWednesday", SkipIdCleaner);
					editor.commit();
				} else {
					editor.putInt("SkipIdCleanerWednesday", 0);
					editor.commit();
					Utility.generateNotification(context);
				}
			}

		}
	}

}
