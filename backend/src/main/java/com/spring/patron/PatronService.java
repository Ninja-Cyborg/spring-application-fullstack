package com.spring.patron;

import com.spring.exception.DuplicateResourceException;
import com.spring.exception.RequestValidationException;
import com.spring.exception.ResourceNotFound;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatronService {

    private final PatronDao patronDao;

    public PatronService(@Qualifier("jdbc")PatronDao patronDao){ this.patronDao = patronDao; }

    public List<Patron> getAllPatrons(){ return patronDao.listAllPatrons(); }

    public Patron getPatron(Integer id){

        return patronDao.getPatronById(id)
                .orElseThrow(() ->
                        new ResourceNotFound(
                                "Patron with id [%s] does not exist.".formatted(id)));
    }

    public void addPatron(PatronRegistrationRequest request) {
        String email = request.email();
        if(patronDao.existsPersonWithEmail(email)){
            throw new DuplicateResourceException(
                    "Email already taken");
        }

        Patron patron = new Patron(
                request.name(), request.email(), request.age(),
                request.gender());

        patronDao.addPatron(patron);
    }

    public void deletePatronById(Integer id) {

        if(!patronDao.existsPersonWithId(id)){
            throw new ResourceNotFound("Patron does not exist with id: " + id);
        }

        patronDao.deletePatronById(id);
    }

    public void updatePatron(Integer patronId,
                               PatronUpdateRequest updateRequest) {
        // TODO: for JPA use .getReferenceById(patronId) as it does does not bring object into memory and instead a reference
        Patron patron = getPatron(patronId);

        boolean changes = false;

        if (updateRequest.name() != null && !updateRequest.name().equals(patron.getName())) {
            patron.setName(updateRequest.name());
            changes = true;
        }

        if (updateRequest.age() != null && !updateRequest.age().equals(patron.getAge())) {
            patron.setAge(updateRequest.age());
            changes = true;
        }

        if (updateRequest.email() != null && !updateRequest.email().equals(patron.getEmail())) {
            if (patronDao.existsPersonWithEmail(updateRequest.email())) {
                throw new DuplicateResourceException(
                        "email already taken"
                );
            }
            patron.setEmail(updateRequest.email());
            changes = true;
        }

        if (!changes) {
            throw new RequestValidationException("no data changes found");
        }

        patronDao.updatePatron(patron);
    }
}
