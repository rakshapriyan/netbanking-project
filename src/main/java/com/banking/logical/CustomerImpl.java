//$Id$
package com.banking.logical;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;

import com.banking.entity.Customer;
import com.banking.service.CustomerService;
import com.banking.util.ConvertJson;

public class CustomerImpl {
	
	private static CustomerService customerService = new CustomerService();
	
	public static void getAllCustomers(HttpServletRequest request, HttpServletResponse response) {
		List<Customer> customers = customerService.getAllCustomer();
		try {
			String json = ConvertJson.toJson(customers);
			response.getWriter().write(json);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void addCustomer(HttpServletRequest request, HttpServletResponse response) {
		Customer customer;
		try {
			customer = ConvertJson.fromJson(request, Customer.class);
			customerService.addCustomer(customer);
		} catch (IOException | JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public static void updateCustomer(HttpServletRequest request, HttpServletResponse response) {
		Customer customer;
		try {
			customer = ConvertJson.fromJson(request, Customer.class);
			customerService.addCustomer(customer);
		} catch (IOException | JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
