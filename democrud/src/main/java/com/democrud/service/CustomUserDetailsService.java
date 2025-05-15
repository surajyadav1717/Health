package com.democrud.service;

//import java.util.Collection;
//import java.util.Collections;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//
//import com.democrud.entity.User;
//import com.democrud.repository.UserRepository;
//
//public class CustomUserDetailsService  implements  UserDetailsService{
//
//	@Autowired
//	private UserRepository userRepository;
//
//
//
//	@Override
//	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//		User user = userRepository.findByName(username)
//				.orElseThrow(() -> new UsernameNotFoundException("User not found"));
//		return new org.springframework.security.core.userdetails.User(
//				user.getName(),
//				user.getPassword(),
//				(Collection<? extends GrantedAuthority>) Collections.singleton(new SimpleGrantedAuthority(user.getRole())));
//	}
//}

