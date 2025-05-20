package com.ecom.model;

public class Product {
	
	private int id;
	private String name;
	private double price;
	
	private Category category;

	public Product() {	}

	public Product(int id, String name, double price, Category category) {
		super();
		this.id = id;
		this.name = name;
		this.price = price;
		this.category = category;
	}

	public int getId() {return id;}
	public void setId(int id) { this.id = id; }
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	public double getPrice() { return price; }
	public void setPrice(double price) { this.price = price; }
	public Category getCategory() { return category; }
	public void setCategory(Category category) { this.category = category; }

	@Override
	public String toString() {
		return "purchase [id=" + id + ", name=" + name + ", price=" + price + ", category=" + category + "]";
	}
	
}