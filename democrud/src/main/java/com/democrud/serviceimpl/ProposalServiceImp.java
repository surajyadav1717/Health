
package com.democrud.serviceimpl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.democrud.entity.PaginationList;
import com.democrud.entity.PersonalDetails;
import com.democrud.entity.PersonalDetailsDto;
import com.democrud.entity.Queue;
import com.democrud.entity.ResponseExcel;
import com.democrud.entity.SearchFilter;
import com.democrud.helper.Helper;
import com.democrud.helper.TokenGenerator;
import com.democrud.repository.GenderRepository;
import com.democrud.repository.ProposerRepository;
import com.democrud.repository.QueueRepository;
import com.democrud.repository.ResponseExcelRepository;
import com.democrud.service.ProposalService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class ProposalServiceImp implements ProposalService {

	@Autowired
	private final TokenGenerator tokenGenerator;

	long count=0;

	Integer totalRecord = 0;

	Integer sucessRecord=0;

	int errorRecord=0;

	@Autowired
	private EntityManager entityManager;


	@Autowired
	private ProposerRepository proposerRepository;

	@Autowired
	private GenderRepository genderRepository;

	@Autowired
	private ResponseExcelRepository excelRepository;


	@Autowired
	private QueueRepository queueRepository;

	ProposalServiceImp(TokenGenerator tokenGenerator) {
		this.tokenGenerator = tokenGenerator;
	}

	//	--------------- 1st 
	public ByteArrayInputStream getActualData() throws IOException {  //data to excel wala method 

		List<PersonalDetails> all = proposerRepository.findAll();
		ByteArrayInputStream byteArrayInputStream = Helper.dataToExcel(all);
		return byteArrayInputStream;
	}



	//------2nd 	
	//@Scheduled(fixedRate=500)
	public ByteArrayInputStream exportEmployeesToExcel() {

		//String filePath ="C:\\ExportData\\DownloadData\\PersonalDetails.xlsx";

		try (Workbook workbook = new XSSFWorkbook ();
				ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			Sheet sheet = workbook.createSheet("PersonalDetails");
			Row headerRow = sheet.createRow(0);

			List<String> headers = Arrays.asList("fullName*", "*gender", "*dateofBirth","*emailId", "*address","city" ,"state" , "pincode");

			for (int i = 0; i < headers.size(); i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(headers.get(i));
			}

			String uid = UUID.randomUUID().toString().replace("-","" );

			String filename= "personal_details_" +uid+".xlsx";

			String filePath = "C:\\Export Data\\DownloadData\\"+filename;
			workbook.write(out);
			//return new ByteArrayInputStream(out.toByteArray());

			FileOutputStream fileOutputStream= new FileOutputStream(filePath);
			workbook.write(fileOutputStream);
			workbook.close();
			fileOutputStream.close();

			System.out.println("File Exported to File Path"+filePath);


		} catch (IOException e) {
			throw new RuntimeException("Error while creating Excel file", e);
		}
		return null;


	}

	//	//3
	//	public List<PersonalDetails> getAllPersonalDetailsData(){
	//		return this.proposerRepository.findAll() ;
	//
	//	}
	//
	//

	@Override
	public PersonalDetails saveProposal(PersonalDetailsDto personalDetailsDto) {


		String fullName = personalDetailsDto.getFullName();

		if (fullName == null || personalDetailsDto.getFullName().trim().isEmpty()) {

			throw new IllegalArgumentException("Name Is required");
		}

		if (personalDetailsDto.getGender() == null || (!personalDetailsDto.getGender().equalsIgnoreCase("Male")
				&& !personalDetailsDto.getGender().equalsIgnoreCase("Female")
				&& !personalDetailsDto.getGender().equalsIgnoreCase("Other"))) {
			throw new IllegalArgumentException("Gender must be Male, Female, or Other.");
		}

		// trim whitespaces remove karta hai
		if (personalDetailsDto.getDateofBirth() == null || personalDetailsDto.getDateofBirth().trim().isEmpty()) {
			throw new IllegalArgumentException("Date of Birth is required.");
		}

		if (personalDetailsDto.getProfession() == null || personalDetailsDto.getProfession().trim().isEmpty()) {
			throw new IllegalArgumentException("Profession is required.");
		}

		if (personalDetailsDto.getOcupation() == null || personalDetailsDto.getOcupation().trim().isEmpty()) {
			throw new IllegalArgumentException("Occupation is required.");
		}

		if (personalDetailsDto.getMaritalStatus() == null || personalDetailsDto.getMaritalStatus().trim().isEmpty()) {
			throw new IllegalArgumentException("Marital Status is required.");
		}

		if (personalDetailsDto.getEmailId() != null && !personalDetailsDto.getEmailId().trim().isEmpty()
				&& personalDetailsDto.getEmailId().contains("@")) {

			boolean al = proposerRepository.existsByEmailId(personalDetailsDto.getEmailId());
			System.out.println("---->" + al);
			if (al) {
				throw new IllegalArgumentException("Email Duplicate");
			}

		} else {
			throw new IllegalArgumentException("Enter Valid Email");
		}

		if (personalDetailsDto.getContactDetails() == null
				|| String.valueOf(personalDetailsDto.getContactDetails()).length() != 9) {
			throw new IllegalArgumentException("Contact number must be 10 digits.");
		}

		if (personalDetailsDto.getAddress() == null || personalDetailsDto.getAddress().trim().isEmpty()) {
			throw new IllegalArgumentException("Address is required.");
		}

		if (personalDetailsDto.getCity() == null || personalDetailsDto.getCity().trim().isEmpty()) {
			throw new IllegalArgumentException("City is required.");
		}

		if (personalDetailsDto.getPinCode() == null || String.valueOf(personalDetailsDto.getPinCode()).length() != 6) {
			throw new IllegalArgumentException("Pincode must be 6 digits.");
		}

		if (personalDetailsDto.getAlternateAddress() == null
				|| personalDetailsDto.getAlternateAddress().trim().isEmpty()) {
			throw new IllegalArgumentException("Alternate Address is required.");
		}

		if ((personalDetailsDto.getPancardNo() == null)
				|| !personalDetailsDto.getPancardNo().matches("[A-Z]{5}[0-9]{4}[A-Z]{1}")) {
			throw new IllegalArgumentException("PAN Card number must be in the format (ABCDE1234F ");
		}

		if (personalDetailsDto.getArea() == null || personalDetailsDto.getArea().trim().isEmpty()) {
			throw new IllegalArgumentException("Area is required.");
		}

		if (personalDetailsDto.getTown() == null || personalDetailsDto.getTown().trim().isEmpty()) {
			throw new IllegalArgumentException("Town is required.");
		}

		if (personalDetailsDto.getAddharCard() == null
				|| String.valueOf(personalDetailsDto.getAddharCard()).length() != 12) {
			throw new IllegalArgumentException("Aadhar Card must be a 12-digit number.");
		}

		if (personalDetailsDto.getState() == null || personalDetailsDto.getState().trim().isEmpty()) {
			throw new IllegalArgumentException("State is required.");
		}

		if (personalDetailsDto.getStatus() == null
				|| (!personalDetailsDto.getStatus().equals('Y') && !personalDetailsDto.getStatus().equals('N'))) {
			throw new IllegalArgumentException("Status must be 'Y' or 'N'.");
		}

		PersonalDetails prdetails = new PersonalDetails(); // object banega simple


		//		System.out.println("Gender Id--------->"+ prdetails.getGender());
		//
		//		String gendertype = personalDetailsDto.getGender();
		//
		//		if(gendertype!=null && !gendertype.isEmpty()) {
		//			Optional<GenderData> genderdata=genderRepository.findByGenderType(gendertype);
		//			if(genderdata.isPresent()) {
		//				//	prdetails.setper(genderdata.get().getGenderId());
		//
		//				prdetails.setPersonalId(genderdata.get().getGenderId());
		//			}
		//			else {
		//				throw new IllegalArgumentException("Gender Type Not Found");
		//			}
		//		}
		//		else {
		//			throw new IllegalArgumentException("Gender should not null");
		//		}



		prdetails.setFullName(personalDetailsDto.getFullName()); //                                       personaldetails refrence variable se data set hoga
		// and personaldto se data get hoga

		System.out.println("Saving Name: " + prdetails.getFullName());
		prdetails.setDateofBirth(personalDetailsDto.getDateofBirth());
		prdetails.setEmailId(personalDetailsDto.getEmailId());
		prdetails.setAddress(personalDetailsDto.getAddress());
		prdetails.setCity(personalDetailsDto.getCity());
		prdetails.setContactDetails(personalDetailsDto.getContactDetails());
		prdetails.setGender(personalDetailsDto.getGender());
		prdetails.setOcupation(personalDetailsDto.getOcupation());
		prdetails.setProfession(personalDetailsDto.getProfession());
		prdetails.setMaritalStatus(personalDetailsDto.getMaritalStatus());
		prdetails.setPincode(personalDetailsDto.getPinCode());
		prdetails.setAlternateAddress(personalDetailsDto.getAlternateAddress());
		prdetails.setAddharCard(personalDetailsDto.getAddharCard());
		prdetails.setArea(personalDetailsDto.getArea());
		prdetails.setPancardNo(personalDetailsDto.getPancardNo());
		prdetails.setState(personalDetailsDto.getState());
		prdetails.setTown(personalDetailsDto.getTown());
		prdetails.setStatus(personalDetailsDto.getStatus());

		// details saved to Y
		personalDetailsDto.setStatus('Y');
		return proposerRepository.save(prdetails);                                                        // save ho jyga prdetails ka ref object
	}

	@Override
	public List<PersonalDetails> getAllPersonalDetails() {

		List<PersonalDetails> all = proposerRepository.findAllByStatus('Y');   // for status
		if(all==null|| all.isEmpty()) {

			throw new IllegalArgumentException("List Is Null And Empty ");
		}

		return all;
	}

	@Override 																										// here same dto se
	public PersonalDetails updateProposal(Integer id, PersonalDetailsDto updatedDetailsDto) {
		// Fetch existing record

		PersonalDetails existingDetails = proposerRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("PersonalDetails with ID " + id + " not found."));

		if (updatedDetailsDto.getFullName() == null || updatedDetailsDto.getFullName().trim().isEmpty()) {

			throw new IllegalArgumentException("Name Is required");
		} else {
			existingDetails.setFullName(updatedDetailsDto.getFullName());
		}

		if (updatedDetailsDto.getDateofBirth() == null || updatedDetailsDto.getDateofBirth().trim().isEmpty()) {
			throw new IllegalArgumentException("Date Of Birth Is required");

		} else {
			existingDetails.setDateofBirth(updatedDetailsDto.getDateofBirth());

		}
		if (updatedDetailsDto.getGender() == null || (!updatedDetailsDto.getGender().equalsIgnoreCase("Male")
				&& !updatedDetailsDto.getGender().equalsIgnoreCase("Female")
				&& !updatedDetailsDto.getGender().equalsIgnoreCase("Other"))) {
			throw new IllegalArgumentException("Gender must be Male, Female, or Other.");
		}

		else {
			existingDetails.setGender(updatedDetailsDto.getGender());

		}
		if (updatedDetailsDto.getOcupation() == null || updatedDetailsDto.getOcupation().trim().isEmpty()) {
			throw new IllegalArgumentException("Occupation Is required");
		}

		else {
			existingDetails.setOcupation(updatedDetailsDto.getOcupation());
		}

		if (updatedDetailsDto.getProfession() == null || updatedDetailsDto.getProfession().trim().isEmpty()) {

			throw new IllegalArgumentException("Profesion Is required");
		} else {
			existingDetails.setProfession(updatedDetailsDto.getProfession());
		}

		if (updatedDetailsDto.getMaritalStatus() == null || updatedDetailsDto.getMaritalStatus().trim().isEmpty()) {
			throw new IllegalArgumentException("Maritial Is required");
		} else {
			existingDetails.setMaritalStatus(updatedDetailsDto.getMaritalStatus());
		}

		if (updatedDetailsDto.getAddress() == null || updatedDetailsDto.getAddress().trim().isEmpty()) {
			throw new IllegalArgumentException("Address Is required");

		} else {
			existingDetails.setAddress(updatedDetailsDto.getAddress());
		}

		if (updatedDetailsDto.getCity() == null || updatedDetailsDto.getCity().trim().isEmpty()) {
			throw new IllegalArgumentException("City Is required");

		} else {
			existingDetails.setCity(updatedDetailsDto.getCity());
		}

		if (updatedDetailsDto.getArea() == null || updatedDetailsDto.getArea().trim().isEmpty()) {
			throw new IllegalArgumentException("Area Is required");

		} else {
			existingDetails.setArea(updatedDetailsDto.getArea());

		}

		if (updatedDetailsDto.getEmailId() != null && !updatedDetailsDto.getEmailId().trim().isEmpty()
				&& updatedDetailsDto.getEmailId().contains("@")) {

			boolean al = proposerRepository.existsByEmailId(updatedDetailsDto.getEmailId());
			// System.out.println("---->"+al);
			if (al) {
				throw new IllegalArgumentException("Email Duplicate ");
			}

			else {
				throw new IllegalArgumentException("Enter Valid Email");
			}

		}

		if (updatedDetailsDto.getAlternateAddress() == null
				|| updatedDetailsDto.getAlternateAddress().trim().isEmpty()) {
			throw new IllegalArgumentException("Alternate Address Is required");

		}

		else {
			existingDetails.setAlternateAddress(updatedDetailsDto.getAlternateAddress());

		}

		if (updatedDetailsDto.getState() == null || updatedDetailsDto.getState().trim().isEmpty()) {
			throw new IllegalArgumentException("State Is required");

		} else {
			existingDetails.setState(updatedDetailsDto.getState());
		}

		if (updatedDetailsDto.getPinCode() == null || String.valueOf(updatedDetailsDto.getPinCode()).length() != 6) {
			throw new IllegalArgumentException("Pincode Of 6 Digit Is required");

		} else {
			existingDetails.setPincode(updatedDetailsDto.getPinCode());

		}

		if (updatedDetailsDto.getContactDetails() == null
				|| String.valueOf(updatedDetailsDto.getContactDetails()).length() == 10)
		{
			throw new IllegalArgumentException("Contact must be 10 Digit ");

		} else {
			existingDetails.setContactDetails(updatedDetailsDto.getContactDetails());

		}

		if (updatedDetailsDto.getAddharCard() == null
				|| String.valueOf(updatedDetailsDto.getAddharCard()).length() != 12) {
			throw new IllegalArgumentException("Aadhar Must be 12 Digit ");

		} else {
			existingDetails.setAddharCard(updatedDetailsDto.getAddharCard());

		}

		if (updatedDetailsDto.getTown() == null || updatedDetailsDto.getTown().trim().isEmpty()) {
			throw new IllegalArgumentException("Field Is Required");

		} else {
			existingDetails.setTown(updatedDetailsDto.getTown());

		}

		if (updatedDetailsDto.getStatus() != null) {
			existingDetails.setStatus(updatedDetailsDto.getStatus());
		}

		else {
			existingDetails.setStatus(updatedDetailsDto.getStatus());
		}
		return proposerRepository.save(existingDetails);

	}


	public PersonalDetails deleteById(Integer id) {

		try {

			if (id == null || id <= 0) {

				throw new IllegalAccessError("Id Should Be Not Negative and Less than Zero");

			}
			// fetching record -- based on id
			Optional<PersonalDetails> optionalDetails = proposerRepository.findById(id);

			// if not present ! - yeh hota hai
			if (!optionalDetails.isPresent()) {

				throw new IllegalArgumentException("PersonalDetails with ID \" + id + \" not found. Status: N\"");
			}

			// Data found, update status to "Y"
			PersonalDetails personalDetails = optionalDetails.get();													// optional hai to get karna hoga data ko
			personalDetails.setStatus('N'); 																									// Mark before deletion
			proposerRepository.save(personalDetails);
			// successfully. Status: N");

		} catch (Exception e) {
			e.printStackTrace();

			throw new IllegalArgumentException("An error :" + e.getMessage());
		}
		return null;

	}

