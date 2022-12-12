package com.mxs.voting.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VoteCountingDto {
    private String code;
    private String description;
    private int amountVotes;
    private float votesPercentage;
    private int position;
    private Boolean hasWon;
}
