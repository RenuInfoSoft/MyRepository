package com.unfoldlabs.redgreen.utilty;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;


import com.unfoldlabs.redgreen.R;
import com.unfoldlabs.redgreen.receivers.FridayReceiver;
import com.unfoldlabs.redgreen.receivers.MondayReceiver;
import com.unfoldlabs.redgreen.receivers.SaturdayReceiver;
import com.unfoldlabs.redgreen.receivers.SundayReceiver;
import com.unfoldlabs.redgreen.receivers.ThursdayReceiver;
import com.unfoldlabs.redgreen.receivers.TuesdayReceiver;
import com.unfoldlabs.redgreen.receivers.WednsdayReceiver;

public class Bean {
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;

    public static void setAlarm(Context context) {

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent sundayIntent = new Intent(context, SundayReceiver.class);
        PendingIntent sundayPendingIntent = PendingIntent.getBroadcast(context, 2243, sundayIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Intent mondayIntent = new Intent(context, MondayReceiver.class);
        PendingIntent mondayPendingIntent = PendingIntent.getBroadcast(context, 3432, mondayIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Intent tuesdayIntent = new Intent(context, TuesdayReceiver.class);
        PendingIntent tuesdayPendingIntent = PendingIntent.getBroadcast(context, 24243, tuesdayIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Intent wednesdayIntent = new Intent(context, WednsdayReceiver.class);
        PendingIntent wednesdayPendingIntent = PendingIntent.getBroadcast(context, 5243, wednesdayIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Intent thursdayIntent = new Intent(context, ThursdayReceiver.class);
        PendingIntent thursdayPendingIntent = PendingIntent.getBroadcast(context, 24243, thursdayIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Intent fridayIntent = new Intent(context, FridayReceiver.class);
        PendingIntent fridayPendingIntent = PendingIntent.getBroadcast(context, 2311, fridayIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Intent saturdayIntent = new Intent(context, SaturdayReceiver.class);
        PendingIntent saturdayPendingIntent = PendingIntent.getBroadcast(context, 2343, saturdayIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.cancel(sundayPendingIntent);
        alarmManager.cancel(mondayPendingIntent);
        alarmManager.cancel(tuesdayPendingIntent);
        alarmManager.cancel(wednesdayPendingIntent);
        alarmManager.cancel(thursdayPendingIntent);
        alarmManager.cancel(fridayPendingIntent);
        alarmManager.cancel(saturdayPendingIntent);

        sharedPreferences = context.getSharedPreferences("MY_SHARED_PREF", Context.MODE_PRIVATE);

        editor = sharedPreferences.edit();
        sharedPreferences = context.getSharedPreferences("MY_SHARED_PREF",
                Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        int weekSetting = sharedPreferences.getInt(
                context.getString(R.string.dialog_txt_sunday), 7);
        int Hour = sharedPreferences.getInt(
                context.getString(R.string.Hour), 22);
        int Minute = sharedPreferences.getInt(
                context.getString(R.string.Minute), 00);
        boolean autoCleanMemorySwitch = sharedPreferences.getBoolean(
                context.getResources().getString(R.string.cleanmemoryswitch), true);


        Calendar calendar = Calendar.getInstance();
        Calendar calNow = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, Hour);
        calendar.set(Calendar.MINUTE, Minute);
        calendar.set(Calendar.SECOND, 00);

        final int nowDay = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        final int nowHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        final int nowMinute = Calendar.getInstance().get(Calendar.MINUTE);



        if (autoCleanMemorySwitch && sharedPreferences.getBoolean("sundayFlag", true)) {

            if (sharedPreferences.getBoolean("SUNDAY", false) && calNow.get(Calendar.DAY_OF_WEEK) >= Calendar.SUNDAY && !(Calendar.SUNDAY == nowDay && Hour < nowHour)
                    && !(Calendar.SUNDAY == nowDay && Hour == nowHour && Minute <= nowMinute))

            {

                calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);


                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sundayPendingIntent);

            }
            if (sharedPreferences.getBoolean("MONDAY", false) && calNow.get(Calendar.DAY_OF_WEEK) <= Calendar.MONDAY && !(Calendar.MONDAY == nowDay && Hour < nowHour)
                    && !(Calendar.MONDAY == nowDay && Hour == nowHour && Minute <= nowMinute)) {
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), mondayPendingIntent);
            }
            if (sharedPreferences.getBoolean("TUESDAY", false) && calNow.get(Calendar.DAY_OF_WEEK) <= Calendar.TUESDAY && !(Calendar.TUESDAY == nowDay && Hour < nowHour)
                    && !(Calendar.TUESDAY == nowDay && Hour == nowHour && Minute <= nowMinute))

            {
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);


                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), tuesdayPendingIntent);

            }
            if (sharedPreferences.getBoolean("WEDNESDAY", false) && calNow.get(Calendar.DAY_OF_WEEK) <= Calendar.WEDNESDAY && !(Calendar.WEDNESDAY == nowDay && Hour < nowHour)
                    && !(Calendar.WEDNESDAY == nowDay && Hour == nowHour && Minute <= nowMinute))


            {
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);


                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), wednesdayPendingIntent);
            }
            if (sharedPreferences.getBoolean("THURSDAY", false) && calNow.get(Calendar.DAY_OF_WEEK) <= Calendar.THURSDAY && !(Calendar.THURSDAY == nowDay && Hour < nowHour)
                    && !(Calendar.THURSDAY == nowDay && Hour == nowHour && Minute <= nowMinute))

            {
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);

                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), thursdayPendingIntent);

            }


            if (sharedPreferences.getBoolean("FRIDAY", false) && calNow.get(Calendar.DAY_OF_WEEK) <= Calendar.FRIDAY && !(Calendar.FRIDAY == nowDay && Hour < nowHour)
                    && !(Calendar.FRIDAY == nowDay && Hour == nowHour && Minute <= nowMinute))

            {
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);


                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), fridayPendingIntent);
            }
            if (sharedPreferences.getBoolean("SATURDAY", true) && calNow.get(Calendar.DAY_OF_WEEK) <= Calendar.SATURDAY && !(Calendar.SATURDAY == nowDay && Hour < nowHour)
                    && !(Calendar.SATURDAY == nowDay && Hour == nowHour && Minute <= nowMinute))

            {
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);

                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), saturdayPendingIntent);
            }
        }

    }

}
