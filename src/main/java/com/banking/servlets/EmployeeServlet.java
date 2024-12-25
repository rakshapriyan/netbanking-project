//$Id$
package com.banking.servlets;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.banking.logical.CustomerImpl;
import com.banking.logical.EmployeeImpl;

public class EmployeeServlet extends HttpServlet{
	
	
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		System.out.println("In Employee Servlet");
		EmployeeImpl.getEmployees(request, response);
	}
	
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
	}
	
	
	public void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
	}

}
