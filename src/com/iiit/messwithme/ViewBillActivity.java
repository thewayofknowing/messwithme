package com.iiit.messwithme;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.iiit.messwithme.adapters.NavBarAdapter;
import com.iiit.messwithme.constants.Constants;

public class ViewBillActivity extends CustomActivity implements Constants{

	List<String> s_billList;
	public static DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
    Builder builder;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewbill);
		prepareList();
		initDrawer();
	    initAlert();
				 
		ListView lv = (ListView) findViewById(R.id.listView1);
		lv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, s_billList));

	}
	
	private void prepareList() {
		s_billList = new ArrayList<String>();
		String formatted_output = getSharedPreferences(SHARED_PREF_APP, Context.MODE_PRIVATE).getString(SHARED_PREF_USER_BILL, "");
        String[] menu = formatted_output.split("--");
       
	    if (! formatted_output.equals("")){    
	        for (String s: menu) { 
	          if (s.length()==0 )
	        	  break;
	          s_billList.add(s);
	        }
	    }
	}
	
	private void initAlert() {
		//Alert dialog builder for booking confirm prompt
		builder = new AlertDialog.Builder(ViewBillActivity.this)
	    .setTitle("Confirm Exit")
	    .setMessage(R.string.exit)
	    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	      
	    	public void onClick(DialogInterface dialog, int which) {
	    		dialog.cancel();
	    		finish();
	        }
	     })
	    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	            dialog.cancel();
	        }
	     })
	    .setIcon(android.R.drawable.ic_dialog_alert);
	}
	
	//Navigation drawer initialization
	private void initDrawer() {
		 mDrawerList = (ListView) findViewById(R.id.drawer_list);
	     NavBarAdapter l_leftNavBarListAdapter = new NavBarAdapter(ViewBillActivity.this);
	     
		 mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
		 mDrawerList.setAdapter(l_leftNavBarListAdapter);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		
		case android.R.id.home:
		{
			if(!mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
				mDrawerLayout.openDrawer(Gravity.LEFT);
				mDrawerList.bringToFront();
				mDrawerList.requestLayout();
			}
			else {
				mDrawerLayout.closeDrawer(Gravity.LEFT);
			}
		}
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onBackPressed() {
		if(mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
			mDrawerLayout.closeDrawer(Gravity.LEFT);
		}
		else {
			builder.show();
		}
	}
	
}
