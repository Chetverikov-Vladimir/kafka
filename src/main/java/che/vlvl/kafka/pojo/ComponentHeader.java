package che.vlvl.kafka.pojo;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import org.apache.kafka.common.serialization.StringDeserializer;
@JsonAutoDetect
public class ComponentHeader {
    private String timestamp;
   private String unit;
 private String tech_place;
    private String name;
    private String comment;

    public ComponentHeader() {
    }

    public ComponentHeader(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getTech_place() {
        return tech_place;
    }

    public void setTech_place(String tech_place) {
        this.tech_place = tech_place;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
