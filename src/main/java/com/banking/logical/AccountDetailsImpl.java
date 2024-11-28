package com.banking.logical;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;

import com.banking.dto.AccountDetails;
import com.banking.service.AccountService;
import com.banking.util.ConvertJson;
import com.banking.util.ThreadLocale;

public class AccountDetailsImpl {
	
	private static AccountService accountService = new AccountService();
	
	public static void getAccountDetails(HttpServletRequest request, HttpServletResponse response) throws JSONException, IOException {
		long userId = ThreadLocale.getUser().getUserId();
		List<AccountDetails> accountDetails = accountService.getAccountWithBranch(userId);
		
		String json = ConvertJson.toJson(accountDetails);
		response.getWriter().write(json);
		
	}
	

}
