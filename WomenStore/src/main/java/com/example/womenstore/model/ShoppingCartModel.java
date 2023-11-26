package com.example.womenstore.model;

import javafx.beans.property.DoubleProperty;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCartModel {

    private List<Product> products; //list of ALL items
    private List<Product> basket; //list of the shopping

    public ShoppingCartModel() {
        products = new ArrayList<>();
        basket = new ArrayList<>();
        initializeProducts();
    }

    private void initializeProducts() {
        products.add(new Clothes("T-Shirt", 20.0, 2, 40));
        products.add(new Shoes("Running Shoes", 50.0, 1, 38));
        products.add(new Accessories("Watch", 30.0, 3));

    }

    public List<Product> getProducts() {
        return products;
    }

    public List<Product> getBasket() {
        return basket;
    }

    public double calculateTotalPrice() {
        double totalPrice = 0;
        for (Product product : basket) {
            totalPrice += product.getPrice();
        }
        return totalPrice;
    }

    public void addToBasket(Product product) {
        basket.add(product);
    }

    public void addToProducts(String type, String name, double price, int quantity, int size){

        if(type.equals("Shoes")){
            Product shoes = new Shoes(name,price,quantity,size);
            this.products.add(shoes);
        }
        else if(type.equals("Clothes")){
            Product clothes = new Clothes(name,price,quantity,size);
            this.products.add(clothes);
        }
        else{
            Product accessories = new Accessories(name,price,quantity);
            this.products.add(accessories);
        }

    }

    public Product checkID(int id){
        for(Product p : products){
            if(p.getId()==id){
                return p;
            }
        }
        return null;
    }

    public void removeProduct(Product p){
         products.remove(p);
    }

    public void modifyProduct(int id, double price, int quantity){
        for(Product p : products){
            if(p.getId() == id){
                if(price != 0){
                    p.setPrice(price);
                }
                if(quantity != 0){
                    p.setNbItems(quantity);
                }

            }
        }
    }

    public void applyDiscount(Product p){
        p.applyDiscount();
    }

    public  void stopDiscount(Product p){
        p.stopDiscount();
    }



}
