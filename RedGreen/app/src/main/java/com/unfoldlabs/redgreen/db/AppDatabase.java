package com.unfoldlabs.redgreen.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.unfoldlabs.redgreen.interfaces.Constants;
import com.unfoldlabs.redgreen.log.Applog;
import com.unfoldlabs.redgreen.utilty.Utility;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * application database class['
 */
public class AppDatabase {

	private static Context mContext = null;
	private static AppDatabase database = null;
	private Object dblock = null;

	private static class DbHelper extends SQLiteOpenHelper {

		public DbHelper(Context context) {
			super(context, Constants.DB_NAME, null, DB_VERSION);
			mContext = context;
		}

		/**
		 * to create the db
		 */
		@Override
		public void onCreate(SQLiteDatabase db) {

		}

		/**
		 * upgrading the existing db
		 */
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		}

		private void copyDataBase() throws IOException {
			InputStream mInput = mContext.getAssets().open(Constants.DB_NAME);
			String mOut = Utility.getDbPath(mContext);
			FileOutputStream mOutput = new FileOutputStream(mOut);
			byte[] buffer = new byte[1024];
			int length;

			while ((length = mInput.read(buffer)) > 0) {
				mOutput.write(buffer, 0, length);
			}
			mOutput.flush();
			mOutput.close();
			mInput.close();
		}

		public SQLiteDatabase openDataBase() throws SQLException {
			boolean isExists = checkDataBase();

			if (isExists) {
				SQLiteDatabase mydb = getReadableDatabase();
				mydb.close();
				try {
					copyDataBase();
				} catch (Exception e) {
					Applog.logString("Exception OpenData base" + e.getMessage());
				}
			}
			return this.getWritableDatabase();
		}

		/**
		 * checking for database
		 *
		 * @return
		 */
		private boolean checkDataBase() {
			SQLiteDatabase tempdb = null;

			try {
				final String mypath = Utility.getDbPath(mContext);
				File dbFile = new File(mypath);
				if (dbFile.exists()) {
					tempdb = SQLiteDatabase.openDatabase(mypath, null,
							SQLiteDatabase.OPEN_READONLY);
				}
			} catch (SQLiteException e) {
				Applog.logString("Exception OpenData base" + e.getMessage());
			}
			if (tempdb != null) {
				tempdb.close();
			}
			return tempdb != null ? true : false;
		}

	}

	private static final int DB_VERSION = 1;
	private static DbHelper dbhelper = null;
	private SQLiteDatabase db = null;

	public AppDatabase(Context context, Object dblock) {
		this.dblock = dblock;
		dbhelper = new DbHelper(context);
	}

	public void close(boolean releaseDb) {
		if (null != db) {
			db.close();
		}
		if (releaseDb) {
			db = null;
		}
	}

	public void open() throws SQLException {
		boolean flag = true;

		if (null != db && db.isOpen()) {
			flag = false;
		}
		if (flag) {
			db = dbhelper.openDataBase();
		}
	}

	/**
	 * checking whether the database is open or close
	 *
	 * @param releaseDb
	 * @return
	 */
	public boolean checkifOpen(boolean releaseDb) {
		if (null != db) {
			return false;
		}
		return db.isOpen();
	}

	public static AppDatabase getDatabase() {
		return database;
	}

	public Cursor rawQuery(String query) {
		synchronized (dblock) {
			return db.rawQuery(query, null);
		}
	}

	/**
	 * raw query
	 *
	 * @param query
	 * @param args
	 * @return
	 */
	public Cursor rawQuery(String query, String[] args) {
		synchronized (dblock) {
			return db.rawQuery(query, args);
		}
	}

	/**
	 * execute sql commands
	 *
	 * @param sql
	 */
	public void executeSQL(String sql) {
		synchronized (dblock) {
			db.execSQL(sql);
		}
	}

	/**
	 * opening a database
	 *
	 * @return
	 */
	public boolean isOpen() {
		synchronized (dblock) {
			return db.isOpen();
		}
	}

	/**
	 * inserting values in table in database
	 *
	 * @param tablename
	 * @param values
	 * @return
	 */
	public long insert(String tablename, ContentValues values) {
		synchronized (dblock) {
			return db.insert(tablename, null, values);
		}
	}

	/**
	 * updating table in database
	 *
	 * @param tablename
	 * @param values
	 * @param whereClause
	 * @param whereArgs
	 * @return
	 */
	public long update(String tablename, ContentValues values,
					   String whereClause, String[] whereArgs) {
		synchronized (dblock) {
			return db.update(tablename, values, whereClause, whereArgs);
		}
	}

	/**
	 * delete table from database
	 *
	 * @param tablename
	 * @param whereClause
	 * @param whereArgs
	 * @return
	 */
	public long delete(String tablename, String whereClause, String[] whereArgs) {
		synchronized (dblock) {
			return db.delete(tablename, whereClause, whereArgs);
		}
	}

	public void cleanUp() {
		releaseDb();
		mContext = null;
	}

	/**
	 * closing database
	 */
	public void releaseDb() {
		synchronized (dblock) {

			if (database != null && database.isOpen()) {
				database.close(true);
			}
		}
	}

	/**
	 * method to open database
	 */
	public void openDb() {
		synchronized (dblock) {
			database.open();
		}
	}

	/**
	 * method to shut down the database
	 */
	public static void shoutDownDb() {
		if (database != null) {
			database.cleanUp();
		}
		database = null;

	}

	/**
	 * intializing database
	 *
	 * @param c
	 * @param dbLock
	 */
	public static void initialized(Context c, Object dbLock) {

		if (database == null) {
			database = new AppDatabase(c, dbLock);
			database.openDb();
		}
	}
}