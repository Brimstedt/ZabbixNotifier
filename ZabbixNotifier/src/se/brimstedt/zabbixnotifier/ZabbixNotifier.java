package se.brimstedt.zabbixnotifier;

import android.app.Application;

public class ZabbixNotifier extends Application {

	static final String C2DM_SENDER = "linus@brimstedt.se";

	static final String PREFS_SERVER_LIST = "ServerList";
	static final String PREFS_SERVER_URL = "ServerUrl";
	static final String HTTP_ZABBIXNOTIFIER_URL = "http://zabbixnotifier.brimstedt.se/register.php";
	// public static final String HTTP_ZABBIXNOTIFIER_URL =
	// "http://192.168.10.124/register.php";

}
