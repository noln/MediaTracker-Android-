package model.persistence;


import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import org.json.JSONException;
import org.json.JSONObject;

import model.*;
import model.jsonreaders.ReadUserItem;
import model.model.MediaList;
import model.model.Tag;


import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.stream.Collectors;


// A reader that can read list data from file. Modeled after Teller App
public class Reader {

    // EFFECTS: returns a list of ReadUserItem read from file;
    // throws IOException if an exception is raised when opening / reading from file, create file if file not found
    // Modeled from https://futurestud.io/tutorials/gson-mapping-of-arrays-and-lists-of-objects
    public static ArrayList<ReadUserItem> readItemFile(String filePath) throws IOException {
        ArrayList<ReadUserItem> info;
        try {
            System.out.println("Reading from a file");
            System.out.println("----------------------------");
            FileReader fileReader = new FileReader(filePath);
            BufferedReader br = new BufferedReader(fileReader);
            Gson gson = new Gson();
            Type mapType = new TypeToken<ArrayList<ReadUserItem>>(){}.getType();
            info = gson.fromJson(br, mapType);
        } catch (FileNotFoundException e) {
            createFile(filePath);
            return null;
        }
        return info;
    }

    // EFFECTS: returns a list of Tag read from file;
    // throws IOException if an exception is raised when opening / reading from file, create file if file not found
    public static ArrayList<Tag> readTagFile(String filePath) throws IOException {
        ArrayList<Tag> info;
        try {
            FileReader fileReader = new FileReader(filePath);
            JsonReader br = new JsonReader(fileReader);
            Gson gson = new Gson();
            Type mapType = new TypeToken<ArrayList<Tag>>(){}.getType();
            info = gson.fromJson(br, mapType);
        } catch (FileNotFoundException e) {
            createFile(filePath);
            return null;
        }
        return info;
    }

    // EFFECTS: returns a list of MediaList read from file;
    // throws IOException if an exception is raised when opening / reading from file, create file if file not found
    public static ArrayList<MediaList> readListFile(String filePath) throws IOException {
        ArrayList<MediaList> info;
        try {
            FileReader fileReader = new FileReader(filePath);
            JsonReader br = new JsonReader(fileReader);
            Gson gson = new Gson();
            Type mapType = new TypeToken<ArrayList<MediaList>>(){}.getType();
            info = gson.fromJson(br, mapType);
        } catch (FileNotFoundException e) {
            createFile(filePath);
            return null;
        }
        return info;
    }

    public static void createFile(String filePath) throws IOException {
        File file = new File(filePath);
        file.createNewFile();
    }

}
