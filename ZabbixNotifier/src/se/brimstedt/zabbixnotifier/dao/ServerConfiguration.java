package se.brimstedt.zabbixnotifier.dao;


public class ServerConfiguration
{
	int	serverId;
	String	serverURL;
	String serverHash;
	String userHash;

	public int getServerId()
	{
		return serverId;
	}
	public void setServerId(int serverId)
	{
		this.serverId = serverId;
	}
	public String getServerURL()
	{
		return serverURL;
	}
	public void setServerUrl(String serverUrl)
	{
		this.serverURL = serverUrl;
	}
	public String getServerHash()
	{
		return serverHash;
	}
	public void setServerHash(String serverHash)
	{
		this.serverHash = serverHash;
	}
	public String getUserHash()
	{
		return userHash;
	}
	public void setUserHash(String userHash)
	{
		this.userHash = userHash;
	}
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((serverHash == null) ? 0 : serverHash.hashCode());
		result = prime * result + serverId;
		result = prime * result + ((serverURL == null) ? 0 : serverURL.hashCode());
		result = prime * result + ((userHash == null) ? 0 : userHash.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		ServerConfiguration other = (ServerConfiguration) obj;
		if (serverHash == null)
		{
			if (other.serverHash != null) return false;
		}
		else if (!serverHash.equals(other.serverHash)) return false;
		if (serverId != other.serverId) return false;
		if (serverURL == null)
		{
			if (other.serverURL != null) return false;
		}
		else if (!serverURL.equals(other.serverURL)) return false;
		if (userHash == null)
		{
			if (other.userHash != null) return false;
		}
		else if (!userHash.equals(other.userHash)) return false;
		return true;
	}
	@Override
	public String toString()
	{
		return "ServerConfiguration [serverId=" + serverId + ", serverURL=" + serverURL + ", serverHash=" + serverHash
				+ ", userHash=" + userHash + "]";
	}
	
}
