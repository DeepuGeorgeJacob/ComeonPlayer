package se.web.comeon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ComeonApplication {

	public static void main(String[] args) {
		SpringApplication.run(ComeonApplication.class, args);
	}

}
