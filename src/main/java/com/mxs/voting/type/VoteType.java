package com.mxs.voting.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum VoteType {
    YES("Y"),
    NO("N");

    private String code;

    public static VoteType of(String code) {
        return Stream.of(VoteType.values())
                .filter(vote -> vote.getCode().equalsIgnoreCase(code))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
