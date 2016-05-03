package com.unfoldlabs.redgreen.utilty;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.unfoldlabs.redgreen.global.ApplicationData;
import com.unfoldlabs.redgreen.model.AnalyticalsInfo;
import com.unfoldlabs.redgreen.model.AppsInfo;

public class JsonUtil {
	
	/**
	 * Method to convert java parameters to JsonObject
	 * @param params
	 * @return jsonObject
	 */
	public static JsonObject toJSon(AnalyticalsInfo params) {

		/**
		 *  Here we convert Java Object to JSON 
		 */
		JsonObject jsonObj = new JsonObject();
		//final ApplicationData applicationData = new ApplicationData();

		/**
		 * adding parameters to json object
		 */
		jsonObj.addProperty("simId", params.getSimId());
        jsonObj.addProperty("imei", params.getImei());
		jsonObj.addProperty("deviceSoftwareVersion", params.getDeviceSoftwareVersion());
		jsonObj.addProperty("deviceName", params.getDeviceSoftwareVersion());
		jsonObj.addProperty("deviceModel",  params.getDeviceModel());
		jsonObj.addProperty("manufacturer",  params.getManufacturer());
		jsonObj.addProperty("internalStorage",  params.getInternalStorage());
		jsonObj.addProperty("ram",  params.getRam());
		jsonObj.addProperty("versionRelease",  params.getVersionRelease());
		jsonObj.addProperty("sdk",  params.getSdk());
		jsonObj.addProperty("osVersion",  params.getOsVersion());
		jsonObj.addProperty("deviceId",  params.getDeviceId());
		jsonObj.addProperty("hardware",  params.getHardware());
		jsonObj.addProperty("deviceInfo",  params.getDeviceInfo());
		jsonObj.addProperty("serialNumber",  params.getSerialNumber());
		jsonObj.addProperty("version",  params.getVersion());
		jsonObj.addProperty("versionReleaseDate",  params.getVersionReleaseDate());
		jsonObj.addProperty("macAddress",  params.getMacAddress());
		jsonObj.addProperty("blutoothAddress",  params.getBlutoothAddress());
		jsonObj.addProperty("rgAppVersion", params.getRedegreenAppversion());
		if(null != params.getDeviceSoftwareVersion()){
			jsonObj.addProperty("deviceSoftwareVersion", params.getDeviceSoftwareVersion());
		}else{
			jsonObj.addProperty("deviceSoftwareVersion", " ");
		}


		if(null != params.getDeviceName()){
			jsonObj.addProperty("deviceName", params.getDeviceName());
		}else{
			jsonObj.addProperty("deviceName", " ");
		}

		if(null != params.getDeviceModel()){
			jsonObj.addProperty("deviceModel", params.getDeviceModel());
		}else{
			jsonObj.addProperty("deviceModel", " ");
		}
		if(null != params.getManufacturer()){
			jsonObj.addProperty("manufacturer", params.getManufacturer());
		}else{
			jsonObj.addProperty("manufacturer", " ");
		}
		if(null != params.getInternalStorage()){
			jsonObj.addProperty("internalStorage", params.getInternalStorage());
		}else{
			jsonObj.addProperty("internalStorage", " ");
		}
		if(null != params.getRam()){
			jsonObj.addProperty("ram", params.getRam());
		}else{
			jsonObj.addProperty("ram", " ");
		}


		if(null != params.getVersionRelease()){
			jsonObj.addProperty("versionRelease", params.getVersionRelease());
		}else{
			jsonObj.addProperty("versionRelease", " ");
		}

		if(null != params.getSdk()){
			jsonObj.addProperty("sdk", params.getSdk());
		}else{
			jsonObj.addProperty("sdk", " ");
		}

		if(null != params.getOsVersion()){
			jsonObj.addProperty("osVersion", params.getOsVersion());
		}else{
			jsonObj.addProperty("osVersion", " ");
		}

		if(null != params.getDeviceId()){
			jsonObj.addProperty("deviceId", params.getDeviceId());
		}else{
			jsonObj.addProperty("deviceId", " ");
		}


		if(null != params.getHardware()){
			jsonObj.addProperty("hardware", params.getHardware());
		}else{
			jsonObj.addProperty("hardware", " ");
		}


		if(null != params.getDeviceInfo()){
			jsonObj.addProperty("deviceInfo", params.getDeviceInfo());
		}else{
			jsonObj.addProperty("deviceInfo", " ");
		}


		if(null != params.getSerialNumber()){
			jsonObj.addProperty("serialNumber", params.getSerialNumber());
		}else{
			jsonObj.addProperty("serialNumber", " ");
		}

		if(null != params.getVersion()){
			jsonObj.addProperty("version", params.getVersion());
		}else{
			jsonObj.addProperty("version", " ");
		}


		if(null != params.getVersionReleaseDate()){
			jsonObj.addProperty("versionReleaseDate", params.getVersionReleaseDate());
		}else{
			jsonObj.addProperty("versionReleaseDate", " ");
		}


		if(null != params.getMacAddress()){
			jsonObj.addProperty("macAddress", params.getMacAddress());
		}else{
			jsonObj.addProperty("macAddress", " ");
		}


		if(null != params.getBlutoothAddress()){
			jsonObj.addProperty("blutoothAddress", params.getBlutoothAddress());
		}else{
			jsonObj.addProperty("blutoothAddress", " ");
		}



		JsonObject jsonAdd = new JsonObject(); // we need another object to store the address

		if(null !=  params.getAddress().getAddress()){
			jsonAdd.addProperty("country",  params.getAddress().getAddress());
		}else{
			jsonAdd.addProperty("country", " ");
		}



		if(null !=  params.getAddress().getCity()){
			jsonAdd.addProperty("city",  params.getAddress().getCity());
		}else{
			jsonAdd.addProperty("city", " ");
		}


		if(null !=  params.getAddress().getState()){
			jsonAdd.addProperty("state",  params.getAddress().getState());
		}else{
			jsonAdd.addProperty("state", " ");
		}

		if(null !=  params.getAddress().getLat()){
			jsonAdd.addProperty("latitude",  params.getAddress().getLat());
		}else{
			jsonAdd.addProperty("latitude", " ");
		}

		if(null !=  params.getAddress().getLon()){
			jsonAdd.addProperty("longitude",  params.getAddress().getLon());
		}else{
			jsonAdd.addProperty("longitude", " ");
		}

		if(params.getAddress().getZipCode()>0){
			jsonAdd.addProperty("postalCode",  params.getAddress().getZipCode());
		}else{
			jsonAdd.addProperty("postalCode", 0);
		}

		/**
		 *  We add the object to the main object
		 */
		jsonObj.add("address", jsonAdd);

		/** and finally we add the phone number
		 In this case we need a json array to hold the java list
		 */
		JsonArray jsonArr = new JsonArray();



		for (AppsInfo appsInfo : params.getApps() ) {
			JsonObject pnObj = new JsonObject();

			/**
			 * adding properties to pnObj
			 */
			if(null !=  appsInfo.getPackageName()){
				pnObj.addProperty("packageName",  appsInfo.getPackageName());
			}else{
				pnObj.addProperty("packageName", "");
			}

			if(null !=  appsInfo.getVersionName()){
				pnObj.addProperty("versionName",  appsInfo.getVersionName());
			}else{
				pnObj.addProperty("versionName", "");
			}

			if(null !=  appsInfo.getVersionCode()){
				pnObj.addProperty("versionCode",  appsInfo.getVersionCode());
			}else{
				pnObj.addProperty("versionCode", "");
			}


			if(null !=  appsInfo.getInstalledTime()){
				pnObj.addProperty("installedTime",  appsInfo.getInstalledTime());
			}else{
				pnObj.addProperty("installedTime", "");
			}


			if(null !=  appsInfo.getUpdatedTime()){
				pnObj.addProperty("updatedTime",  appsInfo.getUpdatedTime());
			}else{
				pnObj.addProperty("updatedTime", "");
			}


			if(null !=  appsInfo.getLabel()){
				pnObj.addProperty("label",  appsInfo.getLabel());
			}else{
				pnObj.addProperty("label", " ");
			}

			//pnObj.addProperty("appSize", appsInfo.getAppSize());
			pnObj.addProperty("pid", appsInfo.getPid());
			jsonArr.add(pnObj);
		}

		jsonObj.add("apps", jsonArr);
		//Extra fields
		jsonObj.addProperty("homeTotalROM",ApplicationData.getInstance().getInstance().getTotalROM());
		//	jsonObj.addProperty("StorageROM",ApplicationData.getInstance().getStorage());
		jsonObj.addProperty("homeUsedROM",ApplicationData.getInstance().getHomeUsedROM());
		jsonObj.addProperty("totalROM",ApplicationData.getInstance().getTotalROM());
		jsonObj.addProperty("usedROM",ApplicationData.getInstance().getUsedROM());
		jsonObj.addProperty("percentageROM",ApplicationData.getInstance().getPercentageROM());
		jsonObj.addProperty("totalRAM",ApplicationData.getInstance().getTotalROM());
		jsonObj.addProperty("usedRAM",ApplicationData.getInstance().getUsedRAM());
		jsonObj.addProperty("percentageRAM",ApplicationData.getInstance().getPercentageRAM());
		jsonObj.addProperty("appCacheUsage",ApplicationData.getInstance().getAppCacheUsage());
		jsonObj.addProperty("menuItemSelected",ApplicationData.getInstance().getMenuItemSelected());
		jsonObj.addProperty("navigationMenuEnteredModule",ApplicationData.getInstance().getNavigationMenuEnteredModule());
		jsonObj.addProperty("exitedMenuScreen",ApplicationData.getInstance().getExitedMenuScreen());
		jsonObj.addProperty("memoryRAMFlag",ApplicationData.getInstance().getMemoryRAMFlag());
		jsonObj.addProperty("browserHistoryFlag",ApplicationData.getInstance().getBrowserHistoryFlag());
		jsonObj.addProperty("clipBoardDataFlag",ApplicationData.getInstance().getClipBoardDataFlag());
		jsonObj.addProperty("residualJunkFilesFlag",ApplicationData.getInstance().getResidualJunkFilesFlag());
		jsonObj.addProperty("junkFiles",ApplicationData.getInstance().getJunkFiles());
		jsonObj.addProperty("noOfbrowserHistoryItems",ApplicationData.getInstance().getNoOfbrowserHistoryItems());
		jsonObj.addProperty("browserHistorySize",ApplicationData.getInstance().getBrowserHistorySize());
		jsonObj.addProperty("noOfEndingBackgroundProcesses",ApplicationData.getInstance().getNoOfEndingBackgroundProcesses());
		jsonObj.addProperty("endingBackgroundProcessesSize",ApplicationData.getInstance().getEndingBackgroundProcessesSize());
		jsonObj.addProperty("noOfStartupBackgroundProcesses",ApplicationData.getInstance().getNoOfStartupBackgroundProcesses());
		jsonObj.addProperty("startupBackgroundProcessesSize",ApplicationData.getInstance().getStartupBackgroundProcessesSize());
		jsonObj.addProperty("junkFilesSize",ApplicationData.getInstance().getJunkFilesSize());
		jsonObj.addProperty("noOfDeletedbrowserHistoryItems",ApplicationData.getInstance().getNoOfDeletedbrowserHistoryItems());
		jsonObj.addProperty("browserHistoryDeletedSavedMemorySize",ApplicationData.getInstance().getBrowserHistoryDeletedSavedMemorySize());
		jsonObj.addProperty("noOfEndingBackgroundProcessesItems",ApplicationData.getInstance().getNoOfEndingBackgroundProcessesItems());
		jsonObj.addProperty("endingBackgroundProcessesSavedMemorySize",ApplicationData.getInstance().getEndingBackgroundProcessesSavedMemorySize());
		jsonObj.addProperty("noOfStartupProcessesItems",ApplicationData.getInstance().getNoOfStartupProcessesItems());
		jsonObj.addProperty("endingStartupProcessesSavedMemorySize",ApplicationData.getInstance().getEndingStartupProcessesSavedMemorySize());
		jsonObj.addProperty("storageTotalSaved",ApplicationData.getInstance().getStorageTotalSaved());
		jsonObj.addProperty("memoryTotalSaved",ApplicationData.getInstance().getMemoryTotalSaved());
		jsonObj.addProperty("doneButtonPressedTime",ApplicationData.getInstance().getDoneButtonPressedTime());
		jsonObj.addProperty("speedBoosterTimeSelected",ApplicationData.getInstance().getSpeedBoosterTimeSelected());
		jsonObj.addProperty("defaultMemorySize",ApplicationData.getInstance().getDefaultMemorySize());
		jsonObj.addProperty("noOfAppsLocked",ApplicationData.getInstance().getNoOfAppsLocked());
		jsonObj.addProperty("appsLocked",ApplicationData.getInstance().getAppsLocked());
		jsonObj.addProperty("changeLockEmail",ApplicationData.getInstance().getChangeLockEmail());
		jsonObj.addProperty("cpuHeatInitially",ApplicationData.getInstance().getCpuHeatInitially());
		jsonObj.addProperty("screenTimeoutFrom",ApplicationData.getInstance().getScreenTimeoutFrom());
		jsonObj.addProperty("screenTimeoutTo",ApplicationData.getInstance().getScreenTimeoutTo());
		jsonObj.addProperty("screenTimeoutPercentage",ApplicationData.getInstance().getScreenTimeoutPercentage());
		jsonObj.addProperty("screenBrightnessFrom",ApplicationData.getInstance().getScreenBrightnessFrom());
		jsonObj.addProperty("screenBrightnessPercentage",ApplicationData.getInstance().getScreenBrightnessPercentage());
		jsonObj.addProperty("noOfProcessesShuttingDown",ApplicationData.getInstance().getNoOfProcessesShuttingown());
		jsonObj.addProperty("shuttingDownMemorySaved",ApplicationData.getInstance().getShuttingDownMemorySaved());
		jsonObj.addProperty("shuttingDownPercentage",ApplicationData.getInstance().getShuttingDownPercentage());
		jsonObj.addProperty("cpuTempLastly",ApplicationData.getInstance().getCpuTempLastly());
		jsonObj.addProperty("displayTempFrom",ApplicationData.getInstance().getDisplayTempFrom());
		jsonObj.addProperty("displayTempTo",ApplicationData.getInstance().getDisplayTempTo());
		jsonObj.addProperty("appInstalledStartTime",ApplicationData.getInstance().getAppInstalledStartTime());
		jsonObj.addProperty("appInstalledEndTime",ApplicationData.getInstance().getAppInstalledEndTime());
		jsonObj.addProperty("appInstalledName",ApplicationData.getInstance().getAppInstalledName());
		jsonObj.addProperty("appInstalledSize",ApplicationData.getInstance().getAppInstalledSize());
		jsonObj.addProperty("appInstalledDate",ApplicationData.getInstance().getAppInstalledDate());
		jsonObj.addProperty("dndappInstalledStartTime",ApplicationData.getInstance().getDndappInstalledStartTime());
		jsonObj.addProperty("dndappInstalledEndTime",ApplicationData.getInstance().getDndappInstalledEndTime());
		jsonObj.addProperty("dndappInstalledName",ApplicationData.getInstance().getDndappInstalledName());
		jsonObj.addProperty("dndappInstalledSize",ApplicationData.getInstance().getDndappInstalledSize());
		jsonObj.addProperty("dndappInstalledDate",ApplicationData.getInstance().getDndappInstalledDat());
		jsonObj.addProperty("daysNotUsed",ApplicationData.getInstance().getDaysNotUsed());



		return jsonObj;



	}

}
