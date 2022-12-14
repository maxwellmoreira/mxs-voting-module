package com.mxs.voting.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum StatusType {
    ACTIVE("A"),
    INACTIVE("I");

    private String code;

    public static StatusType of(String code) {
        return Stream.of(StatusType.values())
                .filter(status -> status.getCode().equalsIgnoreCase(code))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
