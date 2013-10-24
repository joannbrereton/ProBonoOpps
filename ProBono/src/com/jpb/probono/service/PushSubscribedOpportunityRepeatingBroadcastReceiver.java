package com.jpb.probono.service;

import java.util.HashMap;
import java.util.HashSet;

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
import android.widget.Toast;

import com.jpb.probono.HomeActivity;
import com.jpb.probono.R;
import com.jpb.probono.constants.Constants;
import com.jpb.probono.helper.OpportunityListHelper;
import com.jpb.probono.rest.model.OpportunityQueryParameterList;
import com.jpb.probono.utility.PBLogger;

@SuppressLint("HandlerLeak")
public class PushSubscribedOpportunityRepeatingBroadcastReceiver extends
		BroadcastReceiver {

	private static Resources resources = null;
	private Context context = null;
	private static String className = "PushSubscribedOpportunityRepeatingBroadcastReceiver";
	private Handler oppsHandler = new Handler() {
		@SuppressWarnings("deprecation")
		public void handleMessage(Message message) {
			String TAG = className + ".handleMessage";
			PBLogger.entry(TAG);
			String json = (String) message.obj;
			Context context = PushSubscribedOpportunityRepeatingBroadcastReceiver.this.context;
			if (message.arg1 == android.app.Activity.RESULT_OK && json != null) {
				PBLogger.i(TAG, "There are new opportunities found" + json);
				Intent i = new Intent(
						PushSubscribedOpportunityRepeatingBroadcastReceiver.this.context,
						HomeActivity.class);
				PendingIntent pIntent = PendingIntent
						.getActivity(
								PushSubscribedOpportunityRepeatingBroadcastReceiver.this.context,
								0, i, PendingIntent.FLAG_CANCEL_CURRENT);
				// Send a notification
				NotificationManager mgr = (NotificationManager) context
						.getSystemService(Context.NOTIFICATION_SERVICE);
				Notification notification = new Notification(
						R.drawable.pbp1,
						resources.getString(R.string.notification_title),
						System.currentTimeMillis());
				// Set the Notification Info
				notification.setLatestEventInfo(context,
						resources.getString(R.string.notification_title),
						resources.getString(R.string.notification_text),
						pIntent);
				notification.flags |= Notification.FLAG_AUTO_CANCEL;
				mgr.notify(0, notification);
				PBLogger.i(TAG, "Notification sent.");

			} else {
				PBLogger.i(TAG, "No results since last service call.");
			}
			PBLogger.exit(TAG);

		}

	};

	@SuppressWarnings("unchecked")
	@Override
	public void onReceive(Context context, Intent intent) {
		String TAG = className + ".onReceive";
		PBLogger.entry(TAG);
		resources = context.getResources();
		this.context = context;
		Intent service = new Intent(context, OpportunityListService.class);

		// Create a new Messenger for the communication back
		// Pass in the listQueryParms....
		// result comes back to handler
		Messenger messenger = new Messenger(oppsHandler);
		service.putExtra(Constants.MESSENGER, messenger);
		
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		
		HashMap<String,?> prefMap =(HashMap<String,?>)preferences.getAll();
			
		HashSet<String> cats = (HashSet<String>)prefMap.get(Constants.PREFERRED_CATEGORIES);
		HashSet<String> states = (HashSet<String>)prefMap.get(Constants.PREFERRED_STATES);

		OpportunityQueryParameterList parameterList = OpportunityListHelper
				.buildParameterList(this.context,cats, states, true);
		if (parameterList.getCategories() == null
				|| parameterList.getCategories().equals("")
				|| parameterList.getStates() == null
				|| parameterList.getStates().equals("")) {

			Toast.makeText(context, resources.getString(R.string.noSubscribed),
					Toast.LENGTH_LONG).show();
			return;

		}
		service.putExtra(Constants.LIST_QUERY_PARMS, parameterList);
		PBLogger.i(TAG, "About to start service");

		context.startService(service);
		PBLogger.exit(TAG);
	}

}
