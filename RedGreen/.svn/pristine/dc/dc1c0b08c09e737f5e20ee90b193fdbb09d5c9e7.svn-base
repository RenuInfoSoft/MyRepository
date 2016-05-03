package com.unfoldlabs.redgreen.applock.ui;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.MapBuilder;
import com.unfoldlabs.redgreen.R;
import com.unfoldlabs.redgreen.activity.BaseActivity;
import com.unfoldlabs.redgreen.applock.lock.PasswordView;

public class SetApplockPin extends BaseActivity implements OnClickListener, TextWatcher{

	private ViewGroup mLockView = null;
	private PasswordView  mLockPasswordView = null;
	private TextView lock_tv_footer = null;
	private Button numlock_Next_Confirm,numlock_bRight,numlock_b1,numlock_b2,numlock_b3,numlock_b4,numlock_b5,numlock_b6,numlock_b7,numlock_b8,numlock_b9,numlock_b0=null;
	private Button numlock_bLeft;
	private EditText passwordTextView = null;
	private String value = "";
	private ImageView lock_iv_go_back,lock_iv_app_icon;
	private TextView lock_tv_text;
	private String numlock_Next_ConfirmLabel = "Next";
	private Menu mMenu;
	private String nextPassWord = "";
	private SharedPreferences sharedPreferencesAppLock;
	private String MyPREFERENCES = "REDPrefs" ;
	private ImageView deep_linking_image;
	public EasyTracker easyTracker;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sharedPreferencesAppLock = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
		easyTracker = EasyTracker.getInstance(getApplicationContext());
		easyTracker.send(MapBuilder.createEvent(getString(R.string.app_lock), getString(R.string.app_lock_set_pin),
				"track event", null).build());
		getLayoutInflater().inflate(R.layout.activity_locker_twopanes, frameLayout);
		android.app.ActionBar mActionBar = getActionBar();
		Drawable d = getResources().getDrawable(R.drawable.actionbar_baground);
		mActionBar.setBackgroundDrawable(d);
		mActionBar.setIcon(R.drawable.action_bar_icon);
		mActionBar.setTitle(getString(R.string.app_lock));
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
		mLockView = (LinearLayout)findViewById(R.id.lock_lockview);
		lock_iv_go_back = (ImageView)findViewById(R.id.lock_iv_go_back);
		lock_iv_go_back.setOnClickListener(this);
		lock_iv_go_back.setVisibility(View.GONE);
		lock_iv_app_icon = (ImageView)findViewById(R.id.lock_iv_app_icon);
		lock_iv_app_icon.setBackgroundResource(R.drawable.app_icon);
		lock_tv_text = (TextView)findViewById(R.id.lock_tv_text);
		lock_tv_text.setText(R.string.password_change_tit);
		lock_tv_footer = (TextView)findViewById(R.id.lock_tv_footer);
		lock_tv_footer.setText(R.string.password_change_head);

		LayoutInflater li = LayoutInflater.from(this);
		RelativeLayout lyt = (RelativeLayout) li.inflate(R.layout.password_txt_lyt,null);
		deep_linking_image = (ImageView)lyt.findViewById(R.id.icon);
		deep_linking_image.setOnClickListener(this);
		passwordTextView = (EditText)lyt.findViewById(R.id.passwordTextView);

