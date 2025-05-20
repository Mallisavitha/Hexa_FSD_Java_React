package com.ecom.service;

import com.ecom.model.Customer;
import com.ecom.dao.CustomerDao;
import com.ecom.dao.impl.CustomerDaoImpl;
import com.ecom.exception.*;



public class CustomerService {
	
	private CustomerDao customerDao;
	
	public CustomerService() {
		this.customerDao=new CustomerDaoImpl();
	}
	
	public void addCustomer(Customer customer)throws InvalidIdException {
        customerDao.addCustomer(customer);
    }

	public void deleteCustomer(int id)throws InvalidIdException {
        customerDao.deleteCustomer(id);
    }
	
	  public Customer getById(int id)throws InvalidIdException {
	        return customerDao.getById(id);
	    }

}
