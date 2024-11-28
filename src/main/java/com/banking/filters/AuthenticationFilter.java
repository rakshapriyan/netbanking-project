package com.banking.filters;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.banking.entity.Customer;
import com.banking.entity.Employee;
import com.banking.entity.User;
import com.banking.service.AuthService;
import com.banking.util.ThreadLocale;

public class AuthenticationFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        System.out.println("In Authentication filter");
        String path = httpRequest.getRequestURI();
        System.out.println(path + httpRequest.getHeader("Authorization"));
        String token = httpRequest.getHeader("Authorization");

        // Allow login requests
        if (path.equals("/Netbanking1/login")) {
            System.out.println("Path contains login");
            chain.doFilter(request, response);
            return;
        }

        System.out.println("Path does not contain login");
        // For other requests, check for a token in the Authorization header
        if (token != null) {
            System.out.println(token);
            System.out.println("Contains token");

            try {
                // Parse the token
                String[] tokenParts = token.split(":");
                long customerId = Long.parseLong(tokenParts[0]);
                String role = tokenParts[2];

                // Fetch user data based on role
                AuthService authService = new AuthService();
                if ("customer".equalsIgnoreCase(role)) {
                    Customer customer = authService.getUserById(customerId);
                    System.out.println("Customer: " + customer);
                    ThreadLocale.setUser((User)customer);
                } else if ("employee".equalsIgnoreCase(role) || "manager".equalsIgnoreCase(role)) {
                    Employee employee = authService.getEmployeeById(customerId);
                    
                    System.out.println("Employee: " + employee);
                    ThreadLocale.setUser((User)employee);
                } else {
                    System.out.println("Invalid role in token: " + role);
                    httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }

                chain.doFilter(request, response);
            } catch (Exception e) {
                System.err.println("Error processing token: " + e.getMessage());
                httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        } else {
            System.out.println("Token not found");
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    @Override
    public void destroy() {
    }
    
    
    public void printRequest(HttpServletRequest request) {
    	Enumeration<String> parameterNames = request.getParameterNames();
    	System.out.println("Parameters:");
    	while (parameterNames.hasMoreElements()) {
    	    String paramName = parameterNames.nextElement();
    	    System.out.println(paramName + ": " + request.getParameter(paramName));
    	}
    	
    	Enumeration<String> attributeNames = request.getAttributeNames();
    	System.out.println("Attributes:");
    	while (attributeNames.hasMoreElements()) {
    	    String attributeName = attributeNames.nextElement();
    	    System.out.println(attributeName + ": " + request.getAttribute(attributeName));
    	}

    }
}
