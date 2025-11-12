package com.ecommerce.clothingstore.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ecommerce.clothingstore.entity.User;
import com.ecommerce.clothingstore.repository.UserRepository;

@Service
public class CustomerDetailsService implements UserDetailsService{

	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	    User user = userRepository.findByEmail(username)
	            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
     
	    return org.springframework.security.core.userdetails.User.builder()
	            .username(user.getEmail())
	            .password(user.getPassword())
	            .roles(user.getRole().replace("ROLE_", "")) // handles both ROLE_USER and USER
	            .build();
	}

}
