package com.iot_backend_rfid.security;

import com.iot_backend_rfid.exception.AppException;
import com.iot_backend_rfid.model.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JwtTokenUtils {
	@Value("${jwt.expiration}")
	private int EXPIRATION;

	@Value("${jwt.secretKey}")
	private String secretKey;
	public String generateToken(Member member) throws Exception {

		Map<String, Object> claims = new HashMap<>();
		claims.put("username", member.getUsername());
		try {
			String token = Jwts.builder()
					.setClaims(claims)
					.setSubject(member.getUsername())
					.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION * 1000L))
					.signWith(getSignInKey(), SignatureAlgorithm.HS256)
					.compact();
			return token;
		}
		catch (Exception e) {
			throw new AppException("Cannot create jwt token", HttpStatus.BAD_REQUEST);
		}
	}

	private Key getSignInKey() {
		byte[] bytes = Decoders.BASE64.decode(this.secretKey);
		return Keys.hmacShaKeyFor(bytes);
	}

	private Claims extractAllClaims(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(getSignInKey())
				.build()
				.parseClaimsJws(token)
				.getBody();
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = this.extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	public String extractUserName(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	//check expiration
	public boolean isTokenExpired(String token) {
		Date expirationDate = this.extractClaim(token, Claims::getExpiration);
		return expirationDate.before(new Date());
	}

	public boolean validateToken(String token, UserDetails userDetails) {
		String author = extractUserName(token);
		return (author.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}
}
