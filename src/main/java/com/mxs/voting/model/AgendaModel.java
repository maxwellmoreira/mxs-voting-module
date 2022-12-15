package com.mxs.voting.model;

import com.mxs.voting.type.AgendaStatusType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table(name = "agenda")
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AgendaModel extends AuditModel {
    @Column(name = "title", nullable = false)
    private String title;
    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;
    @Column(name = "duration", nullable = false)
    private long duration;
    @Column(name = "started")
    private LocalDateTime started;
    @Column(name = "ended")
    private LocalDateTime ended;
    @Column(name = "total_amount_votes")
    private int totalAmountVotes;
    @Column(name = "winner", length = 36)
    private String winner;
    @Column(name = "status_agenda", nullable = false, length = 1)
    private String statusAgenda;
    @Transient
    private AgendaStatusType agendaStatusType;

    @PrePersist
    public void fillStatusAgenda() {
        if (agendaStatusType != null) {
            statusAgenda = agendaStatusType.getCode();
        } else {
            statusAgenda = AgendaStatusType.CREATED.getCode();
        }
    }

    @PostLoad
    public void fillStatusAgendaType() {
        if (!statusAgenda.isBlank()) {
            agendaStatusType = AgendaStatusType.of(statusAgenda);
        }
    }

    public void setAgendaStatusType(AgendaStatusType agendaStatusType) {
        this.agendaStatusType = agendaStatusType;
        statusAgenda = agendaStatusType.getCode();
    }
}
