package com.banking.dto;

public class TransactionFilter {

    private String search;
    private String startDate; // Use LocalDate if you're using Java 8+ for better date handling
    private String endDate;   // Use LocalDate if you're using Java 8+ for better date handling
    private Double minAmount;
    private Double maxAmount;
    private String type; // debit, credit, or null for all
    private String status; // success, pending, failed, or null for all

    
    // Getters and Setters
    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Double getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(Double minAmount) {
        this.minAmount = minAmount;
    }

    public Double getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(Double maxAmount) {
        this.maxAmount = maxAmount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

	@Override
	public String toString() {
		return "TransactionFilter [search=" + search + ", startDate=" + startDate + ", endDate=" + endDate
				+ ", minAmount=" + minAmount + ", maxAmount=" + maxAmount + ", type=" + type + ", status=" + status
				+ "]";
	}
    
    
}
