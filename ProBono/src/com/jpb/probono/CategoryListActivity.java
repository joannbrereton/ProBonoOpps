package com.jpb.probono;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.jpb.probono.constants.Constants;
import com.jpb.probono.exception.PBException;
import com.jpb.probono.helper.CategoriesListHelper;
import com.jpb.probono.rest.model.OpportunityCategory;
import com.jpb.probono.rest.model.OpportunityQueryParameterList;
import com.jpb.probono.service.CategoryListService;
import com.jpb.probono.utility.PBLogger;

public class CategoryListActivity extends Activity implements
		OnItemClickListener {

	protected ArrayList<OpportunityCategory> cats;
	protected static Resources resources = null;
	private static final String className = "CatLstAct";
	private boolean showOverlay = true;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		String TAG = className + ".onCreate";
		PBLogger.entry(TAG);
		super.onCreate(savedInstanceState);

		// fetch Categories, call service. It comes back to handler.
		Intent categoryListServiceIntent = new Intent(this,
				CategoryListService.class);
		// Create a new Messenger for the communication back
		Messenger messenger = new Messenger(categoriesHandler);
		categoryListServiceIntent.putExtra(Constants.MESSENGER, messenger);
		startService(categoryListServiceIntent);

		PBLogger.exit(TAG);

	}

	private void showCategoriesOverlay() {
		final Dialog dialog = new Dialog(this,
				android.R.style.Theme_Translucent_NoTitleBar);

		dialog.setContentView(R.layout.category_overlay);

		LinearLayout layout = (LinearLayout) dialog
				.findViewById(R.id.llOverlay_activity);
		layout.setBackgroundColor(Color.TRANSPARENT);
		layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				SharedPreferences prefs = PreferenceManager
						.getDefaultSharedPreferences(CategoryListActivity.this);
				prefs.edit().putBoolean("showOverlay", false).commit();
				showOverlay = false;
				dialog.dismiss();

			}

		});

		dialog.show();
	}

	@Override
	protected void onResume() {
		String TAG = className + ".onResume";
		PBLogger.entry(TAG);
		super.onResume();

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
					CategoryListActivity.this.loadCatsIntoPreferences(cats);
					// OK...only now should we proceed to list the categories.

					CategoryListActivity.this.showCategories(cats);
				} catch (PBException e) {
					Toast.makeText(
							CategoryListActivity.this,
							resources
									.getString(R.string.errorTryingToParseCategories),
							Toast.LENGTH_LONG).show();
					PBLogger.e(TAG, resources
							.getString(R.string.errorTryingToParseCategories),
							e);
				}
			} else {
				Toast.makeText(
						CategoryListActivity.this,
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

	private void showCategories(ArrayList<OpportunityCategory> cats) {
		String TAG = className + ".showCategories";
		PBLogger.entry(TAG);
		setContentView(R.layout.activity_category_list);

		// Grab resources - they will come in handy later.
		if (resources == null)
			resources = this.getResources();

		// Put all the categories into the list.
		ListView itemlist = (ListView) findViewById(R.id.listCategory);

		// attach list click adapter.
		ArrayAdapter<OpportunityCategory> adapter = new ArrayAdapter<OpportunityCategory>(
				CategoryListActivity.this, android.R.layout.simple_list_item_1,
				cats);

		if (itemlist == null || adapter == null) {
			PBLogger.e(TAG, "null itemlist or adapter...itemlist = " + itemlist
					+ " adapter " + adapter);
		} else {
			itemlist.setAdapter(adapter);
			itemlist.setSelection(0);
			itemlist.setOnItemClickListener((OnItemClickListener) CategoryListActivity.this);
		}

		// Show overlay - first time only
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(CategoryListActivity.this);
		showOverlay = prefs.getBoolean("showOverlay", true);
		PBLogger.i(TAG, "onCreate showOverlay =" + showOverlay);
		if (showOverlay == true) {
			showCategoriesOverlay();
		}

		PBLogger.exit(TAG);

	}

	public static ArrayList<OpportunityCategory> parseCategories(
			String jsonResponse) throws PBException {
		ArrayList<OpportunityCategory> cats = null;
		String TAG = className + ".parseCategories";
		PBLogger.entry(TAG);
		try {
			JSONArray jsonArray = new JSONArray(jsonResponse);
			cats = new ArrayList<OpportunityCategory>(jsonArray.length());
			jsonArray = new JSONArray(jsonResponse);
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObj = jsonArray.getJSONObject(i);
				OpportunityCategory cat = new OpportunityCategory();
				cat.setId(jsonObj.getInt(Constants.CATEGORY_ID));
				cat.setName(jsonObj.getString(Constants.CATEGORY_NAME));
				cats.add(cat);
			}
		} catch (JSONException e) {

			throw new PBException(
					resources.getString(R.string.errorTryingToParseCategories),
					e);
		}

		PBLogger.d(TAG, "returning cats = " + cats);
		PBLogger.exit(TAG);
		return cats;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.preferences_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		String TAG = className + "onOptionsItemSelected";
		PBLogger.entry(TAG);
		Intent preferencesIntent = null;
		switch (item.getItemId()) {
		case R.id.menu_contact_preferences:
			preferencesIntent = new Intent(getBaseContext(),
					ContactPreferencesActivity.class);
			break;

		case R.id.menu_subscription_preferences:
			preferencesIntent = new Intent(getBaseContext(),
					SubscriptionPreferencesActivity.class);
		default:
			PBLogger.w(TAG,
					"No preferences matching itemId = " + item.getItemId());
			break;

		}
		PBLogger.i(TAG, "Setting Preferences Activity -  = "
				+ preferencesIntent);

		if (preferencesIntent != null) {
			startActivity(preferencesIntent);
		}
		PBLogger.exit(TAG);

		return true;
	}

	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		String TAG = className + ".onItemClick";
		PBLogger.i(TAG, "entry");
		OpportunityCategory cat = cats.get(position);
		PBLogger.i(TAG, "ChosenCategory is " + cat);
		Intent oppListIntent = new Intent(this, OpportunityListActivity.class);

		// Marshall the category into a query.

		OpportunityQueryParameterList listQueryParms = new OpportunityQueryParameterList();
		listQueryParms.addCategory(Integer.toString(cat.getId()));
		PBLogger.i(TAG, "listQueryParms = " + listQueryParms);

		oppListIntent.putExtra(Constants.LIST_QUERY_PARMS, listQueryParms);
		startActivity(oppListIntent);
		PBLogger.i(TAG, "exit");
	}

	// TODO: Trying to figure out how to load the current list of categories
	// into the PREFERENCES list.
	private void loadCatsIntoPreferences(ArrayList<OpportunityCategory> cats) {
		// SharedPreferences preferences = PreferenceManager
		// .getDefaultSharedPreferences(this);
		//
	};

}
