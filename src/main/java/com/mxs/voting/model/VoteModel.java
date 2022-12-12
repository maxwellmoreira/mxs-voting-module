package com.mxs.voting.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Table(name = "vote")
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VoteModel extends AuditModel {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agenda_id", nullable = false, updatable = false)
    private AgendaModel agendaModel;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "voting_option_id", nullable = false, updatable = false)
    private VotingOptionModel votingOptionModel;
}
