package com.jpb.probono;

import com.jpb.probono.utility.PBLogger;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class ContactPreferencesActivity extends PreferenceActivity {
	private static final String className = "ContactPreferencesFragment";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		String TAG = className + ".onCreate";
		PBLogger.entry(TAG);
		super.onCreate(savedInstanceState);

		getFragmentManager()
				.beginTransaction()
				.replace(android.R.id.content, new ContactPreferencesFragment())
				.commit();
		PBLogger.exit(TAG);
	}
}
