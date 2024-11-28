package com.banking.dto;

import java.math.BigDecimal;

public class AccountDetails {
	private Long accountNumber;
	private BigDecimal balance;
	private String accountStatus;
	private Long dateOpened;
	private String name;
	private String ifscCode;
	private String city;
	private String state;
	private Integer pincode;
	
	
	
	public Long getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(Long accountNumber) {
		this.accountNumber = accountNumber;
	}
	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	public String getAccountStatus() {
		return accountStatus;
	}
	public void setAccountStatus(String accountStatus) {
		this.accountStatus = accountStatus;
	}
	public Long getDateOpened() {
		return dateOpened;
	}
	public void setDateOpened(Long dateOpened) {
		this.dateOpened = dateOpened;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIfscCode() {
		return ifscCode;
	}
	public void setIfscCode(String ifscCode) {
		this.ifscCode = ifscCode;
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
		return "AccountDetails [accountNumber=" + accountNumber + ", balance=" + balance + ", accountStatus="
				+ accountStatus + ", dateOpened=" + dateOpened + ", name=" + name + ", ifscCode=" + ifscCode + ", city="
				+ city + ", state=" + state + ", pincode=" + pincode + "]";
	}

	
}
