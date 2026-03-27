package com.ecommerce.sb_ecommerce.respository;

import com.ecommerce.sb_ecommerce.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
