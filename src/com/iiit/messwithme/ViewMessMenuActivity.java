package com.iiit.messwithme;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.iiit.messwithme.adapters.ExpandableListAdapter;
import com.iiit.messwithme.constants.Constants;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.TextView;

public class ViewMessMenuActivity extends Activity implements Constants{
	ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    TextView dialogtext;
    ArrayList<String[]> menu ;
    Dialog d;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.viewmenu);
        		
		expListView = (ExpandableListView) findViewById(R.id.expandableListView1);
        prepareListData();
        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
        expListView.setAdapter(listAdapter);
        final int day = Integer.parseInt(getIntent().getExtras().getString("day"));
        
        /*
         * Strings storing weekly menus
         */
        SharedPreferences sharedPref = getSharedPreferences(SHARED_PREF_APP_MENU, Context.MODE_PRIVATE);
        menu = new ArrayList<String[]>();
        String ffmenu = sharedPref.getString("ff-menu", "").replaceAll(",", "\n");
        menu.add(ffmenu.split("#"));
        String gfmenu = sharedPref.getString("gf-menu", "").replaceAll(",", "\n");
        menu.add(gfmenu.split("#"));
        String nbhmenu = sharedPref.getString("nbh-menu", "").replaceAll(",", "\n");
        menu.add(nbhmenu.split("#"));
        String yuktamenu = sharedPref.getString("yukta-menu", "").replaceAll(",", "\n");
        menu.add(yuktamenu.split("#"));
        

        
        d = new Dialog(ViewMessMenuActivity.this);
        d.setTitle("Menu");
        
        expListView.setOnChildClickListener(new OnChildClickListener() {
        	
        	/*
        	 * extract menu from string stored in sharedPref 
        	 * and display it in a Dialog
        	 * @see android.widget.ExpandableListView.OnChildClickListener#onChildClick(android.widget.ExpandableListView, android.view.View, int, int, long)
        	 */
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				String menuitem = menu.get(groupPosition)[day].split("--")[childPosition];
				LayoutInflater inflater = (LayoutInflater) ViewMessMenuActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		        final View dialogLayout = inflater.inflate(R.layout.dialog, null);
		        dialogtext = (TextView) dialogLayout.findViewById(R.id.textView1);
		        d.setContentView(dialogLayout);
		        dialogtext.setText(" " + menuitem.trim());
				d.show();

				return true;
			}
		});
        
		super.onCreate(savedInstanceState);
	}
    
    /*
     * Preparing the list data
     */
    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();
   
       
          listDataHeader.add(getString(R.string.obh_south));
          listDataHeader.add(getString(R.string.obh_north));
          listDataHeader.add(getString(R.string.nbh));
          listDataHeader.add(getString(R.string.yukta));
          
          List<String> meals= new ArrayList<String>();
          meals.add("Breakfast");
          meals.add("Lunch");
          meals.add("Dinner");
          
          List<String> nmeals= new ArrayList<String>();
          nmeals.add("Breakfast");
          nmeals.add("Lunch");
          nmeals.add("Snacks");
          nmeals.add("Dinner");
          
          listDataChild.put(getString(R.string.obh_south), nmeals); 
          listDataChild.put(getString(R.string.obh_north), meals);
          listDataChild.put(getString(R.string.nbh), nmeals);
          listDataChild.put(getString(R.string.yukta), nmeals);
          
          
        }
       
    }
    

