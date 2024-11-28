package com.banking.filters;


import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CORSFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization logic if needed
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
    	System.out.println("In CORS filter");
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        // Set CORS headers for all incoming requests
        httpResponse.setHeader("Access-Control-Allow-Origin", "http://127.0.0.1:5500");  // Allow all origins or specify multiple origins
        httpResponse.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");  // Allow HTTP methods
        httpResponse.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization, X-Requested-With");  // Allow specific headers
        httpResponse.setHeader("Access-Control-Allow-Credentials", "true");  // Allow credentials (cookies, etc.)
        
        response.setContentType("application/json");

        // Handle preflight requests (OPTIONS)
        if ("OPTIONS".equalsIgnoreCase(httpRequest.getMethod())) {
            httpResponse.setStatus(HttpServletResponse.SC_OK);
            return; 
        }

        chain.doFilter(request, response); 
    }

    @Override
    public void destroy() {
        // Cleanup logic if needed
    }
}
