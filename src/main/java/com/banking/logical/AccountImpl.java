package com.banking.logical;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;

import com.banking.entity.Account;
import com.banking.entity.User;
import com.banking.service.AccountService;
import com.banking.util.ConvertJson;
import com.banking.util.ThreadLocale;

public class AccountImpl {
	
	
	private static AccountService accountService = new AccountService();
	
	public static void getAccountByCustomerId(HttpServletRequest request, HttpServletResponse response) throws IOException, JSONException{
//		User user = (User)request.getSession().getAttribute("user");
		long customerId = ThreadLocale.getUser().getUserId();
		System.out.println("User Id in Account Impl"+ customerId);
		 List<Account>  accounts = accountService.getAccountByCustomer(customerId);
		 System.out.println(accounts);
		 String json = ConvertJson.toJson(accounts);
		 
		 response.getWriter().write(json);
	}

}
