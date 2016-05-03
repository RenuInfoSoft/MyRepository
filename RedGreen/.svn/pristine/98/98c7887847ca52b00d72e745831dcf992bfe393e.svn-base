package com.unfoldlabs.redgreen.db;

import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.preference.PreferenceManager;

import com.unfoldlabs.redgreen.R;
import com.unfoldlabs.redgreen.utilty.Utility;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class SavingDatabaseHelper extends SQLiteOpenHelper {

	private DBListener listener;
	private SQLiteDatabase dbManager;
	private SavingDbConfig config;
	private String DATABASE_PATH = "/data/data/com.unfoldlabs.redgreen/databases/redgreen.sqlite";

	/** method: constructor */
	public SavingDatabaseHelper(SavingDbConfig config) {
		super(config.getContext(), config.getName(), null, config.getVersion());
		this.config = config;
	}

	/**
	 * Open the database with write permission after moving it to the device
	 * 
	 * Important: Make sure you always call createDatabase before it to double
	 * check the database is moved
	 * 
	 * @return SQLiteDatabase to manage database commands
	 */

	public SQLiteDatabase openWritableDatabase() {

		if (dbManager != null && dbManager.isOpen() && !dbManager.isReadOnly())
			return dbManager;

		dbManager = SQLiteDatabase.openDatabase(config.getFullDatabasePath(),
				null, SQLiteDatabase.OPEN_READWRITE);

		return dbManager;
	}

	/**
	 * Notify the listener that database is opened and read to use
	 */

	public void notifyDatabaseOepend() {
		if (listener != null)
			listener.onDatabaseOpened();
	}

	/**
	 * Open read only database after moving it to the device
	 * 
	 * Important: Make sure you always call createDatabase before it to double
	 * check the database is moved
	 * 
	 * @return SQLiteDatabase to manage database commands
	 */

	public SQLiteDatabase openReadableDatabase() {

		if (dbManager != null && dbManager.isOpen() && dbManager.isReadOnly())
			return dbManager;

		dbManager = SQLiteDatabase.openDatabase(config.getFullDatabasePath(),
				null, SQLiteDatabase.OPEN_READWRITE);

		return dbManager;
	}

	/**
	 * Close the opened database instance
	 */
	public void closeDatabase() {
		if (dbManager != null) {
			dbManager.close();
			dbManager = null;
		}
	}

	/**
	 * Create the database by moving it from assets to destination folder
	 * 
	 * Don't worry: if the database with same version is already moved will not
	 * be moved again so make sure you always call it for double check.
	 * 
	 * Note that: for current version if the version changed will move the new
	 * database and old database will be deleted
	 * 
	 */
	public synchronized void createDatabase() throws IOException {

		if (isDatabaseExist()) {
			if (listener != null)
				listener.onDataExist();
		} else {
			this.getReadableDatabase().close();
			try {
				copyDatabase();
				Utility.saveValue(config.getVersionTag(),
						config.getVersion(), config.getContext());
				this.close();
				if (listener != null)
					listener.onFinishCoping();
			} catch (IOException ex) {
				throw new Error("" + R.string.error_copying_database, ex);
			}
		}
	}

	/**
	 * this method check if the database with the current version already exist
	 * in the device
	 * 
	 * @return true if already moved, false otherwise
	 */

	public boolean isDatabaseExist() {
		boolean res = false;
		SQLiteDatabase tempDB = null;
		try {

			File dbFile = new File(DATABASE_PATH);
			if (dbFile.exists()) {
				tempDB = SQLiteDatabase.openDatabase(DATABASE_PATH, null,
						SQLiteDatabase.OPEN_READONLY);
			}

		} catch (SQLiteException e) {
		}
		if (tempDB != null) {
			res = true;
			tempDB.close();
		}

		if (res) {
			SharedPreferences prefManager = PreferenceManager
					.getDefaultSharedPreferences(config.getContext());
			int currentVersion = prefManager.getInt(config.getVersionTag(),
					SavingDbConfig.DEFAULT_VERSION);
			res = (currentVersion == config.getVersion());
			if ((currentVersion != config.getVersion())) {

			}
		}
		return res;
	}

	/**
	 * copying database files
	 * 
	 * @throws IOException
	 */
	private void copyDatabase() throws IOException {
		InputStream input = config.getContext().getAssets()
				.open(config.getName());
		OutputStream output = new FileOutputStream(config.getFullDatabasePath());
		Utility.copyFile(input, output);
		output.flush();
		output.close();
		input.close();
	}

	/**
	 * Call this method if you want to know when the helper finish moving the
	 * data to do any required operation
	 * 
	 * @param DBListener
	 *            listener
	 */
	public void setListener(DBListener listener) {
		this.listener = listener;
	}

	/** called when create data base */
	public void onCreate(SQLiteDatabase db) {

	}

	/**
	 * called when change the version of database in config during update
	 * process
	 */
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		/**
		 * Drop older table if existed
		 */
		db.execSQL("DROP TABLE IF EXISTS " + SavingDBAdapter.CACHE_TABLE.TABLE_NAME);

		db.execSQL("DROP TABLE IF EXISTS " + SavingDBAdapter.RATE_TABLE.TABLE_NAME);

		/**
		 * Create tables again
		 */
		onCreate(db);
	}

	public interface DBListener {
		void onFinishCoping();

		void onDataExist();

		void onDatabaseOpened();
	}
}
