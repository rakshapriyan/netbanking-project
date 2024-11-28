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
	
	public List<Transaction> getLastNTransactions(Long user_id, int size){
		String query = queryBuilder.table(Transaction.class.getSimpleName())
				.select("*").where("customerId", " = ",String.valueOf(user_id))
				.limit(size).orderBy("timestamp", "DESC").build();
		
		List<Transaction> transactions = dbService.executeQuery(query, Transaction.class);
		
		System.out.println(query);
		return transactions;
	}
	
	public void addTransaction(Transaction transaction) throws SQLException {
		dbService.insert(transaction);
	}
	
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

	public List<Transaction> getTransactionWithFilter(long userId, TransactionFilter filter) {
	    QueryBuilder query = queryBuilder.table("Transaction")
	                                      .select("*")
	                                      .where("customerId", "=", String.valueOf(userId))
	                                      .and();

	    if (!Objects.equals(filter.getSearch(), "")) {
	        query.where("description", "LIKE", "%" + filter.getSearch().toLowerCase() + "%").and();
	    }

	    if (!Objects.equals(filter.getStartDate(), "")) {
	        long startMillis = convertDateToMillis(filter.getStartDate());
	        query.where("timestamp", ">=", String.valueOf(startMillis)).and();
	    }

	    if (!Objects.equals(filter.getEndDate(), "")) {
	        long endMillis = convertDateToMillis(filter.getEndDate());
	        query.where("timestamp", "<=", String.valueOf(endMillis)).and();
	    }

	    if (filter.getMinAmount() != null) {
	        query.where("amount", ">=", filter.getMinAmount().toString()).and();
	    }

	    if (filter.getMaxAmount() != null) {
	        query.where("amount", "<=", filter.getMaxAmount().toString()).and();
	    }

	    if (!Objects.equals(filter.getType(), "")) {
	        query.where("transactionType", "=", filter.getType()).and();
	    }

	    if (!Objects.equals(filter.getStatus(), "")) {
	        query.where("status", "=", filter.getStatus());
	    }

	    String finalQuery = query.build();
	    System.out.println("This is the final Query: " + finalQuery);
	    return dbService.executeQuery(finalQuery, Transaction.class);
	}


	
	
	private long convertDateToMillis(String date) {
	    try {
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	        Date parsedDate = sdf.parse(date);
	        return parsedDate.getTime();
	    } catch (ParseException e) {
	        throw new IllegalArgumentException("Invalid date format. Please use yyyy-MM-dd.");
	    }
	}
	
	public List<Integer> getWeeklyTransactionCounts(List<Transaction> transactions) {
		

	    List<Integer> transactionCounts = new ArrayList<>();
	    for (int i = 0; i < 7; i++) {
	        transactionCounts.add(0);
	    }

	    for (Transaction transaction : transactions) {
	        LocalDate transactionDate = Instant.ofEpochMilli(transaction.getTimestamp())
	                                           .atZone(ZoneId.systemDefault())
	                                           .toLocalDate();

	        DayOfWeek dayOfWeek = transactionDate.getDayOfWeek();

	        int index = (dayOfWeek.getValue() % 7); // Adjust to make Sunday index 0

	        transactionCounts.set(index, transactionCounts.get(index) + 1);
	    }

	    return transactionCounts;
	}

	
	
	
	
	
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
	

	
	public static void main(String[] args) {
		TransactionService service = new TransactionService();
		System.out.println(service.getLastNTransactionOfBank());
		System.out.println(service.getWeeklyTransactionCounts(service.getLastNTransactionOfBranch(1)));
	}
}
