package com.iot_backend_rfid.security;

import com.iot_backend_rfid.model.Member;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor

public class JwtTokenFilter extends OncePerRequestFilter {

	private final UserDetailsService userDetailsService;
	private final JwtTokenUtils jwtTokenUtil;

	private static final Logger logger= LoggerFactory.getLogger(JwtTokenFilter.class);
	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request,
			@NonNull HttpServletResponse response,
			@NonNull FilterChain filterChain)
			throws ExpiredJwtException, IOException, ServletException {

		try{
			String jwt = parseJwt(request);
			if(jwt!=null){
				String userName = this.jwtTokenUtil.extractUserName(jwt);
				Member member = (Member) this.userDetailsService.loadUserByUsername(userName);
				if (jwtTokenUtil.validateToken(jwt, member)){
					var authentication =
							new UsernamePasswordAuthenticationToken(member, null, member.getAuthorities());
					authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authentication);
				}
			}

		}catch (Exception e){
			logger.error("Cannot set user authentication : {} ", e.getMessage());
		}
		filterChain.doFilter(request, response);

	}

	private String parseJwt(HttpServletRequest request) {
		String headerAuth=
				(request.getHeader("Authorization")==null || request.getHeader("Authorization").isEmpty())
						? request.getParameter("token") : request.getHeader("Authorization");
		if(!StringUtils.hasText(headerAuth)){
			return null;
		}
		if(headerAuth.startsWith("Bearer ")){
			return headerAuth.substring(7);
		}
		return headerAuth;
	}

}
