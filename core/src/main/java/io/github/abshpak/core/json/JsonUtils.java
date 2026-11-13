package io.github.abshpak.core.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

public final class JsonUtils {

    private static final ObjectMapper MAPPER = JsonMapper.builder()
                                                         .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                                                         .build();

    private JsonUtils() {
    }

    public static String toJson(Object value) {
        try {
            return MAPPER.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Cannot convert object to JSON: " + value, e);
        }
    }

    public static <T> T fromJson(String json, Class<T> type) {
        try {
            return MAPPER.readValue(json, type);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Cannot convert JSON to " + type.getSimpleName() + ": " + json, e);
        }
    }
}
