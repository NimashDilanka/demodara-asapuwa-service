package com.example.demodaraasapuwaservice.entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Date;
import java.util.Collection;
import java.util.Objects;

@Entity
@Data
@Table(name = "process_batch", schema = "panhinda")
public class ProcessBatchEntity {
    @Id
    @Column(name = "id", nullable = false)
    private int id;
    @Column(name = "date", nullable = false)
    private Date date;
    @ManyToOne
    @JoinColumn(name = "process_type_id", referencedColumnName = "id", nullable = false)
    private ProcessTypeEntity processTypeByProcessTypeId;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private UserEntity userByUserId;
    @OneToMany(mappedBy = "processBatchByProcessBatchId")
    private Collection<RowDataEntity> rowDataById;
}
