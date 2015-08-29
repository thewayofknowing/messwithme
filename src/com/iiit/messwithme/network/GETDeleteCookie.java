package com.iiit.messwithme.network;

import java.io.IOException;
import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.iiit.messwithme.constants.Constants;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

/*
 * Common Background task for all http post requests
 * output of each api is handled in the switch statement of onPostExecute
 */

public class GETDeleteCookie extends AsyncTask<String, String, String> implements Constants{

	Activity s_activity;
	String s_url="",s_userName="";
	SharedPreferences s_sharedPref;
	
	public GETDeleteCookie(Activity activity) {
		this.s_activity = activity;
		this.s_userName = s_activity.getSharedPreferences(SHARED_PREF_APP, Context.MODE_PRIVATE).getString(SHARED_PREF_USER_NAME,"");
		s_sharedPref = s_activity.getSharedPreferences(SHARED_PREF_APP, Context.MODE_PRIVATE);
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}
	
	@Override
	protected String doInBackground(String... params) {
        HttpClient Client = new DefaultHttpClient();
		String result = "";
		try {
			HttpGet httpget = new HttpGet(api_reverse + "?url=unlink&user=" + URLEncoder.encode(s_userName));
		    HttpResponse response = Client.execute(httpget);
		    int responseCode = response.getStatusLine().getStatusCode();
			if (responseCode==403) {
				return "403";
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return "error:" + e.toString();
		} catch (IOException e) {
			e.printStackTrace();
			return "error:" + e.toString();
		}
		return result.toString();
	}
	
	@Override
	protected void onPostExecute(String result) {
		Log.d(TAG,"DElete cookie Result: " + result);
		super.onPostExecute(result);
	}
	
}

