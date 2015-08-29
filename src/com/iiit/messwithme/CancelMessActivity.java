package com.iiit.messwithme;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ListView;

import com.iiit.messwithme.adapters.NavBarAdapter;
import com.iiit.messwithme.constants.Constants;
import com.iiit.messwithme.network.POSTCancelMess;

public class CancelMessActivity extends CustomActivity implements Constants {

	private CheckBox s_bfCheckBox,s_lunchCheckBox,s_dinnerCheckBox,s_uncancelCheckBox;
	private DatePicker s_startDatePicker,s_endDatePicker;
	
	public static DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
    Builder builder;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cancelmess);
		 initDrawer();
	     initAlert();
			     
	     /* get references for 3 checkboxes (bf/lunch/diner) 
		 * 					and 2 datepickers (start/end date)
		 */
		
		s_lunchCheckBox = (CheckBox) findViewById(R.id.checkBox1);
		s_bfCheckBox = (CheckBox) findViewById(R.id.checkBox2);
		s_dinnerCheckBox = (CheckBox) findViewById(R.id.checkBox3);
		s_uncancelCheckBox = (CheckBox) findViewById(R.id.checkBox4);
		
		Button b = (Button) findViewById(R.id.button1);
		
		s_startDatePicker = (DatePicker) findViewById(R.id.datePicker1);
		s_endDatePicker = (DatePicker) findViewById(R.id.datePicker2);
		
		s_startDatePicker.setCalendarViewShown(false);
		s_endDatePicker.setCalendarViewShown(false);
		
		final List<NameValuePair> paramList = new ArrayList<NameValuePair>();
		
		/* on click cancel button */
		b.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				paramList.clear();
				
				/* Set up post paramters */
				String startdate = "" + s_startDatePicker.getDayOfMonth() + "-" + months[s_startDatePicker.getMonth()]
						+ "-" + s_startDatePicker.getYear();
				String enddate = "" + s_endDatePicker.getDayOfMonth() + "-" + months[s_endDatePicker.getMonth()]
						+ "-" + s_endDatePicker.getYear();
				if (s_bfCheckBox.isChecked())
					paramList.add(new BasicNameValuePair("breakfast","1"));
				if (s_lunchCheckBox.isChecked())
					paramList.add(new BasicNameValuePair("lunch","1"));
				if (s_dinnerCheckBox.isChecked())
					paramList.add(new BasicNameValuePair("dinner","1"));
				if	(s_uncancelCheckBox.isChecked()) {
					paramList.add(new BasicNameValuePair("uncancel","1"));
				}
				paramList.add(new BasicNameValuePair("startdate",startdate));
				paramList.add(new BasicNameValuePair("enddate",enddate));
			
				cancel(paramList,CancelMessActivity.this);	
				
			}
		});
	}

	 /*
     * create form parameters for cancellation and send 
     */
	private void cancel (List<NameValuePair> paramList, Activity activity) {
		
		/* Call post method */
		new POSTCancelMess(CancelMessActivity.this, paramList, "Cancelling Mess...").execute();

	}
	
	private void initAlert() {
		//Alert dialog builder for booking confirm prompt
		builder = new AlertDialog.Builder(CancelMessActivity.this)
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
	     NavBarAdapter l_leftNavBarListAdapter = new NavBarAdapter(CancelMessActivity.this);
	     
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
