package com.testframework.core.common;

import javax.ws.rs.core.MediaType;

import com.testframework.core.client.OauthClientWrapper;
import com.testframework.core.client.RestAPIClient;
import com.testframework.core.client.RestAPIClientException;
import com.testframework.core.client.RestAPIClientRequest;
import com.testframework.core.client.RestAPIClientWrapper;
import com.testframework.core.model.gen.*;
import com.testframework.core.model.gen.SearchResponse;

public class CommonUtils {
	
	public static String getUserToken(String userName, String pass) throws RestAPIClientException
	{
		   RestAPIClientWrapper client = new RestAPIClientWrapper("https://integration-tests-challenge.herokuapp.com", 443, "/login", false, false);
	       RestAPIClientRequest<Object> request = new RestAPIClientRequest<Object>();
	       request.setAcceptType(MediaType.APPLICATION_JSON_TYPE);
	       request.setRequestType(RestAPIClient.RequestType.POST);
	       LoginRequest login = new LoginRequest();
	       login.setUsername(userName);
	       login.setPassword(pass);
	       request.setRequest(login);
	       LoginResponse response = client.sendRequest(request, LoginResponse.class);
		   return response.getToken();	       
	}
	

	public static CreateRecordResponse createRecord(String name, String module, String token) throws RestAPIClientException
	{
		 
		   RestAPIClientWrapper client = new RestAPIClientWrapper("https://integration-tests-challenge.herokuapp.com", 443, "/record", false, false);
	       RestAPIClientRequest<Object> request = new RestAPIClientRequest<Object>();
	       request.setAcceptType(MediaType.APPLICATION_JSON_TYPE);
	       request.setRequestType(RestAPIClient.RequestType.POST);
	       CreateRecordRequest create = new CreateRecordRequest();
	       create.setName(name);
	       create.setModule(module);
	       create.setToken(token);
	       request.setRequest(create);
	       CreateRecordResponse response = client.sendRequest(request, CreateRecordResponse.class);
		   return response;
		
	}
	
	public static GetAllRecordsResponse getAllRecords(String token) throws RestAPIClientException
	{
		 
		   RestAPIClientWrapper client = new RestAPIClientWrapper("https://integration-tests-challenge.herokuapp.com", 443, "/records?"+token, false, false);
	       RestAPIClientRequest<Object> request = new RestAPIClientRequest<Object>();
	       request.setAcceptType(MediaType.APPLICATION_JSON_TYPE);
	       request.setRequestType(RestAPIClient.RequestType.GET);
	       GetAllRecordsResponse response = client.sendRequest(request, GetAllRecordsResponse.class);
		   return response;
		
	}
	
	public static GetRecordResponse getRecordById(String token,String id) throws RestAPIClientException
	{
		 
		   RestAPIClientWrapper client = new RestAPIClientWrapper("https://integration-tests-challenge.herokuapp.com", 443, "/record?"+token+"&"+id, false, false);
	       RestAPIClientRequest<Object> request = new RestAPIClientRequest<Object>();
	       request.setAcceptType(MediaType.APPLICATION_JSON_TYPE);
	       request.setRequestType(RestAPIClient.RequestType.GET);
	       GetRecordResponse response = client.sendRequest(request, GetRecordResponse.class);
		   return response;
		
	}
}
