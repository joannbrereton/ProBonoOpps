package com.jpb.probono.service;

import java.util.List;

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
import com.jpb.probono.rest.model.ContactInfo;
import com.jpb.probono.rest.model.Opportunity;
import com.jpb.probono.rest.model.OpportunityMailPayload;
import com.jpb.probono.utility.PBLogger;
import com.jpb.probono.utility.RestClient;
import com.jpb.probono.utility.RestClient.RequestMethod;

public class MailService extends IntentService {
	private Intent intent = null; // save intent for service.
	private CallMailWebServiceTask task = null;
	private static Resources resources = null;
	private static final String className = "MailService";
	private static OpportunityMailPayload mailPayload = null;

	public MailService() {
		super(className);
	}
	public MailService(String name) {
		super(name);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		String TAG = className + ".onHandleIntent";
		
		resources = getResources();
		this.intent = intent;	
		mailPayload = intent.getExtras().getParcelable(Constants.MAIL_PARAMS);

		task = new CallMailWebServiceTask();
		task.execute();
		// see post-execute in CallWebServiceTask for what happens next...

		PBLogger.exit(TAG);		

	}
	
	public static String doMail() throws PBException {
		String TAG = className + ".doMail";
		PBLogger.entry(TAG);
		String responseString = null;
		String endpoint = resources.getString(R.string.mailerEndpoint);

		RestClient client = new RestClient(endpoint);
		client.AddHeader(Constants.ACCEPTS, Constants.TEXT_HTML);

		try {
			PBLogger.i(TAG,
					"About to execute REST Call in doMail");
			ContactInfo contact = mailPayload.getContact();
			client.AddParam(Constants.KEY_KEY,Constants.KEY_RESPONSE);
			client.AddParam(Constants.KEY_FIRSTNAME, contact.getFirstName());
			client.AddParam(Constants.KEY_LASTNAME, contact.getLastName());
			client.AddParam(Constants.KEY_EMAIL, contact.getEmail());
			client.AddParam(Constants.KEY_FIRMNAME,contact.getFirmName());
			client.AddParam(Constants.KEY_PHONE, contact.getPhone());
			client.AddParam(Constants.KEY_COMMENTS, mailPayload.getComment());
			client.AddParam(Constants.KEY_ROBOT_SUM, Constants.ROBOT_CONSTANT);
			client.AddParam(Constants.KEY_USER_SUM,Constants.ROBOT_CONSTANT);
			client.AddParam(Constants.KEY_SUBMIT, Constants.SUBMIT_WORD);
			
			List<Opportunity> oppList = mailPayload.getListOfOpps();
			
			for (Opportunity opp : oppList)
			{
			// Hardcoded at the moment....need these values!!!
				client.AddParam("Opp", opp.getOppId());
				client.AddParam("Opp", opp.getOppId());
			}
			PBLogger.i(TAG, "About to post.");
			client.Execute(RequestMethod.POST);
			responseString = client.getResponse();
			PBLogger.i(TAG, "responseString =" + responseString);

		} catch (Exception e) {
			PBLogger.e(TAG,resources.getString(R.string.errorTryingToGetCategoriesFromService),e);
			throw new PBException(resources.getString(R.string.errorTryingToGetCategoriesFromService), e);
		}

		PBLogger.i(TAG, "returning  " + responseString
				+ " from doMail");
		PBLogger.exit(TAG);
		return responseString;
	}
	
	
public class CallMailWebServiceTask extends AsyncTask<Object, Object, Object> {
		

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
						Toast.makeText(MailService.this,resources.getString(R.string.errorTryingToGetCategoriesFromService), Toast.LENGTH_LONG).show();
						PBLogger.w(getClass().getName(),
								"Exception sending message", e1);
						e1.printStackTrace();
					}
					PBLogger.i(TAG,
							"Sent results back to calling activity.");

				}
			} else {
				Toast.makeText(
						MailService.this,
						resources.getString(R.string.noCategoriesFound),
						Toast.LENGTH_LONG).show();
				PBLogger.i(TAG,"No Mail response Found!");
			}
			
			PBLogger.exit(TAG);

		}
		
		
		

		@Override
		protected Object doInBackground(Object... params) {
			String TAG = className + ".doInBackground";
			try {
				return MailService.doMail();
			} catch (PBException e) {
				
				PBLogger.e(TAG,"Do Mail attempt failure.",e);
				return null;
			}
		}
	}


}
