package com.unfoldlabs.redgreen.db;

public class BaseDBAdapter {

	protected static AppDatabase database;

	public BaseDBAdapter() {
		database = AppDatabase.getDatabase();
	}

}
