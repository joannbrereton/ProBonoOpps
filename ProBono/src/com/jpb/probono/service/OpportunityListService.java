package com.jpb.probono.service;

import java.util.ArrayList;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.widget.Toast;

import com.jpb.probono.R;
import com.jpb.probono.constants.Constants;
import com.jpb.probono.exception.PBException;
import com.jpb.probono.rest.model.OpportunityQueryParameterList;
import com.jpb.probono.utility.PBLogger;
import com.jpb.probono.utility.RestClient;
import com.jpb.probono.utility.RestClient.RequestMethod;

public class OpportunityListService extends IntentService {

	private static Resources resources = null;
	private static final String className = "OpportunityListService";
	private CallGetOpportunityListWebServiceTask task = null;
	private Intent intent = null;

	public OpportunityListService() {
		super(className);
	}

	public OpportunityListService(String name) {
		super(name);
		
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		this.intent = intent;
		String TAG = className + ".onHandleIntent";
		PBLogger.entry(TAG);
		resources = getResources();
		OpportunityQueryParameterList queryParms= intent.getExtras().getParcelable(Constants.LIST_QUERY_PARMS);
		PBLogger.i(TAG, queryParms.toString());
		// Call web service to get opportunities, pass along parameters. 
		// 
		task = new CallGetOpportunityListWebServiceTask();
		task.execute(queryParms);
		// see post-execute in CallWebServiceTask for what happens next...

		PBLogger.exit(TAG);
	}

	public class CallGetOpportunityListWebServiceTask extends
			AsyncTask<Object, Object, Object> {

		@Override
		protected void onPreExecute() {
		//	this.dialog = ProgressDialog.show(applicationContext,
		//			resources.getString(R.string.calling),
		//			resources.getString(R.string.gettingListOfOpportunities),
		//			true);
		}

		@Override
		protected void onPostExecute(Object result) {
			String TAG = className + ".onPostExecute";
			PBLogger.entry(TAG);
			if (result != null) {
				Bundle extras = intent.getExtras();
				if (extras != null) {
					Messenger messenger = (Messenger) extras.get(Constants.MESSENGER);
					Message msg = Message.obtain();
					msg.arg1 = Activity.RESULT_OK;
					msg.obj = result;
					// Return results back to Activity
					try {
						messenger.send(msg);
					} catch (android.os.RemoteException e1) {
						Toast.makeText(OpportunityListService.this,resources.getString(R.string.errorTryingToGetOpportunitiesFromService), Toast.LENGTH_LONG).show();
						PBLogger.w(getClass().getName(),
								"Exception sending message", e1);
						e1.printStackTrace();
					}
					PBLogger.i(TAG,"message sent to service.");

				}
			} else {
				Toast.makeText(
						OpportunityListService.this,
						resources.getString(R.string.noResults),
						Toast.LENGTH_LONG).show();
			}
			PBLogger.exit(TAG);

			
		}

		@Override
		protected Object doInBackground(Object... params) {
			String TAG = className + ".doInBackground";
			String json = null;
			PBLogger.entry(TAG);
			OpportunityQueryParameterList listParms = (OpportunityQueryParameterList) params[0];
			PBLogger.i(TAG,
					"querying with" + listParms);

			try {
				json = OpportunityListService.getAllOpportunities(listParms);
			} catch (PBException e) {
				// already toasted
				PBLogger.e(TAG,
						resources
								.getString(R.string.errorTryingToGetOpportunitiesFromService));
				
			}
			PBLogger.exit(TAG);
			return json;
		}

	}

	public static String getAllOpportunities(
			OpportunityQueryParameterList paramList) throws PBException {

		String TAG = className + ".getAllOpportunities";
		String responseString = null;
		PBLogger.entry(TAG);
		
		// Put together endpoint and query parameters
		String endpoint = resources
				.getString(R.string.getOpportunitiesEndpoint);
		RestClient client = new RestClient(endpoint);

		// Now, get the parameters from the paramList passed.

		ArrayList<String> cats = paramList.getCategories();

		if (cats != null && cats.size() > 0) {
			StringBuffer queryCatParmsSB = new StringBuffer("");
			for (String cat : cats) {
				queryCatParmsSB.append(cat);
				queryCatParmsSB.append(Constants.PARAMETER_SEPARATOR);
			}
			// remove last separator
			queryCatParmsSB.deleteCharAt(queryCatParmsSB.length() - 1);
			client.AddParam(Constants.QUERY_CATEGORY,
					queryCatParmsSB.toString());
		}

		ArrayList<String> states = paramList.getStates();
		if (states != null && states.size() > 0) {
			StringBuffer queryStateParmsSB = new StringBuffer("");
			for (String state : states) {
				queryStateParmsSB.append(state);
				queryStateParmsSB.append(Constants.PARAMETER_SEPARATOR);
			}
			// remove last separator
			queryStateParmsSB.deleteCharAt(queryStateParmsSB.length() - 1);
			client.AddParam(Constants.QUERY_STATE, queryStateParmsSB.toString());
		}
		
		String updatedSince = paramList.getSince();
		if (updatedSince !=null && !updatedSince.isEmpty())
			client.AddParam(Constants.QUERY_UPDATED_SINCE, paramList.getSince());

		client.AddHeader(Constants.ACCEPTS, Constants.APPLICATION_JSON);

		try {
			PBLogger.i(TAG,"Calling REST Service."+ client.toString());
			client.Execute(RequestMethod.GET);
			responseString = client.getResponse();
			PBLogger.i(TAG,"Response acquired from REST Service");
		} catch (Exception e) {
			PBLogger.e(TAG, resources
					.getString(
							R.string.errorTryingToGetOpportunitiesFromService,
							e));
			e.printStackTrace();
			throw new PBException(
					resources
							.getString(R.string.errorTryingToGetOpportunitiesFromService),
					e);
		}

		PBLogger.i(TAG, "returning "
				+ responseString);
		PBLogger.exit(TAG);
		return responseString;
	}

}
