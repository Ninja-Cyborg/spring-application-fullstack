package com.spring.patron;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;

class PatronJPADataAccessServiceTest {

    private PatronJPADataAccessService underTest;
    private AutoCloseable autoCloseable;

    @Mock
    private PatronRepository patronRepository;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new PatronJPADataAccessService(patronRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void listAllPatrons() {
        underTest.listAllPatrons();

        verify(patronRepository).findAll();
    }

    @Test
    void getPatronById() {
        int id = 1;

        underTest.getPatronById(id);

        verify(patronRepository).findById(id);
    }

    @Test
    void addPatron() {
        Patron patron = new Patron(
                "Jake Kai",
                "Jake@hotmail.corp",
                21
        );
        underTest.addPatron(patron);

        verify(patronRepository).save(patron);
    }

    @Test
    void deletePatronById() {
        int id = 1;

        underTest.deletePatronById(id);

        verify(patronRepository).deleteById(id);

    }

    @Test
    void updatePatron() {
        Patron patron = new Patron(
                1,
                "Jake Kai",
                "Jake@hotmail.corp",
                21
        );
        // update person
        underTest.updatePatron(patron);

        verify(patronRepository).save(patron);
    }

    @Test
    void existsPersonWithEmail() {
        String email = "random.mail@red.corp";

        underTest.existsPersonWithEmail(email);

        verify(patronRepository).existsPatronByEmail(email);
    }

    @Test
    void existsPersonWithId() {
        int id = 1;

        underTest.existsPersonWithId(id);

        verify(patronRepository).existsPatronById(id);
    }
}