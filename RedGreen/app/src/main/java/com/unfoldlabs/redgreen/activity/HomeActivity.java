package com.unfoldlabs.redgreen.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.analytics.tracking.android.MapBuilder;
import com.unfoldlabs.redgreen.R;
import com.unfoldlabs.redgreen.applock.lock.ShowApplockViewOtherApps;
import com.unfoldlabs.redgreen.applock.ui.SetApplockPin;
import com.unfoldlabs.redgreen.applock.ui.SetEmailActivity;
import com.unfoldlabs.redgreen.applock.util.Util;
import com.unfoldlabs.redgreen.global.AppData;
import com.unfoldlabs.redgreen.global.ApplicationData;
import com.unfoldlabs.redgreen.global.BrowserAsyncTask;
import com.unfoldlabs.redgreen.global.GlobalAsyncTask;
import com.unfoldlabs.redgreen.global.JunkAsyncTask;
import com.unfoldlabs.redgreen.interfaces.Constants;
import com.unfoldlabs.redgreen.model.SaveBatteryModel;
import com.unfoldlabs.redgreen.service.RedGreenService;
import com.unfoldlabs.redgreen.utilty.Utility;
import com.unfoldlabs.redgreen.views.DonutProgress;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class HomeActivity extends BaseActivity {
	private RelativeLayout applock;
	private RelativeLayout dataSecure;
	private RelativeLayout saveBattery;
	private RelativeLayout clearApps;
	private DonutProgress donutProgress;
	private Timer timerDonut;
	private String value;
	private boolean flag;
	private SharedPreferences sharedpreferences;
	private View listviewLayout;
	private List<SaveBatteryModel> memoryBoostList = new ArrayList<SaveBatteryModel>();
	private double cpuTemp;
	private Intent cleanMemory;
	private Intent intent;
	private Intent cleanerIntent;
	private Intent batterySaveIntent;
	private Intent dataSecureIntent;
	private Intent cleanAppIntent;
	private boolean fCheck = false;
	private SharedPreferences  batterySettingSharedPref, cpuCoolerTempSharedPref;
	private Editor cpuCoolerTempEditor;
	private TimerTask mTimerTask;
	private double tempInFar, tempFar;
	private Dialog dialog;
	private String MyPREFERENCES = "REDPrefs" ;
	private String lockFrom = null;
	private int selected;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//when the device restarts, service stopping
		if(!Util.isRedGreenServiceRunning(RedGreenService.class, this)){
			Intent intentService = new Intent(getBaseContext(),RedGreenService.class);
			startService(intentService);
		}else{

		}

		registerBaseActivityReceiver();
		getLayoutInflater().inflate(R.layout.home_lyt, frameLayout);
		getActionBar().setDisplayShowTitleEnabled(false);
		getActionBar().setIcon(R.drawable.actionbar_logo);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		batterySettingSharedPref = getSharedPreferences(
				"BATTERY_SETTINGS_SHARED_PREF", Context.MODE_PRIVATE);
		cpuCoolerTempSharedPref = getSharedPreferences(
				"CPU_COOLER_TEMP_SHARED_PREF", Context.MODE_PRIVATE);
		cpuCoolerTempEditor = cpuCoolerTempSharedPref.edit();

		listviewLayout = findViewById(R.id.container_header_lyt);
		listviewLayout.setVisibility(View.GONE);
		listviewLayout.findViewById(R.id.clean_memory)
				.setOnClickListener(btnClick);
		listviewLayout.findViewById(R.id.cleanapp_image)
				.setOnClickListener(btnClick);
		listviewLayout.findViewById(R.id.applock_image)
				.setOnClickListener(btnClick);
		listviewLayout.findViewById(R.id.savebattery_image)
				.setOnClickListener(btnClick);
		listviewLayout.findViewById(R.id.datasecurity_image)
				.setOnClickListener(btnClick);
		clearApps = (RelativeLayout) findViewById(R.id.cleanapp_image);
		applock = (RelativeLayout) findViewById(R.id.applock_image);
		dataSecure = (RelativeLayout) findViewById(R.id.savebattery_image);
		saveBattery = (RelativeLayout) findViewById(R.id.datasecurity_image);
		donutProgress = (DonutProgress) findViewById(R.id.donut_progress);
		clearApps.setOnClickListener(btnClick);
		applock.setOnClickListener(btnClick);
		saveBattery.setOnClickListener(btnClick);
		dataSecure.setOnClickListener(btnClick);
		donutProgress.setOnClickListener(btnClick);
		getCpuTemparature();
		donutProgress.setProgress(0);
		InternalMemory();
	}

	/**
	 * ROM animation functionality
	 *
	 * @param 	 */
	private void donutProgres(final Double resultFinal) {
		timerDonut = new Timer();
		timerDonut.schedule(new TimerTask() {
			@Override
			public void run() {
				if (null != getApplicationContext())
					runOnUiThread(new Runnable(){
						@Override
						public void run() {
							Double result = 0.0;
							if(resultFinal >= 95){
								result = 95.0;
							}else{
								result = resultFinal;
							}
							if (donutProgress.getProgress() == result || donutProgress.getProgress() <= result)
								donutProgress.setProgress(donutProgress
										.getProgress() + 1);
							if (donutProgress.getProgress() == result) {
								donutProgress.setProgress(donutProgress
										.getProgress() + 1);
								if (donutProgress.getProgress() == result ) {
									donutProgress.setProgress(donutProgress
											.getProgress() + 1);
									try{
										if (timerDonut != null){
											timerDonut.cancel();
											timerDonut.purge();
											timerDonut = null;
										}
									}catch(NullPointerException e){
										e.printStackTrace();
									}
								}
								try{
									if (timerDonut != null){
										timerDonut.cancel();
										timerDonut.purge();
										timerDonut = null;
									}
								}catch(NullPointerException e){
									e.printStackTrace();
								}
								if(donutProgress.getProgress() > result)
									donutROMProgressBack(result);
							}
						}
					});
			}
		}, 1000, 5);
	}
	/**
	 * ROM BACK animation functionality
	 *
	 * @param result
	 */

	private void donutROMProgressBack(final Double result) {
		timerDonut = new Timer();
		timerDonut.schedule(new TimerTask() {
			@Override
			public void run() {

				if (null != getApplicationContext())
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							if (donutProgress.getProgress() > 0) {
								donutProgress.setProgress(donutProgress.getProgress() - 1);
								if (donutProgress.getProgress() == result - 1) {
									donutProgress.setProgress(donutProgress.getProgress() + 1);
									if (donutProgress.getProgress() == result - 1) {
										donutProgress.setProgress(donutProgress.getProgress() - 1);
										try {
											if (timerDonut != null) {
												timerDonut.cancel();
												timerDonut.purge();
												timerDonut = null;
											}
										} catch (NullPointerException e) {
											e.printStackTrace();
										}
									}
									try {
										if (timerDonut != null) {
											timerDonut.cancel();
											timerDonut.purge();
											timerDonut = null;
										}
									} catch (NullPointerException e) {
										e.printStackTrace();
									}
								}

							}

						}
					});
			}
		}, 1000, 100);
	}

	@Override
	protected void onResume() {
		super.onResume();
		Intent intentBundle = getIntent();
		lockFrom = intentBundle.getStringExtra("LOCK_FROM");
		flag = cpuCoolerTempSharedPref.getBoolean(getResources()
				.getString(R.string.cpuCoolerTemp), true);
		if (AppData.getInstance().isFromSplash() ||(null != AppData.getInstance().getList()
				&& AppData.getInstance().getList().isEmpty())) {
			new GlobalAsyncTask(getApplicationContext())
					.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		}
		if (AppData.getInstance().isFromSplash() || (null != AppData.getInstance().getBrowserHistory()
				&& AppData.getInstance().getBrowserHistory().isEmpty())) {
			new BrowserAsyncTask(getApplicationContext())
					.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		}
		if (AppData.getInstance().isFromSplash() ||(null != AppData.getInstance().getJunk()
				&& AppData.getInstance().getJunk().isEmpty()) && (!AppData.getInstance().isTaskRunning())) {
			new JunkAsyncTask(getApplicationContext())
					.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		}
	}

	/**
	 * To show corresponding options for this activity
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.findItem(R.id.action_sort).setVisible(false);
		menu.findItem(R.id.menu_settings).setVisible(false);
		return true;
	}

	/**
	 * Selected module will be open on click
	 */
	private OnClickListener btnClick = new OnClickListener() {

		@SuppressLint("InlinedApi")
		@Override
		public void onClick(View v) {

			switch (v.getId()) {
				/**
				 * to navigate to Speed Booster module from home screen
				 * functionality
				 */
				case R.id.clean_memory:
					cleanMemory = new Intent(getApplicationContext(),
							CleanerActivity.class);
					startActivity(cleanMemory);

					selected = + selected;
					ApplicationData.getInstance().setSpeedBoosterTimeSelected(""+selected);

					easyTracker.send(MapBuilder.createEvent(
							getResources()
									.getString(R.string.clean_memory_activity),
							getResources().getString(
									R.string.clean_memory_activity_navigate),
							"track event", null).build());

					break;
				/**
				 * in home screen Donut(animation) functionality
				 */
				case R.id.donut_progress:
					cleanerIntent = new Intent(getApplicationContext(),
							CleanerActivity.class);

					selected = + selected;
					ApplicationData.getInstance().setSpeedBoosterTimeSelected(""+selected);
					easyTracker.send(MapBuilder.createEvent(
							getResources()
									.getString(R.string.clean_memory_activity),
							getResources().getString(
									R.string.clean_memory_activity_navigate),
							"track event", null).build());

					startActivity(cleanerIntent);
					break;
				/**
				 * To navigate to AppLock screen from home screen
				 */
				case R.id.applock_image:
					AppData.getInstance().setIsHomePackage(true);
					AppData.getInstance().setIsFromHome(true);
					if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
						appLockActivity();
					} else {
						// Check if permission enabled
						if (Util.getUsageStatsList(HomeActivity.this)
								.isEmpty()) {
							applockDialog();
							easyTracker.send(MapBuilder.createEvent(getString(R.string.home_screen),
									getString(R.string.home_screen_applock_usagestates_dialogue_displad),
									"track event", null).build());
						} else {
							appLockActivity();
						}
					}
					easyTracker.send(MapBuilder.createEvent(
							getResources().getString(R.string.app_lock_screen),
							getResources().getString(
									R.string.app_lock_screen_navigate),
							"track event", null).build());

					break;
				/**
				 * To navigate to Save Battery module from home screen functionality
				 */
				case R.id.savebattery_image:
					easyTracker.send(MapBuilder.createEvent(
							getResources()
									.getString(R.string.save_battery_activity),
							getResources().getString(
									R.string.save_battery_activity_navigate),
							"track event", null).build());
					flag = cpuCoolerTempSharedPref.getBoolean(getResources()
							.getString(R.string.cpuCoolerTemp), true);

					batterySaveIntent = new Intent(getApplicationContext(),
							SaveBatteryActivity.class);
					fCheck = batterySettingSharedPref.getBoolean(getResources()
							.getString(R.string.batterysetting), false);

					if (null != memoryBoostList && memoryBoostList.isEmpty()) {

						if (null != AppData.getInstance().getSaveBatteryCpulist()) {
							memoryBoostList = AppData.getInstance().getSaveBatteryCpulist();
						}
					}
					batterySaveIntent.putExtra("cpuTemp", cpuTemp + (memoryBoostList.size()*0.1));
					double ss =  cpuTemp + (memoryBoostList.size()*0.1);
					batterySaveIntent.putExtra("cpuTemp", ss);
					tempFar = cpuTemp + (memoryBoostList.size()*0.1);
					tempInFar = (tempFar * 1.8)+32;
					batterySaveIntent.putExtra("cpuTempFar", tempInFar);

					batterySaveIntent.putExtra("cpuTempFar", tempInFar);
					double dd = (cpuTemp*1.8)+32;

					double reduced_temp_inFar = tempInFar-dd;

					double cpu_reduced_temp_celcius = memoryBoostList.size() * 0.1;
					if (flag) {

						batterySaveIntent.putExtra("cpuTemp", cpuTemp + (memoryBoostList.size()*0.1));
						batterySaveIntent.putExtra("cpuReducedTempCel", cpu_reduced_temp_celcius);
					} else {
						batterySaveIntent.putExtra("cpuTemp", cpuTemp);
					}
					tempFar = cpuTemp + (memoryBoostList.size()*0.1);
					if (flag) {
						tempInFar = (tempFar * 1.8)+32;
						batterySaveIntent.putExtra("cpuTempFar", tempInFar);
						batterySaveIntent.putExtra("cpuReducedTempFar", reduced_temp_inFar);
					} else {
						tempInFar = (cpuTemp*1.8)+32;
						batterySaveIntent.putExtra("cpuTempFar", tempInFar);
					}

					startActivity(batterySaveIntent);

					break;
				/**
				 * To navigate to Speed Booster module from home screen
				 * functionality
				 */
				case R.id.datasecurity_image:
					dataSecureIntent = new Intent(getApplicationContext(),
							CleanerActivity.class);
					startActivity(dataSecureIntent);

					easyTracker.send(MapBuilder.createEvent(
							getResources()
									.getString(R.string.clean_memory_activity),
							getResources().getString(
									R.string.clean_memory_activity_navigate),
							"track event", null).build());

					break;
				/**
				 * To navigate to Clean App module from home screen functionality.
				 */
				case R.id.cleanapp_image:

					if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
						cleanAppIntent = new Intent(getApplicationContext(),AppManagerActivity.class);
						startActivity(cleanAppIntent);
					} else {
						// Check if permission enabled
						if (Util.getUsageStatsList(HomeActivity.this)
								.isEmpty()) {
							easyTracker.send(MapBuilder.createEvent(getString(R.string.home_screen),
									getString(R.string.home_screen_appmanager_usagestates_dialogue_displad),
									"track event", null).build());
							cleanAppPermission();
						} else {
							cleanAppIntent = new Intent(getApplicationContext(),AppManagerActivity.class);
							startActivity(cleanAppIntent);
						}
					}
					easyTracker.send(MapBuilder.createEvent(
							getResources().getString(R.string.clean_app_activity),
							getResources().getString(
									R.string.clean_app_activity_navigate),
							"track event", null).build());
					break;
				default:
					break;
			}
		}

	};
	private String s;
	private void appLockActivity() {
		SharedPreferences sharedPreferencesAppLock = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
		String pass = String.valueOf(sharedPreferencesAppLock.getInt("passwordFinal", 999999));
		SharedPreferences forgotEmailSharedPref = getSharedPreferences("APPLOCK_FORGET_PASSWORD", MODE_PRIVATE);
		String forgotEmailDone = forgotEmailSharedPref.getString(getResources().getString(R.string.forgot_email_done), "");

		if(pass.length()==1){
			s="000"+pass;
		}
		else if(pass.length()==2){
			s="00"+pass;
		}
		else if(pass.length()==3){
			s="0"+pass;
		}else {
			s=pass;
		}
		if(String.valueOf(s).length() == 4  ){
			if(forgotEmailDone.isEmpty()){
				Intent intentSetEmailIntent = new Intent(this,SetEmailActivity.class);
				startActivity(intentSetEmailIntent);
			}else{
				Intent intent = new Intent(this,ShowApplockViewOtherApps.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				if(lockFrom == null){
					intent.putExtra("LOCK_FROM", "fromHome");
				}else{
					intent.putExtra("LOCK_FROM", lockFrom);
				}
				startActivity(intent);
			}
		}else{
			Intent intent = new Intent(this,SetApplockPin.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
		}
	}
	protected void applockDialog() {
		final Dialog dialog = new Dialog(HomeActivity.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.usage_states_dialogue);
		dialog.setCanceledOnTouchOutside(false);
		TextView ok = (TextView) dialog.findViewById(R.id.ok);
		dialog.show();
		ok.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
				intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
				startActivityForResult(intent, Constants.APPLOCK_RESULT_CODE);
			}
		});
	}

	protected void cleanAppPermission() {
		final Dialog dialog = new Dialog(HomeActivity.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.usage_states_dialogue);
		dialog.setCanceledOnTouchOutside(false);
		TextView ok = (TextView) dialog.findViewById(R.id.ok);
		dialog.show();
		ok.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
				intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
				startActivityForResult(intent, Constants.CLEANAPP_RESULT_CODE);
			}
		});
	}

	/**
	 * Changes ListView to GridView or GridView to ListView
	 */
	@Override
	public void changelayout() {
		super.changelayout();
		sharedpreferences = getSharedPreferences(
				getResources().getString(R.string.preference_name),
				Context.MODE_PRIVATE);
		value = sharedpreferences.getString(
				getResources().getString(R.string.sharedpreference_key), "");
		if (value
				.equalsIgnoreCase(getResources().getString(R.string.list_view))) {
			View gridLayout = findViewById(R.id.gridlayout);
			gridLayout.setVisibility(View.GONE);
			donutProgress.setEnabled(false);
			View footerLayout = findViewById(R.id.container_header_lyt);
			footerLayout.setVisibility(View.VISIBLE);

		} else {
			View gridLayout = findViewById(R.id.gridlayout);
			gridLayout.setVisibility(View.VISIBLE);
			View footerLayout = findViewById(R.id.container_header_lyt);
			footerLayout.setVisibility(View.GONE);
			donutProgress.setEnabled(true);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		if (timerDonut != null) {
			timerDonut = null;

		}
		if (cleanMemory != null) {
			cleanMemory = null;

		}
		if (intent != null) {
			intent = null;

		}
		if (mTimerTask != null) {
			mTimerTask = null;

		}

		if (cleanerIntent != null) {
			cleanerIntent = null;

		}
		if (batterySaveIntent != null) {
			batterySaveIntent = null;

		}
		if (dataSecureIntent != null) {
			dataSecureIntent = null;

		}
		if (cleanAppIntent != null) {
			cleanAppIntent = null;

		}
		if (dialog != null) {
			dialog = null;

		}

		unRegisterBaseActivityReceiver();

		unbindDrawables(findViewById(R.id.gridlayout));

		System.gc();

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == Constants.APPLOCK_RESULT_CODE) {
			if(Util.getUsageStatsList(HomeActivity.this)
					.isEmpty()){

			}else{
				appLockActivity();
			}
		} else if(requestCode == Constants.CLEANAPP_RESULT_CODE){
			if(Util.getUsageStatsList(HomeActivity.this)
					.isEmpty()){

			}else{
				Intent cleanAppIntent = new Intent(getApplicationContext(),
						AppManagerActivity.class);
				startActivity(cleanAppIntent);
			}
		}
		else{
		}
	}

	private void getCpuTemparature() {
		Intent intent = registerReceiver(null, new IntentFilter(
				Intent.ACTION_BATTERY_CHANGED));
		float temp = ((float) intent.getIntExtra(
				BatteryManager.EXTRA_TEMPERATURE, 0) / 10);
		cpuTemp = temp;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			flag = cpuCoolerTempSharedPref.getBoolean(getResources()
					.getString(R.string.cpuCoolerTemp), false);

			flag = true;
			cpuCoolerTempEditor.putBoolean(
					getResources().getString(R.string.cpuCoolerTemp),
					flag);
			cpuCoolerTempEditor.commit();

			final Dialog dialog = new Dialog(HomeActivity.this);

			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.exit_dialog);
			dialog.setCanceledOnTouchOutside(false);
			TextView ok = (TextView) dialog.findViewById(R.id.ok);
			TextView cancel = (TextView) dialog.findViewById(R.id.cancel);
			dialog.show();
			ok.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent serviceIntent = new Intent(HomeActivity.this, RedGreenService.class);
					serviceIntent.putExtra("sendAWS","sendAWS");
					startService(serviceIntent);
					Intent i = new Intent(HomeActivity.this, QuitAppSplashActivity.class);
					startActivity(i);
					dialog.dismiss();
				}
			});
			cancel.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}

	private void InternalMemory(){
		double internalMemory;
		easyTracker.send(MapBuilder.createEvent(getString(R.string.home_screen),
				getString(R.string.home_screen_get_storage_details),
				"track event", null).build());
		internalMemory = Utility.getInternalMemory();
		if(AppData.getInstance().getInternalMemory() >= 0.0){
			AppData.getInstance().setInternalMemory(internalMemory);
			donutProgres(AppData.getInstance().getInternalMemory());
		}

	}
}