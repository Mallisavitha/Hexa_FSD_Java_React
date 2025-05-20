package com.ecom.enums;

public enum Coupon {
	
	DIWALI(20),
	PONGAL(10),
	NEWYEAR(15);

	Coupon(int discount) {
		this.discount=discount;
	}
	
	public int getDiscount() {
		return discount;
	}

	private int discount;

}
