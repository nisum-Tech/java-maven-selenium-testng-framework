package api.client;

import api.util.JsonParser;
import config.ApiConfig;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * Lightweight REST client built on the Java 11 {@link HttpClient}.
 *
 * <p>Supports GET, POST, PUT, PATCH, DELETE with optional bearer-token auth
 * and custom headers. Serialises request bodies to JSON via {@link JsonParser}
 * and wraps every response in an {@link ApiResponse} that also captures
 * round-trip time.</p>
 *
 * <p>Thread-safe — {@link HttpClient} is re-used across calls.</p>
 *
 * <pre>
 *   HttpApiClient client = new HttpApiClient("https://reqres.in/api");
 *   ApiResponse r = client.get("/users/2");
 *   System.out.println(r.getStatusCode());   // 200
 *   User user = JsonParser.fromJson(r.getBody(), User.class);
 * </pre>
 */
public class HttpApiClient {

    private final String baseUrl;
    private final HttpClient http;
    private final Map<String, String> defaultHeaders = new HashMap<>();

    public HttpApiClient() {
        this(ApiConfig.getBaseUrl());
    }

    public HttpApiClient(String baseUrl) {
        this.baseUrl = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
        this.http = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(ApiConfig.getConnectTimeoutSeconds()))
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();
        defaultHeaders.put("Content-Type", "application/json");
        defaultHeaders.put("Accept", "application/json");
    }

    // ── Auth ──────────────────────────────────────────────────────────────────

    public HttpApiClient withBearerToken(String token) {
        defaultHeaders.put("Authorization", "Bearer " + token);
        return this;
    }

    public HttpApiClient withHeader(String name, String value) {
        defaultHeaders.put(name, value);
        return this;
    }

    // ── GET ──────────────────────────────────────────────────────────────────

    public ApiResponse get(String path) {
        HttpRequest req = baseRequest(path).GET().build();
        return send(req);
    }

    public ApiResponse get(String path, Map<String, Object> queryParams) {
        String url = baseUrl + path + buildQuery(queryParams);
        HttpRequest req = baseRequest(url, true).GET().build();
        return send(req);
    }

    // ── POST ─────────────────────────────────────────────────────────────────

    public ApiResponse post(String path, Object body) {
        HttpRequest req = baseRequest(path)
                .POST(bodyPublisher(body))
                .build();
        return send(req);
    }

    // ── PUT ──────────────────────────────────────────────────────────────────

    public ApiResponse put(String path, Object body) {
        HttpRequest req = baseRequest(path)
                .PUT(bodyPublisher(body))
                .build();
        return send(req);
    }

    // ── PATCH ────────────────────────────────────────────────────────────────

    public ApiResponse patch(String path, Object body) {
        HttpRequest req = baseRequest(path)
                .method("PATCH", bodyPublisher(body))
                .build();
        return send(req);
    }

    // ── DELETE ───────────────────────────────────────────────────────────────

    public ApiResponse delete(String path) {
        HttpRequest req = baseRequest(path)
                .DELETE()
                .build();
        return send(req);
    }

    // ── internal ─────────────────────────────────────────────────────────────

    private HttpRequest.Builder baseRequest(String path) {
        return baseRequest(baseUrl + path, false);
    }

    private HttpRequest.Builder baseRequest(String fullUrl, boolean absolute) {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(fullUrl))
                .timeout(Duration.ofSeconds(ApiConfig.getReadTimeoutSeconds()));
        defaultHeaders.forEach(builder::header);
        return builder;
    }

    private HttpRequest.BodyPublisher bodyPublisher(Object body) {
        if (body == null) return HttpRequest.BodyPublishers.noBody();
        String json = (body instanceof String s) ? s : JsonParser.toJson(body);
        return HttpRequest.BodyPublishers.ofString(json);
    }

    private ApiResponse send(HttpRequest request) {
        try {
            long start = System.currentTimeMillis();
            HttpResponse<String> raw = http.send(request, HttpResponse.BodyHandlers.ofString());
            long elapsed = System.currentTimeMillis() - start;
            return new ApiResponse(raw, elapsed);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("HTTP request interrupted: " + request.uri(), e);
        } catch (Exception e) {
            throw new RuntimeException("HTTP request failed: " + request.uri(), e);
        }
    }

    private static String buildQuery(Map<String, Object> params) {
        if (params == null || params.isEmpty()) return "";
        StringBuilder sb = new StringBuilder("?");
        params.forEach((k, v) -> {
            if (v != null) sb.append(k).append("=").append(v).append("&");
        });
        String q = sb.toString();
        return q.endsWith("&") ? q.substring(0, q.length() - 1) : q;
    }
}
