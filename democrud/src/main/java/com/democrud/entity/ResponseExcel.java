package com.democrud.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "response_excel_data")
public class ResponseExcel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "response_id")
	private Integer responseId ;

	@Column(name = "status")
	private Boolean status ;
	
	@Column(name="success")
	private String success;

	@Column(name = "error_fields")
	private String errorFields;
	
	@Column(name = "error")
	private String error;
	
	@Column(name = "queue_id")
	private Long queueId;
	
	

	public Integer getResponseId() {
		return responseId;
	}

	public void setResponseId(Integer responseId) {
		this.responseId = responseId;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public String getSuccess() {
		return success;
	}

	public void setSuccess(String success) {
		this.success = success;
	}

	public String getErrorFields() {
		return errorFields;
	}

	public void setErrorFields(String errorFields) {
		this.errorFields = errorFields;
	}
	

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public Long getQueueId() {
		return queueId;
	}

	public void setQueueId(Long queueId2) {
		this.queueId = queueId2;
	}

	public ResponseExcel(Integer responseId, Boolean status, String success, String errorFields, String error,
			Long queueId) {
		super();
		this.responseId = responseId;
		this.status = status;
		this.success = success;
		this.errorFields = errorFields;
		this.error = error;
		this.queueId = queueId;
	}

	public ResponseExcel() {
		super();
		// TODO Auto-generated constructor stub
	}


	
	

	
	
}
