package com.jpb.probono.service;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.preference.PreferenceManager;

import com.jpb.probono.OpportunityListActivity;
import com.jpb.probono.R;
import com.jpb.probono.constants.Constants;
import com.jpb.probono.exception.PBException;
import com.jpb.probono.helper.OpportunityListHelper;
import com.jpb.probono.helper.PreferencesHelper;
import com.jpb.probono.rest.model.Opportunity;
import com.jpb.probono.rest.model.OpportunityQueryParameterList;
import com.jpb.probono.utility.PBLogger;

@SuppressLint("HandlerLeak")
public class PushSubscribedOpportunityRepeatingBroadcastReceiver extends
		BroadcastReceiver {

	private static Resources resources = null;
	private Context context = null;
	private static String className = "OpportunityRptinBdcstRecvr";

	// Start here....
	@Override
	public void onReceive(Context context, Intent intent) {
		String TAG = className + ".onReceive";
		PBLogger.entry(TAG);
		this.context = context;

		boolean preCheck = true;
		// User has to have chosen preferred categories and states for this to
		// work.
		// If we don't pass this precheck, no query is attempted.
		if (!PreferencesHelper.isCategoryChoiceReady(context)) {
			PBLogger.i(TAG, resources.getString(R.string.noSubscribedCats));
			preCheck = false;
		}

		if (!PreferencesHelper.isStateChoiceReady(context)) {
			PBLogger.i(TAG, resources.getString(R.string.noSubscribedStates));
			preCheck = false;
		}

		PBLogger.i(TAG,
				"preCheck for calling opportunityListService from background = "
						+ preCheck);
		if (preCheck == true) {

			resources = context.getResources();
			Intent service = new Intent(context, OpportunityListService.class);

			// Create a new Messenger for the communication back
			// Pass in the listQueryParms....
			// result comes back to handler
			Messenger messenger = new Messenger(oppsHandler);
			service.putExtra(Constants.MESSENGER, messenger);

			SharedPreferences preferences = PreferenceManager
					.getDefaultSharedPreferences(context);

			String cats = null;
			String states = null;

			HashMap<String, ?> prefMap = (HashMap<String, ?>) preferences
					.getAll();

			cats = (String) prefMap.get(Constants.PREFERRED_CATEGORIES);
			states = (String) prefMap.get(Constants.PREFERRED_STATES);

			PBLogger.i(TAG, "preparing parameter list with cats = " + cats
					+ " states = " + states);

			OpportunityQueryParameterList parameterList = OpportunityListHelper
					.buildParameterList(this.context, cats, states, true);

			// put the parameterlist containing the preferred cats and states into the service bundle.
			service.putExtra(Constants.LIST_QUERY_PARMS, parameterList);
			PBLogger.i(TAG, "About to start service with parameterList ="
					+ parameterList);

			// Call the service that queries for opportunities according to preferred cats and states.
			// The return message will be handled via the oppsHandler defined below.  
			context.startService(service);

		}
		PBLogger.exit(TAG);
	}

	// Query result comes back to here from service.
	private Handler oppsHandler = new Handler() {
		public void handleMessage(Message message) {
			String TAG = className + ".handleMessage";
			PBLogger.entry(TAG);
			String json = (String) message.obj;

			// Something comes back from the Find Opportunities service...
			if (message.arg1 != android.app.Activity.RESULT_OK) {
				// bad return value, log it.
				PBLogger.e(TAG,
						"A repeat broadcast call to opportunity finder has failed, returning "
								+ message.arg1);
			} else if (json != null && !json.isEmpty()) {
				// parse the json to determine if there are actual opportunities
				// listed.
				// Note: resources has been set in the onReceive.
				ArrayList<Opportunity> opps = null;
				try {
					opps = OpportunityListHelper.parseOpportunityList(
							resources, json);
					PBLogger.i(TAG, "returned " + opps.size() + "opportunities");
				} catch (PBException e) {
					PBLogger.e(TAG, "Error parsing opportunities, resources = "
							+ resources + ",  json = " + json);
					e.printStackTrace();
				}

				// Notify the user if more than one opportunity has returned.  
				if (opps != null && opps.size() > 0) {
					notifyUserOfNewOpportunities(opps.size());
				}
			} else {
				PBLogger.i(TAG, "No results since last service call.");
			}
			PBLogger.exit(TAG);

		}

	};

	// Notify the user of new opportunities (re-runs the query, when user clicks).
	@SuppressWarnings("deprecation")
	private void notifyUserOfNewOpportunities(int size) {
		String TAG = className + ".notifyUserOfNewOpportunities";

		PBLogger.i(TAG, "There are " + size + " new opportunities found.");
		Intent oppListIntent = new Intent(
				PushSubscribedOpportunityRepeatingBroadcastReceiver.this.context,
				OpportunityListActivity.class);

		// Add in arguments

		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(this.context);

		String cats = null;
		String states = null;

		if (preferences != null) {
			HashMap<String, ?> prefMap = (HashMap<String, ?>) preferences
					.getAll();

			if (prefMap != null) {
				cats = (String) prefMap.get(Constants.PREFERRED_CATEGORIES);
				states = (String) prefMap.get(Constants.PREFERRED_STATES);
			}
		}

		PBLogger.i(TAG, "states = " + states + " cats = " + cats);

		// Getting ready to call opportunities REST service...build the parameters

		OpportunityQueryParameterList listQueryParms = OpportunityListHelper
				.buildParameterList(this.context, cats, states, true);
		oppListIntent.putExtra(Constants.LIST_QUERY_PARMS, listQueryParms);

		// Build a PendingIntent...this intent will run when user clicks the notification.
		// It lists the opportunities for the preferred categories and states.   
		PendingIntent pIntent = PendingIntent
				.getActivity(
						PushSubscribedOpportunityRepeatingBroadcastReceiver.this.context,
						0, oppListIntent, PendingIntent.FLAG_CANCEL_CURRENT);

		// Send a notification
		NotificationManager mgr = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);

		Notification notification = new Notification(R.drawable.pbp1,
				resources.getString(R.string.notification_title),
				System.currentTimeMillis());
				
		notification.setLatestEventInfo(context,
				resources.getString(R.string.notification_title),
				resources.getString(R.string.notification_text), pIntent);
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		mgr.notify(0, notification);
		PBLogger.i(TAG, "Notification sent.");
	}

}
