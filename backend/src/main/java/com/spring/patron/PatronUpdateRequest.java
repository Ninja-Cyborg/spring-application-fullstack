package com.spring.patron;

public record PatronUpdateRequest(
        String name,
        String email,
        Integer age) {
}
