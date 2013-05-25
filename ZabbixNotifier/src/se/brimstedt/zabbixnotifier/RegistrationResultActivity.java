//package se.brimstedt.zabbixnotifier;
//
//import com.googlecode.androidannotations.annotations.EActivity;
//
//import android.app.Activity;
//import android.os.Bundle;
//import android.widget.Toast;
//
//@EActivity(R.layout.activity_result)
//public class RegistrationResultActivity extends Activity
//{
//	@Override
//	protected void onCreate(Bundle savedInstanceState)
//	{
//		//		setContentView(R.layout.activity_result);
//		Bundle extras = getIntent().getExtras();
//		if (extras != null)
//		{
//			String registrationId = extras.getString("registration_id");
//			if (registrationId != null && registrationId.length() > 0)
//			{
//				Toast.makeText(this, "Registered for receiving notifications", Toast.LENGTH_LONG).show();
//			}
//		}
//
//		super.onCreate(savedInstanceState);
//	}
//}
