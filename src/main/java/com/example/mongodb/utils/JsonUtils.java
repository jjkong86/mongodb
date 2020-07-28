package com.example.mongodb.utils;

import com.example.mongodb.exception.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

public class JsonUtils {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static String toJson(Object target) {
        try {
            return OBJECT_MAPPER.writeValueAsString(target);
        } catch (IOException e) {
            throw new JsonParseException(target.toString(), e);
        }
    }
}
