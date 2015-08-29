package com.iiit.messwithme.network;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.iiit.messwithme.adapters.HttpURLConnectionExample;
import com.iiit.messwithme.constants.Constants;

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

public class LoginCAS extends AsyncTask<String, String, String> implements Constants{

	Activity s_activity;
	String s_url;
	SharedPreferences s_sharedPref;
	Editor s_editor;
	ProgressDialog s_progressBar;
	String s_param,s_message;
	HttpClient client;
	
	public LoginCAS(Activity activity, String url, String message) {
		this.s_activity = activity;
		this.s_url = url;
		this.s_progressBar = new ProgressDialog(s_activity);
		this.s_message = message;
		this.s_sharedPref = s_activity.getSharedPreferences(SHARED_PREF_APP, Context.MODE_PRIVATE);
		this.s_editor = s_sharedPref.edit();
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
			client = HttpURLConnectionExample.getNewHttpClient();
			HttpResponse response = makePost(url_cas + "&user=" + URLEncoder.encode(s_sharedPref.getString(SHARED_PREF_USER_NAME, "")),new ArrayList<NameValuePair>());
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
//			Log.d(TAG,"result " + result.toString());
			
			if (result.toString().indexOf("Log In Successful")>=0 || result.toString().indexOf("Student Home")>=0) {
				return "true";
			}
			
//			Log.d(TAG,"sending login params");
			List<NameValuePair> nameValuePairs = new ArrayList<>();
			
			nameValuePairs = getFormParams(result.toString(), s_sharedPref.getString(SHARED_PREF_USER_EMAIL, ""), s_sharedPref.getString(SHARED_PREF_USER_PASSWORD, ""));
			
		    HttpResponse response1 = makePost(url_cas + "&user=" + URLEncoder.encode(s_sharedPref.getString(SHARED_PREF_USER_NAME, "")),nameValuePairs);
			responseCode = response1.getStatusLine().getStatusCode();
			if(responseCode!=200) {
				return "403";
			}
			
			rd = new BufferedReader(
				            new InputStreamReader(response1.getEntity().getContent()));
		 
			result = new StringBuffer();
			line = "";
			
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
//			Log.d(TAG,"result: " + result.toString());
		    Document doc = Jsoup.parse(result.toString());
		    if(doc.getElementsByTag("title").get(0).text().equals("Student Home")) {
		    	return "true";
		    }
	
			//Check if Credentials are incorrect
			int start = result.toString().indexOf("class=\"errors\""); 
			if (start >= 0) {
				return "Incorrect Credentials";
			}
		}
		catch (Exception e) {
			Log.d(TAG,e.toString());
			return "error";
		}
		return "";
	}
	
	private HttpResponse makePost(String url, List<NameValuePair> nameValuePairs) {
		HttpPost post = new HttpPost(url);
		post.setHeader("User-Agent", USER_AGENT);
		post.setHeader("Accept", 
	             "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		post.setHeader("Accept-Language", "en-US,en;q=0.5");
		post.setHeader("Connection", "keep-alive");
		post.setHeader("Referer", "https://login.iiit.ac.in");
		post.setHeader("Content-Type", "application/x-www-form-urlencoded");
		try {
			post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		} catch (UnsupportedEncodingException e) {
			Log.d(TAG,e.toString());
		}
		
		HttpResponse response = null;
		try {
			response = client.execute(post);
		} catch (ClientProtocolException e) {
			Log.d(TAG,e.toString());
		} catch (IOException e) {
			Log.d(TAG,e.toString());
		}
		return response;
	}
	
	@Override
	protected void onPostExecute(String result) {
//		Log.d(TAG,"url: " + s_url);
//		Log.d(TAG,"Result: " + result);
		
		//Close Loading bar
		s_progressBar.hide();
		
		//Handle output of each post request
		if(result.equals("403")) {
			Toast.makeText(s_activity, "Please remove proxy for web.iiit.ac.in", Toast.LENGTH_SHORT).show();
		}
		else if(result.equals("Incorrect Credentials")) {
			Toast.makeText(s_activity, "Incorrect Credentials", Toast.LENGTH_SHORT).show();
		}
		else if (result.indexOf("error")<0) {
			if(s_sharedPref.contains(SHARED_PREF_USER_BILL)==false) {
				new GETBillInfo(s_activity, "").execute();
			}
			new GETMessRegistration(s_activity, url_mess_registration + "&user=" + URLEncoder.encode(s_sharedPref.getString(SHARED_PREF_USER_NAME, "")), "Fetching Mess Registration...").execute();
		}
		//Connection broken
		else {
			Toast.makeText(s_activity, connectionError, Toast.LENGTH_SHORT).show();
		}
		super.onPostExecute(result);
	}
	
	/*
	 * Prepare Login post parameters
	 */
	public List<NameValuePair> getFormParams(
	            String html, String email, String password)
				throws IOException {
		
		Document doc = Jsoup.parse(html);
		Element loginform = doc.getElementById("fm1");
		Elements inputElements = loginform.getElementsByTag("input");
		List<NameValuePair> paramList = new ArrayList<NameValuePair>();
		
		for (Element inputElement : inputElements) {
			String key = inputElement.attr("name");
			if(key.equals("username") || key.equals("password")) {
				continue;
			}
			String value = inputElement.attr("value");
			paramList.add(new BasicNameValuePair(key, value));
		}
		paramList.add(new BasicNameValuePair("username", email));
		paramList.add(new BasicNameValuePair("password", password));
		Log.d(TAG,paramList + "");
		return paramList;
	 }
			
}
