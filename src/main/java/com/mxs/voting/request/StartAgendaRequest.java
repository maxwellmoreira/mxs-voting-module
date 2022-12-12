package com.mxs.voting.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class StartAgendaRequest {
    @NotBlank(message = "Agenda is mandatory")
    private String agendaCode;
}
