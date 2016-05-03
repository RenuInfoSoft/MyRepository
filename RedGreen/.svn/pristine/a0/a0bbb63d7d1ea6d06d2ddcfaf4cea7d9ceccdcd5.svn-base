package com.unfoldlabs.redgreen.db;

import android.content.Context;

/**
 * Class to initial the configuration for the database helper
 * 
 */
public class SavingDbConfig {

	private final static String DB_FOLDER = "/databases/";
	private String databasePath;
	private Context context;
	/** name of the database in the assets folder */
	private String name;
	/**
	 * Database version, changing it will move new database from assets folder
	 * and this will delete user saved data in the old database
	 */
	private int version;
	/** default database version */
	public static int DEFAULT_VERSION = 2;
	private String versionTag;

	/**
	 * Initial database configurations to move file to the default application
	 * private folder
	 * 
	 * IMPORTANT: change the version will override user database with new one
	 * from assets and this will clear the data for the user !
	 * 
	 * @param Context
	 * @param String
	 *            name of database file in the assets
	 * @param int version of database, for every new version will move the
	 *        database from assets folder and this will cause to clear the old
	 *        database data !
	 * 
	 */
	public SavingDbConfig(String dbNamer, int dbVersion, Context context) {
		this.context = context;
		this.name = dbNamer;
		this.version = dbVersion;
		this.databasePath = context.getApplicationInfo().dataDir;
		this.versionTag = dbNamer + ":" + version;
		if (!databasePath.endsWith(DB_FOLDER))
			this.databasePath += DB_FOLDER;
	}

	/**
	 * @return dbFolder
	 */
	public static String getDbFolder() {
		return DB_FOLDER;
	}

	/**
	 * @return database path
	 */
	public String getDatabasePath() {
		return databasePath;
	}

	/**
	 * @return context
	 */
	public Context getContext() {
		return context;
	}

	/**
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return version
	 */
	public int getVersion() {
		return version;
	}

	/**
	 * @return fulldatabasePath
	 */
	public String getFullDatabasePath() {
		return databasePath + name;
	}

	/**
	 * @return versionTag
	 */
	public String getVersionTag() {
		return versionTag;
	}

}
