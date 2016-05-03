package com.unfoldlabs.redgreen.receiver;

import java.util.Calendar;
import java.util.List;

import com.unfoldlabs.redgreen.R;
import com.unfoldlabs.redgreen.model.AlarmModel;
import com.unfoldlabs.redgreen.model.CleanAppDB;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

@SuppressLint("CommitPrefEdits")
public class CleanAppReciever{
	private static SharedPreferences sharedPreferences;
	public static final String ID = "id";
	public static final String NAME = "name";
	public static final String TIME_HOUR = "timeHour";
	public static final String TIME_MINUTE = "timeMinute";
	public static final String TONE = "alarmTone";

	private static AlarmModel alarm;
	private static  List<CleanAppDB> list;
	private static PendingIntent pIntent;


	public static void setAlarms(Context context) {
		sharedPreferences = context.getSharedPreferences("MY_SHARED_PREF",
				Context.MODE_PRIVATE);
		sharedPreferences.edit();
		int repeatSetting = sharedPreferences.getInt(context.getString(R.string.repeat), 0);
		boolean autoCleanSwitch =sharedPreferences.getBoolean(context.getResources().getString(R.string.cleanappswitch),true);
		alarm = new AlarmModel();
		Calendar calendar = Calendar.getInstance();
		int day = calendar.get(Calendar.DAY_OF_WEEK);
		 pIntent = createPendingIntent(context,repeatSetting, autoCleanSwitch);
		//Find next time to set
		final int nowDay = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
		//Else check if it's earlier in the week
		if (autoCleanSwitch) {
			updateModelFromLayout(day, true);
			for (int dayOfWeek = Calendar.SUNDAY; dayOfWeek <= Calendar.SATURDAY; ++dayOfWeek) {
				if (alarm.getRepeatingDay(dayOfWeek - 1) && dayOfWeek <= nowDay && alarm.repeatWeekly) {
					calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek);
					calendar.add(Calendar.WEEK_OF_YEAR, 1);
					setAlarm(context, calendar, pIntent,autoCleanSwitch);
				}//break;
			}
		}
		else{
			CancelAlarm(context, calendar, pIntent);
		}
	}

	@SuppressLint("NewApi")
	private static void setAlarm(Context context, Calendar calendar, PendingIntent pIntent, boolean autoCleanSwitch) {
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
			alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pIntent);
		} else {
			alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pIntent);
		}
	}
	public static void CancelAlarm(Context context, Calendar calendar, PendingIntent pIntent)
    {
      AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pIntent);
    }

	private static PendingIntent createPendingIntent(Context context,Integer repeat, boolean autoCleanSwitch) {
		if(autoCleanSwitch){
			Intent alarmIntent = new Intent(context, CleanReciever.class);
			PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
			return pendingIntent;
		}else {
			return null;
		}
	}
	private static void updateModelFromLayout(int day, boolean status) {		
		alarm.repeatWeekly = true;	
		switch (day) {
		case 1:
			alarm.setRepeatingDay(AlarmModel.SUNDAY, true);	
			break;
		case 2:
			alarm.setRepeatingDay(AlarmModel.MONDAY, true);	
			break;
		case 3:
			alarm.setRepeatingDay(AlarmModel.TUESDAY, true);	
			break;
		case 4:
			alarm.setRepeatingDay(AlarmModel.WEDNESDAY, true);	
			break;
		case 5:
			alarm.setRepeatingDay(AlarmModel.THURSDAY, true);	
			break;
		case 6:
			alarm.setRepeatingDay(AlarmModel.FRDIAY, true);	
			break;
		case 7:
			alarm.setRepeatingDay(AlarmModel.SATURDAY, true);	
			break;
		default:
			break;
		}
		alarm.isEnabled = status;
	}
	public static void setList(List<CleanAppDB> list) {
		CleanAppReciever.list = list;		
	}
}
