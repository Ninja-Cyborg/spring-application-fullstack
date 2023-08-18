package com.spring.integration;

import com.github.javafaker.Faker;
import com.spring.patron.Patron;
import com.spring.patron.PatronDao;
import com.spring.patron.PatronRegistrationRequest;
import com.spring.patron.PatronUpdateRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class PatronIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    private static final String PATRON_URI = "/api/v1/patrons";

    @Test
    void canRegisterPatron(){
        // create registration request
        Faker faker = new Faker();
        String name = faker.name().fullName();
        String email = faker.internet().safeEmailAddress();
        int age = faker.random().nextInt(16,100);

        PatronRegistrationRequest request = new PatronRegistrationRequest(
                 name, email,age
        );
        // POST request to web Client

        // error source?? flaw in post request?

        webTestClient.post()
                .uri(PATRON_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), PatronRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        // get all patrons
        List<Patron> patrons = webTestClient.get()
                .uri(PATRON_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Patron>() {
                })
                .returnResult()
                .getResponseBody();

        Patron expectedPatron = new Patron( name, email, age);

        // verify patron is present/added
        // sql preparedStatement not incrementing while deploying on github

        assertThat(patrons)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(expectedPatron);

        int id = patrons.stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Patron::getId)
                .findFirst()
                .orElseThrow();

        expectedPatron.setId(id);

        // get Patron by Id
        webTestClient.get()
                .uri(PATRON_URI+"/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<Patron>() {
                })
                .isEqualTo(expectedPatron);
    }

    @Test
    void canDeletePatron(){
        // create registration request
        Faker faker = new Faker();
        String name = faker.name().fullName();
        String email = faker.internet().safeEmailAddress();
        int age = faker.random().nextInt(16,100);

        PatronRegistrationRequest request = new PatronRegistrationRequest(
                name, email,age
        );

        // POST registration request
        webTestClient.post()
                .uri(PATRON_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), PatronRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        // verify patron's registered or present in db
        List<Patron> patrons = webTestClient.get()
                .uri(PATRON_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Patron>() {
                })
                .returnResult()
                .getResponseBody();

        int id = patrons.stream().filter(c -> c.getEmail().equals(email))
                .map(Patron::getId)
                .findFirst()
                .orElseThrow();
        // delete patron
        webTestClient.delete()
                .uri(PATRON_URI+"/{id}",id)
                .exchange()
                .expectStatus()
                .isOk();

    }

    @Test
    void canUpdatePatron(){
        // create registration request
        Faker faker = new Faker();
        String name = faker.name().fullName();
        String email = faker.internet().safeEmailAddress();
        int age = faker.random().nextInt(16,100);

        PatronRegistrationRequest request = new PatronRegistrationRequest(
                name, email,age
        );

        // POST registration request
        webTestClient.post()
                .uri(PATRON_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), PatronRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        // getting patron id
        List<Patron> patrons = webTestClient.get()
                .uri(PATRON_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Patron>() {
                })
                .returnResult()
                .getResponseBody();

        int id = patrons.stream().filter(c -> c.getEmail().equals(email))
                .map(Patron::getId)
                .findFirst()
                .orElseThrow();

        // create update request
        PatronUpdateRequest updateRequest = new PatronUpdateRequest(
                name+ " Lee",email+".ca",21
        );

        // PUT update request
        webTestClient.put()
                .uri(PATRON_URI+"/{id}",id)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(updateRequest), PatronUpdateRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        // get updated patron
        PatronDao updatedPatron = webTestClient.get()
                .uri(PATRON_URI+"/{id}",id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(PatronDao.class)
                .returnResult()
                .getResponseBody();

        // assert
        Patron expectedPatron = new Patron(name+ " Lee",email+".ca",21);

        assertThat(updatedPatron).isEqualTo(expectedPatron);
    }
}
