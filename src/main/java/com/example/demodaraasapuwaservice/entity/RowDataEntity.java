package com.example.demodaraasapuwaservice.entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Date;
import java.util.Collection;
import java.util.Objects;

@Entity
@Data
@Table(name = "row_data", schema = "panhinda")
public class RowDataEntity {
    @Id
    @Column(name = "id", nullable = false)
    private int id;
    @Column(name = "txn_date", nullable = true)
    private Date txnDate;
    @Column(name = "description", nullable = true, length = 200)
    private String description;
    @Column(name = "cr", nullable = true, length = 200)
    private String cr;
    @OneToMany(mappedBy = "rowDataByRawDataId")
    private Collection<DataMatchEntity> dataMatchesById;
    @ManyToOne
    @JoinColumn(name = "process_batch_id", referencedColumnName = "id", nullable = false)
    private ProcessBatchEntity processBatchByProcessBatchId;
}
