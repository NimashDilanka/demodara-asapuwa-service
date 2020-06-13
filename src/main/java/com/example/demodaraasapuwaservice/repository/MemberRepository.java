package com.example.demodaraasapuwaservice.repository;

import com.example.demodaraasapuwaservice.dao.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberEntity, Integer> {
    Optional<MemberEntity> findByName(String name);
}
