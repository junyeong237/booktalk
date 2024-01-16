package com.example.booktalk.domain.user.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserRoleType {
    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN"),
    BLOCK("ROLE_BLOCK");

    private final String authority;

}
