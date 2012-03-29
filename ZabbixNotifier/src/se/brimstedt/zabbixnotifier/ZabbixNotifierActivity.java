package se.brimstedt.zabbixnotifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.R.array;
import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ZabbixNotifierActivity extends ListActivity {

	private static final String PREFS_NEXT_ID = "NextId";
	public final static String AUTH = "authentication";
	private ArrayAdapter<String> arrayAdapter;

	// Example Activity to trigger a request for a registration ID to the Google
	// server
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		arrayAdapter = new ArrayAdapter<String>(this, R.layout.server_item);
		populateServerList(arrayAdapter);
		
		setListAdapter(arrayAdapter);

		  ListView lv = getListView();
		  lv.setTextFilterEnabled(true);

		  lv.setOnItemClickListener(new OnItemClickListener() {
		    public void onItemClick(AdapterView<?> parent, View view,
		        int position, long id) {
		      // When clicked, show a toast with the TextView text
		      Toast.makeText(getApplicationContext(), ((TextView) view).getText(),
		          Toast.LENGTH_SHORT).show();
		    }
		  });
	}

	private void populateServerList(ArrayAdapter<String> arrayAdapter2) {
		arrayAdapter2.add("test");

		SharedPreferences settings = getSharedPreferences(
				ZabbixNotifier.PREFS_SERVER_LIST, 0);
		Map<String, ?> all = settings.getAll();
		for(Object item : all.entrySet())
		{
			arrayAdapter2.add(item.toString());
		}
	}

	public void addServer(View view) {

		Log.d("C2DM", "Add new server");
		Intent intent = new Intent(this, EditServerActivity.class);
		intent.putExtra("se.brimstedt.zabbixnotifier.serverToEdit",
				getNextServerId());
		startActivity(intent);
		
		
	}

	private int getNextServerId() {
		SharedPreferences settings = getSharedPreferences(
				ZabbixNotifier.PREFS_SERVER_LIST, MODE_PRIVATE);
		int id = settings.getInt(PREFS_NEXT_ID, 0);
		settings.edit().putInt(PREFS_NEXT_ID, id + 1).commit();
		
		return id;
	}


}