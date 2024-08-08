package com.filters;
import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.services.JwtService;

public class FiltroAutenticacion implements Filter{

private static final JwtService jwtService = new JwtService();
	
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
        throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
            	
                if (cookie.getName().equals("jwt")) {
                	
                    try {
                        if(jwtService.isValidJwt(cookie.getValue())) {
                        	chain.doFilter(req, res);
                        } else {
                        	response.sendRedirect(request.getContextPath() + "/login.jsf");
                        }
                       return;
                       
                    } catch (Exception e) {
                    	
                        response.sendRedirect(request.getContextPath() + "/login.jsf");
                        e.printStackTrace();
                        return;
                    }
                }
            }
        }

        response.sendRedirect(request.getContextPath() + "/login.jsf");
    }
}
