package com.github.manerajona.auth.exception.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    INVALID_FIELD_VALUE("The provided field is not valid"),
    BAD_CREDENTIALS("The user cannot be authenticated"),
    ROLE_INVALID("The user does not have access to the resource");

    private final String defaultMessage;

}
