package com.iiit.messwithme;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

public class CustomActivity extends Activity{

	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar bar = getActionBar();
		bar.setIcon(R.drawable.menu);
		bar.setBackgroundDrawable(new ColorDrawable(Color.rgb(34, 34, 34)));
		bar.setHomeButtonEnabled(true);
		
	}
	
}
