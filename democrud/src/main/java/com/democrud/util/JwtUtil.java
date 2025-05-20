package com.democrud.util;

import java.sql.Date;


import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
@Component
public class JwtUtil {


	private static final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

	public String generateToken(String username) {
		long expirationTimeMs = 1000 * 60 * 60 * 24; // 24 hour
		//  long expirationTimeMs = 1000 * 60; // 1 minute

		Date now = new Date(expirationTimeMs);
		Date expiryDate = new Date(now.getTime() + expirationTimeMs);


		return Jwts.builder()
				.setSubject(username)
				.setIssuedAt(now)
				.setExpiration(expiryDate)
				.signWith(SECRET_KEY)
				.compact();
	}








	//
	//		public static boolean isTokenValid(String token) {
	//		
	//			return false;
	//		}
	//
	//		public static String extractUsername(String token) {
	//			return null;
	//		}
	//	





	//	 private static final String SECRET = "mysecretkeymysecretkeymysecretkey1234";
	//	 
	//	 
	//	 
	//	public String generateToken(String username) {
	//		return Jwts.builder()
	//				.setSubject(username)
	//				.setIssuedAt(new Date(System.currentTimeMillis()))
	//				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
	//				.signWith(Keys.hmacShaKeyFor(SECRET.getBytes()), SignatureAlgorithm.HS512)
	//				.compact();
	//	}
	//
	//	public String extractUsername(String token) {
	//	    Claims claims = Jwts.parserBuilder()
	//	                        .setSigningKey(SECRET)
	//	                        .build()
	//	                        .parseClaimsJws(token)
	//	                        .getBody();
	//	    return claims.getSubject();
	//	}
	//	
	//	
	//	public boolean validateToken(String token, UserDetails userDetails) {
	//		String username = extractUsername(token);
	//		return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
	//	}
	//
	//	private boolean isTokenExpired(String token) {
	//		return Jwts.parser()
	//				.setSigningKey(SECRET)
	//				.parseClaimsJws(token)
	//				.getBody()
	//				.getExpiration()
	//				.before(new Date(0));
	//	}
	//


}
