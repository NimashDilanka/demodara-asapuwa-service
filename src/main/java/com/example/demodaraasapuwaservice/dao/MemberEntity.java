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
@Table(name = "member", schema = "panhinda")
public class MemberEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Column(name = "name", nullable = false, length = 200)
    private String name;
    @Column(name = "amount", nullable = false)
    private int amount;
    @Column(name = "description", nullable = true, length = 200)
    private String description;
    @Column(name = "transaction_date", nullable = true)
    private Date transactionDate;
    @Column(name = "tp_no", nullable = false, length = 100)
    private String tpNo;
    @Column(name = "email", nullable = true, length = 100)
    private String email;
    @Column(name = "added_date", nullable = false)
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date addedDate;
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_modify_date", nullable = false)
    private Date lastModifyDate;
    @ManyToOne
    @JoinColumn(name = "image_id", referencedColumnName = "id")
    private ImageEntity imageByImageId;
}
