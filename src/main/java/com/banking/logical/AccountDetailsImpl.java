package com.banking.logical;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;

import com.banking.dto.AccountDetails;
import com.banking.entity.Account;
import com.banking.service.AccountService;
import com.banking.util.ConvertJson;
import com.banking.util.ThreadLocale;

public class AccountDetailsImpl {
	
	private static AccountService accountService = new AccountService();
	
	public static void getAccountDetails(HttpServletRequest request, HttpServletResponse response) throws JSONException, IOException {
		long userId = ThreadLocale.getUser().getUserId();
		String role = ThreadLocale.getUser().getRole();
		System.out.println("Printing the role of the user"+role);
		List<AccountDetails> accountDetails;
		if(role.equalsIgnoreCase("manager")) {
			System.out.println("Entered user is manager");
			List<Account> accounts = accountService.getAllAccount();
			accountDetails = accountService.toAccountDetails(accounts);
		}
		else if(role.equalsIgnoreCase("employee")) {
			accountDetails = accountService.getAccountWithBranch(userId);
		}
		else {
			accountDetails = null;
		}
		
		String json = ConvertJson.toJson(accountDetails);
		response.getWriter().write(json);
		
	}
}
