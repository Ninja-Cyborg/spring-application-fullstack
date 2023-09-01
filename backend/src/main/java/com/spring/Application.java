package com.spring;

import com.github.javafaker.Faker;
import com.spring.patron.Gender;
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
			String fullname = faker.name().fullName().toLowerCase();
			String email = faker.internet().safeEmailAddress();
			int age = random.nextInt(18,76);
			Gender gender = age % 2 == 0 ? Gender.MALE : Gender.FEMALE;
			Patron john = new Patron(
					fullname,
					email,
					age,
					gender);

			List<Patron> patrons = List.of(john);
			patronRepository.saveAll(patrons);
		};
	}
}