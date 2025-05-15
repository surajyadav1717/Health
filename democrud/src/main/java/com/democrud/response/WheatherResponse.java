package com.democrud.response;

import java.util.ArrayList;

import javax.tools.DocumentationTool.Location;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.security.auth.message.callback.PrivateKeyCallback.Request;

public class WheatherResponse {

	private Request request;
	private Location location;
	private Current current;

	
	
	public Request getRequest() {
		return request;
	}



	public void setRequest(Request request) {
		this.request = request;
	}



	public Location getLocation() {
		return location;
	}



	public void setLocation(Location location) {
		this.location = location;
	}



	public Current getCurrent() {
		return current;
	}



	public void setCurrent(Current current) {
		this.current = current;
	}



	public class Current{
		
		private int temperature;
		
		@JsonProperty("weather_descriptions") // yeh change define karta hai from weather_descriptions to weatherDescriptions 
		private ArrayList<String> weatherDescriptions;
	
		private int feelslike;

		public int getTemperature() {
			return temperature;
		}

		public void setTemperature(int temperature) {
			this.temperature = temperature;
		}

		public ArrayList<String> getWeatherDescriptions() {
			return weatherDescriptions;
		}

		public void setWeatherDescriptions(ArrayList<String> weatherDescriptions) {
			this.weatherDescriptions = weatherDescriptions;
		}

		public int getFeelslike() {
			return feelslike;
		}

		public void setFeelslike(int feelslike) {
			this.feelslike = feelslike;
		}
		
		
		
	}
	

}
