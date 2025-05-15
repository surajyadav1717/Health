package com.democrud.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "queue_tracker")
public class Queue {


	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    private Integer totalRecords;

	    private Integer rowsRead;

	    private String status; 

	    private String filePath;
	    
	    
	    private LocalDateTime runAt = LocalDateTime.now();

		public Long getId() {
			
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public Integer getTotalRecords() {
			return totalRecords;
		}

		public void setTotalRecords(Integer totalRecords) {
			this.totalRecords = totalRecords;
		}

		public Integer getRowsRead() {
			return rowsRead;
		}

		public void setRowsRead(Integer rowsRead) {
			this.rowsRead = rowsRead;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public LocalDateTime getRunAt() {
			return runAt;
		}

		public void setRunAt(LocalDateTime runAt) {
			this.runAt = runAt;
		}
		
		public String getFilePath() {
			return filePath;
		}

		public void setFilePath(String filePath) {
			this.filePath = filePath;
		}

		public void save(Queue queue) {
		
			
		}

		
	  
}
