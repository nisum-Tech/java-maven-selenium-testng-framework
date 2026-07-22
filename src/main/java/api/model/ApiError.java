package api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Represents an error response body returned by the API (e.g. 400 / 401 / 404).
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiError {

    private String error;
    private int    status;
    private String message;

    public ApiError() {}

    public String getError()        { return error; }
    public void setError(String e)  { this.error = e; }

    public int getStatus()          { return status; }
    public void setStatus(int s)    { this.status = s; }

    public String getMessage()          { return message; }
    public void setMessage(String msg)  { this.message = msg; }

    @Override
    public String toString() {
        return "ApiError{status=" + status + ", error='" + error + "', message='" + message + "'}";
    }
}
