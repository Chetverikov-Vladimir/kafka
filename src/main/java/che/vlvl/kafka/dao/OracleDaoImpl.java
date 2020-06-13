package che.vlvl.kafka.dao;

import che.vlvl.kafka.pojo.Roll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

@Repository
public class OracleDaoImpl implements OracleDao{
    private final NamedParameterJdbcOperations namedJdbc;
    private final JdbcOperations jdbc;

    @Autowired
    public OracleDaoImpl(NamedParameterJdbcOperations namedJdbc, JdbcOperations jdbc) {
        this.namedJdbc = namedJdbc;
        this.jdbc = jdbc;
    }


    @Override
    public Map<String, Integer> getFieldsByRolls() {
        String selectFields="SELECT * FROM FIELDS ORDER BY NAM";

        return jdbc.query(selectFields, resultSet -> {
            Map<String, Integer> result=new LinkedHashMap<>();
            while (resultSet.next()){
                result.put(resultSet.getString("NAM"),
                        resultSet.getInt("MAIN"));
            }
            return result;
        });
    }

    @Override
    public void addDataToOracle(List<Roll> rolls) {
        String SQL="INSERT INTO ROLL_TEST (TS,SPEED,CURR,NAM) VALUES (:ts,:speed,:ampere,:tagSpeed)";
        List<Map<String,Object>> batchValues=new ArrayList<>();
        for(Roll roll:rolls){

            for (Map.Entry<Timestamp,Double> speed:roll.getSpeeds().entrySet()){
                batchValues.add(
                        new MapSqlParameterSource()
                                .addValue("ts",speed.getKey())
                                .addValue("speed",speed.getValue())
                                .addValue("ampere",roll.getAmperes().get(speed.getKey()))
                                .addValue("tagSpeed",roll.getRollTagAmpere())
                                .getValues());
            }
        }

        Map[] batchParameters = batchValues.toArray(new Map[batchValues.size()]);
        namedJdbc.batchUpdate(SQL, batchParameters);
        System.out.println("Добавлено в Oracle "+batchParameters.length+" записей");
    }
}
