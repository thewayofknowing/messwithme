package com.iiit.messwithme.widget;

import java.util.Calendar;

import com.iiit.messwithme.MainActivity;
import com.iiit.messwithme.R;
import com.iiit.messwithme.ViewMessWeekOptions;
import com.iiit.messwithme.constants.Constants;
import com.iiit.messwithme.network.FetchMenus;

import android.app.Activity;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.RemoteViews;

public class MyWidgetProvider extends AppWidgetProvider implements Constants{

	static Calendar calendar = Calendar.getInstance();
    static int integer_month = calendar.get(Calendar.MONTH);
    static int s_integerYear = calendar.get(Calendar.YEAR);
    static int day = calendar.get(Calendar.DATE);
    final String[] months = {"January","February","March","April","May","June","July","August","September","October","November","December"};
	static SharedPreferences sharedPref;
	
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
		sharedPref = context.getSharedPreferences(SHARED_PREF_APP, Context.MODE_PRIVATE);
		if(sharedPref.contains(months[integer_month])==false) {
			remoteViews.setTextViewText(R.id.title, "No Registration Found");
			remoteViews.setTextViewText(R.id.content, "");
		}
		else {
			String menu = getMenu(sharedPref.getString(months[integer_month], ""));
			remoteViews.setTextViewText(R.id.title, menu.split(",")[0]);
			remoteViews.setTextViewText(R.id.content, menu.split(",")[1]);
		}
		remoteViews.setOnClickPendingIntent(R.id.container, buildButtonPendingIntent(context));
		ComponentName thisWidget = new ComponentName(context,MyWidgetProvider.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        manager.updateAppWidget(thisWidget, remoteViews);
	}
	
	public static PendingIntent buildButtonPendingIntent(Context context) {
		// initiate widget update request
		Intent intent = new Intent(context, MyWidgetProvider.class);
		intent.setAction(WIDGET_OPEN_ACTIVITY);
		return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
		Log.d(TAG,intent.getAction());
		if(intent.getAction().equals("android.appwidget.action.APPWIDGET_ENABLED")) {
			 context.startService(new Intent(context,MyWidgetService.class));
		}
		else if(intent.getAction().equals(WIDGET_UPDATE_ACTION)) {
			Log.d(TAG,"Received inside");
			RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
			sharedPref = context.getSharedPreferences(SHARED_PREF_APP, Context.MODE_PRIVATE);
			if(sharedPref.contains(months[integer_month])==false) {
				remoteViews.setTextViewText(R.id.title, "No Registration Found");
				remoteViews.setTextViewText(R.id.content, "");
			}
			else {
				String menu = getMenu(sharedPref.getString(months[integer_month], ""));
				remoteViews.setTextViewText(R.id.title, menu.split(",")[0]);
				remoteViews.setTextViewText(R.id.content, menu.split(",")[1]);
			}		
			remoteViews.setOnClickPendingIntent(R.id.container, buildButtonPendingIntent(context));
			
			ComponentName thisWidget = new ComponentName(context,MyWidgetProvider.class);
	        AppWidgetManager manager = AppWidgetManager.getInstance(context);
	        manager.updateAppWidget(thisWidget, remoteViews);
		}
		else if(intent.getAction().equals(WIDGET_OPEN_ACTIVITY)) {
        	Intent viewMenuIntent = new Intent(context, ViewMessWeekOptions.class);
			viewMenuIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
			context.startActivity(viewMenuIntent);
		}
		else if(intent.getAction().equals("android.appwidget.action.APPWIDGET_DELETED")) {
			 context.stopService(new Intent(context,MyWidgetService.class));
		}
	}
	
	public static String getMenu(String source) {
	   String[] menu = source.split("--");
       for (String s: menu) { 
	         if (s=="")
	       	  break;
	         String[] item = s.split(",");
	         String[] dateparameters = item[0].split(" ");
	         if (Integer.parseInt(dateparameters[0])==day) {
	        	 return new String(item[0] + "," + item[1]+"\n"+item[2]+"\n"+item[3]);
	         }
       }
	 return "";
	}
	
}
