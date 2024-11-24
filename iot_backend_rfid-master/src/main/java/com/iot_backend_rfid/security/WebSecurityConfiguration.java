package com.iot_backend_rfid.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebSecurity(debug = true)
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebMvc
@RequiredArgsConstructor
public class WebSecurityConfiguration {
	private final JwtTokenFilter jwtTokenFilter;
	private final JwtAuthEntryPoint jwtAuthEntryPoint;

	private static final String[] PUBLIC_URLS = {
			"/v3/api-docs", "/v3/api-docs/**", "/api-docs", "/api-docs/**",
			"/swagger-resources", "/swagger-resources/**", "/configuration/ui",
			"/configuration/security", "/swagger-ui/**", "/swagger-ui.html",
			"/webjars/swagger-ui/**", "/swagger-ui/index.html", "/**"
	};

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
				.csrf(AbstractHttpConfigurer::disable)
				.exceptionHandling(
						exception -> exception.authenticationEntryPoint(jwtAuthEntryPoint))
				.authorizeHttpRequests(auth -> auth
						.requestMatchers(PUBLIC_URLS).permitAll()
						.anyRequest()
						.authenticated());
		http.addFilterBefore(this.jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}
}
