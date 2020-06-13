package che.vlvl.kafka.dao;

import che.vlvl.kafka.pojo.Roll;

import java.util.List;
import java.util.Map;

public interface OracleDao {

    Map<String,Integer> getFieldsByRolls();
    void addDataToOracle(List<Roll> rolls);
}
