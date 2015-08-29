package com.iiit.messwithme.adapters;

import java.net.URLEncoder;

import com.iiit.messwithme.CancelMessActivity;
import com.iiit.messwithme.ChangeMessDatewiseActivity;
import com.iiit.messwithme.ChangeMessDaywiseActivity;
import com.iiit.messwithme.MainActivity;
import com.iiit.messwithme.R;
import com.iiit.messwithme.ViewBillActivity;
import com.iiit.messwithme.ViewMessRegistrationActivity;
import com.iiit.messwithme.constants.Constants;
import com.iiit.messwithme.network.GETBillInfo;
import com.iiit.messwithme.network.GETMessRegistration;
import com.iiit.messwithme.widget.MyWidgetProvider;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


/*
 * Adapter for Navigation Drawer
 * Layout: drawer_layout.xml
 */

public class NavBarAdapter extends BaseAdapter implements Constants{

	private Activity s_activity;
	
	public NavBarAdapter(Activity activity) {
		s_activity = activity;
	}

	@Override
	public int getCount() {
		return navbar_options.length;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = s_activity.getLayoutInflater();
		convertView = inflater.inflate(R.layout.drawer_layout, null);
		TextView l_tv = (TextView) convertView.findViewById(R.id.content);
		l_tv.setText(navbar_options[position]);
		ImageView l_iv = (ImageView) convertView.findViewById(R.id.icon);
		l_iv.setBackgroundResource(navbar_drawables[position]);
		
		RelativeLayout parentLayout = (RelativeLayout) convertView.findViewById(R.id.parentLayout);
		if(s_activity.getClass().equals(classes[position])) {
			parentLayout.setBackgroundColor(Color.rgb(204, 61, 61));
		}
		parentLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (position<5 && s_activity.getClass().equals(classes[position])) {
					switch (position) {
					case 0:
						ViewMessRegistrationActivity.mDrawerLayout.closeDrawer(Gravity.LEFT);
						break;
					case 1:
						ChangeMessDatewiseActivity.mDrawerLayout.closeDrawer(Gravity.LEFT);
						break;
					case 2:
						ChangeMessDaywiseActivity.mDrawerLayout.closeDrawer(Gravity.LEFT);
						break;
					case 3:
						CancelMessActivity.mDrawerLayout.closeDrawer(Gravity.LEFT);
						break;
					case 4:
						ViewBillActivity.mDrawerLayout.closeDrawer(Gravity.LEFT);
						break;
					default:
						break;
					}
					return;
				}
				
				switch (position) {
				case 0: {
					new GETMessRegistration(s_activity, url_mess_registration + "&user=" + URLEncoder.encode(s_activity.getSharedPreferences(SHARED_PREF_APP, Context.MODE_PRIVATE).getString(SHARED_PREF_USER_NAME, "")), "Fetching Mess Registration...").execute();
					break;
				}
				case 1: {
					Intent changeMessDatewiseIntent = new Intent(s_activity, ChangeMessDatewiseActivity.class);
					changeMessDatewiseIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
					s_activity.startActivity(changeMessDatewiseIntent);
					break;
				}
				case 2: {
					Intent changeMessDaywiseIntent = new Intent(s_activity, ChangeMessDaywiseActivity.class);
					changeMessDaywiseIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
					s_activity.startActivity(changeMessDaywiseIntent);
					break;
				}
				case 3: {
					Intent cancelMessIntent = new Intent(s_activity, CancelMessActivity.class);
					cancelMessIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
					s_activity.startActivity(cancelMessIntent);
					break;
				}
				case 4: {
					new GETBillInfo(s_activity, "Fetching Mess Bill...").execute();
					break;
				}
				case 5: {
					//Alert dialog builder for booking confirm prompt
					Builder builder = new AlertDialog.Builder(s_activity)
				    .setTitle("Confirm Logout")
				    .setMessage(R.string.logout)
				    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				      
				    	public void onClick(DialogInterface dialog, int which) {
				    		s_activity.getSharedPreferences(SHARED_PREF_APP, Context.MODE_PRIVATE).edit().clear().commit();
							Intent pIntent = new Intent(s_activity, MyWidgetProvider.class);
							pIntent.setAction(WIDGET_UPDATE_ACTION);
							s_activity.sendBroadcast(pIntent);
							Intent logoutIntent = new Intent(s_activity, MainActivity.class);
							logoutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
							s_activity.startActivity(logoutIntent);
				        }
				     })
				    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				        public void onClick(DialogInterface dialog, int which) { 
				            dialog.cancel();
				        }
				     })
				    .setIcon(android.R.drawable.ic_dialog_alert);
					builder.show();
					break;
				}
				default:
					break;
				}
			}
		});
		
		return convertView;
	}
	
}
