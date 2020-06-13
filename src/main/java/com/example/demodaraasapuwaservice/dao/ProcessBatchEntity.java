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
@Table(name = "process_batch", schema = "panhinda")
public class ProcessBatchEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
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
