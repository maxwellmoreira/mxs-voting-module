package com.mxs.voting.model;

import com.mxs.voting.type.StatusType;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class AuditModel implements Serializable {
    private static final long serialVersionUID = -1262070442705437088L;
    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private Long id;
    @Column(name = "code", nullable = false, updatable = false, length = 36)
    private String code;
    @Column(name = "created_date", nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime createdDate;
    @Column(name = "last_modified_date", nullable = false)
    @LastModifiedDate
    private LocalDateTime lastModifiedDate;
    @Column(name = "status", nullable = false, length = 1)
    private String status;
    @Transient
    private StatusType statusType;

    @PrePersist
    public void fillStatus() {
        if (code == null) {
            UUID uuid = UUID.randomUUID();
            code = uuid.toString();
        }

        if (statusType == null) {
            status = StatusType.ACTIVE.getCode();
        } else {
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
