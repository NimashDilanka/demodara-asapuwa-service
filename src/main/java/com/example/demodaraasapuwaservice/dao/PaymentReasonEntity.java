package com.example.demodaraasapuwaservice.dao;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "payment_reason", schema = "panhinda")
public class PaymentReasonEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Column(name = "code", nullable = false, length = 200)
    private String code;
    @Column(name = "value", nullable = false, length = 200)
    private String value;
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "added_date", nullable = false)
    private Date addedDate;
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_modify_date", nullable = false)
    private Date lastModifyDate;
}
