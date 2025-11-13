package com.ecommerce.clothingstore.controller;

import com.ecommerce.clothingstore.dto.CartDTO;
import com.ecommerce.clothingstore.entity.Cart;
import com.ecommerce.clothingstore.entity.User;
import com.ecommerce.clothingstore.mapper.EntityMapper;
import com.ecommerce.clothingstore.payload.ApiResponse;
import com.ecommerce.clothingstore.repository.UserRepository;
import com.ecommerce.clothingstore.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final EntityMapper entityMapper;
    private final UserRepository userRepository;
 
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    
    @PostMapping("/add/{productId}")
    public ResponseEntity<ApiResponse<CartDTO>> addToCart(
            @PathVariable Long productId,
            @RequestParam int quantity) {
            	
    	User user = getCurrentUser();
    	Cart cart = cartService.addItemToCart(user.getUserId(), productId, quantity);
        CartDTO dto = entityMapper.toCartDTO(cart);
        
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Item added to cart successfully", dto)
        );
    }
    
    
    @PutMapping("/update/{productId}")
    public ResponseEntity<ApiResponse<CartDTO>> updateItemQuantity(
            @PathVariable Long productId,
            @RequestParam int quantity) {

        User user = getCurrentUser();
        Cart cart = cartService.updateCartItemQuantity(user.getUserId(), productId, quantity);
        CartDTO dto = entityMapper.toCartDTO(cart);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Cart item updated successfully", dto)
        );
    }

    // 🧾 View user's cart
    @GetMapping
    public ResponseEntity<ApiResponse<CartDTO>> getUserCart() {
    	User user = getCurrentUser(); 
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Cart fetched successfully", cartService.getCartByUser(user.getUserId()))
        );
    }

    // ❌ Remove item from cart
    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<ApiResponse<CartDTO>> removeItem(
            @PathVariable Long productId) {
    	
 	    User user = getCurrentUser();  
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Item removed from cart successfully",  cartService.removeItemFromCart(user.getUserId(), productId))
        );
    }

    // 🧹 Clear cart
    @DeleteMapping("/clear")
    public ResponseEntity<ApiResponse<String>> clearCart() {
    	User user = getCurrentUser();
        cartService.clearCart(user.getUserId());
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Cart cleared successfully", "Cart is now empty")
        );
    }
}
