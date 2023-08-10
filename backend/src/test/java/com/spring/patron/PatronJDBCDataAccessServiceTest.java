package com.spring.patron;

import com.spring.AbstractTestContainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class PatronJDBCDataAccessServiceTest extends AbstractTestContainer {

    private PatronJDBCDataAccessService underTest;
    private final PatronRowMapper patronRowMapper = new PatronRowMapper();

    @BeforeEach
    void setUp() {
        underTest = new PatronJDBCDataAccessService(
                getJDBCTemplate(),
                patronRowMapper
        );
    }

    @Test
    void listAllPatrons() {
        Patron patron = new Patron(
                FAKER.name().fullName(),
                FAKER.internet().safeEmailAddress(),
                FAKER.number().numberBetween(16,75)
        );
        underTest.addPatron(patron);
        List<Patron> patrons = underTest.listAllPatrons();

        assertThat(patrons).isNotEmpty();
    }

    @Test
    void getPatronById() {
        String email = FAKER.internet().safeEmailAddress();

        Patron patron = new Patron(
                FAKER.name().fullName(),
                email,
                FAKER.number().numberBetween(16,75)
        );
        underTest.addPatron(patron);

        int id = underTest.listAllPatrons()
                        .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Patron::getId)
                .findFirst()
                .orElseThrow();

        Optional<Patron> actual = underTest.getPatronById(id);

        assertThat(actual).isPresent().hasValueSatisfying( c-> {
                    assertThat(c.getId()).isEqualTo(id);
                    assertThat(c.getName()).isEqualTo(patron.getName());
                    assertThat(c.getAge()).isEqualTo(patron.getAge());
                    assertThat(c.getEmail()).isEqualTo(patron.getEmail());
        });
    }

    @Test
    void returnEmptyIfPatronNotExist(){
        int id = -1;

        var actual = underTest.getPatronById(id);

        assertThat(actual).isEmpty();
    }

    @Test
    void addPatron() {
        String email = FAKER.internet().safeEmailAddress();
        Patron patron = new Patron(
                FAKER.name().fullName(),
                email,
                FAKER.number().numberBetween(16,75)
        );
        underTest.addPatron(patron);

        int id = underTest.listAllPatrons().stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Patron::getId)
                .findFirst()
                .orElseThrow();

        var actual = underTest.getPatronById(id);

        assertThat(actual).isPresent().hasValueSatisfying(c-> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(patron.getName());
            assertThat(c.getAge()).isEqualTo(patron.getAge());
            assertThat(c.getEmail()).isEqualTo(patron.getEmail());
        });
    }

    @Test
    void deletePatronById() {
        // create patron
        String email = FAKER.internet().safeEmailAddress();

        Patron patron = new Patron(
                FAKER.name().fullName(),
                email,
                FAKER.number().numberBetween(16,75)
        );
        underTest.addPatron(patron);

        int id = underTest.listAllPatrons()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Patron::getId)
                .findFirst()
                .orElseThrow();

        // check if created | exist in db
        var actual = underTest.getPatronById(id);

        assertThat(actual).isPresent().hasValueSatisfying(c-> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(patron.getName());
            assertThat(c.getAge()).isEqualTo(patron.getAge());
            assertThat(c.getEmail()).isEqualTo(patron.getEmail());
        });

        // delete
        underTest.deletePatronById(id);

        // check if deleted
        var statusAfterDelete = underTest.getPatronById(id);

        assertThat(statusAfterDelete).isNotPresent();
    }

    @Test
    void updatePatron() {
        // add Patron
        String email = FAKER.internet().safeEmailAddress();

        Patron patron = new Patron(
                FAKER.name().fullName(),
                email,
                FAKER.number().numberBetween(16,75)
        );
        underTest.addPatron(patron);

        int id = underTest.listAllPatrons()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Patron::getId)
                .findFirst()
                .orElseThrow();

        // update Patron field
        String newEmail = "update" + email;

        Patron patron1 = new Patron();
        patron1.setId(id);
        patron1.setName(FAKER.funnyName().name());
        patron1.setEmail(newEmail);
        patron1.setAge(78);

        underTest.updatePatron(patron);

        // check if updated
        Optional<Patron> actual = underTest.getPatronById(id);

        assertThat(actual).isPresent().hasValueSatisfying(c-> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(patron.getName());
            assertThat(c.getAge()).isEqualTo(patron.getAge());
            assertThat(c.getEmail()).isEqualTo(patron.getEmail());
        });
    }

//    @Test
//    void updatePatronWithoutAnyFieldChange() {
//        // add Patron
//        int patronId = 12;
//        String email = FAKER.internet().safeEmailAddress();
//        Patron patron = new Patron(
//                FAKER.name().fullName(),
//                email,
//                patronId
//        );
//        underTest.addPatron(patron);
//
//        int id = underTest.listAllPatrons()
//                .stream()
//                .filter(c -> c.getEmail().equals(email))
//                .map(Patron::getId)
//                .findFirst()
//                .orElseThrow();
//
//        // update without any change
//        Patron patron1 = new Patron();
//        patron1.setId(id);
//
//        underTest.updatePatron(patron1);
//
//        // verify none of field values changed
//        Optional<Patron> actual = underTest.getPatronById(id);
//
//        assertThat(actual).isPresent().hasValueSatisfying(c-> {
//            assertThat(c.getId()).isEqualTo(id);
//            assertThat(c.getName()).isEqualTo(patron.getName());
//            assertThat(c.getAge()).isEqualTo(patron.getAge());
//            assertThat(c.getEmail()).isEqualTo(patron.getEmail());
//        });
//    }

    @Test
    void existsPersonWithEmail() {
        String email = FAKER.internet().safeEmailAddress();
        int patronId = FAKER.number().numberBetween(16,75);
        Patron patron = new Patron(
                FAKER.name().fullName(),
                email,
                patronId
        );
        underTest.addPatron(patron);


        boolean actual = underTest.existsPersonWithEmail(email);

        assertThat(actual).isTrue();
    }

    @Test
    void existsPersonWithEmailReturnFalseIfEmailNotExist(){
        String email = FAKER.internet().safeEmailAddress()+ "not-exist";

        boolean actual = underTest.existsPersonWithEmail(email);

        assertThat(actual).isFalse();
    }

//    @Test
//    void existsPersonWithId() {
//        String email = FAKER.internet().safeEmailAddress();
//        int patronId = FAKER.number().numberBetween(16,75);
//        Patron patron = new Patron(
//                FAKER.name().fullName(),
//                email,
//                patronId
//        );
//        underTest.addPatron(patron);
//
//        // get id
//        int id = underTest.listAllPatrons()
//                .stream()
//                .filter(c -> c.getEmail().equals(email))
//                .map(Patron::getId)
//                .findFirst()
//                .orElseThrow();
//
//        var actual = underTest.existsPersonWithId(id);
//
//        assertThat(actual).isTrue();
//    }

    @Test
    void existsPersonWithIdReturnFalseIfIdNotExist(){
        int id = -1;
        boolean actual = underTest.existsPersonWithId(id);

        assertThat(actual).isFalse();
    }
}