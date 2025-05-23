package com.democrud.service;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.democrud.dto.UserLoginRequest;
import com.democrud.entity.UserInfo;
import com.democrud.helper.TokenGenerator;
import com.democrud.repository.UserRepository;
import com.democrud.util.JwtUtil;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private JwtUtil jwtUtil;


	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	public String registerUser(UserInfo users) {

		if (users.getUsername() == null || users.getUsername().trim().isEmpty()) {
			throw new IllegalArgumentException("Username cannot be null or empty for Register ");
		}

		if(users.getPassword()==null || users.getPassword().trim().isEmpty()) {
			throw new IllegalArgumentException("Password  cannot be null or empty for Register" );
		}


		if (users.getEmailId() == null || !users.getEmailId().matches("^(?!\\s*$)[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"))  {
			throw new IllegalArgumentException("Invalid Email Format for Register " );

		}

		if (userRepository.findByUsername(users.getUsername()).isPresent() &&
				userRepository.findByEmailId(users.getEmailId()).isPresent() && 
				!userRepository.findByRole(users.getRole()).isEmpty()) {

			return "Username already exists & Email ";

		}

		users.setPassword(passwordEncoder.encode(users.getPassword()));
		userRepository.save(users);
		return "User registered successfully" +users.getUsername();
	}


	public String loginUser(UserLoginRequest user) {



		if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
			throw new IllegalArgumentException("Username cannot be null or empty for login");
		}

		if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
			throw new IllegalArgumentException("Password cannot be null or empty for login");
		}

		Optional<UserInfo> existingUser = userRepository.findByUsername(user.getUsername());
		if (existingUser.isEmpty()) {
			throw new IllegalArgumentException("User not found");
		}

		if (!passwordEncoder.matches(user.getPassword(), existingUser.get().getPassword())) {
			throw new IllegalArgumentException("Invalid username or password");
		}

		// Generate JWT token
		//return jwtUtil.generateToken(existingUser.get().getUsername());
		return TokenGenerator.generateToken(existingUser.get()); // ✅ This is where you call it

	}




	//	public String loginUser(UserInfo user) {
	//		
	//		Optional<UserInfo> existingByUsername = userRepository.findByUsername(user.getUsername());
	//	    Optional<UserInfo> existingByEmail = userRepository.findByEmailId(user.getEmailId());
	//		
	//		Optional<UserInfo> existingUser = userRepository.findByUsername(user.getUsername());
	//
	//		if(user.getUsername()==null || user.getUsername().trim().isEmpty() || user.getUsername().equals(existingUser)) {
	//
	//			throw new IllegalArgumentException("Username cannot be null or empty  for login");
	//		}
	//		
	//
	//		 if (existingByUsername.isPresent() || existingByEmail.isPresent()){
	//		        throw new IllegalArgumentException("Username or Email ID already exists");
	//		    }
	//		
	//		
	//		if(user.getPassword()==null || user.getPassword().trim().isEmpty()) {
	//
	//			throw new IllegalArgumentException("Password cannot be null or empty for login");
	//		}
	//
	//		if (user.getEmailId() == null || !user.getEmailId().matches("^(?!\\s*$)[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"))  {
	//			throw new IllegalArgumentException("Invalid Email Format for login " );
	//
	//		}
	//
	//		if (existingUser.isPresent() && existingUser.get().getPassword().equals(user.getPassword())) {
	//			return jwtUtil.generateToken(user.getUsername());
	//		}
	//		return "Invalid username or password for login ";
	//	}
}












//public String loginUser(Users users) {
//        Optional<Users> existingUser = userRepository.findByUsername(users.getUsername());
//
//        if (existingUser.isPresent()) {
//            if (existingUser.get().getPassword().equals(users.getPassword())) {
//                return "Login successful";
//            } else {
//                return "Incorrect password";
//            }
//        } else {
//            return "User not found";
//        }
//    }
