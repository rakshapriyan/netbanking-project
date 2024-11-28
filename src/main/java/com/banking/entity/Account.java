package com.banking.entity;

import java.math.BigDecimal;

public class Account {
	private Long accountNumber;
	private Long customerId;
	private Long branchId;
	private BigDecimal balance;
	private String accountStatus;
	private Long dateOpened;
	private Long dateClosed;
	private Long createdBy;
	private Long createdTimestamp;
	private Long lastModifiedBy;
	private Long lastModifiedTimestamp;

	// Getters and Setters
	public Long getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(Long accountNumber) {
		this.accountNumber = accountNumber;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public Long getBranchId() {
		return branchId;
	}

	public void setBranchId(Long branchId) {
		this.branchId = branchId;
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

	public Long getDateClosed() {
		return dateClosed;
	}

	public void setDateClosed(Long dateClosed) {
		this.dateClosed = dateClosed;
	}

	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public Long getCreatedTimestamp() {
		return createdTimestamp;
	}

	public void setCreatedTimestamp(Long createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
	}

	public Long getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(Long lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	public Long getLastModifiedTimestamp() {
		return lastModifiedTimestamp;
	}

	public void setLastModifiedTimestamp(Long lastModifiedTimestamp) {
		this.lastModifiedTimestamp = lastModifiedTimestamp;
	}

	@Override
	public String toString() {
		return "Account [accountNumber=" + accountNumber + ", customerId=" + customerId + ", branchId=" + branchId
				+ ", balance=" + balance + ", accountStatus=" + accountStatus + ", dateOpened=" + dateOpened
				+ ", dateClosed=" + dateClosed + ", createdBy=" + createdBy + ", createdTimestamp=" + createdTimestamp
				+ ", lastModifiedBy=" + lastModifiedBy + ", lastModifiedTimestamp=" + lastModifiedTimestamp + "]";
	}



}
