package models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Request body for POST /login and POST /register on reqres.in.
 */
public class LoginRequest {

    private String email;
    private String password;

    public LoginRequest() {}

    @JsonCreator
    public LoginRequest(@JsonProperty("email") String email, @JsonProperty("password") String password) {
        this.email    = email;
        this.password = password;
    }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
