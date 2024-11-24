package com.iot_backend_rfid.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorsConfiguration extends OncePerRequestFilter {
	@Override

	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		response.setHeader("Access-Control-Allow-Origin", "http://localhost:4800");
		response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
		response.setHeader("Access-Control-Max-Age", "3600");
		response.setHeader("Access-Control-Allow-Headers", "orgId, Bearer, Authorization, Content-Type, X-XSRF-TOKEN"); // chi nhan ve
		response.addHeader("Access-Control-Expose-Headers", "XSRF-TOKEN, application/json, text/plain, */*"); // chi gui di
		response.setHeader("Access-Control-Allow-Credentials", "true");
		if ("OPTIONS".equals(request.getMethod())) {
			response.setStatus(HttpServletResponse.SC_OK);
		}
		else {
			filterChain.doFilter(request, response);
		}
	}
}
