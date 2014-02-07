package com.jpb.probono;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

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
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.jpb.probono.constants.Constants;
import com.jpb.probono.exception.PBException;
import com.jpb.probono.helper.CategoriesListHelper;
import com.jpb.probono.helper.OpportunityListHelper;
import com.jpb.probono.helper.PreferencesHelper;
import com.jpb.probono.rest.model.OpportunityCategory;
import com.jpb.probono.rest.model.OpportunityQueryParameterList;
import com.jpb.probono.service.CategoryListService;
import com.jpb.probono.utility.PBLogger;

@SuppressLint("HandlerLeak")
public class HomeActivity extends Activity {

	private String className = "HomeAct";
	private static Resources resources = null;
	private ArrayList<OpportunityCategory> cats = null; // List of Categories.

	@Override
	protected void onResume() {
		String TAG = className + ".onResume";
		PBLogger.entry(TAG);
		super.onResume();
		this.syncButtonEnablement();
		PBLogger.exit(TAG);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		String methodName = className + ".onCreate";
		String TAG = className + methodName;
		PBLogger.i(TAG, "entry.");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		resources = getResources();

		// Set the buttons accordingly. We don't want to allow query
		// by category until we've received and cached all the categories.
		// We don't want to enable the query by preference until the user
		// sets the preferences.

		this.syncButtonEnablement();

		// prefetch Categories, call service. It comes back to handler.
		Intent intent = new Intent(this, CategoryListService.class);
		// Create a new Messenger for the communication back
		Messenger messenger = new Messenger(categoriesHandler);
		intent.putExtra(Constants.MESSENGER, messenger);
		startService(intent);

		PBLogger.i(TAG, "exit");
	}


	private void syncButtonEnablement() {
		
		String TAG = className + "syncButtonEnablement";
		PBLogger.entry(TAG);
		
		// if Contact information is not yet set up, disable the 
		// home buttons and force user to fill in.
				
		if (!PreferencesHelper.isContactInfoReady(this)) {
			this.findViewById(R.id.browseOppsByPref).setEnabled(false);
			this.findViewById(R.id.browseAll).setEnabled(false);
			
			Toast.makeText(HomeActivity.this,
					resources.getString(R.string.noContactInfo),
					Toast.LENGTH_LONG).show();

		// Next, check if they've set up preferred cats and states.
		// If not, leave the browseOppsbyPref off.
		} else {
			this.findViewById(R.id.browseAll).setEnabled(true);
						
			if ( !PreferencesHelper.isStateChoiceReady(this) ) {
				this.findViewById(R.id.browseOppsByPref).setEnabled(false);
				Toast.makeText(this, resources.getString(R.string.noSubscribedStates),
						Toast.LENGTH_LONG).show();				
			} 
			
			if( !PreferencesHelper.isCategoryChoiceReady(this) ) 
			{
				this.findViewById(R.id.browseOppsByPref).setEnabled(false);
				Toast.makeText(this, resources.getString(R.string.noSubscribedCats),
						Toast.LENGTH_LONG).show();
			}
			
			if (cats != null  && PreferencesHelper.isCategoryChoiceReady(this) && PreferencesHelper.isStateChoiceReady(this)) // categories returned on initialization.
					this.findViewById(R.id.browseOppsByPref).setEnabled(true);
				
			}
		
		
		PBLogger.exit(TAG);

	}
	
	@SuppressWarnings("unused")
	private void bringUpPreferences()
	{
		String TAG = className + "bringUpPreferences";
		PBLogger.entry(TAG);
		Intent settingsActivity = new Intent(getBaseContext(),
				SettingsActivity.class);
		PBLogger.i(TAG,
				"Setting Preferences Activity - settingsActivity = "
						+ settingsActivity);
		startActivity(settingsActivity);
		PBLogger.exit(TAG);
	}

