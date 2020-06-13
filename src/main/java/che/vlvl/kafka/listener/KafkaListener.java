package che.vlvl.kafka.listener;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;

@EnableKafka
@Configuration
/*
Слушает kafka и генерит запрос на парсинг сообщений
 */
public class KafkaListener {

    private final KafkaParser kafkaParser;

    public KafkaListener(KafkaParser kafkaParser) {
        this.kafkaParser = kafkaParser;
    }

    @org.springframework.kafka.annotation.KafkaListener(topics = "BA-TS.NLMK.P3.HSM.CG_100U")
    public void rollsListener(ConsumerRecord<String,String> record) {
        Path path = Paths.get("D:/kafkaWrite/testMessage.txt");

        try {
            byte[] message=record.value().getBytes();
            Path writtenFile= Files.write(path,message);
            System.out.println("File written");
            kafkaParser.parse();


        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

    }
}
