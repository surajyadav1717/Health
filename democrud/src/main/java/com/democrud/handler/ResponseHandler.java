package com.democrud.handler;

public class ResponseHandler {

	private boolean status ;

	private String message;

	private Object data;

	private long totalRecord;

	private long SucessData;

	private long errorFields;

	private long errorRecord;

//	private Integer userId;
//	
//	private String username;
//	
//	private String emailId;
//	
//	private String role;
//

//	public String getEmailId() {
//		return emailId;
//	}
//
//	public void setEmailId(String emailId) {
//		this.emailId = emailId;
//	}
//
//	public String getRole() {
//		return role;
//	}
//
//	public void setRole(String role) {
//		this.role = role;
//	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public long getTotalRecord() {
		return totalRecord;
	}

	public void setTotalRecord(long totalRecord) {
		this.totalRecord = totalRecord;
	}

	public long getSucessData() {
		return SucessData;
	}

	public void setSucessData(long sucessData) {
		SucessData = sucessData;
	}

	public long getErrorFields() {
		return errorFields;
	}

	public void setErrorFields(long errorFields) {
		this.errorFields = errorFields;
	}




	public long getErrorRecord() {
		return errorRecord;
	}

	public void setErrorRecord(long errorRecord) {
		this.errorRecord = errorRecord;
	}



	public ResponseHandler(boolean status, String message, Object data, long totalRecord, long sucessData,
			long errorFields, long errorRecord) {
		super();
		this.status = status;
		this.message = message;
		this.data = data;
		this.totalRecord = totalRecord;
		SucessData = sucessData;
		this.errorFields = errorFields;
		this.errorRecord = errorRecord;
	}

	public ResponseHandler() {
		// TODO Auto-generated constructor stub
	}

//	public Integer getUserId() {
//		return userId;
//	}
//
//	public void setUserId(Integer userId) {
//		this.userId = userId;
//	}
//
//	public String getUsername() {
//		return username;
//	}
//
//	public void setUsername(String username) {
//		this.username = username;
//	}




}


