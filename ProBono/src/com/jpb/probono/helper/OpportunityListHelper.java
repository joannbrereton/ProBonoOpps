package com.jpb.probono.helper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.preference.PreferenceManager;
import android.util.Log;

import com.jpb.probono.R;
import com.jpb.probono.constants.Constants;
import com.jpb.probono.exception.PBException;
import com.jpb.probono.rest.model.Opportunity;
import com.jpb.probono.rest.model.OpportunityQueryParameterList;
import com.jpb.probono.utility.PBLogger;

public class OpportunityListHelper {
	private static final String className = "OpportunityListHelper";
	

	public static ArrayList<Opportunity> parseOpportunityList(Resources resources,
			String jsonResponse) throws PBException {

		ArrayList<Opportunity> oppsList = null;
		if (jsonResponse != null && !jsonResponse.isEmpty()) {
			try {
				jsonResponse = OpportunityListHelper.cleanUpJsonResponse(jsonResponse);
				JSONArray jsonArray = new JSONArray(jsonResponse);
				oppsList = new ArrayList<Opportunity>(jsonArray.length());
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonObj = jsonArray.getJSONObject(i);
					Opportunity opp = new Opportunity();
					opp.setCity(jsonObj.getString(Constants.CITY));
					opp.setClient(jsonObj.getString(Constants.CLIENT));
					opp.setMatterNo(jsonObj.getString(Constants.MATTER_NO));	
					opp.setMission(jsonObj.getString(Constants.MISSION));
					
					opp.setState(jsonObj.getString(Constants.STATE));
					opp.setLegalWorkRequired(jsonObj
							.getString(Constants.LEGAL_WORK_REQUIRED));
					opp.setOppId(jsonObj.getString(Constants.OPPID));

					oppsList.add(opp);
				}

			} catch (JSONException e) {
				e.printStackTrace();
				PBLogger.e("getOpportunities", resources
						.getString(R.string.errorTryingtoParseOpportunities), e);
				throw new PBException(
						resources
								.getString(R.string.errorTryingtoParseOpportunities),
						e);
			} catch (NotFoundException e) {
				e.printStackTrace();
				PBLogger.e("getOpportunities", resources
						.getString(R.string.errorTryingtoParseOpportunities), e);
				throw new PBException(
						resources
								.getString(R.string.errorTryingtoParseOpportunities),
						e);
			}
		}
		return oppsList;
	}
	
	// This is a hack to clean up a problem with the json Response.  For some reason, the Android
	// JSON parser doesn't like http:// in the string.  So, we'll get rid of it here by excising the entire Mission:, which we
	// don't need anyway.
	private static String cleanUpJsonResponse(String jsonResponse)
	{
		PBLogger.i("OpportunityListHelper.cleanUpJsonResponse",jsonResponse);
		
		String newJsonResponse = jsonResponse;
	//	Pattern htmlRegExp =  Pattern.compile(Constants.HTML_REGEXP);
		newJsonResponse = newJsonResponse.replaceAll(Constants.HTML_REGEXP, Constants.EMPTY);
				
		PBLogger.i("OpportunityListHelper.cleanUpJsonResponse","returning " + jsonResponse);
		return newJsonResponse;
	}
	
	public static OpportunityQueryParameterList buildParameterList(Context context, HashSet<String> cats, HashSet<String> states,  boolean useSince) {
		String TAG = className + ".buildParameterList";
		PBLogger.i(TAG, "entry");
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		
		//Note: Preference has to be stored as Long, but we format it as String in the query.
		Long sinceLong = Constants.BEGINNING_OF_TIME;
		if (useSince)
		{
		    sinceLong = preferences.getLong(Constants.lastUsage,
				Constants.BEGINNING_OF_TIME);
		    PBLogger.i(TAG, "sinceLong = " + sinceLong);
		}
		
		String updatedSince = formatUpdatedSince(new Date(sinceLong));

		

		PBLogger.i(TAG, "states = " + states + " cats = " + cats);

		// Getting ready to call opportunities REST service...

		OpportunityQueryParameterList listQueryParms = new OpportunityQueryParameterList();
		
		listQueryParms.setSince(updatedSince);
		
		
		for (String state : states) {
			listQueryParms.addState(state); // don't need name for now
		}

		
		for (String cat : cats) {
			listQueryParms.addCategory(cat); // don't need name for
														// now
		}
		PBLogger.i(TAG,"returning listQueryParms = " + listQueryParms);
		return listQueryParms;
	}

	public static String formatUpdatedSince(Date date) {
		String TAG = className + ".formatUpdatedSince";
		String updatedSince=null;
		SimpleDateFormat format1 = new SimpleDateFormat(Constants.DATE_SINCE_FORMAT_PATTERN,Locale.US);          
	    updatedSince = format1.format(date);
	    PBLogger.i(TAG,"input date = " + date + " output date = " + updatedSince);
		
		return updatedSince;
	}
}
