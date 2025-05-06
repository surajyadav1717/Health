//package com.democrud.serviceimpl;
//
//import java.util.Optional;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//import com.democrud.entity.User;
//import com.democrud.repository.UserRepository;
//import com.democrud.service.UserService;
//
//public class UserServiceImpl implements UserService {
//
//
//	@Autowired
//	private UserRepository userRepository;
//
//	@Autowired
//	private PasswordEncoder passwordEncoder;
//
//	@Override
//	public User regiterUser(User user) {
//		user.setPassword(passwordEncoder.encode(user.getPassword()));
//		return userRepository.save(user);
//	}
//
//	@Override
//	public Optional<User> findByUsername(String username) {
//		return userRepository.findByUsername(username);
//	}
//
//	
//}
