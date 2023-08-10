package com.spring.patron;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("jpa")
public class PatronJPADataAccessService implements PatronDao{

    private final PatronRepository patronRepository;

    public PatronJPADataAccessService(PatronRepository patronRepository) {
        this.patronRepository = patronRepository;
    }

    @Override
    public List<Patron> listAllPatrons() {
        return patronRepository.findAll();
    }

    @Override
    public Optional<Patron> getPatronById(Integer id) {
        return patronRepository.findById(id);
    }

    @Override
    public void addPatron(Patron patron) {
        patronRepository.save(patron);
    }

    @Override
    public void deletePatronById(Integer id) {
        patronRepository.deleteById(id);
    }

    @Override
    public void updatePatron(Patron patron) {
        patronRepository.save(patron);
    }

    @Override
    public boolean existsPersonWithEmail(String email) {
        return patronRepository.existsPatronByEmail(email);
    }

    @Override
    public boolean existsPersonWithId(Integer id) {
        return patronRepository.existsPatronById(id);
    }

}
