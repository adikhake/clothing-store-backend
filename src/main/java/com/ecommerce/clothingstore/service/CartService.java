package com.ecommerce.clothingstore.service;

import com.ecommerce.clothingstore.dto.CartDTO;
import com.ecommerce.clothingstore.entity.*;
import com.ecommerce.clothingstore.exception.ResourceNotFoundException;
import com.ecommerce.clothingstore.mapper.EntityMapper;
import com.ecommerce.clothingstore.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final EntityMapper mapper;

    public CartService(CartRepository cartRepository, 
                       CartItemRepository cartItemRepository,
                       ProductRepository productRepository,
                       UserRepository userRepository,
                       EntityMapper mapper) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
		this.mapper = mapper;
    }

    @Transactional
    public CartDTO getCartByUser(Long userId) {
        User user = userRepository.findById(userId)
           .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Cart cart = cartRepository.findByUser(user).orElseGet(() -> new Cart(user));
        cart.setTotalPrice(0.0);
           return mapper.toCartDTO(cart);
    }

    // ✅ Add item to cart
    public Cart addItemToCart(Long userId, Long productId, int quantity) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // ✅ Get existing cart or create a new one
        Cart cart = cartRepository.findByUser(user)
                .orElseGet(() -> cartRepository.save(new Cart(user)));

        // ✅ Check if item already exists in cart
        Optional<CartItem> existingItemOpt = cart.getCartItems().stream()
                .filter(i -> i.getProduct().getProductId().equals(productId))
                .findFirst();

        if (existingItemOpt.isPresent()) {
            CartItem existingItem = existingItemOpt.get();
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
            existingItem.setPrice(product.getPrice() * existingItem.getQuantity()); // ✅ update subtotal
        } else {
            CartItem newItem = new CartItem(cart, product, quantity, product.getPrice()); // ✅ subtotal
            cart.getCartItems().add(newItem);
        }

        // ✅ Recalculate total cart value
        double total = cart.getCartItems().stream()
                .mapToDouble(CartItem::getSubTotal)
                .sum();

        cart.setTotalPrice(total);
        cart.setUpdatedAt(LocalDateTime.now());

        // ✅ Save both cart and items (cascade = ALL handles items)
        return cartRepository.save(cart);
    }


     
    private Cart updateCartTotal(Cart cart) {
        double total = cart.getCartItems().stream()
            .mapToDouble(ci -> ci.getPrice() * ci.getQuantity())
            .sum();
        cart.setTotalPrice(total);
        cart.setUpdatedAt(LocalDateTime.now());
      return  cartRepository.save(cart);
    }
  
        @Transactional
    public CartDTO removeItemFromCart(Long userId, Long productId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        CartItem itemToRemove = cart.getCartItems().stream()
                .filter(i -> i.getProduct().getProductId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Item not found in cart"));

        // ✅ Safely remove the item from both sides
        cart.getCartItems().remove(itemToRemove);
        itemToRemove.setCart(null);

        // ✅ Explicitly delete the item (avoid Hibernate confusion)
        cartItemRepository.delete(itemToRemove);

        // ✅ Recalculate total
        Cart savedCart = updateCartTotal(cart);

        return mapper.toCartDTO(savedCart);
    }
    
        
    public void clearCart(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
        cart.getCartItems().clear();
        cart.setTotalPrice(0.0);
        cart.setUpdatedAt(LocalDateTime.now());
        cartRepository.save(cart);
    }

	public Cart updateCartItemQuantity(Long userId, Long productId, int quantity) {
		User user = userRepository.findById(userId)
	            .orElseThrow(() -> new RuntimeException("User not found"));

	    Cart cart = cartRepository.findByUser(user)
	            .orElseThrow(() -> new RuntimeException("Cart not found"));

	    CartItem item = cart.getCartItems().stream()
	            .filter(i -> i.getProduct().getProductId().equals(productId))
	            .findFirst()
	            .orElseThrow(() -> new RuntimeException("Item not found in cart"));

	    if (quantity <= 0) {
	        cart.getCartItems().remove(item);
	    } else {
	        item.setQuantity(quantity);
	        item.setPrice(item.getProduct().getPrice() * quantity);
	    }
	    double total = cart.getCartItems().stream()
	            .mapToDouble(i -> i.getPrice())
	            .sum();
	    cart.setTotalPrice(total);

	    cart.setUpdatedAt(LocalDateTime.now());
		return cartRepository.save(cart);
	}

}
