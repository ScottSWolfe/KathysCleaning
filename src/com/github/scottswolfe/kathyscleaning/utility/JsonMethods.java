package com.github.scottswolfe.kathyscleaning.utility;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.ServiceLoader;

import com.github.scottswolfe.kathyscleaning.enums.SaveType;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;
import com.google.gson.TypeAdapterFactory;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

public class JsonMethods {

    private static final JsonDeserializer<Pair<?, ?>> pairJsonDeserializer =
        (jsonElement, type, jsonDeserializationContext) ->
            jsonDeserializationContext.deserialize(jsonElement, ImmutablePair.class);

    private static final JsonSerializer<Pair<?, ?>> pairJsonSerializer =
        (jsonElement, type, jsonDeserializationContext) ->
            jsonDeserializationContext.serialize(jsonElement, ImmutablePair.class);

    private static final Gson gson;

    static {
        final GsonBuilder gsonBuilder = new GsonBuilder();

        gsonBuilder.registerTypeAdapter(Pair.class, pairJsonDeserializer);
        gsonBuilder.registerTypeAdapter(Pair.class, pairJsonSerializer);

        for (TypeAdapterFactory factory : ServiceLoader.load(TypeAdapterFactory.class)) {
            gsonBuilder.registerTypeAdapterFactory(factory);
        }

        gson = gsonBuilder.create();
    }

    /**
     * Saves the given object to the given file in JSON format
     *
     * @param data the object to be jsonified
     * @param type the class of the given object
     * @param file the file to which to save the object's data
     */
    public static void saveToFileJSON(
                        Object data, Class<?> type, File file) {

        saveToFile(data, type, file, 0);
    }

    /**
     * Saves the given object to the given line in the file in JSON format
     *
     * @param data     the object to be jsonified
     * @param type     the class of the given object
     * @param file     the file to which to save the object's data
     * @param saveType the type of data being saved
     */
    public static void saveToFileJSON(Object data, Class<?> type, File file, SaveType saveType) {
        saveToFile(data, type, file, saveType.getLineNumber());
    }

    /**
     * Returns an object with data parsed from the given JSON file and
     *
     * @param type the class of object to return
     * @param file the file to parse
     * @return the new object
     */
    public static Object loadFromFileJSON(Class<?> type, File file) {
        return loadFromFile(type, file, 0);
    }

    /**
     * Returns an object with data parsed from the given line in the JSON file
     *
     * @param type     the class of object to return
     * @param file     the file to parse
     * @param saveType the type of data being read
     * @return the new object
     */
    public static <T> T loadFromFileJSON(Class<T> type, File file, SaveType saveType) {
        return loadFromFile(type, file, saveType.getLineNumber());
    }

    private static void saveToFile(Object data,
                            Class<?> type, File file, int lineNumber) {

        String json = gson.toJson(data, type);
        List<String> lines = getFileLines(file);
        writeLinesAndJson(json, lines, file, lineNumber);
    }

    private static <T> T loadFromFile(Class<T> type, File file, int lineNumber) {
        try {
            Scanner input = new Scanner(file);
            String json = getLine(input, lineNumber);
            T data = gson.fromJson(json, type);
            input.close();
            return data;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static List<String> getFileLines(File file) {
        List<String> lines = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNext()) {
                lines.add(scanner.nextLine());
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            // do nothing
        }
        return lines;
    }

    private static void writeLinesAndJson(String json, List<String> lines,
                                          File file, int lineNumber) {
        try {
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            for (int i = 0; i < Math.max(lineNumber + 1, lines.size()); i++) {
                if (i == lineNumber) {
                    bw.write(json);
                } else {
                    if (i < lines.size()) {
                        bw.write(lines.get(i));
                    } else {
                        bw.write("{}");
                    }
                }
                bw.write('\n');
            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getLine(Scanner input, int lineNumber) {
        for (int i = 0; i < lineNumber && input.hasNext(); i++) {
            input.nextLine();
        }
        if (input.hasNext()) {
            return input.nextLine();
        } else {
            return "{}";
        }
    }
}
