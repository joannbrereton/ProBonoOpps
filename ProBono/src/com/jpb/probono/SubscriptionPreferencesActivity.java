package com.jpb.probono;

import com.jpb.probono.utility.PBLogger;

import android.app.Activity;
import android.os.Bundle;

public class SubscriptionPreferencesActivity extends Activity {
	private static final String className = "SubscriptionPreferencesFragment";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		String TAG = className + ".onCreate";
		PBLogger.entry(TAG);
		super.onCreate(savedInstanceState);

		getFragmentManager()
				.beginTransaction()
				.replace(android.R.id.content, new SubscriptionPreferencesFragment())
				.commit();
		PBLogger.exit(TAG);
	}
}
