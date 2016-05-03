package com.unfoldlabs.redgreen.model;

import java.io.Serializable;

public class AppsInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer appId;

	private String installedTime;

	private String versionCode;

	private String label;

	private String packageName;
	private String updatedTime;

	private String versionName;

	private Integer pid;

	public long getAppSize() {
		return appSize;
	}

	public void setAppSize(long appSize) {
		this.appSize = appSize;
	}

	private long appSize;

	/**
	 * @return the appId
	 */
	public Integer getAppId() {
		return appId;
	}

	/**
	 * @param appId
	 *            the appId to set
	 */
	public void setAppId(Integer appId) {
		this.appId = appId;
	}

	/**
	 * @return the installedTime
	 */
	public String getInstalledTime() {
		return installedTime;
	}

	/**
	 * @param installedTime
	 *            the installedTime to set
	 */
	public void setInstalledTime(String installedTime) {
		this.installedTime = installedTime;
	}

	/**
	 * @return the versionCode
	 */
	public String getVersionCode() {
		return versionCode;
	}

	/**
	 * @param versionCode
	 *            the versionCode to set
	 */
	public void setVersionCode(String versionCode) {
		this.versionCode = versionCode;
	}

	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @param label
	 *            the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * @return the packageName
	 */
	public String getPackageName() {
		return packageName;
	}

	/**
	 * @param packageName
	 *            the packageName to set
	 */
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	/**
	 * @return the updatedTime
	 */
	public String getUpdatedTime() {
		return updatedTime;
	}

	/**
	 * @param updatedTime
	 *            the updatedTime to set
	 */
	public void setUpdatedTime(String updatedTime) {
		this.updatedTime = updatedTime;
	}

	/**
	 * @return the versionName
	 */
	public String getVersionName() {
		return versionName;
	}

	/**
	 * @param versionName
	 *            the versionName to set
	 */
	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	/**
	 * @return the pid
	 */
	public Integer getPid() {
		return pid;
	}

	/**
	 * @param pid
	 *            the pid to set
	 */
	public void setPid(Integer pid) {
		this.pid = pid;
	}

}
