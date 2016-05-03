package com.unfoldlabs.redgreen.global;

import java.io.Serializable;

/**
 * Created by Shareefa Md on 4/11/2016.
 */
public class ApplicationData implements Serializable {


    private static ApplicationData instance = null;

    public static ApplicationData getInstance() {
        if (instance == null) {
            instance = new ApplicationData();
        }
        return instance;
    }

    public static void setInstance(ApplicationData instance) {
        ApplicationData.instance = instance;
    }
    String homeTotalROM;
    String Storage;
    String homeUsedROM;
    String totalROM;
    String usedROM;
    String percentageROM;
    String totalRAM;
    String usedRAM;
    String percentageRAM;
    String appCacheUsage;
    String menuItemSelected;
    String navigationMenuEnteredModule;
    String exitedMenuScreen;
    String memoryRAMFlag;
    String browserHistoryFlag;
    String clipBoardDataFlag;
    String residualJunkFilesFlag;
    String junkFiles;
    String noOfbrowserHistoryItems;
    String browserHistorySize;
    String noOfEndingBackgroundProcesses;
    String endingBackgroundProcessesSize;
    String noOfStartupBackgroundProcesses;
    String startupBackgroundProcessesSize;
    String junkFilesSize;
    String noOfDeletedbrowserHistoryItems;
    String browserHistoryDeletedSavedMemorySize;
    String noOfEndingBackgroundProcessesItems;
    String endingBackgroundProcessesSavedMemorySize;
    String noOfStartupProcessesItems;
    String endingStartupProcessesSavedMemorySize;
    String storageTotalSaved;
    String memoryTotalSaved;
    String doneButtonPressedTime;
    String speedBoosterTimeSelected;
    String defaultMemorySize;
    String noOfAppsLocked;
    String appsLocked;
    String changeLockEmail;
    String cpuHeatInitially;
    String screenTimeoutFrom;
    String screenTimeoutTo;
    String screenTimeoutPercentage;
    String screenBrightnessFrom;
    String screenBrightnessPercentage;
    String noOfProcessesShuttingown;
    String shuttingDownMemorySaved;
    String shuttingDownPercentage;
    String cpuTempLastly;
    String displayTempFrom;
    String displayTempTo;
    String appInstalledStartTime;
    String appInstalledEndTime;
    String appInstalledName;
    String appInstalledSize;
    String appInstalledDate;
    String dndappInstalledStartTime;
    String dndappInstalledEndTime;
    String dndappInstalledName;
    String dndappInstalledSize;

    public String getAppCacheUsage() {
        return appCacheUsage;
    }

    public void setAppCacheUsage(String appCacheUsage) {
        this.appCacheUsage = appCacheUsage;
    }

    public String getAppInstalledDate() {
        return appInstalledDate;
    }

    public void setAppInstalledDate(String appInstalledDate) {
        this.appInstalledDate = appInstalledDate;
    }

    public String getAppInstalledEndTime() {
        return appInstalledEndTime;
    }

    public void setAppInstalledEndTime(String appInstalledEndTime) {
        this.appInstalledEndTime = appInstalledEndTime;
    }

    public String getAppInstalledSize() {
        return appInstalledSize;
    }

    public void setAppInstalledSize(String appInstalledSize) {
        this.appInstalledSize = appInstalledSize;
    }

    public String getAppInstalledName() {
        return appInstalledName;
    }

    public void setAppInstalledName(String appInstalledName) {
        this.appInstalledName = appInstalledName;
    }

    public String getAppInstalledStartTime() {
        return appInstalledStartTime;
    }

    public void setAppInstalledStartTime(String appInstalledStartTime) {
        this.appInstalledStartTime = appInstalledStartTime;
    }

    public String getBrowserHistoryDeletedSavedMemorySize() {
        return browserHistoryDeletedSavedMemorySize;
    }

    public void setBrowserHistoryDeletedSavedMemorySize(String browserHistoryDeletedSavedMemorySize) {
        this.browserHistoryDeletedSavedMemorySize = browserHistoryDeletedSavedMemorySize;
    }

    public String getAppsLocked() {
        return appsLocked;
    }

