package com.jpb.probono;

import com.jpb.probono.utility.PBLogger;

import android.app.Activity;
import android.os.Bundle;

public class OverlayActivity extends Activity {
	
	private final String className = "StartActivity";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		String TAG = className + ".onCreate";
		PBLogger.entry(TAG);
		super.onCreate(savedInstanceState);
		PBLogger.exit(TAG);
	}
}
