package com.unfoldlabs.redgreen.global;

import android.content.Context;

public class MethodsForRedGreen{
	public float getScreenBrightNessSavings(final int custScreenSettings , final int redGreenScreenSetting){

		final double MA_APROX_CONSUMPTION_PER_SCREEN_ON_FULL_BRIGHTNESS = 300.0;
		float savingsPercentage = 0.0f;
		if(custScreenSettings <= redGreenScreenSetting) {
			savingsPercentage = (float)(( redGreenScreenSetting - custScreenSettings ) / MA_APROX_CONSUMPTION_PER_SCREEN_ON_FULL_BRIGHTNESS) * 100;
		}else {
			savingsPercentage = (float)((custScreenSettings - redGreenScreenSetting) / MA_APROX_CONSUMPTION_PER_SCREEN_ON_FULL_BRIGHTNESS) * 100;
		}
		return savingsPercentage;
	}
	/** Blutooth **/
	public float getBluetoothBatterySavings(){

		final double MAH_APROX_CONSUMPTION_BLUETOOTH = 1.0;
		final double MA_CONSUMPTION_PER_DEVICE_HOUR = 170.0;
		float percentage = (float)(MAH_APROX_CONSUMPTION_BLUETOOTH / MA_CONSUMPTION_PER_DEVICE_HOUR) * 100;
		return percentage;
	}

	/** Gps **/

	public float getGPSBatterySavings(){

		final double MAH_APROX_CONSUMPTION_GPS_ON = 2.0;
		final double MA_CONSUMPTION_PER_DEVICE_HOUR = 170.0;
		float percentage = (float)(MAH_APROX_CONSUMPTION_GPS_ON / MA_CONSUMPTION_PER_DEVICE_HOUR) * 100;
		return percentage;
	}

	/** process battery Saving **/
	public double getProcessBatterySavings(final int totalNumOfProcess, Context context) {

		Object mPowerProfile_ = null;
		final float MA_CONSUMPTION_PER_PROCESS_HOUR = 0.25f;
		final float MA_CONSUMPTION_PER_DEVICE_HOUR = 170.0f;
		float percentage = 0.0f;
		final String P_P_C = "com.android.internal.os.PowerProfile";
		try {
			mPowerProfile_ = Class.forName(P_P_C)
					.getConstructor(Context.class).newInstance(context);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			//Total Battery capacity in mah
			//double Capacity = (double) Class.forName(P_P_C).getMethod("getAveragePower", java.lang.String.class).invoke(mPowerProfile_, "battery.capacity");

			//Battery Life in hours
			//		float totalBatteryLifeHours = (float)(Capacity / MA_CONSUMPTION_PER_DEVICE_HOUR);

			//.25 ma consumed by an average process per hour
			percentage = ( (totalNumOfProcess * MA_CONSUMPTION_PER_PROCESS_HOUR) / MA_CONSUMPTION_PER_DEVICE_HOUR) * 100 ;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return percentage;
	}
	/** ScreenTimeout **/
	public float getScreenTimeoutSavings(final float cstTimeOutSettingInMinutes, final float redGreenTimeOutSettingInMinutes){

		final float MA_CONSUMPTION_PER_DEVICE_HOUR = 170.0f;
		final float SCREEN_ALWAYS_ON = -1f;
		final float SCREEN_ALWAYS_ON_VALUE = 15.0f;
		float savingsPercentage = 0.0f;

		if(cstTimeOutSettingInMinutes == SCREEN_ALWAYS_ON){
			savingsPercentage = (SCREEN_ALWAYS_ON_VALUE / MA_CONSUMPTION_PER_DEVICE_HOUR) * 100;
		}else if(cstTimeOutSettingInMinutes <= redGreenTimeOutSettingInMinutes) {
			savingsPercentage = (( redGreenTimeOutSettingInMinutes - cstTimeOutSettingInMinutes ) / MA_CONSUMPTION_PER_DEVICE_HOUR) * 100;
		}else {
			savingsPercentage = ((cstTimeOutSettingInMinutes - redGreenTimeOutSettingInMinutes) / MA_CONSUMPTION_PER_DEVICE_HOUR) * 100;
		}
		return savingsPercentage;

	}

}