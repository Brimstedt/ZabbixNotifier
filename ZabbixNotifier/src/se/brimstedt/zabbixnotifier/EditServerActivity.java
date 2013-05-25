package se.brimstedt.zabbixnotifier;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
import se.brimstedt.zabbixnotifier.dao.ServerConfiguration;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.provider.Settings.Secure;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.inject.Inject;
import com.googlecode.androidannotations.annotations.AfterInject;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.Extra;
import com.googlecode.androidannotations.annotations.RoboGuice;
import com.googlecode.androidannotations.annotations.ViewById;


@EActivity(R.layout.edit_server)
@RoboGuice
public class EditServerActivity extends Activity
{

	public static final String SERVER_TO_EDIT = "se.brimstedt.zabbixnotifier.serverToEdit";

	@Inject
	IServerList serverList;

	@Extra(SERVER_TO_EDIT)
	int serverId;
	
	@ViewById(R.id.serverurl)
	TextView urlField;

	@Click
	public void saveServer(View view)
	{
		Log.d("C2DM", "Server to save: " + serverId);

		String serverUrl =  urlField.getText().toString();
		String serverHash = md5(serverUrl);

		String deviceId = Secure.getString(getContentResolver(), Secure.ANDROID_ID);
				
		saveServerPreferences(serverId, serverUrl, serverHash, deviceId);

		registerC2DM();

		Log.d("C2DM", "register device and server");
		sendServerRegistrationIdToServer(serverUrl, serverHash, deviceId);

		Intent intent;
		intent = new Intent(this, ZabbixNotifierActivity_.class);
		startActivity(intent);

	}

	@Override
	protected void onResume() {
		
		super.onResume();
		
		Log.d("EditServer", "serverid: " + serverId);
		ServerConfiguration sc = serverList.getServer(serverId);
		if(sc != null)
		{
			urlField.setText(sc.getServerURL());
		}
		System.out.println(sc);
	}
	
	public boolean saveServerPreferences(int serverId, String serverUrl, String serverHash, String deviceId)
	{
		String userHash = md5(deviceId);

		ServerConfiguration sc = new ServerConfiguration();
		sc.setServerId(serverId);
		sc.setServerUrl(serverUrl);
		sc.setServerHash(serverHash);
		sc.setUserHash(userHash);
	
		return serverList.saveServer(sc);
	}

	/**
	 * Registers device wit google
	 */
	@Background
	public void registerC2DM()
	{
		Log.d("C2DM", "start registration process");
		Intent intent = new Intent("com.google.android.c2dm.intent.REGISTER");
		intent.putExtra("app", PendingIntent.getBroadcast(this, 0, new Intent(), 0));

		// Sender currently not used
		intent.putExtra("sender", ZabbixNotifier.C2DM_SENDER);
		startService(intent);
	}

	/**
	 * Registers device with ZabbixNotifier Application server
	 * @param serverUrl
	 */
	@Background
	public void sendServerRegistrationIdToServer(String serverUrl, String serverHash, String deviceId)
	{
		Log.d("C2DM", "Sending registration ID to ZabbixNotifier server");

		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(ZabbixNotifier.HTTP_ZABBIXNOTIFIER_URL);
		try
		{
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
			nameValuePairs.add(new BasicNameValuePair("serverHash", serverHash));
			nameValuePairs.add(new BasicNameValuePair("deviceId", deviceId));

			post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = client.execute(post);
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

			String line = "";
			StringBuffer error = new StringBuffer();
			while ((line = rd.readLine()) != null)
			{
				Log.e("HttpResponse", line);
				error.append(line.trim());
			}
			Log.e("HttpResponse", error.toString());
			if (!error.equals(""))
			{
//				createNotification(getApplicationContext(), deviceId, "ZabbixNotifier registration failed: " + error, 102);
				MessageReceivedActivity.createNotification(getApplicationContext(), getString(R.string.zabbixnotifier_registration_failed), error.toString(), ZabbixNotifier.NOTIFICATION_ZABBIXNOTIFIER_REGISTRATION);

			}
			else
			{
//				createNotification(getApplicationContext(), deviceId, "ZabbixNotifier registration successful" + error, 102);
				MessageReceivedActivity.createNotification(getApplicationContext(), getString(R.string.zabbixnotifier_registration_successful), "", ZabbixNotifier.NOTIFICATION_ZABBIXNOTIFIER_REGISTRATION);

			}
		}
		catch (IOException e)
		{
//			createNotification(getApplicationContext(), deviceId, "ZabbixNotifier registration failed: " + e.getLocalizedMessage(), 102);
			MessageReceivedActivity.createNotification(getApplicationContext(), getString(R.string.zabbixnotifier_registration_failed), e.getLocalizedMessage(), ZabbixNotifier.NOTIFICATION_ZABBIXNOTIFIER_REGISTRATION);

		}
	}

	public String md5(String s)
	{
		try
		{
			// Create MD5 Hash
			MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
			digest.update(s.getBytes());
			byte messageDigest[] = digest.digest();

			// Create Hex String
			StringBuffer hexString = new StringBuffer();
			for (int i = 0; i < messageDigest.length; i++)
				hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
			return hexString.toString();

		}
		catch (NoSuchAlgorithmException e)
		{
			e.printStackTrace();
		}
		return "";
	}
}