    public void setAppsLocked(String appsLocked) {
        this.appsLocked = appsLocked;
    }

    public String getBrowserHistoryFlag() {
        return browserHistoryFlag;
    }

    public void setBrowserHistoryFlag(String browserHistoryFlag) {
        this.browserHistoryFlag = browserHistoryFlag;
    }

    public String getChangeLockEmail() {
        return changeLockEmail;
    }

    public void setChangeLockEmail(String changeLockEmail) {
        this.changeLockEmail = changeLockEmail;
    }

    public String getClipBoardDataFlag() {
        return clipBoardDataFlag;
    }

    public void setClipBoardDataFlag(String clipBoardDataFlag) {
        this.clipBoardDataFlag = clipBoardDataFlag;
    }

    public String getBrowserHistorySize() {
        return browserHistorySize;
    }

    public void setBrowserHistorySize(String browserHistorySize) {
        this.browserHistorySize = browserHistorySize;
    }

    public String getCpuHeatInitially() {
        return cpuHeatInitially;
    }

    public void setCpuHeatInitially(String cpuHeatInitially) {
        this.cpuHeatInitially = cpuHeatInitially;
    }

    public String getDaysNotUsed() {
        return daysNotUsed;
    }

    public void setDaysNotUsed(String daysNotUsed) {
        this.daysNotUsed = daysNotUsed;
    }

    public String getCpuTempLastly() {
        return cpuTempLastly;
    }

    public void setCpuTempLastly(String cpuTempLastly) {
        this.cpuTempLastly = cpuTempLastly;
    }

    public String getDefaultMemorySize() {
        return defaultMemorySize;
    }

    public void setDefaultMemorySize(String defaultMemorySize) {
        this.defaultMemorySize = defaultMemorySize;
    }

    public String getDisplayTempFrom() {
        return displayTempFrom;
    }

    public void setDisplayTempFrom(String displayTempFrom) {
        this.displayTempFrom = displayTempFrom;
    }

    public String getDisplayTempTo() {
        return displayTempTo;
    }

    public void setDisplayTempTo(String displayTempTo) {
        this.displayTempTo = displayTempTo;
    }

    public String getDndappInstalledDat() {
        return dndappInstalledDat;
    }

    public void setDndappInstalledDat(String dndappInstalledDat) {
        this.dndappInstalledDat = dndappInstalledDat;
    }

    public String getDndappInstalledEndTime() {
        return dndappInstalledEndTime;
    }

    public void setDndappInstalledEndTime(String dndappInstalledEndTime) {
        this.dndappInstalledEndTime = dndappInstalledEndTime;
    }

    public String getDndappInstalledName() {
        return dndappInstalledName;
    }

    public void setDndappInstalledName(String dndappInstalledName) {
        this.dndappInstalledName = dndappInstalledName;
    }

    public String getDndappInstalledSize() {
        return dndappInstalledSize;
    }

    public void setDndappInstalledSize(String dndappInstalledSize) {
        this.dndappInstalledSize = dndappInstalledSize;
    }

    public String getDndappInstalledStartTime() {
        return dndappInstalledStartTime;
    }

    public void setDndappInstalledStartTime(String dndappInstalledStartTime) {
        this.dndappInstalledStartTime = dndappInstalledStartTime;
    }

    public String getEndingBackgroundProcessesSavedMemorySize() {
        return endingBackgroundProcessesSavedMemorySize;
    }

    public void setEndingBackgroundProcessesSavedMemorySize(String endingBackgroundProcessesSavedMemorySize) {
        this.endingBackgroundProcessesSavedMemorySize = endingBackgroundProcessesSavedMemorySize;
    }

    public String getEndingBackgroundProcessesSize() {
        return endingBackgroundProcessesSize;
    }

    public void setEndingBackgroundProcessesSize(String endingBackgroundProcessesSize) {
        this.endingBackgroundProcessesSize = endingBackgroundProcessesSize;
    }

    public String getEndingStartupProcessesSavedMemorySize() {
        return endingStartupProcessesSavedMemorySize;
    }

    public void setEndingStartupProcessesSavedMemorySize(String endingStartupProcessesSavedMemorySize) {
        this.endingStartupProcessesSavedMemorySize = endingStartupProcessesSavedMemorySize;
    }

