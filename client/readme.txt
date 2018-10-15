
This test framework is designed to demo how to do  back-end API tests.


There are four packages as below:


1) com.testframework.core.client : This package contains the classes which handle the http request and client, you may customize those request regarding the specific requirements.

2)com.testframework.core.common : This package is designed to contain the util class, which handle the specific http request. Since each request will be wrapped with a method, they can be repeatedly used by different tests.

3)com.testframework.core.model.gen : This package is designed to contain the schema classes for request (POST type) and response. When the API request/response were sent/receive from the REST sever, those schema classes were used to serialized/deserialized to/from json or xml.

4)com.testframework.example: This package is designed to hold all test NG based tests. 



To implement an elegant API level tests, the following things should be considered:

1) The tests should be data independent, which means you can run the same tests in QA.Stage or PROD. To achieve this goal, you should consider the relationship between the APIs, maybe you can use some API to generate some testing data, and verify by some other APIs, and finally, remove the generated data by another APIs.  Or, you might have the opportunity to use the existing data. All this will be depend on the specific use case of the APIs and test case.

2) The good API tests should be simulated from real customer use cases. 

3) Try to avoid obtaining the test data from DB query, and try to obtain from API call instead, and this is especially important for PROD. 

