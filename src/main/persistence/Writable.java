package persistence;

import org.json.JSONObject;

//Code referenced from JsonSerializationDemo project
//Interface to make objects into JSON Objects
public interface Writable {
    //EFFECTS: returns this as a JSON object
    JSONObject toJson();
}
