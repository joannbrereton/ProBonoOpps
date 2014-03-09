package com.jpb.probono;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.jpb.probono.utility.PBLogger;

public class ContactPreferencesFragment extends PreferenceFragment {
	private static final String className = "ContactPreferencesFragment";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		String TAG = className + ".onCreate";
		PBLogger.entry(TAG);
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.contactprefs);
		PBLogger.exit(TAG);
	}

}
