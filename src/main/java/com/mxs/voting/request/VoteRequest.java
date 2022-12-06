package com.mxs.voting.request;

import com.mxs.voting.type.VoteType;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class VoteRequest {
    @NotBlank(message = "Agenda is mandatory")
    private String agendaCode;
    @NotNull(message = "Vote is mandatory")
    private VoteType voteType;
}
