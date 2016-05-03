package com.unfoldlabs.redgreen.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.InflateException;
import android.view.MenuItem;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.MapBuilder;
import com.unfoldlabs.redgreen.R;

public class CleanerActivity extends BaseActivity {
	private boolean windowState = true;
	private int dialogueCount = 0;
	private SharedPreferences sp;
	private SharedPreferences.Editor editor;
	public EasyTracker easyTracker;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			getLayoutInflater().inflate(R.layout.cleaner_activity, frameLayout);
		}catch (InflateException e){
			e.printStackTrace();
		}
		registerBaseActivityReceiver();
		getActionBar().setDisplayHomeAsUpEnabled(true);
		easyTracker = EasyTracker.getInstance(getApplicationContext());
		easyTracker.send(MapBuilder.createEvent(getResources().getString(R.string.speed_booster),
				getResources().getString(R.string.analytics_speedbooster_screen_opened), "track event", null).build());
		sp = getSharedPreferences(getResources().getString(R.string.cleanmemory_pref), Context.MODE_PRIVATE);
		editor = sp.edit();
		dialogueCount = sp.getInt(getResources().getString(R.string.cleanmem_count), dialogueCount);
		dialogueCount += 1;
		editor.putInt(getResources().getString(R.string.cleanmem_count), dialogueCount);
		editor.commit();
		if (sp.getInt(getResources().getString(R.string.cleanmem_count), dialogueCount) == 5
				&& sp.getBoolean(getResources().getString(R.string.cleanmem_state), false) == false)
		{
			cleanMemorySettingsDialog(CleanerActivity.this, windowState);
		}

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_auto_speed_setting:
				easyTracker.send(MapBuilder.createEvent(getResources().getString(R.string.speed_booster_setting_dialog),
						getResources().getString(R.string.speed_booster_setting_dialogr_dialog_button), "track event", null)
						.build());
				cleanMemorySettingsDialog(CleanerActivity.this, windowState);
				break;

			case R.id.menu_auto_speed_memory:
				easyTracker.send(MapBuilder.createEvent(getResources().getString(R.string.speed_booster_by_memory_dialog),
						getResources().getString(R.string.speed_booster_by_memory_dialogr_dialog_button), "track event",
						null).build());
				cleanMemoryByMemoryDialog(CleanerActivity.this, windowState);
				break;
			default:
				break;
		}
		return super.onOptionsItemSelected(item);

	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unRegisterBaseActivityReceiver();
		unbindDrawables(findViewById(R.id.Cleaner_layout));
		System.gc();
		easyTracker.send(MapBuilder.createEvent(getString(R.string.speed_booster),
				getString(R.string.analytics_speedbooster_exit_time),
				"track event", null).build());
	}

}
