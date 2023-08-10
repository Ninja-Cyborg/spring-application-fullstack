package com.spring;

import com.github.javafaker.Faker;
import com.spring.patron.Patron;
import com.spring.patron.PatronRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.Random;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	CommandLineRunner runner(PatronRepository patronRepository){
		return args -> {
			Faker faker = new Faker();
			Random random = new Random();
			Patron john = new Patron(
					faker.name().fullName().toLowerCase(),
					faker.internet().safeEmailAddress(),
					random.nextInt(18,75)
			);

			List<Patron> patrons = List.of(john);
			patronRepository.saveAll(patrons);
		};
	}
}
