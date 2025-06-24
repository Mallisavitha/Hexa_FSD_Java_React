package com.springboot.ecom.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.springboot.ecom.model.Review;

public interface ReviewRepository extends JpaRepository<Review, Integer> {

	@Query("SELECT r FROM Review r JOIN FETCH r.customer WHERE r.product.id = :productId")
	List<Review> findReviewByProductIdWithCustomer(int productId);

}