//	@Override
//	public Optional<PersonalDetails> getPersonalDetailsById(Integer id , PersonalDetails details) {
//
//		PersonalDetailsDto detailsDto = new PersonalDetailsDto();
//
//		if(details ==null ) {
//			throw new IllegalArgumentException("List Of Data Is Null");
//		}
//
//		details.setFullName(detailsDto.getFullName());
//		details.setDateofBirth(details.getDateofBirth());
//		details.setEmailId(detailsDto.getEmailId());
//		details.setAddress(detailsDto.getAddress());
//		details.setCity(detailsDto.getCity());
//		details.setContactDetails(detailsDto.getContactDetails());
//		details.setGender(detailsDto.getGender());
//		details.setOcupation(detailsDto.getOcupation());
//		details.setProfession(detailsDto.getProfession());
//		details.setMaritalStatus(detailsDto.getMaritalStatus());
//		details.setPincode(detailsDto.getPinCode());
//		details.setAlternateAddress(detailsDto.getAlternateAddress());
//		details.setAddharCard(detailsDto.getAddharCard());
//		details.setArea(detailsDto.getArea());
//		details.setPancardNo(detailsDto.getPancardNo());
//		details.setState(detailsDto.getState());
//		details.setTown(detailsDto.getTown());
//		details.setStatus(detailsDto.getStatus());
//
//
//		return proposerRepository.findById(id);
//
//
//	}


	//for (PersonalDetails p : personalDetailsList) {
	//			Map<String, Object> map = new HashMap<>();
	//			map.put("fullName", p.getFullName());
	//			map.put("gender", p.getGender());
	//			map.put("email", p.getEmailId());
	//			map.put("state", p.getState());
	//			resultData.add(map);
	//		}
	//
	//		return resultData;
	//	}

	@Override
	public List<PersonalDetails> getPaginationList(PaginationList paginationList) {


		// 2 stp
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();									 // Yeh actually entity class se ayga
		// criteria builder banega
		CriteriaQuery<PersonalDetails> criteriaQuery = criteriaBuilder.createQuery(PersonalDetails.class); // then

		Root<PersonalDetails> root = criteriaQuery.from(PersonalDetails.class);

		// SearchFilter [] searchFilters = paginationList.getSearchFilters();

		// predicate is easy to add when list we want to add using AND operator
		List<Predicate> predicates = new ArrayList<>();

		List<SearchFilter> searchFilters = paginationList.getFilters();

		String status  = "";

		if (searchFilters != null)

			//statrus yes and no wala 
			for (SearchFilter f : searchFilters) {

				if (f.getStatus() != null) {
					if ("N".equalsIgnoreCase(f.getStatus())) {
						predicates.add(criteriaBuilder.equal(root.get("status"), "N"));
					} else {
						predicates.add(criteriaBuilder.equal(root.get("status"), "Y"));
					}


					predicates.add(criteriaBuilder.like(root.get("status"), "%" + f.getStatus() + "%"));
				}

				if (f.getFullName() != null && !f.getFullName().isEmpty()) {

					predicates.add(criteriaBuilder.like(root.get("fullName"), "%" + f.getFullName() + "%"));

				}

				if (f.getEmailId() != null && !f.getEmailId().isEmpty()) {
					predicates.add(criteriaBuilder.like(root.get("emailid"), "%" + f.getEmailId() + "%"));

				}

				if (f.getTown() != null && !f.getTown().isEmpty()) {
					predicates.add(criteriaBuilder.like(root.get("town"), "%" + f.getTown() + "%"));

				}

				if (f.getStatus() != null) {
					predicates.add(criteriaBuilder.equal(root.get("status"), f.getStatus()));
					// criteriaQuery.where(criteriaBuilder.equal(root.get("status"),f.getStatus()));
				}
			}

		// add karega dynamic filter using and operator ke sath
		if (!predicates.isEmpty()) {
			criteriaQuery.where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));
		}

		// default wala h
		if (paginationList.getPage() >= 0 && paginationList.getSize() >= 0) {

			if (paginationList.getSortBy() == null || paginationList.getSortOrder().isEmpty()) {

				paginationList.setSortBy("id"); // updated sort
				paginationList.setSortOrder("DESC");

			}
		}

		// validation hua hai jab null hua and is empty hai toh
		if (paginationList.getSortBy() != null && !paginationList.getSortBy().isEmpty()) {

			String sortBy = paginationList.getSortBy();

			// if asc is present hai toh sort kar de
			if ("ASC".equalsIgnoreCase(paginationList.getSortOrder())) {

				criteriaQuery.orderBy(criteriaBuilder.asc(root.get(sortBy))); // asc le liye
			}

			else {

				criteriaQuery.orderBy(criteriaBuilder.desc(root.get(sortBy))); // desc ke liye

			}

		}

		TypedQuery<PersonalDetails> typedQuery = entityManager.createQuery(criteriaQuery);

		List<PersonalDetails> resultList = typedQuery.getResultList();


		if (paginationList.getPage() > 0 && paginationList.getSize() > 0) {

			int page = paginationList.getPage();
			int size = paginationList.getSize();

			typedQuery.setFirstResult((page - 1) * size);

			if (paginationList.getPage() != null) {
				System.out.println("---" + page + size);
			} else {
				System.out.println(paginationList.getPage());

				System.out.println("Page nhi mila ");
			}
			int firstResult = typedQuery.getResultList().size();
			//TypedQuery<PersonalDetails> setMaxResults = typedQuery.setMaxResults(size);


			System.err.println("setMaxResults >>>>"+firstResult);
			totalRecord = firstResult;



		}
		return typedQuery.getResultList();




	}

	@Override
	public int totalRecords() {

		return totalRecord;
	}





	@Override
	public List<PersonalDetails> importPersonalDetailsFromExcel(MultipartFile file) throws IOException {


		String uploadDir = "C:\\ExcelFiles\\";
		File dir = new File(uploadDir);
		if (!dir.exists()) {
			dir.mkdirs();
		}

		String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
		String filePath = uploadDir + fileName;


		List<PersonalDetails> savedExcelList= new ArrayList<>();


		try  ( XSSFWorkbook	workbook = new XSSFWorkbook(file.getInputStream())) {
			XSSFSheet sheet = workbook.getSheetAt(0);

			int totalExcelRows = sheet.getLastRowNum(); 

			if (totalExcelRows > 2) {
				Queue queueTracker = new Queue();
				queueTracker.setTotalRecords(totalExcelRows);
				queueTracker.setRowsRead(0);
				queueTracker.setStatus("N"); // Pending
				queueTracker.setRunAt(LocalDateTime.now());
				queueTracker.setFilePath(filePath);
				queueTracker= queueRepository.save(queueTracker);

				//	return  null; // change 
			}

			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
				XSSFRow row = sheet.getRow(i);

				if (row == null)
					continue;

				PersonalDetails p = new PersonalDetails();
				List<String> errorFields = new ArrayList<>();

				Queue queue = new Queue();

				ResponseExcel excel = new ResponseExcel();

				//boolean isSuccess;
				System.out.println("msg commmmming");


				String fullName = getCellString(row.getCell(0));
				if (fullName == null || fullName.isEmpty() ||!fullName.matches("^[A-Za-z\\s]+$") ) {
					System.out.println("Row " + i + "  Full name is missing");
					errorFields.add("fullname"); // 
					excel.setErrorFields(" Error In FullName Fields");
					excel.setStatus(false);
					excel.setSuccess("failed ");
					excel.setError("Fullname Is Mandatory & Empty  ");
					excelRepository.save(excel);
					errorRecord++;
					System.err.println(errorRecord+"Error Record---->");
					//continue;

				}

				else{
					//p.setFullName(fullName);  //error fields all 
					excel.setErrorFields("Record Saved ");
					excel.setStatus(true);
					excel.setSuccess("Success");
					excel.setError("Data Found");
					excelRepository.save(excel);
					p.setFullName(fullName);
					System.err.println("Succes Record--->"+sucessRecord++);
				}


				String gender = getCellString(row.getCell(1));
				if (gender == null || !(gender.equalsIgnoreCase("Male") || gender.equalsIgnoreCase("Female"))) {
					System.out.println("Row " + i + " : Invalid gender (Expected: Male or Female)");
					errorFields.add("Gender"); // 
					excel.setErrorFields("Error In Gender Fields");
					excel.setStatus(false);
					excel.setSuccess("failed");
					excel.setError(" Invalid Gender");
					excelRepository.save(excel);
					errorRecord++;
					System.err.println(errorRecord+"Error Record------>");
					//continue;
				} 
				else {
					//p.setGender(gender);
					excel.setErrorFields("Record Saved-"); 
					excel.setStatus(true);
					excel.setSuccess("Success");
					excel.setError("Data Found");
					excelRepository.save(excel);
					p.setGender(gender);
					System.err.println("Succes Record--->"+sucessRecord++);
				}


				String dob = getCellString(row.getCell(2));

				if (dob == null || !dob.matches("\\d{4}-\\d{2}-\\d{2}")) {
					System.out.println("Row " + i + ": Invalid date format (Expected yyyy-MM-dd)");
					errorFields.add("DOB");
					excel.setErrorFields("Error In DOB Fields");
					excel.setStatus(false);
					excel.setSuccess("failed ");
					excel.setError(" Date of Birth Is Empty(Expected yyyy-MM-dd)");
					excelRepository.save(excel);
					errorRecord++;
					System.err.println(errorRecord+"Error Record------>");
					//continue;
				}
				else {
					//p.setDateofBirth(dob);
					excel.setErrorFields("Record Saved-"); 
					excel.setStatus(true);
					excel.setSuccess("Success");
					excelRepository.save(excel);
					p.setDateofBirth(dob);
					System.err.println("Succes Record--->"+sucessRecord++);

				}

				String emailId = getCellString(row.getCell(3));
				if (emailId == null || !emailId.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
					System.out.println("Row " + i + ": Invalid or missing email ID");
					errorFields.add("emailId");
					excel.setErrorFields("Error In Email Fields");
					excel.setStatus(false);
					excel.setSuccess("failed");
					excel.setError(" Email Is Mandatory& It's  Not Valid ");
					excelRepository.save(excel);
					errorRecord++;
					System.err.println(errorRecord+"Error Record------>");
					//continue;

				}
				else {
					//p.setEmailId(emailId);
					excel.setErrorFields("Record Saved-"); 
					excel.setStatus(true);
					excel.setSuccess("Success");
					excelRepository.save(excel);
					p.setEmailId(emailId);
					System.err.println("Succes Record--->"+sucessRecord++);
				}

				String contactDetails = getCellString(row.getCell(4));
				if (contactDetails == null || !contactDetails.matches("^[6-9]\\d{9}$")) {
					System.out.println("Row " + i + ": Invalid contact number (Expected 10 digits)");
					errorFields.add("contactDetails");
					excel.setErrorFields("Error In Contact Fields");
					excel.setStatus(false);
					excel.setSuccess("failed");
					excel.setError("Contact No Is less than 10 Digit ");
					excelRepository.save(excel);
					errorRecord++;
					System.err.println(errorRecord+" Error Record------>");
					//continue;

				}
				else {
					//p.setContactDetails(contactDetails);
					excel.setErrorFields("Record Saved -"); 
					excel.setStatus(true);
					excel.setSuccess("Success");
					excelRepository.save(excel);
					p.setContactDetails(contactDetails);
					System.err.println("Succes Record--->"+sucessRecord++);

				}


				String address = getCellString(row.getCell(5));
				if (address == null || address.trim().isEmpty()) {
					System.out.println("Row " + i + ": Address is missing");
					errorFields.add("gender");
					excel.setErrorFields("Error In Address Fields ");
					excel.setStatus(false);
					excel.setSuccess("Failed");
					excel.setError("Address  Mandatory and It's Empty");
					excelRepository.save(excel);
					errorRecord++;
					System.err.println(errorRecord+" Error Record ------>");
					//continue;
				}
				else {
					//p.setAddress(address);
					excel.setErrorFields("Record Saved-"); 
					excel.setStatus(true);
					excel.setSuccess("Success");
					excelRepository.save(excel);
					p.setAddress(address);
					System.err.println("Succes Record--->"+sucessRecord++);
				}



				String city = getCellString(row.getCell(6));
				if (city == null || city.trim().isEmpty()) {
					System.out.println("Row " + i + ": City is missing");
					errorFields.add("city");
					excel.setErrorFields("Error In City Fields ");
					excel.setStatus(false);
					excel.setSuccess("Failed ");
					excel.setError("City Is Empty");
					excelRepository.save(excel);
					errorRecord++;
					System.err.println(errorRecord+" Error Record ------>");
					//continue;

				}
				else {
					//p.setCity(city);
					excel.setErrorFields("Record Saved "); 
					excel.setStatus(true);
					excel.setSuccess("Success");
					excelRepository.save(excel);
					p.setCity(city);
					System.err.println("Succes Record--->"+sucessRecord++);
				}

				String pincode = getCellString(row.getCell(7));
				if (pincode == null || !pincode.matches("\\d{6}")) {
					System.out.println("Row " + i + " Invalid pincode (Expected 6 digits)");
					errorFields.add("pincode");
					excel.setErrorFields(" Error In Pincode Field");
					excel.setStatus(false);
					excel.setSuccess("failed");
					excel.setError("Pincode Must be 6 Digit & It's Mandatory");
					excelRepository.save(excel);
					errorRecord++;
					System.err.println(errorRecord+" Error Record ------>");
					//continue;
				}
				else {
					//p.setPincode(pincode);
					excel.setErrorFields("Record Saved"); 
					excel.setStatus(true);
					excel.setSuccess("Success");
					excelRepository.save(excel);
					p.setPincode(pincode);
					System.err.println("Succes Record--->"+sucessRecord++);
				}


				String state = getCellString(row.getCell(8));
				if (state == null || state.trim().isEmpty()) {
					System.out.println("Row " + i + ": State is missing");
					//errorFields.add(state);
					excel.setErrorFields("Error In State Field");
					excel.setStatus(false);
					excel.setSuccess("Failed");
					excel.setError("state Is Mandatory & Missing");
					excelRepository.save(excel);
					errorRecord++;
					System.err.println(errorRecord+" Error Record ------>");
					//continue;
				}
				else{
					//p.setState(state);
					excel.setErrorFields("Record Saved "); 
					excel.setStatus(true);
					excel.setSuccess("Success");
					excelRepository.save(excel);
					p.setState(state);
					System.err.println("Succes Record--->"+sucessRecord++);

				}

				System.out.println("Row " + i + " => fullName: " + fullName + ", gender: " + gender + ", dob "+ dob  +" emaiId "+ emailId +" contactDetails "+ contactDetails  +" address "+ address +"city"+ city +" pincode" +pincode    +" state "+ state);

				if(!errorFields.isEmpty()) {


					Long queueId=queue.getId();

					for(String error : errorFields) {

						ResponseExcel responseExcel = new ResponseExcel();

						responseExcel.setQueueId(queueId);
						responseExcel.setStatus(false);
						responseExcel.setErrorFields(error+ "Error In Error Fields");
						excel.setSuccess("Failed");
						responseExcel.setError(error);
						excelRepository.save(responseExcel);
					}

				}else {

					ResponseExcel excel2 = new ResponseExcel();
					PersonalDetails saved = proposerRepository.save(p);
					excel2.setStatus(true);
					excel2.setSuccess("Success");
					excel2.setErrorFields("Record Saved ");
					excel2.setQueueId(queue != null ? queue.getId() : null); // Safe save
					excelRepository.save(excel2);

				}

				PersonalDetails saved = proposerRepository.save(p);

				//	System.err.println("total ----"+ "-----"+totalData() +"count ====>" +sucessRecord);

				savedExcelList.add(saved);
				count++;


				//System.err.println("total ----"+ "-----"+totalData() +"count ====>" +sucessRecord);
			}
		}
		return savedExcelList;

	}



	private String getCellString(Cell cell) {
		if (cell == null)

			return null; 

		switch (cell.getCellType()) {
		case STRING:
			return cell.getStringCellValue().trim();
		case NUMERIC:
			if (DateUtil.isCellDateFormatted(cell)) {

				return new SimpleDateFormat("yyyy-MM-dd").format(cell.getDateCellValue());
			} else {

				return String.valueOf((long) cell.getNumericCellValue());
			}
		case BOOLEAN:
			return String.valueOf(cell.getBooleanCellValue());
		case FORMULA:
			return cell.getCellFormula();
		case BLANK:
			return "";
		default:
			return "";
		}
	}


	@Override
	public long totalData() {

		return count;






	}


	@Override
	public long getErrorCount() {

		return errorRecord;
	}



	@Override
	//@Scheduled(fixedDelay = 10000) 
	public void processPendingQueues() throws IOException {
		int batchSize = 3;
		List<Queue> pendingQueues = queueRepository.findByStatus("N");

		for (Queue queue : pendingQueues) {
			int processedRows = queue.getRowsRead();
			int totalRows = queue.getTotalRecords();
			int rowsRemaining = totalRows - processedRows;
			int rowsToProcess = Math.min(batchSize, rowsRemaining);

			String filePath = queue.getFilePath();


			System.out.println(" read file at: " + filePath);

			File file = new File(filePath);
			if (!file.exists()) {
				System.err.println("File not found: " + filePath);
				continue;
			}

			try (FileInputStream fis = new FileInputStream(file);
					XSSFWorkbook workbook = new XSSFWorkbook(fis)) {

				XSSFSheet sheet = workbook.getSheetAt(0);
				int lastRowNum = sheet.getLastRowNum();

				int rowsProcessed = 0;

				for (int i = 0; i < rowsToProcess && (processedRows + i) <= lastRowNum; i++) {
					int currentRowNum = processedRows + i;
					XSSFRow row = sheet.getRow(currentRowNum);
					if (row == null)
						continue;

					PersonalDetails p = new PersonalDetails();
					ResponseExcel response = new ResponseExcel();
					List<String> errors = new ArrayList<>();

					String fullName = getCellString(row.getCell(0));
					if (fullName == null || !fullName.matches("^[A-Za-z\\s]+$")) {
						errors.add("Invalid Full Name");
					} else {
						p.setFullName(fullName);
					}

					String gender = getCellString(row.getCell(1));
					if (gender == null || !(gender.equalsIgnoreCase("Male") || gender.equalsIgnoreCase("Female"))) {
						errors.add("Invalid Gender");
					} else {
						p.setGender(gender);
					}

					String dob = getCellString(row.getCell(2));
					if (dob == null || !dob.matches("\\d{4}-\\d{2}-\\d{2}")) {
						errors.add("Invalid DOB");
					} else {
						p.setDateofBirth(dob);
					}

					String emailId = getCellString(row.getCell(3));
					if (emailId == null || !emailId.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
						errors.add("Invalid Email");
					} else {
						p.setEmailId(emailId);
					}

					String contact = getCellString(row.getCell(4));
					if (contact == null || !contact.matches("^[6-9]\\d{9}$")) {
						errors.add("Invalid Contact");
					} else {
						p.setContactDetails(contact);
					}

					String address = getCellString(row.getCell(5));
					if (address == null || address.trim().isEmpty()) {
						errors.add("Empty Address");
					} else {
						p.setAddress(address);
					}

					String city = getCellString(row.getCell(6));
					if (city == null || city.trim().isEmpty()) {
						errors.add("Empty City");
					} else {
						p.setCity(city);
					}

					String pincode = getCellString(row.getCell(7));
					if (pincode == null || !pincode.matches("\\d{6}")) {
						errors.add("Invalid Pincode");
					} else {
						p.setPincode(pincode);
					}

					String state = getCellString(row.getCell(8));
					if (state == null || state.trim().isEmpty()) {
						errors.add("Empty State");
					} else {
						p.setState(state);
					}

					if (errors.isEmpty()) {
						proposerRepository.save(p);
						response.setStatus(true);
						response.setSuccess("Success");
						response.setErrorFields("All fields valid");
					} else {
						response.setStatus(false);
						response.setSuccess("Failed");
						response.setErrorFields(String.join(", ", errors));
					}

					response.setQueueId(queue.getId());
					excelRepository.save(response);
					rowsProcessed++;
				}


				queue.setRowsRead(processedRows + rowsProcessed);
				if (queue.getRowsRead() >= queue.getTotalRecords()) {
					queue.setStatus("Y"); 
				}
				queueRepository.save(queue);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}


	@Override
	public Optional<PersonalDetails> getPersonalDetailsById(Integer id) {

		if(id==null) {
			throw new IllegalArgumentException("Id id Null");
		}
		return proposerRepository.findById(id);
	}
}


























































//	@Override
//	@Scheduled(fixedDelay = 10000) // Every 10 seconds after last run completes
//	public void processPendingQueues() throws IOException {
//		int batchSize = 3; 
//		List<Queue> pendingQueues = queueRepository.findByStatus("N");
//
//		for (Queue queue : pendingQueues) {
//			int processedRows = queue.getRowsRead();
//			int totalRows = queue.getTotalRecords();
//			int rowsRemaining = totalRows - processedRows;
//			int rowsToProcess = Math.min(batchSize, rowsRemaining);
//
//			String filePath = queue.getFilePath();
//
//			File file = new File(filePath);
//			if (!file.exists()) {
//				System.err.println("File not found: " + filePath);
//				continue;
//			}
//
//			try (FileInputStream fis = new FileInputStream(file);
//					XSSFWorkbook workbook = new XSSFWorkbook(fis)) {
//
//				XSSFSheet sheet = workbook.getSheetAt(0);
//				int lastRowNum = sheet.getLastRowNum();
//
//				int rowsProcessed = 0;
//
//				for (int i = 0; i < rowsToProcess && (processedRows + i) <= lastRowNum; i++) {
//					int currentRowNum = processedRows + i;
//					XSSFRow row = sheet.getRow(currentRowNum);
//					if (row == null)
//
//						continue;
//
//					PersonalDetails p = new PersonalDetails();
//					ResponseExcel response = new ResponseExcel();
//					List<String> errors = new ArrayList<>();
//
//					String fullName = getCellString(row.getCell(0));
//					if (fullName == null || !fullName.matches("^[A-Za-z\\s]+$")) {
//						errors.add("Invalid Full Name");
//					} else {
//						p.setFullName(fullName);
//					}
//
//					String gender = getCellString(row.getCell(1));
//					if (gender == null || !(gender.equalsIgnoreCase("Male") || gender.equalsIgnoreCase("Female"))) {
//						errors.add("Invalid Gender");
//					} else {
//						p.setGender(gender);
//					}
//
//					String dob = getCellString(row.getCell(2));
//					if (dob == null || !dob.matches("\\d{4}-\\d{2}-\\d{2}")) {
//						errors.add("Invalid DOB");
//					} else {
//						p.setDateofBirth(dob);
//					}
//
//					String emailId = getCellString(row.getCell(3));
//					if (emailId == null || !emailId.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
//						errors.add("Invalid Email");
//					} else {
//						p.setEmailId(emailId);
//					}
//
//					String contact = getCellString(row.getCell(4));
//					if (contact == null || !contact.matches("^[6-9]\\d{9}$")) {
//						errors.add("Invalid Contact");
//					} else {
//						p.setContactDetails(contact);
//					}
//
//					String address = getCellString(row.getCell(5));
//					if (address == null || address.trim().isEmpty()) {
//						errors.add("Empty Address");
//					} else {
//						p.setAddress(address);
//					}
//
//					String city = getCellString(row.getCell(6));
//					if (city == null || city.trim().isEmpty()) {
//						errors.add("Empty City");
//					} else {
//						p.setCity(city);
//					}
//
//					String pincode = getCellString(row.getCell(7));
//					if (pincode == null || !pincode.matches("\\d{6}")) {
//						errors.add("Invalid Pincode");
//					} else {
//						p.setPincode(pincode);
//					}
//
//					String state = getCellString(row.getCell(8));
//					if (state == null || state.trim().isEmpty()) {
//						errors.add("Empty State");
//					} else {
//						p.setState(state);
//					}
//
//
//					if (errors.isEmpty()) {
//						proposerRepository.save(p);
//						response.setStatus(true);
//						response.setSuccess("Success");
//						response.setErrorFields("All fields valid");
//					} else {
//						response.setStatus(false);
//						response.setSuccess("Failed");
//						response.setErrorFields(String.join(", ", errors));
//					}
//
//					response.setQueueId(queue.getId());
//					excelRepository.save(response);
//					rowsProcessed++;
//				}
//
//
//				queue.setRowsRead(processedRows + rowsProcessed);
//
//				queue.setRowsRead(processedRows + rowsProcessed);
//				if (queue.getRowsRead() >= queue.getTotalRecords()) {
//					queue.setStatus("Y"); 
//				}
//				queueRepository.save(queue);
//
//			}catch (Exception e) {
//				System.err.println("Error processing file: " + filePath);
//				queue.setStatus("F"); 
//				queueRepository.save(queue);
//			}
//		}
//	}
//}
//
//











































































//	
//	
//	
//	@Override
//	@Scheduled(fixedDelay = 10000)
//	public void processPendingQueues() {
//		int batchSize = 3;
//
//		//List<Queue> pendingQueues = queueRepository.findByStatus("P"); // 
//		List<Queue> pendingQueues = queueRepository.findByStatusIn(Arrays.asList("N", "P"));
//		System.out.println("Pending queues: " + pendingQueues.size());
//
//		for (Queue queue : pendingQueues) {
//			int processedRows = queue.getRowsRead();
//			int totalRows = queue.getTotalRecords();
//			int rowsRemaining = totalRows - processedRows;
//			int rowsToProcess = Math.min(batchSize, rowsRemaining);
//
//			String filePath = queue.getFilePath();
//
//			
//			File file = new File(filePath);
//			if (!file.exists()) {
//				System.err.println(" File not found: " + filePath);
//				queue.setStatus("F"); 
//				queueRepository.save(queue);
//				continue;
//			}
//
//			try (FileInputStream fis = new FileInputStream(file);
//					XSSFWorkbook workbook = new XSSFWorkbook(fis)) {
//
//				XSSFSheet sheet = workbook.getSheetAt(0);
//				int lastRowNum = sheet.getLastRowNum();
//
//				int rowsProcessedThisBatch = 0;
//
//				for (int i = 0; i < rowsToProcess && (processedRows + i + 1) <= lastRowNum; i++) {
//					int currentRowNum = processedRows + i + 1; // +1 if row 0 is header
//					XSSFRow row = sheet.getRow(currentRowNum);
//					if (row == null) continue;
//
//					PersonalDetails p = new PersonalDetails();
//					ResponseExcel response = new ResponseExcel();
//					List<String> errors = new ArrayList<>();
//
//					String fullName = getCellString(row.getCell(0));
//					if (fullName == null || !fullName.matches("^[A-Za-z\\s]+$")) {
//						errors.add("Invalid Full Name");
//					} else {
//						p.setFullName(fullName);
//					}
//
//					String gender = getCellString(row.getCell(1));
//					if (gender == null || !(gender.equalsIgnoreCase("Male") || gender.equalsIgnoreCase("Female"))) {
//						errors.add("Invalid Gender");
//					} else {
//						p.setGender(gender);
//					}
//
//					String dob = getCellString(row.getCell(2));
//					if (dob == null || !dob.matches("\\d{4}-\\d{2}-\\d{2}")) {
//						errors.add("Invalid DOB");
//					} else {
//						p.setDateofBirth(dob);
//					}
//
//					String emailId = getCellString(row.getCell(3));
//					if (emailId == null || !emailId.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
//						errors.add("Invalid Email");
//					} else {
//						p.setEmailId(emailId);
//					}
//
//					String contact = getCellString(row.getCell(4));
//					if (contact == null || !contact.matches("^[6-9]\\d{9}$")) {
//						errors.add("Invalid Contact");
//					} else {
//						p.setContactDetails(contact);
//					}
//
//					String address = getCellString(row.getCell(5));
//					if (address == null || address.trim().isEmpty()) {
//						errors.add("Empty Address");
//					} else {
//						p.setAddress(address);
//					}
//
//					String city = getCellString(row.getCell(6));
//					if (city == null || city.trim().isEmpty()) {
//						errors.add("Empty City");
//					} else {
//						p.setCity(city);
//					}
//
//					String pincode = getCellString(row.getCell(7));
//					if (pincode == null || !pincode.matches("\\d{6}")) {
//						errors.add("Invalid Pincode");
//					} else {
//						p.setPincode(pincode);
//					}
//
//					String state = getCellString(row.getCell(8));
//					if (state == null || state.trim().isEmpty()) {
//						errors.add("Empty State");
//					} else {
//						p.setState(state);
//					}
//
//					if (errors.isEmpty()) {
//						proposerRepository.save(p);
//						response.setStatus(true);
//						response.setSuccess("Success");
//						response.setErrorFields("All fields valid");
//					} else {
//						response.setStatus(false);
//						response.setSuccess("Failed");
//						response.setErrorFields(String.join(", ", errors));
//					}
//
//					response.setQueueId(queue.getId());
//					excelRepository.save(response);
//					rowsProcessedThisBatch++;
//				}
//
//				processedRows += rowsProcessedThisBatch;
//				queue.setRowsRead(processedRows);
//
//				if (processedRows >= totalRows) {
//					queue.setStatus("Y"); 
//				}
//
//				queueRepository.save(queue);
//
//			} catch (Exception e) {
//				System.err.println(" Exception while processing: " + filePath);
//				e.printStackTrace();
//				queue.setStatus("F");
//				queueRepository.save(queue);
//			}
//		}
//		
//		
//	}





//	@Override
//	@Scheduled(fixedDelay = 10000)
//	public void processPendingQueues() {
//		int batchSize = 3;
//		List<Queue> pendingQueues = queueRepository.findByStatus("N"); 
//
//		for (Queue queue : pendingQueues) {
//			int processedRows = queue.getRowsRead();
//			int totalRows = queue.getTotalRecords();
//			int rowsRemaining = totalRows - processedRows;
//			int rowsToProcess = Math.min(batchSize, rowsRemaining);
//
//			String filePath = queue.getFilePath();
//
//			try (FileInputStream fis = new FileInputStream(filePath);
//					XSSFWorkbook workbook = new XSSFWorkbook(fis)) {
//
//				XSSFSheet sheet = workbook.getSheetAt(0);
//				int lastRowNum = sheet.getLastRowNum();
//
//				int rowsProcessedThisBatch = 0;
//
//				for (int i = 0; i < rowsToProcess && (processedRows + i) <= lastRowNum; i++) {
//					int currentRowNum = processedRows + i;
//					XSSFRow row = sheet.getRow(currentRowNum);
//					if (row == null) continue;
//
//					PersonalDetails p = new PersonalDetails();
//					ResponseExcel response = new ResponseExcel();
//					List<String> errors = new ArrayList<>();
//
//					String fullName = getCellString(row.getCell(0));
//					if (fullName == null || !fullName.matches("^[A-Za-z\\s]+$")) {
//						errors.add("Invalid Full Name");
//					} else {
//						p.setFullName(fullName);
//					}
//
//					String gender = getCellString(row.getCell(1));
//					if (gender == null || !(gender.equalsIgnoreCase("Male") || gender.equalsIgnoreCase("Female"))) {
//						errors.add("Invalid Gender");
//					} else {
//						p.setGender(gender);
//					}
//
//					String dob = getCellString(row.getCell(2));
//					if (dob == null || !dob.matches("\\d{4}-\\d{2}-\\d{2}")) {
//						errors.add("Invalid DOB");
//					} else {
//						p.setDateofBirth(dob);
//					}
//
//					String emailId = getCellString(row.getCell(3));
//					if (emailId == null || !emailId.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
//						errors.add("Invalid Email");
//					} else {
//						p.setEmailId(emailId);
//					}
//
//					String contact = getCellString(row.getCell(4));
//					if (contact == null || !contact.matches("^[6-9]\\d{9}$")) {
//						errors.add("Invalid Contact");
//					} else {
//						p.setContactDetails(contact);
//					}
//
//					String address = getCellString(row.getCell(5));
//					if (address == null || address.trim().isEmpty()) {
//						errors.add("Empty Address");
//					} else {
//						p.setAddress(address);
//					}
//
//					String city = getCellString(row.getCell(6));
//					if (city == null || city.trim().isEmpty()) {
//						errors.add("Empty City");
//					} else {
//						p.setCity(city);
//					}
//
//					String pincode = getCellString(row.getCell(7));
//					if (pincode == null || !pincode.matches("\\d{6}")) {
//						errors.add("Invalid Pincode");
//					} else {
//						p.setPincode(pincode);
//					}
//
//					String state = getCellString(row.getCell(8));
//					if (state == null || state.trim().isEmpty()) {
//						errors.add("Empty State");
//					} else {
//						p.setState(state);
//					}
//
//					if (errors.isEmpty()) {
//						proposerRepository.save(p);
//						response.setStatus(true);
//						response.setSuccess("Success");
//						response.setErrorFields("All fields valid");
//					} else {
//						response.setStatus(false);
//						response.setSuccess("Failed");
//						response.setErrorFields(String.join(", ", errors));
//					}
//
//					response.setQueueId(queue.getId());
//					excelRepository.save(response);
//					rowsProcessedThisBatch++;
//				}
//
//				// Update the processed rows count only by the actual rows processed in this batch
//				queue.setRowsRead(processedRows + rowsProcessedThisBatch);
//
//				if ((processedRows + rowsProcessedThisBatch) >= totalRows) {
//					queue.setStatus("Y");
//				}
//
//				queueRepository.save(queue);
//
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//	}
//}
//


























//
//	Integer batchSize=3;
//
//	@Override
//	@Scheduled(fixedDelay = 10000) // runs every 60 seconds when last execution ends
//	public void processPendingQueues() {
//
//		List<Queue> pendingQueues = queueRepository.findByStatus("N");
//
//
//		for (Queue queue : pendingQueues) {
//			int processedRows = queue.getRowsRead();
//			int totalRows = queue.getTotalRecords();
//			int rowsRemaining = totalRows - processedRows;
//			int rowsToProcess = Math.min(batchSize, rowsRemaining);
//
//			String filePath = queue.getFilePath();
//
//			File file = new File(filePath);
//			try (FileInputStream fis = new FileInputStream(queue.getFilePath());
//					XSSFWorkbook workbook = new XSSFWorkbook(fis)) {
//
//				XSSFSheet sheet = workbook.getSheetAt(0);
//
//				for (int i = 0; i < rowsToProcess; i++) {
//					int currentRow = processedRows + 1;
//					if (currentRow > sheet.getLastRowNum()) break;
//
//					XSSFRow row = sheet.getRow(currentRow);
//					if (row == null) 
//
//						continue;
//
//					PersonalDetails p = new PersonalDetails();
//					ResponseExcel response = new ResponseExcel();
//					List<String> errors = new ArrayList<>();
//
//					String fullName = getCellString(row.getCell(0));
//					if (fullName == null || !fullName.matches("^[A-Za-z\\s]+$")) {
//						errors.add("Invalid Full Name");
//					} else {
//						p.setFullName(fullName);
//					}
//
//					String gender = getCellString(row.getCell(1));
//					if (gender == null || !(gender.equalsIgnoreCase("Male") || gender.equalsIgnoreCase("Female"))) {
//						errors.add("Invalid Gender");
//					} else {
//						p.setGender(gender);
//					}
//
//					String dob = getCellString(row.getCell(2));
//					if (dob == null || !dob.matches("\\d{4}-\\d{2}-\\d{2}")) {
//						errors.add("Invalid DOB");
//					} else {
//						p.setDateofBirth(dob);
//					}
//
//					String emailId = getCellString(row.getCell(3));
//					if (emailId == null || !emailId.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
//						errors.add("Invalid Email");
//					} else {
//						p.setEmailId(emailId);
//					}
//
//					String contact = getCellString(row.getCell(4));
//					if (contact == null || !contact.matches("^[6-9]\\d{9}$")) {
//						errors.add("Invalid Contact");
//					} else {
//						p.setContactDetails(contact);
//					}
//
//					String address = getCellString(row.getCell(5));
//					if (address == null || address.trim().isEmpty()) {
//						errors.add("Empty Address");
//					} else {
//						p.setAddress(address);
//					}
//
//					String city = getCellString(row.getCell(6));
//					if (city == null || city.trim().isEmpty()) {
//						errors.add("Empty City");
//					} else {
//						p.setCity(city);
//					}
//
//					String pincode = getCellString(row.getCell(7));
//					if (pincode == null || !pincode.matches("\\d{6}")) {
//						errors.add("Invalid Pincode");
//					} else {
//						p.setPincode(pincode);
//					}
//
//					String state = getCellString(row.getCell(8));
//					if (state == null || state.trim().isEmpty()) {
//						errors.add("Empty State");
//					} else {
//						p.setState(state);
//					}
//
//					if (errors.isEmpty()) {
//						proposerRepository.save(p);
//						response.setStatus(true);
//						response.setSuccess("Success");
//						response.setErrorFields("All fields valid");
//					} else {
//						response.setStatus(false);
//						response.setSuccess("Failed");
//						response.setErrorFields(String.join(", ", errors));
//					}
//
//					response.setQueueId(queue.getId());
//					excelRepository.save(response);
//					processedRows++;
//				}
//
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//
//			queue.setRowsRead(processedRows);
//			if (processedRows >= totalRows) {
//				queue.setStatus("Y");
//			}
//
//			queueRepository.save(queue);
//		}
//	}
//
//}





































//		for (Queue queue : pendingQueues) {
//
//
//			int processedRows = queue.getRowsRead();
//			int totalRows = queue.getTotalRecords();
//
//			int rowsRemaining = totalRows - processedRows;
//
//			int rowsToProcess = Math.min(batchSize, rowsRemaining); 
//			
//			 try (FileInputStream fis = new FileInputStream(queue.getFilePath());
//		             XSSFWorkbook workbook = new XSSFWorkbook(fis)) {
//				 
//				 XSSFSheet sheet = workbook.getSheetAt(0);
//				 
//			 } catch (FileNotFoundException e) {
//				
//				e.printStackTrace();
//			} catch (IOException e) {
//				
//				e.printStackTrace();
//			}
//
//
//			for (int i = 0; i < rowsToProcess; i++) { 
//				 int currentRow = processedRows + 1;
//				 if (currentRow > sheet.getLastRowNum())
//					 
//					 break;
//				processedRows++;
//
//			}
//			queue.setRowsRead(processedRows);
//
//
//			if(processedRows >= totalRows) {
//
//				queue.setStatus("Y"); 
//			}
//
//			queueRepository.save(queue); 
//
//		}
//	}
//}

