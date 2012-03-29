package se.brimstedt.zabbixnotifier;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MessageReceivedActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_result);
		Bundle extras = getIntent().getExtras();

		if (extras != null) {
			String server = extras
					.getString("se.brimstedt.zabbixnotifier.server");
			String message = extras
					.getString("se.brimstedt.zabbixnotifier.message");

			Log.d("C2DM", "Payload: " + server + ":" + message);
			if (message != null && message.length() > 0) {
				TextView view = (TextView) findViewById(R.id.server);
				view.setText(server);

				TextView messageView = (TextView) findViewById(R.id.result);
				messageView.setText(message);

			}
		}

		super.onCreate(savedInstanceState);
	}

}
