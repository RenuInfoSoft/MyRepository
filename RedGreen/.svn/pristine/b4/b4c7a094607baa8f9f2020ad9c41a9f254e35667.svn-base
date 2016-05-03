package com.unfoldlabs.redgreen.applock.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.analytics.tracking.android.MapBuilder;
import com.unfoldlabs.redgreen.R;
import com.unfoldlabs.redgreen.activity.BaseActivity;
import com.unfoldlabs.redgreen.global.ApplicationData;
import com.unfoldlabs.redgreen.interfaces.Constants;

public class SetEmailActivity extends BaseActivity{

	private EditText emailEditText;
	private SharedPreferences forgotEmailSharedPref;
	private Editor forgotEmailEditor;
	private Menu mMenu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getLayoutInflater().inflate(R.layout.forgot_password_screen,frameLayout);
		android.app.ActionBar mActionBar = getActionBar();
		Drawable d = getResources().getDrawable(R.drawable.actionbar_baground);
		mActionBar.setBackgroundDrawable(d);
		easyTracker.send(MapBuilder.createEvent(getString(R.string.app_lock), getString(R.string.set_email_activity),
				"track event", null).build());
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
		forgotEmailSharedPref = getSharedPreferences("APPLOCK_FORGET_PASSWORD", MODE_PRIVATE);
		forgotEmailEditor = forgotEmailSharedPref.edit();
		getActionBar().setDisplayHomeAsUpEnabled(true);
		emailEditText = (EditText) findViewById(R.id.edit_text);
		String forgotEmailDone = forgotEmailSharedPref.getString(getResources().getString(R.string.forgot_email_done), "");
		emailEditText.setText(forgotEmailDone);
		emailEditText.setSelection(emailEditText.getText().toString().length());
		Button done_email = (Button) findViewById(R.id.done_forgot_email);
		done_email.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				easyTracker.send(MapBuilder.createEvent(getResources().getString(R.string.set_email_activity),
						getResources().getString(R.string.set_email_activity_done_button),"track event", null).build());
				if (!emailEditText.getText().toString().matches(Constants.MATHES)) {
					emailEditText.setError(getResources().getString(R.string.applock_valid_email_error));
				}
				else {
					String forgot_email = emailEditText.getText().toString();
					forgotEmailEditor.putString(getResources().getString(R.string.forgot_email_done),forgot_email);
					forgotEmailEditor.putBoolean(getResources().getString(R.string.forgot_email_done_status),true);
					forgotEmailEditor.commit();
					/** AWS setChangeLockEmail**/
					ApplicationData.getInstance().setChangeLockEmail(""+forgot_email);
					Intent i = new Intent(SetEmailActivity.this,MainActivity.class);
					i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(i);
				}
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		this.mMenu = menu;
		menu.findItem(R.id.action_sort).setVisible(false);
		menu.findItem(R.id.menu_settings).setVisible(false);
		return true;
	}
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		onCancelPressed();
		easyTracker.send(MapBuilder.createEvent(getString(R.string.app_lock), getString(R.string.applock_set_email_exit),
				"track event", null).build());
		finish();
	}

	private void onCancelPressed() {
		forgotEmailEditor = forgotEmailSharedPref.edit();
		forgotEmailEditor.putBoolean("backFinish",true);
		forgotEmailEditor.commit();
	}
}
