package com.democrud.entity;

public class SearchFilter {

	private String fullName;
	private String emailId;
	private String town;
    private String status;
    
    
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	
	
	
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public String getTown() {
		return town;
	}
	public void setTown(String town) {
		this.town = town;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public SearchFilter(String fullName, String emailid, Integer pincode, String town, String status) {
		super();
		this.fullName = fullName;
		this.emailId = emailid;
		
		this.town = town;
		this.status = status;
	} 
	
    
    
	
}

