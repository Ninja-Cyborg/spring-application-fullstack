package com.spring.patron;

import com.spring.exception.DuplicateResourceException;
import com.spring.exception.RequestValidationException;
import com.spring.exception.ResourceNotFound;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PatronServiceTest {

    @Mock
    private PatronDao patronDao;
    private PatronService underTest;

    @BeforeEach
    void setUp() {
        underTest = new PatronService(patronDao);
    }

    @Test
    void getAllPatrons() {
        underTest.getAllPatrons();

        verify(patronDao).listAllPatrons();
    }

    @Test
    void getPatron() {
        int id = 7;
        Patron patron = new Patron(
                id, "Jay","Jay@email.co",25,
                Gender.MALE);

        when(patronDao.getPatronById(id))
                .thenReturn(Optional.of(patron));

        Patron actual = underTest.getPatron(7);

        assertThat(actual).isEqualTo(patron);
    }

    @Test
    void willThrowExceptionWhenGetPatronIsEmpty(){
        int id = 7;

        when(patronDao.getPatronById(id))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> underTest.getPatron(id))
                .isInstanceOf(ResourceNotFound.class)
                .hasMessageContaining("Patron with id [%s] does not exist.".formatted(id));
    }

    @Test
    void addPatron() {

        String email = "jo@mail.corp";
        when(patronDao.existsPersonWithEmail(email))
                .thenReturn(false);

        PatronRegistrationRequest request = new PatronRegistrationRequest(
                "Jo", email, 18, Gender.MALE
        );

        underTest.addPatron(request);
        ArgumentCaptor<Patron> patronArgumentCaptor = ArgumentCaptor.forClass(
                Patron.class
        );

        verify(patronDao).addPatron(patronArgumentCaptor.capture());

        Patron capturedPatron = patronArgumentCaptor.getValue();

        assertThat(capturedPatron.getId()).isNull();
        assertThat(capturedPatron.getName()).isEqualTo(request.name());
        assertThat(capturedPatron.getEmail()).isEqualTo(request.email());
        assertThat(capturedPatron.getAge()).isEqualTo(request.age());
        assertThat(capturedPatron.getGender()).isEqualTo(request.gender());

    }

    @Test
    void addPatronThrowEmailWhenEmailExist() {

        String email = "jo@mail.corp";
        when(patronDao.existsPersonWithEmail(email)).thenReturn(true);

        PatronRegistrationRequest request = new PatronRegistrationRequest(
                "Jo", email, 18, Gender.MALE
        );

        // adding patron throw exception
        assertThatThrownBy(() -> underTest.addPatron(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("Email already taken");

        // verify no patron is inserted
        verify(patronDao, never()).addPatron(any());
    }

    @Test
    void deletePatronById() {
        int id = 10;

        when(patronDao.existsPersonWithId(id)).thenReturn(true);

        underTest.deletePatronById(id);

        verify(patronDao).deletePatronById(id);
    }

    @Test
    void updatePatron() {

        int id = 8;
        String email = "jo@mail.corp";
        Gender gender = Gender.MALE;
        Patron patron = new Patron(id, "Jo", email, 18, gender);

        when(patronDao.getPatronById(id)).thenReturn(Optional.of(patron));

        // update patron
        String updateEmail = email + ".co";
        PatronUpdateRequest updateRequest = new PatronUpdateRequest("Joe", updateEmail, 19, gender);
        when(patronDao.existsPersonWithEmail(updateEmail)).thenReturn(false);

        underTest.updatePatron(id, updateRequest);

        ArgumentCaptor<Patron> patronArgumentCaptor = ArgumentCaptor.forClass(Patron.class);

        verify(patronDao).updatePatron(patronArgumentCaptor.capture());

        Patron capturedPatron = patronArgumentCaptor.getValue();

        assertThat(capturedPatron.getName()).isEqualTo(updateRequest.name());
        assertThat(capturedPatron.getEmail()).isEqualTo(updateRequest.email());
        assertThat(capturedPatron.getAge()).isEqualTo(updateRequest.age());
        assertThat(capturedPatron.getGender()).isEqualTo(updateRequest.gender());

    }

    @Test
    void doNotUpdatePatronWhenNoChangesPassed(){
        int id = 12;
        Gender gender = Gender.MALE;
        Patron patron = new Patron(id, "Jo", "jo@mail.corp", 18, gender);
        when(patronDao.getPatronById(id)).thenReturn(Optional.of(patron));

        // pass update request with same values
        PatronUpdateRequest updateRequest = new PatronUpdateRequest(patron.getName(), patron.getEmail(), patron.getAge(), patron.getGender());

        assertThatThrownBy(() -> underTest.updatePatron(id, updateRequest))
                .isInstanceOf(RequestValidationException.class).hasMessage("no data changes found");

        // verify patrons do not change
        verify(patronDao, never()).updatePatron(any());
    }
}