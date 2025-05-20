package com.democrud.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.mapping.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.democrud.dto.PersonalDetailsWithUserDto;
import com.democrud.dto.UserLoginRequest;
import com.democrud.entity.PaginationList;
import com.democrud.entity.PersonalDetails;
import com.democrud.entity.PersonalDetailsDto;
import com.democrud.entity.UserInfo;
import com.democrud.handler.ResponseHandler;
import com.democrud.helper.Auth;
import com.democrud.helper.TokenGenerator;
import com.democrud.repository.ProposerRepository;
import com.democrud.repository.QueueRepository;
import com.democrud.repository.UserRepository;
import com.democrud.service.ProposalService;
import com.democrud.service.UserService;
import com.democrud.serviceimpl.ProposalServiceImp;

import io.jsonwebtoken.lang.Collections;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletRequest;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/proposal")
public class ProposalController {


	@Autowired
	private  ProposerRepository proposerRepository;

	@Autowired
	private ProposalService proposalService;

	@Autowired
	private ProposalServiceImp proposalServiceImp;

	@Autowired
	private QueueRepository queueRepository;


	@Autowired
	private  UserRepository userRepository;

	@Autowired
	private Auth auth;

	@Autowired
	private TokenGenerator tokenGenerator;


	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private UserService userService;


	@PostMapping("/register")
	public ResponseHandler registerUser(@RequestBody UserInfo user) {
		ResponseHandler response = new ResponseHandler();

		try {
			String result  = userService.registerUser(user);
			response.setStatus(true);
			userRepository.save(user);
			response.setMessage("User registered successfully");
			response.setData(result); 

		} catch (IllegalArgumentException e) {
			response.setStatus(false);
			response.setMessage(e.getMessage());
		}
		catch (Exception e1) {
			response.setStatus(false);
			response.setData(new ArrayList<>());
			response.setMessage(e1.getMessage());
		}
		return response;
	}


	@PostMapping("/login")
	public ResponseHandler loginUser(@RequestBody UserLoginRequest user) {
		ResponseHandler response = new ResponseHandler();
		try {
			String token = userService.loginUser(user);
			//String token1 = TokenGenerator.generateToken(isAuthenticated.getUsername(), isAuthenticated.getUserId());
			response.setData(token);
			response.setMessage("Login successful");
			response.setStatus(true);
		} catch (IllegalArgumentException e) {
			response.setMessage(e.getMessage());
			response.setStatus(false);
		} catch (Exception e) {
			e.printStackTrace();
			response.setMessage("Internal server error");
			response.setStatus(false);
		}
		return response;
	}


