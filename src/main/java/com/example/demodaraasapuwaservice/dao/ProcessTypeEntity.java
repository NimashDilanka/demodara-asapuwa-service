package com.example.demodaraasapuwaservice.dao;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "process_type", schema = "panhinda")
public class ProcessTypeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Column(name = "type", nullable = false, length = 200)
    private String type;
    @OneToMany(mappedBy = "processTypeByProcessTypeId")
    private List<ProcessBatchEntity> processBatchesById;
}
