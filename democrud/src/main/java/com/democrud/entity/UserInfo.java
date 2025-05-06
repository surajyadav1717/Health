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

		public UserInfo(Integer userId, String username, String password) {
			super();
			this.userId = userId;
			this.username = username;
			this.password = password;
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

		public UserInfo get() {
			
			return null;
		}

		public boolean isPresent() {
			
			return false;
		}
		
		
		
		

		
}