package com.example.demodaraasapuwaservice.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

@Entity
@Data
@Table(name = "user", schema = "panhinda")
public class UserEntity {
    @Id
    @Column(name = "id", nullable = false)
    private int id;
    @Column(name = "name", nullable = false, length = 200)
    private String name;
    @OneToMany(mappedBy = "userByUserId")
    private Collection<ProcessBatchEntity> processBatchesById;
}
