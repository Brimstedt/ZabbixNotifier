package se.brimstedt.zabbixnotifier.dao;

import java.util.List;

public interface IServerList {

	public abstract boolean saveServer(ServerConfiguration server);

	public abstract int getNewServerId();

	public abstract ServerConfiguration getServer(int serverId);

	public abstract List<ServerConfiguration> getAll();

}