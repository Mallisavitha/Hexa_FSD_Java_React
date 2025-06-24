package com.springboot.ecom.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.ecom.model.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {



}
