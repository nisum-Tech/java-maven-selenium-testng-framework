package api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Generic user model matching the reqres.in /users response shape.
 * Used both as a request body (name + job) and a response object.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {

    private Integer id;
    private String name;
    private String job;
    private String email;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    private String avatar;

    @JsonProperty("created_at")
    private String createdAt;

    @JsonProperty("updated_at")
    private String updatedAt;

    // ── Constructors ──────────────────────────────────────────────────────────

    public User() {}

    private User(Builder builder) {
        this.name  = builder.name;
        this.job   = builder.job;
        this.email = builder.email;
    }

    // ── Builder ───────────────────────────────────────────────────────────────

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String name;
        private String job;
        private String email;

        public Builder name(String name)   { this.name  = name;  return this; }
        public Builder job(String job)     { this.job   = job;   return this; }
        public Builder email(String email) { this.email = email; return this; }

        public User build() { return new User(this); }
    }

    // ── Getters / Setters ────────────────────────────────────────────────────

    public Integer getId()          { return id; }
    public void setId(Integer id)   { this.id = id; }

    public String getName()         { return name; }
    public void setName(String n)   { this.name = n; }

    public String getJob()          { return job; }
    public void setJob(String j)    { this.job = j; }

    public String getEmail()        { return email; }
    public void setEmail(String e)  { this.email = e; }

    public String getFirstName()            { return firstName; }
    public void setFirstName(String fn)     { this.firstName = fn; }

    public String getLastName()             { return lastName; }
    public void setLastName(String ln)      { this.lastName = ln; }

    public String getAvatar()               { return avatar; }
    public void setAvatar(String avatar)    { this.avatar = avatar; }

    public String getCreatedAt()            { return createdAt; }
    public void setCreatedAt(String t)      { this.createdAt = t; }

    public String getUpdatedAt()            { return updatedAt; }
    public void setUpdatedAt(String t)      { this.updatedAt = t; }

    @Override
    public String toString() {
        return "User{id=" + id + ", name=" + name + ", email=" + email + "}";
    }
}
