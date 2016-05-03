package com.unfoldlabs.redgreen.global;

import com.unfoldlabs.redgreen.model.AppsListItem;
import com.unfoldlabs.redgreen.model.CleanAppDB;
import com.unfoldlabs.redgreen.model.ListItem;
import com.unfoldlabs.redgreen.model.SaveBatteryModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for all the global variables
 *
 */
public class AppData {

	private boolean appLockRedirect;
	private boolean AppManagerUnInstall;
	private boolean appManagerState1;
	public List<CleanAppDB> appManagerInstalledAppsList;
	private boolean applockExit = false;
	private boolean appManagerState0;
	private static AppData instance = null;
	private boolean isAskDeleteCancel;
	private boolean isNotificationON = true;
	private boolean isFromNoifiction = true;
	private int notificationSettings;
	private double internalMemory;
	private String appInstalledTime;
	private double memorysize;
	private long junkData;
	public List<ListItem> browserHistory = new ArrayList<ListItem>();
	public List<ListItem> list = new ArrayList<ListItem>();
	public List<ListItem> bagroundlist;
	public List<AppsListItem> junk;
	public boolean ramTaskFlag;
	public boolean isSpeedBoosterService;
	private boolean isFromSplash = true;
	public double ram;
	private boolean isTaskRunning;
	private long internalStorage;
	private boolean isBoostMemory = true;
	private long dynamicMaxJunkCacheMemorySize= -1;
	private long dynamicJunkCacheMemorySize= -1;
	private long dynamicMinJunkCacheMemorySize= -1;
	private boolean isJunkCleaned = false;
	private boolean isFromHome;
	private boolean isHomePackage;
	protected AppData() {
	}

	public boolean isHomePackage() {
		return isHomePackage;
	}

	public void setIsHomePackage(boolean isHomePackage) {
		this.isHomePackage = isHomePackage;
	}

	public static AppData getInstance() {
		if (instance == null) {
			instance = new AppData();
		}
		return instance;
	}

	public boolean isFromHome() {
		return isFromHome;
	}

	public void setIsFromHome(boolean isFromHome) {
		this.isFromHome = isFromHome;
	}

	public boolean isFromNoifiction() {
		return isFromNoifiction;
	}

	public void setFromNoifiction(boolean isFromNoifiction) {
		this.isFromNoifiction = isFromNoifiction;
	}
	public long getDynamicJunkCacheMemorySize() {
		return dynamicJunkCacheMemorySize;
	}
	public void setDynamicJunkCacheMemorySize(long dynamicJunkCacheMemorySize) {
		this.dynamicJunkCacheMemorySize = dynamicJunkCacheMemorySize;
	}
	public long getDynamicMinJunkCacheMemorySize() {
		return dynamicMinJunkCacheMemorySize;
	}
	public void setDynamicMinJunkCacheMemorySize(long dynamicMinJunkCacheMemorySize) {
		this.dynamicMinJunkCacheMemorySize = dynamicMinJunkCacheMemorySize;
	}
	public long getDynamicMaxJunkCacheMemorySize() {
		return dynamicMaxJunkCacheMemorySize;
	}
	public void setDynamicMaxJunkCacheMemorySize(long dynamicMaxJunkCacheMemorySize) {
		this.dynamicMaxJunkCacheMemorySize = dynamicMaxJunkCacheMemorySize;
	}
	public boolean isSpeedBoosterService() {
		return isSpeedBoosterService;
	}

	public void setSpeedBoosterService(boolean isSpeedBoosterService) {
		this.isSpeedBoosterService = isSpeedBoosterService;
	}
	public List<SaveBatteryModel> getSaveBatteryCPUlist() {
		return saveBatteryCPUlist;
	}

	public void setSaveBatteryCPUlist(List<SaveBatteryModel> saveBatteryCPUlist) {
		this.saveBatteryCPUlist = saveBatteryCPUlist;
	}
	public static void setInstance(AppData instance) {
		AppData.instance = instance;
	}

	public boolean isRamTaskFlag() {
		return ramTaskFlag;
	}

	public void setRamTaskFlag(boolean ramTaskFlag) {
		this.ramTaskFlag = ramTaskFlag;
	}

	public List<SaveBatteryModel> saveBatteryCPUlist;
	public List<SaveBatteryModel> saveBatteryBagroundlist;


	public double getRam() {
		return ram;
	}

	public void setRam(double ram) {
		this.ram = ram;
	}

	/**
	 * @return is ask delete cancel
	 */
	public boolean isAskDeleteCancel() {
		return isAskDeleteCancel;
	}

	/**
	 * the isAskDeleteCancel to set
	 */
	public void setAskDeleteCancel(boolean isAskDeleteCancel) {
		this.isAskDeleteCancel = isAskDeleteCancel;
	}

	/**
	 * @return isNotificationON
	 */
	public boolean isNotificationON() {
		return isNotificationON;
	}
	/**
	 * the NotificationON to set
	 */
	public void setNotificationON(boolean isNotificationON) {
		this.isNotificationON = isNotificationON;
	}
	/**
	 * @return getNotificationSettings
	 */
	public int getNotificationSettings() {
		return notificationSettings;
	}

