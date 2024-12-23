package com.banking.service;

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
import com.banking.entity.Branch;
import com.banking.util.Constant;


public class AccountService {


	private QueryBuilder queryBuilder;
	private DBService dbService;

	public AccountService() {
		queryBuilder = new QueryBuilder();
		dbService = new DBService(Constant.YAML_PATH);
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
	
	
	public Account getAccountById(long accountNumber) {
		Criteria c = new Criteria();
		c.setColumn("accountNumber");
		c.setCompareOperator(" = ");
		c.setValue(String.valueOf(accountNumber));
		List<Criteria> criterias = new ArrayList<Criteria>();
		criterias.add(c);
		
		Account account = (Account) dbService.get(Account.class, criterias, null).get(0);
		return account;
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
	
	public List<Account> getAllAccountWithBranch(long branchId){
		Criteria c = new Criteria();
		c.setColumn("branchId");
		c.setCompareOperator(" = ");
		c.setValue(String.valueOf(branchId));
		List<Criteria> criterias = new ArrayList<Criteria>();
		criterias.add(c);
		List<Account> accounts = dbService.get(Account.class, criterias, null);
		return accounts;
	}
	
	public List<Account> getAllAccount(){
		
		List<Account> accounts = dbService.get(Account.class, null, null);
		return accounts;
	}
	
	
	public List<AccountDetails> toAccountDetails(List<Account> accounts){
		List<AccountDetails> accountDetailss = new ArrayList<>();
		List<Branch> branchs = new BranchService().getAllBranches();
		for(Account account : accounts) {
			Branch branch = getBranchBybranchId(account.getBranchId(), branchs);
			AccountDetails ad = new AccountDetails();
			ad.setAccountNumber(account.getAccountNumber());
			ad.setAccountStatus(account.getAccountStatus());
			ad.setBalance(account.getBalance());
			ad.setCity(branch.getCity());
			ad.setDateOpened(account.getCreatedTimestamp());
			ad.setIfscCode(branch.getIfscCode());
			ad.setPincode(branch.getPincode());
			ad.setState(branch.getState());
		}
		
		return accountDetailss;
	}
	
	public Branch getBranchBybranchId(Long branchId,List<Branch> branchs ) {
		for(Branch b : branchs) {
			if(b.getBranchId() == branchId) {
				return b;
			}
		}
		return null;
	}


	public static void main(String[] args) {
		AccountService accountService = new AccountService();

		System.out.println(accountService.getAllAccount());

	}

}
