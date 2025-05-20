//package com.democrud.dto;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.democrud.entity.UserInfo;
//import com.democrud.service.UserService;
//
//@RestController
//@RequestMapping("/auth")
//public class UserController {
//
//
//
//	@Autowired
//	private UserService userService;
//
//	
//	@PostMapping("register")
//	public ResponseEntity<String> register(@RequestBody UserInfo info) {
//		return ResponseEntity.ok(userService.registerUser(info));
//	}
//
//	@PostMapping("login")
//	public ResponseEntity<String> login(@RequestBody UserInfo user) {
//		return ResponseEntity.ok(userService.loginUser(user));
//	}
//
//
//}



