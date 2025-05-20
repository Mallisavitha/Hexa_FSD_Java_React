package com.ecom.controller;

import java.util.Scanner;

import com.ecom.exception.InvalidCouponException;
import com.ecom.exception.InvalidIdException;
import com.ecom.exception.InvalidInputException;
import com.ecom.model.Customer;
import com.ecom.model.Product;
import com.ecom.service.CustomerService;
import com.ecom.service.ProductService;
import com.ecom.service.PurchaseService;

public class App {
	
	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		CustomerService customerService = new CustomerService();
		ProductService productService = new ProductService();
		PurchaseService purchaseService = new PurchaseService();
		Product product = new Product();
		
		while(true) {
			System.out.println("1. Add Customer");
			System.out.println("2. Get Customer by ID");
			System.out.println("3. Delete Customer");
			System.out.println("4. Add Product");
			System.out.println("5. Get Product by Category Id");
			System.out.println("6. Add Purchase");
			System.out.println("0. Exit.....");
			int choice = sc.nextInt();
			if(choice==0) {
				System.out.println("Exiting......Thank You");
				break;
			}
			switch(choice) {
			case 1:
				sc.nextLine();
                 Customer newCustomer = new Customer();

                 System.out.println("Enter Customer Name:");
                 newCustomer.setName(sc.nextLine());

                 try {

                	 customerService.addCustomer(newCustomer);
                	 System.out.println("Customer Added");
                 }
                 catch(InvalidIdException e) {
                	 System.out.println(e.getMessage());
                 }
                 break;
                 
			case 2:
				 System.out.println("Enter Customer ID:");
                 int getId = sc.nextInt();
                 try {
                     Customer c = customerService.getById(getId);
                     System.out.println("Customer ID: " + c.getId());
                     System.out.println("Name: " + c.getName());
                 } catch (InvalidIdException e) {
                     System.out.println(e.getMessage());
                 }
                 break;
				
			case 3:
				 System.out.println("Enter Customer ID to delete:");
                 int deleteId = sc.nextInt();
                 try {
                     customerService.deleteCustomer(deleteId);
                     System.out.println("Customer Deleted");
                 } catch (InvalidIdException e) {
                     System.out.println(e.getMessage());
                 }
                 break;
                 
			case 4:
				sc.nextLine();
				System.out.println("Enter Product Name");
				product.setName(sc.nextLine());
				System.out.println("Enter Product Price");
				product.setPrice(sc.nextDouble());
				System.out.println("Enter Category ID");
				int categoryId =  sc.nextInt();
				try {
					productService.insert(product, categoryId);
					System.out.println("Product Inserted");
				} catch (InvalidInputException e) {
					System.out.println(e.getMessage());
				}
				break;
				
			case 5:
				System.out.println("Enter Category ID");
				try {
					productService.getByCategoryId(sc.nextInt()).stream().forEach(p->{
						System.out.println(p.getId() + ", "
								+ p.getName() + ", "
								+ p.getPrice() + ", "
								+ p.getCategory().getId() + ", "
								+ p.getCategory().getName());
					});
				} catch (InvalidIdException e) {
					System.out.println(e.getMessage());
				}
				break;
				
			case 6:
				System.out.println("Enter Customer ID");
				int customerId = sc.nextInt();
				System.out.println("Enter Product ID");
				int productId = sc.nextInt();
				try {
					purchaseService.insert(customerId, productId, sc);
					System.out.println("Purchase done by Customer");
				} catch (InvalidIdException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvalidCouponException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				break;
				
			default:
				System.out.println("Invalid Input");
			}
		}
	}
}