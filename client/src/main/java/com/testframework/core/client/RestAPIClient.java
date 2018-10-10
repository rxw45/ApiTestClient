package com.testframework.core.client;

import java.util.Map;

import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.jaxrs.JacksonJaxbJsonProvider;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.SerializationConfig;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.LoggingFilter;
import com.sun.jersey.api.json.JSONConfiguration;

/**
 * Client is the HTTP client to invoke HTTP request to any Rest web services
 * Created with IntelliJ IDEA.
 * User: roland
 * Date: 11/11/14
 * Time: 1:07 AM
 * To change this template use File | Settings | File Templates.
 */
public class RestAPIClient {

    public static enum RequestType {
        GET,
        POST,
        PUT,
        DELETE;
    }

    public static class TestJacksonJaxbProviderWrapped extends JacksonJaxbJsonProvider{
        public TestJacksonJaxbProviderWrapped() {
            super();
            configure(SerializationConfig.Feature.WRAP_ROOT_VALUE, true);
            configure(DeserializationConfig.Feature.UNWRAP_ROOT_VALUE, true);
        }
    }
    
    public static class TestJacksonJaxbProviderUnwrapped extends JacksonJaxbJsonProvider{
        public TestJacksonJaxbProviderUnwrapped() {
            super();
            configure(SerializationConfig.Feature.WRAP_ROOT_VALUE, false);
            configure(DeserializationConfig.Feature.UNWRAP_ROOT_VALUE, false);
        }
    }
    
    public static class TestJacksonJaxbProviderUnwrappedRequest extends JacksonJaxbJsonProvider{
        public TestJacksonJaxbProviderUnwrappedRequest() {
            super();
            configure(SerializationConfig.Feature.WRAP_ROOT_VALUE, false);
            configure(DeserializationConfig.Feature.UNWRAP_ROOT_VALUE, true);
        }
    }
    
    public static class TestJacksonJaxbProviderUnwrappedResponse extends JacksonJaxbJsonProvider{
        public TestJacksonJaxbProviderUnwrappedResponse() {
            super();
            configure(SerializationConfig.Feature.WRAP_ROOT_VALUE, true);
            configure(DeserializationConfig.Feature.UNWRAP_ROOT_VALUE, false);
        }
    }

    protected Client client;

    /**
     *
     * @param isRequestWrapped determines if the Request is wrapped with root element in JSON
     * @param isResponseWrapped set to true if the Response is going to be wrapped with root element in JSON
     */
    public RestAPIClient(boolean isRequestWrapped, boolean isResponseWrapped) {
        ClientConfig clientConfig = new DefaultClientConfig();
        clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
        if(isRequestWrapped && isResponseWrapped) {
            clientConfig.getClasses().add(TestJacksonJaxbProviderWrapped.class);
        } else if (!isRequestWrapped && !isResponseWrapped) {
            clientConfig.getClasses().add(TestJacksonJaxbProviderUnwrapped.class);
        } else if(!isRequestWrapped) {
            clientConfig.getClasses().add(TestJacksonJaxbProviderUnwrappedRequest.class);
        } else {
            clientConfig.getClasses().add(TestJacksonJaxbProviderUnwrappedResponse.class);
        }
        client = Client.create(clientConfig);
    }

    /**
     * Sends request to the URL provided to invoke a service call
     * @param url - service end point
     * @param request - request object to be marshalled in XML/JSON
     * @param contentType - contentType of request to be marshalled in
     * @param acceptMediaType - Response type desired
     * @param requestType - Type of the request (GET, POST, PUT, DELETE)
     * @param headers - additional headers of the request
     * @return
     */
    public ClientResponse sendRequest(String url, Object request, MediaType contentType, MediaType acceptMediaType, RequestType requestType, Map<String, Object> headers) {
        WebResource resource = client.resource(url);
        client.addFilter(new LoggingFilter(System.out));
        WebResource.Builder builder = resource.accept(acceptMediaType).type(contentType);
        if(headers != null && !headers.isEmpty()) {
            for(Map.Entry<String, Object> header : headers.entrySet()) {
                builder.header(header.getKey(), header.getValue());
            }
        }

        ClientResponse clientResponse = null;
        switch (requestType) {
            case GET:
                clientResponse = builder.get(ClientResponse.class);
                break;
            case POST:
                clientResponse = builder.post(ClientResponse.class, request);
                break;
            case PUT:
                if(request != null) {
                    clientResponse = builder.put(ClientResponse.class, request);
                } else {
                    clientResponse = builder.put(ClientResponse.class);
                }
                break;
            case DELETE:
                if(request != null) {
                    clientResponse = builder.delete(ClientResponse.class, request);
                } else {
                    clientResponse = builder.delete(ClientResponse.class);
                }
                break;
        }
        return clientResponse;
    }
}
