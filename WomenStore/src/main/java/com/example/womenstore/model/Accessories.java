package com.example.womenstore.model;

public class Accessories extends Product{

  private double originalPrice;
  public Accessories(String name, double price, int nbItems) {
    super(name, price, nbItems);
  }

  @Override
  public String toString() {
    return "Accessories{"+ super.toString()+"}";
  }

  @Override
  public String getCategory() {
    return "Accessories";
  }

  @Override
  public void applyDiscount() {
    originalPrice = getPrice();
    System.out.println(originalPrice);
    setPrice(getPrice() * (1 - DISCOUNT_ACCESSORIES));
  }

  @Override
  public void stopDiscount() {
    setPrice(originalPrice);
    // No need to set originalPrice to 0 here
  }

}
