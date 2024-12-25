//$Id$
package com.banking.service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	
	
	public Customer getCustomerById(long id) {

	    String query = queryBuilder
	            .select("*")
	            .table("Customer")
	            .where("customerId", "=", String.valueOf(id))	            
	            .build();

	    System.out.println(query);

	    List<Customer> customers = dbService.executeQuery(query, Customer.class);

	    return customers.isEmpty() ? null : customers.get(0);
	}
	
	
	public int updateCustomer(Customer customer) throws SQLException {
	
	    Customer existingCustomer = getCustomerById(customer.getUserId());


	    QueryBuilder query = queryBuilder.table("Customer");
	    
	  
	    Map<String, String> map = new HashMap<>();

	
	    if (!customer.getName().equals(existingCustomer.getName())) {
	        map.put("name", customer.getName());
	    }
	    if (!customer.getPhoneNumber().equals(existingCustomer.getPhoneNumber())) {
	        map.put("phoneNumber", customer.getPhoneNumber().toString());
	    }
	    if (!customer.getEmailId().equals(existingCustomer.getEmailId())) {
	        map.put("emailId", customer.getEmailId());
	    }
	    
	    if (!customer.getAddress().equals(existingCustomer.getAddress())) {
	        map.put("address", customer.getAddress());
	    }
	    if (!customer.getCity().equals(existingCustomer.getCity())) {
	        map.put("city", customer.getCity());
	    }
	    if (!customer.getState().equals(existingCustomer.getState())) {
	        map.put("state", customer.getState());
	    }
	    if (!customer.getPincode().equals(existingCustomer.getPincode())) {
	        map.put("pincode", customer.getPincode().toString());
	    }
	    if (!map.isEmpty()) {

	        query.update(map).where("userId"," = ",String.valueOf(customer.getUserId().toString()));
	        return dbService.executeUpdate(query.build());
	    }

	    return 0;
	}
}