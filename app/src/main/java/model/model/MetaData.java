package model.model;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Objects;

//represents information about a MediaItem
public class MetaData implements Serializable {
    private String date; //date added
    private String nameOfObject; //name of Object added

    public MetaData() {

    }

    public MetaData(String nameOfObject) {
        Calendar calendar = Calendar.getInstance();
        this.date = calendar.getTime().toString();
        this.nameOfObject = nameOfObject;
    }

    public String getDate() {
        return this.date;
    }

    public String getNameOfObject() {
        return this.nameOfObject;
    }

    public void setNameOfObject(String nameOfObject) {
        this.nameOfObject = nameOfObject;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MetaData metaData = (MetaData) o;
        return nameOfObject.equals(metaData.nameOfObject);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nameOfObject);
    }

    // EFFECTS: save the information of the object into json
    public JSONObject save() throws JSONException {
        JSONObject dataObject = new JSONObject();
        dataObject.put("date", this.date);
        dataObject.put("nameOfObject", this.nameOfObject);
        return dataObject;
    }


}
