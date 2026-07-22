package utils;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Map;

/**
 * Thin wrapper around REST Assured that pre-applies the base URL, default
 * headers (Content-Type / Accept JSON), and optional bearer-token auth.
 *
 * Usage:
 *   ApiClient client = new ApiClient("https://reqres.in/api");
 *   Response r = client.get("/users/2");
 *   r.then().statusCode(200);
 */
public class ApiClient {

    private final String baseUrl;
    private String bearerToken;

    public ApiClient(String baseUrl) {
        this.baseUrl = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
    }

    public ApiClient withBearerToken(String token) {
        this.bearerToken = token;
        return this;
    }

    // ── GET ──────────────────────────────────────────────────────────────────

    public Response get(String path) {
        return spec().get(baseUrl + path);
    }

    public Response get(String path, Map<String, Object> queryParams) {
        return spec().queryParams(queryParams).get(baseUrl + path);
    }

    // ── POST ─────────────────────────────────────────────────────────────────

    public Response post(String path, Object body) {
        return spec().body(body).post(baseUrl + path);
    }

    // ── PUT ──────────────────────────────────────────────────────────────────

    public Response put(String path, Object body) {
        return spec().body(body).put(baseUrl + path);
    }

    // ── PATCH ────────────────────────────────────────────────────────────────

    public Response patch(String path, Object body) {
        return spec().body(body).patch(baseUrl + path);
    }

    // ── DELETE ───────────────────────────────────────────────────────────────

    public Response delete(String path) {
        return spec().delete(baseUrl + path);
    }

    // ── internal ─────────────────────────────────────────────────────────────

    private RequestSpecification spec() {
        RequestSpecification req = RestAssured.given()
                .header("Content-Type", "application/json")
                .header("Accept", "application/json");
        if (bearerToken != null && !bearerToken.isBlank()) {
            req.header("Authorization", "Bearer " + bearerToken);
        }
        return req;
    }
}
