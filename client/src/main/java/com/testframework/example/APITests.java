package com.testframework.example;

import org.testng.asserts.SoftAssert;

import com.testframework.core.client.RestAPIClientException;
import com.testframework.core.common.CommonUtils;
import com.testframework.core.model.gen.CreateRecordResponse;
import com.testframework.core.model.gen.GetAllRecordsResponse;
import com.testframework.core.model.gen.GetRecordResponse;

public class APITests {

	public void TestCreateRecord() throws RestAPIClientException
	{
		String userName = "salesforce@example.com";
		String pass = "example";
		String name = "Patrick";
		String module = "Account"; 
		String token = CommonUtils.getUserToken(userName, pass);
		GetAllRecordsResponse allprev = CommonUtils.getAllRecords(token);
		int countprev= allprev.getRecords().size();
		CreateRecordResponse resp =  CommonUtils.createRecord(name, module, token);
		GetAllRecordsResponse all = CommonUtils.getAllRecords(token);
		int countafter = all.getRecords().size();
		boolean found = false;
		SoftAssert soft = new SoftAssert();
		for(GetAllRecordsResponse.Record record : all.getRecords())
		{
			if(record.getId().equals(resp.getId()))
			{
				found = true;
				soft.assertEquals(record.getModule(), resp.getModule(),"this record was found, but module seems wrong:" + record.getModule() + " actual: "+ resp.getModule());
				soft.assertEquals(record.getName(), resp.getName(),"this record was found, but name seems wrong:" + record.getName() + " actual: "+ resp.getName());
			}			
		}
		soft.assertEquals(found, true, "this record is not found from GelALLRecords API");
		soft.assertEquals(countprev, countafter, "the count of records should be increased by 1 after creation");
		soft.assertAll();
	}
	
	
	public void TestGetRecordByID() throws RestAPIClientException
	{
		String userName = "dynamics@example.com";
		String pass = "example";
		String name = "Patrick";
		String module = "Account"; 
		String token = CommonUtils.getUserToken(userName, pass);
		CreateRecordResponse resp =  CommonUtils.createRecord(name, module, token);
		String id = resp.getId();
		GetRecordResponse getRecordbyId = CommonUtils.getRecordById(token, id);
		SoftAssert soft = new SoftAssert();
		soft.assertEquals(getRecordbyId.getModule(), resp.getModule(),"this record was found, but module seems wrong:" + getRecordbyId.getModule() + " actual: "+ resp.getModule());
		soft.assertEquals(getRecordbyId.getName(), resp.getName(),"this record was found, but name seems wrong:" + getRecordbyId.getName() + " actual: "+ resp.getName());			
		soft.assertAll();
	}
}
