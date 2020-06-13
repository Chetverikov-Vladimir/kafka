package che.vlvl.kafka.pojo;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class Roll {
    private String rollTagSpeed;
    private String rollTagAmpere;
    private Map<Timestamp,Double> speeds;
    private Map<Timestamp,Double> amperes;

    public Roll(String rollTagSpeed, String rollTagAmpere) {
        this();
        this.rollTagSpeed = rollTagSpeed;
        this.rollTagAmpere = rollTagAmpere;
    }

    public Roll() {
        speeds=new TreeMap<>();
        amperes=new TreeMap<>();
    }

    public Roll(String rollTagSpeed) {
        this();
        this.rollTagSpeed = rollTagSpeed;
    }

    public String getRollTagSpeed() {
        return rollTagSpeed;
    }

    public void setRollTagSpeed(String rollTagSpeed) {
        this.rollTagSpeed = rollTagSpeed;
    }

    public String getRollTagAmpere() {
        return rollTagAmpere;
    }

    public void setRollTagAmpere(String rollTagAmpere) {
        this.rollTagAmpere = rollTagAmpere;
    }

    public Map<Timestamp, Double> getSpeeds() {
        return speeds;
    }

    public void setSpeeds(Map<Timestamp, Double> speeds) {
        this.speeds = speeds;
    }

    public void addSpeeds(Timestamp timestamp,Double speed){
         speeds.put(timestamp, speed);
    }

    public Map<Timestamp, Double> getAmperes() {
        return amperes;
    }

    public void setAmperes(Map<Timestamp, Double> amperes) {
        this.amperes = amperes;
    }
    public void addAmperes(Timestamp timestamp,Double ampere){
        amperes.put(timestamp, ampere);
    }
}
