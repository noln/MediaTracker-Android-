package model.requests;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ApiKey {

    private static String key;

    public static String getKey(Context context) {
        if (key == null) {
            try {
                FileReader fileReader = new FileReader( context.getFilesDir().getPath() + "/key.txt");
                BufferedReader br = new BufferedReader(fileReader);
                key = br.readLine();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return key;
    }

    public static void setKey(String k) {
        key = k;
    }

}
