package com.jpb.probono.helper;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.res.Resources;

import com.jpb.probono.R;
import com.jpb.probono.constants.Constants;
import com.jpb.probono.exception.PBException;
import com.jpb.probono.rest.model.OpportunityCategory;

public class CategoriesListHelper {
	public static ArrayList<OpportunityCategory> parseCategoriesFromJson(Resources resources,
			String jsonResponse) throws PBException {
		ArrayList<OpportunityCategory> cats = null;
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

		return cats;
	}

}
