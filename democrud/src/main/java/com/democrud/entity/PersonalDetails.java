package com.democrud.entity;

import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.democrud.enums.Status;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "personal_details")
public class PersonalDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer personalId ;


	@CreationTimestamp 
	@Column(updatable = false)  
	private LocalDateTime createdAt;

	@UpdateTimestamp 
	private LocalDateTime updatedAt;

	@Column(name ="fullname")
	private String fullName;

	@Column(name ="gender")
	private String gender; 

	@Column(name ="dateofbirth")
	private String dateofBirth;

	@Column(name = "profession")
	private String profession;

	@Column(name = "occupation")
	private String ocupation;

	@Column(name ="maritalstatus")
	private String maritalStatus;


	@Column(name="emailid")
	private String emailId;

	@Column(name = "contactdetails")
	private String contactDetails;

	@Column(name = "address")
	private String address ;

	@Column(name = "alternateaddress")
	private String alternateAddress;

	@Column(name = "city")
	private String city;

	@Column(name = "area")
	private String area;

	@Column(name ="town")
	private String town;

	@Column(name = "state")
	private String state;

	@Column(name = "pincode")
	private String pincode;

	@Column(name = "addhar_card")
	private String addharCard;

	@Column(name = "pancardno")
	private String pancardNo;

	@Column(name = "status")
	private Character status;

//	@Column(name = "status")
//	private Status status;
	

	@Column(name = "genderId)")
	private Integer genderId1;


	public Integer getPersonalId() {
		return personalId;
	}

	public void setPersonalId(Integer personalId) {
		this.personalId = personalId;
	}


	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
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

	public String getAlternateAddress() {
		return alternateAddress;
	}

	public void setAlternateAddress(String alternateAddress) {
		this.alternateAddress = alternateAddress;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
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

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public String getAddharCard() {
		return addharCard;
	}

	public void setAddharCard(String addharCard) {
		this.addharCard = addharCard;
	}

	public String getPancardNo() {
		return pancardNo;
	}

	public void setPancardNo(String pancardNo) {
		this.pancardNo = pancardNo;
	}

	

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Integer getGenderId1() {
		return genderId1;
	}

	public void setGenderId1(Integer genderId1) {
		this.genderId1 = genderId1;
	}

	
	public PersonalDetails(Integer personalId, LocalDateTime createdAt, LocalDateTime updatedAt, String fullName,
			String gender, String dateofBirth, String profession, String ocupation, String maritalStatus,
			String emailId, String contactDetails, String address, String alternateAddress, String city, String area,
			String town, String state, String pincode, String addharCard, String pancardNo, Character status,
			Integer genderId1) {
		super();
		this.personalId = personalId;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.fullName = fullName;
		this.gender = gender;
		this.dateofBirth = dateofBirth;
		this.profession = profession;
		this.ocupation = ocupation;
		this.maritalStatus = maritalStatus;
		this.emailId = emailId;
		this.contactDetails = contactDetails;
		this.address = address;
		this.alternateAddress = alternateAddress;
		this.city = city;
		this.area = area;
		this.town = town;
		this.state = state;
		this.pincode = pincode;
		this.addharCard = addharCard;
		this.pancardNo = pancardNo;
		this.status = status;
		this.genderId1 = genderId1;
	}

	public PersonalDetails() {
		super();
		// TODO Auto-generated constructor stub
	}



	//	@OneToOne
	//	@JoinColumn(name = "fk_gender_id")
	//	private GenderData genderData;





}






