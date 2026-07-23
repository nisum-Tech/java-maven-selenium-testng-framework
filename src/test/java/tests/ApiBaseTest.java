package tests;

import io.restassured.RestAssured;
import org.testng.annotations.BeforeClass;
import io.restassured.response.Response;
import models.LoginRequest;
import utils.ApiClient;

/**
 * Base class for all API tests.
 * Sets up a shared ApiClient pointing at the configured base URL.
 */
public class ApiBaseTest {

    protected static final String BASE_URL = "https://reqres.in/api";

    protected ApiClient apiClient;

    @BeforeClass
    public void setupApi() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        apiClient = new ApiClient(BASE_URL);

        // Perform login and set bearer token for subsequent requests
        LoginRequest loginBody = new LoginRequest("eve.holt@reqres.in", "cityslicka");
        Response loginResponse = apiClient.post("/login", loginBody);
        loginResponse.then().statusCode(200); // Assert that login was successful
        String token = loginResponse.jsonPath().getString("token");

        if (token != null && !token.isBlank()) {
            apiClient.withBearerToken(token);
        } else {
            throw new RuntimeException("Failed to obtain authentication token during setup.");
        }
    }
}
