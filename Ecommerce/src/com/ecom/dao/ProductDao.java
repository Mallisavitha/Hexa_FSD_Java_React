package com.ecom.dao;

import java.util.List;

import com.ecom.exception.InvalidIdException;
import com.ecom.exception.InvalidInputException;
import com.ecom.model.Product;

public interface ProductDao {
	
	Product getById(int id) throws InvalidIdException;
	void insert(Product product,int categoryId) throws InvalidInputException;
	List<Product> getByCategoryId(int categoryId) throws InvalidIdException;
	
}