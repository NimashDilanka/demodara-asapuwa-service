package com.example.demodaraasapuwaservice.dao;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "address", schema = "panhinda")
public class AddressEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Column(name = "unit_no", unique = true, nullable = false, length = 200)
    private String unitNo;
    @Column(name = "street", unique = true, nullable = false, length = 200)
    private String Street;
    @Column(name = "town", unique = true, nullable = false, length = 100)
    private String town;
    @Column(name = "country", unique = true, nullable = false, length = 100)
    private String country;
    @OneToOne(mappedBy = "address")
    private MemberEntity memberEntity;

}
