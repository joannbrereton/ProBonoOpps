package com.jpb.probono.rest.model;

import java.util.ArrayList;
import java.util.Date;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.jpb.probono.helper.OpportunityListHelper;

/**
 * 
 * @author brereton
 * This class used solely to pass query parameters to OpportunityList Intent.
 *
 */
public class OpportunityQueryParameterList implements Parcelable {

	ArrayList<String> categories = null;
	ArrayList<String> states = null;
	String              since = null;
	
	public String getSince() {
		return since;
	}
	public void setSince(String updatedSince) {
		this.since = updatedSince;
	}
	public OpportunityQueryParameterList()
	{
		super();
		categories = new ArrayList<String>();
		states     = new ArrayList<String>();
		since      = OpportunityListHelper.formatUpdatedSince(new Date(0)); // since forever
	}
	public ArrayList<String> getCategories() {
		return categories;
	}
	public void setCategories(ArrayList<String> categories) {
		this.categories = categories;
	}
	public ArrayList<String> getStates() {
		return states;
	}
	public void setStates(ArrayList<String> states) {
		this.states = states;
	}
	public OpportunityQueryParameterList(ArrayList<String>cats, ArrayList<String>states)
	{
		this.categories = cats;
		this.states = states;
	}
	
	public OpportunityQueryParameterList(ArrayList<String>cats, ArrayList<String>states,String since)
	{
		this.categories = cats;
		this.states = states;
		this.since = since;
	}
	
	public void addCategory(String cat)
	{
		
		categories.add(cat);
	}
	
	public void addState(String state)
	{
	
		states.add(state);
	}

	public OpportunityQueryParameterList(Parcel parcel) {
		this();
		Log.i("OpportunityQueryParameterList(Parcel)","Entry" + parcel);
		readFromParcel(parcel);		
		Log.i("OpportunityQueryParameterList(Parcel)","Exit");
		
	}

	private void readFromParcel(Parcel parcel) {
		parcel.readStringList(categories);
		parcel.readStringList(states);	
		since = parcel.readString();
	}
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		Log.i("OpportunityQueryParameterList.writeToParcel","Entry");
		dest.writeStringList(categories);
		dest.writeStringList(states);
		dest.writeString(since);
		Log.i("OpportunityQueryParameterList.writeToParcel","Exit");

	}

	public final static Parcelable.Creator<OpportunityQueryParameterList> CREATOR = new Parcelable.Creator<OpportunityQueryParameterList>() {
		public OpportunityQueryParameterList createFromParcel(Parcel source) {
			return new OpportunityQueryParameterList(source);
			
		}

		public OpportunityQueryParameterList[] newArray(int size) {
			return new OpportunityQueryParameterList[size];
		}
	};
	public String toString()
	{
		return this.categories + " " + this.states + " since " + since;
	}

}
