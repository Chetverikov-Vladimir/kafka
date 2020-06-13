package che.vlvl.kafka;

import che.vlvl.kafka.listener.KafkaParser;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;
import java.text.ParseException;


@SpringBootApplication
public class KafkaApplication {

	public static void main(String[] args) throws IOException, ParseException {

		ConfigurableApplicationContext context = SpringApplication.run(KafkaApplication.class, args);


	}





}
