package com.ecommerce.sb_ecommerce.respository;

import com.ecommerce.sb_ecommerce.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
