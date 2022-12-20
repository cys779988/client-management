package dev.be.domain.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

    ADMIN("ROLE_ADMIN", "ADMIN"),
    GUEST("ROLE_GUEST", "GUEST");

    private final String key;
    private final String value;
}