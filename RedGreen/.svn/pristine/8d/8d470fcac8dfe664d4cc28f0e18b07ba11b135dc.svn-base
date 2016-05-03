package com.unfoldlabs.redgreen.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;


import com.unfoldlabs.redgreen.R;
import com.unfoldlabs.redgreen.utilty.Utility;

public class SaturdayReceiver extends BroadcastReceiver{

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
				boolean flagCleaner = sharedPreferences.getBoolean("flagCleanerSaturday",
						false);
				if (!flagCleaner) {
					flagCleaner = !flagCleaner;
					editor.putBoolean("flagCleanerSaturday", flagCleaner);
					editor.commit();
				} else {
					flagCleaner = !flagCleaner;
					editor.putBoolean("flagCleanerSaturday", flagCleaner);
					editor.commit();
					Utility.generateNotification(context);
				}
			} else if (repeat == 2) {
				sharedPreferences.getInt("SkipIdCleanerSaturday", 0);
				if (sharedPreferences.getInt("SkipIdCleanerSaturday",0) <= 2) {
					int	SkipIdCleaner = sharedPreferences.getInt("SkipIdCleanerSaturday",0)+1;
					editor.putInt("SkipIdCleanerSaturday", SkipIdCleaner);
					editor.commit();
				} else {
					editor.putInt("SkipIdCleanerSaturday", 0);
					editor.commit();
					Utility.generateNotification(context);
				}
			}

		}

	}

}
