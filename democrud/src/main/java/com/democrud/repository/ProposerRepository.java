package com.democrud.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.democrud.entity.PersonalDetails;
import com.democrud.enums.Status;

@Repository
public interface ProposerRepository extends JpaRepository<PersonalDetails, Integer>{


	List<PersonalDetails> findByStatus(Status status); //back 

	List<PersonalDetails> findAllByStatus(Character c);  // repository me hamesa entity class ka value ayega

	boolean existsByEmailId(String emailId);

	Optional<PersonalDetails> findByPersonalIdAndStatus(Integer personalId, Character status);


}

