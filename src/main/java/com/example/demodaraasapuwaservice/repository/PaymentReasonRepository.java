package com.example.demodaraasapuwaservice.repository;

import com.example.demodaraasapuwaservice.dao.PaymentReasonEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentReasonRepository extends JpaRepository<PaymentReasonEntity, Integer> {
}
