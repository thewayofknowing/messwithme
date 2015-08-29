package com.iiit.messwithme;

import com.iiit.messwithme.constants.Constants;
import com.iiit.messwithme.network.FetchMenus;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

		/*
		 * Create a list view of Day options
		 * open viewmenu page with parameter:day on item click 
		 */
public class ViewMessWeekOptions extends Activity implements Constants{

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.weekoptions);
        
        SharedPreferences sharedPref = getSharedPreferences(SHARED_PREF_APP_MENU, Context.MODE_PRIVATE);
        if (sharedPref.contains("ff-menu")==false || sharedPref.getString("ff-menu", "").length()<=0) {
        	Log.d(TAG,"Fetching menus from web.iiit");
			new FetchMenus(ViewMessWeekOptions.this).execute();	
        }	     
		
		String[] list = {"Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"};
		ListView lv = (ListView) findViewById(R.id.listView1);
		lv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list));
		lv.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(getBaseContext(), ViewMessMenuActivity.class);
				intent.putExtra("day", "" + arg2);
				startActivity(intent);
			}
			
		});
		
		((LinearLayout)findViewById(R.id.refresh)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new FetchMenus(ViewMessWeekOptions.this).execute();	
			}
		});
		
	}

}
