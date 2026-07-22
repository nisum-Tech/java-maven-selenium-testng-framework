package api.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.util.List;
import java.util.Map;

/**
 * Thin Jackson facade used throughout the main API layer.
 *
 * <p>All methods throw {@link RuntimeException} on failure so callers
 * don't have to handle checked exceptions in test assertions.</p>
 */
public final class JsonParser {

    private static final ObjectMapper MAPPER = new ObjectMapper()
            .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

    private JsonParser() {}

    /** Serialise any object to a JSON string. */
    public static String toJson(Object value) {
        try {
            return MAPPER.writeValueAsString(value);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialise to JSON: " + value, e);
        }
    }

    /** Pretty-print any object as a formatted JSON string. */
    public static String toPrettyJson(Object value) {
        try {
            return MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(value);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialise to pretty JSON: " + value, e);
        }
    }

    /** Deserialise a JSON string into the given class. */
    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return MAPPER.readValue(json, clazz);
        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialise JSON into " + clazz.getSimpleName(), e);
        }
    }

    /** Deserialise a JSON string into a generic type (e.g. {@code List<User>}). */
    public static <T> T fromJson(String json, TypeReference<T> typeRef) {
        try {
            return MAPPER.readValue(json, typeRef);
        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialise JSON", e);
        }
    }

    /** Parse a JSON string into a {@link JsonNode} for flexible field access. */
    public static JsonNode parse(String json) {
        try {
            return MAPPER.readTree(json);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse JSON", e);
        }
    }

    /** Extract a top-level string field from a JSON response body. */
    public static String getString(String json, String field) {
        JsonNode node = parse(json).get(field);
        return (node == null || node.isNull()) ? null : node.asText();
    }

    /** Extract a top-level int field from a JSON response body. */
    public static int getInt(String json, String field) {
        JsonNode node = parse(json).get(field);
        if (node == null || node.isNull()) throw new RuntimeException("Field not found: " + field);
        return node.asInt();
    }

    /** Convert a JSON body to a {@code Map<String, Object>}. */
    public static Map<String, Object> toMap(String json) {
        return fromJson(json, new TypeReference<>() {});
    }

    /** Convert a JSON array body to a {@code List<Map<String,Object>>}. */
    public static List<Map<String, Object>> toList(String json) {
        return fromJson(json, new TypeReference<>() {});
    }
}
