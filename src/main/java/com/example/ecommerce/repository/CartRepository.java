package com.example.ecommerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.ecommerce.entity.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
	@Query("SELECT c FROM Cart c WHERE c.user.email = ?1 AND c.id = ?2")
	Cart findCartByEmailAndCartId(String email, Long cartId);

	@Query("SELECT c FROM Cart c JOIN FETCH c.cartItems ci JOIN FETCH ci.product p WHERE p.id = ?1")
	List<Cart> findCartsByProductId(Long productId);

}