    public String getExitedMenuScreen() {
        return exitedMenuScreen;
    }

    public void setExitedMenuScreen(String exitedMenuScreen) {
        this.exitedMenuScreen = exitedMenuScreen;
    }

    public String getJunkFiles() {
        return junkFiles;
    }

    public void setJunkFiles(String junkFiles) {
        this.junkFiles = junkFiles;
    }

    public String getJunkFilesSize() {
        return junkFilesSize;
    }

    public void setJunkFilesSize(String junkFilesSize) {
        this.junkFilesSize = junkFilesSize;
    }

    public String getMemoryRAMFlag() {
        return memoryRAMFlag;
    }

    public void setMemoryRAMFlag(String memoryRAMFlag) {
        this.memoryRAMFlag = memoryRAMFlag;
    }

    public String getMemoryTotalSaved() {
        return memoryTotalSaved;
    }

    public void setMemoryTotalSaved(String memoryTotalSaved) {
        this.memoryTotalSaved = memoryTotalSaved;
    }

    public String getMenuItemSelected() {
        return menuItemSelected;
    }

    public void setMenuItemSelected(String menuItemSelected) {
        this.menuItemSelected = menuItemSelected;
    }

    public String getNavigationMenuEnteredModule() {
        return navigationMenuEnteredModule;
    }

    public void setNavigationMenuEnteredModule(String navigationMenuEnteredModule) {
        this.navigationMenuEnteredModule = navigationMenuEnteredModule;
    }

    public String getNoOfAppsLocked() {
        return noOfAppsLocked;
    }

    public void setNoOfAppsLocked(String noOfAppsLocked) {
        this.noOfAppsLocked = noOfAppsLocked;
    }

    public String getNoOfbrowserHistoryItems() {
        return noOfbrowserHistoryItems;
    }

    public void setNoOfbrowserHistoryItems(String noOfbrowserHistoryItems) {
        this.noOfbrowserHistoryItems = noOfbrowserHistoryItems;
    }

    public String getNoOfDeletedbrowserHistoryItems() {
        return noOfDeletedbrowserHistoryItems;
    }

    public void setNoOfDeletedbrowserHistoryItems(String noOfDeletedbrowserHistoryItems) {
        this.noOfDeletedbrowserHistoryItems = noOfDeletedbrowserHistoryItems;
    }

    public String getNoOfEndingBackgroundProcesses() {
        return noOfEndingBackgroundProcesses;
    }

    public void setNoOfEndingBackgroundProcesses(String noOfEndingBackgroundProcesses) {
        this.noOfEndingBackgroundProcesses = noOfEndingBackgroundProcesses;
    }

    public String getNoOfEndingBackgroundProcessesItems() {
        return noOfEndingBackgroundProcessesItems;
    }

    public void setNoOfEndingBackgroundProcessesItems(String noOfEndingBackgroundProcessesItems) {
        this.noOfEndingBackgroundProcessesItems = noOfEndingBackgroundProcessesItems;
    }

    public String getNoOfProcessesShuttingown() {
        return noOfProcessesShuttingown;
    }

    public void setNoOfProcessesShuttingown(String noOfProcessesShuttingown) {
        this.noOfProcessesShuttingown = noOfProcessesShuttingown;
    }

    public String getNoOfStartupBackgroundProcesses() {
        return noOfStartupBackgroundProcesses;
    }

    public void setNoOfStartupBackgroundProcesses(String noOfStartupBackgroundProcesses) {
        this.noOfStartupBackgroundProcesses = noOfStartupBackgroundProcesses;
    }

    public String getNoOfStartupProcessesItems() {
        return noOfStartupProcessesItems;
    }

    public void setNoOfStartupProcessesItems(String noOfStartupProcessesItems) {
        this.noOfStartupProcessesItems = noOfStartupProcessesItems;
    }

    public String getResidualJunkFilesFlag() {
        return residualJunkFilesFlag;
    }

    public void setResidualJunkFilesFlag(String residualJunkFilesFlag) {
        this.residualJunkFilesFlag = residualJunkFilesFlag;
    }

