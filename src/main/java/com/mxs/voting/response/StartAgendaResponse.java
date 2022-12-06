package com.mxs.voting.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StartAgendaResponse {
    private String code;
    private String status;
    private LocalDateTime started;
    private LocalDateTime ended;
}
