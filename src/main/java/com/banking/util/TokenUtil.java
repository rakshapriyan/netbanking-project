package com.banking.util;

import java.util.Base64;

public class TokenUtil {
	 
	    public static String generateToken(String username) {
	        return Base64.getEncoder().encodeToString(username.getBytes());
	    }

	    // Validate a token by decoding it
	    public static String validateToken(String token) {
	        try {
	            return new String(Base64.getDecoder().decode(token));
	        } catch (IllegalArgumentException e) {
	            return null; 
	        }
	    }
	


}
