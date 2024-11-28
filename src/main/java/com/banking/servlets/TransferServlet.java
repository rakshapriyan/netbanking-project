package com.banking.servlets;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;

import com.banking.logical.TransferImpl;

public class TransferServlet extends HttpServlet {


	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response){
		try {
			System.out.println("In transfer servlet");
			TransferImpl.transfer(request, response);
		} catch (IOException | JSONException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
