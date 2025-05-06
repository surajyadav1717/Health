package com.democrud.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.democrud.entity.Queue;

@Repository
public interface QueueRepository extends JpaRepository<Queue, Long> {

	public List<Queue>  findByStatus(String status);
	
	//List<Queue> findByStatusIn(String string);
}
