package com.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.ecom.exception.InvalidIdException;
import com.ecom.exception.InvalidInputException;
import com.ecom.model.Category;
import com.ecom.model.Product;
import com.ecom.service.ProductService;

public class ProductServiceTest {
	
	ProductService productService;
	Product p1,p2,p3;
	List<Product> list;
	
	@BeforeEach
	public void init() {
		productService=new ProductService();
		p1=new Product(1,"Redmi",8500.0,new Category(1,"Mobiles"));
		p2=new Product(2,"Asus",85000.0,new Category(2,"Laptops"));
		p3=new Product(3,"Samsung",12000.0,new Category(3,"Mobiles"));
		
		list=Arrays.asList(p1,p2,p3);
	}
	
	@Test
	public void insertProductTest() {
		 // Use Case 1: Valid product
        assertDoesNotThrow(() -> {
            productService.insert(p1, p1.getCategory().getId());
        });
		
        // Use Case 2: Invalid name - null
        InvalidInputException e = assertThrows(InvalidInputException.class, () -> {
            Product p = new Product(4, null, 1500.0, new Category(1, "Mobiles"));
            productService.insert(p, 1);
        });
        assertEquals("invalid name is given", e.getMessage().toLowerCase());
        
     // Use Case 3: Invalid price - 0.0
        e = assertThrows(InvalidInputException.class, () -> {
            Product p = new Product(6, "HP", 0.0, new Category(2, "Laptops"));
            productService.insert(p, 2);
        });
        assertEquals("invalid price is given", e.getMessage().toLowerCase());
	}
	
	 @Test
	    public void getByCategoryIdTest() {
	        // Use Case 1: Valid category ID
	        assertDoesNotThrow(() -> {
	            productService.getByCategoryId(1); // for Mobiles
	        });

	        // Use Case 2: Invalid category ID
	        InvalidIdException e = assertThrows(InvalidIdException.class, () -> {
	            productService.getByCategoryId(-99);
	        });
	        assertEquals("invalid category id", e.getMessage().toLowerCase());
	 }
	

}
