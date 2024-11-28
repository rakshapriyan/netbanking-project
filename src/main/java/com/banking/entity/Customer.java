package com.banking.entity;

public class Customer extends User {
	private Long customerId;
	private Long aadharNumber;
	private String panNumber;
	private String address;
	private String city;
	private String state;
	private Integer pincode;

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public Long getAadharNumber() {
		return aadharNumber;
	}

	public void setAadharNumber(Long aadharNumber) {
		this.aadharNumber = aadharNumber;
	}

	public String getPanNumber() {
		return panNumber;
	}

	public void setPanNumber(String panNumber) {
		this.panNumber = panNumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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

	public Integer getPincode() {
		return pincode;
	}

	public void setPincode(Integer pincode) {
		this.pincode = pincode;
	}

	@Override
	public String toString() {
		return "Customer [customerId=" + customerId + ", aadharNumber=" + aadharNumber + ", panNumber=" + panNumber
				+ ", address=" + address + ", city=" + city + ", state=" + state + ", pincode=" + pincode + ", userId="
				+ userId + ", username=" + username + ", password=" + password + ", name=" + name + ", phoneNumber="
				+ phoneNumber + ", emailId=" + emailId + ", role=" + role + ", isOnline=" + isOnline + ", userStatus="
				+ userStatus + ", createdBy=" + createdBy + ", createdTimestamp=" + createdTimestamp
				+ ", lastModifiedBy=" + lastModifiedBy + ", lastModifiedTimestamp=" + lastModifiedTimestamp
				+ ", getCustomerId()=" + getCustomerId() + ", getAadharNumber()=" + getAadharNumber()
				+ ", getPanNumber()=" + getPanNumber() + ", getAddress()=" + getAddress() + ", getCity()=" + getCity()
				+ ", getState()=" + getState() + ", getPincode()=" + getPincode() + ", getUserId()=" + getUserId()
				+ ", getUsername()=" + getUsername() + ", getPassword()=" + getPassword() + ", getName()=" + getName()
				+ ", getPhoneNumber()=" + getPhoneNumber() + ", getEmailId()=" + getEmailId() + ", getRole()="
				+ getRole() + ", getIsOnline()=" + getIsOnline() + ", getUserStatus()=" + getUserStatus()
				+ ", getCreatedBy()=" + getCreatedBy() + ", getCreatedTimestamp()=" + getCreatedTimestamp()
				+ ", getLastModifiedBy()=" + getLastModifiedBy() + ", getLastModifiedTimestamp()="
				+ getLastModifiedTimestamp() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode()
				+ ", toString()=" + super.toString() + "]";
	}




}