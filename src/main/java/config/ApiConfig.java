package config;

import java.io.InputStream;
import java.util.Properties;

/**
 * Centralised configuration for the HTTP API client.
 *
 * <p>Reads from {@code api.properties} on the classpath; falls back to
 * sensible defaults when the file is absent so the framework works
 * out-of-the-box without any setup.</p>
 *
 * <p>Any property can be overridden at runtime via a system property
 * with the same key, e.g. {@code -Dapi.base.url=https://staging.example.com}.</p>
 */
public final class ApiConfig {

    private static final Properties PROPS = new Properties();

    static {
        try (InputStream is = ApiConfig.class.getClassLoader()
                .getResourceAsStream("api.properties")) {
            if (is != null) PROPS.load(is);
        } catch (Exception ignored) {
            // file is optional — defaults apply
        }
    }

    private ApiConfig() {}

    public static String getBaseUrl() {
        return get("api.base.url", "https://reqres.in/api");
    }

    public static int getConnectTimeoutSeconds() {
        return getInt("api.connect.timeout.seconds", 10);
    }

    public static int getReadTimeoutSeconds() {
        return getInt("api.read.timeout.seconds", 30);
    }

    public static String getDefaultBearerToken() {
        return get("api.bearer.token", null);
    }

    // ── helpers ──────────────────────────────────────────────────────────────

    private static String get(String key, String defaultValue) {
        String sysProp = System.getProperty(key);
        if (sysProp != null && !sysProp.isBlank()) return sysProp;
        return PROPS.getProperty(key, defaultValue);
    }

    private static int getInt(String key, int defaultValue) {
        try {
            return Integer.parseInt(get(key, String.valueOf(defaultValue)));
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
