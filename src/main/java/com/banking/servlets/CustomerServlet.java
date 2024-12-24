//$Id$
package com.banking.servlets;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.banking.logical.CustomerImpl;

public class CustomerServlet extends HttpServlet {
	
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		CustomerImpl.getAllCustomers(request, response);
	}
	
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		CustomerImpl.getAllCustomers(request, response);
	}
	
	
	public void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
		CustomerImpl.getAllCustomers(request, response);
	}
}
