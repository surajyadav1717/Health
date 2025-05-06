package com.democrud.helper;

import java.security.Key;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

import javax.naming.AuthenticationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.democrud.entity.UserInfo;
import com.democrud.repository.UserRepository;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class TokenGenerator {


	@Autowired
	private  UserRepository userRepository;

	@Autowired
	private Auth auth;

	private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256); // HMAC-SHA256

//	public static String generateToken(String username, Integer userId) {
	
	public static String generateToken(UserInfo userInfo) {
		
		Map<String, Object> claims  = new HashMap<>();
		claims.put("userId", userInfo.getUserId());
		claims.put("username", userInfo.getUsername());
		claims.put("password", userInfo.getPassword());
		
		
		
		long nowMillis = System.currentTimeMillis();
		long expMillis = nowMillis + 3600000; // Token valid for 1 hour
		Date now = new Date(nowMillis);
		Date expiry = new Date(expMillis);
		
		

		return Jwts.builder()
				.setSubject(userInfo.getUsername())
				.setClaims(claims) // maps 
				.setIssuedAt(now)
				.setExpiration(expiry)
				.signWith(key)
				.compact();
	}

	public String authenticateAndGenerateToken(String username, String password , Integer userId) throws AuthenticationException {
		if (auth.authenticateUser(username, password) != null) {
			return generateToken(username, userId);
		} else {
			throw new AuthenticationException("Invalid username or password");
		}
	}

	//maps
	public static String generateToken(String username, Integer userId) {
		// TODO Auto-generated method stub
		return null;
	}

	//	
	//bearer 
	public static String extractUsername(String token) {
		return Jwts.parserBuilder().setSigningKey(key).build()
				.parseClaimsJws(token).getBody().getSubject();
	}

	public boolean isTokenValid(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
			return true;
		} catch (JwtException e) {
			return false;
		}

	}
}

