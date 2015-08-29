package com.iiit.messwithme;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.iiit.messwithme.adapters.ExpandableListAdapter;
import com.iiit.messwithme.constants.Constants;
import com.iiit.messwithme.network.FetchMenus;
import com.iiit.messwithme.widget.MyWidgetService;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends Activity implements Constants{
	   /*
	    * integer_month - current month (0 base)
	    * month - current month in string format
	    * year - current year (YYYY)
	    * dat- current day (s,m,t,w,t,f,s) - 0 base
	    */
	  
	   ExpandableListAdapter listAdapter;
	   ExpandableListView expListView;
	   List<String> listDataHeader;
	   HashMap<String, List<String>> listDataChild;
	   String username,name;
	   TextView tv;
	   static SharedPreferences s_sharedPref;

		 @Override
			protected void onCreate(Bundle savedInstanceState) {
				super.onCreate(savedInstanceState);
				setContentView(R.layout.activity_main);
				
				startService(new Intent(MainActivity.this,MyWidgetService.class));
											 
				s_sharedPref = getSharedPreferences(SHARED_PREF_APP, Context.MODE_PRIVATE);
				
				/*
				 * set up expandable list
				 */
		        expListView = (ExpandableListView) findViewById(R.id.expandableListView1);
		        prepareListData();
		        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
		        expListView.setAdapter(listAdapter);
		        
				tv = (TextView) findViewById(R.id.textView1);
				username = s_sharedPref.getString(SHARED_PREF_USER_NAME, "N/A");
				String[] list = {"Login","View Mess (Offline)","View Mess Menu"};
				String[] list2 = {"About Us"};
				tv.setText(username);
				
				ListView lv = (ListView) findViewById(R.id.listView1);
				lv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list));
				lv.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						Intent intent = null;
						/*
						 * 0 - Go to login page
						 * 1 - View offline registration
						 * 2 - View mess menus
						 1
						 */
						switch(arg2) {
							case 0:{
								intent = new Intent(getBaseContext(), LoginActivity.class);
								startActivity(intent);	
								break;
							}
							case 1:{
								intent = new Intent(getBaseContext(), ViewMessOfflineActivity.class);
								startActivity(intent);	
								break;
							}
							case 2: {
								/*
						         * Fetch menus if not in sharedPref storage
						         */
								intent = new Intent(getBaseContext(), ViewMessWeekOptions.class);
								startActivity(intent);	
							}
						}
					}
				});
				
				ListView lv2 = (ListView) findViewById(R.id.listView2);
				lv2.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list2));
				lv2.setOnItemClickListener(new OnItemClickListener() {
				
				/*
				 * View "About Us" page
				 * (non-Javadoc)
				 * @see android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget.AdapterView, android.view.View, int, long)
				 */
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						Intent intent = new Intent(getBaseContext(), AboutUsActivity.class);
						startActivity(intent);
					}
				});
			}
		 
		 /*
		  * set last login and offline billl on activity restart
		  * @see android.app.Activity#onRestart()
		  */
		 @Override
			protected void onRestart() {
			 	tv.setText(s_sharedPref.getString(SHARED_PREF_USER_NAME, "N/A"));
				prepareListData();
				listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
		        expListView.setAdapter(listAdapter);
				super.onRestart();
			}
		 
		 /*
		  * Prepare expandable list
		  */
		 private void prepareListData() {
		        listDataHeader = new ArrayList<String>();
		        listDataChild = new HashMap<String, List<String>>();
		   	    String formatted_output = s_sharedPref.getString(SHARED_PREF_USER_BILL, "");
		        String[] menu = formatted_output.split("--");
		         // Adding child data
		        listDataHeader.add("View Bill (Offline)");
			    List<String> child = new ArrayList<String>();
			    if (! formatted_output.equals("")){    
			        for (String s: menu) { 
			          if (s.length()==0 || Integer.parseInt(s.split("[.]")[1])==0)
			        	  break;
			          child.add(s);
			        }
			    }
			    else {
			    	child.add("Please Log in to Get Bill");
			    }
		        listDataChild.put(listDataHeader.get(0), child);
		       
		    }
		 
		 /*
		  * Fetch mess menus if not in SharedPref
		  */
		public void getMenus() {
			new FetchMenus(MainActivity.this).execute();	
		}
		
	}
