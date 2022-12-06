package com.mxs.voting.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class StartAgendaRequest {
    @NotBlank(message = "Code is mandatory")
    private String code;
}
