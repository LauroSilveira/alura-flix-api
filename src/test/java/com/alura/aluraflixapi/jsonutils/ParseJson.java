package com.alura.aluraflixapi.jsonutils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.json.JsonParseException;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ParseJson {

    private static final String JSON_FOLDER = "src/test/resources/json/";
    private static final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    /**
     * Convert a JSON file in to a java object
     *
     * @param file  json
     * @param clazz to be converted
     * @param <T>   type of class
     * @return the java object
     */
    protected static <T> T parseToJavaObject(final File file, Class<T> clazz) {
        try {
            return objectMapper.readValue(file, clazz);
        } catch (IOException ex) {
            throw new JsonParseException(ex.getCause());
        }
    }

    /**
     * @param json file
     * @return Json {@link File} passed as argument
     */
    protected static File getJsonFile(final String json) {

        try {
            return ResourceUtils.getFile(JSON_FOLDER + json);
        } catch (FileNotFoundException ex) {
            throw new RuntimeException("Error to get json file", ex.getCause());
        }
    }

    /**
     * @param jsonFile JSON File
     * @param clazz    Clazz want to be parsed
     * @param <T>      Object Type
     * @return a List of elements
     */

    public static <T> List<T> parseToList(File jsonFile, Class<T> clazz) {
        try {
            final var collectionType = objectMapper.getTypeFactory()
                    .constructCollectionType(ArrayList.class, clazz);
            return objectMapper.readValue(jsonFile, collectionType);
        } catch (IOException e) {
            throw new RuntimeException("Error converting JSON to List");
        }
    }
}
