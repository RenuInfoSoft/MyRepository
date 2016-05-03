package com.unfoldlabs.redgreen.applock.lock;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
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
import com.unfoldlabs.redgreen.applock.ui.MainActivity;
import com.unfoldlabs.redgreen.global.AppData;
import com.unfoldlabs.redgreen.interfaces.Constants;
import com.unfoldlabs.redgreen.utilty.Utility;

public class ShowApplockViewOtherApps extends Activity implements OnClickListener,  TextWatcher{
	private ViewGroup mLockView = null;
	private PasswordView  mLockPasswordView = null;
	private TextView mViewTitle = null;
	private Button  numlock_Next_Confirm,numlock_bRight,numlock_b1,numlock_b2,numlock_b3,numlock_b4,numlock_b5,numlock_b6,numlock_b7,numlock_b8,numlock_b9,numlock_b0=null;
	private Button numlock_bLeft;
	private EditText passwordTextView = null;
	private String value = "";
	private String lockFrom = "";
	private TextView forgotPasswordImageView;
	private ImageView deep_linking_image, mAppIcon, mLogoRedGreen, mAppIconBack;
	private Menu mMenu;
	private String mPackageName = "";
	private static PackageManager packageManager;
	private SharedPreferences sharedPreferencesAppLock;
	public EasyTracker easyTracker;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_alias_locker);
		sharedPreferencesAppLock = getSharedPreferences(Constants.MyPREFERENCES, Context.MODE_PRIVATE);
		easyTracker = EasyTracker.getInstance(getApplicationContext());
		easyTracker.send(MapBuilder.createEvent(getString(R.string.app_lock),
				getString(R.string.app_lock_set_for_applications),
				"track event", null).build());
		android.app.ActionBar mActionBar = getActionBar();
		Drawable d = getResources().getDrawable(R.drawable.actionbar_baground);
		mActionBar.setBackgroundDrawable(d);
		mActionBar.setIcon(R.drawable.action_bar_icon);
		mActionBar.setTitle("App Lock");
		mActionBar.setDisplayUseLogoEnabled(true);
		mActionBar.setDisplayShowHomeEnabled(true);
		mActionBar.setDisplayShowTitleEnabled(true);
		mActionBar.setDisplayHomeAsUpEnabled(false);
		int actionBarTitleId = Resources.getSystem().getIdentifier("action_bar_title", "id", "android");
		if (actionBarTitleId > 0) {
			TextView title = (TextView) findViewById(actionBarTitleId);
			if (title != null) {
				title.setTextColor(Color.WHITE);
			}
		}
		packageManager = getPackageManager();
		Intent intent=	 getIntent();
		lockFrom = 	 intent.getStringExtra("LOCK_FROM");
		mLockView = (LinearLayout)findViewById(R.id.lock_lockview);
		mViewTitle = (TextView)findViewById(R.id.lock_tv_text);
		mAppIcon = (ImageView) findViewById(R.id.lock_iv_app_icon);
		mLogoRedGreen = (ImageView) findViewById(R.id.logo_redGreen);
		mAppIconBack = (ImageView) findViewById(R.id.lock_iv_go_back);
		mAppIcon.setVisibility(View.VISIBLE);

		LayoutInflater li = LayoutInflater.from(this);
		RelativeLayout lyt = (RelativeLayout) li.inflate(R.layout.password_txt_lyt,null);
		deep_linking_image = (ImageView)lyt.findViewById(R.id.icon);
		deep_linking_image.setOnClickListener(this);
		passwordTextView = (EditText)lyt.findViewById(R.id.passwordTextView);
		passwordTextView.addTextChangedListener(this);
		passwordTextView.setTextColor(Color.WHITE);
		mLockView.addView(lyt);
		forgotPasswordImageView = (TextView)findViewById(R.id.forgot_password);
		forgotPasswordImageView.setOnClickListener(this);
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
		numlock_Next_Confirm.setVisibility(View.INVISIBLE);

		numlock_bRight = (Button)mLockPasswordView.findViewById(R.id.numlock_bRight);//accept button
		numlock_bRight.setOnClickListener(this);
		numlock_bRight.setBackgroundResource(R.drawable.ic_action_accept);
		mLockView.addView(mLockPasswordView);
		mPackageName = sharedPreferencesAppLock.getString("TOP_PACKAGE", "");
		getAppName();
		passwordTextView.setOnTouchListener(new OnTouchListener() {

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
	}

	@Override
	protected void onResume() {
		super.onResume();
		getAppName();
	}

	public void getAppName(){
		if(AppData.getInstance().isHomePackage()){
			deep_linking_image.setVisibility(View.GONE);
			mPackageName = "com.unfoldlabs.redgreen";
		}else{
			deep_linking_image.setVisibility(View.VISIBLE);
			mPackageName = sharedPreferencesAppLock.getString("TOP_PACKAGE", "");
		}

		try {
			String appName = (String)packageManager.getApplicationLabel(packageManager.getApplicationInfo(
					mPackageName, PackageManager.GET_META_DATA));
			Drawable appLogo = packageManager.getApplicationIcon(mPackageName);
			mViewTitle.setText(appName);
			mAppIcon.setImageDrawable(appLogo);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		this.mMenu = menu;
		return true;
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
				break;

			case R.id.numlock_bRight:
				//cancel
				//ok
				refreshLayout();
				break;

			case R.id.forgot_password:
				String password = String.valueOf(sharedPreferencesAppLock.getInt("passwordFinal", 0));
				SharedPreferences forgotEmailSharedPref = getSharedPreferences("APPLOCK_FORGET_PASSWORD", MODE_PRIVATE);
				String forgotEmailDone = forgotEmailSharedPref.getString(getResources().getString(R.string.forgot_email_done), "");
				Utility.postMethod(forgotEmailDone, password, ShowApplockViewOtherApps.this);
				easyTracker.send(MapBuilder.createEvent(getString(R.string.app_lock),
						getString(R.string.app_lock_forgot_password_sent_email),
						"track event", null).build());
				break;

			case R.id.icon:
				Intent intent = new Intent();
				intent.setComponent(new ComponentName(getPackageName(), "com.unfoldlabs.redgreen.activity.HomeActivity"));
				intent.addCategory(Intent.CATEGORY_HOME);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.putExtra("LOCK_FROM", "deeplink");
				startActivity(intent);
				easyTracker.send(MapBuilder.createEvent(getString(R.string.app_lock),
						getString(R.string.app_lock_deep_linking),
						"track event", null).build());
			default:
				break;
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
	public void afterTextChanged(Editable s) {
		passwordTextView.setSelection(s.length());
		refreshLayout();
	}

	private void refreshLayout() {
		int pass = sharedPreferencesAppLock.getInt("passwordFinal", 0000);

		if (value.length() == 4 && pass == Integer.parseInt(value)) {

			if (lockFrom != null) {
				if (lockFrom.equalsIgnoreCase("fromHome") || lockFrom.equalsIgnoreCase("deeplink")) {
					easyTracker.send(MapBuilder.createEvent(getString(R.string.app_lock),
							getString(R.string.app_lock_pin_success_applist),
							"track event", null).build());
					Intent intent = new Intent(this, MainActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
					finish();
				} else {
					finish();
				}
			}
		} else if (value.length() == 4 && pass != Integer.parseInt(value)) {
			value = "";
			passwordTextView.setText("");
			passwordTextView.setEnabled(true);
			passwordTextView.setFocusable(true);
			passwordTextView.setFocusableInTouchMode(true);
			Toast.makeText(getApplicationContext(), "Invalid Password", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onBackPressed() {
		AppData.getInstance().setIsHomePackage(false);
		mPackageName = sharedPreferencesAppLock.getString("TOP_PACKAGE", "");
		if(AppData.getInstance().isFromHome()){
			AppData.getInstance().setIsFromHome(false);
			super.onBackPressed();
		}else{
			Intent startMain = new Intent(Intent.ACTION_MAIN);
			startMain.addCategory(Intent.CATEGORY_HOME);
			startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(startMain);
			finish();
			easyTracker.send(MapBuilder.createEvent(getString(R.string.app_lock),
					getString(R.string.app_lock_appslist_ext_time),
					"track event", null).build());
		}
	}
}
