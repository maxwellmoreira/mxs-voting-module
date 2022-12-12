package com.mxs.voting.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Table(name = "voting_option")
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VotingOptionModel extends AuditModel {
    @Column(name = "description", nullable = false, updatable = false)
    private String description;
    @Column(name = "amount_votes")
    private int amountVotes;
    @Column(name = "percentage_votes")
    private float percentageVotes;
    @Column(name = "position")
    private int position;
    @Column(name = "has_won")
    private Boolean hasWon;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agenda_id", nullable = false, updatable = false)
    private AgendaModel agendaModel;
}
