package com.democrud.service;

import org.springframework.stereotype.Component;

@Component
public class WheatherService {


//	public static final String apiKey="5227e82b36d2253b4f3a20f7e722f86e";
//
//
//	public static final String API="http://api.weatherstack.com/current?access_key=API_KEY&query=CITY";
//
//	//class hai jo http request laage ke  response dete hai
//	@Autowired
//	private RestTemplate restTemplate;
//
//
//	
//                                                                                                                                                                                                                                                                                                                 
//	public WheatherResponse getWheather(String city) {
//
//		String finalAPI = API.replace("CITY", city).replace("API_KEY", apiKey);
//		
//
//		ResponseEntity<WheatherResponse> response=restTemplate.exchange(finalAPI, HttpMethod.GET,null,WheatherResponse.class); //WheatherResponse.class is used to convert  json to java
//
//		//Yeh Post ka request hai jo apn kar sakhte hai 
//		ResponseEntity<WheatherResponse> response1= restTemplate.exchange(finalAPI, HttpMethod.POST,null,WheatherResponse.class);
//		
//		WheatherResponse body =response.getBody();
//		return body;
//	}
//
//	
//	

                                                        
}
