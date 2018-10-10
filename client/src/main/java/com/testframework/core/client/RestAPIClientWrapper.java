package com.testframework.core.client;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriBuilder;
import javax.xml.ws.Response;

import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.testframework.core.client.RestAPIClient.RequestType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA. User: Roland Date: 11/11/14 Time: 11:05 AM
 * To change this template use File | Settings | File Templates.
 * Refer to TestClient for examples of how to use this class
 */
public class RestAPIClientWrapper {
	protected RestAPIClient apiClient;
	private String host;
	private Integer port;
	private String fixedPath;

	/**
	 * 
	 * @param host - host of the service provider
	 * @param port - port number
	 * @param fixedPath
	 * @param isRequestWrapped - does request needs root element wrapped for json request
	 * @param isResponseWrapped - does response have root element wrapped
	 */
	public RestAPIClientWrapper(String host, Integer port, String fixedPath, boolean isRequestWrapped, boolean isResponseWrapped) {
		this.host = host;
		this.port = port;
		this.fixedPath = fixedPath;
		apiClient = new RestAPIClient(isRequestWrapped, isResponseWrapped);
	}

    /**
     *
     * @param host - host of the service provider
     * @param fixedPath
     * @param isRequestWrapped - does request needs root element wrapped for json request
     * @param isResponseWrapped - does response have root element wrapped
     */
    public RestAPIClientWrapper(String host, String fixedPath, boolean isRequestWrapped, boolean isResponseWrapped) {
        this(host, 80, fixedPath, isRequestWrapped, isResponseWrapped);
    }

	/**
	 * Sends request
	 * @param apiClientRequest
	 */
    public void sendRequest(RestAPIClientRequest<?> apiClientRequest) throws RestAPIClientException {
    	if(apiClientRequest == null) {
    		throw new IllegalArgumentException("Null value supplied");
    	}
    	sendRequest(apiClientRequest.getDynamicPath(), apiClientRequest.getDynamicPathVariable(),
    			apiClientRequest.getQueryParameter(), apiClientRequest.getRequest(),
    			apiClientRequest.getContentType(), apiClientRequest.getRequestType(),
    			apiClientRequest.getExpectedStatusCode(), apiClientRequest.getHeaders(),
    			apiClientRequest.isExpectingEmptyResponseBody());
    }

    
    public  <T> T sendRequest(RestAPIClientRequest<?> apiClientRequest, Class<T> responseClass) throws RestAPIClientException {
    	if(apiClientRequest == null) {
    		throw new IllegalArgumentException("Null value supplied");
    	}
    	return sendRequest(apiClientRequest.getDynamicPath(), apiClientRequest.getDynamicPathVariable(),
    			apiClientRequest.getQueryParameter(), apiClientRequest.getRequest(), responseClass,
    			apiClientRequest.getContentType(), apiClientRequest.getAcceptType(), apiClientRequest.getRequestType(),
    			apiClientRequest.getExpectedStatusCode(), apiClientRequest.getHeaders());
    }

    public  <T> ResponseMaster<T> sendRequest(RestAPIClientRequest<?> apiClientRequest, Class<T> responseClass, boolean responseHeaderExpected) throws RestAPIClientException {
        if(apiClientRequest == null) {
            throw new IllegalArgumentException("Null value supplied");
        }
        return sendRequest(apiClientRequest.getDynamicPath(), apiClientRequest.getDynamicPathVariable(),
                apiClientRequest.getQueryParameter(), apiClientRequest.getRequest(), responseClass,
                apiClientRequest.getContentType(), apiClientRequest.getAcceptType(), apiClientRequest.getRequestType(),
                apiClientRequest.getExpectedStatusCode(), apiClientRequest.getHeaders(),responseHeaderExpected);
    }

	/**
	 * 
	 * @param path
	 *            - the path (excluding the fixed path, and query parameter)
	 * @param pathVariables
	 *            - Map of Key value which will be replaced in the path
	 * @param queryParameter
	 *            - Query request URL parameter to be supplied
	 * @param request
	 *            - request object
	 * @param contentType
	 *            - contentType of request body
	 * @param requestType
	 *            - type of the request
	 * @param expectedStatusCode
	 *            - expected status code from response
	 * @param headers
	 *            - request headers
	 * @param expectingEmptyResponseBody
	 *            - expected status code from response
	 */
	public void sendRequest(String path, Map<String, String> pathVariables,
			Map<String, Object> queryParameter, Object request,
			MediaType contentType, RequestType requestType,
			Integer expectedStatusCode, Map<String, Object> headers,
			boolean expectingEmptyResponseBody) throws RestAPIClientException{
		ClientResponse clientResponse = sendRequest(path, pathVariables,
				queryParameter, request, contentType, MediaType.WILDCARD_TYPE,
				requestType, expectedStatusCode, headers);
		if (expectingEmptyResponseBody) {
			if (clientResponse.hasEntity()) {
				clientResponse.bufferEntity();
				String errorMsg = getError(clientResponse);
                throw new RestAPIClientException("No response was expected, but got " + errorMsg);
			}
		}
	}

