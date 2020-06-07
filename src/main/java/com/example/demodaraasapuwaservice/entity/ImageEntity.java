package com.example.demodaraasapuwaservice.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Data
@Table(name = "image", schema = "panhinda")
public class ImageEntity {
    @Id
    @Column(name = "id", nullable = false)
    private int id;
    @Column(name = "path", nullable = false, length = 200)
    private String path;
    @OneToMany(mappedBy = "imageByImageId")
    private Collection<MemberEntity> membersById;
}
