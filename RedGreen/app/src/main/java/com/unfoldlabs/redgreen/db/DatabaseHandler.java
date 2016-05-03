
package com.unfoldlabs.redgreen.db;

import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.unfoldlabs.redgreen.global.AppData;
import com.unfoldlabs.redgreen.global.ApplicationData;
import com.unfoldlabs.redgreen.log.Applog;
import com.unfoldlabs.redgreen.model.CleanAppDB;
import com.unfoldlabs.redgreen.utilty.Utility;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

	/**
	 * Database Version
	 */
	private static final int DATABASE_VERSION = 3;

	/**
	 * Database Name
	 */
	private static final String DATABASE_NAME = "RedGreen.sqlite";

	/**
	 * Contacts table name
	 */
	private static final String TABLE_CLEAN_APPS = "tbl_clean_app";

	/**
	 * Contacts Table Columns names
	 */
	private static final String KEY_ID = "id";
	private static final String PACKAGE_NAME = "package_name";
	private static final String DATE_TIME = "date_time";
	private static final String DONT_DELETE_APPS = "dont_delete_apps";
	private static final String APP_SIZE = "app_size";

	private boolean isSelected;

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	/**
	 * Creating Tables
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_CLEAN_APP_TABLE = "CREATE TABLE " + TABLE_CLEAN_APPS
				+ "(" + KEY_ID + " INTEGER PRIMARY KEY," + PACKAGE_NAME
				+ " TEXT," + DATE_TIME + " TEXT," + DONT_DELETE_APPS
				+ " INTEGER DEFAULT 0," +APP_SIZE +" TEXT " + ")";
		db.execSQL(CREATE_CLEAN_APP_TABLE);
	}

	/**
	 * Upgrading database
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		/**
		 * Drop older table if existed
		 */
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CLEAN_APPS);

		/**
		 * Create tables again
		 */
		onCreate(db);
	}

	public void dontDeleteApps(int dontDeleteApp, String appName) {
		ApplicationData.getInstance().setDndappInstalledStartTime(""+Utility.getDefaultDate());
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(DONT_DELETE_APPS, dontDeleteApp);

		db.update(TABLE_CLEAN_APPS, values, PACKAGE_NAME + " = ?",
				new String[]{String.valueOf(appName)});
		ApplicationData.getInstance().setDndappInstalledEndTime("" + Utility.getDefaultDate());
		ApplicationData.getInstance().setDndappInstalledDat("" + Utility.getDefaultDate());
		ApplicationData.getInstance().setDndappInstalledName(""+appName);
	}


	/**
	 * Inserting into db
	 *
	 * @param appDb
	 * @param isUpdate
	 */
	public void insertCleanApp(CleanAppDB appDb, boolean isUpdate) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();

		/**
		 * appDb Name
		 */
		values.put(PACKAGE_NAME, appDb.getPackageName());
		/**
		 * appDb Phone
		 */
		if(null != appDb.getDateTime()){
			values.put(DATE_TIME, appDb.getDateTime());
		}else{
			values.put(DATE_TIME, "");
		}


		values.put(APP_SIZE, appDb.getApp_size());

		/**
		 * Inserting Row
		 */
		if (isAppPresent(appDb.getPackageName())) {
			if (isUpdate)
				upDateColumn(appDb.getPackageName(), Utility.getDefaultDate());
		} else {
			db.insert(TABLE_CLEAN_APPS, null, values);
		}

		/**
		 * Closing database connection
		 */
		db.close();
	}

	public boolean isAppPresent(String appName) {
		boolean isAppPresent = false;

		String selectQuery = "SELECT  * FROM " + TABLE_CLEAN_APPS + " WHERE "
				+ PACKAGE_NAME + " = '" + appName + "'";

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = null;
		try {
			cursor = db.rawQuery(selectQuery, null);
			if (null != cursor && cursor.getCount() > 0) {
				cursor.moveToFirst();
				isAppPresent = true;
				return isAppPresent;
			}
		} catch (Exception e) {
			Applog.logString("Exception on retriving" + e.getMessage());
		} finally {
			if (cursor != null) {
				cursor.close();
				cursor = null;
			}
		}
		return isAppPresent;

	}

	public List<CleanAppDB> getAllApps(PackageManager packageManager) {

		List<CleanAppDB> contactList = new ArrayList<CleanAppDB>();

		/**
		 * Select All Query
		 */
		String selectQuery = "SELECT * FROM tbl_clean_app where dont_delete_apps = 0";

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		/**
		 * looping through all rows and adding to list
		 */
		if (cursor.moveToNext()) {
			do {

				try {
					@SuppressWarnings("unused")
					String label = (String) packageManager
							.getApplicationLabel(packageManager
									.getApplicationInfo((cursor.getString(1)),
											PackageManager.GET_META_DATA));
					CleanAppDB appDb = new CleanAppDB(isSelected);
					appDb.setPackageName(cursor.getString(1));
					appDb.setDateTime(cursor.getString(2));
					appDb.setApp_size(cursor.getString(4));

					/**
					 * Adding contact to list
					 */

					contactList.add(appDb);

				} catch (NameNotFoundException e) {
					deleteApp(cursor.getString(1));
					e.printStackTrace();
				}

			} while (cursor.moveToNext());
		}

		AppData.getInstance().setAppManagerInstalledAppsList(contactList);
		return contactList;
	}

	public List<CleanAppDB> getDontDeleteApps(PackageManager packageManager) {

		List<CleanAppDB> contactList = new ArrayList<CleanAppDB>();

		/**
		 * Select All Query
		 */
		String selectQuery = "SELECT * FROM tbl_clean_app where dont_delete_apps = 1";

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		/**
		 * looping through all rows and adding to list
		 */
		if (cursor.moveToFirst()) {
			do {

				try {
					@SuppressWarnings("unused")
					String label = (String) packageManager
							.getApplicationLabel(packageManager
									.getApplicationInfo((cursor.getString(1)),
											PackageManager.GET_META_DATA));
					CleanAppDB appDb = new CleanAppDB(isSelected);
					appDb.setPackageName(cursor.getString(1));
					appDb.setDateTime(cursor.getString(2));
					appDb.setApp_size(cursor.getString(4));
					/**
					 * Adding contact to list
					 */
					contactList.add(appDb);

				} catch (NameNotFoundException e) {
					deleteApp(cursor.getString(1));
					e.printStackTrace();
				}

			} while (cursor.moveToNext());
		}
		AppData.getInstance().setDoNotDeleteAppsList(contactList);
		return contactList;
	}

	/**
	 * Updating single appDb
	 *
	 * @param packagename
	 */

	/**
	 * Deleting app from db
	 *
	 * @param packagename
	 */
	public void deleteApp(String packagename) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_CLEAN_APPS, PACKAGE_NAME + " = ?",
				new String[] { String.valueOf(packagename) });
		db.close();
	}

	public void deleteAllApps() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_CLEAN_APPS, null, null);
		db.close();
	}

	/**
	 * Getting appDbs Count
	 *
	 * @return
	 */
	public int getappDbsCount() {
		String countQuery = "SELECT  * FROM " + TABLE_CLEAN_APPS;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();

		/**
		 * Return count
		 */
		return cursor.getCount();
	}

	/**
	 * to get last used time of the app
	 *
	 * @param packagename
	 * @return
	 */
	public String getLastusedTime(String packagename) {

		String lastUsedTime = "";

		String selectQuery = "SELECT * FROM tbl_clean_app where package_name = '"
				+ packagename + "' ";

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			lastUsedTime = cursor.getString(2);
		}

		return lastUsedTime;

	}

	public void upDateColumn(String packageName, String date){
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues con = new ContentValues();
		con.put(DATE_TIME, Utility.getDefaultDate());
		db.update(TABLE_CLEAN_APPS, con, PACKAGE_NAME + " = ?",	new String[] { String.valueOf(packageName)});

		if(db != null)
			db.close();
	}
}