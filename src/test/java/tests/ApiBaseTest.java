package tests;

import io.restassured.RestAssured;
import org.testng.annotations.BeforeClass;
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
    }
}
