//$Id$
package com.banking.service;

import java.sql.SQLException;
import java.util.List;

import com.banking.config.QueryBuilder;
import com.banking.databaseOperations.DBService;
import com.banking.entity.Customer;
import com.banking.util.Constant;

public class CustomerService {
	
	
	private QueryBuilder queryBuilder;
	private DBService dbService;


	public CustomerService() {
		queryBuilder = new QueryBuilder();
		dbService = new DBService(Constant.YAML_PATH);
	}
	
	
	public List<Customer> getAllCustomer(){
		return dbService.get(Customer.class, null, null);
	}
	
	
	public void addCustomer(Customer customer) throws SQLException {
		dbService.insert(customer);
	}

}
