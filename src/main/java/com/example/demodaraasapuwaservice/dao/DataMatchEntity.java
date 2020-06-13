package com.example.demodaraasapuwaservice.dao;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "data_match", schema = "panhinda")
public class DataMatchEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "member_id", referencedColumnName = "id", nullable = false)
    private MemberEntity memberByMemberId;
    @ManyToOne
    @JoinColumn(name = "raw_data_id", referencedColumnName = "id", nullable = false)
    private RowDataEntity rowDataByRawDataId;
}
