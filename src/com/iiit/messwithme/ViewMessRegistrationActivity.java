package com.iiit.messwithme;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.iiit.messwithme.adapters.ExpandableListAdapter;
import com.iiit.messwithme.adapters.NavBarAdapter;
import com.iiit.messwithme.constants.Constants;
import com.iiit.messwithme.network.GETMessRegistration;
import com.iiit.messwithme.network.POSTCancelMess;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ExpandableListView.ExpandableListContextMenuInfo;

public class ViewMessRegistrationActivity extends CustomActivity implements Constants{

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    String date, year;
    int newmonth,newyear;
    Builder builder;
	
    public static DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
    
    static Calendar calendar = Calendar.getInstance();
    static int integer_month = calendar.get(Calendar.MONTH);
    static String s_stringMonth = "";
    static String s_currentMonth = "";
    static int s_integerYear = calendar.get(Calendar.YEAR);
    static int day = calendar.get(Calendar.DAY_OF_MONTH);
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.viewmess);
        initDrawer();
        initAlert();
        
        expListView = (ExpandableListView) findViewById(R.id.expandableListView1);
        date = getIntent().getExtras().getString("date");
        final String[] months = {"January","February","March","April","May","June","July","August","September","October","November","December"};
        s_stringMonth = date.split(" ")[0];
        s_currentMonth = months[integer_month]; 
        year = date.split(" ")[1];
    	
    	
    	newmonth = 0;
		for (String s: months) {
			if (s_stringMonth.equals(s)) {
				break;  
			}
			newmonth++;
		}
		
		/*
		 * load source code for mess registration
		 */
		Bundle extras = getIntent().getExtras();
    	String source = extras.getString("source");
        TextView tv = (TextView) findViewById(R.id.textView1);
        ImageButton left = (ImageButton) findViewById(R.id.imageButton1);
        ImageButton right = (ImageButton) findViewById(R.id.imageButton2);
        tv.setText(s_stringMonth + " " + year);
        
        /*
         * left and right buttons to change month
         */
        
        left.setOnClickListener(new  OnClickListener() {
			
			@Override
			public void onClick(View v) {
				newmonth--;
				if (newmonth < 0) {
					newmonth = 11;
					newyear = Integer.parseInt(year)-1;
				}
				else {
					newyear = Integer.parseInt(year);
				}
				new GETMessRegistration(ViewMessRegistrationActivity.this, url_mess_registration + URLEncoder.encode("?month="+(newmonth+1)+"&year="+newyear) + "&user=" + URLEncoder.encode(getSharedPreferences(SHARED_PREF_APP, Context.MODE_PRIVATE).getString(SHARED_PREF_USER_NAME, "")), "Fetching Mess Registration...").execute();

			}

		});
        
        right.setOnClickListener(new  OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				newmonth++;
				if (newmonth == 12) {
					newmonth = 0;
					newyear = Integer.parseInt(year)+1;
				}
				else {
					newyear = Integer.parseInt(year);
				}
				new GETMessRegistration(ViewMessRegistrationActivity.this, url_mess_registration + URLEncoder.encode("?month="+(newmonth+1)+"&year="+newyear) + "&user=" + URLEncoder.encode(getSharedPreferences(SHARED_PREF_APP, Context.MODE_PRIVATE).getString(SHARED_PREF_USER_NAME, "")), "Fetching Mess Registration...").execute();

			}

		});

        /*
         * prepare list view
         */
        prepareListData(source,date);
        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
        expListView.setAdapter(listAdapter);
        if( extras.containsKey("day") ){
        	int groupPos = extras.getInt("day")-1;
        	expListView.setSelectionFromTop(groupPos,0);
			expListView.expandGroup(groupPos);
        }
        else if (newmonth == integer_month) {
			
        	expListView.setSelectionFromTop(day-1,0);
			expListView.expandGroup(day-1);
//				expListView.post(new Runnable() {
//				 
//		            @Override
//		            public void run() {
//		            	expListView.expandGroup(day-1);
//		                expListView.smoothScrollToPositionFromTop(day-1, 0, 40*(day-1));;
//		            }
//		        });
		}
        
        registerForContextMenu(expListView);

	}
	
	private void initAlert() {
		//Alert dialog builder for booking confirm prompt
		builder = new AlertDialog.Builder(ViewMessRegistrationActivity.this)
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
	     NavBarAdapter l_leftNavBarListAdapter = new NavBarAdapter(ViewMessRegistrationActivity.this);
	     
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
	
		/*
	    * call cancel function (cancel/uncancel) on context item selection
	    * @see android.app.Activity#onContextItemSelected(android.view.MenuItem)
	    */
	    public boolean onContextItemSelected(MenuItem item) {
	        ExpandableListContextMenuInfo info = (ExpandableListContextMenuInfo) item.getMenuInfo();

	        int type = ExpandableListView.getPackedPositionType(info.packedPosition);
	        if (type == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
	            int groupPos = ExpandableListView.getPackedPositionGroup(info.packedPosition);
	            int childPos = ExpandableListView.getPackedPositionChild(info.packedPosition);
	            int uncancel = item.getItemId();
	            cancel(groupPos+1, childPos, uncancel+"",groupPos);
	            return true;
	        } 
	        else  
	        	return false;
	    }
	
	    /*
	     * create context options - cancel/uncancel
	     * @see android.app.Activity#onCreateContextMenu(android.view.ContextMenu, android.view.View, android.view.ContextMenu.ContextMenuInfo)
	     */
	    @Override
	    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
	    	ExpandableListView.ExpandableListContextMenuInfo info =
	    		    (ExpandableListView.ExpandableListContextMenuInfo) menuInfo;
	    	int type =
	    			    ExpandableListView.getPackedPositionType(info.packedPosition);
	    	if (type == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
		        menu.add(0, 0, 0, "Cancel");
		        menu.add(0, 1, 0, "UnCancel");
	    	}
	    }
	    
	    /*
	     * create form parameters for cancellation and send 
	     */
	    public void cancel (int changedate , int meal, String uncancel,int groupPos) {
	        List<NameValuePair> paramList = new ArrayList<NameValuePair>();
	        String start_enddate = changedate + "-" + s_stringMonth.toUpperCase(Locale.US) + "-" + year;
			String[] meals = {"breakfast","lunch","dinner"};

			paramList.add(new BasicNameValuePair("startdate",start_enddate));
			paramList.add(new BasicNameValuePair("enddate",start_enddate));
			paramList.add(new BasicNameValuePair(meals[meal],"1"));
			paramList.add(new BasicNameValuePair("uncancel",uncancel));
			try {
				new POSTCancelMess(ViewMessRegistrationActivity.this, paramList, "Cancelling Mess...", changedate, url_mess_registration + URLEncoder.encode("?month="+(newmonth+1)+"&year="+year) + "&user=" + URLEncoder.encode(getSharedPreferences(SHARED_PREF_APP, Context.MODE_PRIVATE).getString(SHARED_PREF_USER_NAME, ""))).execute();
			} catch (Exception e) {
				e.printStackTrace();
			}
	    }
	   
	    /*
	     * Preparing the list data
	     */
	    private void prepareListData(String source , String date) {
	        listDataHeader = new ArrayList<String>();
	        listDataChild = new HashMap<String, List<String>>();
		        
	        String[] menu = source.split("--");
	         // Adding child data
	        int count = 0;
	        for (String s: menu) { 
	          if (s=="")
	        	  break;
	          String[] item = s.split(",");
	          String[] dateparameters = item[0].split(" ");
	          if (s_currentMonth.equalsIgnoreCase(s_stringMonth) 
	        		  && Integer.parseInt(dateparameters[0])==day) {
	        	listDataHeader.add("Today ");
	          }
	          else {
	          	listDataHeader.add(item[0]);
	          }
	          List<String> child = new ArrayList<String>();
	          child.add(item[1]);
	          child.add(item[2]);
	          child.add(item[3]);
	          listDataChild.put(listDataHeader.get(count), child); // Header, Child data
	          count++;
	        }
	    }
	    
	//Extract Mess Registration
	private String calcRegistration(Element table) {
		String Bill = "";
	    Elements children = table.children();
	    Element tbody = children.get(1);
	    Elements rows = tbody.children();
	    for(int index=1;index<rows.size();index++) {
	    	Element row = rows.get(index);
	    	Elements cells = row.getElementsByClass("textual");
//	    	Log.d(TAG,cells.get(0).text());
	    }
//		Log.d(TAG,Bill);
		return Bill;	
	}
			
}
