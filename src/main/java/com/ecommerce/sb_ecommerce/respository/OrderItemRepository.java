package com.ecommerce.sb_ecommerce.respository;

import com.ecommerce.sb_ecommerce.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
