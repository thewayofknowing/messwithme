package com.iiit.messwithme.constants;

import com.iiit.messwithme.CancelMessActivity;
import com.iiit.messwithme.ChangeMessDatewiseActivity;
import com.iiit.messwithme.ChangeMessDaywiseActivity;
import com.iiit.messwithme.MainActivity;
import com.iiit.messwithme.R;
import com.iiit.messwithme.ViewBillActivity;
import com.iiit.messwithme.ViewMessRegistrationActivity;

/*
 * Constants.. only constants
 * Change API here if you want
 */

public interface Constants {

	public final String TAG = "MessLog";
	
	public static final String SHARED_PREF_APP = "MyPreferences";
	public static final String SHARED_PREF_APP_MENU = "MyPreferencesMenu";
	public final String SHARED_PREF_USER_EMAIL = "User Email";
	public final String SHARED_PREF_USER_MAIL_SERVER = "Mail Type";
	public final String SHARED_PREF_USER_NAME = "User Name";
	public final String SHARED_PREF_USER_PASSWORD = "User Password";
	public final String SHARED_PREF_SAVED_CREDENTIALS = "Saved Cred";
	public final String SHARED_PREF_USER_BILL = "User Bill";
	public final String SHARED_PREF_WIDGET_ID = "Widget id";
	
	public final String WIDGET_UPDATE_ACTION = "com.iiit.messwithme.WIDGET_UPDATE_ACTION";
	public final String WIDGET_OPEN_ACTIVITY = "com.iiit.messwithme.WIDGET_OPEN_ACTIVITY";

	public final String api_reverse = "https://web.iiit.ac.in/~kartik.kohli/messWithme.php";
	public final String url_cas = api_reverse + "?url=https%3A%2F%2Flogin.iiit.ac.in%2Fcas%2Flogin%3Fservice%3Dhttps%253A%252F%252Fmess.iiit.ac.in%252Fmess%252Fweb%252Findex.php";
	public final String url_submit_cas = api_reverse + "?url=https%3A%2F%2Flogin.iiit.ac.in/cas/login";
	public final String url_index = api_reverse + "?url=https%3A%2F%2Fmess.iiit.ac.in%2Fmess%2Fweb%2Findex.php";
	public final String url_mess_registration = api_reverse + "?url=http%3A%2F%2Fmess.iiit.ac.in%2Fmess%2Fweb%2Fstudent_view_registration.php";
	public final String url_mess_bill = api_reverse + "?url=https%3A%2F%2Fmess.iiit.ac.in%2Fmess%2Fweb%2Fstudent_mess_bill.php";
	public final String url_mess_cancel = api_reverse + "?url=https%3A%2F%2Fmess.iiit.ac.in%2Fmess%2Fweb%2Fstudent_cancel_process.php";
	public final String url_mess_change = api_reverse + "?url=https%3A%2F%2Fmess.iiit.ac.in%2Fmess%2Fweb%2Fstudent_change_mess_process.php";
	
    static String[] months = {"JAN","FEB","MAR","APR","MAY","JUN","JUL","AUG","SEP","OCT","NOV","DEC"};
	public final String USER_AGENT = "Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.0.1) Gecko/2008071615 Fedora/3.0.1-1.fc9 Firefox/3.0.1";
	public final String PROPERTY_REG_ID = "registration_id";
	public final String[] menu = {"ff","gf","nbh","yukta"};
	
	public final String connectionError = "Please check your Internet Connection";
	public final String casError = "CAS Session Expired";
	public final String cas_logout = "Logout successful";
	
	public final String[] navbar_options = {
		"View Mess Registration",
		"Change Mess Datewise",
		"Change Mess Daywise",
		"Cancel Mess",
		"View Mess Bill",
		"Logout"
	};
	
	public final int[] navbar_drawables = {
		R.drawable.eye,
		R.drawable.calendar,
		R.drawable.calendar_day,
		R.drawable.cancel,
		R.drawable.bill,
		R.drawable.logout
	};
	
	public final Class[] classes = {
		ViewMessRegistrationActivity.class,
		ChangeMessDatewiseActivity.class,
		ChangeMessDaywiseActivity.class,
		CancelMessActivity.class,
		ViewBillActivity.class,
		MainActivity.class
	};
}
