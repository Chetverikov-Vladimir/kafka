package che.vlvl.kafka.parser;

import che.vlvl.kafka.dao.OracleDao;
import che.vlvl.kafka.pojo.ComponentHeader;
import che.vlvl.kafka.pojo.JsonDeserialize;
import che.vlvl.kafka.pojo.Roll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Component
/*
Управление данными по роликам (создание, добавление данных)
 */
public class RollsManager {
    private final OracleDao oracleDao;
    private List<Roll> rolls;

    @Autowired
    public RollsManager(OracleDao oracleDao) {
        this.oracleDao = oracleDao;
        rolls = new ArrayList<>();
        createRolls();
    }

    private void createRolls() {
        Map<String, Integer> fieldsByRolls = oracleDao.getFieldsByRolls();

        Iterator<Map.Entry<String, Integer>> iterator = fieldsByRolls.entrySet().iterator();

        Roll currentRoll = null;

        while (iterator.hasNext()) {
            Map.Entry<String, Integer> next = iterator.next();

            if (next.getValue() == 0) {
                currentRoll = new Roll(next.getKey());
            } else {
                currentRoll.setRollTagAmpere(next.getKey());
                rolls.add(currentRoll);
                currentRoll = null;
            }
        }
    }

    public void fillData(JsonDeserialize jsonDeserialize) throws ParseException {
        int indexSpeed;
        int indexAmpere;
        int indexTs;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        ArrayList<ComponentHeader> headers = jsonDeserialize.getHeaders();

        for (Roll roll : rolls) {


            indexSpeed = headers.indexOf(headers.stream()
                    .filter(componentHeader -> componentHeader.getName() != null)
                    .filter(componentHeader -> componentHeader.getName().equals(roll.getRollTagSpeed()))
                    .findFirst()
                    .get());
            indexAmpere = headers.indexOf(headers.stream()
                    .filter(componentHeader -> componentHeader.getName() != null)
                    .filter(componentHeader -> componentHeader.getName().equals(roll.getRollTagAmpere()))
                    .findFirst()
                    .get());
            indexTs = headers.indexOf(headers.stream()
                    .filter(componentHeader -> componentHeader.getTimestamp() != null)
                    .filter(componentHeader -> componentHeader.getTimestamp().equals("timestamp"))
                    .findFirst()
                    .get());


            for (ArrayList<Object> value : jsonDeserialize.getValues()) {
                Date fromTs = format.parse((String) value.get(indexTs));
                Timestamp ts = new Timestamp(fromTs.getTime());
                roll.addSpeeds(ts, Double.valueOf(value.get(indexSpeed).toString()));
                roll.addAmperes(ts, Double.valueOf(value.get(indexAmpere).toString()));
            }
        }
    }

    public List<Roll> separateData() {

        List<Roll> separatedRolls = new ArrayList<>();

        for (Roll roll : rolls) {

            Roll currentRoll = new Roll(roll.getRollTagSpeed(), roll.getRollTagAmpere());
            Timestamp beginTs = null;
            Timestamp endTs = null;
            int count = 0;
            Iterator<Map.Entry<Timestamp, Double>> iterator = roll.getSpeeds().entrySet().iterator();

            while (iterator.hasNext()) {

                Map.Entry<Timestamp, Double> currentEntry = iterator.next();
                if (Double.compare(currentEntry.getValue(), 3.0) < 0 && Double.compare(currentEntry.getValue(), 1.0) > 0) {

                    if (count == 0) {
                        beginTs = currentEntry.getKey();
                    }

                    count++;

                    if (count == 512) {
                        count = 0;
                        endTs = currentEntry.getKey();
                        currentRoll.getSpeeds().putAll(getMapFromTs(roll.getSpeeds(), beginTs, endTs));
                        currentRoll.getAmperes().putAll(getMapFromTs(roll.getAmperes(), beginTs, endTs));
                    }

                } else {
                    count = 0;
                    beginTs = null;
                    endTs = null;

                }
            }

            separatedRolls.add(currentRoll);

            if(beginTs!=null){

                Map<Timestamp, Double> speeds = getMapFromRemain(roll.getSpeeds(), beginTs);
                Map<Timestamp, Double> amperes = getMapFromRemain(roll.getAmperes(), beginTs);

                roll.getSpeeds().clear();
                roll.getAmperes().clear();

                roll.getSpeeds().putAll(speeds);
                roll.getAmperes().putAll(amperes);

               // System.out.println("Есть необработанный кусок");
            }

            else {
                roll.getSpeeds().clear();
                roll.getAmperes().clear();
            }
        }

        return separatedRolls;
    }

    private Map<Timestamp, Double> getMapFromRemain(Map<Timestamp, Double> mapFromRoll, Timestamp beginTs) {

        return getMapFromTs(mapFromRoll,beginTs,new Timestamp(Long.MAX_VALUE));
      }


    public Map<Timestamp, Double> getMapFromTs(Map<Timestamp, Double> mapFromRoll, Timestamp beginTs, Timestamp endTs) {

        Map<Timestamp, Double> result = new TreeMap<>();
        Iterator<Map.Entry<Timestamp, Double>> iterator = mapFromRoll.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<Timestamp, Double> currentEntry = iterator.next();
            if (currentEntry.getKey().compareTo(endTs) <= 0 && currentEntry.getKey().compareTo(beginTs) >= 0) {
                result.put(currentEntry.getKey(), currentEntry.getValue());
            }
        }
        return result;
    }


    public void insertToOracle(List<Roll> separatedRolls) {
        oracleDao.addDataToOracle(separatedRolls);
    }

}
