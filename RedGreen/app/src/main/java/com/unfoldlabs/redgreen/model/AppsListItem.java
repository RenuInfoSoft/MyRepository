package com.unfoldlabs.redgreen.model;

import android.graphics.drawable.Drawable;

public class AppsListItem {

	private long mCacheSize;
	private String mPackageName, mApplicationName;
	private Drawable mIcon;
	private boolean cacheAppSelected;

	public boolean isCacheAppSelected() {
		return cacheAppSelected;
	}

	/**
	 * @param cacheAppSelected
	 *            to set
	 */
	public void setCacheAppSelected(boolean cacheAppSelected) {
		this.cacheAppSelected = cacheAppSelected;
	}

	public AppsListItem(String packageName, String applicationName,
			Drawable icon, long cacheSize) {
		mCacheSize = cacheSize;
		mPackageName = packageName;
		mApplicationName = applicationName;
		mIcon = icon;
	}

	/**
	 * @return application icon
	 */
	public Drawable getApplicationIcon() {
		return mIcon;
	}

	/**
	 * @return application name
	 */
	public String getApplicationName() {
		return mApplicationName;
	}

	/**
	 * @return cache size
	 */
	public long getCacheSize() {
		return mCacheSize;
	}

	/**
	 * @return package name
	 */
	public String getPackageName() {
		return mPackageName;
	}
}
