package com.example.womenstore.model;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ShoppingCartModel {

    private ObservableList<Product> products; // list of ALL items
    private ObservableList<Product> basket; // list of the shopping
    private final DoubleProperty totalIncomeProperty = new SimpleDoubleProperty();
    private final DoubleProperty totalOutcomeProperty = new SimpleDoubleProperty();

    public ShoppingCartModel() {
        products = FXCollections.observableArrayList();
        basket = FXCollections.observableArrayList();
        initializeProducts();
    }

    private void initializeProducts() {
        products.addAll(new Clothes("T-Shirt", 20.0, 2, 40),
                new Shoes("Running Shoes", 50.0, 1, 38),
                new Accessories("Watch", 30.0, 3));
    }

    public ObservableList<Product> getProducts() {
        return products;
    }

    public ObservableList<Product> getBasket() {
        return basket;
    }

    public double getTotalIncome() {
        return totalIncomeProperty.get();
    }

    public DoubleProperty totalIncomeProperty() {
        return totalIncomeProperty;
    }

    public double getTotalOutcome() {
        return totalOutcomeProperty.get();
    }

    public DoubleProperty totalOutcomeProperty() {
        return totalOutcomeProperty;
    }

    public double calculateTotalPrice() {
        double totalPrice = 0;
        for (Product product : basket) {
            totalPrice += product.getPrice();
        }
        return totalPrice;
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

    public void removeProduct(Product p) {
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

    public void applyDiscount(Product p) {
        p.applyDiscount();
    }

    public void stopDiscount(Product p) {
        p.stopDiscount();
    }


    /********Buying and selling =>basket list********/

    public void buying(String type, String name, double price, int quantity, int size){
        if(type.equals("Shoes")){
            Product shoes = new Shoes(name,price,quantity,size);
            this.basket.add(shoes);
        }
        else if(type.equals("Clothes")){
            Product clothes = new Clothes(name,price,quantity,size);
            this.basket.add(clothes);
        }
        else{
            Product accessories = new Accessories(name,price,quantity);
            this.basket.add(accessories);
        }

    }
}
