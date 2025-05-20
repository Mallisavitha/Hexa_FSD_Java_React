package com.ecom.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.ecom.dao.PurchaseDao;
import com.ecom.exception.InvalidCouponException;
import com.ecom.exception.InvalidIdException;
import com.ecom.model.Purchase;
import com.ecom.utility.DBUtility;

public class PurchaseDaoImpl implements PurchaseDao {
	
	DBUtility db = DBUtility.getInstance();

	@Override
	public void insert(Purchase purchase) throws InvalidIdException,InvalidCouponException{
		Connection con = db.connect();
	    String sql = "INSERT INTO purchase (id, date_of_purchase, customer_id, product_id, coupon, amount_paid) VALUES (?, ?, ?, ?, ?, ?)";
	    int id = (int) (Math.random() * 10000);

	    try {
	        PreparedStatement pstmt = con.prepareStatement(sql);
	        pstmt.setInt(1, id);
	        pstmt.setString(2, purchase.getPurchaseDate().toString());  // date_of_purchase
	        pstmt.setInt(3, purchase.getCustomer().getId());            // customer_id
	        pstmt.setInt(4, purchase.getProduct().getId());             // product_id
	        pstmt.setString(5, String.valueOf(purchase.getCoupon()));                   // coupon
	        pstmt.setDouble(6, purchase.getAmountPaid());               // amount_paid
	        pstmt.executeUpdate();
	    } catch (SQLException e) {
	        System.out.println(e.getMessage());
	    } finally {
	        db.close();
	    }

	}
}