package com.example.womenstore.model;

public class Transactions {
    private Product product;
    private String type;

    public Transactions(Product p, String type) {
        this.product=p;
        this.type = type;
    }

    // Getters and setters
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getType(){
        return type;
    }

    public void  setType(String t){
        type=t;
    }


}
