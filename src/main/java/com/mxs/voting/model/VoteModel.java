package com.mxs.voting.model;

import com.mxs.voting.type.VoteType;
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
    @Column(name = "vote", nullable = false, updatable = false, length = 1)
    private String vote;
    @Transient
    private VoteType voteType;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agenda_code", nullable = false, updatable = false)
    private AgendaModel agendaModel;

    @PrePersist
    public void fillStatus() {
        if (voteType != null) {
            vote = voteType.getCode();
        }
    }

    @PostLoad
    public void fillStatusType() {
        if (!vote.isBlank()) {
            voteType = VoteType.of(vote);
        }
    }

    public void setVoteType(VoteType voteType) {
        this.voteType = voteType;
        vote = voteType.getCode();
    }
}
