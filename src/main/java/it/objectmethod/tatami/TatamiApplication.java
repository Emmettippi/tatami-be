package it.objectmethod.tatami;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TatamiApplication {

	public static void main(String[] args) {
		SpringApplication.run(TatamiApplication.class, args);
	}
}
