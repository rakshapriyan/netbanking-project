package com.banking.service;


import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.banking.config.Criteria;
import com.banking.config.QueryBuilder;
import com.banking.databaseOperations.DBService;
import com.banking.dto.TransferDTO;
import com.banking.entity.Account;
import com.banking.entity.Transaction;
import com.banking.util.ThreadLocale;

public class TransferService {
	
	private QueryBuilder queryBuilder;
	private DBService dbService;
	private AccountService service;

	public TransferService() {
		queryBuilder = new QueryBuilder();
		dbService = new DBService("/home/raksh-pt7616/eclipse-ee-workspace/Netbanking1/src/main/java/mapping.yaml");
		service = new AccountService();
	}
	
	
	
	public void transferAmount(TransferDTO dto) throws SQLException {
		System.out.println("In transfer service");
		Long userId = ThreadLocale.getUser().getUserId();
		Long recipientAccountNumber = null;
		
		
		Account account = getAccount(dto.getFromAccount());
		
		
		if (account.getBalance().compareTo(dto.getAmount()) < 0) {
		    // account balance is less than the amount
		}

		
		if((dto.getIfscCode() == null || dto.getIfscCode().equals(""))) {
			Account toAccount = getAccount(dto.getToAccount());
			if(toAccount == null) {
				// error here
			}
			recipientAccountNumber = toAccount.getAccountNumber();
		}
		
		
		
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
		
		if(recipientAccountNumber != null) {
			addForRecipient(transaction);
			dbService.insert(transaction);
			
		}
	}
	
	
	public int updateBalance(long accNo, BigDecimal amount) {
		Map<String, String> value = new HashMap<String, String>();
		value.put("balance", String.valueOf(amount.floatValue()));
		Criteria c = new  Criteria();
		c.setColumn("accountNumber");
		c.setCompareOperator(" = ");
		c.setValue(String.valueOf(accNo));
		List<Criteria> criterias = new ArrayList<Criteria>();
		criterias.add(c);
		int result = dbService.performUpdate(Account.class.getSimpleName(), value, criterias, null);
		return result;
	}
	
	
	private void addForRecipient(Transaction transaction) {
		long customerId = new AccountService().getIdByAccount(transaction.getAccountNumber());
		transaction.setCustomerId(customerId);
		long accountNumber = transaction.getTransactionAccountNumber();
		transaction.setTransactionAccountNumber(transaction.getAccountNumber());
		transaction.setAccountNumber(accountNumber);
		transaction.setTransactionType("credit");
	
	}
	
	public Account getAccount(Long accNo) {
		Account account = service.getAccountById(accNo);
		return account;
	}

}
