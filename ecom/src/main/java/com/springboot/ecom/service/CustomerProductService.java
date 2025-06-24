package com.springboot.ecom.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import com.springboot.ecom.model.Customer;
import com.springboot.ecom.model.CustomerProduct;
import com.springboot.ecom.model.Product;
import com.springboot.ecom.repository.CustomerProductRepository;
import com.springboot.ecom.repository.CustomerRepository;
import com.springboot.ecom.repository.ProductRepository;

@Service
public class CustomerProductService {

	private CustomerRepository customerRepository;
	private ProductRepository productRepository;
	private CustomerProductRepository customerProductRepository;

	public CustomerProductService(CustomerRepository customerRepository, ProductRepository productRepository,
			CustomerProductRepository customerProductRepository) {
		super();
		this.customerRepository = customerRepository;
		this.productRepository = productRepository;
		this.customerProductRepository = customerProductRepository;
	}


	public CustomerProduct purchaseProduct(String username, int productId) {
        Customer customer = customerRepository.findByUser_Username(username);
                
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        CustomerProduct cp = new CustomerProduct();
        cp.setCustomer(customer);
        cp.setProduct(product);
        cp.setQty(1);
        cp.setCoupon("NO_COUPON");
        cp.setDateOfPurchase(LocalDate.now());

        return customerProductRepository.save(cp);
    }

}
