package com.democrud.helper;

import java.security.Key;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

import javax.naming.AuthenticationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.democrud.dto.UserLoginRequest;
import com.democrud.entity.UserInfo;
import com.democrud.repository.UserRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class TokenGenerator {


	private static final Object Object = null;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private Auth auth;

	@Value("${jwt.secret}")
	private String secretKeyString;

	private static Key key;

	@PostConstruct 
	public void init() {
		TokenGenerator.key = Keys.hmacShaKeyFor(secretKeyString.getBytes());
	}


	public static String generateToken(UserInfo userInfo) {

		Map<String, Object> claims = new HashMap<>();
		claims.put("userId", userInfo.getUserId());
		///	claims.put("username", userInfo.getUsername());
		claims.put("emailId", userInfo.getEmailId());
		claims.put("role", userInfo.getRole());   

		//claims.put("password", userInfo.getPassword());

		long nowMillis = System.currentTimeMillis();
		long expMillis = nowMillis +  1000 * 60 * 60 * 24; // 24 hrs 
		//  long expirationTimeMs = 1000 * 60 * 60; // 24 hour

		Date now = new Date(nowMillis);
		Date expiry = new Date(expMillis);
		 return Jwts.builder()
	                .setClaims(claims)
	                .setSubject(userInfo.getUsername())  // ✅ username
	                .setIssuedAt(now)
	                .setExpiration(expiry)
	                .signWith(key)
	                .compact();
	    }
	
	 public String authenticateAndGenerateToken(String username, String password, Integer userId)
	            throws AuthenticationException {
	        UserInfo userInfo = auth.authenticateUser(username, password);
	        if (userInfo != null) {
	            return generateToken(userInfo);  // ✅ Fixed line
	        } else {
	            throw new AuthenticationException("Invalid username or password");
	        }
	    }

//	public String authenticateAndGenerateToken(String username, String password, Integer userId)
//			throws AuthenticationException {
//		if (auth.authenticateUser(username, password) != null) {
//			return generateToken(username, userId);
//		} else {
//			throw new AuthenticationException("Invalid username or password");
//		}
//	}


//	private String generateToken(String username, Integer userId) {
//		return null;
//	}


	public static Claims extractAllClaims(String token) {
		  Claims claims = Jwts.parserBuilder()
		            .setSigningKey(key)
		            .build()
		            .parseClaimsJws(token)
		            .getBody();
	   // System.out.println("Extracted Claims: " + claims);  // <== ADD THIS DEBUG
		return claims;

	}


	public static Integer extractUserId(String token) {
		return extractAllClaims(token).get("userId", Integer.class);

	}

	public static String extractUsername(String token) {
		return extractAllClaims(token).getSubject();
	}


	public static String extractEmailId(String token) {
		return extractAllClaims(token).get("emailId", String.class);

	}

	public static String extractRole(String token) {
		return extractAllClaims(token).get("role", String.class);

	}



	//
	//	public static String extractAllClaims (Integer userId , String username , String email, String role ,String token) {
	//
	//		Integer userId1 = extractUserId(token);
	//		String  username1 =extractUsername(token);
	//		String email1 = extractEmailId(token);
	//		String role1 = extractRole(token);
	//
	//		return extractAllClaims(userId1, username1, email1, role1, token);
	//	}






	public boolean isTokenValid(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
			return true;
		} catch (JwtException e) {
			return false;
		}
	}


	public static String extractTokenFromRequest(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}


	

	public static String extractClaim(String token, Object object2) {
		// TODO Auto-generated method stub
		return null;
	}
}





































//Generates token from username and userId (optional use)
//	public String generateToken(String username, Integer userId) {
//		Map<String, Object> claims = new HashMap<>();
//		claims.put("userId", userId);
//		claims.put("username", username);
//
//		long nowMillis = System.currentTimeMillis();
//		long expMillis = nowMillis + 5 * 60 * 1000; // 5 minute = 60000 ms
//		Date now = new Date(nowMillis);
//		Date expiry = new Date(expMillis);
//
//		return Jwts.builder()
//				.setSubject(username)
//				.setClaims(claims)
//				.setIssuedAt(now)
//				.setExpiration(expiry)
//				.signWith(key)
//				.compact();
//	}


































//	@Autowired
//	private  UserRepository userRepository;
//
//	@Autowired
//	private Auth auth;
//
//	private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256); // HMAC-SHA256
//
////	public static String generateToken(String username, Integer userId) {
//	
//	public static String generateToken(UserInfo userInfo) {
//		
//		Map<String, Object> claims  = new HashMap<>();
//		claims.put("userId", userInfo.getUserId());
//		claims.put("username", userInfo.getUsername());
//		claims.put("password", userInfo.getPassword());
//		
//		
//		
//		long nowMillis = System.currentTimeMillis();
//		long expMillis = nowMillis + 3600000; // Token valid for 1 hour
//		Date now = new Date(nowMillis);
//		Date expiry = new Date(expMillis);
//		
//		
//
//		return Jwts.builder()
//				.setSubject(userInfo.getUsername())
//				.setClaims(claims) // maps 
//				.setIssuedAt(now)
//				.setExpiration(expiry)
//				.signWith(key)
//				.compact();
//	}
//
//	public String authenticateAndGenerateToken(String username, String password , Integer userId) throws AuthenticationException {
//		if (auth.authenticateUser(username, password) != null) {
//			return generateToken(username, userId);
//		} else {
//			throw new AuthenticationException("Invalid username or password");
//		}
//	}
//
//	//maps
//	public static String generateToken(String username, Integer userId) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	//	
//	//bearer 
//	public static String extractUsername(String token) {
//		return Jwts.parserBuilder().setSigningKey(key).build()
//				.parseClaimsJws(token).getBody().getSubject();
//	}
//
//	public boolean isTokenValid(String token) {
//		try {
//			Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
//			return true;
//		} catch (JwtException e) {
//			return false;
//		}
//
//	}
//}

