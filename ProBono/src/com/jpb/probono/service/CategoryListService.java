package com.jpb.probono.service;

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
import com.jpb.probono.utility.PBLogger;
import com.jpb.probono.utility.RestClient;
import com.jpb.probono.utility.RestClient.RequestMethod;

public class CategoryListService extends IntentService {
	private Intent intent = null; // save intent for service.
	private CallCategoriesWebServiceTask task = null;
	private static Resources resources = null;
	private static final String className = "CategoryListService";
	
	public CategoryListService()
	{
		super(className);
	}
	
	public CategoryListService(String name) {
		super(name);
		
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		String TAG = className + ".onHandleIntent";
		PBLogger.entry(TAG);
		// Normally we would do some work here, like download a file.
		// For our sample, we just sleep for 5 seconds.
		resources = getResources();
		this.intent = intent;		

		task = new CallCategoriesWebServiceTask();
		task.execute();
		// see post-execute in CallWebServiceTask for what happens next...

		PBLogger.exit(TAG);		
	}
	
	public static String getAllCategories() throws PBException {
		String TAG = className + ".getAlLCategories";
		PBLogger.entry(TAG);
		String responseString = null;

		String endpoint = resources.getString(R.string.getCategoriesEndpoint);

		RestClient client = new RestClient(endpoint);
		client.AddHeader(Constants.ACCEPTS, Constants.APPLICATION_JSON);

		try {
			PBLogger.i(TAG,
					"About to execute REST Call in getAllCategories");
			client.Execute(RequestMethod.GET);
			responseString = client.getResponse();

		} catch (Exception e) {
			PBLogger.e(TAG,resources.getString(R.string.errorTryingToGetCategoriesFromService),e);
			throw new PBException(resources.getString(R.string.errorTryingToGetCategoriesFromService), e);
		}

		PBLogger.i(TAG, "returning  " + responseString
				+ " from getAllCategories");
		PBLogger.exit(TAG);
		return responseString;
	}
	
	
	public class CallCategoriesWebServiceTask extends AsyncTask<Object, Object, Object> {
		

		@Override
		protected void onPreExecute() {

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
						Toast.makeText(CategoryListService.this,resources.getString(R.string.errorTryingToGetCategoriesFromService), Toast.LENGTH_LONG).show();
						PBLogger.w(getClass().getName(),
								"Exception sending message", e1);
						e1.printStackTrace();
					}
					PBLogger.i(TAG,
							"Sent results back to calling activity.");

				}
			} else {
				Toast.makeText(
						CategoryListService.this,
						resources.getString(R.string.noCategoriesFound),
						Toast.LENGTH_LONG).show();
				PBLogger.i(TAG,"No Categories Found!");
			}
			
			PBLogger.exit(TAG);

		}

		@Override
		protected Object doInBackground(Object... params) {
			String TAG = className + ".doInBackground";
			try {
				return CategoryListService.getAllCategories();
			} catch (PBException e) {
				
				PBLogger.e(TAG,"Get All Category attempt failure.",e);
				return null;
			}
		}
	}

}
