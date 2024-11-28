package com.banking.logical;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;

import com.banking.dto.TransactionFilter;
import com.banking.entity.Transaction;
import com.banking.service.AuthService;
import com.banking.service.TransactionService;
import com.banking.util.CachedBodyHttpServletRequestWrapper;
import com.banking.util.ConvertJson;
import com.banking.util.ThreadLocale;

public class TransactionImpl {

    private static final String ROLE_CUSTOMER = "customer";
    private static final String ROLE_EMPLOYEE = "employee";

    private static final TransactionService transactionService = new TransactionService();
    private static final AuthService authService = new AuthService();

    public static void getTransaction(HttpServletRequest request, HttpServletResponse response) throws IOException, JSONException {
    	System.out.println("In transaction Impl");
        long customerId = ThreadLocale.getUser().getUserId();
        String role = ThreadLocale.getUser().getRole();

        switch (role) {
            case ROLE_CUSTOMER:
                handleCustomerTransactions(request, response, customerId);
                break;

            case ROLE_EMPLOYEE:
                handleEmployeeTransactions(response, customerId);
                break;

            default:
                handleBankTransactions(response);
                break;
        }
    }

    private static void handleCustomerTransactions(HttpServletRequest request, HttpServletResponse response, long customerId) throws IOException, JSONException {
        // Wrap the request body for multiple reads
        CachedBodyHttpServletRequestWrapper wrappedRequest = new CachedBodyHttpServletRequestWrapper(request);
        String requestBody = wrappedRequest.getBody();

        if (isTransactionFilterRequest(requestBody)) {
            TransactionFilter filter = ConvertJson.fromJson(wrappedRequest, TransactionFilter.class);
            List<Transaction> transactions = transactionService.getTransactionWithFilter(customerId, filter);
            writeResponse(response, transactions);
        } else {
            List<Transaction> transactions = transactionService.getLastNTransactions(customerId, 10);
            writeResponse(response, transactions);
        }
    }

    private static void handleEmployeeTransactions(HttpServletResponse response, long customerId) throws IOException, JSONException {
        long branchId = authService.getEmployeeById(customerId).getBranchId();
        List<Transaction> transactions = transactionService.getLastNTransactionOfBranch(branchId);
        writeResponse(response, transactions);
    }

    private static void handleBankTransactions(HttpServletResponse response) throws IOException, JSONException {
        List<Transaction> transactions = transactionService.getLastNTransactionOfBank();
        writeResponse(response, transactions);
    }

    private static boolean isTransactionFilterRequest(String requestBody) {
        return requestBody != null && (requestBody.contains("search") || requestBody.contains("minAmount"));
    }

    private static void writeResponse(HttpServletResponse response, List<Transaction> transactions) throws IOException, JSONException {
        String json = ConvertJson.toJson(transactions);
        response.getWriter().write(json);
    }
}
