package com.democrud.dto;

import java.util.List;

import com.democrud.entity.PersonalDetails;

public class PersonalDetailsWithUserDto {
	private Integer userId;
	private String username;
	private String email;
	private String role;
	private List<PersonalDetails> personalDetails;


	

	
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public List<PersonalDetails> getPersonalDetails() {
		return personalDetails;
	}
	public void setPersonalDetails(List<PersonalDetails> personalDetails) {
		this.personalDetails = personalDetails;
	}
	public PersonalDetailsWithUserDto() {
		super();
		// TODO Auto-generated constructor stub
	}
	public PersonalDetailsWithUserDto(Integer userId, String username, String email, String role,
			List<PersonalDetails> personalDetails) {
		super();
		this.userId = userId;
		this.username = username;
		this.email = email;
		this.role = role;
		this.personalDetails = personalDetails;
	}


}