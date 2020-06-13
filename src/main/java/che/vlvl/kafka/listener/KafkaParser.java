package che.vlvl.kafka.listener;

import che.vlvl.kafka.parser.RollsManager;
import che.vlvl.kafka.pojo.ComponentHeader;
import che.vlvl.kafka.pojo.JsonDeserialize;
import che.vlvl.kafka.pojo.Roll;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
/*
Парсит сообщения
 */
public class KafkaParser {


    private final RollsManager rollsManager;

    public KafkaParser(RollsManager rollsManager) {
        this.rollsManager = rollsManager;
    }


    public void parse() throws IOException, ParseException {
        Path path = Paths.get("D:/kafkaWrite/testMessage.txt");

        //Parse message from kafka to POJO object
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserialize jsonDeserialize = mapper.readValue(Files.readString(path), JsonDeserialize.class);

        //Заполняем rollsManager из сообщения
        rollsManager.fillData(jsonDeserialize);
        //Забираем данные по перевалкам из роликов
        List<Roll> separatedRolls = rollsManager.separateData();
        //Кладем данные по перевалка в Oracle

        if (separatedRolls.size() > 0) rollsManager.insertToOracle(separatedRolls);

        System.out.println("Сообщение полностью обработано");
    }


}
