package com.spring.patron;

import org.hibernate.annotations.Comment;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class PatronRowMapper implements RowMapper {
    @Override
    public Patron mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Patron(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getInt("age")
        );
    }
}
