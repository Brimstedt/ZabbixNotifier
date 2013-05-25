package se.brimstedt.zabbixnotifier;

import java.util.List;

import roboguice.application.RoboApplication;
import roboguice.config.AbstractAndroidModule;
import se.brimstedt.zabbixnotifier.dao.IPersitanceService;
import se.brimstedt.zabbixnotifier.dao.IServerList;
import se.brimstedt.zabbixnotifier.dao.PersitanceServiceSqlLiteImpl;
import se.brimstedt.zabbixnotifier.dao.ServerListImpl;

import com.google.inject.Module;

public class ZabbixNotifier extends RoboApplication {

	static final String C2DM_SENDER = "linus@brimstedt.se";

//	static final String HTTP_ZABBIXNOTIFIER_URL = "http://zabbixnotifier.brimstedt.se/register.php";
	static final String HTTP_ZABBIXNOTIFIER_URL = "http://192.168.69.150/register.php";

	public static final int NOTIFICATION_GOOGLE_REGISTRATION = 100;
	public static final int NOTIFICATION_ZABBIXNOTIFIER_REGISTRATION = 101;
	public static final int NOTIFICATION_GENERAL_MESSAGE = 0;
	public static final int NOTIFICATION_TRIGGER_BASE = 100000;

    @Override
    protected void addApplicationModules(List<Module> modules) {
            modules.add(new ZabbixNotifierModule());
    }

    static class ZabbixNotifierModule extends AbstractAndroidModule {
            @Override
            protected void configure() {
                bind(IPersitanceService.class).to(PersitanceServiceSqlLiteImpl.class);
                bind(IServerList.class).to(ServerListImpl.class);
                
            }
    }

}
