package com.springboot.ecom.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.springboot.ecom.exception.AccessDeniedException;
import com.springboot.ecom.model.Customer;
import com.springboot.ecom.model.Product;
import com.springboot.ecom.model.Review;
import com.springboot.ecom.repository.CustomerProductRepository;
import com.springboot.ecom.repository.CustomerRepository;
import com.springboot.ecom.repository.ReviewRepository;

@Service
public class ReviewService {
	
	private ReviewRepository reviewRepository;
	private CustomerProductRepository customerProductRepository;
	private CustomerRepository customerRepository;
	
	public ReviewService(ReviewRepository reviewRepository, CustomerProductRepository customerProductRepository,
			CustomerRepository customerRepository) {
		super();
		this.reviewRepository = reviewRepository;
		this.customerProductRepository = customerProductRepository;
		this.customerRepository = customerRepository;
	}


	public Review addReview(String username, int productId, Review review) {

		if (!customerProductRepository.hasPurchasedProduct(username, productId)) {
            throw new AccessDeniedException("You must purchase the product to add a review.");
        }
		
		Customer customer=customerRepository.findByUser_Username(username);
		review.setCustomer(customer);
		
		Product product = new Product();
        product.setId(productId);
        review.setProduct(product);

        return reviewRepository.save(review);
	}


	public List<Review> getReviewByProduct(int productId) {
		return reviewRepository.findReviewByProductIdWithCustomer(productId);
	}
	


}
