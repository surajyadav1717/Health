package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "student_details")
public class Student {


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "student_id")
	private Integer studentId;

	@Column(name = "student_first_name")
	private String studentFirstName;

	@Column(name = "student_last_name")
	private String studentLastName;

	@Column(name = "email_id")
	private String emailId;

	@Column(name = "contact_number")
	private Long contactNumber;

	@Column(name = "address")
	private String address;

	@Column(name = "alternate_address")
	private String alternateAdress;

	@Column(name = "area")
	private String area;

	@Column(name = "city")
	private String city;

	@Column(name = "state")
	private String state;

	@Column(name = "pincode")
	private Long pincode;

	public Integer getStudentId() {
		return studentId;
	}

	public void setStudentId(Integer studentId) {
		this.studentId = studentId;
	}

	public String getStudentFirstName() {
		return studentFirstName;
	}

	public void setStudentFirstName(String studentFirstName) {
		this.studentFirstName = studentFirstName;
	}

	public String getStudentLastName() {
		return studentLastName;
	}

	public void setStudentLastName(String studentLastName) {
		this.studentLastName = studentLastName;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public Long getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(Long contactNumber) {
		this.contactNumber = contactNumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAlternateAdress() {
		return alternateAdress;
	}

	public void setAlternateAdress(String alternateAdress) {
		this.alternateAdress = alternateAdress;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Long getPincode() {
		return pincode;
	}

	public void setPincode(Long pincode) {
		this.pincode = pincode;
	}

	public Student(Integer studentId, String studentFirstName, String studentLastName, String emailId,
			Long contactNumber, String address, String alternateAdress, String area, String city, String state,
			Long pincode) {
		super();
		this.studentId = studentId;
		this.studentFirstName = studentFirstName;
		this.studentLastName = studentLastName;
		this.emailId = emailId;
		this.contactNumber = contactNumber;
		this.address = address;
		this.alternateAdress = alternateAdress;
		this.area = area;
		this.city = city;
		this.state = state;
		this.pincode = pincode;
	}

	public Student() {
		super();
		// TODO Auto-generated constructor stub
	}








}
