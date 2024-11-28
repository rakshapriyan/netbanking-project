package com.banking.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class CachedBodyHttpServletRequestWrapper extends HttpServletRequestWrapper {

    
	private final String body;

    public CachedBodyHttpServletRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
        }
        this.body = stringBuilder.toString();
    }

    @Override
    public BufferedReader getReader() {
        return new BufferedReader(new StringReader(this.body));
    }

    public String getBody() {
        return this.body;
    }
}
