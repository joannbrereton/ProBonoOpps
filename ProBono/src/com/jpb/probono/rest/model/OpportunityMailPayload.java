package com.jpb.probono.rest.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class OpportunityMailPayload implements Parcelable {
	ContactInfo contact;
	String      comment;
	String      listOfOpps;
	
	public OpportunityMailPayload()
	{
		
	}
	public OpportunityMailPayload(Parcel parcel) {
		this();
		Log.i("OpportunityMailPayload(Parcel)","Entry" + parcel);
		readFromParcel(parcel);		
		Log.i("OpportunityMailPayload(Parcel)","Exit");
		
	}
	
	
	public String getListOfOpps() {
		return listOfOpps;
	}
	public void setListOfOpps(String listOfOpps) {
		this.listOfOpps = listOfOpps;
	}
	public ContactInfo getContact() {
		return contact;
	}
	public String getComment() {
		return comment;
	}
	public void setContact(ContactInfo contact) {
		this.contact = contact;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	@Override
	public int describeContents() {
		return 0;
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		Log.i("OpportunityMailPayload.writeToParcel","Entry");
		dest.writeSerializable(contact);
		dest.writeString(comment);
		dest.writeString(listOfOpps);
		Log.i("OpportunityMailPayload.writeToParcel","Exit");

	}

	public final static Parcelable.Creator<OpportunityMailPayload> CREATOR = new Parcelable.Creator<OpportunityMailPayload>() {
		public OpportunityMailPayload createFromParcel(Parcel source) {
			return new OpportunityMailPayload(source);
			
		}

		public OpportunityMailPayload[] newArray(int size) {
			return new OpportunityMailPayload[size];
		}
	};
	
	private void readFromParcel(Parcel parcel) {
	    contact = (ContactInfo)parcel.readSerializable();
	    comment = parcel.readString();
	    listOfOpps = parcel.readString();
	}
}
