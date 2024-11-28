package com.banking.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.banking.config.Criteria;
import com.banking.config.DBConfig;
import com.banking.config.QueryBuilder;
import com.banking.databaseOperations.DBService;
import com.banking.dto.AccountDetails;
import com.banking.entity.Account;


public class AccountService {


	private QueryBuilder queryBuilder;
	private DBService dbService;

	public AccountService() {
		queryBuilder = new QueryBuilder();
		dbService = new DBService("/home/raksh-pt7616/eclipse-ee-workspace/Netbanking1/src/main/java/mapping.yaml");
	}

	public void addAccount(Account account) throws SQLException {
		dbService.insert(account);
	}

	public List<Account> getAccountByCustomer(Long customerId){
		Criteria c = new Criteria();
		c.setColumn("customerId");
		c.setCompareOperator(" = ");
		c.setValue(String.valueOf(customerId));
		List<Criteria> criteria = new ArrayList<>();
		criteria.add(c);
		List<Account> list = dbService.get(Account.class, criteria, null);
		return list;

	}
	
	public Long getIdByAccount(long accountNumber) {
	    String query = queryBuilder.table("Account")
	                               .select("customerId")
	                               .where("accountNumber", "=", String.valueOf(accountNumber))
	                               .build();
	    System.out.println(query +"From account service");
	    
	    Long id = dbService.executeQuery(query, Long.class).get(0);
	    return id;
	}
	
	public List<AccountDetails> getAccountWithBranch(long userId){
		String query = queryBuilder.table("Account").select("*")
				.join("branch", "account.branch_id", "branch.branch_id")
				.where("customerId", " = ", String.valueOf(userId)).build();
		
		
		List<AccountDetails> accountDetailsList = new ArrayList<>();
		
		try (PreparedStatement preparedStatement = DBConfig.getConnection().prepareStatement(query)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    AccountDetails accountDetails = new AccountDetails();

                    accountDetails.setAccountNumber(resultSet.getLong("account_number"));
                    accountDetails.setBalance(resultSet.getBigDecimal("balance"));
                    accountDetails.setAccountStatus(resultSet.getString("account_status"));
                    accountDetails.setDateOpened(resultSet.getLong("date_opened"));
                    accountDetails.setName(resultSet.getString("name"));
                    accountDetails.setIfscCode(resultSet.getString("ifsc_code"));
                    accountDetails.setCity(resultSet.getString("city"));
                    accountDetails.setState(resultSet.getString("state"));
                    accountDetails.setPincode(resultSet.getInt("pincode"));

                    accountDetailsList.add(accountDetails);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
           
        }

        return accountDetailsList;
		
	}


	public static void main(String[] args) {
		AccountService accountService = new AccountService();

		System.out.println(accountService.getAccountWithBranch(1L));

	}

}