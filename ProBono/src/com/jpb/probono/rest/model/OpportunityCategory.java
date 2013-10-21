package com.jpb.probono.rest.model;


import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * The persistent class for the OPPORTUNITYCATEGORY database table.
 * 
 */

public class OpportunityCategory implements Parcelable {

	private int    id;
	private String name;
	private List<Opportunity> opportunityList;
	

	public OpportunityCategory() {
	}
	

	public OpportunityCategory(int id, String name) {
		this.id = id;
		this.name = name;
	}
	public OpportunityCategory(String name) {
		this.name = name;
	}

	
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void addOpportunity(Opportunity opp)
	{
		if (opportunityList == null)
		{
			opportunityList = new ArrayList<Opportunity>();
		}
		opportunityList.add(opp);
	}
	
	public List<Opportunity> getListOfOpportunities()
	{
		return opportunityList;
	}
	
	public String toString()
    {
        // limit how much text you display
		StringBuffer sb= new StringBuffer("id = " + this.id);
        if (name != null && name.length() > 42)
        {
            sb.append("name = " + name.substring(0, 42) + "...");
        }
        else
        {
        	sb.append("name is null");
        }
        return name;
    }

	// Parcelable methods.
	
	public OpportunityCategory(Parcel parcel)
	{
		this.setId(parcel.readInt());
		this.setName(parcel.readString());
	}

	@Override
	public int describeContents() {
		
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.getId());
		dest.writeString(this.getName());
		
	}
	
	
	
	public final static  Parcelable.Creator<OpportunityCategory> CREATOR = new Parcelable.Creator<OpportunityCategory>() {
	      public OpportunityCategory createFromParcel(Parcel source) {
	            return new OpportunityCategory(source);
	      }
	      public OpportunityCategory[] newArray(int size) {
	            return new OpportunityCategory[size];
	      }
	};


	
	
}