		passwordTextView.addTextChangedListener(this);
		passwordTextView.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				v.onTouchEvent(event);
				InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
				if (imm != null) {
					imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
				}
				return true;
			}
		});
		mLockView.addView(lyt);
		/**
		 * lock screen number panel with and without footer buttons
		 */
		mLockPasswordView = (PasswordView) li.inflate(R.layout.view_lock_number, null);

		numlock_b1 =	 (Button)mLockPasswordView.findViewById(R.id.numlock_b1);
		numlock_b1.setOnClickListener(this);

		numlock_b2 =	(Button) mLockPasswordView.findViewById(R.id.numlock_b2);
		numlock_b2.setOnClickListener(this);

		numlock_b3 =	(Button) mLockPasswordView.findViewById(R.id.numlock_b3);
		numlock_b3.setOnClickListener(this);

		numlock_b4 =	(Button) mLockPasswordView.findViewById(R.id.numlock_b4);
		numlock_b4.setOnClickListener(this);

		numlock_b5 =	(Button) mLockPasswordView.findViewById(R.id.numlock_b5);
		numlock_b5.setOnClickListener(this);

		numlock_b6 =	(Button) mLockPasswordView.findViewById(R.id.numlock_b6);
		numlock_b6.setOnClickListener(this);

		numlock_b7 =	(Button)mLockPasswordView.findViewById(R.id.numlock_b7);
		numlock_b7.setOnClickListener(this);

		numlock_b8 =	(Button) mLockPasswordView.findViewById(R.id.numlock_b8);
		numlock_b8.setOnClickListener(this);

		numlock_b9 =	(Button) mLockPasswordView.findViewById(R.id.numlock_b9);
		numlock_b9.setOnClickListener(this);

		numlock_b0 =	(Button) mLockPasswordView.findViewById(R.id.numlock_b0);
		numlock_b0.setOnClickListener(this);

		numlock_bLeft =	(Button) mLockPasswordView.findViewById(R.id.numlock_bLeft); //back button
		numlock_bLeft.setOnClickListener(this);

		numlock_Next_Confirm = (Button)mLockPasswordView.findViewById(R.id.numlock_Next_Confirm);//next and confirm
		numlock_Next_Confirm.setOnClickListener(this);
		numlock_Next_Confirm.setText("Next");

		numlock_bRight = (Button)mLockPasswordView.findViewById(R.id.numlock_bRight);//cancel button
		numlock_bRight.setOnClickListener(this);
		numlock_bRight.setText("Cancel");

		mLockView.addView(mLockPasswordView);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		easyTracker.send(MapBuilder.createEvent(getString(R.string.app_lock),
				getString(R.string.app_lock_set_pin_exit),
				"track event", null).build());
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.numlock_b0:
				if(value.length() <= 4){
					value = value+"0";
					setPasswordSet();
				}
				break;
			case R.id.numlock_b1:
				if(value.length() <= 4){
					value = value+"1";
					setPasswordSet();
				}
				break;
			case R.id.numlock_b2:
				if(value.length() <= 4)
					value = value+"2";
				setPasswordSet();
				break;
			case R.id.numlock_b3:
				if(value.length() <= 4){
					value = value+"3";
					setPasswordSet();
				}
				break;
			case R.id.numlock_b4:
				if(value.length() <= 4){
					value = value+"4";
					setPasswordSet();}
				break;
			case R.id.numlock_b5:
				if(value.length() <= 4){
					value = value+"5";
					setPasswordSet();
				}
				break;
			case R.id.numlock_b6:
				if(value.length() <= 4){
					value = value+"6";
					setPasswordSet();
				}
				break;
			case R.id.numlock_b7:
				if(value.length() <= 4){
					value = value+"7";
					setPasswordSet();
				}
				break;
			case R.id.numlock_b8:
				if(value.length() <= 4){
					value = value+"8";
					setPasswordSet();
				}
				break;
			case R.id.numlock_b9:
				if(value.length() <= 4){
					value = value+"9";
					setPasswordSet();
				}

				break;
			case R.id.numlock_bLeft:
				//back
				if(value.length() > 0){
					StringBuilder sb = new StringBuilder(value);
					sb.deleteCharAt(sb.length() - 1);

					value = sb.toString();
					setPasswordSet();
					if(sb != null)
						sb = null;
				}
				break;

			case R.id.numlock_Next_Confirm:
				easyTracker.send(MapBuilder.createEvent(getString(R.string.app_lock),
						getResources().getString(R.string.app_lock_next_button),
						"track event", null).build());
				//first time , when user enter password and clicks next button
				if(numlock_Next_Confirm.getText().toString().equalsIgnoreCase("Next")){
					if(!passwordTextView.getText().toString().equalsIgnoreCase("")){
						if(passwordTextView.getText().toString().length() == 4){
							nextPassWord =  passwordTextView.getText().toString();
							SharedPreferences.Editor editor = sharedPreferencesAppLock.edit();
							editor.putInt("passwordnext", Integer.parseInt(nextPassWord));
							editor.commit();
							value = "";
							passwordTextView.setText("");
							numlock_Next_Confirm.setText("Confirm");
							lock_tv_footer.setText(R.string.password_change_confirm);
						}else{
							Toast.makeText(getApplicationContext(), "Please enter 4 digits", Toast.LENGTH_LONG).show();
						}
					}else{
						Toast.makeText(getApplicationContext(), "Please enter new password", Toast.LENGTH_LONG).show();
					}
				}else{
					easyTracker.send(MapBuilder.createEvent(
							getResources().getString(R.string.set_newpassword_confirm),
							getString(R.string.app_lock_enter_pin_screen),
							"track event", null).build());
					if(!passwordTextView.getText().toString().equalsIgnoreCase("")){
						if(passwordTextView.getText().toString().length() == 4){
							SharedPreferences.Editor editor = sharedPreferencesAppLock.edit();
							editor.putInt("password", Integer.parseInt(passwordTextView.getText().toString()));
							editor.commit();
							if(sharedPreferencesAppLock.getInt("password", 0) == sharedPreferencesAppLock.getInt("passwordnext", 0)){
								editor.putInt("passwordFinal", Integer.parseInt(passwordTextView.getText().toString()));
								editor.commit();
								//success, move to next activity if email is empty
								Intent intentSetEmailIntent = new Intent(this,SetEmailActivity.class);
								intentSetEmailIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								startActivity(intentSetEmailIntent);
								finish();
							}else{
								Toast.makeText(getApplicationContext(), "Please enter correct password", Toast.LENGTH_LONG).show();
							}
						}else{
							Toast.makeText(getApplicationContext(), "Please enter 4 digit password", Toast.LENGTH_LONG).show();
						}
					}else{
						Toast.makeText(getApplicationContext(), "Please enter correct password", Toast.LENGTH_LONG).show();
					}

				}
				break;

			case R.id.numlock_bRight:
				easyTracker.send(MapBuilder.createEvent(
						getResources().getString(R.string.set_newpassword_cancel),getResources().getString(R.string.set_newpassword_cancel_button),
						"track event", null).build());

				backCancel();
				break;

			case R.id.lock_iv_go_back:
				backCancel();
				break;

			case R.id.icon:
				Intent intent = new Intent();
				intent.setComponent(new ComponentName(getPackageName(), "com.unfoldlabs.redgreen.activity.HomeActivity"));
				intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.putExtra("LOCK_FROM", "deeplink");
				getApplicationContext().startActivity(intent);
				finish();
				break;
			default:
				break;
		}
	}
	private void backCancel() {
		if(numlock_Next_Confirm.getText().toString().equalsIgnoreCase("Next")){
			//next cancel
			finish();
		}else{
			//confirm cancel
			value = "";
			passwordTextView.setText("");
			numlock_Next_Confirm.setText("Next");
			lock_tv_footer.setText(R.string.password_change_head);
		}
	}

	private void setPasswordSet() {
		passwordTextView.setText(value);
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
								  int after) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
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
	public void afterTextChanged(Editable s) {
		passwordTextView.setSelection(s.length());
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(numlock_Next_Confirm.getText().toString().equalsIgnoreCase("Next")){
			onBackPressed();
		}else{
			Intent intent = new Intent(this,SetApplockPin.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
		}
		return true;
	}
}
