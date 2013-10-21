package com.jpb.probono;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.jpb.probono.constants.Constants;
import com.jpb.probono.exception.PBException;
import com.jpb.probono.helper.OpportunityListHelper;
import com.jpb.probono.rest.model.Opportunity;
import com.jpb.probono.rest.model.OpportunityQueryParameterList;
import com.jpb.probono.service.OpportunityListService;
import com.jpb.probono.utility.PBLogger;

@SuppressLint("HandlerLeak")
public class OpportunityListActivity extends Activity implements
		OnItemClickListener {

	private static final String className = "OpportunityListActivity";
	private static Resources resources = null;
	private ArrayList<Opportunity> oppList = null;
	private OpportunityAdapter adapter = null;
	private ListView listView = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		String TAG = className + ".onCreate";
		PBLogger.entry(TAG);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_opplist);
		listView = (ListView) this.findViewById(R.id.listOpportunity);
		listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);


		// Get Category from parcelable.
		Bundle b = getIntent().getExtras();
		OpportunityQueryParameterList listQueryParms = b
				.getParcelable(Constants.LIST_QUERY_PARMS);
		PBLogger.i(TAG, "Calling opportunity list service - listQueryParms = "
				+ listQueryParms);
		

		Intent intent = new Intent(this, OpportunityListService.class);
		// Create a new Messenger for the communication back
		// Pass in the listQueryParms....
		// result comes back to handler
		Messenger messenger = new Messenger(opportunitiesHandler);
		intent.putExtra(Constants.MESSENGER, messenger);
		intent.putExtra(Constants.LIST_QUERY_PARMS, listQueryParms);
		PBLogger.i(TAG, "About to start service");
		startService(intent);
		PBLogger.exit(TAG);

	}

	// Handler that handles the returned categories.
	@SuppressLint("HandlerLeak")
	private Handler opportunitiesHandler = new Handler() {
		public void handleMessage(Message message) {
			String TAG = className + ".handleMessage";
			PBLogger.entry(TAG);

			String json = (String) message.obj;
			if (message.arg1 == RESULT_OK && json != null) {
				resources = OpportunityListActivity.this.getResources();

				try {
					oppList = OpportunityListHelper.parseOpportunityList(
							resources, (String) json);

				} catch (PBException e) {
					PBLogger.e(
							TAG,
							resources
									.getString(R.string.errorTryingtoParseOpportunities),
							e);
					e.printStackTrace();
					Toast.makeText(
							OpportunityListActivity.this,
							resources
									.getString(R.string.errorTryingtoParseOpportunities),
							Toast.LENGTH_LONG).show();
				}
				// parse results into opportunities.
				PBLogger.i(TAG, "oppList returned = " + oppList);

				if (oppList != null && !oppList.isEmpty()) {

					adapter = new OpportunityAdapter(
							OpportunityListActivity.this, oppList);

					listView.setAdapter(adapter);

					listView.setSelection(0);

					listView.setOnItemClickListener((OnItemClickListener) OpportunityListActivity.this);
				} else // no results...
				{
					showNoResultsDialog();

				}
			} else {
				Toast.makeText(
						OpportunityListActivity.this,
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

	public void showComments(View v) {
		String TAG = "showComments";
		PBLogger.entry(TAG);
		try {
			Intent commentIntent = new Intent(this, CommentActivity.class);
//			SparseBooleanArray checked = null;
			ArrayList<Opportunity> selectedItems = new ArrayList<Opportunity>();
			// Marshall the category into a query.
			if (oppList != null && !oppList.isEmpty()) {
				for (Opportunity opp : oppList)
				{
					if (opp.isSelected())
					{
						selectedItems.add(opp);
					}
				}
				PBLogger.i(TAG, selectedItems.toString());
				commentIntent.putExtra(Constants.SELECTED_ITEMS, selectedItems);
				startActivity(commentIntent);
				this.finish();
			}
			else {
				Toast.makeText(this, resources.getString(R.string.noSelectedOpps),
						Toast.LENGTH_LONG).show();
			}

			
			PBLogger.exit(TAG);

		} catch (android.content.ActivityNotFoundException ex) {
			Toast.makeText(OpportunityListActivity.this, ex.getMessage(),
					Toast.LENGTH_SHORT).show();
		}

		

	}

	private void showNoResultsDialog() {

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

		// set title
		alertDialogBuilder.setTitle(resources
				.getString(R.string.title_activity_no_results));

		// set dialog message

		alertDialogBuilder
				.setTitle(
						resources.getString(R.string.title_activity_no_results))
				.setMessage(R.string.noResults)
				.setNeutralButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						OpportunityListActivity.this.finish();
					}

				});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		String TAG = "onItemClick";
		PBLogger.entry(TAG);
		String selected = "";

		int cntChoice = listView.getCount();

		SparseBooleanArray sparseBooleanArray = listView
				.getCheckedItemPositions();

		for (int i = 0; i < cntChoice; i++) {

			if (sparseBooleanArray.get(i)) {

				selected += listView.getItemAtPosition(i).toString() + "\n";

			}

		}

		Toast.makeText(OpportunityListActivity.this, selected,
				Toast.LENGTH_LONG).show();

		PBLogger.exit(TAG);
	}
}
