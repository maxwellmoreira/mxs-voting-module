package com.mxs.voting.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CreateVotingOptionRequest {
    @NotBlank(message = "Description is mandatory")
    private String description;
    @NotBlank(message = "Agenda is mandatory")
    private String agendaCode;
}
