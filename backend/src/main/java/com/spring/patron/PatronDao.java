package com.spring.patron;

import java.util.List;
import java.util.Optional;

public interface PatronDao {
    List<Patron> listAllPatrons();
    Optional<Patron> getPatronById(Integer id);
    void addPatron(Patron patron);
    void deletePatronById(Integer id);
    void updatePatron(Patron patron);
    boolean existsPersonWithEmail(String email);
    boolean existsPersonWithId(Integer id);
}
