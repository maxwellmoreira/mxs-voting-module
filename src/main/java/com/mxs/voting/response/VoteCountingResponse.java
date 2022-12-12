package com.mxs.voting.response;

import com.mxs.voting.dto.VoteCountingDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VoteCountingResponse {
    private String agendaCode;
    private String title;
    private LocalDateTime started;
    private LocalDateTime ended;
    private int totalAmountVotes;
    private String winnerCode;
    private String winner;
    private List<VoteCountingDto> voteCountingDtoList;
}
