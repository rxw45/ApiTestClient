package com.testframework.core.client;

/**
 * Created with IntelliJ IDEA.
 * User: Roland
 * Date: 11/11/14
 * Time: 5:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class RestAPIClientException extends Exception {

    public RestAPIClientException(Exception ex) {
        super(ex);
    }

    public RestAPIClientException(String message) {
        super(message);
    }

    public RestAPIClientException(Exception ex, String message) {
        super(message, ex);
    }
}
