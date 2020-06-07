package com.example.demodaraasapuwaservice.entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Date;
import java.util.Objects;

@Entity
@Data
@Table(name = "system_property", schema = "panhinda")
public class SystemPropertyEntity {
    @Id
    @Column(name = "id", nullable = false)
    private int id;
    @Column(name = "code", nullable = false, length = 200)
    private String code;
    @Column(name = "value", nullable = false, length = 200)
    private String value;
    @Column(name = "enable", nullable = false)
    private byte enable;
    @Column(name = "added_date", nullable = false)
    private Date addedDate;
    @Column(name = "last_modify_date", nullable = false)
    private Date lastModifyDate;
}
