package com.banking.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.banking.config.DBConfig;
import com.banking.config.QueryBuilder;
import com.banking.databaseOperations.DBService;
import com.banking.dto.AccountDetails;
import com.banking.entity.Customer;
import com.banking.entity.Employee;
import com.banking.entity.LoginActivity;
import com.banking.util.Constant;

public class AuthService {

	private QueryBuilder queryBuilder;
	private DBService dbService;


	public AuthService() {
		queryBuilder = new QueryBuilder();
		dbService = new DBService(Constant.YAML_PATH);
	}

	public Optional<Customer> getUserByEmailOrUsername(String email) {

	    String query = queryBuilder
	            .select("*")
	            .table("Customer")
	            .where("emailId", "=", email)
	            .or()
	            .where("username", "=", email)
	            .build();

	    System.out.println(query);

	    // Execute the query and retrieve results
	    List<Customer> customers = dbService.executeQuery(query, Customer.class);

	    // Return the first customer if available, wrapped in Optional
	    return customers.isEmpty() ? Optional.empty() : Optional.of(customers.get(0));
	}
	
	public Optional<Employee> getEmployeeByEmailOrUsername(String email) {

	    String query = queryBuilder
	            .select("*")
	            .table("Employee")
	            .where("emailId", "=", email)
	            .or()
	            .where("username", "=", email)
	            .build();

	    System.out.println(query);

	    // Execute the query and retrieve results
	    List<Employee> employees = dbService.executeQuery(query, Employee.class);

	    // Return the first customer if available, wrapped in Optional
	    return employees.isEmpty() ? Optional.empty() : Optional.of(employees.get(0));
	}
	
	
	public Customer getUserById(long id) {

	    String query = queryBuilder
	            .select("*")
	            .table("Customer")
	            .where("customerId", "=", String.valueOf(id))	            
	            .build();

	    System.out.println(query);

	    // Execute the query and retrieve results
	    List<Customer> customers = dbService.executeQuery(query, Customer.class);

	    // Return the first customer if available, wrapped in Optional
	    return customers.isEmpty() ? null : customers.get(0);
	}
	
	
	public Employee getEmployeeById(long id) {

	    String query = queryBuilder
	            .select("*")
	            .table("Employee")
	            .where("employeeId", "=", String.valueOf(id))	            
	            .build();

	    System.out.println(query);

	    // Execute the query and retrieve results
	    List<Employee> employees = dbService.executeQuery(query, Employee.class);

	    // Return the first customer if available, wrapped in Optional
	    return employees.isEmpty() ? null : employees.get(0);
	}


	public void makeUserActive(String email) {
		Map<String, String> map = new HashMap<>();
		map.put("userStatus", "active");
		String query = queryBuilder.table("Customer").update(map).where("emailId", " = ", email).or().where("username", " = ", email).build();
		System.out.println(query);
	}


	public void makeUserInActive(String email) {
		Map<String, String> map = new HashMap<>();
		map.put("userStatus", "active");
		String query = queryBuilder.table("Customer").update(map).where("emailId", " = ", email).or().where("username", " = ", email).build();
		System.out.println(query);
	}
	
	

	public void addLoginActivity() {

	}

	public Long findCustomerId(String username) {
	    // Generate the query using queryBuilder
	    String query = queryBuilder
	    		.table("Customer")
	        .select("customerId")

	        .where("emailId", " = ", username)
	        .or()
	        .where("username", " = ", username)
	        .build();

	    System.out.println(query);

	    // Initialize the customerId variable
	    Long customerId = dbService.executeQuery(query, Long.class).get(0);
	    return customerId;
	}


	public long addLoginActivity(Long customerId) throws SQLException {
		LoginActivity loginActivity = new LoginActivity();
		loginActivity.setUserId(customerId);
		loginActivity.setLoginTimestamp(Instant.now().toEpochMilli());
		dbService.insert(loginActivity);

		String query = "SELECT login_id FROM login_activity WHERE user_id = ? AND login_timestamp = ? ORDER BY login_timestamp DESC LIMIT 1";
	    long loginId = dbService.executeQuery(query, Long.class).get(0);
	    return loginId;
	}
	
	

	public void addLogoutActivity(int  loginId) throws SQLException {
		Map<String, String> map = new HashMap<>();
		map.put("logoutTimestamp", String.valueOf(Instant.now().toEpochMilli()));
		String query = queryBuilder.table("LoginActivity").update(map).
				where("loginId", " = ", String.valueOf(loginId)).
				build();

		System.out.println(query);
		
		dbService.executeUpdate(query);

	}


	public static void main(String[] args) {
		AuthService authService = new AuthService();
		authService.getUserByEmailOrUsername("john_Doe");
	}

}
