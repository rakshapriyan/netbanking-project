package com.banking.filters;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.spi.ServiceRegistry.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.banking.entity.User;

public class AuthorizationFilter implements Filter {
    private static final Map<String, List<String>> accessControlMap = new HashMap<>();

    static {
        accessControlMap.put("/servleta", Arrays.asList("CUSTOMER", "EMPLOYEE", "MANAGER"));
        accessControlMap.put("/servletb", Arrays.asList("EMPLOYEE", "MANAGER"));
        accessControlMap.put("/servletc", Arrays.asList("MANAGER"));
    }

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        String servletPath = request.getServletPath();

        // Check if user is logged in
        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // Check access permissions
        if (!isAuthorized(user, servletPath)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied");
            return;
        }

        chain.doFilter(req, res);
    }

    private boolean isAuthorized(User user, String servletPath) {
        List<String> allowedRoles = accessControlMap.get(servletPath);
        return allowedRoles != null && allowedRoles.contains(user.getRole());
    }

	@Override
	public boolean filter(Object provider) {
		// TODO Auto-generated method stub
		return false;
	}
}
