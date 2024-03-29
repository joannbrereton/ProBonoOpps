package com.jpb.probono.helper;

import java.util.Date;
import java.util.HashMap;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.jpb.probono.constants.Constants;
import com.jpb.probono.rest.model.ContactInfo;
import com.jpb.probono.utility.PBLogger;

public class PreferencesHelper {
	private static final String className = "PrefHelper";

	public static boolean isContactInfoReady(Context context) {
		String TAG = "isContactInfoReady";
		PBLogger.entry(TAG);
		ContactInfo contactInfo = getContactInfoFromPreferences(context);
		PBLogger.i(TAG, "contactInfo = " + contactInfo);
		boolean ready = false;
		if (contactInfo.getEmail() != null && !contactInfo.getEmail().isEmpty()) {
			ready = true;
		}
		PBLogger.i(TAG, "ContactInfo ready = " + ready);
		PBLogger.exit(TAG);
		
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
		String TAG = className + ".isCategoryChoiceReady";
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
		String TAG = "getContactInfoPref";
		PBLogger.entry(TAG);
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
		PBLogger.i(TAG,"ContactInfo = " + contactInfo);
		PBLogger.exit(TAG);
		return contactInfo;
	}

	public static boolean isSubscribed(Context context) {
			return false;
	}

	public static void setUpdatedSince(Context context) {
		
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = settings.edit();
		editor.putLong(Constants.lastUsage, new Date(/*NOW*/).getTime());
		editor.commit();
		
	}

}
