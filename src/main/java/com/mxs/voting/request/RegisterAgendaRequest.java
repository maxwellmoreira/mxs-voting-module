package com.mxs.voting.request;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
public class RegisterAgendaRequest {
    @NotBlank(message = "Title is mandatory")
    private String title;
    @NotBlank(message = "Description is mandatory")
    private String description;
    @Max(value = 480, message = "Maximum duration is 480 minutes")
    @Min(value = 1, message = "Minimum duration is 1 minute")
    private long duration;
}
