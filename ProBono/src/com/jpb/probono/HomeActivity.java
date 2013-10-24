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
import com.jpb.probono.rest.model.OpportunityCategory;
import com.jpb.probono.rest.model.OpportunityQueryParameterList;
import com.jpb.probono.service.CategoryListService;
import com.jpb.probono.utility.PBLogger;

@SuppressLint("HandlerLeak")
public class HomeActivity extends Activity {

	private String className = "HomeActivity";
	private static Resources resources = null;
	private ArrayList<OpportunityCategory> cats = null; // List of Categories.

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		String methodName = ".onCreate";
		String TAG = className + methodName;
		PBLogger.i(TAG, "entry.");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		resources = getResources();

		// prefetch Categories, call service. It comes back to handler.
		Intent intent = new Intent(this, CategoryListService.class);
		// Create a new Messenger for the communication back
		Messenger messenger = new Messenger(categoriesHandler);
		intent.putExtra(Constants.MESSENGER, messenger);
		startService(intent);

		PBLogger.i(TAG, "exit");
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
		String methodName = ".AllCategoriesOnClickListener.onClick";
		String TAG = className + methodName;
		PBLogger.entry(TAG);
		Intent categoryIntent = new Intent(this, CategoryListActivity.class);

		categoryIntent.putExtra(Constants.CATEGORIES, cats);

		startActivity(categoryIntent);
		PBLogger.exit(TAG);

	}

	@SuppressWarnings("unchecked")
	public void doBrowseByPref(View v) {
		String methodName = ".doBrowseByPref";
		String TAG = className + methodName;
		PBLogger.i(TAG, "entry.");

		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		
		HashMap<String,?> prefMap =(HashMap<String,?>)preferences.getAll();
			
		HashSet<String> cats = (HashSet<String>)prefMap.get(Constants.PREFERRED_CATEGORIES);
		HashSet<String> states = (HashSet<String>)prefMap.get(Constants.PREFERRED_STATES);
		
		
		PBLogger.i(TAG, "states = " + states + " cats = " + cats);

		if ((states == null) || (cats == null)) {
			Toast.makeText(this, resources.getString(R.string.noSubscribed),
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
		SharedPreferences.Editor editor = preferences.edit();
		editor.putLong(Constants.lastUsage, System.currentTimeMillis());
		// Note: Preference has to be stored as Long, but we format it as String
		// in the query (from PushSubscribed...).

		PBLogger.i(TAG,
				"Last usage time updated to: " + System.currentTimeMillis());
		editor.commit();

	}

}
