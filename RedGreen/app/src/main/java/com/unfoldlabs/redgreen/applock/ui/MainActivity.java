package com.unfoldlabs.redgreen.applock.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.MapBuilder;
import com.unfoldlabs.redgreen.R;
import com.unfoldlabs.redgreen.activity.BaseActivity;
import com.unfoldlabs.redgreen.applock.appselect.AppAdapter;
import com.unfoldlabs.redgreen.applock.appselect.AppListElement;
import com.unfoldlabs.redgreen.global.AppData;

import java.util.HashSet;
import java.util.Set;

public class MainActivity extends BaseActivity {
	private AppAdapter mAdapter;
	private Set<String> set;
	private AppListElement item;
	private SharedPreferences.Editor editor ;
	private SharedPreferences sharedPreferencesAppLock;
	private String MyPREFERENCES = "REDPrefs" ;
	private SharedPreferences forgotEmailSharedPref;
	private Menu mMenu = null;
	public EasyTracker easyTracker;
	private boolean isLoadMenu = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sharedPreferencesAppLock = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
		editor = sharedPreferencesAppLock.edit();

		forgotEmailSharedPref = getSharedPreferences("APPLOCK_FORGET_PASSWORD", Context.MODE_PRIVATE);
		editor = forgotEmailSharedPref.edit();
		easyTracker = EasyTracker.getInstance(getApplicationContext());
		getLayoutInflater().inflate(R.layout.fragment_applist, frameLayout);
		android.app.ActionBar mActionBar = getActionBar();
		Drawable d = getResources().getDrawable(R.drawable.actionbar_baground);
		mActionBar.setBackgroundDrawable(d);
		mActionBar.setIcon(R.drawable.action_bar_icon);
		mActionBar.setTitle("App Lock");
		mActionBar.setDisplayUseLogoEnabled(true);
		mActionBar.setDisplayShowHomeEnabled(true);
		mActionBar.setDisplayShowTitleEnabled(true);
		mActionBar.setDisplayHomeAsUpEnabled(true);
		int actionBarTitleId = Resources.getSystem().getIdentifier("action_bar_title", "id", "android");
		if (actionBarTitleId > 0) {
			TextView title = (TextView) findViewById(actionBarTitleId);
			if (title != null) {
				title.setTextColor(Color.WHITE);
			}
		}

		ListView mListView = (ListView)findViewById(R.id.lvAppList);
		mAdapter = new AppAdapter(this);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				AppListElement item = (AppListElement) mAdapter.getItem(position);
				MainActivity.this.item =item;
				if (item.isApp()) {
					mAdapter.toggle(item);
					Switch lockSwitch = (Switch) view.findViewById(R.id.applist_item_image);
					lockSwitch.setChecked(item.locked ? true : false);
					// And the menu
					updateSwitchkMenuLayout(item);
				}

			}
		});

	}

	private void updateSwitchkMenuLayout(AppListElement item2){
		SharedPreferences sharedPreferencesAppLock = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
		Set<String> set = sharedPreferencesAppLock.getStringSet("key", null);

		if(mAdapter.getCount()-2 == set.size()){
			mMenu.findItem(R.id.apps_menu_lock_all).setVisible(false);
			mMenu.findItem(R.id.apps_menu_unlock_all).setVisible(true);
			mAdapter.prepareUndo();
		}else{
			mMenu.findItem(R.id.apps_menu_lock_all).setVisible(true);
			mMenu.findItem(R.id.apps_menu_unlock_all).setVisible(false);
			mAdapter.prepareUndo();
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		isLoadMenu = forgotEmailSharedPref.getBoolean("lockMenu", false);
		set = sharedPreferencesAppLock.getStringSet("key", null);
		if (set == null){
			set = new HashSet<String>();
			editor.putStringSet("key", set);
			editor.commit();
		}
	}

	/**
	 * update option menu
	 * @param b
	 */
	private void updateMenuLayout(boolean b) {
		if(b){
			onLockAllOptions(true);
		}else{
			onLockAllOptions(false);
		}
	}
	/**
	 * creating options menu
	 */

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		this.mMenu = menu;
		menu.findItem(R.id.action_sort).setVisible(false);
		menu.findItem(R.id.menu_settings).setVisible(false);
		getMenuInflater().inflate(R.menu.apps, menu);
		if(isLoadMenu){
			updateMenuLayout(isLoadMenu);
		}
		return true;
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
		AppData.getInstance().setIsHomePackage(false);
		if(AppData.getInstance().isFromHome()){
			AppData.getInstance().setIsFromHome(false);
		}
	}

	/**
	 * option menu item selection functionality
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_change_lock:
				easyTracker.send(MapBuilder.createEvent(
						getResources().getString(R.string.change_lock),getResources().getString(R.string.change_lock_navigate),
						"track event", null).build());
				Intent intent = new Intent(this,SetApplockPin.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				return true;
			case R.id.apps_menu_lock_all:
				easyTracker.send(MapBuilder.createEvent(
						getResources().getString(R.string.app_lock),getResources().getString(R.string.app_lock_button),
						"track event", null).build());
				new LockAll().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


				return true;
			case R.id.apps_menu_unlock_all:
				easyTracker.send(MapBuilder.createEvent(
						getResources().getString(R.string.app_unlock),getResources().getString(R.string.app_unlock_button),
						"track event", null).build());
				new UnLockAll().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

				return true;
			default:
				return super.onOptionsItemSelected(item);
		}

	}
	/**
	 * lock all apps functionality
	 * @param lockall
	 */
	private void onLockAllOptions(boolean lockall) {
		mMenu.findItem(R.id.apps_menu_lock_all).setVisible(!lockall);
		mMenu.findItem(R.id.apps_menu_unlock_all).setVisible(lockall);
		mAdapter.prepareUndo();
		mAdapter.setAllLocked(lockall, item);
	}

	private class LockAll extends AsyncTask<Void, Void, Void>{
		ProgressDialog progressDialog;
		@Override
		protected void onPreExecute() {
			progressDialog = new ProgressDialog(MainActivity.this);
			progressDialog.setMessage("Processing...");
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.show();

		}
		@Override
		protected Void doInBackground(Void... params) {

			editor.putBoolean("lockMenu", true);
			editor.commit();
			isLoadMenu = forgotEmailSharedPref.getBoolean("lockMenu", true);
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			onLockAllOptions(true);
			if (progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
		}
	}
	private class UnLockAll extends AsyncTask<Void, Void, Void>{
		ProgressDialog progressDialog;
		@Override
		protected void onPreExecute() {
			progressDialog = new ProgressDialog(MainActivity.this);
			progressDialog.setMessage("Processing...");
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.show();

		}
		@Override
		protected Void doInBackground(Void... params) {

			editor.putBoolean("lockMenu", false);
			editor.commit();
			isLoadMenu = forgotEmailSharedPref.getBoolean("lockMenu", false);
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			onLockAllOptions(false);
			if (progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
		}
	}
}
