package com.banking.service;


import java.sql.SQLException;

import com.banking.config.QueryBuilder;
import com.banking.databaseOperations.DBService;
import com.banking.dto.TransferDTO;
import com.banking.entity.Transaction;
import com.banking.util.ThreadLocale;

public class TransferService {
	
	private QueryBuilder queryBuilder;
	private DBService dbService;

	public TransferService() {
		queryBuilder = new QueryBuilder();
		dbService = new DBService("/home/raksh-pt7616/eclipse-ee-workspace/Netbanking1/src/main/java/mapping.yaml");
	}
	
	public void transferAmount(TransferDTO dto) throws SQLException {
		System.out.println("In transfer service");
		Long userId = ThreadLocale.getUser().getUserId();
		
		
		long lastTransactionId = new TransactionService().getLastTransactionId() + 1;
		Transaction transaction = new Transaction();
		
		
		transaction.setTransactionNumber(lastTransactionId);
		transaction.setCustomerId(userId);
		transaction.setAccountNumber(dto.getFromAccount());
		transaction.setTransactionAccountNumber(dto.getToAccount());
		transaction.setTransactionType("debit");
		transaction.setAmount(dto.getAmount());
		transaction.setTimestamp(System.currentTimeMillis());
		transaction.setStatus("success");
		transaction.setDescription(dto.getDescription());
		transaction.setCreatedBy(userId);
		transaction.setCreatedTimestamp(System.currentTimeMillis());
		
		
		dbService.insert(transaction);
		addForRecipient(transaction);
		dbService.insert(transaction);
	}
	
	
	private void addForRecipient(Transaction transaction) {
		long customerId = new AccountService().getIdByAccount(transaction.getAccountNumber());
		transaction.setCustomerId(customerId);
		long accountNumber = transaction.getTransactionAccountNumber();
		transaction.setTransactionAccountNumber(transaction.getAccountNumber());
		transaction.setAccountNumber(accountNumber);
		transaction.setTransactionType("credit");
	
	}

}
