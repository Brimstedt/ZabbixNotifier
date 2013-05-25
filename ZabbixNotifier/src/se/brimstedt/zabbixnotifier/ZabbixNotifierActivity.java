package se.brimstedt.zabbixnotifier;

import se.brimstedt.zabbixnotifier.dao.IServerList;
import se.brimstedt.zabbixnotifier.dao.ServerConfiguration;
import android.app.ListActivity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.inject.Inject;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.RoboGuice;

@EActivity(R.layout.main)
@RoboGuice
public class ZabbixNotifierActivity extends ListActivity
{

	public final static String AUTH = "authentication";
	private ArrayAdapter<ServerConfiguration> arrayAdapter;
//	private PersitanceServiceSqlLiteImpl serverDatabase;
	
	@Inject
	private IServerList serverList;

	@AfterViews
	public void afterViews()
	{

		arrayAdapter = new ServerAdapter(this, R.layout.server_item, serverList);
//		populateServerList(arrayAdapter);

		setListAdapter(arrayAdapter);

		ListView lv = getListView();
		lv.setTextFilterEnabled(true);

		lv.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				Log.d("C2DM", "Add new server");
				Intent intent = new Intent(parent.getContext(), EditServerActivity_.class);
				intent.putExtra(EditServerActivity.SERVER_TO_EDIT, arrayAdapter.getItem(position).getServerId());
				startActivity(intent);

				// When clicked, show a toast with the TextView text
//				Toast.makeText(getApplicationContext(), ((TextView) view).getText(), Toast.LENGTH_SHORT).show();
			}
		});
	}


	@Click
	public void addServer(View view)
	{ 
		Log.d("C2DM", "Add new server");
		Intent intent = new Intent(this, EditServerActivity_.class);
		intent.putExtra(EditServerActivity.SERVER_TO_EDIT, serverList.getNewServerId());
		startActivity(intent);
  
	}


}
