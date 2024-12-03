package com.banking.service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import com.banking.config.DBConfig;
import com.banking.config.QueryBuilder;
import com.banking.databaseOperations.DBService;
import com.banking.dto.TransactionFilter;
import com.banking.entity.Transaction;

public class TransactionService {
	
	private QueryBuilder queryBuilder;
	private DBService dbService;

	public TransactionService() {
		queryBuilder = new QueryBuilder();
		dbService = new DBService("/home/raksh-pt7616/eclipse-ee-workspace/Netbanking1/src/main/java/mapping.yaml");
	}
	
	
	//add a transaction
	public void addTransaction(Transaction transaction) throws SQLException {
		dbService.insert(transaction);
	}
	
	
	// get last transaction id for insertion
	public long getLastTransactionId() {
	    String query = queryBuilder.table("Transaction")
	                                .select("transactionNumber")
	                                .orderBy("transactionNumber", "DESC")
	                                .limit(1)
	                                .build();
	    
	    System.out.println(query);

	    long transactionNumber = dbService.executeQuery(query, Long.class).get(0);
	    return transactionNumber;
	}
	
	// Last N transactions of a user
	public List<Transaction> getLastNTransactions(Long user_id){
		String query = queryBuilder.table(Transaction.class.getSimpleName())
				.select("*").where("customerId", " = ",String.valueOf(user_id))
				.orderBy("timestamp", "DESC").build();
		
		List<Transaction> transactions = dbService.executeQuery(query, Transaction.class);
		
		System.out.println(query);
		return transactions;
	}
	
	// last N trasactions of a bank
	public List<Transaction> getLastNTransactionOfBank(){
		
		List<Transaction> transactions = new ArrayList<Transaction>();
		LocalDate today = LocalDate.now();

        LocalDate previousSunday = today.with(TemporalAdjusters.previous(DayOfWeek.SUNDAY));
        System.out.println(previousSunday);
        long millis = previousSunday.atStartOfDay().atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli();
        String query = queryBuilder.table("Transaction").select("*").where("timestamp", ">=", String.valueOf(millis)).build();

        transactions = dbService.executeQuery(query, Transaction.class);
        
        return transactions;
	}
	
	
	
	// last N transactions of a branch
	public List<Transaction> getLastNTransactionOfBranch(long branchId){
		
		List<Transaction> transactions = new ArrayList<Transaction>();
		LocalDate today = LocalDate.now();

        LocalDate previousSunday = today.with(TemporalAdjusters.previous(DayOfWeek.SUNDAY));
        System.out.println(previousSunday);
        long millis = previousSunday.atStartOfDay().atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli();
        String query = queryBuilder.table("Transaction").select("*").join("account", "transaction.account_number", "account.account_number")
        		.where("timestamp", ">=", String.valueOf(millis)).and().where("branch_id", " = ", String.valueOf(branchId)).build();
        
        
        System.out.println(query);
        transactions = dbService.executeQuery(query, Transaction.class);
        
        return transactions;
			
	}
	
	
		
		// helper method for writing queries with filter
		private QueryBuilder getTransactionWithFilter(TransactionFilter filter, QueryBuilder query) {
	    
	    if (filter.getSearch() != null && !filter.getSearch().isEmpty()) {
	    	query.and();
	        query.where("description", "LIKE", "%" + filter.getSearch().toLowerCase() + "%");
	    }
	
	    if (filter.getStartDate() != null && !filter.getStartDate().isEmpty()) {
	    	query.and();
	        long startMillis = convertDateToMillis(filter.getStartDate());
	        query.where("timestamp", ">=", String.valueOf(startMillis));
	    }
	
	    if (filter.getEndDate() != null && !filter.getEndDate().isEmpty()) {
	    	query.and();
	        long endMillis = convertDateToMillis(filter.getEndDate());
	        query.where("timestamp", "<=", String.valueOf(endMillis));
	    }
	
	    if (filter.getMinAmount() != null) {
	    	query.and();
	        query.where("amount", ">=", filter.getMinAmount().toString());
	    }
	
	    if (filter.getMaxAmount() != null) {
	    	query.and();
	        query.where("amount", "<=", filter.getMaxAmount().toString());
	    }
	
	    if (filter.getType() != null && !filter.getType().isEmpty()) {
	    	query.and();
	        query.where("transactionType", "=", filter.getType());
	    }
	
	    if (filter.getStatus() != null && !filter.getStatus().isEmpty()) {
	    	query.and();
	        query.where("status", "=", filter.getStatus());
	    }
	    
	    return query;
	 
	}
		
	// Transactions with filter for a particular user;
	public List<Transaction> getCustomerTransactionWithFilter(TransactionFilter filter, long userId) {
		QueryBuilder querybuilder = queryBuilder.table("Transaction").select("*").where("customerId", "=",String.valueOf(userId));
         
		String query = getTransactionWithFilter(filter, querybuilder).build();
		
		List<Transaction> list = dbService.executeQuery(query, Transaction.class);
		return list;
		
	}
	
	
	// Transactions with filter for a particular Bank;
		public List<Transaction> getBankTransactionWithFilter(TransactionFilter filter) {
			
			QueryBuilder querybuilder = queryBuilder.table("Transaction").select("*");
			
			String query = getTransactionWithFilter(filter, querybuilder).build();
			
			List<Transaction> list = dbService.executeQuery(query, Transaction.class);
			return list;
			
		}
		
		
		//  Transactions with filter for a particular Branch;
		public List<Transaction> getBranchTransactionWithFilter(TransactionFilter filter, long branchId){
			LocalDate today = LocalDate.now();



	        QueryBuilder querybuilder = queryBuilder.table("Transaction").select("*")
	        		.join("account", "transaction.account_number", "account.account_number")
	        		.where("branch_id", " = ", String.valueOf(branchId));
	        String query = getTransactionWithFilter(filter, querybuilder).build();
	        
	        List<Transaction> list = dbService.executeQuery(query, Transaction.class);
			return list;
	        
		}


	
	// helper method to convert date to millis
	private long convertDateToMillis(String date) {
	    try {
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	        Date parsedDate = sdf.parse(date);
	        return parsedDate.getTime();
	    } catch (ParseException e) {
	        throw new IllegalArgumentException("Invalid date format. Please use yyyy-MM-dd.");
	    }
	}
	
	
	
	

	
	public static void main(String[] args) {
		TransactionService service = new TransactionService();
		System.out.println(service.getLastNTransactionOfBank());
	}
}
