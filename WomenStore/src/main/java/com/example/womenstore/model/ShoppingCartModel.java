package com.example.womenstore.model;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCartModel {

    private List<Product> products; //list of ALL items
    private List<Product> basket; //list of your shopping

    public ShoppingCartModel() {
        products = new ArrayList<>();
        basket = new ArrayList<>();
        initializeProducts();
    }

    private void initializeProducts() {
        products.add(new Clothes("T-Shirt", 20.0, 2, 40));
        products.add(new Shoes("Running Shoes", 50.0, 1, 38));
        products.add(new Accessories("Watch", 30.0, 3));
        // Add more products as needed
    }

    public List<Product> getProducts() {
        return products;
    }

    public List<Product> getBasket() {
        return basket;
    }

    public void addToBasket(Product product) {
        basket.add(product);
    }

    public double calculateTotalPrice() {
        double totalPrice = 0;
        for (Product product : basket) {
            totalPrice += product.getPrice();
        }
        return totalPrice;
    }

}
