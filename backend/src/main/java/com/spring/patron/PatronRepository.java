package com.spring.patron;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

//@Repository
@Transactional
public interface PatronRepository extends JpaRepository<Patron, Integer> {
    boolean existsPatronByEmail(String email);
    boolean existsPatronById(Integer id);
}
