package com.democrud.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.democrud.entity.GenderData;

@Repository
public interface GenderRepository  extends JpaRepository<GenderData, Integer>{

	
	Optional<GenderData> findByGenderType(String genderType);


}
