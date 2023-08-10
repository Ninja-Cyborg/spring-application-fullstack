package com.spring.patron;

import com.spring.AbstractTestContainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PatronRepositoryTest extends AbstractTestContainer {

    @Autowired
    private PatronRepository underTest;

    @BeforeEach
    void setUp() {
    }

    @Test
    void existsPatronByEmail() {
        String email = FAKER.internet().safeEmailAddress();
        Patron patron = new Patron(
                FAKER.name().fullName(),
                email,
                FAKER.number().numberBetween(16,75)
        );
        underTest.save(patron);

        int id = underTest.findAll()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Patron::getId)
                .findFirst()
                .orElseThrow();

        var actual = underTest.existsPatronByEmail(email);

        assertThat(actual).isTrue();
    }

    @Test
    void patronNotExistWithoutEmail() {
        String email = FAKER.internet().safeEmailAddress();

        var actual = underTest.existsPatronByEmail(email);

        assertThat(actual).isFalse();
    }

    @Test
    void existsPatronById() {
        String email = FAKER.internet().safeEmailAddress();
        Patron patron = new Patron(
                FAKER.name().fullName(),
                email,
                FAKER.number().numberBetween(16,75)
        );
        underTest.save(patron);

        int id = underTest.findAll()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Patron::getId)
                .findFirst()
                .orElseThrow();

        var actual = underTest.existsPatronById(id);

        assertThat(actual).isTrue();
    }

    @Test
    void patronNotExistWithInvalidId(){
        int id = -1;

        var actual = underTest.existsPatronById(id);

        assertThat(actual).isFalse();
    }
}