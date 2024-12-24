package com.banking.logical;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;

import com.banking.dto.AccountCreationDetail;
import com.banking.entity.Account;
import com.banking.service.AccountService;
import com.banking.service.AuthService;
import com.banking.util.ConvertJson;
import com.banking.util.ThreadLocale;

public class AccountImpl {
	
	
	private static AccountService accountService = new AccountService();
	private static AuthService authService = new AuthService();
	
	public static void getAccountByCustomerId(HttpServletRequest request, HttpServletResponse response) throws IOException, JSONException{
//		User user = (User)request.getSession().getAttribute("user");
		List<Account>  accounts = null;
		long userId = ThreadLocale.getUser().getUserId();
		String role = ThreadLocale.getUser().getRole();
		System.out.println("User Id in Account Impl"+ userId);
		
		if(role.equalsIgnoreCase("customer")) {
			accounts = accountService.getAccountByCustomer(userId);
			System.out.println(accounts);
			
		}
		else if(role.equalsIgnoreCase("employee")) {
			long branchId = authService.getEmployeeById(userId).getBranchId();
			accounts = accountService.getAllAccountWithBranch(branchId);
		}
		else if(role.equalsIgnoreCase("manager")) {
			accounts = accountService.getAllAccount();
		}
		 String json = ConvertJson.toJson(accounts);
		 
		 response.getWriter().write(json);
	}
	
	
	public static void insertAccount(HttpServletRequest request, HttpServletResponse response){
		try {
			AccountCreationDetail accountCreationDetail = ConvertJson.fromJson(request, AccountCreationDetail.class);
			
			accountService.addAccount(accountCreationDetail);
		} catch (IOException | JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
