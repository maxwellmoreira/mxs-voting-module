package com.mxs.voting.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum AgendaStatusType {
    CREATED("C"),
    VOTING("V"),
    FINISHED("F"),
    INTERRUPTED("I");

    private String code;

    public static AgendaStatusType of(String code) {
        return Stream.of(AgendaStatusType.values())
                .filter(agendaStatus -> agendaStatus.getCode().equalsIgnoreCase(code))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}