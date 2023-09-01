package com.spring.patron;

import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PatronRowMapperTest {

    @Test
    void mapRow() throws SQLException {
        // Given row mapper
        PatronRowMapper patronRowMapper = new PatronRowMapper();

        // mock
        ResultSet resultSet = mock(ResultSet.class);

        when(resultSet.getInt("id")).thenReturn(1);
        when(resultSet.getString("name")).thenReturn("Jake");
        when(resultSet.getString("email")).thenReturn("Jake@corp.ca");
        when(resultSet.getInt("age")).thenReturn(21);
        when(resultSet.getString("gender")).thenReturn("MALE");
        // When row is mapped
        Patron actual = patronRowMapper.mapRow(resultSet,1);

        // Then assertion
        Patron expected = new Patron(1, "Jake", "Jake@corp.ca",21, Gender.MALE);
        assertThat(actual).isEqualTo(expected);

    }
}