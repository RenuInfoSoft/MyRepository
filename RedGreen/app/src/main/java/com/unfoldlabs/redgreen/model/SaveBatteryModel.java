package com.unfoldlabs.redgreen.model;

import android.graphics.drawable.Drawable;

public class SaveBatteryModel {
	private String installedAppName;
	private int installedAppIcon;
	private Drawable appIcon;
	private String apptitle;

	private long id;
	private String info;
	private Drawable icon;
	private String packageName;

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public Drawable getIcon() {
		return icon;
	}

	public void setIcon(Drawable icon) {
		this.icon = icon;
	}
	public String getInstalledAppName() {
		return installedAppName;
	}

	public void setInstalledAppName(String installedAppName) {
		this.installedAppName = installedAppName;
	}

	public int getInstalledAppIcon() {
		return installedAppIcon;
	}

	public void setInstalledAppIcon(int installedAppIcon) {
		this.installedAppIcon = installedAppIcon;
	}


	public String getApptitle() {
		return apptitle;
	}

	public void setApptitle(String apptitle) {
		this.apptitle = apptitle;
	}

	public Drawable getAppIcon() {
		return appIcon;
	}

	public void setAppIcon(Drawable appIcon) {
		this.appIcon = appIcon;
	}
}
