package com.jpb.probono;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.jpb.probono.constants.Constants;
import com.jpb.probono.rest.model.ContactInfo;
import com.jpb.probono.rest.model.Opportunity;
import com.jpb.probono.rest.model.OpportunityMailPayload;
import com.jpb.probono.service.MailService;
import com.jpb.probono.utility.PBLogger;

public class CommentActivity extends Activity {
	
	private Messenger messenger = null;
	private Resources resources = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comment);
		
		// handler to return from mailService
		 messenger = new Messenger(mailHandler);
		 
		// resources for grabbing messages to user.
		 resources = this.getResources();
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.preferences_menu, menu);
		return true;
	}

	public void volunteer(View v) {
		String TAG = "volunteer";
		PBLogger.entry(TAG);
		
		EditText commentText =(EditText)findViewById(R.id.commentText);

		Bundle bundle = this.getIntent().getExtras();

		ArrayList<Opportunity> opps = bundle
				.getParcelableArrayList(Constants.SELECTED_ITEMS);
		if (opps != null) {
			PBLogger.i(TAG, "Volunteered for..." + opps.toString());

			try {
				Intent mailIntent = new Intent(this, MailService.class);
				OpportunityMailPayload mailParams = new OpportunityMailPayload();

				ContactInfo contact = getContactInfoFromPreferences();
				if (contact.isValid()) {
				
					mailParams.setListOfOpps(opps);
					mailParams.setContact(contact);					
					mailParams.setComment(commentText.getText().toString());
					mailIntent.putExtra(Constants.MAIL_PARAMS, mailParams);
					mailIntent.putExtra(Constants.MESSENGER, messenger);

					startService(mailIntent);
				} else
				{
				
					Toast.makeText(CommentActivity.this, resources.getString(R.string.noContactInfo),
							Toast.LENGTH_LONG).show();
				}
			} catch (android.content.ActivityNotFoundException ex) {
				Toast.makeText(CommentActivity.this, ex.getMessage(),
						Toast.LENGTH_LONG).show();
			}

		} else {
			PBLogger.i(TAG, "null pointer received for opportunities chosen.");
			
		}
		PBLogger.exit(TAG);
	}

	private ContactInfo getContactInfoFromPreferences() {
		ContactInfo contactInfo = new ContactInfo();
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		
	
		contactInfo.setFirstName(prefs.getString(Constants.PREF_KEY_FIRSTNAME, ""));
		contactInfo.setLastName(prefs.getString(Constants.PREF_KEY_LASTNAME, ""));
		contactInfo.setEmail(prefs.getString(Constants.PREF_KEY_EMAIL, ""));
		contactInfo.setPhone(prefs.getString(Constants.PREF_KEY_PHONE, ""));
		contactInfo.setFirmName(prefs.getString(Constants.PREF_KEY_FIRM,""));
		
		return contactInfo;
	}
	
	// Handler that handles the returned categories.
		@SuppressLint("HandlerLeak")
		private Handler mailHandler = new Handler() {
			public void handleMessage(Message message) {
				PBLogger.entry("mailHandler");
				Toast.makeText(
						CommentActivity.this,
						CommentActivity.this.getResources().getString(R.string.mailSent),
						Toast.LENGTH_LONG).show();
				PBLogger.i("mailHandler", message.toString());
				CommentActivity.this.finish();
				PBLogger.exit("mailHandler");
			}
		};
}
