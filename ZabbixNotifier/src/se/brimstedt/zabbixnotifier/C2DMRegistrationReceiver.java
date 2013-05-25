package se.brimstedt.zabbixnotifier;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import se.brimstedt.zabbixnotifier.dao.IServerList;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings.Secure;
import android.util.Log;

import com.google.inject.Inject;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.EReceiver;

@EReceiver
public class C2DMRegistrationReceiver extends BroadcastReceiver
{

	@Inject
	IServerList serverList;
	
	@Override
	public void onReceive(Context context, Intent intent)
	{
		String action = intent.getAction();
		Log.w("C2DM", "Registration Receiver called");
		if ("com.google.android.c2dm.intent.REGISTRATION".equals(action))
		{
			Log.w("C2DM", "Received registration ID");
			final String registrationId = intent.getStringExtra("registrationId");

			String error = intent.getStringExtra("error");

			Log.d("C2DM", "dmControl: registrationId = " + registrationId + ", error = " + error);
			String deviceId = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
			
//			createNotification(context, registrationId, R.string.google_registration_successfull, 100);
			MessageReceivedActivity.createNotification(context, context.getString(R.string.google_registration_successfull), "", ZabbixNotifier.NOTIFICATION_GOOGLE_REGISTRATION);
			
			
			sendRegistrationIdToServer(context, deviceId, registrationId);

		}
	}


//	public void createNotification(Context context, String registrationId, String message, int id)
//	{
//		NotificationManager notificationManager = (NotificationManager) context
//				.getSystemService(Context.NOTIFICATION_SERVICE);
//		Notification notification = new Notification(R.drawable.ic_launcher,
//		// TODO: Change to "icon"
//				message, System.currentTimeMillis());
//		// Hide the notification after its selected
//		notification.flags |= Notification.FLAG_AUTO_CANCEL;
//
//		Intent intent = new Intent(context, ZabbixNotifierActivity_.class);
//		intent.putExtra("registrationId", registrationId);
//		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
//		notification.setLatestEventInfo(context, "Registration", message, pendingIntent);
//		notificationManager.notify(id, notification);
//	}

	// Incorrect usage as the receiver may be canceled at any time
	// do this in an service and in an own thread
	@Background
	public void sendRegistrationIdToServer(Context context, String deviceId, String registrationId)
	{
		Log.d("C2DM", "Sending registration ID to my application server");
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(ZabbixNotifier.HTTP_ZABBIXNOTIFIER_URL);
		try
		{
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
			// Get the deviceID
			nameValuePairs.add(new BasicNameValuePair("deviceId", deviceId));
			nameValuePairs.add(new BasicNameValuePair("registrationId", registrationId));

			post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			
			HttpResponse response = client.execute(post);
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

			String line = "";
			StringBuffer error = new StringBuffer("");
			while ((line = rd.readLine()) != null)
			{
				Log.e("HttpResponse", line);
				error.append(line.trim());
			}
			Log.e("HttpResponse", error.toString());
			if (!error.toString().equals(""))
			{
//				createNotification(context, registrationId, "ZabbixNotifier registration failed: " + error, 101);
				System.out.println(error);
				MessageReceivedActivity.createNotification(context, context.getString(R.string.zabbixnotifier_registration_failed), error.toString(), ZabbixNotifier.NOTIFICATION_ZABBIXNOTIFIER_REGISTRATION);

			}
			else
			{
//				createNotification(context, registrationId, "ZabbixNotifier registration successful" + error, 101);
				MessageReceivedActivity.createNotification(context, context.getString(R.string.zabbixnotifier_registration_successful), ":-)", ZabbixNotifier.NOTIFICATION_ZABBIXNOTIFIER_REGISTRATION);
			}
		}
		catch (IOException e)
		{
//			createNotification(context, deviceId, "ZabbixNotifier registration failed: " + e.getLocalizedMessage(), 101);
			MessageReceivedActivity.createNotification(context, context.getString(R.string.zabbixnotifier_registration_failed), e.getLocalizedMessage(), ZabbixNotifier.NOTIFICATION_ZABBIXNOTIFIER_REGISTRATION);

		}

	}
}
