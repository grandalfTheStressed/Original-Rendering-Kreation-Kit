package com.orkDevEngine.core.engine.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.lwjgl.system.MemoryUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Utilities {

    public static FloatBuffer storeDataInFloatBuffer(float[] data) {
        FloatBuffer buffer = MemoryUtil.memAllocFloat(data.length);
        buffer.put(data).flip();
        return buffer;
    }

    public static IntBuffer storeDataInIntBuffer(int[] data) {
        IntBuffer buffer = MemoryUtil.memAllocInt(data.length);
        buffer.put(data).flip();
        return buffer;
    }

    public static String loadResource(String filename) throws Exception {
        String result;
        try (InputStream in = Utilities.class.getResourceAsStream(filename); Scanner scanner = new Scanner(in, StandardCharsets.UTF_8.name())) {
                 result = scanner.useDelimiter("\\A").next();
             }
        return result;
    }

    public static List<String> readAllLine(String filename) {
        List<String> list = new ArrayList<>();
        try(BufferedReader br = new BufferedReader(new InputStreamReader(Class.forName(Utilities.class.getName()).getResourceAsStream(filename)))) {
             String line;
             while((line = br.readLine()) != null){
                 list.add(line);
             }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static String objectToGson(Object obj) {
        GsonBuilder gb = new GsonBuilder();
        Gson gson = gb.setPrettyPrinting().create();
        return gson.toJson(obj);
    }
}
