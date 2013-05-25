package se.brimstedt.zabbixnotifier;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.Extra;
import com.googlecode.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_result)
public class MessageReceivedActivity extends Activity
{


	public static final String TITLE = "se.brimstedt.zabbixnotifier.title";
	public static final String MESSAGE = "se.brimstedt.zabbixnotifier.message";

	@ViewById(R.id.server)
	TextView serverView;

	@ViewById(R.id.result)
	TextView messageView;
	
	@Extra(TITLE)
	String server;
	
	@Extra(MESSAGE)
	String message;
	
	
	@AfterViews 
	protected void afterViews()
	{
		serverView.setText(server);
		messageView.setText(message);
	}

	@Override
	protected void onPostResume()
	{
		super.onPostResume();
		serverView.setText(server);
		messageView.setText(message);
	}


	public static void createNotification(Context context, String title, String message, int notificationId) {
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		
		Notification notification = new Notification(R.drawable.ic_launcher,
				// @TODO: Change to "icon"
				title + ": " + message , System.currentTimeMillis());
		
		// Hide the notification after its selected
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		notification.defaults = Notification.DEFAULT_ALL;
		
		Intent intent = new Intent(context, MessageReceivedActivity_.class);
		intent.putExtra(MessageReceivedActivity.TITLE, title);
		intent.putExtra(MessageReceivedActivity.MESSAGE, message);
		
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
				intent, Intent.FLAG_ACTIVITY_NEW_TASK);
			
		notification.setLatestEventInfo(context, title,
				message, pendingIntent);
		
		notificationManager.notify(notificationId, notification);

	}

}
