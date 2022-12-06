package com.mxs.voting.dto;

import com.mxs.voting.type.VoteType;
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
    private static final long serialVersionUID = 5166447783808250649L;
    private String voteCode;
    private String agendaCode;
    private VoteType voteType;
}
