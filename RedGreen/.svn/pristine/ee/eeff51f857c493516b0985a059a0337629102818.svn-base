package com.unfoldlabs.redgreen.receivers;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.unfoldlabs.redgreen.R;
import com.unfoldlabs.redgreen.utilty.Utility;

public class MondayReceiver extends BroadcastReceiver{
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
				boolean flagCleaner = sharedPreferences.getBoolean("flagCleanerMonday",
						false);
				if (!flagCleaner) {
					flagCleaner = !flagCleaner;
					editor.putBoolean("flagCleanerMonday", flagCleaner);
					editor.commit();
				} else {
					flagCleaner = !flagCleaner;
					editor.putBoolean("flagCleanerMonday", flagCleaner);
					editor.commit();
					Utility.generateNotification(context);
				}
			} else if (repeat == 2) {
				sharedPreferences.getInt("SkipIdCleanerMonday", 0);
				if (sharedPreferences.getInt("SkipIdCleanerMonday", 0) <= 2) {
				    int	SkipIdCleaner = sharedPreferences.getInt("SkipIdCleanerMonday", 0)+1;
					editor.putInt("SkipIdCleanerMonday", SkipIdCleaner);
					editor.commit();
				} else {
					editor.putInt("SkipIdCleanerMonday", 0);
					editor.commit();
					Utility.generateNotification(context);
				}
			}

		}
	}

}
