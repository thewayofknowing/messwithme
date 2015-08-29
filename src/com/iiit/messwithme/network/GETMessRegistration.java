package com.iiit.messwithme.network;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.URLEncode;
import org.jsoup.select.Elements;

import com.iiit.messwithme.LoginActivity;
import com.iiit.messwithme.MainActivity;
import com.iiit.messwithme.ViewMessRegistrationActivity;
import com.iiit.messwithme.constants.Constants;
import com.iiit.messwithme.widget.MyWidgetProvider;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

/*
 * Common Background task for all http post requests
 * output of each api is handled in the switch statement of onPostExecute
 */

public class GETMessRegistration extends AsyncTask<String, String, String> implements Constants{

	Activity s_activity;
	String s_url="",s_userName="";
	Editor s_editor;
	SharedPreferences s_sharedPref;
	ProgressDialog s_progressBar;
	String s_param,s_message;
	int day=-1;
	
	public GETMessRegistration(Activity activity, String url, String message) {
		this.s_activity = activity;
		this.s_url = url;
		this.s_progressBar = new ProgressDialog(s_activity);
		this.s_message = message;
		this.s_userName = s_activity.getSharedPreferences(SHARED_PREF_APP, Context.MODE_PRIVATE).getString(SHARED_PREF_USER_NAME,"");
		s_sharedPref = s_activity.getSharedPreferences(SHARED_PREF_APP, Context.MODE_PRIVATE);
		s_editor = s_sharedPref.edit();
		//Initialize Loading bar
		s_progressBar.setIndeterminate(true);
		s_progressBar.setCancelable(false);
		s_progressBar.setMessage(message);
	}
	
	public GETMessRegistration(Activity activity, String url, String message, int day) {
		this.s_activity = activity;
		this.s_url = url;
		this.s_progressBar = new ProgressDialog(s_activity);
		this.s_message = message;
		this.day = day;
		this.s_userName = s_activity.getSharedPreferences(SHARED_PREF_APP, Context.MODE_PRIVATE).getString(SHARED_PREF_USER_NAME,"");
		s_sharedPref = s_activity.getSharedPreferences(SHARED_PREF_APP, Context.MODE_PRIVATE);
		s_editor = s_sharedPref.edit();
		//Initialize Loading bar
		s_progressBar.setIndeterminate(true);
		s_progressBar.setCancelable(false);
		s_progressBar.setMessage(message);
	}
	
	@Override
	protected void onPreExecute() {
		if(s_message.length()>0) s_progressBar.show();
		super.onPreExecute();
	}
	
	@Override
	protected String doInBackground(String... params) {
        HttpClient Client = new DefaultHttpClient();
		String result = "";
		try {
			HttpGet httpget = new HttpGet(s_url);
		    HttpResponse response = Client.execute(httpget);
		    int responseCode = response.getStatusLine().getStatusCode();
			if (responseCode==403) {
				return "403";
			}
			HttpEntity entity = response.getEntity();
		    result = EntityUtils.toString(entity);
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
		if (s_progressBar.isShowing()) {
			s_progressBar.hide();		
		}
		
		//Handle output of each post request
		if(result.equals("403")) {
			Toast.makeText(s_activity, "Please remove proxy for web.iiit.ac.in", Toast.LENGTH_SHORT).show();
		}
		else if (result.indexOf("error")<0) {
			Log.d(TAG,result);
		    Document doc = Jsoup.parse(result);
		    if(doc.getElementsByTag("title").get(0).text().indexOf("Initial Registration")>=0) {
		    	Toast.makeText(s_activity, "Set Initial Mess Registration on the portal", Toast.LENGTH_LONG).show();
		    	return;
		    }
		    String date = doc.getElementsByTag("h1").get(1).text().replace("«", "").replace("»", "");
		    Log.d(TAG,date);
		    Element table = doc.getElementsByClass("calendar").first();
		    Intent intent = new Intent(s_activity, ViewMessRegistrationActivity.class);
	        String month = date.replaceAll("[^a-zA-Z]", "");
	        String year =  date.replaceAll("[^0-9]", "");
	        date = month + " " + year;
		    String reg = calcRegistration(table, date);
		    s_editor.putString(month, reg).commit();
		    //update widget
		    Intent pIntent = new Intent(s_activity, MyWidgetProvider.class);
			pIntent.setAction(WIDGET_UPDATE_ACTION);
			s_activity.sendBroadcast(pIntent);
			
		    intent.putExtra("date", date);
		    intent.putExtra("source", reg);
		    if(day!=-1) {
		    	intent.putExtra("day", day);
		    }
		    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
		    s_activity.startActivity(intent);
		} 
		else if(result.indexOf(cas_logout)>=0) {
			Toast.makeText(s_activity, casError, Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(s_activity,LoginActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
			s_activity.startActivity(intent);
		}
		else {
			Toast.makeText(s_activity, connectionError, Toast.LENGTH_SHORT).show();
		}
		super.onPostExecute(result);
	}
	
	//Extract Mess Registration
		private String calcRegistration(Element table, String date) {
			String source = "";
		    Elements children = table.children();
		    Element tbody = children.get(1);
		    Elements rows = tbody.children();
		    for(int index=1;index<rows.size();index++) {
		    	Element row = rows.get(index);
		    	Elements cells = row.getElementsByClass("textual");
		    	for(Element cell: cells) {
		    		String[] elements = cell.text().split(" ");
		    		source += elements[0] + " " + date + ",";
		    		for(int i=1;i<4;i++) {
		    			source += elements[i] + ",";
		    		}
		    		source += "--";
		    	}
		    }
			return source;	
		}
}
