package com.example.womenstore.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public abstract class Product implements Discount, Comparable<Product> {

  private int id;
  private String name;
  private final DoubleProperty price;
  private int nbItems;

  private static int nb = 0;
  private static double income = 0;

  public Product(String name, double price, int nbItems) {
    this.id = ++nb;
    this.name = name;
    this.price = new SimpleDoubleProperty(price);
    setPrice(price);
    this.nbItems = nbItems;
  }

  public Product(int id, String name, double price, int nbItems) {
    this.id = id;
    this.name = name;
    this.price = new SimpleDoubleProperty(price);
    setPrice(price);
    this.nbItems = nbItems;
  }

  public static double getIncome() {
    return income;
  }

  public void sell(int soldItems) throws IllegalArgumentException {
    if (soldItems <= this.nbItems) {
      this.setNbItems(this.nbItems - soldItems);
      income += soldItems * price.get();
      System.out.println("Sell OK");
    } else {
      throw new IllegalArgumentException("Unavailable product");
    }
  }

  public void purchase(int purchasedItems) throws IllegalArgumentException {
    if (purchasedItems > 0) {
      this.setNbItems(this.nbItems + purchasedItems);
      System.out.println("Purchase OK !");
    } else {
      throw new IllegalArgumentException("Purchase with negative number !!");
    }
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public DoubleProperty priceProperty() {
    return price;
  }

  public double getPrice() {
    return price.get();
  }

  public void setPrice(double price) throws IllegalArgumentException {
    if (price >= 0) {
      this.price.set(price);
    } else {
      throw new IllegalArgumentException("Price is negative");
    }
  }

  public int getNbItems() {
    return nbItems;
  }

  public void setNbItems(int nbItems) {
    this.nbItems = nbItems;
  }

  @Override
  public String toString() {
    return "Product{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", price=" + price +
            ", nbItems=" + nbItems +
            '}';
  }

  public abstract String getCategory();

  public abstract boolean isDiscountApplied();

  @Override
  public int compareTo(Product o) {
    return Double.compare(this.getPrice(), o.getPrice());
  }
}
