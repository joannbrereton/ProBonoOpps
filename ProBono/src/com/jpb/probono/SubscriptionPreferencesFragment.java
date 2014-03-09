package com.jpb.probono;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.jpb.probono.utility.PBLogger;

public class SubscriptionPreferencesFragment extends PreferenceFragment {
	
	private static final String className = "SubscriptionPreferencesFragment";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		String TAG = className + ".onCreate";
		PBLogger.entry(TAG);
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.subscriptionprefs);
		PBLogger.exit(TAG);
	}
}
