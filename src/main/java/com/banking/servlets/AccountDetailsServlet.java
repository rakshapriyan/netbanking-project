package com.banking.servlets;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;

import com.banking.logical.AccountDetailsImpl;

public class AccountDetailsServlet  extends HttpServlet{
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		try {
			AccountDetailsImpl.getAccountDetails(request, response);
		} catch (JSONException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		
	}
}
