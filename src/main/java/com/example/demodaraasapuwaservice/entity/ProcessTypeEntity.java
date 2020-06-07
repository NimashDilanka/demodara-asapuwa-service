package com.example.demodaraasapuwaservice.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

@Entity
@Data
@Table(name = "process_type", schema = "panhinda")
public class ProcessTypeEntity {
    @Id
    @Column(name = "id", nullable = false)
    private int id;
    @Column(name = "type", nullable = false, length = 200)
    private String type;
    @OneToMany(mappedBy = "processTypeByProcessTypeId")
    private Collection<ProcessBatchEntity> processBatchesById;
}