	@GetMapping("/excel")
	public ResponseHandler download() {
		ResponseHandler responseHandler = new ResponseHandler();

		try {
			String filename = "PersonalDetails.xlsx";
			ByteArrayInputStream in = proposalServiceImp.getActualData();

			byte[] excelBytes = in.readAllBytes();
			String base64Excel = Base64.getEncoder().encodeToString(excelBytes);

			Map<String, String> responseMap = new HashMap<>();
			responseMap.put("fileName", filename);
			responseMap.put("fileContent", base64Excel);
			responseMap.put("contentType", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

			responseHandler.setStatus(true);
			responseHandler.setData(responseMap);
			responseHandler.setMessage("Excel file generated successfully.");

		} catch (IllegalArgumentException msg) {
			msg.printStackTrace();
			responseHandler.setStatus(false);
			responseHandler.setData(new ArrayList<>());
			responseHandler.setMessage("Failed to generate Excel file."+msg.getMessage());

		}catch (IOException e) {
			e.printStackTrace();
			responseHandler.setStatus(false);
			responseHandler.setData(new ArrayList<>());
			responseHandler.setMessage("File Not Found ");

		}

		return responseHandler;
	}

	@GetMapping("/export/excel")
	public ResponseHandler exportToExcel() throws IOException {
		ResponseHandler responseHandler = new ResponseHandler();

		try {
			String filename = "PersonalDetails.xlsx";
			ByteArrayInputStream in = proposalServiceImp.exportEmployeesToExcel();

			byte[] excelBytes = in.readAllBytes();
			String base64Excel = Base64.getEncoder().encodeToString(excelBytes);

			Map<String, String> data = new HashMap<>();
			data.put("fileName", filename);
			data.put("fileContent", base64Excel);
			data.put("contentType", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

			responseHandler.setStatus(true);
			responseHandler.setMessage("Excel file generated successfully.");
			responseHandler.setData(data);

		} catch (IllegalArgumentException ex) {
			responseHandler.setStatus(false);
			responseHandler.setMessage("IllegalArgumentException: " + ex.getMessage());
			responseHandler.setData(new ArrayList<>());
		} catch (Exception e) {
			responseHandler.setStatus(false);
			responseHandler.setMessage("Exception: " + e.getMessage());
			responseHandler.setData(new ArrayList<>());
		}

		return responseHandler;
	}



	@PostMapping(value ="/importPersonalData", consumes =MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseHandler importPersonalDetails(
			@Parameter(description = "Excel File uploaded ", required = true)
			@RequestParam("file") MultipartFile file
			) throws IOException {

		ResponseHandler responseHandler = new ResponseHandler();


		try {
			List<PersonalDetails> savedExcelList= proposalService.importPersonalDetailsFromExcel(file);

			responseHandler.setStatus(true);
			responseHandler.setData(savedExcelList);
			responseHandler.setTotalRecord(savedExcelList.size()); 																// total find karega 
			//responseHandler.setSucessData(proposalServiceImp.totalData()); //sucess data print karega 
			//responseHandler.setErrorRecord(proposalServiceImp.totalData()-proposalServiceImp.totalRecords()); ///error find karega 
			responseHandler.setMessage("Excel data Imported Succesfully..Success Record Is -> "+ savedExcelList.size()+ " And  Failed Record Is -> " + proposalServiceImp.getErrorCount());

		}catch (IllegalArgumentException e) {
			e.printStackTrace();
			responseHandler.setStatus(false);
			responseHandler.setMessage("Failed to import Excel data ");
			responseHandler.setData(new ArrayList<>());
		}
		return responseHandler;
	}


	@PostMapping("/add")
	public ResponseHandler createProposal(@RequestBody PersonalDetailsDto personalDetailsDto ,HttpServletRequest request) {

		ResponseHandler responseHandler = new ResponseHandler(); 															//object bana

		try {
			PersonalDetails saveProposal = proposalService.saveProposal(personalDetailsDto);
			responseHandler.setStatus(true);
			responseHandler.setData(saveProposal);
			responseHandler.setMessage("Person Details Saved Succesfully");

		}catch (IllegalArgumentException e) {
			e.printStackTrace();
			responseHandler.setStatus(false);
			responseHandler.setData(new ArrayList<>());
			responseHandler.setMessage(e.getMessage());
		}catch (Exception e) {
			e.printStackTrace();
			responseHandler.setStatus(false);
			responseHandler.setData(new ArrayList<>());
			responseHandler.setMessage(e.getMessage());
		}
		return responseHandler;

	}


	@GetMapping("/list")
	public ResponseHandler getAllList(HttpServletRequest request) {
	    ResponseHandler responseHandler = new ResponseHandler();
	    try {
	        PersonalDetailsWithUserDto userData = proposalService.getAllPersonalDetails(request);
	        responseHandler.setStatus(true);
	        responseHandler.setData(userData);
	        responseHandler.setMessage("Person Details List Fetch Successfully");
	       // responseHandler.setTotalRecord(proposalService.totalRecords());
	    } catch (IllegalArgumentException e) {
	        responseHandler.setStatus(false);
	        responseHandler.setData(null);
	        responseHandler.setMessage(e.getMessage());
	    } catch (Exception e) {
	        responseHandler.setStatus(false);
	        responseHandler.setData(null);
	        responseHandler.setMessage(e.getMessage());
	    }

	    return responseHandler;
	}
	
	
		@PutMapping("/update/{id}")
	public ResponseHandler updateProposal(@PathVariable Integer id, @RequestBody PersonalDetailsDto updatepersonalDetailsdto) {

		ResponseHandler responseHandler = new ResponseHandler(); //object bana

		try {
			PersonalDetails details3 = proposalService.updateProposal(id, updatepersonalDetailsdto);
			responseHandler.setStatus(true);
			responseHandler.setData(details3);
			responseHandler.setMessage("Person Details Updated Succesfully For Id-> "+id);

		}catch (IllegalArgumentException e) {
			e.printStackTrace();
			responseHandler.setStatus(false);
			responseHandler.setData(new ArrayList<>());
			responseHandler.setMessage(e.getMessage());
		}catch (Exception e) {
			e.printStackTrace();
			responseHandler.setStatus(false);
			responseHandler.setData(new ArrayList<>());
			responseHandler.setMessage("nop ");
		}
		return responseHandler;
		//PersonalDetails updatedDetails = proposalService.updateproposal(id, updatepersonalDetailsdto);

		//return new ResponseEntity<>(updatedDetails, HttpStatus.OK);
	}


	@DeleteMapping("/delete/{id}")
	public ResponseHandler deleteData(@PathVariable Integer id) {

		ResponseHandler responseHandler = new ResponseHandler(); 											//object bana

		try {
			PersonalDetails details1 = proposalService.deleteById(id);
			responseHandler.setStatus(true);
			responseHandler.setData(new ArrayList<>());
			responseHandler.setMessage("Person Details Deleted Succesfully for Id->"+id);

		}catch (IllegalArgumentException e) {
			e.printStackTrace();
			responseHandler.setStatus(false);
			responseHandler.setData(new ArrayList<>());
			responseHandler.setMessage(e.getMessage());
		}catch (Exception e) {
			e.printStackTrace();
			responseHandler.setStatus(false);
			responseHandler.setData(new ArrayList<>());
			responseHandler.setMessage(e.getMessage());
		}
		return responseHandler;
	}

	@PostMapping("/listing")
	public ResponseHandler getAllPaginatedList(@RequestBody PaginationList paginationList) {
		ResponseHandler responseHandler = new ResponseHandler();

		try {
			List<PersonalDetails> details = proposalService.getPaginationList(paginationList);
			responseHandler.setStatus(true);
			responseHandler.setData(details);  // Send the list, whether empty or not
			responseHandler.setMessage("Pagination List Retrieved");
			responseHandler.setTotalRecord(proposalService.totalRecords());

		}catch (IllegalArgumentException e) {
			e.printStackTrace();
			responseHandler.setStatus(false);
			responseHandler.setData(new ArrayList<>());
			responseHandler.setMessage("Error occurred during pagination: " + e.getMessage());

		} catch (Exception e) {
			e.printStackTrace();
			responseHandler.setStatus(false);
			responseHandler.setData(new ArrayList<>());
			responseHandler.setMessage("Error occurred during pagination: " + e.getMessage());
			responseHandler.setTotalRecord(proposalService.totalRecords());
		}

		return responseHandler;
	}


	@GetMapping("/personal-details/{id}")
	public ResponseHandler getPersonalDetailsById(@PathVariable Integer id) {
		ResponseHandler responseHandler = new ResponseHandler();

		try {
			PersonalDetailsDto dto = proposalService.getPersonalDetailsById(id); 
			responseHandler.setStatus(true);
			responseHandler.setData(dto);
			responseHandler.setMessage("Record found");

		} catch (IllegalArgumentException ex) {
			responseHandler.setStatus(false);
			responseHandler.setData(null);
			responseHandler.setMessage(ex.getMessage());
		} catch (Exception e) {
			responseHandler.setStatus(false);
			responseHandler.setData(null);
			responseHandler.setMessage("An error occurred");
		}

		return responseHandler;
	}

}





















//@GetMapping("/list")
//public ResponseHandler getAllList(HttpServletRequest request){
//
//	ResponseHandler responseHandler = new ResponseHandler(); //object bana
//
//	try {
//		List<PersonalDetails> details2 = proposalService.getAllPersonalDetails(request);
//		proposalService.totalRecords();
//		responseHandler.setStatus(true);
//		responseHandler.setData(details2);
//		responseHandler.setMessage("Person Details List Fetch Succesfully");
//		responseHandler.setTotalRecord(proposalService.totalRecords());
//
//	}catch (IllegalArgumentException e) {
//		e.printStackTrace();
//		responseHandler.setStatus(false);
//		responseHandler.setData(new ArrayList<>());
//		responseHandler.setMessage(e.getMessage());
//	}catch (Exception e) {
//		e.printStackTrace();
//		responseHandler.setStatus(false);
//		responseHandler.setData(new ArrayList<>());
//		responseHandler.setMessage(e.getMessage());
//	}
//
//	return responseHandler;
//	//	return proposalService.getallpersonaldetails();
//}









//	@GetMapping("/export/excel")
//	public ResponseHandler exportToExcel() throws IOException {
//	    ResponseHandler responseHandler = new ResponseHandler();
//
//	    try {
//	    String filename = "PersonalDetails.xlsx";
//		ByteArrayInputStream in = proposalServiceImp.exportEmployeesToExcel();
//
//		// (in == null) {
//		    responseHandler.setStatus(false);
//		    responseHandler.setMessage("Failed to generate Excel file. Stream is null.");
//		    responseHandler.setData(new ArrayList<>());
//		    
//		   // return responseHandler;
//	//	}
//	    }catch (IllegalArgumentException msg) {
//	    	responseHandler.setStatus(false);
//	    	responseHandler.setData(new ArrayList<>());
//	    	responseHandler.setMessage("Exception Presnt "+msg.getMessage());
//	    	
//		}catch (Exception e) {
//			responseHandler.setStatus(false);
//	    	responseHandler.setData(new ArrayList<>());
//	    	responseHandler.setMessage("Exception Presnt "+e.getMessage());
//		}
//	   
//		byte[] excelBytes = in.readAllBytes();
//		String base64Excel = Base64.getEncoder().encodeToString(excelBytes);
//
//		Map<String, String> data = new HashMap<>();
//		data.put("fileName", filename);
//		data.put("fileContent", base64Excel);
//		data.put("contentType", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
//
//		responseHandler.setStatus(true);
//		responseHandler.setMessage("Excel file generated successfully.");
//		responseHandler.setData(data);
//
//	    return responseHandler;
//	}
//
//	
//	




//	
//	@GetMapping("/export/excel")
//	public ResponseEntity<InputStreamResource> exportToExcel() {
//	    ByteArrayInputStream in = proposalServiceImp.exportEmployeesToExcel();
//
//	    return ResponseEntity.ok()
//	            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=PersonalDetails.xlsx")
//	            .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
//	            .body(new InputStreamResource(in));
//	}
//
//	

//	@GetMapping("/export/excel")
//	public ResponseEntity<InputStreamResource> exportToExcel() {
//		ByteArrayInputStream in = proposalServiceImp.exportEmployeesToExcel();
//		
//		if (in == null) {
//			return ResponseEntity.internalServerError().build();
//		}
//		
//		return ResponseEntity.ok()
//				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=PersonalDetails.xlsx")
//				.contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
//				.body(new InputStreamResource(in));
//	}


































//
//
//	@PostMapping("/listing")
//	public ResponseHandler getallPagelists(@RequestBody PaginationList paginationList) {
//
//		ResponseHandler responseHandler = new ResponseHandler(); //object bana
//
//		try {
//
//			List<PersonalDetails> details4=	 proposalServiceImp.getPaginationList(paginationList);
//
//			if(details4.isEmpty()) {
//				responseHandler.setStatus(false);
//				responseHandler.setData(new ArrayList<>());
//				responseHandler.setMessage("Pagination List Not Found");
//				responseHandler.setTotalRecord(proposalService.totalRecords());
//
//			}else {
//				responseHandler.setStatus(true);
//				responseHandler.setData(details4);
//				responseHandler.setMessage("Pagination List Found" );
//				proposalServiceImp.getPaginationList(paginationList);
//			}
//			responseHandler.setTotalRecord(proposalService.totalRecords());
//
//			
//			
//		}catch (Exception e) {
//			e.printStackTrace();
//			responseHandler.setStatus(false);
//			responseHandler.setData(new ArrayList<>());
//			responseHandler.setMessage("Error occoured");
//			e.printStackTrace();
//		}
//		return responseHandler;
//
//	}





// third party API Example

//	@GetMapping("/getkeys/api")
//	public ResponseEntity<?> greeting(){
//
//	//	Authentication authentication =SecurityContextHolder.getContext().getAuthentication();
//
//		WheatherResponse wheatherResponse= wheatherService.getWheather("Mumbai");
//		
//		String greeting="";
//		
//		if(wheatherResponse!=null) {
//			
//			greeting="Wheater feels like "+ wheatherResponse.getCurrent().getFeelslike();
//		}
//		System.out.println("------>>>>>"+greeting);
//		System.out.println("----->>"+wheatherResponse.getCurrent());
//		System.out.println("--------->>"+wheatherService.getWheather(greeting));
//		return  new ResponseEntity<>("Hii"+ greeting ,HttpStatus.OK);
//		
//		
//
//		


//} 



//	
//
//	@PostMapping("/login")
//	public ResponseHandler loginUser(@RequestBody UserLoginRequest user) {
//
//		ResponseHandler response = new ResponseHandler();
//
//		try {
//			UserInfo isAuthenticated = auth.authenticateUser(user.getUsername(), user.getPassword());
//
//			if (isAuthenticated != null) {
//
//				//String token = TokenGenerator.generateToken(isAuthenticated.getUsername(), isAuthenticated.getUserId());
//				String token=TokenGenerator.generateToken(isAuthenticated); 
//				response.setData(token);
//				response.setMeatus(true);
//			} else {ssage("Login successful");
//				response.setSt
//				response.setMessage("Invalid username or password");
//				response.setStatus(false);
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			response.setMessage("Internal server error");
//			response.setStatus(false);
//		}
//
//		return response;
//	}


////1
//@GetMapping("/excel")
//public ResponseEntity<ByteArrayResource> download() throws IOException{
//
//
//	String filename= "PersonalDetails.xlsx";
//	ByteArrayInputStream in=proposalServiceImp.getActualData();
//
//	ByteArrayResource resource = new ByteArrayResource(in.readAllBytes());
//
//	return ResponseEntity.ok()
//			.header("Content-Disposition", "attachment; filename=PersonalDetails.xlsx") 
//			.header(HttpHeaders.CONTENT_TYPE, "application/octet-stream") 
//			.body(resource);
//}


//@GetMapping("/excel")
//public ResponseHandler download() {
//    ResponseHandler responseHandler = new ResponseHandler();
//
//    try {
//        String filename = "PersonalDetails.xlsx";
//        ByteArrayInputStream in = proposalServiceImp.getActualData();
//
//        ByteArrayResource resource = new ByteArrayResource(in.readAllBytes());
//
//        responseHandler.setStatus(true);
//        responseHandler.setData(resource);
//        responseHandler.setMessage("Excel file generated successfully: " + filename);
//
//    } catch (IOException e) {
//        e.printStackTrace();
//        responseHandler.setStatus(false);
//        responseHandler.setData(null);
//        responseHandler.setMessage("Failed to generate Excel file");
//    }
//
//    return responseHandler;
//}
//

