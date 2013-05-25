package se.brimstedt.zabbixnotifier.dao;

import android.database.sqlite.SQLiteDatabase;

public interface IPersitanceService {

	SQLiteDatabase getWritableDatabase();
	SQLiteDatabase getReadableDatabase();

}