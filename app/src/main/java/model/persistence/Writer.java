package model.persistence;

import org.json.JSONException;

import java.io.*;

//A writer that can write list data to a file. Modeled after Teller App
public class Writer {
    private FileWriter listFile;
    private FileWriter tagFile;
    private FileWriter itemFile;

    // EFFECTS: constructs a writer that will write data to file
    public Writer(File listFile, File tagFile, File itemFile) throws IOException {
        this.listFile = new FileWriter(listFile);
        this.tagFile = new FileWriter(tagFile);
        this.itemFile = new FileWriter(itemFile);
    }

    // MODIFIES: this
    // EFFECTS: writes SaveAble to file
    public void write(SaveAble saveable) throws JSONException, IOException {
        saveable.save(listFile, tagFile, itemFile);
    }

    // MODIFIES: this
    // EFFECTS: close print writer
    // NOTE: you MUST call this method when you are done writing data!
    public void close() throws IOException {
        listFile.flush();
        listFile.close();
        tagFile.flush();
        tagFile.close();
        itemFile.flush();
        itemFile.close();
    }
}
