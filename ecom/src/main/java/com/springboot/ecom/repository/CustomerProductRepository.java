package com.springboot.ecom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.springboot.ecom.model.CustomerProduct;

public interface CustomerProductRepository extends JpaRepository<CustomerProduct, Integer>{

	@Query("SELECT COUNT(cp) > 0 FROM CustomerProduct cp WHERE cp.customer.user.username = :username AND cp.product.id = :productId")
	boolean hasPurchasedProduct(String username, int productId);

}
