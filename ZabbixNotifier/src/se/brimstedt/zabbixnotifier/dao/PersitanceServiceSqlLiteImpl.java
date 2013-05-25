package se.brimstedt.zabbixnotifier.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.inject.Inject;

public class PersitanceServiceSqlLiteImpl extends SQLiteOpenHelper implements IPersitanceService
{
	    private static final int DATABASE_VERSION = 2;

		private static final String DATABASE_NAME = "serverlist";

		@Inject
	    public PersitanceServiceSqlLiteImpl(Context context) {
	        super(context, DATABASE_NAME, null, DATABASE_VERSION);
	    }

	    @Override
	    public void onCreate(SQLiteDatabase db) {
	        db.execSQL("CREATE TABLE ServerConfiguration (serverId INT PRIMARY KEY, serverURL VARCHAR(512), serverHash CHAR(32), userHash char(16))");
	}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
		{
			if(oldVersion == 1 && newVersion == 2)
			{
				db.execSQL("DROP TABLE ServerConfiguration");
				onCreate(db);
			}
		}
		
}
