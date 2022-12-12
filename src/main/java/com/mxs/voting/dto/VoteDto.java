package com.mxs.voting.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VoteDto implements Serializable {
    private static final long serialVersionUID = -2069186850042599082L;
    private String agendaCode;
    private String voteCode;
    private String votingOptionCode;
}
