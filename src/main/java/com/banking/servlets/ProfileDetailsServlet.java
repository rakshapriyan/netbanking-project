package com.banking.servlets;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;

import com.banking.logical.ProfileDetailsImpl;

public class ProfileDetailsServlet  extends HttpServlet{
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		try {
			ProfileDetailsImpl.getProfileDetails(request, response);
		} catch (JSONException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
