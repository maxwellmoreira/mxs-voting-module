package com.mxs.voting.model;

import com.mxs.voting.type.StatusType;
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

    @Column(name = "title", nullable = false, length = 60)
    private String title;
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    @Column(name = "duration", nullable = false)
    private long duration;
    @Column(name = "started")
    private LocalDateTime started;
    @Column(name = "ended")
    private LocalDateTime ended;
    @Column(name = "status", nullable = false, length = 1)
    private String status;
    @Transient
    private StatusType statusType;

    @PrePersist
    public void fillStatus() {
        if (statusType != null) {
            status = statusType.getCode();
        }
    }

    @PostLoad
    public void fillStatusType() {
        if (!status.isBlank()) {
            statusType = StatusType.of(status);
        }
    }

    public void setStatusType(StatusType statusType) {
        this.statusType = statusType;
        status = statusType.getCode();
    }
}
