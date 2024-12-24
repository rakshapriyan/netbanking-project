//$Id$
package com.banking.dto;

import java.math.BigDecimal;

public class AccountCreationDetail {
	
	private long customerId;
	private long branchId;
	private BigDecimal initialBalance;
	public long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}
	public long getBranchId() {
		return branchId;
	}
	public void setBranchId(long branchId) {
		this.branchId = branchId;
	}
	public BigDecimal getInitialBalance() {
		return initialBalance;
	}
	public void setInitialBalance(BigDecimal initialBalance) {
		this.initialBalance = initialBalance;
	}
	@Override
	public String toString() {
		return "AccountCreationDetail [customerId=" + customerId + ", branchId=" + branchId + ", initialBalance=" + initialBalance + "]";
	}
	
	

}
