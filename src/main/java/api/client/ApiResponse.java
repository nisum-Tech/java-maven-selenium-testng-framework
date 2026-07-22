package api.client;

import java.net.http.HttpResponse;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Wraps a raw {@link HttpResponse} and exposes status, headers, body,
 * and elapsed time in a single convenient object.
 */
public class ApiResponse {

    private final int statusCode;
    private final String body;
    private final Map<String, String> headers;
    private final long elapsedMs;

    ApiResponse(HttpResponse<String> raw, long elapsedMs) {
        this.statusCode = raw.statusCode();
        this.body       = raw.body();
        this.elapsedMs  = elapsedMs;
        this.headers    = raw.headers().map().entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> String.join(", ", e.getValue())));
    }

    public int getStatusCode()          { return statusCode; }
    public String getBody()             { return body; }
    public long getElapsedMs()          { return elapsedMs; }
    public Map<String, String> getHeaders() { return headers; }

    public String getHeader(String name) {
        return headers.getOrDefault(name.toLowerCase(), null);
    }

    public boolean isSuccess() {
        return statusCode >= 200 && statusCode < 300;
    }

    @Override
    public String toString() {
        return "ApiResponse{status=" + statusCode + ", elapsedMs=" + elapsedMs
                + ", body=" + (body.length() > 200 ? body.substring(0, 200) + "..." : body) + "}";
    }
}
