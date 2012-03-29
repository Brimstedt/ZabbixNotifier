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

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.googlecode.androidannotations.annotations.Background;

public class EditServerActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d("C2DM", "Edit Server");
		setContentView(R.layout.edit_server);
		Bundle extras = getIntent().getExtras();
		
//		if (extras != null) {
//			String server = extras.getString("se.brimstedt.zabbixnotifier.server");
//			String message = extras.getString("se.brimstedt.zabbixnotifier.message");
//			
//			Log.d("C2DM", "Payload: " + server + ":" + message);
//			if (message != null && message.length() > 0) {
//				TextView view = (TextView) findViewById(R.id.server);
//				view.setText(server);
//				
//				TextView messageView = (TextView) findViewById(R.id.result);
//				messageView.setText(message);
//				
//			}
//		}
		super.onCreate(savedInstanceState);
	}
		
		public void saveServer(View view) {
			
			Log.d("C2DM", "Save server");
			Bundle extras = getIntent().getExtras();
			int serverId = extras.getInt("se.brimstedt.zabbixnotifier.serverToEdit");
			Log.d("C2DM", "Server to save: " + serverId);
			
			TextView urlField = (TextView) findViewById(R.id.server);
			String serverUrl = (String) urlField.getText();
			
			saveServerPreferences(serverId, serverUrl);

			registerC2DM();
			
			Log.d("C2DM", "register device and server");
			sendServerRegistrationIdToServer(serverUrl);
						
			Intent intent;
			intent = new Intent(this, ZabbixNotifierActivity.class);
	        startActivity(intent);

		}

		@Background
		private void saveServerPreferences(int serverId, String serverUrl) {
			SharedPreferences settings = getSharedPreferences(
					ZabbixNotifier.PREFS_SERVER_LIST, 0);
			
			Editor edit = settings.edit();
			edit.putString(ZabbixNotifier.PREFS_SERVER_URL + serverId, serverUrl);
			edit.commit();
		}

		@Background
		private void registerC2DM() {
			Log.d("C2DM", "start registration process");
			Intent intent = new Intent("com.google.android.c2dm.intent.REGISTER");
			intent.putExtra("app",
					PendingIntent.getBroadcast(this, 0, new Intent(), 0));
			
			// Sender currently not used
			intent.putExtra("sender", ZabbixNotifier.C2DM_SENDER);
			startService(intent);
		}
	
		@Background
		public void sendServerRegistrationIdToServer(String serverUrl) {
			Log.d("C2DM", "Sending registration ID to my application server");
			
			String deviceId = Secure.getString( getContentResolver(),
					Secure.ANDROID_ID);
			String serverHash = md5(serverUrl);
			
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(ZabbixNotifier.HTTP_ZABBIXNOTIFIER_URL);
			try {
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
				// Get the deviceID
				nameValuePairs.add(new BasicNameValuePair("serverHash", serverHash));
				nameValuePairs.add(new BasicNameValuePair("deviceid", deviceId));

				post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				HttpResponse response = client.execute(post);
				BufferedReader rd = new BufferedReader(new InputStreamReader(
						response.getEntity().getContent()));

				String line = "";
				while ((line = rd.readLine()) != null) {
					Log.e("HttpResponse", line);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		public String md5(String s) {
		    try {
		        // Create MD5 Hash
		        MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
		        digest.update(s.getBytes());
		        byte messageDigest[] = digest.digest();
		        
		        // Create Hex String
		        StringBuffer hexString = new StringBuffer();
		        for (int i=0; i<messageDigest.length; i++)
		            hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
		        return hexString.toString();
		        
		    } catch (NoSuchAlgorithmException e) {
		        e.printStackTrace();
		    }
		    return "";
		}
}
