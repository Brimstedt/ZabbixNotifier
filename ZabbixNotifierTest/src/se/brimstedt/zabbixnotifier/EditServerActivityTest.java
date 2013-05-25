package se.brimstedt.zabbixnotifier;

import se.brimstedt.zabbixnotifier.dao.PersitanceServiceSqlLiteImpl;
import se.brimstedt.zabbixnotifier.dao.ServerListImpl;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;



public class EditServerActivityTest extends AndroidTestCase 
{

	EditServerActivity_ editServerActivity;
	private ServerListImpl sl;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		PersitanceServiceSqlLiteImpl db = new PersitanceServiceSqlLiteImpl(getContext());
		SQLiteDatabase writableDatabase = db.getWritableDatabase();
		writableDatabase.execSQL("DELETE FROM ServerConfiguration");
		
		sl = new ServerListImpl();
		sl.setServerDatabase(db);

		editServerActivity = new EditServerActivity_();
		editServerActivity.serverList = sl;
		
	}
	
	public void testSaveServerPreferences_puts_stuff_in_db()
	{
		
		assertTrue(editServerActivity.saveServerPreferences(666, "serverUrl", "serverHash", "deviceId"));
		assertEquals("Expect one entry in server db", 1, sl.getAll().size());
		
		
	}
}
