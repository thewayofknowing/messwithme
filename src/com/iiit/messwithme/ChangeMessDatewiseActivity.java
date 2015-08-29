package com.iiit.messwithme;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.iiit.messwithme.adapters.NavBarAdapter;
import com.iiit.messwithme.constants.Constants;
import com.iiit.messwithme.network.POSTChangeMess;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

/* Datewise change mess page 3checkbox(bf/lunch/dinner) spinner(mess options)
 * 	 2 datepicker (start/end date) 
 */
public class ChangeMessDatewiseActivity extends CustomActivity implements Constants{
	private Spinner spinner;
	
	/* First option is South */
	private String messoption = new String ("1");
	public static DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
    Builder builder;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.changedatewise);
		initDrawer();
	    initAlert();
	    		
		 /* Get references to elements */
		final CheckBox lunch = (CheckBox) findViewById(R.id.checkBox1);
		final CheckBox bf = (CheckBox) findViewById(R.id.checkBox2);
		final CheckBox dinner = (CheckBox) findViewById(R.id.checkBox3);
		Button b = (Button) findViewById(R.id.button1);
		final DatePicker start = (DatePicker) findViewById(R.id.datePicker1);
		final DatePicker end = (DatePicker) findViewById(R.id.datePicker2);
		
		start.setCalendarViewShown(false);
		end.setCalendarViewShown(false);
			
		spinner = (Spinner) findViewById(R.id.spinner1);
		
		/* prepare spinner items */
		addItemsOnSpinner();
		addListenerOnSpinnerItemSelection();
		
		final List<NameValuePair> paramList = new ArrayList<NameValuePair>();
		b.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				/* clear parameters from last (possibly) submission */
				paramList.clear();
				
				/* prepare post parameters */
				String startdate = "" + start.getDayOfMonth() + "-" + months[start.getMonth()]
						+ "-" + start.getYear();
				String enddate = "" + end.getDayOfMonth() + "-" + months[end.getMonth()]
						+ "-" + end.getYear();
				if (bf.isChecked())
					paramList.add(new BasicNameValuePair("breakfast","1"));
				if (lunch.isChecked())
					paramList.add(new BasicNameValuePair("lunch","1"));
				if (dinner.isChecked())
					paramList.add(new BasicNameValuePair("dinner","1"));
				paramList.add(new BasicNameValuePair("startdate",startdate));
				paramList.add(new BasicNameValuePair("enddate",enddate));
				paramList.add(new BasicNameValuePair("mess_name",messoption));
				paramList.add(new BasicNameValuePair("Normal","1"));
				
				/* Send post parameters */
				new POSTChangeMess(ChangeMessDatewiseActivity.this, paramList, "Changing Mess...").execute();
				
			}
		});
	}
	
	/* add mess options on spinner */ 
	public void addItemsOnSpinner() {
		List<String> list = new ArrayList<String>();
		list.add(getString(R.string.obh_south));
		list.add(getString(R.string.obh_north));
		list.add(getString(R.string.yukta));
		list.add(getString(R.string.nbh));
		list.add(getString(R.string.canteen));
		list.add(getString(R.string.nbh_egg));
		
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
			android.R.layout.simple_spinner_item, list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(dataAdapter);
	  }
	 
	  public void addListenerOnSpinnerItemSelection() {
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				 messoption = ""+(arg2+1);
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
			}
			
		});
	  }
	  
	  private void initAlert() {
			//Alert dialog builder for booking confirm prompt
			builder = new AlertDialog.Builder(ChangeMessDatewiseActivity.this)
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
		     NavBarAdapter l_leftNavBarListAdapter = new NavBarAdapter(ChangeMessDatewiseActivity.this);
		     
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
