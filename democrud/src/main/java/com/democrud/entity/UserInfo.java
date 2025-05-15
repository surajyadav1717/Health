package com.democrud.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_info")
public class UserInfo {
	

		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		@Column(name = "user_id")
		private Integer userId;

		@Column(name = "user_name")
		private String username;

		@Column(name = "password")
		private String password;
		
	
		@Column(name="email_id")
		private String emailId;
		
		@Column(name = "role")
		private String role;
		

		public UserInfo(Integer userId, String username, String password, String emailId , String role) {
			super();
			this.userId = userId;
			this.username = username;
			this.password = password;
			this.emailId=emailId;
			this.role=role;
		}

		public UserInfo() {
			super();
			// TODO Auto-generated constructor stub
		}

		public Integer getUserId() {
			return userId;
		}

		public void setUserId(Integer userId) {
			this.userId = userId;
		}

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}
		
		
		public String getEmailId() {
			return emailId;
		}

		public void setEmailId(String emailId) {
			this.emailId = emailId;
		}

		public String getRole() {
			return role;
		}

		public void setRole(String role) {
			this.role = role;
		}

		public UserInfo get() {
			
			return null;
		}

		public boolean isPresent() {
			
			return false;
		}
		
		
		
		

		
}