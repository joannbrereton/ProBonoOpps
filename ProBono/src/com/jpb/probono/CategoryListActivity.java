package com.jpb.probono;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.jpb.probono.constants.Constants;
import com.jpb.probono.exception.PBException;
import com.jpb.probono.rest.model.OpportunityCategory;
import com.jpb.probono.rest.model.OpportunityQueryParameterList;
import com.jpb.probono.utility.PBLogger;

public class CategoryListActivity extends Activity implements
		OnItemClickListener {

	protected ArrayList<OpportunityCategory> cats;
	protected static Resources resources = null;
	private static final String className = "CategoryListActivity";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		String TAG = className + ".onCreate";
		PBLogger.entry(TAG);
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_category_list);
		Bundle b = getIntent().getExtras();
		cats = b.getParcelableArrayList(Constants.CATEGORIES);
		
		// Grab resources - they will come in handy later.
		if (resources == null)
			resources = this.getResources();
		
		// Put all the categories into the list.
		ListView itemlist = (ListView) findViewById(R.id.listCategory);
		ArrayAdapter<OpportunityCategory> adapter = new ArrayAdapter<OpportunityCategory>(
				CategoryListActivity.this,
				android.R.layout.simple_list_item_1,
				cats);
		
		if (itemlist == null)
		{
			//throw new PBException();
			
		}
		else
		{
			itemlist.setAdapter(adapter);
			itemlist.setSelection(0);
			itemlist.setOnItemClickListener((OnItemClickListener) CategoryListActivity.this);
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

			throw new PBException(resources.getString(R.string.errorTryingToParseCategories), e);
		}

		PBLogger.d(TAG,"returning cats = " + cats);
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
		String TAG = className + ".onOptionsItemSelected";
		Intent settingsActivity = new Intent(getBaseContext(),
				ProBonoPreferencesActivity.class);
		PBLogger.i(TAG,
				"Setting Preferences Activity - settingsActivity = "
						+ settingsActivity);
		startActivity(settingsActivity);
		return true;
	}

	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		String TAG = className + ".onItemClick";
		PBLogger.i(TAG,"entry");
		OpportunityCategory cat = cats.get(position);
		PBLogger.i(TAG, "ChosenCategory is " + cat);
		Intent oppListIntent = new Intent(this, OpportunityListActivity.class);

		// Marshall the category into a query.

		OpportunityQueryParameterList listQueryParms = new OpportunityQueryParameterList();
		listQueryParms.addCategory(Integer.toString(cat.getId()));
		PBLogger.i(TAG, "listQueryParms = "
				+ listQueryParms);

		oppListIntent.putExtra(Constants.LIST_QUERY_PARMS, listQueryParms);
		startActivity(oppListIntent);
		PBLogger.i(TAG,"exit");
	}

	

	
}
