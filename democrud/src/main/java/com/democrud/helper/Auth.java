package com.democrud.helper;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.democrud.dto.UserLoginRequest;
import com.democrud.entity.UserInfo;
import com.democrud.repository.UserRepository;

@Component
public class Auth {


	@Autowired
	private  UserRepository userRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	public UserInfo authenticateUser(String username, String rawPassword) {
		Optional<UserInfo> userOpt = userRepository.findByUsername(username);

		//Optional<UserInfo> userOptEmail= userRepository.findByEmailId(rawPassword)

		if (userOpt.isPresent()) {
			UserInfo user = userOpt.get();
			if (passwordEncoder.matches(rawPassword, user.getPassword())) {
				return user;
			}
		}
		return null;
	}





























	public String loginUser(UserLoginRequest user) {
		// TODO Auto-generated method stub
		return null;
	}
}















//	public UserInfo authenticateUser(String username, String password) {
//	    Optional<UserInfo> user = userRepository.findByUsername(username);
//
//	    if (user.isPresent() && user.get().getPassword().equals(password)) {
//	        System.out.println("Generated JWT Token:");
//	        System.out.println("Username: " + username + ", Password: " + password);
//	        return user.get();  // Return the actual UserInfo object
//	    }
//
//	    return null;  // Return null if authentication fails
//	}