	/**
	 * the NotificationSettings to set
	 */
	public void setNotificationSettings(int notificationSettings) {
		this.notificationSettings = notificationSettings;
	}
	/**
	 * @return getInternalMemory
	 */
	public double getInternalMemory() {
		return internalMemory;
	}

	/**
	 * the InternalMemory to set
	 */
	public void setInternalMemory(double internalMemory) {
		this.internalMemory = internalMemory;
	}

	/**
	 * @return getAppInstalledTime
	 */
	public String getAppInstalledTime() {
		return appInstalledTime;
	}

	/**
	 * the AppInstalledTime to set
	 */
	public void setAppInstalledTime(String appInstalledTime) {
		this.appInstalledTime = appInstalledTime;
	}

	public List<ListItem> getList() {
		return list;
	}

	public void setList(List<ListItem> list) {
		this.list = list;
	}

	public List<ListItem> getBrowserHistory() {
		return browserHistory;
	}

	public void setBrowserHistory(List<ListItem> browserHistory) {
		this.browserHistory = browserHistory;
	}

	public double getMemorysize() {
		return memorysize;
	}

	public void setMemorysize(double memorysize) {
		this.memorysize = memorysize;
	}

	public List<SaveBatteryModel> getSaveBatteryCpulist() {
		return saveBatteryCPUlist;
	}

	public void setSaveBatteryCpulist(List<SaveBatteryModel> saveBatteryModellist) {
		this.saveBatteryCPUlist = saveBatteryModellist;
	}

	public List<ListItem> getBagroundlist() {
		return bagroundlist;
	}

	public void setBagroundlist(List<ListItem> bagroundlist) {
		this.bagroundlist = bagroundlist;
	}

	public List<SaveBatteryModel> getSaveBatteryBagroundlist() {
		return saveBatteryBagroundlist;
	}

	public void setSaveBatteryBagroundlist(List<SaveBatteryModel> saveBatteryBagroundlist) {
		this.saveBatteryBagroundlist = saveBatteryBagroundlist;
	}

	/**
	 * @return the junk
	 */
	public List<AppsListItem> getJunk() {
		return junk;
	}

	/**
	 * @param junk the junk to set
	 */
	public void setJunk(List<AppsListItem> junk) {
		this.junk = junk;
	}

	/**
	 * @return the junkData
	 */
	public long getJunkData() {
		return junkData;
	}

	/**
	 * @param mCacheSize the junkData to set
	 */
	public void setJunkData(long mCacheSize) {
		this.junkData = mCacheSize;
	}

	public boolean isFromSplash() {
		return isFromSplash;
	}

	public void setFromSplash(boolean isFromSplash) {
		this.isFromSplash = isFromSplash;
	}

	public boolean isTaskRunning() {
		return isTaskRunning;
	}

	public void setTaskRunning(boolean isTaskRunning) {
		this.isTaskRunning = isTaskRunning;
	}

	public long getInternalStorage() {
		return internalStorage;
	}

	public void setInternalStorage(long internalStorage) {
		this.internalStorage = internalStorage;
	}

	public boolean isBoostMemory() {
		return isBoostMemory;
	}

	public void setBoostMemory(boolean isBoostMemory) {
		this.isBoostMemory = isBoostMemory;
	}

	public boolean isJunkCleaned() {
		return isJunkCleaned;
	}

	public void setJunkCleaned(boolean isJunkCleaned) {
		this.isJunkCleaned = isJunkCleaned;
	}


	public boolean isApplockExit() {
		return applockExit;
	}

	public void setApplockExit(boolean applockExit) {
		this.applockExit = applockExit;
	}

	public List<CleanAppDB> getAppManagerInstalledAppsList() {
		return appManagerInstalledAppsList;
	}

	public void setAppManagerInstalledAppsList(List<CleanAppDB> appManagerInstalledAppsList) {
		this.appManagerInstalledAppsList = appManagerInstalledAppsList;
	}

	public List<CleanAppDB> getDoNotDeleteAppsList() {
		return appManagerInstalledAppsList;
	}

	public void setDoNotDeleteAppsList(List<CleanAppDB> appManagerInstalledAppsList) {
		this.appManagerInstalledAppsList = appManagerInstalledAppsList;
	}

	public boolean isAppManagerState0() {
		return appManagerState0;
	}

	public void setAppManagerState0(boolean appManagerState0) {
		this.appManagerState0 = appManagerState0;
	}

	public boolean isAppManagerState1() {
		return appManagerState1;
	}

	public void setAppManagerState1(boolean appManagerState1) {
		this.appManagerState1 = appManagerState1;
	}

	public boolean isAppManagerUnInstall() {
		return AppManagerUnInstall;
	}

	public void setAppManagerUnInstall(boolean appManagerUnInstall) {
		this.AppManagerUnInstall = appManagerUnInstall;
	}

}