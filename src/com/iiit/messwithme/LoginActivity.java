/**
 * 
 */
package com.iiit.messwithme;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import com.iiit.messwithme.constants.Constants;
import com.iiit.messwithme.network.GETDeleteCookie;
import com.iiit.messwithme.network.LoginCAS;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.opengl.Visibility;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

/**
 * Login Page student id/password - EditText box
 * 			  student/research - spinner
 */
public class LoginActivity extends Activity implements Constants  {
	   static String user,pass,atTheRate,saveduser;
	   Spinner s_spinner;
	   SharedPreferences s_sharedPref;
	   SharedPreferences.Editor s_editor;
	   CheckBox s_rememberMe;
	   EditText s_passwordEditText,s_usernNameEditText;
	   
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.login);
						
			/* Get references to page elements */
			s_usernNameEditText = (EditText) findViewById(R.id.editText2);
			Button b = (Button) findViewById(R.id.button1);
			s_passwordEditText = (EditText) findViewById(R.id.editText1);
			s_spinner = (Spinner) findViewById(R.id.spinner1);
			s_rememberMe = (CheckBox) findViewById(R.id.checkBox1);

			s_sharedPref = getSharedPreferences(SHARED_PREF_APP, Context.MODE_PRIVATE);
			saveduser = s_sharedPref.getString(SHARED_PREF_USER_NAME,"");
			s_usernNameEditText.setText(saveduser);
			
			/* Delete previous cookies */
			new GETDeleteCookie(LoginActivity.this).execute();
			
			/* Prepare form in case "remember me" checked */
			if (Integer.parseInt(s_sharedPref.getString(SHARED_PREF_SAVED_CREDENTIALS, "0")) == 1) {
				s_rememberMe.setChecked(true);
				s_passwordEditText.setText(s_sharedPref.getString(SHARED_PREF_USER_PASSWORD, ""));
			}
			/* @ - default value - students.iiit.ac.in */
			atTheRate = s_sharedPref.getString(SHARED_PREF_USER_MAIL_SERVER, "students.iiit.ac.in");
			addItemsOnSpinner();
			addListenerOnSpinnerItemSelection();
			
			b.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					/* prepare form post elements */
					saveduser = s_usernNameEditText.getText().toString();
					user = saveduser+"@"+atTheRate;
					pass = s_passwordEditText.getText().toString();
					
					s_editor = getSharedPreferences(SHARED_PREF_APP, Context.MODE_PRIVATE).edit();
					s_editor.putString(SHARED_PREF_USER_NAME, saveduser);
					s_editor.putString(SHARED_PREF_USER_EMAIL, user);
					s_editor.putString(SHARED_PREF_USER_MAIL_SERVER, atTheRate);
					s_editor.putString(SHARED_PREF_USER_PASSWORD, pass);
					
					/* Set password in case remember me checked */
					if (s_rememberMe.isChecked()) {
						s_editor.putString(SHARED_PREF_SAVED_CREDENTIALS, "1");
					}
					else {
						s_editor.putString(SHARED_PREF_SAVED_CREDENTIALS, "0");
					}
					s_editor.commit();
					
					/* Send login parameters */
					try {
						List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
						new LoginCAS(LoginActivity.this, url_mess_bill + "&user=" + URLEncoder.encode(saveduser), "Logging in...").execute();
					} catch (Exception e) {
						e.printStackTrace();
						Toast.makeText(getBaseContext(), e.toString(),Toast.LENGTH_SHORT).show();
					}
					
				}
			});
		}
		
		
		/* Spinner for @ option
		 * students/research .iiit.ac.in
		 */
		public void addItemsOnSpinner() {
			 
			List<String> list = new ArrayList<String>();
			if (s_sharedPref.getString(SHARED_PREF_USER_MAIL_SERVER, "students.iiit.ac.in").indexOf("student")>=0) {
				list.add("students.iiit.ac.in");
				list.add("research.iiit.ac.in");
			}
			else {
				list.add("research.iiit.ac.in");
				list.add("students.iiit.ac.in");
			}
			ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, list);
			dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			s_spinner.setAdapter(dataAdapter);
		  }
		 
		  public void addListenerOnSpinnerItemSelection() {
			s_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					if (arg2 == 0) {
						if(atTheRate.indexOf("student")>=0) {
							atTheRate = "students.iiit.ac.in";
						}
						else {
							atTheRate = "research.iiit.ac.in";
						}
						
					}
					else {
						if (atTheRate.indexOf("student")>=0) {
							atTheRate = "research.iiit.ac.in";
						}
						else {
							atTheRate = "students.iiit.ac.in";
						}
					}
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					
				}
				
			});
		  }
		
}
