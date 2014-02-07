package com.jpb.probono.service;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.jpb.probono.R;
import com.jpb.probono.constants.Constants;
import com.jpb.probono.utility.PBLogger;

/**
 * 
 * @author brereton
 * 
 *         This class, which is registered in the AndroidManifest.xml to
 *         received Boot Completion messages runs the Opportunity Seeker service
 *         in background on Android, polling every few hours or so (dependent on
 *         the user preferences), to see if there are new opportunities in the
 *         database.
 * 
 */
public class PushSubscribedOpportunityStartupBroadcastReceiver extends
		BroadcastReceiver {
	// Restart service every 30 seconds
	public static final String className = "OpportunityStartupBR";

	@Override
	public void onReceive(Context context, Intent intent) {
		String TAG = className + ".onReceive";
		PBLogger.entry(TAG);

		// We should only set up the service if the user has indicated
		// that they
		// want to be periodically notified, within the Preferences.

		if (isNotificationPreferred(context)) {
			PBLogger.i(TAG,"isNotificationPreferred returned true." );
			
			int days = this.getNotificationIntervalDays(context);
			AlarmManager service = (AlarmManager) context
					.getSystemService(Context.ALARM_SERVICE);
			Intent i = new Intent(context,
					PushSubscribedOpportunityRepeatingBroadcastReceiver.class);

			PendingIntent pending = PendingIntent.getBroadcast(context, 0, i,
					PendingIntent.FLAG_CANCEL_CURRENT);
			Calendar cal = Calendar.getInstance();
			// Start 30 seconds after boot completed
			cal.add(Calendar.SECOND, 30);
			// InexactRepeating allows Android to optimize the energy
			// consumption
			service.setInexactRepeating(AlarmManager.RTC_WAKEUP,
					cal.getTimeInMillis(), days * Constants.MILLISECONDS_IN_A_DAY, pending);

			// service.setRepeating(AlarmManager.RTC_WAKEUP,
			// cal.getTimeInMillis(),
			// REPEAT_TIME, pending);
			PBLogger.i(TAG, "timer set to repeat every " + days + " days.");
		}
		else
		{
			PBLogger.i(TAG,"isNotificationPreferred returned false.");
		}
		PBLogger.exit(TAG);

	}

	private boolean isNotificationPreferred(Context context) {
		String TAG = className + ".isNotificationPreferred";
		PBLogger.entry(TAG);
		boolean preferred = false;
		SharedPreferences sharedPrefs = PreferenceManager
				.getDefaultSharedPreferences(context);

		String pref = sharedPrefs.getString(context.getResources().getString(R.string.pref_sync_frequency_key), "0");
		PBLogger.i(TAG, "pref = " + pref);
		if (pref.equals("0"))
			preferred = false;
		else
			preferred = true;
		
		PBLogger.exit(TAG);
		return preferred;

	}
	
	private int getNotificationIntervalDays(Context context) {
		String TAG = className + ".getNotificationIntervalDays";
		PBLogger.entry(TAG);
		int days = 0;
		SharedPreferences sharedPrefs = PreferenceManager
				.getDefaultSharedPreferences(context);

		String sDays = sharedPrefs.getString(context.getResources().getString(R.string.pref_sync_frequency_key), "0");
		PBLogger.i(TAG, "sDays = " + sDays);
		days = Integer.parseInt(sDays);
		PBLogger.i(TAG, "days = " + days);
			
		PBLogger.exit(TAG);
		return days;	
	}

}
