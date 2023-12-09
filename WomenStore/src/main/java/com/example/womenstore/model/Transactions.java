package com.example.womenstore.model;

import java.sql.Timestamp;

public class Transactions {
    private int id;
    private double total;
    private String type;

    public Transactions(int id, double total, String type) {
        this.id = id;
        this.total = total;
        this.type = type;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getType(){
        return type;
    }
    public void  setType(String t){
        type=t;
    }
}
