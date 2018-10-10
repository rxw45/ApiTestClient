package com.testframework.core.client;


import static junit.framework.Assert.assertNotNull;

import org.junit.Test;

import com.testframework.core.client.RestAPIClient;
import com.testframework.core.client.RestAPIClientRequest;
import com.testframework.core.client.RestAPIClientWrapper;
import com.testframework.core.model.gen.SearchResponse;

import javax.ws.rs.core.MediaType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Roland
 * Date: 11/16/14
 * Time: 9:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestClient {
    @Test
    public void testFewRequests() throws Exception {
        OauthClientWrapper client = new OauthClientWrapper("api.twitter.com", 443, "/1.1/search/tweets.json", true, true);
        RestAPIClientRequest<Object> request = new RestAPIClientRequest<Object>();
        request.setAcceptType(MediaType.APPLICATION_JSON_TYPE);
        request.setRequestType(RestAPIClient.RequestType.GET);
        request.setDynamicPath("q=%23freebandnames&since_id=24012619984051000&max_id=250126199840518145&result_type=mixed&count=4");
        SearchResponse response = client.sendRequest(request, SearchResponse.class);
        assertNotNull(response);
 
    }
}

