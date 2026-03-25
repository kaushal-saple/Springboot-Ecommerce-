package com.ecommerce.sb_ecommerce.Service;

import com.ecommerce.sb_ecommerce.payload.CartDTO;

import java.util.List;

public interface CartService {
    CartDTO addProductToCart(Long productId, int quantity);
    List<CartDTO> getAllCarts();
    CartDTO getCart(String email, Long cartId);
    CartDTO updateProductQuantityInCart(Long productId, Integer delete);
    String deleteProductFromCart(Long cartId, Long productId);
    void updateProductInCarts(Long cartId, Long productId);

}
