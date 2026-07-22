package tests;

import io.restassured.response.Response;
import models.LoginRequest;
import models.UserRequest;
import models.UserResponse;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

/**
 * API tests for the reqres.in Users endpoint.
 *
 * Covers: GET list, GET single, POST create, PUT update, PATCH update,
 *         DELETE, POST login (valid + invalid), paginated list.
 */
public class UserApiTest extends ApiBaseTest {

    // ── GET ──────────────────────────────────────────────────────────────────

    @Test(description = "GET /users returns page 1 with a non-empty user list")
    public void getUserListReturns200() {
        Response response = apiClient.get("/users", Map.of("page", 1));

        response.then().statusCode(200);

        int total = response.jsonPath().getInt("total");
        Assert.assertTrue(total > 0, "Expected at least one user in total");

        List<Object> data = response.jsonPath().getList("data");
        Assert.assertFalse(data.isEmpty(), "Expected non-empty data array on page 1");
    }

    @Test(description = "GET /users/{id} returns correct user for id=2")
    public void getSingleUserReturns200() {
        Response response = apiClient.get("/users/2");

        response.then().statusCode(200);

        UserResponse user = response.jsonPath().getObject("data", UserResponse.class);
        Assert.assertEquals(user.getId(), 2, "User id should be 2");
        Assert.assertNotNull(user.getEmail(), "Email should not be null");
        Assert.assertFalse(user.getFirstName().isBlank(), "First name should not be blank");
    }

    @Test(description = "GET /users/{id} returns 404 for a non-existent user")
    public void getMissingUserReturns404() {
        Response response = apiClient.get("/users/9999");
        response.then().statusCode(404);
    }

    @Test(description = "GET /users page 2 returns a different set of users than page 1")
    public void getUserListPage2HasDifferentUsers() {
        List<Integer> page1Ids = apiClient.get("/users", Map.of("page", 1))
                .jsonPath().getList("data.id");

        List<Integer> page2Ids = apiClient.get("/users", Map.of("page", 2))
                .jsonPath().getList("data.id");

        Assert.assertFalse(page1Ids.stream().anyMatch(page2Ids::contains),
                "Page 1 and page 2 should contain different user ids");
    }

    // ── POST create ──────────────────────────────────────────────────────────

    @Test(description = "POST /users creates a new user and returns 201 with generated id")
    public void createUserReturns201() {
        UserRequest body = new UserRequest("Jane Doe", "QA Engineer");

        Response response = apiClient.post("/users", body);
        response.then().statusCode(201);

        String createdId = response.jsonPath().getString("id");
        String name      = response.jsonPath().getString("name");
        String job       = response.jsonPath().getString("job");

        Assert.assertNotNull(createdId, "Created user should have an id");
        Assert.assertEquals(name, "Jane Doe");
        Assert.assertEquals(job, "QA Engineer");
    }

    @Test(description = "POST /users with multiple payloads — data provider",
          dataProvider = "userPayloads")
    public void createUserWithVariousPayloads(String name, String job) {
        Response response = apiClient.post("/users", new UserRequest(name, job));
        response.then().statusCode(201);
        Assert.assertEquals(response.jsonPath().getString("name"), name);
        Assert.assertEquals(response.jsonPath().getString("job"), job);
    }

    @DataProvider(name = "userPayloads")
    public Object[][] userPayloads() {
        return new Object[][] {
                { "Alice",   "Developer"  },
                { "Bob",     "Architect"  },
                { "Charlie", "DevOps"     },
        };
    }

    // ── PUT update ───────────────────────────────────────────────────────────

    @Test(description = "PUT /users/{id} fully replaces user data and returns 200")
    public void updateUserWithPutReturns200() {
        UserRequest body = new UserRequest("Janet Updated", "Senior QA");

        Response response = apiClient.put("/users/2", body);
        response.then().statusCode(200);

        Assert.assertEquals(response.jsonPath().getString("name"), "Janet Updated");
        Assert.assertEquals(response.jsonPath().getString("job"), "Senior QA");
        Assert.assertNotNull(response.jsonPath().getString("updatedAt"),
                "updatedAt timestamp should be present");
    }

    // ── PATCH update ─────────────────────────────────────────────────────────

    @Test(description = "PATCH /users/{id} partially updates job field and returns 200")
    public void patchUserJobReturns200() {
        UserRequest body = new UserRequest(null, "Lead Automation Engineer");

        Response response = apiClient.patch("/users/2", body);
        response.then().statusCode(200);

        Assert.assertEquals(response.jsonPath().getString("job"), "Lead Automation Engineer");
    }

    // ── DELETE ───────────────────────────────────────────────────────────────

    @Test(description = "DELETE /users/{id} returns 204 No Content")
    public void deleteUserReturns204() {
        Response response = apiClient.delete("/users/2");
        response.then().statusCode(204);
        Assert.assertEquals(response.body().asString(), "",
                "DELETE response body should be empty");
    }

    // ── POST login ───────────────────────────────────────────────────────────

    @Test(description = "POST /login with valid credentials returns a token")
    public void loginWithValidCredentialsReturnsToken() {
        LoginRequest body = new LoginRequest("eve.holt@reqres.in", "cityslicka");

        Response response = apiClient.post("/login", body);
        response.then().statusCode(200);

        String token = response.jsonPath().getString("token");
        Assert.assertNotNull(token, "Token should be returned on valid login");
        Assert.assertFalse(token.isBlank(), "Token should not be blank");
    }

    @Test(description = "POST /login without password returns 400 with error message")
    public void loginWithMissingPasswordReturns400() {
        LoginRequest body = new LoginRequest("eve.holt@reqres.in", null);

        Response response = apiClient.post("/login", body);
        response.then().statusCode(400);

        String error = response.jsonPath().getString("error");
        Assert.assertNotNull(error, "Error message should be present");
        Assert.assertFalse(error.isBlank(), "Error message should not be blank");
    }

    // ── Response time ────────────────────────────────────────────────────────

    @Test(description = "GET /users response time should be under 3 seconds")
    public void getUserListResponseTimeIsAcceptable() {
        Response response = apiClient.get("/users", Map.of("page", 1));
        long elapsed = response.time();
        Assert.assertTrue(elapsed < 3000,
                "Expected response in < 3000ms but got " + elapsed + "ms");
    }
}
