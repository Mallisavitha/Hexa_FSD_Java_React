package com.ecom.model;

import java.time.LocalDate;

import com.ecom.enums.Coupon;

public class Purchase {
	
	private int id;
	private LocalDate purchaseDate;
	private double amountPaid;
	private Coupon coupon;
	
	private Customer customer;
	private Product product;
	
	public Purchase() {	}

	public Purchase(int id, LocalDate purchaseDate, double amountPaid, Coupon coupon, Customer customer,
			Product product) {
		super();
		this.id = id;
		this.purchaseDate = purchaseDate;
		this.amountPaid = amountPaid;
		this.coupon = coupon;
		this.customer = customer;
		this.product = product;
	}

	public int getId() { return id; }
	public void setId(int id) { this.id = id; }
	public LocalDate getPurchaseDate() { return purchaseDate; }
	public void setPurchaseDate(LocalDate purchaseDate) { this.purchaseDate = purchaseDate; }
	public double getAmountPaid() { return amountPaid; }
	public void setAmountPaid(double amountPaid) { this.amountPaid = amountPaid; }
	public Coupon getCoupon() { return coupon; }
	public void setCoupon(Coupon coupon) { this.coupon = coupon; }
	public Customer getCustomer() { return customer; }
	public void setCustomer(Customer customer) { this.customer = customer; }
	public Product getProduct() { return product; }
	public void setProduct(Product product) { this.product = product; }

	@Override
	public String toString() {
		return "Purchase [id=" + id + ", purchaseDate=" + purchaseDate + ", amountPaid=" + amountPaid + ", coupon="
				+ coupon + ", customer=" + customer + ", product=" + product + "]";
	}
			
}