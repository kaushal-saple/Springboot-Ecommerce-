package com.ecommerce.sb_ecommerce.respository;

import com.ecommerce.sb_ecommerce.model.CartItem;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CartItemRepository extends JpaRepository<CartItem,Long> {

    @Query("SELECT ci FROM CartItem ci WHERE ci.product.productId = ?1 AND ci.cart.cartId = ?2")
    CartItem findCartItemByProductIdAndCartId(Long productId, Long cartId);

    @Transactional
    @Modifying
    @Query("DELETE FROM CartItem ci WHERE ci.product.productId = ?1 AND ci.cart.cartId = ?2")
    void deleteByProductProductIdAndCartCartId(Long productId, Long cartId);
}
