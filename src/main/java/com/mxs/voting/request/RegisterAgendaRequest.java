package com.mxs.voting.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class RegisterAgendaRequest {
    @NotBlank(message = "Title is mandatory")
    private String title;
    @NotBlank(message = "Description is mandatory")
    private String description;
    private long duration;
}
