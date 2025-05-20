package com.democrud.config;

import java.io.IOException;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.democrud.helper.TokenGenerator;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {


	@Autowired
	@org.springframework.context.annotation.Lazy
	private TokenGenerator tokenGenerator;


	@Override
	protected void doFilterInternal(HttpServletRequest request, 
			HttpServletResponse response, 
			FilterChain filterChain)
					throws ServletException, IOException {


		final String authHeader = request.getHeader("Authorization");

		Claims username = null;
		String token = null;
		Integer userId=null;
		String emailid=null;
		String role=null;


		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			token = authHeader.substring(7);  
			try {
				username = TokenGenerator.extractAllClaims(token);
			} catch (ExpiredJwtException e) {
				System.out.println("Expired token error:--->> " + e.getMessage());
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); 
				response.setContentType("application/json");
				response.getWriter().write("{\"error\": \"Token has expired, please log in again\"}");
				return;
				
			} catch (JwtException e) {
				System.out.println("JWT parsing error:---->>> " + e.getMessage());
				response.setStatus(HttpServletResponse.SC_FORBIDDEN); 
				response.setContentType("application/json");
				response.getWriter().write("{\"error\": \"Invalid token\"}");
				return; 
			}
		}


		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
					username, null, Collections.emptyList());
			auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
			SecurityContextHolder.getContext().setAuthentication(auth);

		}

		filterChain.doFilter(request, response);
		}
	}





		//		String username = jwtUtil.extractUsername(token);
		//		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
		//		    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
		//
		//		    if (TokenGenerator.validateToken(token, userDetails)) {
		//		        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
		//		            userDetails, null, userDetails.getAuthorities());
		//
		//		        auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		//		        SecurityContextHolder.getContext().setAuthentication(auth);
		//		    }
		//		}



	








//	@Override
//	protected void doFilterInternal(HttpServletRequest request, 
//			HttpServletResponse response, 
//			FilterChain filterChain)
//					throws ServletException, IOException {
//
//		final String authHeader = request.getHeader("Authorization");
//
//		String username = null;
//		String token = null;
//
//		try {
//			if (authHeader != null && authHeader.startsWith("Bearer ")) {
//				token = authHeader.substring(7);
//				try {
//					username = TokenGenerator.extractUsername(token);
//				} catch (ExpiredJwtException  e) {
//					System.out.println("JWT parsing error: " + e.getMessage());
//					response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 Unauthorized
//					response.setContentType("application/json");
//					response.getWriter().write("{\"message\": \"Token has expired, please log in again\"}");
//					return; 
//
//				}catch (JwtException e) {
//					response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//					response.setContentType("application/json");
//					response.getWriter().write("{\"message\": \"Invalid token\"}");
//					return; 
//				}
//
//			}
//
//			if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//				UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
//						username, null, Collections.emptyList());
//
//				auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//				SecurityContextHolder.getContext().setAuthentication(auth);
//			}
//
//			filterChain.doFilter(request, response);
//
//		}
//
//	}
