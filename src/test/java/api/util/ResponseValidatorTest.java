package api.util;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Map;
import java.util.function.Predicate;

// This interface is a stub for api.client.ApiResponse, which is not provided in the context.
// The methods are inferred from its usage in ResponseValidator.java
interface ApiResponse {
    int getStatusCode();
    boolean isSuccess();
    String getBody();
    Map<String, String> getHeaders();
    String getHeader(String name);
    long getExecutionTimeMillis();
}

public class ResponseValidatorTest {

    // Mock implementation of ApiResponse for testing purposes
    private static class MockApiResponse implements ApiResponse {
        private final int statusCode;
        private final String body;
        private final Map<String, String> headers;
        private final long executionTime;

        public MockApiResponse(int statusCode, String body, Map<String, String> headers, long executionTime) {
            this.statusCode = statusCode;
            this.body = body;
            this.headers = headers;
            this.executionTime = executionTime;
        }

        @Override
        public int getStatusCode() { return statusCode; }

        @Override
        public boolean isSuccess() { return statusCode >= 200 && statusCode < 300; }

        @Override
        public String getBody() { return body; }

        @Override
        public Map<String, String> getHeaders() { return headers; }

        @Override
        public String getHeader(String name) { return headers.get(name); }

        @Override
        public long getExecutionTimeMillis() { return executionTime; }
    }

    @Test
    public void testOf() {
        ApiResponse response = new MockApiResponse(200, "", Map.of(), 0);
        Assert.assertNotNull(ResponseValidator.of(response));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testOfNullResponse() {
        ResponseValidator.of(null);
    }

    @Test
    public void testStatusIs_Success() {
        ApiResponse response = new MockApiResponse(200, "", Map.of(), 0);
        ResponseValidator.of(response).statusIs(200);
    }

    @Test(expectedExceptions = AssertionError.class)
    public void testStatusIs_Failure() {
        ApiResponse response = new MockApiResponse(404, "", Map.of(), 0);
        ResponseValidator.of(response).statusIs(200);
    }

    @Test
    public void testStatusIsSuccess_Success() {
        ApiResponse response = new MockApiResponse(204, "", Map.of(), 0);
        ResponseValidator.of(response).statusIsSuccess();
    }

    @Test(expectedExceptions = AssertionError.class)
    public void testStatusIsSuccess_Failure() {
        ApiResponse response = new MockApiResponse(400, "", Map.of(), 0);
        ResponseValidator.of(response).statusIsSuccess();
    }

    @Test
    public void testStatusBetween_Success() {
        ApiResponse response = new MockApiResponse(201, "", Map.of(), 0);
        ResponseValidator.of(response).statusBetween(200, 299);
    }

    @Test(expectedExceptions = AssertionError.class)
    public void testStatusBetween_Failure() {
        ApiResponse response = new MockApiResponse(300, "", Map.of(), 0);
        ResponseValidator.of(response).statusBetween(200, 299);
    }

    @Test
    public void testBodyNotEmpty_Success() {
        ApiResponse response = new MockApiResponse(200, "content", Map.of(), 0);
        ResponseValidator.of(response).bodyNotEmpty();
    }

    @Test(expectedExceptions = AssertionError.class)
    public void testBodyNotEmpty_Failure_Null() {
        ApiResponse response = new MockApiResponse(200, null, Map.of(), 0);
        ResponseValidator.of(response).bodyNotEmpty();
    }

    @Test(expectedExceptions = AssertionError.class)
    public void testBodyNotEmpty_Failure_Empty() {
        ApiResponse response = new MockApiResponse(200, "", Map.of(), 0);
        ResponseValidator.of(response).bodyNotEmpty();
    }

    @Test(expectedExceptions = AssertionError.class)
    public void testBodyNotEmpty_Failure_Blank() {
        ApiResponse response = new MockApiResponse(200, "   ", Map.of(), 0);
        ResponseValidator.of(response).bodyNotEmpty();
    }

    @Test
    public void testBodyContains_Success() {
        ApiResponse response = new MockApiResponse(200, "hello world", Map.of(), 0);
        ResponseValidator.of(response).bodyContains("world");
    }

    @Test(expectedExceptions = AssertionError.class)
    public void testBodyContains_Failure() {
        ApiResponse response = new MockApiResponse(200, "hello", Map.of(), 0);
        ResponseValidator.of(response).bodyContains("world");
    }

    @Test
    public void testBodyNotContains_Success() {
        ApiResponse response = new MockApiResponse(200, "hello", Map.of(), 0);
        ResponseValidator.of(response).bodyNotContains("world");
    }

    @Test(expectedExceptions = AssertionError.class)
    public void testBodyNotContains_Failure() {
        ApiResponse response = new MockApiResponse(200, "hello world", Map.of(), 0);
        ResponseValidator.of(response).bodyNotContains("world");
    }

    @Test
    public void testBodyMatches_Success() {
        ApiResponse response = new MockApiResponse(200, "12345", Map.of(), 0);
        Predicate<String> isNumeric = s -> s.matches("\\d+");
        ResponseValidator.of(response).bodyMatches(isNumeric, "body should be numeric");
    }

    @Test(expectedExceptions = AssertionError.class)
    public void testBodyMatches_Failure() {
        ApiResponse response = new MockApiResponse(200, "abc", Map.of(), 0);
        Predicate<String> isNumeric = s -> s.matches("\\d+");
        ResponseValidator.of(response).bodyMatches(isNumeric, "body should be numeric");
    }
}
