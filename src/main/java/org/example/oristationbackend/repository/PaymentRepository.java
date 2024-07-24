package org.example.oristationbackend.repository;

import org.example.oristationbackend.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    boolean existsByMerchantUid(String merchantUid);
    boolean existsByImpUid(String impUid);
}
