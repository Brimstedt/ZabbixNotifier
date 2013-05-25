package se.brimstedt.zabbixnotifier;

import se.brimstedt.zabbixnotifier.dao.IServerList;
import se.brimstedt.zabbixnotifier.dao.ServerConfiguration;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ServerAdapter extends ArrayAdapter<ServerConfiguration> 
{

	Context context;
	int layoutResourceId;
	
	IServerList data = null;
	private final IServerList serverList;

	
	public ServerAdapter(Context context, int layoutResourceId, IServerList serverList) {
		super(context, layoutResourceId);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.serverList = serverList;


		
	}
	
	@Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
       
        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
        }
       
        TextView serverUrl = (TextView) row.findViewById(R.id.server_url);
        ServerConfiguration sc = serverList.getAll().get(position);
        serverUrl.setText(sc.getServerURL());
       
        return row;
    }
	
}
