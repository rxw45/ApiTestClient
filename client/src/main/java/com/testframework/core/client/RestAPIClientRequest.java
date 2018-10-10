package com.testframework.core.client;

import javax.ws.rs.core.MediaType;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Roland
 * Date: 11/11/14
 * Time: 1:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class RestAPIClientRequest<T> {
    private MediaType contentType = MediaType.APPLICATION_XML_TYPE;
    private MediaType acceptType = MediaType.APPLICATION_XML_TYPE;
    private RestAPIClient.RequestType requestType = RestAPIClient.RequestType.GET;
    private String dynamicPath;
    private Map<String, String> dynamicPathVariable;
    private Map<String, Object> queryParameter;
    private T request;
    private Integer expectedStatusCode = 200;
    private Map<String, Object> headers;
    private boolean expectingEmptyResponseBody = true;

    public boolean isExpectingEmptyResponseBody() {
		return expectingEmptyResponseBody;
	}

	public void setExpectingEmptyResponseBody(boolean expectingEmptyResponseBody) {
		this.expectingEmptyResponseBody = expectingEmptyResponseBody;
	}

	public MediaType getContentType() {
        return contentType;
    }

    public void setContentType(MediaType contentType) {
        this.contentType = contentType;
    }

    public MediaType getAcceptType() {
        return acceptType;
    }

    public void setAcceptType(MediaType acceptType) {
        this.acceptType = acceptType;
    }

    public RestAPIClient.RequestType getRequestType() {
        return requestType;
    }

    public void setRequestType(RestAPIClient.RequestType requestType) {
        this.requestType = requestType;
    }

    public String getDynamicPath() {
        return dynamicPath;
    }

    public void setDynamicPath(String dynamicPath) {
        this.dynamicPath = dynamicPath;
    }

    public Map<String, String> getDynamicPathVariable() {
        return dynamicPathVariable;
    }

    public void setDynamicPathVariable(Map<String, String> dynamicPathVariable) {
        this.dynamicPathVariable = dynamicPathVariable;
    }

    public Map<String, Object> getQueryParameter() {
        return queryParameter;
    }

    public void setQueryParameter(Map<String, Object> queryParameter) {
        this.queryParameter = queryParameter;
    }

    public T getRequest() {
        return request;
    }

    public void setRequest(T request) {
        this.request = request;
    }

    public Integer getExpectedStatusCode() {
        return expectedStatusCode;
    }

    public void setExpectedStatusCode(Integer expectedStatusCode) {
        this.expectedStatusCode = expectedStatusCode;
    }

    public Map<String, Object> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, Object> headers) {
        this.headers = headers;
    }
}