    public String getScreenBrightnessFrom() {
        return screenBrightnessFrom;
    }

    public void setScreenBrightnessFrom(String screenBrightnessFrom) {
        this.screenBrightnessFrom = screenBrightnessFrom;
    }

    public String getScreenBrightnessPercentage() {
        return screenBrightnessPercentage;
    }

    public void setScreenBrightnessPercentage(String screenBrightnessPercentage) {
        this.screenBrightnessPercentage = screenBrightnessPercentage;
    }

    public String getScreenTimeoutFrom() {
        return screenTimeoutFrom;
    }

    public void setScreenTimeoutFrom(String screenTimeoutFrom) {
        this.screenTimeoutFrom = screenTimeoutFrom;
    }

    public String getScreenTimeoutPercentage() {
        return screenTimeoutPercentage;
    }

    public void setScreenTimeoutPercentage(String screenTimeoutPercentage) {
        this.screenTimeoutPercentage = screenTimeoutPercentage;
    }

    public String getScreenTimeoutTo() {
        return screenTimeoutTo;
    }

    public void setScreenTimeoutTo(String screenTimeoutTo) {
        this.screenTimeoutTo = screenTimeoutTo;
    }

    public String getShuttingDownMemorySaved() {
        return shuttingDownMemorySaved;
    }

    public void setShuttingDownMemorySaved(String shuttingDownMemorySaved) {
        this.shuttingDownMemorySaved = shuttingDownMemorySaved;
    }

    public String getShuttingDownPercentage() {
        return shuttingDownPercentage;
    }

    public void setShuttingDownPercentage(String shuttingDownPercentage) {
        this.shuttingDownPercentage = shuttingDownPercentage;
    }

    public String getSpeedBoosterTimeSelected() {
        return speedBoosterTimeSelected;
    }

    public void setSpeedBoosterTimeSelected(String speedBoosterTimeSelected) {
        this.speedBoosterTimeSelected = speedBoosterTimeSelected;
    }

    public String getStartupBackgroundProcessesSize() {
        return startupBackgroundProcessesSize;
    }

    public void setStartupBackgroundProcessesSize(String startupBackgroundProcessesSize) {
        this.startupBackgroundProcessesSize = startupBackgroundProcessesSize;
    }

    public String getStorage() {
        return Storage;
    }

    public void setStorage(String storage) {
        Storage = storage;
    }

    public String getStorageTotalSaved() {
        return storageTotalSaved;
    }

    public void setStorageTotalSaved(String storageTotalSaved) {
        this.storageTotalSaved = storageTotalSaved;
    }

    public String getTotalRAM() {
        return totalRAM;
    }

    public void setTotalRAM(String totalRAM) {
        this.totalRAM = totalRAM;
    }

    public String getTotalROM() {
        return totalROM;
    }

    public void setTotalROM(String totalROM) {
        this.totalROM = totalROM;
    }

    public String getUsedRAM() {
        return usedRAM;
    }

    public void setUsedRAM(String usedRAM) {
        this.usedRAM = usedRAM;
    }

    String dndappInstalledDat;
    String daysNotUsed;

    public String getHomeTotalROM() {
        return homeTotalROM;
    }

    public void setHomeTotalROM(String homeTotalROM) {
        this.homeTotalROM = homeTotalROM;
    }
    public String getHomeUsedROM() {
        return homeUsedROM;
    }

    public void setHomeUsedROM(String homeUsedROM) {
        this.homeUsedROM = homeUsedROM;
    }

    public String getUsedROM() {
        return usedROM;
    }

    public void setUsedROM(String usedROM) {
        this.usedROM = usedROM;
    }

    public String getPercentageROM() {
        return percentageROM;
    }

    public void setPercentageROM(String percentageROM) {
        this.percentageROM = percentageROM;
    }

    public String getPercentageRAM() {
        return percentageRAM;
    }

    public void setPercentageRAM(String percentageRAM) {
        this.percentageRAM = percentageRAM;
    }
    public String getDoneButtonPressedTime() {
        return doneButtonPressedTime;
    }

    public void setDoneButtonPressedTime(String doneButtonPressedTime) {
        this.doneButtonPressedTime = doneButtonPressedTime;
    }

}
