package che.vlvl.kafka.pojo;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
@JsonAutoDetect
public class JsonDeserialize {
    @JsonProperty("headers")
    private ArrayList<ComponentHeader> headers;
    @JsonProperty("values")
    private ArrayList<ArrayList<Object>> values;


    public JsonDeserialize() {
    }

    public JsonDeserialize(ArrayList<ComponentHeader> headers) {
        this.headers = headers;
    }

    public ArrayList<ComponentHeader> getHeaders() {
        return headers;
    }

    public void setHeaders(ArrayList<ComponentHeader> headers) {
        this.headers = headers;
    }

    public ArrayList<ArrayList<Object>> getValues() {
        return values;
    }

    public void setValues(ArrayList<ArrayList<Object>> values) {
        this.values = values;
    }
}
