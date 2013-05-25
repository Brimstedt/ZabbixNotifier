package se.brimstedt.zabbixnotifier.dao;

import java.util.ArrayList;
import java.util.List;

import se.brimstedt.zabbixnotifier.ZabbixNotifier;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.inject.Inject;
import com.googlecode.androidannotations.annotations.AfterInject;
import com.googlecode.androidannotations.annotations.App;
import com.googlecode.androidannotations.annotations.EBean;

@EBean
public class ServerListImpl implements IServerList
{

    @App
    ZabbixNotifier application;

    @Inject
	private IPersitanceService serverDatabase = null;

    @AfterInject 
    void injectRoboGuiceDependencies() {
        application.getInjector().injectMembers(this);
    }
    
	/* (non-Javadoc)
	 * @see se.brimstedt.zabbixnotifier.dao.IServerList#saveServer(se.brimstedt.zabbixnotifier.dao.ServerConfiguration)
	 */
	@Override
	public boolean saveServer(ServerConfiguration server)
	{
		long result = 0;
		SQLiteDatabase db = getServerDatabase().getWritableDatabase();
		System.out.println(server);
		if (server.getServerId() <= 0)
		{

			server.setServerId(getNewServerId());
		}
		
		if(!serverExists(server.getServerId()))
		{
			result = db.insert("Serverconfiguration", null, getServerContentValues(server));
		}
		else
		{
			result = db.update("Serverconfiguration", getServerContentValues(server),
					"serverId = " + server.getServerId(), null);
		}
		return (result != -1);
	}

	private boolean serverExists(int serverId) {
		return getServer(serverId) != null;
	}

	private ContentValues getServerContentValues(ServerConfiguration server)
	{
		ContentValues values = new ContentValues();
		values.put("serverId", server.getServerId());
		values.put("serverUrl", server.getServerURL());
		values.put("serverHash", server.getServerHash());
		values.put("userHash", server.getUserHash());

		return values;
	}

	/* (non-Javadoc)
	 * @see se.brimstedt.zabbixnotifier.dao.IServerList#getNewServerId()
	 */
	@Override
	public int getNewServerId()
	{
		SQLiteDatabase readableDatabase = getServerDatabase().getReadableDatabase();
		Cursor rawQuery = readableDatabase.rawQuery("SELECT MAX(serverId) FROM ServerConfiguration", null);

		if (rawQuery.getCount() == 0)
		{
			return 1;
		}
		rawQuery.moveToFirst();
		return rawQuery.getInt(0) + 1;
	}

	/* (non-Javadoc)
	 * @see se.brimstedt.zabbixnotifier.dao.IServerList#getServer(int)
	 */
	@Override
	public ServerConfiguration getServer(int serverId)
	{
		SQLiteDatabase db = getServerDatabase().getReadableDatabase();

		Cursor cursor = db.rawQuery(
				"SELECT serverId, serverHash, serverUrl, userHash FROM ServerConfiguration WHERE serverId = "
						+ serverId, null);
		if(cursor.getCount() == 0)
		{
			return null;
		}
		cursor.moveToFirst();
		ServerConfiguration sc = getServerConfigurationFromRow(cursor);

		return sc;
	}

	private ServerConfiguration getServerConfigurationFromRow(Cursor cursor)
	{
		ServerConfiguration sc = new ServerConfiguration();
		sc.setServerId(cursor.getInt(0));
		sc.setServerHash(cursor.getString(1));
		sc.setServerUrl(cursor.getString(2));
		sc.setUserHash(cursor.getString(3));
		return sc;
	}
	

	/* (non-Javadoc)
	 * @see se.brimstedt.zabbixnotifier.dao.IServerList#getAll()
	 */
	@Override
	public List<ServerConfiguration> getAll()
	{
		List<ServerConfiguration> list = new ArrayList<ServerConfiguration>();
		SQLiteDatabase db = getServerDatabase().getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT serverId, serverHash, serverUrl, userHash  FROM ServerConfiguration", null);
		while(cursor.moveToNext())
		{
			list.add(getServerConfigurationFromRow(cursor));
		}
		
		return list;
	}

	public IPersitanceService getServerDatabase() {
		return serverDatabase;
	}

	public void setServerDatabase(IPersitanceService serverDatabase) {
		this.serverDatabase = serverDatabase;
	}

}
