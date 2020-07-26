package model.persistence;


import org.json.JSONException;

import java.io.FileWriter;
import java.io.IOException;

// Represents data that can be saved to file. Modeled after Teller App
public interface SaveAble {

    // MODIFIES: printWriter
    // EFFECTS: writes the SaveAble to printerWriter
    void save(FileWriter listFile, FileWriter tagFile, FileWriter itemFile) throws JSONException, IOException;
}
