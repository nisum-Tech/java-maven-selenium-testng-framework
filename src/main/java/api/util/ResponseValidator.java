package api.util;

import api.client.ApiResponse;

import java.util.function.Predicate;

/**
 * Fluent helper for asserting conditions on an {@link ApiResponse} without
 * coupling the main API layer to a specific test framework (TestNG / JUnit).
 *
 * <p>Throws {@link AssertionError} on any failed expectation so TestNG
 * picks it up as a test failure automatically.</p>
 *
 * <pre>
 *   ResponseValidator.of(response)
 *       .statusIs(200)
 *       .bodyContains("first_name")
 *       .headerPresent("content-type")
 *       .respondedWithin(2000);
 * </pre>
 */
public class ResponseValidator {

    private final ApiResponse response;

    private ResponseValidator(ApiResponse response) {
        this.response = response;
    }

    public static ResponseValidator of(ApiResponse response) {
        if (response == null) throw new IllegalArgumentException("ApiResponse must not be null");
        return new ResponseValidator(response);
    }

    // ── Status ───────────────────────────────────────────────────────────────

    public ResponseValidator statusIs(int expected) {
        int actual = response.getStatusCode();
        if (actual != expected) {
            fail("Expected status " + expected + " but got " + actual
                    + "\nBody: " + truncate(response.getBody()));
        }
        return this;
    }

    public ResponseValidator statusIsSuccess() {
        if (!response.isSuccess()) {
            fail("Expected 2xx status but got " + response.getStatusCode()
                    + "\nBody: " + truncate(response.getBody()));
        }
        return this;
    }

    public ResponseValidator statusBetween(int from, int to) {
        int code = response.getStatusCode();
        if (code < from || code > to) {
            fail("Expected status between " + from + " and " + to + " but got " + code);
        }
        return this;
    }

    // ── Body ─────────────────────────────────────────────────────────────────

    public ResponseValidator bodyNotEmpty() {
        String body = response.getBody();
        if (body == null || body.isBlank()) {
            fail("Expected a non-empty response body");
        }
        return this;
    }

    public ResponseValidator bodyContains(String substring) {
        String body = response.getBody();
        if (body == null || !body.contains(substring)) {
            fail("Expected body to contain \"" + substring + "\" but body was: " + truncate(body));
        }
        return this;
    }

    public ResponseValidator bodyNotContains(String substring) {
        String body = response.getBody();
        if (body != null && body.contains(substring)) {
            fail("Expected body NOT to contain \"" + substring + "\"");
        }
        return this;
    }

    public ResponseValidator bodyMatches(Predicate<String> predicate, String description) {
        if (!predicate.test(response.getBody())) {
            fail("Body failed predicate: " + description);
        }
        return this;
    }

    // ── Field extraction helpers ──────────────────────────────────────────────

    public ResponseValidator fieldEquals(String jsonField, String expectedValue) {
        String actual = JsonParser.getString(response.getBody(), jsonField);
        if (!expectedValue.equals(actual)) {
            fail("Expected field \"" + jsonField + "\" = \"" + expectedValue
                    + "\" but got \"" + actual + "\"");
        }
        return this;
    }

    public ResponseValidator fieldNotNull(String jsonField) {
        String actual = JsonParser.getString(response.getBody(), jsonField);
        if (actual == null) {
            fail("Expected field \"" + jsonField + "\" to be present and non-null");
        }
        return this;
    }

    // ── Headers ──────────────────────────────────────────────────────────────

    public ResponseValidator headerPresent(String headerName) {
        String value = response.getHeader(headerName.toLowerCase());
        if (value == null) {
            fail("Expected header \"" + headerName + "\" to be present");
        }
        return this;
    }

    public ResponseValidator headerContains(String headerName, String substring) {
        String value = response.getHeader(headerName.toLowerCase());
        if (value == null || !value.contains(substring)) {
            fail("Expected header \"" + headerName + "\" to contain \"" + substring
                    + "\" but was: " + value);
        }
        return this;
    }

    // ── Performance ──────────────────────────────────────────────────────────

    public ResponseValidator respondedWithin(long maxMs) {
        long actual = response.getElapsedMs();
        if (actual > maxMs) {
            fail("Expected response within " + maxMs + "ms but took " + actual + "ms");
        }
        return this;
    }

    // ── internal ─────────────────────────────────────────────────────────────

    private static void fail(String message) {
        throw new AssertionError(message);
    }

    private static String truncate(String s) {
        if (s == null) return "(null)";
        return s.length() > 300 ? s.substring(0, 300) + "…" : s;
    }
}
