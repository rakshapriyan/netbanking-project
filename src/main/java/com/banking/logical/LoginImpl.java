package com.banking.logical;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;

import com.banking.dto.LoginRequest;
import com.banking.dto.LoginResponse;
import com.banking.entity.Customer;
import com.banking.entity.Employee;
import com.banking.service.AuthService;
import com.banking.util.ConvertJson;

public class LoginImpl {
	
	private static AuthService authService = new AuthService();

	public static void login(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException, JSONException {
	    // Parse the login request
	    LoginRequest loginRequest = ConvertJson.fromJson(request, LoginRequest.class);

	    // Try to fetch Customer
	    Optional<Customer> customer = authService.getUserByEmailOrUsername(loginRequest.getUsername());

	    // Try to fetch Employee
	    Optional<Employee> employee = authService.getEmployeeByEmailOrUsername(loginRequest.getUsername());

	    // If neither customer nor employee exists, return a 403 error
	    if (!customer.isPresent() && !employee.isPresent()) {
	        response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403 Forbidden
	        response.getWriter().write("User not found");
	        return;
	    }

	    // Initialize a variable for the LoginResponse
	    LoginResponse loginResponse = new LoginResponse();

	    // Check if the user is a Customer
	    if (customer.isPresent() && customer.get().getPassword().equals(loginRequest.getPassword())) {
	        Customer validCustomer = customer.get();
	        loginResponse.setName(validCustomer.getName());
	        loginResponse.setRole(validCustomer.getRole());

	        // Generate token for Customer
	        String token = validCustomer.getCustomerId() + ":" + validCustomer.getName() + ":" + validCustomer.getRole();
	        loginResponse.setToken(token);

	    } 
	    // Check if the user is an Employee
	    else if (employee.isPresent() && employee.get().getPassword().equals(loginRequest.getPassword())) {
	        Employee validEmployee = employee.get();
	        loginResponse.setName(validEmployee.getName());
	        loginResponse.setRole(validEmployee.getRole());

	        // Generate token for Employee
	        String token = validEmployee.getEmployeeId() + ":" + validEmployee.getName() + ":" + validEmployee.getRole();
	        loginResponse.setToken(token);

	    } else {
	        // Invalid credentials
	        response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403 Forbidden
	        response.getWriter().write("Invalid credentials");
	        return;
	    }

	    // Convert the response to JSON and return success
	    String json = ConvertJson.toJson(loginResponse);
	    response.setStatus(HttpServletResponse.SC_OK); // 200 OK
	    response.getWriter().write(json);
	}

}
