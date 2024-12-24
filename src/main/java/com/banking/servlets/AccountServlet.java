package com.banking.servlets;


import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;

import com.banking.logical.AccountImpl;

public class AccountServlet extends HttpServlet {
	
	
	
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
			System.out.println("In Account servlet");
			AccountImpl.insertAccount(request, response);
		
	}
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			System.out.println("In Account servlet");
			AccountImpl.getAccountByCustomerId(request, response);
		} catch (IOException | JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	

}
