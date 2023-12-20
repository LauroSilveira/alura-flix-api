package com.alura.aluraflixapi.jsonutils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.json.JsonParseException;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ParseJson {

    private static final String JSON_FOLDER = "src/test/resources/json/";

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
            return new ObjectMapper().readValue(file, clazz);
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
}
