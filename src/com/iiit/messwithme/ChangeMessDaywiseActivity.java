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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;


/* Change mess daywise page 
 * 	 3spinners (mess option/day/meal option) 
*/
public class ChangeMessDaywiseActivity extends CustomActivity implements Constants {
		
	private Spinner spinner1,spinner2,spinner3;
		String day = "Mon";
		String mealname = "1";
		String messname = "1";
		String[] days = {"Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"};
		String[] days_short = {"Mon","Tue","Wed","Thu","Fri","Sat","Sun"};
		String[] meals = {"Breakfast","Lunch","Dinner"};
		List<String> mess;
		
		public static DrawerLayout mDrawerLayout;
		private ListView mDrawerList;
	    Builder builder;
		
		protected void onCreate(Bundle savedInstanceState) {
				super.onCreate(savedInstanceState);
				setContentView(R.layout.changedaywise);
				initDrawer();
			    initAlert();
			   			    
			    mess = new ArrayList<String>();
			    mess.add(getString(R.string.obh_south));
			    mess.add(getString(R.string.obh_north));
			    mess.add(getString(R.string.yukta));
			    mess.add(getString(R.string.nbh));
			    mess.add(getString(R.string.canteen));
			    mess.add(getString(R.string.nbh_egg));
			    
				/* Get references to page elements */
				spinner1 = (Spinner) findViewById(R.id.spinner1);
				spinner2 = (Spinner) findViewById(R.id.spinner2);
				spinner3 = (Spinner) findViewById(R.id.spinner3);
				Button b = (Button) findViewById(R.id.button1);
				
				/* Add items on the 3 spinners */
				addItemsOnSpinner();
				addListenerOnSpinnerItemSelection();
				final List<NameValuePair> paramList = new ArrayList<NameValuePair>();

				b.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						/* clear post parameters from previous submissions*/
						paramList.clear();
						/* prepare post parameters */
						paramList.add(new BasicNameValuePair("Daily","1"));
						paramList.add(new BasicNameValuePair("day",day));
						paramList.add(new BasicNameValuePair("meal_name",mealname));
						paramList.add(new BasicNameValuePair("mess_name",messname));
						
						/* Send post parameters */
						new POSTChangeMess(ChangeMessDaywiseActivity.this, paramList, "Changing Mess...").execute();
						
					}
				});
		}

public void addItemsOnSpinner() {
	
	/* Add day(mon/tue...) options on spinner 1 */
	List<String> list1 = new ArrayList<String>();
	for (String s: days) {
		list1.add(s);
	}
	
	ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(this,
		android.R.layout.simple_spinner_item, list1);
	dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	spinner1.setAdapter(dataAdapter1);
	
	/* Add meal (bf/lunch/dinner) options on spinner 2 */
	List<String> list2 = new ArrayList<String>();
	for (String s: meals) {
		list2.add(s);
	}
	ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this,
			android.R.layout.simple_spinner_item, list2);
		dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner2.setAdapter(dataAdapter2);
	
	/* Add mess options on spinner 3 */	
	List<String> list3 = new ArrayList<String>();
	for (String s: mess) {
		list3.add(s);
	}
	ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<String>(this,
			android.R.layout.simple_spinner_item, list3);
		dataAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner3.setAdapter(dataAdapter3);
  }
 

  /* OnClick listeners for the 3 spinners */
  public void addListenerOnSpinnerItemSelection() {
	spinner1.setOnItemSelectedListener(new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1,
				int arg2, long arg3) {
			 day = days_short[arg2];
			
		}
		
		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
		}
		
	});
	spinner2.setOnItemSelectedListener(new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
				mealname = "" + (arg2+1);
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			
		}
	});
	spinner3.setOnItemSelectedListener(new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
				messname = "" + (arg2+1);
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			
		}
	});
  }
  
  private void initAlert() {
		//Alert dialog builder for booking confirm prompt
		builder = new AlertDialog.Builder(ChangeMessDaywiseActivity.this)
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
	     NavBarAdapter l_leftNavBarListAdapter = new NavBarAdapter(ChangeMessDaywiseActivity.this);
	     
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