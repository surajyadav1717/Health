package com.democrud.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.democrud.entity.ResponseExcel;

@Repository
public interface ResponseExcelRepository extends JpaRepository<ResponseExcel, Integer> {


	
}
