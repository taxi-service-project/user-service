package com.example.user_service.repository;

import com.example.user_service.entity.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {
    boolean existsByUserId(Long userId);
    List<PaymentMethod> findByUserId(Long userId);
}
