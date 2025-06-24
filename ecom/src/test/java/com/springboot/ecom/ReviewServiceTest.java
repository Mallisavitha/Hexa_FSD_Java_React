package com.springboot.ecom;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.springboot.ecom.model.Customer;
import com.springboot.ecom.model.Product;
import com.springboot.ecom.model.Review;
import com.springboot.ecom.repository.CustomerProductRepository;
import com.springboot.ecom.repository.CustomerRepository;
import com.springboot.ecom.repository.ReviewRepository;
import com.springboot.ecom.service.ReviewService;

@SpringBootTest
public class ReviewServiceTest {

	@InjectMocks
	private ReviewService reviewService;

	@Mock
	private ReviewRepository reviewRepository;

	@Mock
	private CustomerProductRepository customerProductRepository;

	@Mock
	private CustomerRepository customerRepository;

	private Customer customer;
	private Product product;
	private Review review;

	@BeforeEach
	public void init() {
		customer = new Customer();
		customer.setId(1);
		customer.setName("Test Customer");

		product = new Product();
		product.setId(1);
		product.setTitle("Test Product");

		review = new Review();
		review.setComment("Excellent");
		review.setRate(5);
	}

	@Test
	public void testAddReview_ValidPurchase() {
		String username = "testuser";
		int productId = 1;

		// Mocks
		when(customerProductRepository.hasPurchasedProduct(username, productId)).thenReturn(true);
		when(customerRepository.findByUser_Username(username)).thenReturn(customer);
		when(reviewRepository.save(any(Review.class))).thenAnswer(invocation -> invocation.getArgument(0));

		// Act
		Review result = reviewService.addReview(username, productId, review);

		// Assert
		assertEquals("Excellent", result.getComment());
		assertEquals(5, result.getRate());
		assertEquals(customer, result.getCustomer());
		assertEquals(productId, result.getProduct().getId());
	}
}
