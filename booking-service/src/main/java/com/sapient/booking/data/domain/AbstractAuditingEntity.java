package com.sapient.booking.data.domain;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.UUID;


/**
 * Abstract Entity used to add Audit fields to all entities
 */
@MappedSuperclass
@Data
@EntityListeners(AuditingEntityListener.class)
public class AbstractAuditingEntity {

    @Column(name = "created_by", nullable = false, updatable = false)
    private UUID createdBy;

    @Column(name = "created_date", updatable = false)
    private LocalDate createdDate ;


    @Column(name = "last_modified_by")
    private UUID lastModifiedBy;

    @Column(name = "last_modified_date")
    private LocalDate lastModifiedDate ;

    @PrePersist
    public void prePersist(){
        setCreatedBy(UUID.randomUUID()); //TODO: This needs to come from Spring Security Context
        setLastModifiedBy(UUID.randomUUID());  //TODO: This needs to come from Spring Security Context
        setCreatedDate(LocalDate.now());
        setLastModifiedDate(LocalDate.now());
    }

    @PreUpdate
    public void preUpdate(){
        setLastModifiedBy(UUID.randomUUID());  //TODO: This needs to come from Spring Security Context
        setLastModifiedDate(LocalDate.now());
    }


}
