package com.democrud.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
import com.democrud.util.JwtUtil;

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

			//			if (userRepository.findByUsername(user.getUsername()).isPresent() &&
			//					userRepository.findByEmailId(user.getEmailId()).isPresent() &&
			//					userRepository.findByRole(user.getRole()).isEmpty()) { 

			if("username & Email  already exists for Register".equals(result)) {
				response.setStatus(false);
				response.setMessage("Username & Email  already exists for Register"+result);
			} 
			else {

				response.setStatus(true);
				userRepository.save(user);
				response.setMessage("User registered successfully");
				response.setData(result); 
				//				user.setPassword(passwordEncoder.encode(user.getPassword()));
				//				userRepository.save(user);
				//				response.setStatus(true);
				//				response.setMessage("User registered successfully");
				//				response.setData(user.getUsername());
			}
		} catch (Exception e) {

			response.setStatus(false);
			response.setMessage("Internal server error");
		}

		return response;
	}


	@PostMapping("/login")
	public ResponseHandler loginUser(@RequestBody UserLoginRequest user) {
		ResponseHandler response = new ResponseHandler();

		try {
			UserInfo isAuthenticated = auth.authenticateUser(user.getUsername(), user.getPassword());

			if (isAuthenticated != null) {

				//String token = TokenGenerator.generateToken(isAuthenticated.getUsername(), isAuthenticated.getUserId());

				String token=TokenGenerator.generateToken(isAuthenticated); //maps
				response.setData(token);
				response.setMessage("Login successful");
				response.setStatus(true);
			} else {

				response.setMessage("Invalid username or password");
				response.setStatus(false);
			}

		} catch (Exception e) {
			e.printStackTrace();
			response.setMessage("Internal server error");
			response.setStatus(false);
		}

		return response;
	}


	//1
	@GetMapping("/excel")
	public ResponseEntity<ByteArrayResource> download() throws IOException{


		String filename= "PersonalDetails.xlsx";
		ByteArrayInputStream in=proposalServiceImp.getActualData();

		ByteArrayResource resource = new ByteArrayResource(in.readAllBytes());


		return ResponseEntity.ok()
				.header("Content-Disposition", "attachment; filename=PersonalDetails.xlsx") 
				.header(HttpHeaders.CONTENT_TYPE, "application/octet-stream") 
				.body(resource);
	}



	@GetMapping("/export/excel")
	public ResponseEntity<InputStreamResource> exportToExcel() {
		ByteArrayInputStream in = proposalServiceImp.exportEmployeesToExcel();

		if (in == null) {
			return ResponseEntity.internalServerError().build();
		}

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=PersonalDetails.xlsx")
				.contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
				.body(new InputStreamResource(in));
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

		//		String header = request.getHeader("Authorization");
		//		if(header!=null && header.startsWith("Bearer ")) {
		//			String token = header.substring(7);
		//			JwtUtil.extractUsername(token);
		//		}



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
	public ResponseHandler getAllList(HttpServletRequest request){

		ResponseHandler responseHandler = new ResponseHandler(); //object bana
		String header = request.getHeader("Authorization");

		if(header!=null && header.startsWith("Bearer ")) {
			String token = header.substring(7);

			Integer userId = TokenGenerator.extractUserId(token);
			String username = TokenGenerator.extractUsername(token);
			String email= TokenGenerator.extractEmailId(token);
			String role=TokenGenerator.extractRole(token);


			System.err.println("userID-------->"+ userId+ "username-------->"+ username + "email---->"+email +"role--->"+role);

			responseHandler.setUserId(userId);
			responseHandler.setUsername(username);
			responseHandler.setEmailId(email);
			responseHandler.setRole(role);


		}

		try {

			List<PersonalDetails> details2 = proposalService.getAllPersonalDetails();
			proposalService.totalRecords();
			responseHandler.setStatus(true);
			responseHandler.setData(details2);
			responseHandler.setMessage("Person Details List Fetch Succesfully");
			responseHandler.setTotalRecord(proposalService.totalRecords());
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


		//	return proposalService.getallpersonaldetails();

	}

	//	@GetMapping("/{id}")
	//	public Optional<PersonalDetails> getpraposalbyid (@PathVariable Integer id) {
	//		return proposalService.getPersonaldetailsById(id);
	//	}


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
	public ResponseHandler getallPagelists(@RequestBody PaginationList paginationList) {

		ResponseHandler responseHandler = new ResponseHandler(); //object bana

		try {

			List<PersonalDetails> details4=	 proposalServiceImp.getPaginationList(paginationList);

			if(details4.isEmpty()) {
				responseHandler.setStatus(false);
				responseHandler.setData(new ArrayList<>());
				responseHandler.setMessage("Pagination List Not Found");
				responseHandler.setTotalRecord(proposalService.totalRecords());

			}else {
				responseHandler.setStatus(true);
				responseHandler.setData(details4);
				responseHandler.setMessage("Pagination List Found" );
				proposalServiceImp.getPaginationList(paginationList);
			}
			responseHandler.setTotalRecord(proposalService.totalRecords());

		}catch (Exception e) {
			e.printStackTrace();
			responseHandler.setStatus(false);
			responseHandler.setData(new ArrayList<>());
			responseHandler.setMessage("Error occoured");
			e.printStackTrace();
		}
		return responseHandler;

	}
	
	
	
	@GetMapping("/personal-details/{id}")
	public ResponseHandler getPersonalDetailsById(@PathVariable Integer id) {

	    ResponseHandler responseHandler = new ResponseHandler();

	    try {
	        Optional<PersonalDetails> optional = proposalService.getPersonalDetailsById(id);

	        if (optional.isPresent()) {
	            responseHandler.setStatus(true);
	            responseHandler.setData(optional.get());
	            responseHandler.setMessage("Record found");
	        } else {
	            responseHandler.setStatus(false);
	            responseHandler.setData(null);
	            responseHandler.setMessage("Record not found with ID: " + id);
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	        responseHandler.setStatus(false);
	        responseHandler.setData(null);
	        responseHandler.setMessage("An error occurred while fetching the record.");
	    }

	    return responseHandler;
	}
}

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





