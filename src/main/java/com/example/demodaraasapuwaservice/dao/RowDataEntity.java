package com.example.demodaraasapuwaservice.dao;

import lombok.*;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "row_data", schema = "panhinda")
public class RowDataEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
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
