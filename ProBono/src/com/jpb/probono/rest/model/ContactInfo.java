package com.jpb.probono.rest.model;

import java.io.Serializable;


public class ContactInfo implements Serializable  {
	private static final long serialVersionUID = 1L;



	private String address1;
	private String address2;
	private String firmName;
	private String city;
	private String email;
	private String firstName;
	private String lastName;
	private String state;
	private String zip;
    private String phone;

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public ContactInfo() {
	}

	public ContactInfo(String firstname, String lastname, String address1,
			String address2, String city, String state, String zip,
			String email, String phone) {
		super();
		this.firstName = firstname;
		this.lastName = lastname;
		this.address1 = address1;
		this.address2 = address2;
		this.city = city;
		this.state = state;
		this.zip = zip;
		this.email = email;
		this.phone = phone;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstname) {
		this.firstName = firstname;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastname) {
		this.lastName = lastname;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getFirmName() {
		return firmName;
	}

	public void setFirmName(String firmName) {
		this.firmName = firmName;
	}
	
	public boolean isValid() {
		boolean isValid = false;
		if (!(firstName == null) && !(lastName == null) && !(firmName == null) && !(email == null)) {
			isValid = true;
		}
		return isValid;
	}

	public String toString()
	{
		return "firstName = " + firstName + " lastName = " + lastName + " firmName = " + firmName + " email = " + email + " phone " + phone;
	}
	
}
