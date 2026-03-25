package com.ecommerce.sb_ecommerce.Service;

import com.ecommerce.sb_ecommerce.exception.ApiException;
import com.ecommerce.sb_ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.sb_ecommerce.model.Cart;
import com.ecommerce.sb_ecommerce.model.CartItem;
import com.ecommerce.sb_ecommerce.model.Product;
import com.ecommerce.sb_ecommerce.payload.APIResponse;
import com.ecommerce.sb_ecommerce.payload.CartDTO;
import com.ecommerce.sb_ecommerce.payload.ProductDTO;
import com.ecommerce.sb_ecommerce.respository.CartItemRepository;
import com.ecommerce.sb_ecommerce.respository.CartRepository;
import com.ecommerce.sb_ecommerce.respository.ProductRepository;
import com.ecommerce.sb_ecommerce.util.AuthUtil;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
public class CartServiceImpl implements CartService{

    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private AuthUtil authUtil;

    @Override
    public CartDTO addProductToCart(Long productId, int quantity) {
        //check cart exist or create new cart
        Cart cart = createCart();

        //get product details
        Product productDetail = productRepository.findById(productId)
                .orElseThrow(()->new ResourceNotFoundException("Product" , "productId", productId));

        //validate product
        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(productDetail.getProductId(), cart.getCartId());

        if(cartItem!=null){
            throw new ApiException("Product " + productDetail.getProductName() + " already exist in cart");
        }

        if(productDetail.getQuantity()==0){
            throw new ApiException( productDetail.getProductName() + " is not available");
        }

        if(productDetail.getQuantity()<quantity){
            throw new ApiException("Please,make an order of the " + productDetail.getProductName() + " less than or equal to quantity " + productDetail.getQuantity());
        }

        //create cartItem
        CartItem newCartItem = new CartItem();
        newCartItem.setProduct(productDetail);
        newCartItem.setQuantity(quantity);
        newCartItem.setCart(cart);
        newCartItem.setDiscount(productDetail.getDiscount());
        newCartItem.setProductPrice(productDetail.getSpecialPrice());

        //save cartItem
        CartItem savedCartItem = cartItemRepository.save(newCartItem);
        productDetail.setQuantity(productDetail.getQuantity());
        cart.getCartItems().add(savedCartItem);

        cart.setTotalPrice(cart.getTotalPrice() + (savedCartItem.getProductPrice() * savedCartItem.getQuantity()));
        cartRepository.save(cart);

        CartDTO cartDTO = modelMapper.map(cart,CartDTO.class);

        List<CartItem> cartItems = cart.getCartItems();

        Stream<ProductDTO> productDTOStream = cartItems.stream().map(item->{
                    ProductDTO map = modelMapper.map(item.getProduct(),ProductDTO.class);
                    map.setQuantity(item.getQuantity());
                    return map;
                });

        //return updated cart
        cartDTO.setProducts(productDTOStream.toList());
        return cartDTO;
    }

    @Override
    public List<CartDTO> getAllCarts() {
        List<Cart> carts = cartRepository.findAll();
        if(carts.isEmpty()){
            throw new ApiException("No Cart exist");
        }

        List<CartDTO> cartDTOs = carts.stream().map(cart -> {
            CartDTO cartDTO = modelMapper.map(cart,CartDTO.class);
            List<ProductDTO> productDTOS = cart.getCartItems().stream()
                            .map(items ->{
                                ProductDTO productDTO = modelMapper.map(items.getProduct(),ProductDTO.class);
                                productDTO.setQuantity(items.getQuantity());
                                return productDTO;
                            }).toList();
            cartDTO.setProducts(productDTOS);

            return cartDTO;
        }).toList();
        return cartDTOs;
    }

