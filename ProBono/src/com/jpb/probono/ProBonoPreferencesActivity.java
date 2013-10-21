package com.jpb.probono;

import java.util.HashMap;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;

import com.jpb.probono.constants.Constants;
import com.jpb.probono.preference.ListPreferenceMultiSelect;

public class ProBonoPreferencesActivity extends PreferenceActivity implements
		OnSharedPreferenceChangeListener {

	private HashMap<String, String> prefKeyToSummary = new HashMap<String, String>();

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		Resources resources = this.getResources();
		prefKeyToSummary.put(Constants.PREF_KEY_EMAIL,
				resources.getString(R.string.summary_email_preference));
		prefKeyToSummary.put(Constants.PREF_KEY_FIRSTNAME,
				resources.getString(R.string.summary_firstName_preference));
		prefKeyToSummary.put(Constants.PREF_KEY_LASTNAME,
				resources.getString(R.string.summary_lastName_preference));
		prefKeyToSummary.put(Constants.PREF_KEY_PHONE, resources
				.getString(R.string.summary_telephone_number_preference));
		prefKeyToSummary.put(Constants.PREF_KEY_FIRM,
				resources.getString(R.string.summary_firm_preference));
		prefKeyToSummary.put(Constants.PREF_CATEGORIES,
				resources.getString(R.string.promptForPreferredCategories));
		prefKeyToSummary.put(Constants.PREF_STATES,
				resources.getString(R.string.promptForPreferredStates));

		
	}

	// Set Preference Change listeners
	@SuppressWarnings("deprecation")
	@Override
	protected void onResume() {
		super.onResume();
		// Set up a listener whenever a key changes
		getPreferenceScreen().getSharedPreferences()
				.registerOnSharedPreferenceChangeListener(this);

		this.updatePreference(Constants.PREF_KEY_EMAIL);
		this.updatePreference(Constants.PREF_KEY_FIRSTNAME);
		this.updatePreference(Constants.PREF_KEY_LASTNAME);
		this.updatePreference(Constants.PREF_KEY_PHONE);
		this.updatePreference(Constants.PREF_KEY_FIRM);
		this.updatePreference(Constants.PREF_CATEGORIES);
		this.updatePreference(Constants.PREF_STATES);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onPause() {
		super.onPause();
		// Unregister the listener whenever a key changes
		getPreferenceScreen().getSharedPreferences()
				.unregisterOnSharedPreferenceChangeListener(this);
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		updatePreference(key);
	}

	private void updatePreference(String key) {
		if (prefKeyToSummary.containsKey(key)) {

			@SuppressWarnings("deprecation")
			Preference preference = findPreference(key);
			if (preference instanceof EditTextPreference) {
				EditTextPreference editTextPreference = (EditTextPreference) preference;
				if (editTextPreference.getText().trim().length() > 0) {
					editTextPreference.setSummary(editTextPreference.getText());
				} else {
					editTextPreference.setSummary(prefKeyToSummary.get(key));
				}
			} else if (preference instanceof com.jpb.probono.preference.ListPreferenceMultiSelect) {
				ListPreferenceMultiSelect listPreference = (ListPreferenceMultiSelect) preference;
				String values = listPreference.getValuesAsString();
				if (values.trim().length() > 0) {
					listPreference.setSummary(values);
				} else {
					listPreference.setSummary(prefKeyToSummary.get(key));
				}

			} else if (preference instanceof ListPreference) {
				
			}

		}

	}

}
