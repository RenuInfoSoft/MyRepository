package com.unfoldlabs.redgreen.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.StaleDataException;

import com.unfoldlabs.redgreen.model.CacheList;

import java.util.ArrayList;

public class SavingDBAdapter extends SavingDatabaseAdapter {
	public static String DataBase_NAME = "redgreen.sqlite";
	public static String DATABASE_PATH = "/data/data/com.unfoldlabs.redgreen/databases/redgreen.sqlite";
	public static String SDCARD_PATH = "sdcard/Redgreen";
	private static SavingDBAdapter instance;
	private Cursor cursor;
	public static int DEFAULT_VERSION = 3;
	public Context context;

	public static SavingDBAdapter getInstance() {
		if (instance == null)
			instance = new SavingDBAdapter();
		return instance;
	}

	/**
	 * getting cache from database to cursor object
	 *
	 * @return
	 */
	public Cursor getCache() {
		String query= "SELECT * from "+CACHE_TABLE.TABLE_NAME;
		cursor = fetchContacts(query);
		/*cursor = fetchDataWhere(CACHE_TABLE.TABLE_NAME, CACHE_TABLE.COL_ID,
				CACHE_TABLE.CLEARED_CACHE_DATA, CACHE_TABLE.UPDATED_TD, null);*/
		if (cursor != null) {
			cursor.moveToFirst();
		}
		if(cursor != null)
			cursor.close();
		return cursor;
	}

	/**
	 * adding cache values to cache_table
	 *
	 * @param cleared
	 * @param updated
	 * @param ramSize
	 */
	public void addCache(int cleared, String updated, int ramSize) {
		ContentValues rowValues = CACHE_TABLE.createContentValues(cleared,
				updated, ramSize);
		insert(CACHE_TABLE.TABLE_NAME, rowValues);
	}

	/**
	 * removing cache from cache_table
	 *
	 * @param id
	 */
	public void removeCache(long id) {
		String slection = CACHE_TABLE.COL_ID + "= ?";
		String where[] = new String[] { String.valueOf(id) };
		delete(CACHE_TABLE.TABLE_NAME, slection, where);
	}

	/**
	 * Table Information to unify using the fields from once place You can use
	 * the library without implementing this but make sure you always send the
	 * right table name and columns names
	 */

	public static class CACHE_TABLE {

		public final static String TABLE_NAME = "newcache";
		public final static String COL_ID = "sno";
		public final static String CLEARED_CACHE_DATA = "cleared";
		public final static String UPDATED_TD = "updated";
		public final static String RAM_CLEANED = "ram_clean";

		public static ContentValues createContentValues(int cleared,
														String updated, int ramSize) {
			ContentValues values = new ContentValues();
			values.put(CLEARED_CACHE_DATA, cleared);
			values.put(UPDATED_TD, updated);
			values.put(RAM_CLEANED, ramSize);
			return values;
		}
		/**
		 * method: create content values for cache count
		 */
	}

	public Cursor getRate() {
		cursor = fetchDataWhere(RATE_TABLE.TABLE_NAME, RATE_TABLE.COL_ID, null,
				RATE_TABLE.COUNT, null);
		if (cursor != null) {
			cursor.moveToFirst();
		}
		return cursor;
	}

	public Cursor getRateCount() {
		cursor = fetchDataWhere(RATE_TABLE.TABLE_NAME, RATE_TABLE.COL_ID, null,
				RATE_TABLE.COUNT, null);
		if (cursor != null) {
			cursor.moveToFirst();
		}
		return cursor;
	}

	public void addRateCount(int count) {
		ContentValues rowValues = RATE_TABLE.createContentValues(count);
		insert(RATE_TABLE.TABLE_NAME, rowValues);
	}

	public void removeRateCount(long id) {
		String slection = RATE_TABLE.COL_ID + "= ?";
		String where[] = new String[] { String.valueOf(id) };
		delete(RATE_TABLE.TABLE_NAME, slection, where);
	}

	public static class RATE_TABLE {
		public final static String TABLE_NAME = "rateus";
		public final static String COL_ID = "sno";
		public final static String COUNT = "count";

		public static ContentValues createContentValues(int count) {
			ContentValues values = new ContentValues();
			values.put(COUNT, count);
			return values;
		}
		/**
		 * method: create content values for rate count
		 */
	}

	public ArrayList<CacheList> getAllCache() {
		ArrayList<CacheList> cacheArrayList = new ArrayList<CacheList>();

		String sqlQuery = "SELECT * from "+CACHE_TABLE.TABLE_NAME +" WHERE "
				+ CACHE_TABLE.UPDATED_TD +" >= date('now','localtime', '-30 day')";
		try {
			Cursor cursor = fetchContacts(sqlQuery);
			/**
			 * looping through all rows and adding to list
			 */
			if (cursor.moveToFirst()) {
				do {
					CacheList cacheList = new CacheList();
					cacheList.setId(Integer.parseInt(cursor.getString(0)));
					cacheList.setCacheClean(cursor.getInt(1));
					cacheList.setUpdatedDate(cursor.getString(2));
					cacheList.setRamClean(cursor.getInt(3));
					/**
					 * Adding cacheList to list
					 */
					cacheArrayList.add(cacheList);

				} while (cursor.moveToNext());

			}
		}catch (StaleDataException e){
			e.printStackTrace();
		}
		/**
		 * return contact list
		 */
		return cacheArrayList;
	}

	public void open(String databaseName, int i, Context context) {
		DATABASE_PATH = databaseName;
		DEFAULT_VERSION = i;
		this.context = context;
	}
}
