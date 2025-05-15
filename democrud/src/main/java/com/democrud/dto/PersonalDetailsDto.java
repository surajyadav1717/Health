package com.democrud.dto;
//dto me hamesa request hoga simple 


public class PersonalDetailsDto {
	

	private Integer id ; // not present 
	private String fullName;
	private String gender;
	private String dateofBirth;
	private String profession;
	private String ocupation;
	private String maritalStatus;
	private String emailId;
	private String contactDetails;
	private String address ;
	private String city;
	private String addharCard;
	private String pinCode;
	private String alternateAddress;
	private String pancardNo;
	private String area;
	private String town;
	private String state;
	private Character status;
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getDateofBirth() {
		return dateofBirth;
	}
	public void setDateofBirth(String dateofBirth) {
		this.dateofBirth = dateofBirth;
	}
	public String getProfession() {
		return profession;
	}
	public void setProfession(String profession) {
		this.profession = profession;
	}
	public String getOcupation() {
		return ocupation;
	}
	public void setOcupation(String ocupation) {
		this.ocupation = ocupation;
	}
	public String getMaritalStatus() {
		return maritalStatus;
	}
	public void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public String getContactDetails() {
		return contactDetails;
	}
	public void setContactDetails(String contactDetails) {
		this.contactDetails = contactDetails;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getAddharCard() {
		return addharCard;
	}
	public void setAddharCard(String addharCard) {
		this.addharCard = addharCard;
	}
	public String getPinCode() {
		return pinCode;
	}
	public void setPinCode(String pinCode) {
		this.pinCode = pinCode;
	}
	public String getAlternateAddress() {
		return alternateAddress;
	}
	public void setAlternateAddress(String alternateAddress) {
		this.alternateAddress = alternateAddress;
	}
	public String getPancardNo() {
		return pancardNo;
	}
	public void setPancardNo(String pancardNo) {
		this.pancardNo = pancardNo;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getTown() {
		return town;
	}
	public void setTown(String town) {
		this.town = town;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	
	
	public Character getStatus() {
		return status;
	}
	public void setStatus(char c) {
		this.status = c;
	}
	
	
	
	public PersonalDetailsDto(Integer id, String fullName, String gender, String dateofBirth, String profession,
			String ocupation, String maritalStatus, String emailId, String contactDetails, String address, String city,
			String addharCard, String pinCode, String alternateAddress, String pancardNo, String area, String town,
			String state, Character status) {
		super();
		this.id = id;
		this.fullName = fullName;
		this.gender = gender;
		this.dateofBirth = dateofBirth;
		this.profession = profession;
		this.ocupation = ocupation;
		this.maritalStatus = maritalStatus;
		this.emailId = emailId;
		this.contactDetails = contactDetails;
		this.address = address;
		this.city = city;
		this.addharCard = addharCard;
		this.pinCode = pinCode;
		this.alternateAddress = alternateAddress;
		this.pancardNo = pancardNo;
		this.area = area;
		this.town = town;
		this.state = state;
		this.status=status;
	}
	public PersonalDetailsDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	
	

}
