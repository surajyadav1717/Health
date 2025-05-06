package com.democrud.service;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.democrud.entity.PaginationList;
import com.democrud.entity.PersonalDetails;
import com.democrud.entity.PersonalDetailsDto;

@Service
public interface ProposalService {

	//Wheather App API Key
	//5227e82b36d2253b4f3a20f7e722f86e

	//Request url api of wheather api 
	//https://api.weatherstack.com/current?access_key=5227e82b36d2253b4f3a20f7e722f86e&query=New%20York

	//validation method
	PersonalDetails  saveProposal(PersonalDetailsDto personalDetailsDto);  //add update me hamesa dto ke method se call hoga 

	public List<PersonalDetails> getAllPersonalDetails();


	PersonalDetails updateProposal(Integer id, PersonalDetailsDto updatedDetailsDto); //add update me hamesa dto ke method se call hoga 



	public PersonalDetails deleteById(Integer id);

	List<PersonalDetails> getPaginationList(PaginationList paginationList);

	public int totalRecords();

	public long totalData();


	public  long getErrorCount();

	List<PersonalDetails> importPersonalDetailsFromExcel(MultipartFile multipartFile ) throws IOException;


	public void processPendingQueues() throws IOException;

}


