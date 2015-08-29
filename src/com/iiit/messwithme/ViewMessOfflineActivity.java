package com.iiit.messwithme;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import com.iiit.messwithme.adapters.ExpandableListAdapter;
import com.iiit.messwithme.constants.Constants;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ExpandableListView;
import android.widget.TextView;


public class ViewMessOfflineActivity extends CustomActivity implements Constants{
	 
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    String month;
    static Calendar calendar = Calendar.getInstance();
    static int integer_month = calendar.get(Calendar.MONTH);
    static int s_integerYear = calendar.get(Calendar.YEAR);
    static int day = calendar.get(Calendar.DATE);
    final String[] months = {"January","February","March","April","May","June","July","August","September","October","November","December"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewmessoffline);
        
        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.expandableListView1);
        month = months[integer_month];
        
        /*
         * get string for offline mess registration
         */
        SharedPreferences sharedPref = getSharedPreferences(SHARED_PREF_APP, Context.MODE_PRIVATE);
    	String source = sharedPref.getString(month, "");
        TextView tv = (TextView) findViewById(R.id.textView1);
        
        if (source.equals("")) {
        	tv.setText("Please Login to Get Latest Registration");
        }
        else if (source.split(",", 3)[0].split(" ")[1].equals(month)) {
	        tv.setText(month + " " + s_integerYear);
	        // preparing list data
	        prepareListData(source);
	        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
	        expListView.setAdapter(listAdapter);
	        expListView.setSelectionFromTop(day-1,0);
			expListView.expandGroup(day-1);
//	        expListView.post(new Runnable() {
//				 
//	            @Override
//	            public void run() {
//	            	expListView.expandGroup(day-1);
//	                expListView.smoothScrollToPositionFromTop(day-1, 0, 40*(day-1));;
//	            }
//	        });
        }
        else {
        	tv.setText("Please Login to Get Latest Registration ");
        }
      }
    
    
    /*
     * Preparing the list data
     */
    private void prepareListData(String source) {
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
          if (Integer.parseInt(dateparameters[0])==day) {
        	listDataHeader.add("Today");
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
    

}