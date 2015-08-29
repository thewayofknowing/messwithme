package com.iiit.messwithme.widget;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.iiit.messwithme.constants.Constants;

public class MyWidgetService extends Service implements Constants {
	
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Calendar c = Calendar.getInstance();
		long now = c.getTimeInMillis();
		c.add(Calendar.DATE, 1);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		long millisecondsUntilMidnight = c.getTimeInMillis()+2000 - now;
		
		int minutes = (int) (millisecondsUntilMidnight/(1000*60));
		int hours = minutes/60;
		Log.d(TAG,"hours: " + hours + ", minutes: " + minutes%60);
		
		Intent pIntent = new Intent();
		pIntent.setAction(WIDGET_UPDATE_ACTION);
		
		AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, millisecondsUntilMidnight, 24*60*60*1000, PendingIntent.getBroadcast(getBaseContext(), 0, pIntent, 0));
		return super.onStartCommand(intent, flags, startId);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	
}
