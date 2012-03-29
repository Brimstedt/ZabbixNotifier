package se.brimstedt.zabbixnotifier;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

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
			// TODO Send this to my application server to get the real data
			// Lets make something visible to show that we received the message
			createNotification(context, server, message);

		}
	}

	public void createNotification(Context context, String server, String message) {
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification(R.drawable.ic_launcher,
				// @TODO: Change to "icon"
				"Message received: " + message , System.currentTimeMillis());
		// Hide the notification after its selected
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		notification.defaults = Notification.DEFAULT_ALL;
		
		Log.d("C2DM", "creating notification " + server);
		
		Intent intent = new Intent(context, MessageReceivedActivity.class);
		intent.putExtra("se.brimstedt.zabbixnotifier.server", server);
		intent.putExtra("se.brimstedt.zabbixnotifier.message", message);
		
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
				intent, Intent.FLAG_ACTIVITY_NEW_TASK);
			
		notification.setLatestEventInfo(context, "Message",
				message, pendingIntent);
		
		notificationManager.notify(0, notification);

	}

}
