package com.democrud.helper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.democrud.entity.PersonalDetails;

public class Helper {


	public static String[] HEADERS= {

			//"personalId",
			"fullName",
			"gender",
			"profession",
			"ocupation",
			"maritalStatus",
			"emailId",
			"contactDetails",
			"address",
			"area",
			"town",
			"state"

	};

	public static String SHEET_NAME="personal_details_data";


	public static ByteArrayInputStream dataToExcel(List<PersonalDetails> list) throws IOException {


		
		Workbook workbook = new XSSFWorkbook();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {

			Sheet sheet=workbook.createSheet(SHEET_NAME);

			Row row=sheet.createRow(0);			        // Header row banega																	//baneag row 

			for(int i =0 ; i<HEADERS.length; i++) {  //set krna hai row shell ko

				Cell cell=row.createCell(i);
				cell.setCellValue(HEADERS[i]);
			}


			int  rowIndex=1 ;

			for(PersonalDetails p:list) {

				Row dataRow=sheet.createRow(rowIndex);

				rowIndex++;      // increase karta jyga  row 

				//dataRow.createCell(0).setCellValue(p.getPersonalId());  	//values row 
				dataRow.createCell(1).setCellValue(p.getFullName());
				dataRow.createCell(2).setCellValue(p.getGender());
				dataRow.createCell(3).setCellValue(p.getProfession());
				dataRow.createCell(4).setCellValue(p.getOcupation());
				dataRow.createCell(5).setCellValue(p.getMaritalStatus());
				dataRow.createCell(6).setCellValue(p.getEmailId());	
				dataRow.createCell(7).setCellValue(p.getContactDetails());										
				dataRow.createCell(8).setCellValue(p.getAddress());										
				dataRow.createCell(9).setCellValue(p.getArea());										
				dataRow.createCell(10).setCellValue(p.getTown());										
				dataRow.createCell(11).setCellValue(p.getState());										


			}


			workbook.write(out);   // pura data out wale variable me hai 

			return new ByteArrayInputStream(out.toByteArray());

		}catch (IOException e) {

			e.printStackTrace();
			System.out.print("Failed To Export  Data Excel and retrive db data in excel");

			return null;
		}finally {

			workbook.close();
			out.close();
		}
	}

	
	
	
	
	
	
	
	
	
	
	
	
//	public static boolean checkExcelFormat(MultipartFile file) {
//
//
//		String contentType=file.getContentType();
//
//		if(contentType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
//
//			return true ;
//		}
//		else {
//			return false;
//		}
//
//	}
//	
	
//	public static List  <PersonalDetails> converExcelToList(InputStream is){
//
//
//		List<PersonalDetails> list = new ArrayList<>();
//
//		try {
//
//			XSSFWorkbook workbook = new XSSFWorkbook(is);
//
//			XSSFSheet sheet= workbook.getSheet("PersonalDetails.xlsx");
//
//			int rowNumber=0;
//
//			Iterator<Row> iterator = sheet.iterator();
//
//			while (iterator.hasNext()) {
//				Row row = iterator.next();
//
//				if(rowNumber==0) {
//
//					rowNumber++;
//					continue;
//				}
//
//				Iterator<Cell> cells = row.iterator();
//
//				int cid =0;
//
//				PersonalDetails p = new PersonalDetails();
//
//				while(iterator.hasNext()) {
//
//
//					Cell cell = cells.next();
//
//					double numericCellValue = cell.getNumericCellValue(); // here double is defined for casting it 
//
//					System.err.println("------"+numericCellValue);
//
//					String stringCellValue = cell.getStringCellValue();
//					System.err.println("------"+stringCellValue);
//					System.out.println("Helper.converExcelToList()");
//					switch(cid) {
//
//
//					case 0 :
//						p.setPersonalId((int) cell.getNumericCellValue());
//						System.err.println(cell.getNumericCellValue());
//
//						break;
//
//					case 1:
//						p.setFullName(cell.getStringCellValue());
//						break;
//
//					case 3:
//						p.setGender(cell.getStringCellValue());
//						break;
//					case 4 :
//						p.setDateofBirth(cell.getStringCellValue());
//						break;
//					default:
//						break;
//					}
//
//					cid++;
//				}
//				list.add(p);
//
//			}
//		}catch (Exception e) {
//
//			e.printStackTrace();
//		}
//
//		return list;
//	}


}