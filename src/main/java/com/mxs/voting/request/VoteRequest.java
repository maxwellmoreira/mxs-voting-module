package com.mxs.voting.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class VoteRequest {
    @NotBlank(message = "Agenda is mandatory")
    private String agendaCode;
    @NotBlank(message = "Voting option is mandatory")
    private String votingOptionCode;
}
