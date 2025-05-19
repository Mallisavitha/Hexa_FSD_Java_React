package com.ecom.service;

import com.ecom.dao.impl.ProductDaoImpl;
import com.ecom.exception.InvalidIdException;
import com.ecom.exception.InvalidInputException;
import com.ecom.model.Product;

import java.util.List;

import com.ecom.dao.ProductDao;

public class ProductService {
	
	private ProductDao productDao = new ProductDaoImpl();
	
	public void insert(Product product,int categoryId) throws InvalidInputException {
		if(product.getName()==null||product.getName().equals("null"))
			throw new InvalidInputException("Invalid name is given");
		if(product.getPrice()==0)
			throw new InvalidInputException("Invalid price is given");
		productDao.insert(product,categoryId);
	}
	
	public List<Product> getByCategoryId(int categoryId) throws InvalidIdException {
		return productDao.getByCategoryId(categoryId);
	}
	
}