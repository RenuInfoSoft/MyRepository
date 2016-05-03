package com.unfoldlabs.redgreen.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.MailTo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnKeyListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.analytics.tracking.android.MapBuilder;
import com.unfoldlabs.redgreen.R;

@SuppressLint("SetJavaScriptEnabled")
public class AboutUs extends BaseActivity {

	private WebView termsConditions;
	private ProgressDialog progressDialog = null;
	private WebClientClass webViewClient;
	private Intent i;
	private String[] mailToString;
	private WebChromeClient webChromeClient;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		getLayoutInflater().inflate(R.layout.terms_conditions, frameLayout);
		registerBaseActivityReceiver();
		easyTracker.send(MapBuilder.createEvent(getString(R.string.home_screen),
				getString(R.string.home_screen_about_us_displayed),
				"track event", null).build());
		termsConditions = (WebView) findViewById(R.id.terms_conditions);
		progressDialog = new ProgressDialog(AboutUs.this);
		progressDialog.setMessage("Loading...");
		termsConditions.getSettings().setJavaScriptEnabled(true);
		termsConditions.loadUrl("file:///android_asset/ABOUT_US.html");
		webViewClient = new WebClientClass();
		termsConditions.setWebViewClient(webViewClient);
		termsConditions.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
		termsConditions.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		termsConditions.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
		webChromeClient = new WebChromeClient();
		termsConditions.setWebChromeClient(webChromeClient);
		termsConditions.loadUrl("file:///android_asset/ABOUT_US.html");
		termsConditions.setOnKeyListener(new OnKeyListener() {

			/**
			 * To work "Back Button" functionality in AboutUs HTML page
			 */
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					WebView webView = (WebView) v;
					switch (keyCode) {
						case KeyEvent.KEYCODE_BACK:
							if (webView.canGoBack()) {
								webView.goBack();
								easyTracker.send(MapBuilder.createEvent(getString(R.string.home_screen),
										getString(R.string.home_screen_about_us_terms_conditions_exit),
										"track event", null).build());
								return true;
							}
							break;
					}
				}

				return false;
			}
		});
	}

	public class WebClientClass extends WebViewClient {
		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
			easyTracker.send(MapBuilder.createEvent(getString(R.string.home_screen),
					getString(R.string.home_screen_about_us_terms_conditions_displyed),
					"track event", null).build());
			if (!progressDialog.isShowing())
				progressDialog.show();
		}

		/**
		 * close progress dialog after finishing page loading
		 */
		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			if (progressDialog.isShowing())
				progressDialog.dismiss();
		}

		/**
		 * In AboutUs page "mailto" hyperlink functionality
		 */
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			if (url.startsWith("mailto:")) {
				MailTo mt = MailTo.parse(url);
				i = new Intent(Intent.ACTION_SEND);
				i.setType("text/plain");
				mailToString	= new String[] { mt.getTo() };
				i.putExtra(Intent.EXTRA_EMAIL, mailToString);
				i.putExtra(Intent.EXTRA_SUBJECT, mt.getSubject());
				i.putExtra(Intent.EXTRA_CC, mt.getCc());
				i.putExtra(Intent.EXTRA_TEXT, mt.getBody());
				startActivity(i);
				view.reload();
				return true;
			}
			return super.shouldOverrideUrlLoading(view, url);
		}
	}

	/**
	 * To create options in actionbar
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.findItem(R.id.action_sort).setVisible(false);
		menu.findItem(R.id.menu_settings).setVisible(false);
		return true;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		{
			if(termsConditions != null){
				termsConditions = null;
			}
			if(progressDialog != null){
				progressDialog = null;
			}
			if(webViewClient != null){
				webViewClient = null;
			}
			if(i != null){
				i = null;
			}
			if(mailToString != null){
				mailToString = null;
			}
			if(webChromeClient != null){
				webChromeClient = null;
			}
			unRegisterBaseActivityReceiver();
			unbindDrawables(findViewById(R.id.terms_conditions_lyt));
			System.gc();
		}}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}
}
