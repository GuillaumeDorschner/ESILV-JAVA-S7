package com.example.womenstore.model;

import java.sql.Timestamp;

public class Transactions {
    private int id;
    private Timestamp time;
    private double total;

    public Transactions(int id, Timestamp time, double total) {
        this.id = id;
        this.time = time;
        this.total = total;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
