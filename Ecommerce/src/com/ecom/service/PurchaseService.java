package com.ecom.service;

import java.time.LocalDate;
import java.util.Scanner;

import com.ecom.dao.CustomerDao;
import com.ecom.dao.ProductDao;
import com.ecom.dao.PurchaseDao;
import com.ecom.dao.impl.CustomerDaoImpl;
import com.ecom.dao.impl.ProductDaoImpl;
import com.ecom.dao.impl.PurchaseDaoImpl;
import com.ecom.enums.Coupon;
import com.ecom.exception.InvalidCouponException;
import com.ecom.exception.InvalidIdException;
import com.ecom.model.Customer;
import com.ecom.model.Product;
import com.ecom.model.Purchase;

public class PurchaseService {
	
	private CustomerDao customerDao = new CustomerDaoImpl();
	private ProductDao productDao = new ProductDaoImpl();
	private PurchaseDao purchaseDao = new PurchaseDaoImpl();
	
	public void insert(int customerId, int productId, Scanner sc) throws InvalidIdException, InvalidCouponException {
		Purchase purchase = new Purchase();
		Customer customer = customerDao.getById(customerId);
		purchase.setCustomer(customer);
		Product product = productDao.getById(productId);
		purchase.setProduct(product);
		purchase.setPurchaseDate(LocalDate.now());
		System.out.println("Do you have Coupon(Y/N):");
		String ans = sc.next().toUpperCase();
		if(ans.equals("Y")) {
			System.out.println("Enter Coupon code");
			String couponCode = sc.next().toUpperCase();
			try {
				Coupon coupon = Coupon.valueOf(couponCode);
				double discount = (double)coupon.getDiscount();
				System.out.println("Discount : "+discount);
				double discountedPrice = product.getPrice()-(product.getPrice()*(discount/100));
				System.out.println("After Discount Price is: "+discountedPrice);
				purchase.setCoupon(coupon);
				purchase.setAmountPaid(discountedPrice);
			} catch (IllegalArgumentException e) {
				throw new InvalidCouponException("Invalid Coupon");
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
		else {
			System.out.println("No coupon applied....");
			purchase.setAmountPaid(product.getPrice());
		}
		purchaseDao.insert(purchase);
	}
}