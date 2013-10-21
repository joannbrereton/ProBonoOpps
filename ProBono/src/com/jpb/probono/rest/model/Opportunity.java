package com.jpb.probono.rest.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * The persistent class for the OPPORTUNITY database table.
 * 
 */

public class Opportunity implements Parcelable {

	private Boolean activeflag;
	private int    categoryId;
	private String categoryName;
	private String client;
	private String city;
	private String state;
	private String mission;
	private String matterNo; // actual key.
	private String legalWorkRequired;
	
	private long date;
	
	private boolean selected = false;
	
	

	//private List<OpportunityInterest> opportunityInterests;

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public Opportunity() {
	}

	public Opportunity(String matterno, Boolean activeFlag, int categoryId, 
			String categoryName, String legalWorkRequired,
			String client, String city, String state, String mission) {
		this.matterNo = matterno;
		this.activeflag = activeFlag;
		this.categoryId = categoryId;
		this.categoryName = categoryName;
		this.legalWorkRequired = legalWorkRequired;
		this.date = System.currentTimeMillis();
		this.client = client;
		this.city = city;
		this.state = state;
		this.mission = mission;

	}

	
	public Boolean getActiveflag() {
		return activeflag;
	}

	public void setActiveflag(Boolean activeflag) {
		this.activeflag = activeflag;
	}



	public String getClient() {
		return client;
	}

	public void setClient(String client) {
		this.client = client;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getMission() {
		return mission;
	}

	public void setMission(String mission) {
		this.mission = mission;
	}

	public String getMatterNo() {
		return matterNo;
	}

	public void setMatterNo(String matterNo) {
		this.matterNo = matterNo;
	}

	public String getLegalWorkRequired() {
		return legalWorkRequired;
	}

	public void setLegalWorkRequired(String legalWorkRequired) {
		this.legalWorkRequired = legalWorkRequired;
	}

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		//ignore and use today's date & time
		date = System.currentTimeMillis();
	}
	
	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	
	// Parcelable methods.
	
		public Opportunity(Parcel parcel)
		{			
			this.setCategoryId(parcel.readInt());
			this.setCategoryName(parcel.readString());
			this.setMatterNo(parcel.readString());
	//		this.setCategoryName(parcel.readString());
			this.setCity(parcel.readString());
			this.setClient(parcel.readString());
			this.setLegalWorkRequired(parcel.readString());
			this.setMission(parcel.readString());
			this.setState(parcel.readString());
	//		this.setActiveflag(Boolean.parseBoolean(parcel.readString()));
			this.setDate(parcel.readLong());
		
		}

		@Override
		public int describeContents() {
			
			return 0;
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			dest.writeInt(this.getCategoryId());
		//	dest.writeString(this.getCategoryName());
			dest.writeString(this.getMatterNo());
			dest.writeString(this.getCategoryName());
			dest.writeString(this.getCity());
			dest.writeString(this.getClient());
			dest.writeString(this.getLegalWorkRequired());
			dest.writeString(this.getMission());
			dest.writeString(this.getState());
		//	dest.writeString(this.getActiveflag().toString());
			dest.writeLong(this.getDate());
		
		}
		
		
		
		public final static  Parcelable.Creator<Opportunity> CREATOR = new Parcelable.Creator<Opportunity>() {
		      public Opportunity createFromParcel(Parcel source) {
		            return new Opportunity(source);
		      }
		      public Opportunity[] newArray(int size) {
		            return new Opportunity[size];
		      }
		};

	
	public String toString()
	{
		return "MatterNo: " + this.matterNo + "\n" +
	           "Client: " + this.client + "\n" +
			   "City: "  + this.city + "\n" +
	           "State: "  + this.state + "\n" +
			   "Mission:" + this.mission + "\n" +
	           "Work Required: " + this.legalWorkRequired +"\n";
	}
	
}