	// Handler that handles the returned categories.
	@SuppressLint("HandlerLeak")
	private Handler categoriesHandler = new Handler() {
		public void handleMessage(Message message) {
			String TAG = className + ".handleMessage";
			PBLogger.entry(TAG);
			String json = (String) message.obj;
			if (message.arg1 == RESULT_OK && json != null) {
				try {
					cats = CategoriesListHelper.parseCategoriesFromJson(
							resources, json);
					HomeActivity.this.loadCatsIntoPreferences(cats);
					HomeActivity.this.syncButtonEnablement(); // should enable
																// Browse by
																// Category.
				} catch (PBException e) {
					Toast.makeText(
							HomeActivity.this,
							resources
									.getString(R.string.errorTryingToParseCategories),
							Toast.LENGTH_LONG).show();
					PBLogger.e(TAG, resources
							.getString(R.string.errorTryingToParseCategories),
							e);
				}
			} else {
				Toast.makeText(
						HomeActivity.this,
						resources
								.getString(R.string.errorTryingToGetCategoriesFromService),
						Toast.LENGTH_LONG).show();
				PBLogger.e(
						TAG,
						resources
								.getString(R.string.errorTryingToGetCategoriesFromService));

			}
			PBLogger.exit(TAG);

		}

	};

	// TODO: Trying to figure out how to load the current list of categories
	// into the PREFERENCES list.
	private void loadCatsIntoPreferences(ArrayList<OpportunityCategory> cats) {
		// SharedPreferences preferences = PreferenceManager
		// .getDefaultSharedPreferences(this);
		//
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.preferences_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		String TAG = className + "onOptionsItemSelected";
		PBLogger.entry(TAG);
		Intent settingsActivity = new Intent(getBaseContext(),
				SettingsActivity.class);
		PBLogger.i(TAG, "Setting Preferences Activity - settingsActivity = "
				+ settingsActivity);
		startActivity(settingsActivity);
		PBLogger.exit(TAG);
		return true;
	}

	public void doBrowseAll(View v) {
		String methodName = className + ".AllCategoriesOnClickListener.onClick";
		String TAG = className + methodName;
		PBLogger.entry(TAG);

		Intent categoryIntent = new Intent(this, CategoryListActivity.class);
		categoryIntent.putExtra(Constants.CATEGORIES, cats);
		updateLastUsage();
		startActivity(categoryIntent);

		PBLogger.exit(TAG);

	}

	@SuppressWarnings("unchecked")
	public void doBrowseByPref(View v) {
		String methodName = className + ".doBrowseByPref";
		String TAG = className + methodName;
		PBLogger.i(TAG, "entry.");
		

		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(this);

		HashMap<String, ?> prefMap = (HashMap<String, ?>) preferences.getAll();

		HashSet<String> cats = (HashSet<String>) prefMap
				.get(Constants.PREFERRED_CATEGORIES);
		HashSet<String> states = (HashSet<String>) prefMap
				.get(Constants.PREFERRED_STATES);

		PBLogger.i(TAG, "states = " + states + " cats = " + cats);

		if ((states == null) || (cats == null)) {
			Toast.makeText(this, resources.getString(R.string.noSubscribedStates),
					Toast.LENGTH_LONG).show();
			PBLogger.exit(TAG);

		} else {

			// Getting ready to call opportunities REST service...

			OpportunityQueryParameterList listQueryParms = OpportunityListHelper
					.buildParameterList(v.getContext(), cats, states, false);

			Intent oppListIntent = new Intent(this,
					OpportunityListActivity.class);
			oppListIntent.putExtra(Constants.LIST_QUERY_PARMS, listQueryParms);
			updateLastUsage();

			startActivity(oppListIntent);

			PBLogger.exit(TAG);
		}

	}

	// Update the timestamp for the last usage of the query.
	private void updateLastUsage() {
		String TAG = className + ".updateLastUsage";
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(this);

		PBLogger.i(
				TAG,
				"before Usage update: "
						+ preferences.getLong(Constants.lastUsage,
								Constants.BEGINNING_OF_TIME));
		SharedPreferences.Editor editor = preferences.edit();

		editor.putLong(Constants.lastUsage, System.currentTimeMillis());
		// Note: Preference has to be stored as Long, but we format it as String
		// in the query (from PushSubscribed...).

		PBLogger.i(
				TAG,
				"Last usage time updated to: "
						+ preferences.getLong(Constants.lastUsage,
								Constants.BEGINNING_OF_TIME));
		editor.commit();

	}

	

}
