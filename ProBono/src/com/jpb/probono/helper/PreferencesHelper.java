package com.jpb.probono.helper;

import java.util.HashMap;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.jpb.probono.constants.Constants;
import com.jpb.probono.rest.model.ContactInfo;
import com.jpb.probono.utility.PBLogger;

public class PreferencesHelper {
	private static final String className = "PreferenceHelper";

	public static boolean isContactInfoReady(Context context) {
		ContactInfo contactInfo = getContactInfoFromPreferences(context);
		boolean ready = false;
		if (contactInfo.getEmail() != null && !contactInfo.getEmail().isEmpty()) {
			ready = true;
		}
		return ready;
	}

	public static boolean isStateChoiceReady(Context context) {
		String TAG = className + ".isStateChoiceReady";
		boolean ready = false;
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);

		HashMap<String, ?> prefMap = (HashMap<String, ?>) preferences.getAll();
		PBLogger.i(TAG, "prefMap = " + prefMap);
		if (prefMap != null) {
			Object o = prefMap.get(Constants.PREFERRED_STATES) ;
			if (o!=null) PBLogger.i(TAG, o + " is of type: " + o.getClass().getName());
			String prefStates = (String) prefMap
					.get(Constants.PREFERRED_STATES);

			PBLogger.i(TAG, "prefStates = " + prefStates );

			if ((prefStates != null) && !prefStates.isEmpty()) {
				ready = true;
			}
		}
		return ready;
	}

	public static boolean isCategoryChoiceReady(Context context) {
		String TAG = className + ".isStateChoiceReady";
		boolean ready = false;
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);

		HashMap<String, ?> prefMap = (HashMap<String, ?>) preferences.getAll();

		if (prefMap != null) {
			String prefCats = (String) prefMap.get(Constants.PREF_CATEGORIES);

			PBLogger.i(TAG, "prefCats = " + prefCats);

			if ((prefCats != null) && !prefCats.isEmpty()) {
				ready = true;
			}
		}
		return ready;
	}

	public static ContactInfo getContactInfoFromPreferences(Context context) {
		ContactInfo contactInfo = new ContactInfo();
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);

		contactInfo.setFirstName(prefs.getString(Constants.PREF_KEY_FIRSTNAME,
				""));
		contactInfo.setLastName(prefs
				.getString(Constants.PREF_KEY_LASTNAME, ""));
		contactInfo.setEmail(prefs.getString(Constants.PREF_KEY_EMAIL, ""));
		contactInfo.setPhone(prefs.getString(Constants.PREF_KEY_PHONE, ""));
		contactInfo.setFirmName(prefs.getString(Constants.PREF_KEY_FIRM, ""));

		return contactInfo;
	}

	public static boolean isSubscribed(Context context) {
			return false;
	}

}
