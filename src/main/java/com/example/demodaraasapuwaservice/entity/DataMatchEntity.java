package com.example.demodaraasapuwaservice.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Data
@Table(name = "data_match", schema = "panhinda")
public class DataMatchEntity {
    @Id
    @Column(name = "id", nullable = false)
    private int id;
    @ManyToOne
    @JoinColumn(name = "member_id", referencedColumnName = "id", nullable = false)
    private MemberEntity memberByMemberId;
    @ManyToOne
    @JoinColumn(name = "raw_data_id", referencedColumnName = "id", nullable = false)
    private RowDataEntity rowDataByRawDataId;
}
