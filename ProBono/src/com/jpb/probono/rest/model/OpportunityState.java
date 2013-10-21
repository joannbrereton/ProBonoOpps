package com.jpb.probono.rest.model;

import android.os.Parcel;
import android.os.Parcelable;



// Using JDO and JAXB.


public class OpportunityState implements Parcelable {

	private String abbreviation;
	
	private String name;
	
    public OpportunityState() {
    }
    
    public OpportunityState(String abbrev, String name)
    {
    	this.abbreviation = abbrev;
    	this.name = name;
    }


	public String getAbbreviation() {
		return this.abbreviation;
	}

	public void setAbbreviation(String abbrev) {
		this.abbreviation = abbrev;
	}


	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	// Parcelable methods.
	
		public OpportunityState(Parcel parcel)
		{
			this.setAbbreviation(parcel.readString());
			this.setName(parcel.readString());
		}

		@Override
		public int describeContents() {
			
			return 0;
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			dest.writeString(this.getAbbreviation());
			dest.writeString(this.getName());
			
		}
		
		
		
		public final static  Parcelable.Creator<OpportunityState> CREATOR = new Parcelable.Creator<OpportunityState>() {
		      public OpportunityState createFromParcel(Parcel source) {
		            return new OpportunityState(source);
		      }
		      public OpportunityState[] newArray(int size) {
		            return new OpportunityState[size];
		      }
		};

	


}