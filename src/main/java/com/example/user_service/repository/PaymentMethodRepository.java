package com.example.user_service.repository;

import com.example.user_service.entity.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {
    boolean existsByUserId(Long userId);
    List<PaymentMethod> findByUserId(Long userId);

    Optional<PaymentMethod> findByUserUserIdAndIsDefaultTrue(String userId);

}
