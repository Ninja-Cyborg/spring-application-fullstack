package com.spring.patron;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("jdbc")
public class PatronJDBCDataAccessService implements PatronDao{

    private final JdbcTemplate jdbcTemplate;
    private final PatronRowMapper patronRowMapper;

    public PatronJDBCDataAccessService(JdbcTemplate jdbcTemplate, PatronRowMapper patronRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.patronRowMapper = patronRowMapper;
    }

    @Override
    public List<Patron> listAllPatrons() {
        var sql = """
                SELECT id, name, email, age
                FROM patron
                """;

        return jdbcTemplate.query(sql, patronRowMapper);
    }

    @Override
    public Optional<Patron> getPatronById(Integer id) {
        var sql = """
                SELECT id, name, email, age
                FROM patron
                WHERE id = ?
                """;

        return jdbcTemplate.query(sql, patronRowMapper, id)
                .stream().findFirst();
    }

    @Override
    public void addPatron(Patron patron) {
        var sql = """
                INSERT INTO patron (name, email, age)
                VALUES (?,?,?)
                """;
        jdbcTemplate.update(sql,
                patron.getName(),
                patron.getEmail(),
                patron.getAge());
    }

    @Override
    public void deletePatronById(Integer id) {
        var sql = """
                DELETE
                FROM patron
                WHERE id = ?
                """;
        jdbcTemplate.update(sql, id);
    }

    @Override
    public void updatePatron(Patron patron) {
        if(patron.getName() != null){
            String query = "Update patron SET name = ? WHERE id = ?";
            jdbcTemplate.update(query,
                    patron.getName(),
                    patron.getId());
        }
        if(patron.getAge() == 0){
            String query = "Update patron SET age = ? WHERE id = ?";
            jdbcTemplate.update(query,
                    patron.getAge(),
                    patron.getId());
        }
        if(patron.getEmail() != null){
            String query = "Update patron SET email = ? WHERE id = ?";
            jdbcTemplate.update(query,
                    patron.getEmail(),
                    patron.getId());
        }
    }

    @Override
    public boolean existsPersonWithEmail(String email) {
        var sql = """
                SELECT count(id)
                FROM patron
                WHERE email = ?
                """;
        int count = jdbcTemplate.queryForObject(sql, int.class, email);
        return count > 0;
    }

    @Override
    public boolean existsPersonWithId(Integer id) {
        var sql = """
                SELECT count(id)
                FROM patron
                WHERE id = ?
                """;
        int count = jdbcTemplate.queryForObject(sql, int.class, id);
        return count > 0;
    }
}
