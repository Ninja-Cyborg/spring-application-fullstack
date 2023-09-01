package com.spring.patron;

public record PatronRegistrationRequest(
        String name,
        String email,
        Integer age,
        Gender gender) {
}
