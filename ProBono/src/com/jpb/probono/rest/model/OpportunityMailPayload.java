package com.jpb.probono.rest.model;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class OpportunityMailPayload implements Parcelable {
	ContactInfo contact;
	String      comment;
	ArrayList<Opportunity>      listOfOpps;
	
	public OpportunityMailPayload()
	{
		
	}
	public OpportunityMailPayload(Parcel parcel) {
		this();
		Log.i("OpportunityMailPayload(Parcel)","Entry" + parcel);
		readFromParcel(parcel);		
		Log.i("OpportunityMailPayload(Parcel)","Exit");
		
	}
	
	
	public ArrayList<Opportunity> getListOfOpps() {
		return listOfOpps;
	}
	public void setListOfOpps(ArrayList<Opportunity> listOfOpps) {
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
		dest.writeTypedList(listOfOpps);
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
		Log.i("OpportunityMailPayload.readFromParcel","Entry");
	    contact = (ContactInfo)parcel.readSerializable();
	    comment = parcel.readString();
	    listOfOpps = new ArrayList<Opportunity>();
	    parcel.readTypedList(listOfOpps,Opportunity.CREATOR);
		Log.i("OpportunityMailPayload.readFromParcel","Exit");

	}
}
