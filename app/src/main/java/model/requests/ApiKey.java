package model.requests;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ApiKey {

    private static String key = "d49fc8d6eamsh5d3ae1a2f2143fbp1ebd4cjsn9217e6bdb490";

    public static String getKey() {
        return key;
    }

    public static void setKey(String k) {
        key = k;
    }

}
