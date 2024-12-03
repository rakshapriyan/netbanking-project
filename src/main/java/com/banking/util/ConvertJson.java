package com.banking.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ConvertJson {

    // Method to convert an object or list of objects to a JSON string
    public static String toJson(Object obj) throws JSONException {
        if (obj instanceof List) {
            // Handle List of objects
            JSONArray jsonArray = new JSONArray();
            for (Object item : (List<?>) obj) {
                jsonArray.put(objectToJson(item));
            }
            return jsonArray.toString();
        } else {
            // Handle single object
            return objectToJson(obj).toString();
        }
    }

    // Helper method to convert a single object to JSONObject
    private static JSONObject objectToJson(Object obj) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        try {
            Class<?> clazz = obj.getClass();
            while (clazz != null) { // Loop through class hierarchy
                Field[] fields = clazz.getDeclaredFields();
                for (Field field : fields) {
                    field.setAccessible(true); // Access private fields
                    Object value = field.get(obj);
                    if (value != null) {
                        jsonObject.put(field.getName(), value);
                    }
                }
                clazz = clazz.getSuperclass(); // Move to the parent class
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Error converting object to JSON: " + e.getMessage(), e);
        }
        return jsonObject;
    }

    // Method to convert a JSON string to an object using reflection
    public static <T> T fromJson(HttpServletRequest request, Class<T> clazz) throws IOException, JSONException {
        JSONObject jsonObject = getRequestBodyAsJson(request);
        return parseObject(jsonObject, clazz);
    }

    // Method to convert a JSON string to a list of objects
    public static <T> List<T> fromJsonList(HttpServletRequest request, Class<T> clazz) throws IOException, JSONException {
        JSONArray jsonArray = getRequestBodyAsJsonArray(request);
        List<T> list = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            list.add(parseObject(jsonObject, clazz));
        }
        return list;
    }

    // Utility method to parse an object from JSONObject
    private static <T> T parseObject(JSONObject jsonObject, Class<T> clazz) {
    try {
        T obj = clazz.getDeclaredConstructor().newInstance();
        while (clazz != null && !clazz.getSimpleName().equals("Object")) { // Loop through class hierarchy
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true); // Access private fields
                if (jsonObject.has(field.getName())) {
                    if (jsonObject.isNull(field.getName())) {
                        // Explicitly assign null for null values in JSON
                        field.set(obj, null);
                    } else {
                        Object value = jsonObject.get(field.getName());
                        // Handle field type casting
                        if (field.getType().isAssignableFrom(value.getClass())) {
                            field.set(obj, value);
                        } else if (field.getType() == int.class || field.getType() == Integer.class) {
                            field.set(obj, jsonObject.getInt(field.getName()));
                        } else if (field.getType() == long.class || field.getType() == Long.class) {
                            field.set(obj, jsonObject.getLong(field.getName()));
                        } else if (field.getType() == double.class || field.getType() == Double.class) {
                            field.set(obj, jsonObject.getDouble(field.getName()));
                        } else if (field.getType() == boolean.class || field.getType() == Boolean.class) {
                            field.set(obj, jsonObject.getBoolean(field.getName()));
                        } else if (field.getType() == String.class) {
                            field.set(obj, jsonObject.getString(field.getName()));
                        } else if (field.getType() == BigDecimal.class) {
                            // Handle BigDecimal by converting the value to a string and then to BigDecimal
                            field.set(obj, new BigDecimal(jsonObject.get(field.getName()).toString()));
                        } else {
                            // Handle other types as needed
                            field.set(obj, value.toString());
                        }
                    }
                }
            }
            clazz = (Class<T>) clazz.getSuperclass(); // Move to the parent class
        }
        return obj;
    } catch (Exception e) {
        throw new RuntimeException("Error converting JSON to object: " + e.getMessage(), e);
    }
}

    // Helper method to get request body as JSONObject
    public static JSONObject getRequestBodyAsJson(HttpServletRequest request) throws IOException, JSONException {
        BufferedReader reader = request.getReader();
        StringBuilder requestBody = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            requestBody.append(line);
        }
        return new JSONObject(requestBody.toString());
    }

    // Helper method to get request body as JSONArray
    private static JSONArray getRequestBodyAsJsonArray(HttpServletRequest request) throws IOException, JSONException {
        BufferedReader reader = request.getReader();
        StringBuilder requestBody = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            requestBody.append(line);
        }
        return new JSONArray(requestBody.toString());
    }
}
