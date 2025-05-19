package com.ecom.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ecom.dao.ProductDao;
import com.ecom.exception.InvalidIdException;
import com.ecom.exception.InvalidInputException;
import com.ecom.model.Category;
import com.ecom.model.Product;
import com.ecom.utility.DBUtility;

public class ProductDaoImpl implements ProductDao {

    DBUtility db = DBUtility.getInstance();

    @Override
    public Product getById(int id) throws InvalidIdException {
        Product product = null;
        Connection con = db.connect();
        String sql = "SELECT * FROM product WHERE id=?";
        try {
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, id);
            ResultSet rst = pstmt.executeQuery();
            while (rst.next()) {
                product = new Product();
                product.setId(rst.getInt("id"));
                product.setName(rst.getString("name"));
                product.setPrice(rst.getDouble("price"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        db.close();
        return product;
    }

    @Override
    public void insert(Product product, int categoryId) throws InvalidInputException {
        Connection con = db.connect();
        String sql = "INSERT INTO product (name, price, category_id) VALUES (?, ?, ?)";
        try {
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, product.getName());
            pstmt.setDouble(2, product.getPrice());
            pstmt.setInt(3, categoryId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        db.close();
    }

    @Override
    public List<Product> getByCategoryId(int categoryId) throws InvalidIdException {
        Connection con = db.connect();
        String sql = "SELECT p.id AS pid, p.name AS pname, p.price, c.id AS cid, c.name AS cname " +
                     "FROM product p JOIN category c ON p.category_id = c.id WHERE p.category_id = ?";
        List<Product> list = new ArrayList<>();
        try {
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, categoryId);
            ResultSet rst = pstmt.executeQuery();
            while (rst.next()) {
                Product product = new Product();
                product.setId(rst.getInt("pid"));
                product.setName(rst.getString("pname"));
                product.setPrice(rst.getDouble("price"));

                Category category = new Category();
                category.setId(rst.getInt("cid"));
                category.setName(rst.getString("cname"));

                product.setCategory(category);
                list.add(product);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        db.close();
        return list;
    }
}
