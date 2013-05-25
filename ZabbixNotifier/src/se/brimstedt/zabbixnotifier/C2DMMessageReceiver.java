package se.brimstedt.zabbixnotifier;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.googlecode.androidannotations.annotations.EReceiver;

@EReceiver
public class C2DMMessageReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		Log.w("C2DM", "Message Receiver called");
		if ("com.google.android.c2dm.intent.RECEIVE".equals(action)) {
			Log.w("C2DM", "Received message");
			final String server = intent.getStringExtra("server");
			final String message = intent.getStringExtra("message");
			Log.d("C2DM", "dmControl: server = " + server);
			
			MessageReceivedActivity.createNotification(context, server, message, ZabbixNotifier.NOTIFICATION_TRIGGER_BASE);

		}
	}


}
