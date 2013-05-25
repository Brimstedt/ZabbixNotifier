package se.brimstedt.zabbixnotifier.dao;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

public class ServerListTest extends AndroidTestCase {

	private ServerListImpl sl;

	public void setUp() {
		PersitanceServiceSqlLiteImpl db = new PersitanceServiceSqlLiteImpl(
				getContext());
		SQLiteDatabase writableDatabase = db.getWritableDatabase();
		writableDatabase.execSQL("DELETE FROM ServerConfiguration");

		sl = new ServerListImpl();
		sl.setServerDatabase(db);
	}

	public void testSave_then_get_server() throws URISyntaxException {
		ServerConfiguration sc = getNewServerConfig();

		sl.saveServer(sc);
		ServerConfiguration loaded = sl.getServer(1);

		assertEquals(sc, loaded);
	}

	private ServerConfiguration getNewServerConfig() {
		ServerConfiguration sc = new ServerConfiguration();
		sc.serverHash = "hash";
		sc.serverURL = "http://url";
		sc.userHash = "user";
		return sc;
	}

	public void testSave_multiple_times() {
		ServerConfiguration sc = getNewServerConfig();

		assertTrue(sl.saveServer(sc));
		assertTrue(sl.saveServer(sc));
		assertTrue(sl.saveServer(sc));
		assertEquals(1, sl.getAll().size());

	}


	public void testSave_unexisting_with_predefined_serverId_is_saved() {
		ServerConfiguration sc = getNewServerConfig();

		sc.setServerId(666);
		assertTrue(sl.saveServer(sc));
		assertTrue(sl.saveServer(sc));
		assertEquals(1, sl.getAll().size());
	}

	public void testgetAll() throws URISyntaxException {
		ServerConfiguration sc;

		List<ServerConfiguration> expected = new ArrayList<ServerConfiguration>();

		assertEquals(0, sl.getAll().size());

		sc = getNewServerConfig();
		assertTrue(sl.saveServer(sc));
		expected.add(sc);
		assertEquals(1, sl.getAll().size());

		sc = getNewServerConfig();
		assertTrue(sl.saveServer(sc));
		expected.add(sc);
		assertEquals(2, sl.getAll().size());

		sc = getNewServerConfig();
		assertTrue(sl.saveServer(sc));
		expected.add(sc);
		assertEquals(3, sl.getAll().size());

		assertEquals(expected, sl.getAll());

	}

	public void testSave_new_servers_multiple_times() throws URISyntaxException {

		assertTrue(sl.saveServer(getNewServerConfig()));
		assertTrue(sl.saveServer(getNewServerConfig()));
		assertTrue(sl.saveServer(getNewServerConfig()));

		assertEquals(3, sl.getAll().size());
	}

	// private void dumpQuery(SQLiteOpenHelper db, String sql)
	// {
	// Cursor rawQuery = db.getReadableDatabase().rawQuery(sql, null);
	// while(rawQuery.moveToNext())
	// {
	// for(int columnIndex = 0; columnIndex < rawQuery.getColumnCount();
	// columnIndex++)
	// {
	// Log.d("SQL", rawQuery.getColumnName(columnIndex) + ": " +
	// rawQuery.getString(columnIndex));
	// }
	// }
	// }

}
