package com.ecom.dao;

import com.ecom.exception.InvalidIdException;
import com.ecom.model.Customer;

public interface CustomerDao {
	
	void addCustomer(Customer customer)throws InvalidIdException;
	Customer getById(int id) throws InvalidIdException; 
	void deleteCustomer(int id)throws InvalidIdException;
	
}