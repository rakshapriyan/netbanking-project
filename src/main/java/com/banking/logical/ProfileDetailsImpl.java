package com.banking.logical;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;

import com.banking.entity.Customer;
import com.banking.service.AuthService;
import com.banking.util.ConvertJson;
import com.banking.util.ThreadLocale;

public class ProfileDetailsImpl {
	
	private static AuthService authService = new AuthService();
	
	public static void getProfileDetails(HttpServletRequest request, HttpServletResponse response) throws JSONException, IOException {
		long userid = ThreadLocale.getUser().getUserId();
		Customer customer = authService.getUserById(userid);
		String json = ConvertJson.toJson(customer);
		response.getWriter().write(json);
		
		
	}

}
