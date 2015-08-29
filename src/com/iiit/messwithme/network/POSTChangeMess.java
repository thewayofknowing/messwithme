package com.iiit.messwithme.network;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.iiit.messwithme.LoginActivity;
import com.iiit.messwithme.adapters.HttpURLConnectionExample;
import com.iiit.messwithme.constants.Constants;

/*
 * Common Background task for all http post requests
 * output of each api is handled in the switch statement of onPostExecute
 */

public class POSTChangeMess extends AsyncTask<String, String, String> implements Constants{

	Activity s_activity;
	List<NameValuePair> s_nameValuePairs;
	String s_userName;
	ProgressDialog s_progressBar;
	String s_param,s_message;
	
	public POSTChangeMess(Activity activity, List<NameValuePair> nameValuePairs,String message) {
		this.s_activity = activity;
		this.s_nameValuePairs = nameValuePairs;
		this.s_progressBar = new ProgressDialog(s_activity);
		this.s_message = message;
		this.s_userName = s_activity.getSharedPreferences(SHARED_PREF_APP, Context.MODE_PRIVATE).getString(SHARED_PREF_USER_NAME, "");
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
		try {
			HttpClient client = HttpURLConnectionExample.getNewHttpClient();
			HttpPost post = new HttpPost(url_mess_change + "&user=" + URLEncoder.encode(s_userName));
			post.setHeader("User-Agent", USER_AGENT);
			post.setHeader("Accept", 
		             "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			post.setHeader("Accept-Language", "en-US,en;q=0.5");
			//post.setHeader("Cookie", com.iiit.messwithme.adapters.HttpURLConnectionExample.getCookies());
			post.setHeader("Connection", "keep-alive");
			post.setHeader("Referer", "https://login.iiit.ac.in");
			post.setHeader("Content-Type", "application/x-www-form-urlencoded");
			
			post.setEntity(new UrlEncodedFormEntity(s_nameValuePairs));
			
			HttpResponse response = null;
			
			response = client.execute(post);
			
		 
			/*
			 * return error response code 
			 */
			int responseCode = response.getStatusLine().getStatusCode();
			if(responseCode!=200) {
				return "403";
			}
			BufferedReader rd = null;
			
			rd = new BufferedReader(
				            new InputStreamReader(response.getEntity().getContent()));
			
		 
			StringBuffer result = new StringBuffer();
			String line = "";
			
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
			return result.toString();
		}
		catch (Exception e) {
			return "error";
		}
		
	}
	
	@Override
	protected void onPostExecute(String result) {
		Log.d(TAG,"Result: " + result);
		
		//Close Loading bar
		s_progressBar.hide();
		
		//Handle output of each post request
		if(result.equals("403")) {
			Toast.makeText(s_activity, "Please remove proxy for web.iiit.ac.in", Toast.LENGTH_SHORT).show();
		}
		else if (result.indexOf("error")<0) {
			Document doc = Jsoup.parse(result);
			Elements msg = doc.getElementsByAttribute("color");
			Log.d(TAG,"" + msg.size());
			for(Element e: msg) {
				if(e.attr("color").equals("green") || e.attr("color").equals("red")) {
					if(e.text().length()>0) {
						Toast.makeText(s_activity, e.text(), Toast.LENGTH_SHORT).show();
						break;
					}
				}
			}
		}
		else if(result.indexOf(cas_logout)>=0) {
			Toast.makeText(s_activity, casError, Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(s_activity,LoginActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
			s_activity.startActivity(intent);
		}
		//Connection broken
		else {
			Toast.makeText(s_activity, connectionError, Toast.LENGTH_SHORT).show();
		}
		super.onPostExecute(result);
	}
			
}