package com.ecommerce.clothingstore.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userId;
	private String name;
	@Column(unique = true,nullable =false)
	private String email;
	@Column(nullable =false)
	private String password;
	@Column(nullable = false)
	private String role;	
	@OneToOne(mappedBy = "user",cascade = CascadeType.ALL,orphanRemoval = true)
	private Cart cart;
	
	public static final String ROLE_USER = "ROLE_USER";
	public static final String ROLE_ADMIN = "ROLE_ADMIN";
	 
	 @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	 private List<Order> orders = new ArrayList<>();
}
