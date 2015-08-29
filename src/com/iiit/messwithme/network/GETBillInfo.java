package com.iiit.messwithme.network;

import java.io.IOException;
import java.net.URLEncoder;

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
import com.iiit.messwithme.ViewBillActivity;
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

public class GETBillInfo extends AsyncTask<String, String, String> implements Constants{

	Activity s_activity;
	String s_url="",s_userName="";
	Editor s_editor;
	SharedPreferences s_sharedPref;
	ProgressDialog s_progressBar;
	String s_param,s_message;
	
	public GETBillInfo(Activity activity, String message) {
		this.s_activity = activity;
		this.s_progressBar = new ProgressDialog(s_activity);
		this.s_message = message;
		s_sharedPref = s_activity.getSharedPreferences(SHARED_PREF_APP, Context.MODE_PRIVATE);
		s_userName = s_sharedPref.getString(SHARED_PREF_USER_NAME,"");
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
			HttpGet httpget = new HttpGet(url_mess_bill + "&user=" + URLEncoder.encode(s_userName));
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
		Log.d(TAG,result.toString());
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
			Element billTable = null;
		    Document doc = Jsoup.parse(result);
			Elements table = doc.getElementsByTag("table");
			for(Element e: table) {
				if(e.hasAttr("align") && e.attr("align").equals("center")
					&& e.hasAttr("cellpadding") && e.attr("cellpadding").equals("2")
					&& e.hasAttr("border") && e.attr("border").equals("1")) {
					billTable = e;
					break;
				}
			}
			String Bill = "";
			Bill = calcbill(billTable);
			s_editor.putString(SHARED_PREF_USER_BILL, Bill).commit();
			if(s_message.length()>0) {
				Intent intent = new Intent(s_activity,ViewBillActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
				s_activity.startActivity(intent);
			}
		}
		else if(result.indexOf(cas_logout)>=0) {
			if(s_message.length()>0) {
				Toast.makeText(s_activity, casError, Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(s_activity,LoginActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
				s_activity.startActivity(intent);
			}
		}
		else {
			Toast.makeText(s_activity, connectionError, Toast.LENGTH_SHORT).show();
		}
		super.onPostExecute(result);
	}
	

	//Extract bill amounts from source code
		private String calcbill(Element table) {
			String Bill = "";
		    Elements tbody = table.children();
			Elements rows = tbody.get(0).children();
			for(int index=1;index<rows.size();index++) {
				Element row = rows.get(index);
				String BillE = "";
				Elements cells = row.children();
				BillE += cells.get(0).text().replace("<br>",	" ") + ": Rs.";
				int cost = Integer.parseInt(cells.last().text().split("[.]")[0]);
				BillE += cost;
				BillE += "--";
				//Log.i(TAG,BillE);
				Bill += BillE;
			}
			return Bill;	
		}
	
}