	private ClientResponse sendRequest(String path,
			Map<String, String> pathVariables,
			Map<String, Object> queryParameter, Object request,
			MediaType contentType, MediaType acceptMediaType,
			RequestType requestType, Integer expectedStatusCode,
			Map<String, Object> headers) throws RestAPIClientException{
		String url = getUrl(path, pathVariables, queryParameter);
        if(contentType == null) {
            throw new RestAPIClientException("contentType should not be null : contentType: " + contentType);
        }
        if(acceptMediaType == null) {
            throw new RestAPIClientException("acceptMediaType should not be null : acceptMediaType: " + acceptMediaType);
        }
        if(requestType == null) {
            throw new RestAPIClientException("requestType should not be null : requestType: " + requestType);
        }
		ClientResponse clientResponse = apiClient.sendRequest(url, request,
				contentType, acceptMediaType, requestType, headers);
		if (expectedStatusCode != null) {
			if( !expectedStatusCode.equals(Integer.valueOf(clientResponse.getStatus()))) {
                throw new RestAPIClientException("Did not receive expected status code(" + expectedStatusCode + "):, received (" + clientResponse.getStatus() + ")");
            }
		}
		return clientResponse;
	}

	/**
	 * 
	 * @param path
	 *            - the path (excluding the fixed path, and query parameter)
	 * @param pathVariables
	 *            - Map of Key value which will be replaced in the path
	 * @param queryParameter
	 *            - Query request URL parameter to be supplied
	 * @param request
	 *            - request object
	 * @param responseClass
	 *            - Expected response class
	 * @param contentType
	 *            - contentType of request body
	 * @param acceptMediaType
	 *            - accept Type of response body
	 * @param requestType
	 *            - type of the request
	 * @param expectedStatusCode
	 *            - expected status code from response
	 * @param headers
	 *            - request headers
	 * @return
	 */
	public <T> T sendRequest(String path, Map<String, String> pathVariables,
			Map<String, Object> queryParameter, Object request,
			Class<T> responseClass, MediaType contentType,
			MediaType acceptMediaType, RequestType requestType,
			Integer expectedStatusCode, Map<String, Object> headers) throws RestAPIClientException {
        ResponseMaster<T> responseMaster = sendRequest(path, pathVariables,
                queryParameter, request,
                responseClass, contentType,
                acceptMediaType, requestType,
                expectedStatusCode, headers, false);
        return responseMaster.getResponseObject();
	}

    public <T> ResponseMaster<T> sendRequest(String path, Map<String, String> pathVariables,
                             Map<String, Object> queryParameter, Object request,
                             Class<T> responseClass, MediaType contentType,
                             MediaType acceptMediaType, RequestType requestType,
                             Integer expectedStatusCode, Map<String, Object> headers, boolean responseHeaderExpected) throws RestAPIClientException {
        if(responseClass == null) {
            throw new RestAPIClientException("Response Class should not be null");
        }
        ResponseMaster<T> result = null;
        T resultEntity = null;
        MultivaluedMap<String, String> resultHeaders = null;
        ClientResponse clientResponse = sendRequest(path, pathVariables,
                queryParameter, request, contentType, acceptMediaType,
                requestType, expectedStatusCode, headers);
        clientResponse.bufferEntity();
        try {
            resultEntity = clientResponse.getEntity(responseClass);
            if (responseHeaderExpected) {
                resultHeaders = clientResponse.getHeaders();
            }
            result = new ResponseMaster<T>(resultEntity, resultHeaders);
        } catch (ClientHandlerException ex) {
            throw new RestAPIClientException("Unable to unmarshal response, expected :"
                    + responseClass.getCanonicalName() + " received message: "
                    + getError(clientResponse) + ex.getMessage());
        } catch (WebApplicationException ex) {
            throw new RestAPIClientException("Unable to unmarshal response, expected :"
                    + responseClass.getCanonicalName() + " received message : "
                    + getError(clientResponse) + ex.getMessage());
        } finally {
            clientResponse.close();
        }
        return result;
    }



	private String getError(ClientResponse clientResponse) {
        try {
            clientResponse.getEntityInputStream().reset();
            return clientResponse.getEntity(String.class);
        } catch (Exception e) {
            return null;
        }
	}

	private String getUrl(String pathResourceUrl,
			Map<String, String> webResourceParameters,
			Map<String, Object> queryParam) {
		String webResourceUrl = pathResourceUrl;
		if (pathResourceUrl != null && webResourceParameters != null) {
			for (Map.Entry<String, String> entry : webResourceParameters
					.entrySet()) {
				webResourceUrl = webResourceUrl.replace("$" + entry.getKey(),
						entry.getValue());
			}
		}

		UriBuilder uriBuilder = UriBuilder.fromUri(fixedPath);
		uriBuilder.scheme("http");
		uriBuilder.host(host);
		uriBuilder.port(port);
		if (webResourceUrl != null) {
			uriBuilder.path(webResourceUrl);
		}
		if (queryParam != null) {
			for (Map.Entry<String, Object> entry : queryParam.entrySet()) {
				uriBuilder.queryParam(entry.getKey(), entry.getValue());
			}
		}

		String url = uriBuilder.build().toString();
		return url;
	}

    public static class ResponseMaster<T> {
        private T responseObject;
        private Map<String, List<String>> headers;

        public ResponseMaster(T responseObject, Map<String, List<String>> headers) {
            this.responseObject = responseObject;
            this.headers = headers;
        }

        public T getResponseObject() {
            return responseObject;
        }

        public Map<String, List<String>> getHeaders() {
            return headers;
        }
    }
}
