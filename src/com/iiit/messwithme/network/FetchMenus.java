package com.iiit.messwithme.network;


import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.iiit.messwithme.ViewMessWeekOptions;
import com.iiit.messwithme.constants.Constants;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

/*
 * Common Background task for all http post requests
 * output of each api is handled in the switch statement of onPostExecute
 */

public class FetchMenus extends AsyncTask<String, String, String> implements Constants{
	
	Activity s_activity;
	Editor s_editor;
	ProgressDialog s_progressBar;
	String s_message;
	
	public FetchMenus(Activity activity) {
		this.s_activity = activity;
		s_editor = s_activity.getSharedPreferences(SHARED_PREF_APP_MENU, Context.MODE_PRIVATE).edit();
		//Initialize Loading bar
		s_progressBar = new ProgressDialog(activity);
		s_progressBar.setIndeterminate(true);
		s_progressBar.setCancelable(false);
		s_progressBar.setMessage("Fetching Menus");
	}
	
	@Override
	protected void onPreExecute() {
		s_progressBar.show();
		super.onPreExecute();
	}
	
	@Override
	protected String doInBackground(String... params) {
        HttpClient Client = new DefaultHttpClient();
		int type;
		for(type=0;type<4;type++) {
			String result = "";
			String s_url = "https://web.iiit.ac.in/~kartik.kohli/" + menu[type] + ".txt";
			try {
				HttpGet httpget = new HttpGet(s_url);
                HttpResponse response = Client.execute(httpget);
                int responseCode = response.getStatusLine().getStatusCode();
    			if (responseCode==403) {
    				break;
    			}
    			HttpEntity entity = response.getEntity();
                result = EntityUtils.toString(entity);
    			s_editor.putString(menu[type] + "-menu", result).commit();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
				return "error:" + e.toString();
			} catch (IOException e) {
				e.printStackTrace();
				return "error:" + e.toString();
			}
		}
		Log.d(TAG,"Type: " + type);
		if(type==4) {
			return "true";
		}
		else {
			return "false";
		}
	}
	
	@Override
	protected void onPostExecute(String result) {
		s_progressBar.dismiss();
		if(result.indexOf("error")>=0) {
			Toast.makeText(s_activity, connectionError, Toast.LENGTH_SHORT).show();
		}
		else if(result.equals("true")) {
			Toast.makeText(s_activity, "Fetched Successfully", Toast.LENGTH_SHORT).show();
		}
		else {
			Toast.makeText(s_activity, "Please remove proxy for web.iiit.ac.in", Toast.LENGTH_SHORT).show();
		}
		super.onPostExecute(result);
	}
		
}