    @Override
    public CartDTO getCart(String email, Long cartId) {
        Cart cart = cartRepository.findCartByUserEmailAndCartId(email,cartId);
        if(cart == null){
            throw new ResourceNotFoundException("Cart" , "cartId", cartId);
        }

        cart.getCartItems().forEach(item -> item.getProduct().setQuantity(item.getQuantity()));

        CartDTO cartDTO = modelMapper.map(cart,CartDTO.class);
        List<ProductDTO> productDTOS = cart.getCartItems().stream()
                .map(item -> modelMapper.map(item.getProduct(),ProductDTO.class)).toList();
        cartDTO.setProducts(productDTOS);
        return cartDTO;
    }

    @Override
    @Transactional
    public CartDTO updateProductQuantityInCart(Long productId, Integer quantity) {
        String emailId = authUtil.loggedInEmail();
        Cart userCart = cartRepository.findCartByEmail(emailId);
        if(userCart == null){
            throw new ResourceNotFoundException("Cart" , "email", emailId);
        }
        Long cartId  = userCart.getCartId();

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        if (product.getQuantity() == 0) {
            throw new ApiException(product.getProductName() + " is not available");
        }

        if (product.getQuantity() < quantity) {
            throw new ApiException("Please, make an order of the " + product.getProductName()
                    + " less than or equal to the quantity " + product.getQuantity() + ".");
        }

        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(productId, cartId);
        if(cartItem == null){
            throw new ApiException("Product " + product.getProductName() + " not available in the cart!!!");
        }

        int newQuantity = cartItem.getQuantity() + quantity;

        if (newQuantity < 0) {
            throw new ApiException("The resulting quantity cannot be negative.");
        }

        if(newQuantity == 0){
            deleteProductFromCart(cartId, productId);
            cart = cartRepository.findById(cartId)
                    .orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));
        } else {
            cartItem.setProductPrice(product.getSpecialPrice());
            cartItem.setQuantity(newQuantity);
            cartItem.setDiscount(product.getDiscount());
            cart.setTotalPrice(cart.getTotalPrice() + (product.getSpecialPrice() * quantity));
            cartRepository.save(cart);
            cartItemRepository.save(cartItem);
        }

        CartDTO cartDTO = modelMapper.map(cart,CartDTO.class);

        List<CartItem> cartItems = cart.getCartItems();
        Stream<ProductDTO> productDTOStream = cartItems.stream()
                .map(item->{
                    ProductDTO productDTO = modelMapper.map(item.getProduct(),ProductDTO.class);
                    productDTO.setQuantity(item.getQuantity());
                    return productDTO;
                });

        cartDTO.setProducts(productDTOStream.toList());
        return cartDTO;
    }

    @Override
    @Transactional
    public String deleteProductFromCart(Long cartId, Long productId) {

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(()->new ResourceNotFoundException("Cart" , "cartId", cartId));

        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(productId, cart.getCartId());
        if(cartItem==null){
            throw new ApiException("Product " + productId + " does not exist in cart");
        }

        cart.setTotalPrice(cart.getTotalPrice() - (cartItem.getProductPrice() * cartItem.getQuantity()));

        cartItemRepository.deleteByProductProductIdAndCartCartId(productId,cartId);

        return "product " + cartItem.getProduct().getProductName() + " has been deleted";
    }

    @Override
    public void updateProductInCarts(Long cartId, Long productId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cartId, productId);

        if (cartItem == null) {
            throw new ApiException("Product " + product.getProductName() + " not available in the cart!!!");
        }

        double cartPrice = cart.getTotalPrice()
                - (cartItem.getProductPrice() * cartItem.getQuantity());

        cartItem.setProductPrice(product.getSpecialPrice());

        cart.setTotalPrice(cartPrice
                + (cartItem.getProductPrice() * cartItem.getQuantity()));

        cartItem = cartItemRepository.save(cartItem);
    }

    private Cart createCart(){
        Cart userCart = cartRepository.findCartByEmail(authUtil.loggedInEmail());
        if(userCart!=null){
            return userCart;
        }else{
            Cart cart = new Cart();
            cart.setTotalPrice(0.0);
            cart.setUser(authUtil.loggedInUser());
            return cartRepository.save(cart);

        }
    }
}
