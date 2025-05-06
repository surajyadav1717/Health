package com.democrud.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.democrud.entity.PersonalDetails;

@Repository
public interface ProposerRepository extends JpaRepository<PersonalDetails, Integer>{
	
	
	List<PersonalDetails> findByStatus(String status); //back 

	List<PersonalDetails> findAllByStatus(char c);  // repository me hamesa entity class ka value ayega

	boolean existsByEmailId(String emailId);

	
}

