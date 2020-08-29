package com.example.demodaraasapuwaservice.repository;

import com.example.demodaraasapuwaservice.dao.SystemPropertyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SystemPropertyRepository extends JpaRepository<SystemPropertyEntity,Integer> {
    public Optional<SystemPropertyEntity> getByCode(String code);
}
