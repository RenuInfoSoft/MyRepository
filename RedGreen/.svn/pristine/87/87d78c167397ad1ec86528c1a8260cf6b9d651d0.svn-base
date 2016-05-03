package com.unfoldlabs.redgreen.db;

import java.io.IOException;

import com.unfoldlabs.redgreen.db.SavingDatabaseHelper.DBListener;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Database adapter that use the DatabaseHelper with extra query and insert
 * methods
 * 
 * HowTo: Call getInstance() then open to use this adapter
 * 
 * Custom:to custom the adapter and write your layer of methods extend this
 * adapter in your project
 * 
 * Important: make sure to call open() method at least once in your app
 */

public class SavingDatabaseAdapter {

	private static SavingDatabaseAdapter instance;
	private static Cursor cursor;
	private SavingDatabaseHelper dbHelper;
	private SQLiteDatabase db;

	/**
	 * Get instance from database adapter
	 * 
	 * @return DatabaseAdapter instance
	 */
	public static SavingDatabaseAdapter getInstance() {
		if (instance == null)
			instance = new SavingDatabaseAdapter();
		return instance;
	}

	/**
	 * Check if the database is already opened
	 * 
	 * @return true if open
	 */
	public boolean isOpen() {
		return dbHelper != null && db != null && db.isOpen();
	}

	/**
	 * Open and create the database. Make sure to call it at least once with
	 * each adapter instance
	 * 
	 * @throws IOException
	 */
	public void open(SavingDbConfig config) throws IOException {
		open(config, null);
	}

	public void open(SavingDbConfig config, DBListener listener)
			throws IOException {
		if (dbHelper == null) {
			dbHelper = new SavingDatabaseHelper(config);
			if (listener != null)
				dbHelper.setListener(listener);
			dbHelper.createDatabase();
		}

		if (db == null || !db.isOpen()) {
			db = dbHelper.getWritableDatabase();
			dbHelper.notifyDatabaseOepend();
		}
	}

	/** close database helper */
	public void close() {
		if (db != null) {
			db.close();
		}
		if (dbHelper != null) {
			dbHelper.close();
			dbHelper = null;
		}
	}

	/**
	 * Fetch specific rows from received table
	 * 
	 * @param String
	 *            table name
	 * @param String
	 *            field declare which rows to return
	 * @param String
	 *            [] you may include ? in selection which will be replaced with
	 *            values in this array
	 * @param String
	 *            order by column
	 * @param String
	 *            name of columns to return
	 * @return Cursor object positioned before the first object, not that
	 *         cursors are not synchronized
	 */
	public Cursor fetchDataWhere(String tableName, String selection,
			String keyName, String orderby, String keyBvalue) {

try{
	cursor = db.query(tableName, null, null, null, null, null, null);
}catch(NullPointerException e){
	e.printStackTrace();
}
		return cursor;
	}

	public Cursor fetchContacts(String query) {
		try {
			cursor = db.rawQuery(query, null);
			return cursor;
		}catch (NullPointerException e){

		}
		return null;
	}

	/**
	 * Insert data into received table
	 * 
	 * @param String
	 *            table name
	 * @param ContentValues
	 * @return number of effected fields
	 */
	public long insert(String tableName, ContentValues rowValues) {
		return insert(tableName, rowValues, null);

	}

	/**
	 * Insert data into received table
	 * 
	 * @param String
	 *            table name
	 * @param ContentValues
	 * @param String
	 *            replace any null field with received value
	 * @return number of effected fields
	 */
	public long insert(String tableName, ContentValues rowValues,
			String replaceNullWith) {
		if (db == null) {
			return 0;
		}
		return db.insert(tableName, replaceNullWith, rowValues);
	}

	/**
	 * Update fields for received table
	 * 
	 * @param String
	 *            table name
	 * @param ContentValues
	 *            (column names to return)
	 * @param String
	 *            field declare which rows to return
	 * @param String
	 *            [] you may include ? in selection which will be replaced with
	 *            values in this array
	 * @return number of effected fields
	 */
	public int update(String tableName, ContentValues rowValues,
			String selection, String[] where) {
		return db.update(tableName, rowValues, selection, where);
	}

	/**
	 * updating cleared cache data, RAM cleaned into table
	 * @param rowId
	 * @param cleared
	 * @param updated
	 * @param ramClean
	 * @return
	 */
	public boolean updateCache(long rowId, int cleared, String updated,
			int ramClean) {
		ContentValues args = new ContentValues();
		args.put(SavingDBAdapter.CACHE_TABLE.CLEARED_CACHE_DATA, cleared);
		args.put(SavingDBAdapter.CACHE_TABLE.UPDATED_TD, updated);
		args.put(SavingDBAdapter.CACHE_TABLE.RAM_CLEANED, ramClean);
		return db.update(SavingDBAdapter.CACHE_TABLE.TABLE_NAME, args,
				SavingDBAdapter.CACHE_TABLE.COL_ID + "=" + rowId, null) > 0;
	}

	/**
	 * updating rate count into table
	 * @param rowId
	 * @param count
	 * @return
	 */
	public boolean updateRateCount(long rowId, int count) {
		ContentValues args = new ContentValues();
		args.put(SavingDBAdapter.RATE_TABLE.COUNT, count);
		return db.update(SavingDBAdapter.RATE_TABLE.TABLE_NAME, args,
				SavingDBAdapter.RATE_TABLE.COL_ID + "=" + rowId, null) > 0;
	}

	/**
	 * Delete fields for received table
	 * 
	 * @param String
	 *            table name
	 * @param String
	 *            field declare which rows to return
	 * @param String
	 *            [] you may include ? in selection which will be replaced with
	 *            values in this array
	 * @return number of effected fields
	 */
	public int delete(String tableName, String selection, String[] where) {
		return db.delete(tableName, selection, where);
	}

	public void setListener(DBListener listener) {
		if (dbHelper != null)
			dbHelper.setListener(listener);
	}
}
