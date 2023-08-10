package com.spring.patron;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository("list")
public class PatronListDataAccessService implements PatronDao{

    private static final List<Patron> patrons;

    static{
        patrons = new ArrayList<>();

        Patron john = new Patron(
                1,
                "John",
                "john@hotmail.com",
                21
        );
        Patron jane = new Patron(
                2,
                "Jane",
                "Jane@hotmail.com",
                23
        );
        patrons.add(john);
        patrons.add(jane);

    }

    @Override
    public List<Patron> listAllPatrons() {
        return patrons;
    }

    @Override
    public Optional<Patron> getPatronById(Integer id) {
        return patrons.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst();
    }

    @Override
    public void addPatron(Patron patron) {
        patrons.add(patron);
    }

    @Override
    public boolean existsPersonWithEmail(String email) {
        return patrons.stream()
                .anyMatch(c -> c.getEmail().equals(email));
    }

    @Override
    public boolean existsPersonWithId(Integer id) {
        return patrons.stream()
                .anyMatch(c -> c.getId().equals(id));
    }

    @Override
    public void deletePatronById(Integer id) {
        patrons.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .ifPresent(patrons::remove);
    }

    @Override
    public void updatePatron(Patron patron) {
        patrons.add(patron);
    }
}
